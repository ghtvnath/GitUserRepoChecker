package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.exception.ServiceInvokerException;
import com.nath.codeworks.repochecker.model.dto.GithubRepo;
import com.nath.codeworks.repochecker.model.dto.GithubResultList;
import com.nath.codeworks.repochecker.model.ghresponse.Repository;
import com.nath.codeworks.repochecker.model.ghresponse.User;

import java.util.List;

public interface GithubRestClient {

    User getGithubUserInfo (String username) throws ServiceInvokerException, RepoCheckerAppException;

    GithubResultList<Repository> getGithubRepositories (String repositoriesApiUrl)
            throws ServiceInvokerException, RepoCheckerAppException;

    List<GithubRepo> getRepositoryContributors (List<Repository> listOfRepositories)
            throws ServiceInvokerException, RepoCheckerAppException;

}
