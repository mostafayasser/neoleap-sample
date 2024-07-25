package com.example.flutter_application_3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;
import java.util.HashMap;

import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.widget.Toast;
import java.net.Socket;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;


public class NeoLeapPaymentActivity extends MposBaseActivity {
    private static final int MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT = 0;
    int SERVER_PORT = 80;
    public static PrintWriter output;
    public static BufferedReader input;
    MPOSService mposService = null;
    String SERVER_IP;
    Socket socket;
    Thread Thread1 = null;
    DataInputStream dataInputStream = null;

    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    class Thread1 implements Runnable {

        @Override
        public void run() {
            System.out.println("Thread1");
            try {
                Log.d("SERVER_IP", SERVER_IP);
                Log.d("SERVER_PORT", String.valueOf(SERVER_PORT));
                System.out.println("before socket");
                socket = new Socket(SERVER_IP, SERVER_PORT);
                System.out.println("socket " + socket);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("input " + input);
                output = new PrintWriter(socket.getOutputStream());
                System.out.println("output " + output);
            } catch (Exception e) {
                e.printStackTrace();
                returnResult(getStackTrace(e));
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            new Thread(new Thread2()).start();
        }
    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            if(socket==null){
                returnResult("Connection failed");
                return;
            }
            while (true) {
                try {
                    System.out.println("Thread2");
                    String message = null;
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    if (dataInputStream.read() != -1) {
                        message = readStream(dataInputStream);
                    }
                    if (message != null && !message.equals("AAAAAA\u0003")) {
                        System.out.println("in message ");
                        int msglen = message.length();
                        Log.d("message", message.substring(7, msglen));
                        message = message.substring(7, msglen);
                        // message=message.replace("1U00854","");
                        //showTransactionResult(message);
                        returnResult("Transaction success");
                    } else {
                        returnResult("Transaction cancelled");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    returnResult("Transaction failed");
                }
            }
        }
    }

    public class Thread3 implements Runnable {
        private String message = "";

        public Thread3(String _message) {
            this.message = _message;
        }

        @Override
        public void run() {
            try{
            output.write(message);
            output.flush();
            } catch (Exception e) {
                e.printStackTrace();
                returnResult(e.getMessage());
            }

        }
    }

    private void connectWifi() {
        AlertDialog.Builder wifibuilder = new AlertDialog.Builder(this);
        wifibuilder.setTitle("Enter IP Address ");
        final EditText wifiinput = new EditText(this);
        // wifiinput.setInputType(InputType.TYPE_CLASS_NUMBER);
        wifiinput.setText("192.168.0.0");

        wifibuilder.setView(wifiinput);

        // Set up the buttons
        wifibuilder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                    //if (txt_deviceName.equals("")) {
                        // txt_deviceName = wifiinput.getText().toString();

                        SERVER_IP = wifiinput.getText().toString().trim();

                        Thread1 = new Thread(new Thread1());
                        Thread1.start();

                        try {
                            // Wait for thread1 to finish
                            Thread1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //obj_shared.setIsWifi(true);
                        Toast.makeText(getBaseContext(), "Connected" + SERVER_IP, Toast.LENGTH_LONG).show();
                        System.out.println("Connected to " + SERVER_IP);
                        //returnResult("Connected to " + SERVER_IP);
                        HashMap<String, Object> argumentsMap = (HashMap<String, Object>) getIntent().getSerializableExtra("arguments");
                        triggerSaleTransaction(argumentsMap);

                            // wifiStatus.setText("Wifi MPOS Device"+":"+SERVER_IP);
                            // bluetooth_status.setText("192.168.8.113");
                        //Log.d("IP", txt_deviceName);

                    /* } else {
                        Log.d("Note", "Device is connected ");
                    } */

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        returnResult("Connection failed");
                    }
                }

                });
        wifibuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        returnResult("Transaction cancelled");
                    }
            });

        wifibuilder.show();
    }

    private void initMposService() {
        mposService = MPOSService.getInstance(this);
        System.out.println("initMposService mposService: " + mposService);
        /* mposService.connectToDevice(new MPOSServiceCallback() {
            @Override
            public void onComplete(int statusCode, String message, Object result) {
                System.out.println("initMposService onComplete statusCode: " + statusCode);
                System.out.println("initMposService onComplete message: " + message);
            }

            @Override
            public void onFailed(int statusCode, String message) {
                System.out.println("initMposService onFailed statusCode: " + statusCode);
                System.out.println("initMposService onFailed message: " + message);
            }
        }); */
    }

    private void returnResult(String result) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", result);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("NeoLeapPaymentActivity onCreate");
        super.onCreate(savedInstanceState);
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    CallWebService.Login("username", "password", NeoLeapPaymentActivity.this);  
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        /* initMposService();
        handleIntent(); */
        // Check and request Bluetooth permission
        /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT);
        } else {
            // Permission already granted, initialize the MPOS service
            initMposService();
            handleIntent();
        } */
    }

    /* @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize the MPOS service
                initMposService();
                handleIntent();
            } else {
                // Permission denied, handle the error
                returnResult("Bluetooth permission denied");
            }
        }
    } */

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // check is service in not null
        if (mposService != null){
            mposService.stop();
        }
        super.onDestroy();
    }

    private void cancelTransaction() {
        mposService.cancelTransaction();
    }

    private void triggerSaleTransaction(HashMap<String, Object> arguments) {
        System.out.println("triggerSaleTransaction");
       // String currency = (String) arguments.get("currency");
        String amount = (String) arguments.get("amount");

        String xmlRequest = "<TransactionRequest>" +
                "<Command>SALE</Command>" +
                "<Amount></Amount>" +
                "<PrintFlag>01</PrintFlag>" +
                "<Phone></Phone>" +
                "<Email></Email>" +
                "<UserId></UserId>" +
                "<DeviceId></DeviceId>" +
                "<RRN></RRN>" +
                "<AuthCode></AuthCode>" +
                "<MaskedPAN></MaskedPAN>" +
                "<AdditionalData></AdditionalData>" +
                "</TransactionRequest>";
        xmlRequest = xmlRequest.replace("<Amount></Amount>", "<Amount>" + amount + "</Amount>");
    try{
        new Thread(new Thread3("\u000205018\u0002  "+amount+"'\u0001��\u00032")).start();
        // mposService.startTransaction(xmlRequest, new MPOSServiceCallback() {
        //     @Override
        //     public void onComplete(int statusCode, String message, Object result) {
        //         String xmlResponse = (String) result;
        //         System.out.println("xmlResponse: " + xmlResponse);
        //         returnResult("triggerSaleTransaction success");
        //     }

        //     @Override
        //     public void onFailed(int statusCode, String message) {
        //         System.out.println("triggerSaleTransaction statusCode: " + statusCode);
        //         System.out.println("triggerSaleTransaction message: " + message);
        //         returnResult("triggerSaleTransaction failed");
        //     }
        // }); 
    } catch (Exception e) {
        e.printStackTrace();
        returnResult("triggerSaleTransaction failed");
    }
}

    private void handleIntent() {
        String method = getIntent().getStringExtra("method");
        Object arguments = getIntent().getSerializableExtra("arguments");
        System.out.println("NeoLeapPaymentActivity method: " + method);
        System.out.println("NeoLeapPaymentActivity arguments: " + arguments);
        try{
            if (method.equals("startPayment")) {
                HashMap<String, Object> argumentsMap = (HashMap<String, Object>) getIntent().getSerializableExtra("arguments");
                triggerSaleTransaction(argumentsMap);
            } else if (method.equals("cancel")) {
                cancelTransaction();
            } else if (method.equals("connectWifi")) {
                connectWifi();
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnResult("Invalid method");
        }
    }

    String readStream(InputStream is) {
        try {
            int len = is.available();
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i;
            for (int x = 0; x < len; x++) {
                i = is.read();
                bo.write(i);

            }
            return bo.toString();
        } catch (Exception ex) {
            String error = ex.getMessage();
            return error;
        }
    }
}
