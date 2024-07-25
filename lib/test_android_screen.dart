import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'shared_widgets/toast.dart';

class TestAndroidScreen extends StatefulWidget {
  const TestAndroidScreen({super.key});

  @override
  State<TestAndroidScreen> createState() => _TestAndroidScreenState();
}

class _TestAndroidScreenState extends State<TestAndroidScreen> {
  TextEditingController amountController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              width: 200,
              child: TextFormField(
                controller: amountController,
              ),
            ),
            /* TextButton(
              onPressed: () async {
                const platform = MethodChannel('samples.flutter.dev/datetime');
                String dateTime;
                try {
                  final String result =
                      await platform.invokeMethod('getCurrentDateTime');
                  dateTime = 'Current date and time: $result.';
                } on PlatformException catch (e) {
                  dateTime = "Failed to get date and time: '${e.message}'.";
                }

                debugPrint(dateTime);
              },
              child: const Text('Get Date Time'),
            ), */
            TextButton(
              onPressed: () async {
                const platform = MethodChannel(
                    'com.example.flutter_application_3/neoleap-android');
                try {
                  //SimpleLoaderW.startLoading();
                  double amount = double.tryParse(amountController.text) ?? 0;
                  int amountInt = (amount * 100).toInt();
                  var result = await platform.invokeMethod(
                    'connectWifi',
                    {
                      'amount': amountInt.toString(),
                    },
                  );
                  //SimpleLoaderW.stopLoading();
                  showToast("result $result", milliseconds: 10000);
                } on PlatformException catch (e) {
                  //SimpleLoaderW.stopLoading();
                  showToast(
                    "Failed to pay: '${e.message}'.",
                    milliseconds: 10000,
                    isError: true,
                  );
                }
              },
              child: const Text('Connect'),
            ),
            /* TextButton(
              onPressed: () async {
                /* const platform = MethodChannel('samples.flutter.dev/datetime');
                String dateTime;
                try {
                  final String result =
                      await platform.invokeMethod('getCurrentDateTime');
                  dateTime = 'Current date and time: $result.';
                } on PlatformException catch (e) {
                  dateTime = "Failed to get date and time: '${e.message}'.";
                }
            
                print(dateTime); */
                //requestBluetoothPermission();
                const platform =
                    MethodChannel('com.example.flutter_application_3/neoleap-android');
                try {
                  SimpleLoaderW.startLoading();
                  double amount = double.tryParse(amountController.text) ?? 0;
                  int amountInt = (amount * 100).toInt();
                  var result = await platform.invokeMethod(
                    'startPayment',
                    {
                      'amount': amountInt.toString(),
                    },
                  );
                  SimpleLoaderW.stopLoading();
                  showToast("result $result", milliseconds: 5000);
                } on PlatformException catch (e) {
                  SimpleLoaderW.stopLoading();
                  showToast(
                    "Failed to pay: '${e.message}'.",
                    milliseconds: 5000,
                    isError: true,
                  );
                }
              },
              child: Text('Pay'),
            ), */
          ],
        ),
      ),
    );
  }
}
