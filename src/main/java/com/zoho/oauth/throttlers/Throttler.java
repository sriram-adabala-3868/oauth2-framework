package com.zoho.oauth.throttlers;

import com.zoho.oauth.request.handlers.OAuthParameters;

public class Throttler implements ThrottlerInterface {

    @Override
    public boolean allowRequest(OAuthParameters params) {
        return true;
    }

    @Override
    public long getRetryAfterSeconds(OAuthParameters params) {
        return -1;
    }

    @Override
    public void recordCompletion(OAuthParameters params, boolean success) {
    }
}