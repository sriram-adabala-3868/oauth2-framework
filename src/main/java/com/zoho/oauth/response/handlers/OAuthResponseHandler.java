package com.zoho.oauth.response.handlers;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class OAuthResponseHandler {

    private int statusCode = 200;
    private JSONObject body;
    private final Map<String, String> headers = new LinkedHashMap<>();

    public OAuthResponseHandler setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public OAuthResponseHandler setBody(JSONObject body) {
        this.body = body;
        return this;
    }

    public OAuthResponseHandler addHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public OAuthResponseHandler build() {
        return this;
    }

    public void writeTo(HttpServletResponse response) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        for (Map.Entry<String, String> h : headers.entrySet()) {
            response.setHeader(h.getKey(), h.getValue());
        }
        if (body != null) {
            response.getWriter().write(body.toString());
        }
    }

    public int getStatusCode() { return statusCode; }
    public JSONObject getBody() { return body; }
}