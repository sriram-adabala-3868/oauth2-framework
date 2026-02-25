package com.zoho.oauth.throttlers;

import com.zoho.oauth.request.handlers.OAuthParameters;

public interface ThrottlerInterface {
    boolean allowRequest(OAuthParameters params);
    long getRetryAfterSeconds(OAuthParameters params);
    void recordCompletion(OAuthParameters params, boolean success);
}