package com.example.flutter_application_3;

import android.app.Activity;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * A factory for SSLContexts.
 * Builds an SSLContext with custom KeyStore and TrustStore, to work with a client cert signed by a self-signed CA cert.
 */
public class SSLContextFactory {

    private static SSLContextFactory theInstance = null;

    private SSLContextFactory() {
    }

    public static SSLContextFactory getInstance() {
        if(theInstance == null) {
            theInstance = new SSLContextFactory();
        }
        return theInstance;
    }

    /**
     * Creates an SSLContext with the client and server certificates
     * @param clientCertFile A File containing the client certificate
     * @param clientCertPassword Password for the client certificate
     * @param caCertString A String containing the server certificate
     * @return An initialized SSLContext
     * @throws Exception
     */
    public SSLContext makeContext(File clientCertFile, String clientCertPassword, String caCertString,String SubcaCertString,Activity activity) throws Exception {
        final KeyStore keyStore = loadPKCS12KeyStore(clientCertFile, clientCertPassword,activity);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, clientCertPassword.toCharArray());
        KeyManager[] keyManagers = kmf.getKeyManagers();

        final KeyStore trustStore = loadPEMTrustStore(caCertString,SubcaCertString);
        TrustManager[] trustManagers = {new CustomTrustManager(trustStore)};

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);

        return sslContext;
    }
    private KeyStore loadPEMTrustStore(String caCertificateString, String subCaCertificateString) throws Exception {

        byte[] der = loadPemCertificate(new ByteArrayInputStream(caCertificateString.getBytes()));
        ByteArrayInputStream derInputStream = new ByteArrayInputStream(der);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(derInputStream);
        String alias = cert.getSubjectX500Principal().getName();

        byte[] subCaDer = loadPemCertificate(new ByteArrayInputStream(subCaCertificateString.getBytes()));
        ByteArrayInputStream subCaDerDerInputStream = new ByteArrayInputStream(subCaDer);
        CertificateFactory subCaCertificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate subCaCert = (X509Certificate) subCaCertificateFactory.generateCertificate(subCaDerDerInputStream);
        String subCaAlias = cert.getSubjectX500Principal().getName();

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry(alias, cert);
        trustStore.setCertificateEntry(subCaAlias, subCaCert);

        return trustStore;
    }
    /**
     * Produces a KeyStore from a PKCS12 (.p12) certificate file, typically the client certificate
     * @param certificateFile A file containing the client certificate
     * @param clientCertPassword Password for the certificate
     * @return A KeyStore containing the certificate from the certificateFile
     * @throws Exception
     */
    private KeyStore loadPKCS12KeyStore(File certificateFile, String clientCertPassword, Activity activity) throws Exception {
        KeyStore keyStore = null;
        InputStream ins = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
             ins = activity.getResources().openRawResource(
                    activity.getResources().getIdentifier("clientchain",
                            "raw", activity.getPackageName()));

            keyStore.load(ins, clientCertPassword.toCharArray());
        } finally {
            try {
                if(ins != null) {
                    ins.close();
                }
            } catch(IOException ex) {
                // ignore
            }
        }
        return keyStore;
    }

    /**
     * Reads and decodes a base-64 encoded DER certificate (a .pem certificate), typically the server's CA cert.
     * @param certificateStream an InputStream from which to read the cert
     * @return a byte[] containing the decoded certificate
     * @throws IOException
     */
    byte[] loadPemCertificate(InputStream certificateStream) throws IOException {

        byte[] der = null;
        BufferedReader br = null;

        try {
            StringBuilder buf = new StringBuilder();
            br = new BufferedReader(new InputStreamReader(certificateStream));

            String line = br.readLine();
            while(line != null) {
                if(!line.startsWith("--")){
                    buf.append(line);
                }
                line = br.readLine();
            }

            String pem = buf.toString();
            der = Base64.decode(pem, Base64.DEFAULT);

        } finally {
           if(br != null) {
               br.close();
           }
        }

        return der;
    }
}
