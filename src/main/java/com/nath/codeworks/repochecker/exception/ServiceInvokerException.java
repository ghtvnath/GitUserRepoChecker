package com.nath.codeworks.repochecker.exception;

import java.io.IOException;

/**
 * ServiceInvokerException would be thrown while invoking a Rest API
 * to GitHub. It would be used either when there is an error response
 * from Github or when the response status code is not the expected one.
 */
public class ServiceInvokerException extends RuntimeException{

    public ServiceInvokerException(String message) {
        super(message);
    }

}
