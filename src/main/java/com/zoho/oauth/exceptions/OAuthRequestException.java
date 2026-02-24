package com.zoho.oauth.exceptions;

/**
 * Concrete request-level exception backed by an {@link OAuthRequestError}.
 */
public class OAuthRequestException extends OAuthExceptionBase {

    private final OAuthRequestError error;

    public OAuthRequestException(OAuthRequestError error) {
        super(error.getCode(), error.getHttpStatus(), error.getDefaultMessage());
        this.error = error;
    }

    public OAuthRequestException(OAuthRequestError error, String customMessage) {
        super(error.getCode(), error.getHttpStatus(), customMessage);
        this.error = error;
    }

    public OAuthRequestException(OAuthRequestError error, Throwable cause) {
        super(error.getCode(), error.getHttpStatus(), error.getDefaultMessage(), cause);
        this.error = error;
    }

    public OAuthRequestError getError() {
        return error;
    }
}