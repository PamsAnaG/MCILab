/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.twitterlab.domain;

import com.ipn.mci.twitterlab.helpers.EncodingHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Pam
 */
public class OAuth {

    private String requestMethod;
    private String requestURL;
    private String oauthSignature;

    private String consumerKey;
    private String consumerSecret;
    private String oauthToken;
    private String oauthAccessSecret;
    private String oauthNonce;
    private String oauthTimeStamp;
    private String oauthSignatureMeth = "HMAC-SHA1";
    private String oauthV = "1.0";

    private SimpleDateFormat formatoNonce = new SimpleDateFormat("ddMMyyyyHHmmSS");

    public OAuth() {
        super();
        // SE INICIALIZA DE MANERA UNICA POR INSTANCIA DE LA CLASE
        oauthNonce = EncodingHelper.sha256(formatoNonce.format(new Date()));
        oauthTimeStamp = String.valueOf(new Date().getTime());
    }

    /**
     * @return the oauthSignature
     */
    public String getOauthSignature() {
        try {

            StringBuffer parameterString = new StringBuffer();

            Map<String, String> oauthParams = new TreeMap<String, String>();

            oauthParams.put(URLEncoder.encode("oauth_consumer_key", "UTF-8"), URLEncoder.encode(getConsumerKey(), "UTF-8"));
            oauthParams.put(URLEncoder.encode("oauth_nonce", "UTF-8"), URLEncoder.encode(getOauthNonce(), "UTF-8"));
            oauthParams.put(URLEncoder.encode("oauth_signature_method", "UTF-8"), URLEncoder.encode(getOauthSignatureMeth(), "UTF-8"));
            oauthParams.put(URLEncoder.encode("oauth_timestamp", "UTF-8"), URLEncoder.encode(getOauthTimeStamp(), "UTF-8"));
            oauthParams.put(URLEncoder.encode("oauth_token", "UTF-8"), URLEncoder.encode(getOauthToken(), "UTF-8"));
            oauthParams.put(URLEncoder.encode("oauth_version", "UTF-8"), URLEncoder.encode(getOauthV(), "UTF-8"));

            int counter = 0;
            for (Map.Entry<String, String> mapData : oauthParams.entrySet()) {
                parameterString.append(mapData.getKey()).append("=").append(mapData.getValue());
                counter++;
                if (counter < oauthParams.size()) {
                    parameterString.append("&");
                }
            }

            StringBuffer baseString = new StringBuffer();

            baseString.append(getRequestMethod().toUpperCase()).append("&").append(URLEncoder.encode(getRequestURL(), "UTF-8")).append("&")
                    .append(URLEncoder.encode(parameterString.toString(), "UTF-8"));

            StringBuffer signingKey = new StringBuffer();

            signingKey.append(URLEncoder.encode(consumerKey, "UTF-8")).append("&").append(URLEncoder.encode(oauthAccessSecret, "UTF-8")).append("&");

            byte[] encodedBytes = Base64.encodeBase64(computeSignature(baseString.toString(), signingKey.toString()).getBytes());
            
            oauthSignature = new String(encodedBytes);
            System.out.println(oauthSignature);

        } catch (Exception excp) {
            excp.printStackTrace();
            System.out.println("No fue posible generar oauth signature: " + excp.getMessage());
        }

        return oauthSignature;
    }

    private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {

        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");

        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
    }

    private static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }

    /**
     * @param oauthSignature the oauthSignature to set
     */
    public void setOauthSignature(String oauthSignature) {
        this.oauthSignature = oauthSignature;
    }

    /**
     * @return the consumerSecret
     */
    public String getConsumerSecret() {
        return consumerSecret;
    }

    /**
     * @param consumerSecret the consumerSecret to set
     */
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    /**
     * @return the oauthAccessSecret
     */
    public String getOauthAccessSecret() {
        return oauthAccessSecret;
    }

    /**
     * @param oauthAccessSecret the oauthAccessSecret to set
     */
    public void setOauthAccessSecret(String oauthAccessSecret) {
        this.oauthAccessSecret = oauthAccessSecret;
    }

    /**
     * @return the requestMethod
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * @param requestMethod the requestMethod to set
     */
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * @return the requestURL
     */
    public String getRequestURL() {
        return requestURL;
    }

    /**
     * @param requestURL the requestURL to set
     */
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    /**
     * @param oauthNonce the oauthNonce to set
     */
    public void setOauthNonce(String oauthNonce) {
        this.oauthNonce = oauthNonce;
    }

    /**
     * @param oauthTimeStamp the oauthTimeStamp to set
     */
    public void setOauthTimeStamp(String oauthTimeStamp) {
        this.oauthTimeStamp = oauthTimeStamp;
    }

    /**
     * @return the formatoNonce
     */
    public SimpleDateFormat getFormatoNonce() {
        return formatoNonce;
    }

    /**
     * @param formatoNonce the formatoNonce to set
     */
    public void setFormatoNonce(SimpleDateFormat formatoNonce) {
        this.formatoNonce = formatoNonce;
    }

    /**
     * @return the oauthTimeStamp
     */
    public String getOauthTimeStamp() {
        return oauthTimeStamp;
    }

    /**
     * @return the consumerKey
     */
    public String getConsumerKey() {
        return consumerKey;
    }

    /**
     * @param consumerKey the consumerKey to set
     */
    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    /**
     * @return the oauthToken
     */
    public String getOauthToken() {
        return oauthToken;
    }

    /**
     * @param oauthToken the oauthToken to set
     */
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    /**
     * @return the oauthNonce
     */
    public String getOauthNonce() {
        return oauthNonce;
    }

    /**
     * @return the oauthSignatureMeth
     */
    public String getOauthSignatureMeth() {
        return oauthSignatureMeth;
    }

    /**
     * @param oauthSignatureMeth the oauthSignatureMeth to set
     */
    public void setOauthSignatureMeth(String oauthSignatureMeth) {
        this.oauthSignatureMeth = oauthSignatureMeth;
    }

    /**
     * @return the oauthV
     */
    public String getOauthV() {
        return oauthV;
    }

    /**
     * @param oauthV the oauthV to set
     */
    public void setOauthV(String oauthV) {
        this.oauthV = oauthV;
    }

}
