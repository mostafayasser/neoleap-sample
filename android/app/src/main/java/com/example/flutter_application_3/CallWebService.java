package com.example.flutter_application_3;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import com.example.flutter_application_3.R;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import org.ksoap2.transport.HttpsTransportSE;
import android.util.Log; 

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

// import static com.example.flutter_application_3.DeviceInfo.getTerminalId;
// import com.example.flutter_application_3.Transaction_Model;
// import com.mpos.Model.Users;


public class CallWebService {
    static String NAMESPACE = "https://tempuri.org/";
    private final String URL = "https://83.101.139.173:1334/TrpWebService.asmx";
    URL url;
    HttpURLConnection conn;

    private SharedClass obj_Shar = new SharedClass();

    private static SSLContext sslContext;
    private String clientCertificateName;
    private static String caCertificateName;
    private static String clientCertificatePassword;
    private int lastResponseCode;
    static HttpsTransportSE androidhttpsTransportSE;
    static AuthenticationParameters authParams;


    public int getLastResponseCode() {
        return lastResponseCode;
    }

    private static final CallWebService obj_CallWebService = new CallWebService();

    /*private CallWebService() {

    }

    public static void initialize(Activity activity) throws Exception {

        HandelConnection(activity);
    }*/

    public static void Login(String UserName, String Password, Activity activity) throws Exception {
        Log.d("Login", "Login");
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        final String METHOD_NAME = "Login";
        final String SOAP_ACTION = "https://tempuri.org/Login";

        SharedClass obj_Share = new SharedClass();
        Users obj_User = new Users();
        if (obj_Share.checkInternetConnection(activity)) {
            Log.d("Login", "checkInternetConnection");
            //initialization tje connection to the server
            HandelConnection(activity);
            Log.d("Login", "HandelConnection");
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("userName", UserName);
            request.addProperty("Password", Password);
            Log.d("Login", "SoapObject");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            try {
                Log.d("Login", "try");
                androidhttpsTransportSE = new HttpsTransportSE("83.101.139.173", 1334, "/TrpWebService.asmx", Integer.MAX_VALUE);
                Log.d("Login", "HttpsTransportSE");
                ((HttpsServiceConnectionSE) androidhttpsTransportSE.getServiceConnection()).setSSLSocketFactory(sslContext.getSocketFactory());
                Log.d("Login", "setSSLSocketFactory");
                androidhttpsTransportSE.call(SOAP_ACTION, envelope);
                Log.d("Login", "call");
                SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
                Log.d("Login", "result");

                obj_User.webServiceResult = result.toString();
                Log.d("result", result.toString());
                obj_User.NoConnectionError = "true";
            } catch (Exception ex) {
                obj_User.webServiceResult = "false";
                obj_User.NoConnectionError = "true";
                ex.printStackTrace();
            }
        } else obj_User.NoConnectionError = "false";

    }

    // public static void ValidateRetailerData(String CommercialRegisterOrIDNumber, String TID, String Mobile, boolean isID, boolean isInsert, String Password, String userName, String code, Activity activity) throws Exception {
    //     SharedClass obj_Share = new SharedClass();
    //     final String METHOD_NAME = "validateAndCreateRetailerUser";
    //     final String SOAP_ACTION = "https://tempuri.org/validateAndCreateRetailerUser";
    //     SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    //     Users obj_User = new Users();
    //     if (obj_Share.checkInternetConnection(activity)) {

    //         //initialization tje connection to the server
    //         HandelConnection(activity);

    //         if (!isID) {
    //             request.addProperty("CommercialRegister", CommercialRegisterOrIDNumber);
    //             request.addProperty("ID_Number", "");
    //         } else {
    //             request.addProperty("ID_Number", CommercialRegisterOrIDNumber);
    //             request.addProperty("CommercialRegister", "");
    //         }


    //         request.addProperty("SerialNumber", "123123123");// obj_Share.getTerminalSerialNo(activity));
    //         request.addProperty("TID", TID);
    //         request.addProperty("Mobile", "0" + Mobile);
    //         request.addProperty("isID", isID);
    //         request.addProperty("isInsert", isInsert);
    //         request.addProperty("Password", Password);
    //         request.addProperty("UserName", userName);
    //         request.addProperty("ConfirmationCode", code);


    //         SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    //         envelope.dotNet = true;
    //         envelope.setOutputSoapObject(request);


    //         try {
    //             androidhttpsTransportSE = new HttpsTransportSE("83.101.139.173", 1334, "/TrpWebService.asmx", Integer.MAX_VALUE);
    //             ((HttpsServiceConnectionSE) androidhttpsTransportSE.getServiceConnection()).setSSLSocketFactory(sslContext.getSocketFactory());
    //             androidhttpsTransportSE.call(SOAP_ACTION, envelope);

