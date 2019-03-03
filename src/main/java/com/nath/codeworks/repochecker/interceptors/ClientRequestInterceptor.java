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

    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private static final String USER_AGENT_HEADER_VALUE = "RepoChecker";

    /**
     * Add headers that should be with every Github API Rest Request
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        LOGGER.debug("Adding request headers to Github API Request {}", httpRequest.getURI());
        httpRequest.getHeaders().add(USER_AGENT_HEADER_NAME, USER_AGENT_HEADER_VALUE);

        /* later, Authorization header also can be added here with every request that is being sent by the
        Application. Currently only 60 requests per hour is supported by Github for unauthenticated access.
        With a token, this can be increased to 5000. */

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
