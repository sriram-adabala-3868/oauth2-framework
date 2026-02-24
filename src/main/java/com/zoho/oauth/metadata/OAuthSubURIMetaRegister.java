package com.zoho.oauth.metadata;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OAuthSubURIMetaRegister {

    public enum EndpointType {
        RESPONSE_TYPE("response_type"),
        GRANT_TYPE("grant_type"),
        STAND_ALONE("stand_alone");

        private final String yamlValue;

        EndpointType(String yamlValue) { this.yamlValue = yamlValue; }

        public static EndpointType fromYaml(String v) {
            for (EndpointType t : values()) {
                if (t.yamlValue.equals(v)) return t;
            }
            throw new IllegalArgumentException("Unknown endpoint type: " + v);
        }
    }

    private final String subUri;
    private final EndpointType type;
    private final String[] methods;
    private final String authorization;
    private final String resource;
    private final String audit;
    private final String auditEventName;
    private final List<String> clientTypes;
    private final Map<String, OAuthTypeValuesMetaRegister> valueRegisters;

    private OAuthSubURIMetaRegister(String subUri,
                                     EndpointType type,
                                     String[] methods,
                                     String authorization,
                                     String resource,
                                     String audit,
                                     String auditEventName,
                                     List<String> clientTypes,
                                     Map<String, OAuthTypeValuesMetaRegister> valueRegisters) {
        this.subUri = subUri;
        this.type = type;
        this.methods = methods;
        this.authorization = authorization;
        this.resource = resource;
        this.audit = audit;
        this.auditEventName = auditEventName;
        this.clientTypes = clientTypes;
        this.valueRegisters = valueRegisters;
    }

    @SuppressWarnings("unchecked")
    public static OAuthSubURIMetaRegister from(Map<String, Object> cfg) {
        String subUri = (String) cfg.get("sub_uri");
        EndpointType type = EndpointType.fromYaml((String) cfg.get("type"));
        String methodsRaw = (String) cfg.get("method");
        String[] methods = methodsRaw.split("\\s+");

        String authorization = (String) cfg.get("authorization");
        String resource = (String) cfg.get("resource");
        String audit = (String) cfg.get("audit");
        String auditEventName = (String) cfg.get("audit_event_name");
        List<String> clientTypes = (List<String>) cfg.get("client_types");

        Map<String, OAuthTypeValuesMetaRegister> valueRegisters = new LinkedHashMap<>();
        List<Map<String, Object>> values =
                (List<Map<String, Object>>) cfg.get("values");
        if (values != null) {
            for (Map<String, Object> valCfg : values) {
                OAuthTypeValuesMetaRegister vr = OAuthTypeValuesMetaRegister.from(valCfg);
                valueRegisters.put(vr.getValue(), vr);
            }
        }

        return new OAuthSubURIMetaRegister(
                subUri, type, methods,
                authorization, resource, audit, auditEventName,
                clientTypes != null ? Collections.unmodifiableList(clientTypes) : Collections.<String>emptyList(),
                Collections.unmodifiableMap(valueRegisters));
    }

    public String getSubUri() { return subUri; };
    public EndpointType getType() { return type; };
    public String[] getMethods() { return methods; };
    public String getAuthorization() { return authorization; };
    public String getResource() { return resource; };
    public String getAudit() { return audit; };
    public String getAuditEventName() { return auditEventName; };
    public List<String> getClientTypes() { return clientTypes; }

    public OAuthTypeValuesMetaRegister getValueRegister(String value) {
        return valueRegisters.get(value);
    }

    public Map<String, OAuthTypeValuesMetaRegister> getAllValueRegisters() {
        return valueRegisters;
    }

    public boolean isMethodAllowed(String method) {
        for (String m : methods) {
            if (m.equalsIgnoreCase(method)) return true;
        }
        return false;
    }
}