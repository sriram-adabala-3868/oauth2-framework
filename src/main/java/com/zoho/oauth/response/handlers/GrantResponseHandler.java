package com.zoho.oauth.response.handlers;

public class GrantResponseHandler extends OAuthResponseHandler {

    public GrantResponseHandler() {
        addHeader("Cache-Control", "no-store");
        addHeader("Pragma", "no-cache");
    }
}