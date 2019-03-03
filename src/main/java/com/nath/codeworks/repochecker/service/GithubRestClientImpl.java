package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.interceptors.RestTemplateErrorHandler;
import com.nath.codeworks.repochecker.model.dto.GithubRepo;
import com.nath.codeworks.repochecker.model.dto.GithubResultList;
import com.nath.codeworks.repochecker.model.ghresponse.Repository;
import com.nath.codeworks.repochecker.exception.ServiceInvokerException;
import com.nath.codeworks.repochecker.model.ghresponse.User;
import com.nath.codeworks.repochecker.util.RepoCheckerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Default implementation of GithubRestClient. This will invoke APIs on Github
 * and return responses in the format that is useful for the application.
 * <p>
 * When the application requires a different implementation, new implementation can
 * be added without much changes.
 */
@Service
public class GithubRestClientImpl implements GithubRestClient {

    private Logger LOGGER = LoggerFactory.getLogger(GithubRestClientImpl.class);

    private final RestTemplate restTemplate;
    private GithubRestClientProperties properties;

    @Autowired
    public GithubRestClientImpl(RestTemplateBuilder restTemplateBuilder,
                                ClientHttpRequestInterceptor requestInterceptor,
                                RestTemplateErrorHandler restTemplateErrorHandler,
                                GithubRestClientProperties githubRestClientProperties) {
        restTemplate = restTemplateBuilder.errorHandler(restTemplateErrorHandler).build();
        restTemplate.getInterceptors().add(requestInterceptor);
        properties = githubRestClientProperties;
    }

    private static final String AMP_STR = "&";
    private static final String QM_STR  = "?";
    private static final String LINK_STR = "Link";
    private static final String PER_PAGE_QP = "per_page=";


    /**
     * @param username
     * @return
     * @throws ServiceInvokerException (Runtime Exception)
     * @throws RepoCheckerAppException <p>Provided username as the input, this service would fetch User info by invoking
     *                                 Github user API. If there is no such user exists, or if the service invocation to
     *                                 Github API fails, this service would throw ServiceInvokerException runtime exception.</p>
     */
    @Override
    public User getGithubUserInfo(String username) throws ServiceInvokerException, RepoCheckerAppException {
        if (StringUtils.isBlank(username)) {
            LOGGER.error("Username cannot be empty to fetch user info from Github.");
            /* as username is an input that is provided by the user,
             validation error can be directly communicated to user */
            throw new RepoCheckerAppException("Username cannot be empty to fetch user info from Github.");
        }

        User user = null;
        try {
            user = restTemplate.getForObject(properties.getUserUrl(), User.class, username);
        } catch (ServiceInvokerException siEx) {
            LOGGER.error("ServiceInvokerException occurred while getting Github User info. {}", siEx.getMessage());
            throw siEx;
        } catch (Exception ex) {
            LOGGER.error("Application error occurred while getting Github User info.", ex);
            throw new RepoCheckerAppException("Application error occurred while getting Github User info.");
        }
        return user;
    }

    /**
     * @param repositoriesApiUrl
     * @return
     * @throws ServiceInvokerException (Runtime Exception)
     * @throws RepoCheckerAppException <p>Provided repositoriesApiUrl as the input, this service would fetch Git Repositories details
     *                                 by invoking Github API. If the service invocation to Github API fails, this service would throw
     *                                 ServiceInvokerException runtime exception.</p>
     */
    @Override
    public GithubResultList<Repository> getGithubRepositories(String repositoriesApiUrl) throws ServiceInvokerException,
            RepoCheckerAppException {
        try {
            StringBuilder sb = new StringBuilder(repositoriesApiUrl);
            if (repositoriesApiUrl.contains(QM_STR)){
                sb.append(AMP_STR);
            }else{
                sb.append(QM_STR);
            }
            repositoriesApiUrl = sb.toString();
            sb.append(PER_PAGE_QP).append(properties.getResultsPerPage());
            ResponseEntity<List<Repository>> response = restTemplate.exchange(
                    repositoriesApiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Repository>>() {});

            List<Repository> listOfRepositories = response.getBody();
            String nexPagetUrl = null;

            String linkHeader = response.getHeaders().get(LINK_STR).get(0);

            if (StringUtils.isNoneBlank(linkHeader)) {
                nexPagetUrl = RepoCheckerUtils.parseNextUrl(linkHeader);
            }

            GithubResultList<Repository> githubRepoResultList = new GithubResultList<>(listOfRepositories, nexPagetUrl);

            return githubRepoResultList;

        } catch (ServiceInvokerException siEx) {
            LOGGER.error("ServiceInvokerException occurred while getting Github Repositories for User. {}", siEx.getMessage());
            throw siEx;

        } catch (Exception ex) {
            LOGGER.error("Application error occurred while getting Github Repositories for User.", ex);
            throw new RepoCheckerAppException("Application error occurred while getting Github Repositories for User.");
        }
    }


    public List<GithubRepo> getRepositoryContributors(List<Repository> listOfRepositories)
            throws ServiceInvokerException, RepoCheckerAppException {
        try {
            /* create a thread pool of the size of the list of repositories. If the size of
             list of repositories is bigger than the maximum number of threads configured
             to used, then create the thread pool only for the size of configured amount */
            int numberOfThreads = listOfRepositories.size() <= properties.getMaxNumOfThreads() ?
                    listOfRepositories.size() : properties.getMaxNumOfThreads();
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

            List<Future<GithubRepo>> gitHubRepoFutures = new ArrayList<>(listOfRepositories.size());
            for (Repository repository : listOfRepositories) {
                StringBuilder sb = new StringBuilder(repository.getContributors_url());
                sb.append(QM_STR).append(PER_PAGE_QP).append(properties.getResultsPerPage());
                String contributorUrl = sb.toString();
                gitHubRepoFutures.add(executorService.submit(new AysncRepoDetailsChecker(restTemplate,
                        contributorUrl, repository.getName())));
            }
            List<GithubRepo> listOfGitHubRepo = new ArrayList<>(listOfRepositories.size());
            for (Future<GithubRepo> githubRepoFuture : gitHubRepoFutures) {
                listOfGitHubRepo.add(githubRepoFuture.get());
            }

            // shutdown executor service after executing
            executorService.shutdown();

            return listOfGitHubRepo;

        } catch (ServiceInvokerException siEx) {
            LOGGER.error("ServiceInvokerException occurred while getting Github Repository contributors. {}", siEx.getMessage());
            throw siEx;
        } catch (Exception ex) {
            LOGGER.error("Application error occurred while getting Github Repository contributors.", ex);
            throw new RepoCheckerAppException("Application error occurred while getting Github Repository contributors.");
        }
    }


}
