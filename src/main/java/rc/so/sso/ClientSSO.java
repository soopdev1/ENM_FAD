/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rc.so.sso;

import com.fasterxml.jackson.databind.ObjectMapper;
import rc.so.engine.Action;
import static rc.so.engine.Action.estraiEccezione;
import static rc.so.engine.Action.log;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Administrator
 */
public class ClientSSO {

    public static final String ENDPOINT = "https://sso.selfiemployment.net/";

    public static ResponseSSO logout(String username) {
        ResponseSSO output;
        String link = ENDPOINT + "/connect/disconnect";
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
            newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            newBuilder.hostnameVerifier((hostname, session) -> true);
            OkHttpClient client = newBuilder.build();
            RequestBody formBody = new FormBody.Builder()
                    .add("client_id", "Auth_App")
                    .add("client_secret", "d3ee88c4139a50b18b1c5179dc93015b2e8eccb399b3134d0840fde29856f266")
                    .add("username", username)
                    .build();

            Request request = new Request.Builder().url(link).post(formBody).build();
            try (Response response = client.newCall(request).execute()) {
                if (response == null || response.body() == null) {
                    output = new ResponseSSO("ERROR CODE: 500", "ERROR MESSAGE: RESPONSE NULL");
                } else if (response.code() == 204) {
                    output = new ResponseSSO("OK", "OK");
                } else {
                    output = new ResponseSSO("ERROR CODE: " + response.code(), "ERROR MESSAGE: " + response.body().string());
                }
            }
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
            output = new ResponseSSO("ERROR CODE: 500", "ERROR MESSAGE: " + ex.getMessage());
        }
        return output;
    }

    public static ResponseSSO login(String username, String password) {
        ResponseSSO output;
        String link = ENDPOINT + "/connect/token";
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
            newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            newBuilder.hostnameVerifier((hostname, session) -> true);
            OkHttpClient client = newBuilder.build();
            RequestBody formBody = new FormBody.Builder()
                    .add("client_id", "Auth_App")
                    .add("client_secret", "d3ee88c4139a50b18b1c5179dc93015b2e8eccb399b3134d0840fde29856f266")
                    .add("grant_type", "password")
                    .add("username", username)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder().url(link).post(formBody).build();
            try (Response response = client.newCall(request).execute()) {
                if (response == null || response.body() == null) {
                    output = new ResponseSSO("ERROR CODE: 500", "ERROR MESSAGE: RESPONSE NULL");
                } else if (response.code() == 200) {
                    output = new ObjectMapper().readValue(response.body().string(), ResponseSSO.class);
                } else {
                    output = new ResponseSSO("ERROR CODE: " + response.code(), "ERROR MESSAGE: " + response.body().string());
                }
            }
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
            output = new ResponseSSO("ERROR CODE: 500", "ERROR MESSAGE: " + ex.getMessage());
        }
        return output;
    }
    
    
    
    public static String encrypt(String plaintext) {
        try {
            String key = new String(Base64.decodeBase64(Action.get_Path("puk_cer")), Charset.defaultCharset());
            String publicKeyPEM = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PUBLIC KEY-----", "");
            byte[] encoded = Base64.decodeBase64(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            RSAPublicKey key1 = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key1);
            return Base64.encodeBase64String(
                    cipher.doFinal(plaintext.getBytes("UTF8"))
            );
//            return new String(cipher.doFinal(plaintext.getBytes("UTF8")), "UTF8");
        } catch (Exception ex) {
            log.severe(estraiEccezione(ex));
            return null;
        }
    }
}
