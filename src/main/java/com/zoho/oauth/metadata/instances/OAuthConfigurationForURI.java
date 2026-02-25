package com.zoho.oauth.metadata.instances;

import com.zoho.oauth.auth.handlers.OAuthAuthorizationHelper;
import com.zoho.oauth.auth.handlers.OAuthStandAloneAuthorization;
import com.zoho.oauth.exceptions.OAuthExceptionBase;
import com.zoho.oauth.exceptions.OAuthRequestError;
import com.zoho.oauth.exceptions.OAuthRequestException;
import com.zoho.oauth.metadata.OAuthSubURIMetaRegister;
import com.zoho.oauth.metadata.OAuthTypeValuesMetaRegister;
import com.zoho.oauth.metadata.OAuthURIMetaRegister;
import com.zoho.oauth.response.handlers.OAuthResourceHelper;
import com.zoho.oauth.util.OAuthFrameworkUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OAuthConfigurationForURI {

    private static final Logger LOG = Logger.getLogger(OAuthConfigurationForURI.class.getName());

    private static final ConcurrentHashMap<String, OAuthConfigurationForURI> instances =
            new ConcurrentHashMap<>();

    private final OAuthURIMetaRegister uriMeta;
    private final OAuthSubURIMetaRegister subUriMeta;

    private final ConcurrentHashMap<String, OAuthAuthAndResourceInstance> valueBasedCache =
            new ConcurrentHashMap<>();

    private volatile StandAloneAuthAndResourceInstance standAloneInstance;

    private OAuthConfigurationForURI(OAuthURIMetaRegister uriMeta,
                                      OAuthSubURIMetaRegister subUriMeta) {
        this.uriMeta = uriMeta;
        this.subUriMeta = subUriMeta;
    }

    public static OAuthConfigurationForURI get(OAuthURIMetaRegister uriMeta,
                                                OAuthSubURIMetaRegister subUriMeta) {
        String key = uriMeta.getBaseUri() + "/" + subUriMeta.getSubUri();
        return instances.computeIfAbsent(key,
                k -> new OAuthConfigurationForURI(uriMeta, subUriMeta));
    }

    public OAuthAuthAndResourceInstance resolveValueHandlers(String value,
                                                             String version,
                                                             String clientType)
            throws OAuthExceptionBase {

        OAuthTypeValuesMetaRegister valMeta = subUriMeta.getValueRegister(value);
        if (valMeta == null) {
            throw new OAuthRequestException(
                    subUriMeta.getType() == OAuthSubURIMetaRegister.EndpointType.GRANT_TYPE
                            ? OAuthRequestError.UNSUPPORTED_GRANT_TYPE
                            : OAuthRequestError.UNSUPPORTED_RESPONSE_TYPE);
        }

        String cacheKey = value + ":" + version + ":" + clientType;
        OAuthAuthAndResourceInstance cached = valueBasedCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        return valueBasedCache.computeIfAbsent(cacheKey, k -> {
            try {
                String pkg = uriMeta.getHandlerPackage(version);
                String subPkg = pkg + "." + OAuthFrameworkUtil.toSubPackage(subUriMeta.getSubUri())
                        + "." + OAuthFrameworkUtil.toSubPackage(value);

                OAuthAuthorizationHelper auth = loadAuth(valMeta, subPkg, clientType);
                OAuthResourceHelper res = loadResource(valMeta, subPkg, clientType);
                return new OAuthAuthAndResourceInstance(auth, res);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Handler resolution failed for " + cacheKey, e);
                throw new RuntimeException(e);
            }
        });
    }

    public StandAloneAuthAndResourceInstance resolveStandAloneHandlers(String version)
            throws OAuthExceptionBase {

        if (standAloneInstance != null) {
            return standAloneInstance;
        }
        synchronized (this) {
            if (standAloneInstance != null) {
                return standAloneInstance;
            }
            try {
                String pkg = uriMeta.getHandlerPackage(version);
                String subPkg = pkg + "." + OAuthFrameworkUtil.toSubPackage(subUriMeta.getSubUri());

                String className = OAuthFrameworkUtil.toPascalCase(subUriMeta.getSubUri());

                OAuthStandAloneAuthorization auth = null;
                if (!"disabled".equals(subUriMeta.getAuthorization())) {
                    String authClass = subPkg + "." + className + "Authorization";
                    auth = (OAuthStandAloneAuthorization) Class.forName(authClass).newInstance();
                }

                OAuthResourceHelper res = null;
                if (subUriMeta.getResource() != null) {
                    String resClass = subPkg + "." + className + "Resource";
                    res = (OAuthResourceHelper) Class.forName(resClass).newInstance();
                }

                standAloneInstance = new StandAloneAuthAndResourceInstance(auth, res);
                return standAloneInstance;
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "Stand-alone handler resolution failed", e);
                throw new OAuthRequestException(OAuthRequestError.SERVER_ERROR, e);
            }
        }
    }

    private OAuthAuthorizationHelper loadAuth(OAuthTypeValuesMetaRegister valMeta,
                                               String subPkg,
                                               String clientType) throws Exception {
        String authType = valMeta.getAuthorization();
        if ("disabled".equals(authType)) {
            return null;
        }
        String className;
        if ("dynamic".equals(authType)) {
            className = subPkg + "." + OAuthFrameworkUtil.toPascalCase(clientType) + "Authorization";
        } else {
            className = subPkg + "." + OAuthFrameworkUtil.toPascalCase(valMeta.getValue()) + "Authorization";
        }
        return (OAuthAuthorizationHelper) Class.forName(className).newInstance();
    }

    private OAuthResourceHelper loadResource(OAuthTypeValuesMetaRegister valMeta,
                                              String subPkg,
                                              String clientType) throws Exception {
        String resType = valMeta.getResource();
        String className;
        if ("dynamic".equals(resType)) {
            className = subPkg + "." + OAuthFrameworkUtil.toPascalCase(clientType) + "Resource";
        } else {
            className = subPkg + "." + OAuthFrameworkUtil.toPascalCase(valMeta.getValue()) + "Resource";
        }
        return (OAuthResourceHelper) Class.forName(className).newInstance();
    }

    public OAuthURIMetaRegister getUriMeta() { return uriMeta; }
    public OAuthSubURIMetaRegister getSubUriMeta() { return subUriMeta; }
}