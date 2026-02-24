package com.zoho.oauth.metadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiModelMetaRegister {

    private final Map<String, ModelEntry> models;

    private MultiModelMetaRegister(Map<String, ModelEntry> models) {
        this.models = models;
    }

    @SuppressWarnings("unchecked")
    public static MultiModelMetaRegister from(List<Map<String, Object>> modelList) {
        Map<String, ModelEntry> map = new HashMap<>();
        if (modelList != null) {
            for (Map<String, Object> m : modelList) {
                ModelEntry entry = ModelEntry.from(m);
                map.put(entry.computeKey(), entry);
            }
        }
        return new MultiModelMetaRegister(Collections.unmodifiableMap(map));
    }

    public ModelEntry resolve(String subjectTokenType,
                              String actorTokenType,
                              String requestedTokenType) {
        String key = ModelEntry.buildKey(subjectTokenType, actorTokenType, requestedTokenType);
        return models.get(key);
    }

    public Map<String, ModelEntry> getAllModels() {
        return models;
    }

    public static class ModelEntry {

        private final String name;
        private final String subjectTokenType;
        private final String actorTokenType;
        private final String requestedTokenType;
        private final String resource;
        private final String auditEventName;
        private final List<String> clientTypes;

        private ModelEntry(String name,
                           String subjectTokenType,
                           String actorTokenType,
                           String requestedTokenType,
                           String resource,
                           String auditEventName,
                           List<String> clientTypes) {
            this.name = name;
            this.subjectTokenType = subjectTokenType;
            this.actorTokenType = actorTokenType;
            this.requestedTokenType = requestedTokenType;
            this.resource = resource;
            this.auditEventName = auditEventName;
            this.clientTypes = clientTypes;
        }

        @SuppressWarnings("unchecked")
        public static ModelEntry from(Map<String, Object> cfg) {
            String name = (String) cfg.get("name");
            Map<String, String> kp = (Map<String, String>) cfg.get("auth_keypair");
            String stt = kp != null ? kp.get("subject_token_type") : null;
            String att = kp != null ? kp.get("actor_token_type") : null;
            String rtt = kp != null ? kp.get("requested_token_type") : null;
            String resource = (String) cfg.get("resource");
            String auditEventName = (String) cfg.get("audit_event_name");
            List<String> clientTypes = (List<String>) cfg.get("client_types");

            return new ModelEntry(name, stt, att, rtt, resource, auditEventName,
                    clientTypes != null ? Collections.unmodifiableList(clientTypes) : Collections.<String>emptyList());
        }

        public String computeKey() {
            return buildKey(subjectTokenType, actorTokenType, requestedTokenType);
        }

        public static String buildKey(String stt, String att, String rtt) {
            return safe(stt) + "_" + safe(att) + "_" + safe(rtt) + "_";
        }

        private static String safe(String v) {
            return v != null ? v : "";
        }

        public String getName() { return name; }
        public String getSubjectTokenType() { return subjectTokenType; }
        public String getActorTokenType() { return actorTokenType; }
        public String getRequestedTokenType() { return requestedTokenType; }
        public String getResource() { return resource; }
        public String getAuditEventName() { return auditEventName; }
        public List<String> getClientTypes() { return clientTypes; }
    }
}