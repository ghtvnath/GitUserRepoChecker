package com.nath.codeworks.repochecker.exception;

/**
 * This exception will be thrown when application
 * faces error during executing the solution logic.
 */
public class RepoCheckerAppException extends Exception{

    public RepoCheckerAppException(String message) {
        super(message);
    }

}
