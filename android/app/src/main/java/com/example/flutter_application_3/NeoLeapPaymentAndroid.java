package com.example.flutter_application_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import java.util.HashMap;
import java.util.Map;

public class NeoLeapPaymentAndroid {
    private static final String CHANNEL = "com.example.flutter_application_3/neoleap-android";

    private final ActivityResultLauncher<Intent> activityResultLauncher;
    private MethodChannel.Result pendingResult;  // To store the result callback

    public NeoLeapPaymentAndroid(@NonNull Activity activity, @NonNull FlutterEngine flutterEngine) {
        Context context = activity.getApplicationContext();

        // Register for activity result
        activityResultLauncher = ((FragmentActivity) activity).registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        // Get result as String from data
                        String resultData = data.getStringExtra("result");

                        // Send result back to Flutter
                        if (pendingResult != null) {
                            pendingResult.success(resultData);
                            pendingResult = null;  // Reset pending result
                        }
                    }
                } else {
                    // Handle result canceled or error
                    if (pendingResult != null) {
                        pendingResult.error("RESULT_CANCELED", "The result was canceled", null);
                        pendingResult = null;  // Reset pending result
                    }
                }
            });

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
            .setMethodCallHandler((call, result) -> {
                try{
                    System.out.println("Method call: " + call.method);
                    System.out.println("Method arguments: " + call.arguments);
                    //if (call.method.equals("startPayment")) {
                        // Save the result callback
                        pendingResult = result;

                        // Prepare and launch the intent
                        Intent intent = new Intent(context, NeoLeapPaymentActivity.class);
                        intent.putExtra("method", call.method);
                        if(call.arguments != null){
                            // Convert the Map<String, dynamic> to a HashMap
                            HashMap<String, Object> hashMap = new HashMap<>();
                            Map<String, Object> arguments = (Map<String, Object>) call.arguments;
                            for (Map.Entry<String, Object> entry : arguments.entrySet()) {
                                // Handle different types of values here
                                if (entry.getValue() instanceof String) {
                                    hashMap.put(entry.getKey(), (String) entry.getValue());
                                } else if (entry.getValue() instanceof Integer) {
                                    hashMap.put(entry.getKey(), (Integer) entry.getValue());
                                }
                            }
                            intent.putExtra("arguments", hashMap);
                        }

                        // Launch the activity for result
                        activityResultLauncher.launch(intent);
                    /* } else {
                        result.notImplemented();
                    } */
                }  catch(Exception e){
                    System.out.println("Error: " + e.getMessage());
                    result.error("UNEXPECTED_ERROR", e.getMessage(), null);
                }
            });
    }
}
