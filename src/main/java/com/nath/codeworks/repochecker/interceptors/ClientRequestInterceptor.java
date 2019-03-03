package com.nath.codeworks.repochecker.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClientRequestInterceptor implements ClientHttpRequestInterceptor{

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRequestInterceptor.class);

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private static final String USER_AGENT_HEADER_VALUE = "RepoChecker";

    /**
     * Add Authorization and User-Agent Request headers with every Github API Rest Request
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        LOGGER.debug("Adding request headers to Github API Request {}", httpRequest.getURI());
        httpRequest.getHeaders().add(AUTHORIZATION_HEADER_NAME, "token 30f77ce99fe4b3ec07f3cd8b29870d79d646c5ac");
        httpRequest.getHeaders().add(USER_AGENT_HEADER_NAME, USER_AGENT_HEADER_VALUE);
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