    //             SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
    //             obj_User.ValidateRetailerData = result.toString();
    //             obj_User.NoConnectionError = "true";
    //         } catch (Exception ex) {

    //             obj_User.NoConnectionError = "false";
    //             obj_User.ValidateRetailerData = "false";
    //         }
    //     } else obj_User.NoConnectionError = "false";


    // }

    // public static void Synchronization(Activity activity) throws Exception {
    //     Cursor res;
    //     SoapObject result;
    //     String lastSyncDate = "";
    //     final String METHOD_NAME = "Synchronization";
    //     final String SOAP_ACTION = "https://tempuri.org/Synchronization";


    //     //  HandelConnection(activity);
    //     SharedClass obj_Shar = new SharedClass();
    //     DataBaseHelper obj_db = new DataBaseHelper(activity);
    //     Users obj_User = new Users();

    //     ArrayList<Transaction_Model> lst_SyncData = new ArrayList<>();
    //     Transaction_Model obj_SyncData = new Transaction_Model();
    //     res = obj_db.getAllData("lastSyncDateTimeTable", "", "");

    //     if (res.getCount() > 0) {
    //         while (res.moveToNext()) {
    //             lastSyncDate = res.getString(0);
    //         }

    //     } else {
    //         lastSyncDate = Long.toString(obj_Shar.getLastSexMonthIDate(activity));
    //         obj_db.InsertData(lastSyncDate, "", "", "", "lastSyncDateTime");
    //     }
    //     if (getTerminalId() == null || getTerminalId().isEmpty() || getTerminalId().equals("null")) {
    //         obj_User.ConnectedToTheDevice = "false";
    //         return;
    //     } else {
    //         obj_User.ConnectedToTheDevice = "true";
    //     }

    //     if (obj_Shar.checkInternetConnection(activity)) {

    //         //initialization tje connection to the server
    //         //HandelConnection(activity);
    //         SoapObject request1 = new SoapObject(NAMESPACE, METHOD_NAME);
    //         request1.addProperty("dateTime", lastSyncDate);
    //         request1.addProperty("TerminalId", getTerminalId());


    //         SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    //         envelope.dotNet = true;

    //         envelope.setOutputSoapObject(request1);


    //         try {
    //             androidhttpsTransportSE = new HttpsTransportSE("83.101.139.173", 1334, "/TrpWebService.asmx", 600000);
    //             ((HttpsServiceConnectionSE) androidhttpsTransportSE.getServiceConnection()).setSSLSocketFactory(sslContext.getSocketFactory());

    //             androidhttpsTransportSE.call(SOAP_ACTION, envelope);

    //             if (envelope.getResponse() != null) {
    //                 result = (SoapObject) envelope.getResponse();
    //                 for (int i = 0; i < result.getPropertyCount(); i++) {
    //                     PropertyInfo pi = new PropertyInfo();
    //                     result.getPropertyInfo(i, pi);

    //                     Object property = result.getProperty(i);
    //                     if (pi.name.equals("Transaction_Model") && property instanceof SoapObject) {
    //                         SoapObject transDetail = (SoapObject) property;
    //                         String Amount = transDetail.getPrimitivePropertyAsString("Amount");
    //                         String RRN = transDetail.getPrimitivePropertyAsString("RRN");
    //                         String Date = transDetail.getPrimitivePropertyAsString("StartDateTime");
    //                         if (i == result.getPropertyCount() - 1) {
    //                             lastSyncDate = transDetail.getPrimitivePropertyAsString("iStartDateTime");
    //                         }
    //                         obj_SyncData = new Transaction_Model();
    //                         obj_SyncData.setAmount(Amount);
    //                         obj_SyncData.setRRN(RRN);
    //                         obj_SyncData.setDate(Date);

    //                         // add item to list
    //                         lst_SyncData.add(obj_SyncData);
    //                     }

    //                 }
    //                 // check if data found then insert data to data base and update last Sync date
    //                 if (lst_SyncData.size() > 0) {
    //                     obj_db.synk(lst_SyncData);

    //                     obj_db.updateLastSyncDate(lastSyncDate, "lastSyncDateTimeTable");
    //                 }
    //             }


    //             obj_User.webServiceResult = "true";
    //             obj_User.NoConnectionError = "true";
    //         } catch (Exception ex) {
    //             obj_User.webServiceResult = "false";
    //             obj_User.NoConnectionError = "false";
    //         }
    //     } else obj_User.NoConnectionError = "false";

    // }

    // public static String getTransactionXML(Activity activity, String RRN) throws Exception {

    //     final String METHOD_NAME = "getTransactionXML";
    //     final String SOAP_ACTION = "https://tempuri.org/getTransactionXML";
    //     SharedClass obj_Share = new SharedClass();
    //     String XML = "";
    //     Users obj_User = new Users();

    //     //initialization tje connection to the server
    //     //  HandelConnection(activity);
    //     SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
    //     request.addProperty("RRN", RRN);
    //     request.addProperty("TerminalId", getTerminalId());


