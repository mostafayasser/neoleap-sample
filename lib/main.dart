import 'package:flutter/material.dart';
import 'package:overlay_support/overlay_support.dart';

import 'test_android_screen.dart';

void main() {
  runApp(const MainApp());
}

class MainApp extends StatelessWidget {
  const MainApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const OverlaySupport.global(
      child: MaterialApp(
        home: TestAndroidScreen(),
      ),
    );
  }
}
