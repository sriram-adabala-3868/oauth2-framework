package com.zoho.oauth.metadata.instances;

import com.zoho.oauth.auth.handlers.OAuthAuthorizationHelper;
import com.zoho.oauth.response.handlers.OAuthResourceHelper;

public class OAuthAuthAndResourceInstance {

    private final OAuthAuthorizationHelper authorization;
    private final OAuthResourceHelper resource;

    public OAuthAuthAndResourceInstance(OAuthAuthorizationHelper authorization,
                                        OAuthResourceHelper resource) {
        this.authorization = authorization;
        this.resource = resource;
    }

    public OAuthAuthorizationHelper getAuthorization() { return authorization; }
    public OAuthResourceHelper getResource() { return resource; }
}