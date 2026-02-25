package com.zoho.oauth.metadata.instances;

import com.zoho.oauth.auth.handlers.OAuthStandAloneAuthorization;
import com.zoho.oauth.response.handlers.OAuthResourceHelper;

public class StandAloneAuthAndResourceInstance {

    private final OAuthStandAloneAuthorization authorization;
    private final OAuthResourceHelper resource;

    public StandAloneAuthAndResourceInstance(OAuthStandAloneAuthorization authorization,
                                             OAuthResourceHelper resource) {
        this.authorization = authorization;
        this.resource = resource;
    }

    public OAuthStandAloneAuthorization getAuthorization() { return authorization; }
    public OAuthResourceHelper getResource() { return resource; }
}