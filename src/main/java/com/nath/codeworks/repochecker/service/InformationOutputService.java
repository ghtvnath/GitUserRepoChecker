package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.model.dto.GithubRepo;
import com.nath.codeworks.repochecker.model.ghresponse.User;

import java.io.PrintStream;
import java.util.List;

public interface InformationOutputService {

    void displayUserInformation (PrintStream out, User user) throws RepoCheckerAppException;

    void displayRepositoryInformation (PrintStream out, List<GithubRepo> githubRepoList)
            throws RepoCheckerAppException;

}
