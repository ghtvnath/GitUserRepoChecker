package com.nath.codeworks.repochecker.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties manager for GithubRestClient.
 * Properties are defined in application.properties file in resources.
 */
@Component
@ConfigurationProperties("github.rest.client")
public class GithubRestClientProperties {

    // url for getting user profile by git username
    private String userUrl;

    // maximum number of threads to be used when calling Rest APIs in parallel
    private int maxNumOfThreads;

    // number of results that should be retrieved per page when invoking a Github API
    private int resultsPerPage;

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public int getMaxNumOfThreads() {
        return maxNumOfThreads;
    }

    public void setMaxNumOfThreads(int maxNumOfThreads) {
        this.maxNumOfThreads = maxNumOfThreads;
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}
