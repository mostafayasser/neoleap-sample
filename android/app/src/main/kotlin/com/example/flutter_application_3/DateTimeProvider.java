package com.example.flutter_application_3;;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeProvider {
    private static final String CHANNEL = "samples.flutter.dev/datetime";

    public DateTimeProvider(FlutterEngine flutterEngine) {
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if (call.method.equals("getCurrentDateTime")) {
                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                String formatDateTime = now.format(formatter);
                                result.success(formatDateTime);
                            } else {
                                result.notImplemented();
                            }
                        }
                );
    }
}