    //     SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    //     envelope.dotNet = true;
    //     envelope.setOutputSoapObject(request);
    //     if (getTerminalId() == null || getTerminalId().isEmpty() || getTerminalId().equals("null")) {
    //         obj_User.ConnectedToTheDevice = "false";
    //         return "";
    //     } else {
    //         obj_User.ConnectedToTheDevice = "true";
    //     }
    //     if (obj_Share.checkInternetConnection(activity)) try {


    //         androidhttpsTransportSE = new HttpsTransportSE("83.101.139.173", 1334, "/TrpWebService.asmx", 600000);
    //         ((HttpsServiceConnectionSE) androidhttpsTransportSE.getServiceConnection()).setSSLSocketFactory(sslContext.getSocketFactory());

    //         androidhttpsTransportSE.call(SOAP_ACTION, envelope);
    //         try {
    //             SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
    //             XML = result.toString();
    //         } catch (Exception ex) {
    //         }


    //         obj_User.webServiceResult = "true";
    //         obj_User.NoConnectionError = "true";
    //     } catch (Exception ex) {
    //         obj_User.webServiceResult = "false";
    //         obj_User.NoConnectionError = "false";
    //     }
    //     else {
    //         obj_User.webServiceResult = "false";
    //         obj_User.NoConnectionError = "false";
    //     }
    //     return XML;
    // }

    public static void HandelConnection(Activity activity) throws Exception {
        clientCertificatePassword = "Inters0ft477";
        authParams = new AuthenticationParameters();
        authParams.setClientCertificate(getClientCertFile(activity));
        authParams.setClientCertificatePassword(clientCertificatePassword);
        authParams.setCaCertificate(readCaCert(activity));
        authParams.setSubCaCertificateString(readSubCaCert(activity));
        File clientCertFile = authParams.getClientCertificate();
        sslContext = SSLContextFactory.getInstance().makeContext(clientCertFile, authParams.getClientCertificatePassword(), authParams.getCaCertificate(), authParams.getSubCaCertificateString(), activity);
        Log.d("sslContext", sslContext.toString());
        CookieHandler.setDefault(new CookieManager());

    }

    /**
     * Creates a {@link javax.net.ssl.HttpsURLConnection} which is configured to work with a custom authority
     * certificate.
     *
     * @param urlString     remote url string.
     * @param context       Application Context
     * @param certRawResId  R.raw.id of certificate file (*.crt). Should be stored in /res/raw.
     * @param allowAllHosts If true then client will not check server against host names of certificate.
     * @return Http url connection.
     * @throws Exception If there is an error initializing the connection.
     */
    public static HttpsURLConnection getHttpsUrlConnection(String urlString, Context context, int certRawResId, boolean allowAllHosts) throws Exception {

        // build key store with ca certificate
        KeyStore keyStore = buildKeyStore(context, certRawResId);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Create a connection from url
        java.net.URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        // skip hostname security check if specified
        if (allowAllHosts) {
            urlConnection.setHostnameVerifier(new AllowAllHostnameVerifier());
        }

        return urlConnection;
    }

    private static KeyStore buildKeyStore(Context context, int certRawResId) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        // init a default key store
        final char[] KEY_PASSWORD = "Inters0ft477".toCharArray();
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, KEY_PASSWORD);

        // read and add certificate authority
        Certificate cert = readCert(context, certRawResId);
        keyStore.setCertificateEntry("ca", cert);

        return keyStore;
    }

    private static Certificate readCert(Context context, int certResourceId) throws CertificateException, IOException {

        // read certificate resource
        InputStream caInput = context.getResources().openRawResource(certResourceId);

        Certificate ca;
        try {
            // generate a certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ca = cf.generateCertificate(caInput);
        } finally {
            caInput.close();
        }

        return ca;
    }


    private static File getClientCertFile(Activity activity) {

        String strfile1 = activity.getApplicationContext().getFilesDir().getAbsolutePath() + "/raw/clientchain";
        Log.d("strfile1", strfile1);
        AssetManager am = activity.getAssets();

        try {
            String[] names = am.list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File("android.resource://" + activity.getPackageName() + "/" + R.raw.clientchain);

        if (file.isFile()) {
            String[] children = file.list();

        }


        return new File(strfile1);
    }

    private static String readCaCert(Activity activity) throws Exception {
        caCertificateName = "intermediate";
        InputStream ins = activity.getResources().openRawResource(activity.getResources().getIdentifier("intermediate", "raw", activity.getPackageName()));
        return IOUtil.readFully(ins);
    }

    private static String readSubCaCert(Activity activity) throws Exception {

        InputStream ins = activity.getResources().openRawResource(activity.getResources().getIdentifier("ca", "raw", activity.getPackageName()));
        return IOUtil.readFully(ins);
    }
}
