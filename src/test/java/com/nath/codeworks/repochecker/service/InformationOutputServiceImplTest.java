package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.model.dto.GithubRepo;
import com.nath.codeworks.repochecker.model.ghresponse.Contributor;
import com.nath.codeworks.repochecker.model.ghresponse.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InformationOutputServiceImplTest {

    private InformationOutputService informationOutputService;
    private ByteArrayOutputStream baos;
    private PrintStream ps;

    @Before
    public void setUp() {
        informationOutputService = new InformationOutputServiceImpl();
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
    }

    @Test
    public void testDisplayUserInformation() throws RepoCheckerAppException{
        User user = getUser();
        informationOutputService.displayUserInformation(ps, user);
        Assert.assertEquals("Github username : ghtvnath\n" +
                "Github user : Tharindu Vishwanath\n\n", baos.toString());
    }

    @Test
    public void testDisplayRepositoryInformation() throws RepoCheckerAppException{
        List<GithubRepo> githubRepoList = new ArrayList<>();
        githubRepoList.add(getGitHubRepo1());
        githubRepoList.add(getGitHubRepo2());
        informationOutputService.displayRepositoryInformation(ps, githubRepoList);
        Assert.assertEquals("Repo name : Test Repository 1\n" +
                "\t\t---- Contributors and number of contributions ----\n" +
                "\t\tghtvnath (10)\n" +
                "\t\ttvwaruni (7)\n" +
                "\n" +
                "Repo name : Test Repository 2\n" +
                "\t\t---- No contributors ----\n\n", baos.toString());
    }

    @Test
    public void testDisplayRepositoryInformationNoRepositories() throws RepoCheckerAppException{
        informationOutputService.displayRepositoryInformation(ps, null);
        Assert.assertEquals("User does not have any repository on Github.\n", baos.toString());
    }




    private User getUser () {
        User user = new User();
        user.setLogin("ghtvnath");
        user.setName("Tharindu Vishwanath");
        return user;
    }

    private GithubRepo getGitHubRepo1 () {
        GithubRepo githubRepo = new GithubRepo();
        githubRepo.setRepoName("Test Repository 1");

        Contributor contributor1 = new Contributor();
        contributor1.setLogin("ghtvnath");
        contributor1.setContributions(10);
        Contributor contributor2 = new Contributor();
        contributor2.setLogin("tvwaruni");
        contributor2.setContributions(7);

        List<Contributor> contributors = Arrays.asList(contributor1, contributor2);
        githubRepo.setContributors(contributors);

        return githubRepo;
    }

    private GithubRepo getGitHubRepo2 () {
        GithubRepo githubRepo = new GithubRepo();
        githubRepo.setRepoName("Test Repository 2");

        return githubRepo;
    }


}
