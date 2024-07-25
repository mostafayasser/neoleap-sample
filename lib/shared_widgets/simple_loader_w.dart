import 'package:flutter/material.dart';
import 'package:get/get.dart';

class SimpleLoaderW {
  static startLoading({
    bool? isSyncDesign,
    String? title,
    bool showOrdersProgress = false,
    bool isDeliverct = false,
  }) async {
    return Get.dialog(
      Scaffold(
        backgroundColor: Colors.black.withOpacity(0.5),
        body: const Center(
          child: CircularProgressIndicator(
            color: Colors.blue,
          ),
        ),
      ),
      barrierDismissible: true,
      name: "loading",
    );
  }

  static stopLoading({bool? closeOverlays}) {
    Get.back(closeOverlays: closeOverlays ?? true);
  }
}
