package com.example.flutter_application_3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.ActionBar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;
import com.mpos.mpossdk.api.TerminalData;
import com.mpos.mpossdk.api.TerminalStatus;
import com.mpos.mpossdk.api.data.appsettings.AppSettings;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;
import java.util.zip.Inflater;

import androidx.appcompat.app.AppCompatActivity;

public class MposBaseActivity extends AppCompatActivity {

    Menu optionMenu;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private boolean isRunning = true;
    private String txt_deviceName = "";
    String SERVER_IP;
    int SERVER_PORT = 9999;
    public static PrintWriter output;
    public static BufferedReader input;
    Thread Thread1 = null;
    DataInputStream dataInputStream = null;
    Socket socket;

    class Thread1 implements Runnable {

        @Override
        public void run() {

            try {
                Log.d("SERVER_IP", SERVER_IP);
                Log.d("SERVER_PORT", String.valueOf(SERVER_PORT));
                socket = new Socket(SERVER_IP, SERVER_PORT);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
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
            while (true) {
                try {
                    String message = null;
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    if (dataInputStream.read() != -1) {
                        message = readStream(dataInputStream);
                    }
                    if (message != null && !message.equals("AAAAAA\u0003")) {
                        int msglen = message.length();
                        Log.d("message", message.substring(7, msglen));
                        message = message.substring(7, msglen);
                        // message=message.replace("1U00854","");
                        //showTransactionResult(message);
                    } else {

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Thread3 implements Runnable {
        private String message = "";

        public Thread3(String _message) {
            this.message = _message;
        }

        @Override
        public void run() {
            output.write(message);
            output.flush();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    protected void onPause() {

        super.onPause();
        isRunning = false;

    }

    @Override
    protected void onResume() {

        super.onResume();
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
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
