package com.zoho.oauth.exceptions;

/**
 * Root exception for every error the OAuth framework can raise.
 */
public class OAuthExceptionBase extends Exception {

    private final int httpStatus;
    private final String errorCode;

    public OAuthExceptionBase(String errorCode, int httpStatus, String message) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public OAuthExceptionBase(String errorCode, int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }
}