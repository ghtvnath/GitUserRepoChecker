package com.nath.codeworks.repochecker.service;

import com.nath.codeworks.repochecker.exception.RepoCheckerAppException;
import com.nath.codeworks.repochecker.exception.ServiceInvokerException;
import com.nath.codeworks.repochecker.interceptors.RestTemplateErrorHandler;
import com.nath.codeworks.repochecker.model.dto.GithubResultList;
import com.nath.codeworks.repochecker.model.ghresponse.Repository;
import com.nath.codeworks.repochecker.model.ghresponse.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GithubRestClientTest {

    @Mock
    RestTemplateBuilder restTemplateBuilder;

    @Mock
    RestTemplate restTemplate;

    @Mock
    GithubRestClientProperties restClientProperties;

    @Mock
    RestTemplateErrorHandler restTemplateErrorHandler;

    @Mock
    ClientHttpRequestInterceptor clientHttpRequestInterceptor;

    GithubRestClientImpl githubRestClient;

    private static final String USER_API_URL = "https://api.github.com/users/{username}";

    private static final String VALID_REPO_URL = "https://api.github.com/users/ghtvnath/repos";

    private static final String VALID_GITHUB_USERNAME = "ghtvnath";

    private static final String INVALID_GITHUB_USERNAME = "waruniumesha";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(restTemplateBuilder.errorHandler(eq(restTemplateErrorHandler)))
                .thenReturn(restTemplateBuilder);

        when(restTemplateBuilder.build()).thenReturn(restTemplate);



        githubRestClient = new GithubRestClientImpl(restTemplateBuilder, clientHttpRequestInterceptor,
                restTemplateErrorHandler, restClientProperties);

        doAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocationOnMock) throws Throwable {
                String str = invocationOnMock.getArgument(2);
                User user = null;
                if (VALID_GITHUB_USERNAME.equals(str)) {
                    user = getUser();
                }
                return user;
            }
        }).when(restTemplate).getForObject(eq(USER_API_URL),
                eq(User.class), anyString());
    }

    @Test
    public void testGetGithubUserInfo() throws ServiceInvokerException, RepoCheckerAppException {
        when(restClientProperties.getUserUrl()).thenReturn(USER_API_URL);
        User user = githubRestClient.getGithubUserInfo(VALID_GITHUB_USERNAME);
        Assert.assertNotNull(user);
        Assert.assertEquals(VALID_GITHUB_USERNAME, user.getLogin());
    }

    @Test
    public void testGetGithubUserInfoInvalidUser() throws ServiceInvokerException, RepoCheckerAppException {
        when(restClientProperties.getUserUrl()).thenReturn(USER_API_URL);
        User user = githubRestClient.getGithubUserInfo(INVALID_GITHUB_USERNAME);
        Assert.assertNull(user);
    }

    @Test(expected = RepoCheckerAppException.class)
    public void testGetGithubUserInfoInvalidUsername() throws ServiceInvokerException, RepoCheckerAppException {
        User user = githubRestClient.getGithubUserInfo(null);
    }

    @Test
    public void testGetGithubRepositories() throws ServiceInvokerException, RepoCheckerAppException {
        when(restClientProperties.getResultsPerPage()).thenReturn(30);
        when(restTemplate.exchange(
                eq("https://api.github.com/users/ghtvnath/repos?per_page=30"),
                eq(HttpMethod.GET),
                isNull(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Repository>>>any()))
                .thenReturn(getRepositoriesResponse());

        GithubResultList<Repository> githubRepoResultList =  githubRestClient.getGithubRepositories(VALID_REPO_URL);
        Assert.assertNotNull(githubRepoResultList);
        Assert.assertTrue(githubRepoResultList.getResultsList().size() == 2);
        Assert.assertEquals("https://api.github.com/user/2946769/repos?page=3", githubRepoResultList.getNextPageUrl());
        Assert.assertTrue(githubRepoResultList.hasNext());
        Assert.assertFalse(githubRepoResultList.isEmpty());
    }


    private User getUser() {
        User user = new User();
        user.setLogin("ghtvnath");
        user.setName("Tharindu Vishwanath");
        return user;
    }

    private ResponseEntity<List<Repository>> getRepositoriesResponse () {
        Repository repo1 = new Repository();
        repo1.setName("Repo 1");
        repo1.setContributors_url("sample/repo/url1");
        Repository repo2 = new Repository();
        repo1.setName("Repo 2");
        repo1.setContributors_url("sample/repo/url2");

        List<Repository> repoList = Arrays.asList(repo1, repo2);

        String linkHeaderValue = "<https://api.github.com/user/2946769/repos?page=1>; rel=\"prev\"," +
                " <https://api.github.com/user/2946769/repos?page=3>; rel=\"next\"," +
                " <https://api.github.com/user/2946769/repos?page=6>; rel=\"last\"," +
                " <https://api.github.com/user/2946769/repos?page=1>; rel=\"first\"";
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Link", linkHeaderValue);

        return ResponseEntity.ok().headers(responseHeaders).body(repoList);

    }

}
