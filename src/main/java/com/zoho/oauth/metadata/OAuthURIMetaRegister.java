package com.zoho.oauth.metadata;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class OAuthURIMetaRegister {

    private static final Logger LOG = Logger.getLogger(OAuthURIMetaRegister.class.getName());

    private final String baseUri;
    private final String[] versions;
    private final boolean orgContext;
    private final boolean auditRequired;
    private final boolean throttleRequired;
    private final boolean retryHeaderAllowed;
    private final List<String> clientTypesSupported;
    private final Map<String, String> handlerPackages;
    private final Map<String, OAuthSubURIMetaRegister> subUriRegisters;

    private OAuthURIMetaRegister(String baseUri,
                                  String[] versions,
                                  boolean orgContext,
                                  boolean auditRequired,
                                  boolean throttleRequired,
                                  boolean retryHeaderAllowed,
                                  List<String> clientTypesSupported,
                                  Map<String, String> handlerPackages,
                                  Map<String, OAuthSubURIMetaRegister> subUriRegisters) {
        this.baseUri = baseUri;
        this.versions = versions;
        this.orgContext = orgContext;
        this.auditRequired = auditRequired;
        this.throttleRequired = throttleRequired;
        this.retryHeaderAllowed = retryHeaderAllowed;
        this.clientTypesSupported = clientTypesSupported;
        this.handlerPackages = handlerPackages;
        this.subUriRegisters = subUriRegisters;
    }

    @SuppressWarnings("unchecked")
    public static OAuthURIMetaRegister from(Map<String, Object> cfg) {
        String baseUri = (String) cfg.get("base_uri");
        String versionsRaw = (String) cfg.get("versions");
        String[] versions = versionsRaw.split("\\s*,\\s*");

        boolean orgContext = Boolean.TRUE.equals(cfg.get("org_context"));
        boolean auditRequired = Boolean.TRUE.equals(cfg.get("audit_required"));
        boolean throttleRequired = Boolean.TRUE.equals(cfg.get("throttle_required"));
        boolean retryHeaderAllowed = Boolean.TRUE.equals(cfg.get("retry_header_allowed"));

        List<String> clientTypes = (List<String>) cfg.getOrDefault("client_types_supported",
                Collections.emptyList());

        Map<String, String> handlerPackages = new LinkedHashMap<>();
        for (String v : versions) {
            String key = v + "_handler_package";
            String pkg = (String) cfg.get(key);
            if (pkg != null) {
                handlerPackages.put(v, pkg);
            }
        }

        List<Map<String, Object>> subUriList =
                (List<Map<String, Object>>) cfg.getOrDefault("sub_uris", Collections.emptyList());

        Map<String, OAuthSubURIMetaRegister> subUriRegisters = new LinkedHashMap<>();
        for (Map<String, Object> subCfg : subUriList) {
            OAuthSubURIMetaRegister sub = OAuthSubURIMetaRegister.from(subCfg);
            subUriRegisters.put(sub.getSubUri(), sub);
        }

        return new OAuthURIMetaRegister(
                baseUri, versions, orgContext, auditRequired,
                throttleRequired, retryHeaderAllowed,
                Collections.unmodifiableList(clientTypes),
                Collections.unmodifiableMap(handlerPackages),
                Collections.unmodifiableMap(subUriRegisters));
    }

    public String getBaseUri() { return baseUri; };
    public String[] getVersions() { return versions; };
    public boolean isOrgContext() { return orgContext; };
    public boolean isAuditRequired() { return auditRequired; };
    public boolean isThrottleRequired() { return throttleRequired; };
    public boolean isRetryHeaderAllowed() { return retryHeaderAllowed; };
    public List<String> getClientTypesSupported() { return clientTypesSupported; };

    public String getHandlerPackage(String version) {
        return handlerPackages.get(version);
    }

    public OAuthSubURIMetaRegister getSubUriRegister(String subUri) {
        return subUriRegisters.get(subUri);
    }

    public Map<String, OAuthSubURIMetaRegister> getAllSubUriRegisters() {
        return subUriRegisters;
    }
}