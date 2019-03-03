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
     * @param clientHttpResponse
     * @return
     * @throws IOException
     *
     * <p>If the status code is not 200 by RestTemplate response, this method is used to
     *    log error response details.
     *    When true is returned from this method, handleError method would be invoked.</p>
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
     * @param clientHttpResponse
     * @throws IOException
     *
     * <p>When hasError method returns true to indicate that there is Error in response,
     *    this method is invoked which in return will throw ServiceInvokationException. </p>
     */
    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
            LOGGER.error("Throwing ServiceInvokationException");
            throw new ServiceInvokerException("Error occurred while retrieving user information");
    }
}
