package com.zoho.oauth.response.handlers;

import com.zoho.oauth.exceptions.OAuthExceptionBase;
import com.zoho.oauth.request.handlers.OAuthParameters;

import javax.servlet.http.HttpServletResponse;

public abstract class OAuthResourceHelper {

    public abstract OAuthResponseHandler handleRequest(OAuthParameters params,
                                                        String method,
                                                        HttpServletResponse response)
            throws OAuthExceptionBase;

    public OAuthResponseHandler getFailureResponse(OAuthExceptionBase e,
                                                    OAuthParameters params) {
        return null;
    }
}