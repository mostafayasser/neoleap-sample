package com.example.flutter_application_3;

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: FlutterFragmentActivity() {
    private lateinit var neoLeapPaymentAndroid: NeoLeapPaymentAndroid

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        DateTimeProvider(flutterEngine)
        neoLeapPaymentAndroid = NeoLeapPaymentAndroid(this, flutterEngine)
    }
}
