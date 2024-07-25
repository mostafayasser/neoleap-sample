import 'package:flutter/material.dart';
import 'package:overlay_support/overlay_support.dart';

showToast(String msg, {bool isError = false, int milliseconds = 500}) async {
  showSimpleNotification(
    Text(
      msg,
      textAlign: TextAlign.center,
      style: const TextStyle(
        fontSize: 20,
        color: Colors.white,
        fontWeight: FontWeight.bold,
      ),
    ),
    duration: isError ? null : Duration(milliseconds: milliseconds),
    background: !isError ? Colors.green.shade400 : Colors.red,
  );
}
