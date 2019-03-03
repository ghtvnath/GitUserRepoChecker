package com.nath.codeworks.repochecker.interceptors;

import com.nath.codeworks.repochecker.exception.ServiceInvokerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateErrorHandler.class);

    /**
     * <p>If the status code is not 200 by RestTemplate response, this method is used to
     * log error response details.
     * When true is returned from this method, handleError method would be invoked.</p>
     *
     * @param clientHttpResponse Response received from Github API
     * @return boolean True if an error occurred, false if expected response status 200, 201
     * @throws IOException
     */
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        if (clientHttpResponse.getStatusCode() == HttpStatus.OK ||
                clientHttpResponse.getStatusCode() == HttpStatus.NO_CONTENT) {
            return false;
        }
        LOGGER.warn("Status code: {}", clientHttpResponse.getStatusCode());
        LOGGER.warn("Response: {}", clientHttpResponse.getStatusText());
        return true;
    }

    /**
     * <p>When hasError method returns true to indicate that there is Error in response,
     * this method is invoked which in return will throw ServiceInvokationException. </p>
     *
     * @param clientHttpResponse Response received from Github API
     * @throws ServiceInvokerException Handle error will throw ServiceInvokerException
     */
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws ServiceInvokerException {
        LOGGER.error("Throwing Service Invoker Exception");
        throw new ServiceInvokerException("Error occurred while retrieving user information");
    }
}
