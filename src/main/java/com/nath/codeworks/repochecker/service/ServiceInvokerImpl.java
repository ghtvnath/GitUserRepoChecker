package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.exception.ServiceInvokerException;
import com.nath.codeworks.repochecker.model.dto.GithubRepo;
import com.nath.codeworks.repochecker.model.dto.GithubResultList;
import com.nath.codeworks.repochecker.model.ghresponse.Repository;
import com.nath.codeworks.repochecker.model.ghresponse.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceInvokerImpl implements ServiceInvoker{

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubRestClientImpl.class);

    @Autowired
    GithubRestClient githubRestClient;

    @Autowired
    InformationOutputService outputService;

    @Override
    public void displayUserGitRepoInformation(String username)  {
        try {
            LOGGER.debug("Getting Github user information for username {}", username);
            User user = githubRestClient.getGithubUserInfo(username);

            LOGGER.debug("Displaying user information");
            outputService.displayUserInformation(System.out, user);

            String repositoriesUrl = user.getRepos_url();
            LOGGER.debug("Initial Github repositories url for {} - {}", username, repositoriesUrl);

            /* in case information are paginated, the method is running in a loop
            to call the next url for continuously getting repository information.
            This can be later changed even to get the user consent to display more.
            But as in the first iteration, even the username is taken as a command line
            argument, all the information will be displayed. */

            boolean isInitialRepoApiInvocation = true;

            LOGGER.debug("Displaying Repository information");
            while (repositoriesUrl != null ) {
                GithubResultList<Repository> githubRepoResult = githubRestClient.getGithubRepositories(repositoriesUrl);

                if (isInitialRepoApiInvocation && githubRepoResult.isEmpty()) {
                    System.out.println("**** No repositories found for user ****");
                } else{
                    isInitialRepoApiInvocation = false;
                    List<GithubRepo> githubRepoList = githubRestClient.getRepositoryContributors(githubRepoResult.getResultsList());

                    outputService.displayRepositoryInformation(System.out, githubRepoList);

                    repositoriesUrl = githubRepoResult.getNextPageUrl();
                    LOGGER.info("Next page url for repositories {}", repositoriesUrl);
                }
            }
        } catch (ServiceInvokerException siEx) {
            LOGGER.error("Service Invoker Exception occurred", siEx);
            System.err.println("Application error occurred due to connectivity issues with Github API.");
        } catch (RepoCheckerAppException appEx) {
            LOGGER.error("Repo Checker Application Exception occurred", appEx);
            System.err.println("Application error occurred while displaying Github user information and stats.");
        } catch (Exception e) {
            LOGGER.error("Unknown error occurred", e);
            System.err.println("Unknown error occurred");
        }


    }
}
