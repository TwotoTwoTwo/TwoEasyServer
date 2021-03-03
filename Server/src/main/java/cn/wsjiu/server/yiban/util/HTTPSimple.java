package cn.wsjiu.server.yiban.util;

import java.util.List;
import java.security.KeyStore;

import java.net.URL;
import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.Header;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.apache.http.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;


public class HTTPSimple {
    public HTTPSimple() {
    }

    public static String GET(String var0) {
        String var1 = "";

        try {
            CloseableHttpClient var2 = getClientInstance(var0);
            HttpGet var3 = new HttpGet(var0);
            CloseableHttpResponse var4 = var2.execute(var3);
            int var5 = var4.getStatusLine().getStatusCode();
            if (var5 > 300 && var5 < 310) {
                Header[] var6 = var4.getHeaders("Location");
                if (var6.length > 0) {
                    var2.close();
                    return GET(var6[0].toString().substring(10));
                }
            }

            HttpEntity var8 = var4.getEntity();
            var1 = EntityUtils.toString(var8);
            EntityUtils.consume(var8);
            var2.close();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return var1;
    }

    public static String POST(String var0, List<NameValuePair> var1) {
        String var2 = "";
        int var3 = var0.indexOf(63);
        if (var3 > 0) {
            var0 = var0.substring(0, var3);
        }

        try {
            CloseableHttpClient var4 = getClientInstance(var0);
            HttpPost var5 = new HttpPost(var0);
            var5.setEntity(new UrlEncodedFormEntity(var1));
            CloseableHttpResponse var6 = var4.execute(var5);
            int var7 = var6.getStatusLine().getStatusCode();
            if (var7 > 300 && var7 < 310) {
                Header[] var8 = var6.getHeaders("Location");
                if (var8.length > 0) {
                    var4.close();
                    return POST(var8[0].toString().substring(10), var1);
                }
            }

            HttpEntity var10 = var6.getEntity();
            var2 = EntityUtils.toString(var10);
            EntityUtils.consume(var10);
            var4.close();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return var2;
    }

    private static boolean isSecurity(String var0) throws Exception {
        URL var1 = new URL(var0);
        return var1.getProtocol().contentEquals("https");
    }

    private static CloseableHttpClient getClientInstance(String var0) throws Exception {
        CloseableHttpClient var1 = null;
        if (isSecurity(var0)) {
            KeyStore var2 = KeyStore.getInstance(KeyStore.getDefaultType());
            SSLContext var3 = SSLContexts.custom().loadTrustMaterial(var2, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] var1, String var2) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory var4 = new SSLConnectionSocketFactory(var3, new String[]{"TLSv1"}, (String[])null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            var1 = HttpClients.custom().setSSLSocketFactory(var4).build();
        } else {
            var1 = HttpClients.createDefault();
        }

        return var1;
    }
}