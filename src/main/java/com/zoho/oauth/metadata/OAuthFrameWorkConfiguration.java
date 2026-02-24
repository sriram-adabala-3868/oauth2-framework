package com.zoho.oauth.metadata;

import org.yaml.snakeyaml.Yaml;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OAuthFrameWorkConfiguration implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(OAuthFrameWorkConfiguration.class.getName());
    private static final String ROOT_CONFIG = "conf/oauth/oauthconfig.yml";
    private static final String CONFIG_DIR = "conf/oauth/";

    private static volatile Map<String, OAuthURIMetaRegister> uriRegisters;
    private static volatile boolean isInited = false;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (isInited) {
            return;
        }
        synchronized (OAuthFrameWorkConfiguration.class) {
            if (isInited) {
                return;
            }
            try {
                load();
                isInited = true;
                LOG.info("OAuth framework configuration loaded successfully.");
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Failed to load OAuth configuration", e);
                throw new RuntimeException("OAuth configuration init failed", e);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @SuppressWarnings("unchecked")
    private void load() {
        Yaml yaml = new Yaml();
        Map<String, Object> root = loadYaml(yaml, ROOT_CONFIG);
        List<String> files = (List<String>) root.get("files");
        if (files == null || files.isEmpty()) {
            throw new IllegalStateException("oauthconfig.yml must declare at least one file");
        }

        Map<String, OAuthURIMetaRegister> registers = new LinkedHashMap<>();
        for (String file : files) {
            Map<String, Object> cfg = loadYaml(yaml, CONFIG_DIR + file);
            OAuthURIMetaRegister register = OAuthURIMetaRegister.from(cfg);
            registers.put(register.getBaseUri(), register);
            LOG.info("Loaded OAuth config: " + file + " -> /" + register.getBaseUri());
        }
        uriRegisters = Collections.unmodifiableMap(registers);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> loadYaml(Yaml yaml, String path) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if (is == null) {
            throw new IllegalStateException("YAML resource not found on classpath: " + path);
        }
        return yaml.loadAs(is, Map.class);
    }

    public static OAuthURIMetaRegister getRegister(String baseUri) {
        checkInited();
        return uriRegisters.get(baseUri);
    }

    public static Map<String, OAuthURIMetaRegister> getAllRegisters() {
        checkInited();
        return uriRegisters;
    }

    private static void checkInited() {
        if (!isInited) {
            throw new IllegalStateException("OAuthFrameWorkConfiguration has not been initialized");
        }
    }
}
