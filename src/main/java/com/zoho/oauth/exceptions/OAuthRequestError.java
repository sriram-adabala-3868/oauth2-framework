package com.zoho.oauth.exceptions;

/**
 * Enumeration of well-known OAuth error codes mapped to HTTP status codes.
 */
public enum OAuthRequestError {

    INVALID_REQUEST("invalid_request", 401, "Missing or malformed parameter"),
    UNSUPPORTED_GRANT_TYPE("unsupported_grant_type", 400, "Unknown grant type"),
    UNSUPPORTED_RESPONSE_TYPE("unsupported_response_type", 400, "Unknown response type"),
    INVALID_CLIENT("invalid_client", 401, "Client does not exist"),
    INVALID_SCOPE("invalid_scope", 400, "Missing or invalid scopes"),
    THROTTLE_LIMIT_REACHED("throttle_limit_reached", 429, "Rate limit exceeded"),
    SERVER_ERROR("server_error", 500, "Internal server error");

    private final String code;
    private final int httpStatus;
    private final String defaultMessage;

    OAuthRequestError(String code, int httpStatus, String defaultMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}