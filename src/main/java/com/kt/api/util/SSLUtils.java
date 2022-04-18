package com.kt.api.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public final class SSLUtils {

    private static final Logger logger = LoggerFactory.getLogger(SSLUtils.class);

    /**
     * Basic Auth시 필요한 Header의 Value값을 생성한다.
     *
     * @param username Username
     * @param password Password
     * @return Header의 Value값
     */
    public static String getBasicAuth(String username, String password) {
        String authString = username + ":" + password;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        return new String(authEncBytes);
    }

    public HttpHeaders getHttpHeaders(String userAgent, String host) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());
        headers.set(HttpHeaders.USER_AGENT, userAgent);
        logger.info("host=" + host);
        if (null != host) {
            headers.set(HttpHeaders.HOST, host);
        }

        return headers;
    }

    /**
     * SSL을 지원하는 REST Template을 생성한다.
     *
     * @param proxyHost Proxy Host (null인 경우 사용하지 않음)
     * @param proxyPort Proxy Port (Proxy Host가 null인 경우 사용하지 않음)
     * @return REST Template
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static RestTemplate getRestTemplate(String proxyHost, int proxyPort) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = new TrustSelfSignedStrategy();
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy)
                .build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        if (null != proxyHost && proxyPort > 0) {
            logger.info("PROXY CONFIGURED | proxyHost=" + proxyHost + " | proxyPort=" + proxyPort);
            HttpHost proxy = new HttpHost(proxyHost, proxyPort, Proxy.Type.HTTP.name());
            httpClient = HttpClients.custom().setSSLSocketFactory(csf).setRoutePlanner(new DefaultProxyRoutePlanner(proxy)).build();
        }
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

    public ResponseEntity<String> getApiResponse(HttpMethod httpMethod, final String URL, final String userAgent,
                                                 String proxyHost, int proxyPort, String host) throws HttpClientErrorException {
        ResponseEntity<String> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(getHttpHeaders(userAgent, host));
        try {
            if (null != httpMethod && null != URL) {
                RestTemplate request = null;
                try {
                    request = getRestTemplate(proxyHost, proxyPort);
                    response = request.exchange(URL, httpMethod, httpEntity, String.class);
                } catch (KeyManagementException | KeyStoreException | NoSuchAlgorithmException e) {
                    logger.warn("Error creating Rest Template", e);
                }
            }
        } catch (HttpClientErrorException ex) {
            logger.warn("Method = " + httpMethod.toString() + "Request URL = " + URL);
            logger.warn("Headers =" + getHttpHeaders(userAgent, host));
            logger.warn("Response Status = " + ex.getStatusText());
            logger.warn("Response Body = " + ex.getResponseBodyAsString());
            throw ex;
        }
        return response;
    }
}
