package com.zoho.oauth.metadata;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OAuthTypeValuesMetaRegister {

    private final String value;
    private final String authorization;
    private final String resource;
    private final String auditEventName;
    private final String audit;
    private final List<String> clientTypes;
    private final boolean multiModel;
    private final MultiModelMetaRegister multiModelRegister;

    private OAuthTypeValuesMetaRegister(String value,
                                         String authorization,
                                         String resource,
                                         String auditEventName,
                                         String audit,
                                         List<String> clientTypes,
                                         boolean multiModel,
                                         MultiModelMetaRegister multiModelRegister) {
        this.value = value;
        this.authorization = authorization;
        this.resource = resource;
        this.auditEventName = auditEventName;
        this.audit = audit;
        this.clientTypes = clientTypes;
        this.multiModel = multiModel;
        this.multiModelRegister = multiModelRegister;
    }

    @SuppressWarnings("unchecked")
    public static OAuthTypeValuesMetaRegister from(Map<String, Object> cfg) {
        String value = (String) cfg.get("value");
        String authorization = (String) cfg.get("authorization");
        String resource = (String) cfg.get("resource");
        String auditEventName = (String) cfg.get("audit_event_name");
        String audit = (String) cfg.get("audit");
        List<String> clientTypes = (List<String>) cfg.get("client_types");
        boolean isMultiModel = Boolean.TRUE.equals(cfg.get("is_multimodel"));

        MultiModelMetaRegister mmr = null;
        if (isMultiModel) {
            List<Map<String, Object>> models =
                    (List<Map<String, Object>>) cfg.get("models");
            mmr = MultiModelMetaRegister.from(models);
        }

        return new OAuthTypeValuesMetaRegister(
                value, authorization, resource, auditEventName, audit,
                clientTypes != null ? Collections.unmodifiableList(clientTypes) : Collections.<String>emptyList(),
                isMultiModel, mmr);
    }

    public String getValue() { return value; }
    public String getAuthorization() { return authorization; }
    public String getResource() { return resource; }
    public String getAuditEventName() { return auditEventName; }
    public String getAudit() { return audit; }
    public List<String> getClientTypes() { return clientTypes; }
    public boolean isMultiModel() { return multiModel; }
    public MultiModelMetaRegister getMultiModelRegister() { return multiModelRegister; }
}