package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.model.dto.GithubRepo;
import com.nath.codeworks.repochecker.model.ghresponse.Contributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Callable;

public class AsyncRepoDetailsChecker implements Callable<GithubRepo>{

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRepoDetailsChecker.class);

    private RestTemplate restTemplate;
    private String url;
    private String repoName;

    protected AsyncRepoDetailsChecker(RestTemplate restTemplate, String url, String repoName) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.repoName = repoName;
    }

    /**
     * <p>This method will be run in ExecutorService. Hence contributions per repository
     * can be retrieved in parallel. This would be used only if the order in which
     * repositories are displayed does not matter.</p>
     *
     * @return GithubRepo This would contain the repository name, and list of contributors
     * @throws Exception
     */
    @Override
    public GithubRepo call() throws Exception {
        LOGGER.debug("Contributor Url {}", url);
        ResponseEntity<List<Contributor>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Contributor>>() {
                });
        List<Contributor> contributorsList = response.getBody();

        /*contributorsList is sent by Github API endpoint sorted by number of contributions
        in descending order as to specification
        If that was not the case, the list would have been required to sort as following to meet requirement

        contributorsList.sort(Comparator.comparing(Contributor::getContributions).reversed());
        */

        GithubRepo githubRepo = new GithubRepo();
        githubRepo.setRepoName(repoName);
        githubRepo.setContributors(contributorsList);
        return githubRepo;
    }
}
