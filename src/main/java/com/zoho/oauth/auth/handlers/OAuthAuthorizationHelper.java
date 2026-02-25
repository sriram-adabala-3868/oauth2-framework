package com.zoho.oauth.auth.handlers;

import com.zoho.oauth.exceptions.OAuthExceptionBase;
import com.zoho.oauth.request.handlers.OAuthParameters;

public abstract class OAuthAuthorizationHelper {
    public abstract void validateClient(OAuthParameters params) throws OAuthExceptionBase;
    public abstract void validateScopes(OAuthParameters params) throws OAuthExceptionBase;
    public abstract void handleRequestSanity(String userId, OAuthParameters params) throws OAuthExceptionBase;
}