package com.utro.bmw.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by 123 on 17.05.2015.
 */
public class HttpsImageLoader {
    String url;

    public HttpsImageLoader(String url){
        this.url = url;
    }

    public Bitmap getBitmap()
    {
        //File f=fileCache.getFile(url);
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);

            HttpURLConnection conn = null;

            //if (imageUrl.getProtocol().toLowerCase().equals("https")) {
            trustAllHosts();
            //HttpsURLConnection https = (HttpsURLConnection) imageUrl.openConnection();
            //https.setHostnameVerifier(DO_NOT_VERIFY);
            //conn = https;
            /*} else {
                conn = (HttpURLConnection) imageUrl.openConnection();
            }*/

            conn = (HttpURLConnection) imageUrl.openConnection();
            //conn.setConnectTimeout(30000);
            //conn.setReadTimeout(30000);
            //conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            //OutputStream os = new FileOutputStream(f);
            //Utils.CopyStream(is, os);
            //os.close();
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    public static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
