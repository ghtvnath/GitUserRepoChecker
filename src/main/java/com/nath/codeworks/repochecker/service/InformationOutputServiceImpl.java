package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.model.dto.GithubRepo;
import com.nath.codeworks.repochecker.model.ghresponse.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.PrintStream;
import java.util.List;

@Service
public class InformationOutputServiceImpl implements InformationOutputService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InformationOutputServiceImpl.class);

    /**
     * <p>This method is invoked to display basic user information username (login) and name</p>
     *
     * @param out PrintStream to which the output should be written
     * @param user User for whose the information should be displayed
     * @throws RepoCheckerAppException
     */
    @Override
    public void displayUserInformation(PrintStream out, User user) throws RepoCheckerAppException {
        try {
            LOGGER.debug("Displaying user information");
            out.println(String.format("Github username : %s", user.getLogin()));
            out.println(String.format("Github user : %s", user.getName()));
            out.println();
        } catch (Exception ex) {
            LOGGER.error("Error occurred while displaying user information");
            throw new RepoCheckerAppException("Error occurred while displaying user information");
        }
    }

    /**
     * <p>This method is invoked to display the list of Repository names and the contributors list of
     * each repository</p>
     *
     * @param out PrintStream to which the output should be written
     * @param githubRepoList List<GithubRepo> List of GithubRepo of which information should be displayed
     * @throws RepoCheckerAppException
     */
    @Override
    public void displayRepositoryInformation(PrintStream out, List<GithubRepo> githubRepoList)
            throws RepoCheckerAppException {
        try {
            if (CollectionUtils.isEmpty(githubRepoList)) {
                LOGGER.debug("User does not have any repository on Github");
                out.println("User does not have any repository on Github.");
                return;
            }
            githubRepoList.forEach(r -> printRepoDetails(out, r));
        } catch (Exception ex) {
            LOGGER.error("Error occurred while displaying user repositories information");
            throw new RepoCheckerAppException("Error occurred while displaying user repositories information");
        }
    }

    private void printRepoDetails(PrintStream out, GithubRepo repo) {
        out.println(String.format("Repo name : %s", repo.getRepoName()));
        if (CollectionUtils.isEmpty(repo.getContributors())) {
            out.println("\t\t---- No contributors ----");
        } else {
            out.println("\t\t---- Contributors and number of contributions ----");
            repo.getContributors().forEach(s -> out.println(String.format("\t\t%s (%d)",
                    s.getLogin(), s.getContributions())));
        }
        out.println();
    }

}
