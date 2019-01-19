/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ipn.mci.twitterlab;

import com.ipn.mci.twitterlab.domain.OAuth;
import static java.awt.SystemColor.text;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Pam
 */
public class OauthTwitter {

    public String getOauthAuthString(OAuth oauthData) {

        try {

            StringBuffer authorizationString = new StringBuffer();

            authorizationString.append("OAuth oauth_consumer_key=\"").append(URLEncoder.encode(oauthData.getConsumerKey(), "UTF-8")).append("\"")
                    .append(", ").append("oauth_nonce=\"").append(URLEncoder.encode(oauthData.getOauthNonce(), "UTF-8")).append("\"")
                    .append(", ").append("oauth_signature=\"").append(URLEncoder.encode(oauthData.getOauthSignature(), "UTF-8")).append("\"")
                    .append(", ").append("oauth_signature_method=\"").append(URLEncoder.encode(oauthData.getOauthSignatureMeth(), "UTF-8")).append("\"")
                    .append(", ").append("oauth_timestamp=\"").append(URLEncoder.encode(oauthData.getOauthTimeStamp(), "UTF-8")).append("\"")
                    .append(", ").append("oauth_token=\"").append(URLEncoder.encode(oauthData.getOauthToken(), "UTF-8")).append("\"")
                    .append(", ").append("oauth_version=\"").append(URLEncoder.encode(oauthData.getOauthV(), "UTF-8")).append("\"");

            return authorizationString.toString();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
        return "";

    }

    public String hmacSha1(String value, String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String type = "HmacSHA1";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return bytesToHex(bytes);
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private String generateSignature(String signatueBaseStr, String oAuthConsumerSecret, String oAuthTokenSecret) throws Exception {
        byte[] byteHMAC = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec;
            if (null == oAuthTokenSecret) {
                String signingKey = encode(oAuthConsumerSecret) + '&';
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            } else {
                String signingKey = encode(oAuthConsumerSecret) + '&' + encode(oAuthTokenSecret);
                spec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
            }
            mac.init(spec);
            byteHMAC = mac.doFinal(signatueBaseStr.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(byteHMAC);
    }

    private String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sb = "";
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb += "%2A";
            } else if (focus == '+') {
                sb += "%20";
            } else if (focus == '%' && i + 1 < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb += '~';
                i += 2;
            } else {
                sb += focus;
            }
        }
        return sb.toString();
    }

    public static void main(String arg[]) {

        /*OauthTwitter oauthService = new OauthTwitter();
        OAuth oauth = new OAuth();
        oauth.setConsumerKey("1otukjBXtwAkNkC0m1UwtovIF");
        oauth.setOauthToken("161048591-KxCYXE0gXQUbmnhOLjot4J9LMcTqbrcSS9l2lelo");
        oauth.setRequestMethod("GET");
        oauth.setRequestURL("https://stream.twitter.com/1.1/statuses/filter.json");
        oauth.setOauthAccessSecret("p4tqX2mpRppuILzYCmdex5RO08Zug47OtSSTX9raHuFgx");
        System.out.println(oauthService.getOauthAuthString(oauth));*/
        try {
            OauthTwitter oauthService = new OauthTwitter();            

            System.out.println(new String(Base64.encodeBase64("842B5299887E88760212A056AC4EC2EE1626B549".getBytes())));

        } catch (Exception excp) {
            excp.printStackTrace();

        }

    }

}
