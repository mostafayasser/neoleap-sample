package com.example.flutter_application_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;
import com.mpos.mpossdk.api.TerminalData;
import com.mpos.mpossdk.api.TerminalStatus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by m.alibraheem on 07/01/2018.
 */

public class SharedClass {
    String TID = "";
    String TSNo = "";
    public static boolean isWifi ;

    public String getCurrentDateTime(Activity activity) {
        String currentDateTime = "";
        try {
            Date today= null;
            Calendar calendar = java.util.Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            today = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            currentDateTime = formatter.format(today);
        } catch (Exception e) {

        }
        return currentDateTime;
    }
    public String getLastSexMonthDateTime(Activity activity) {
        String currentDateTime = "";
        try {
            Date today= null;
            Calendar calendar = java.util.Calendar.getInstance();
            calendar.add(Calendar.MONTH, -6);
            today = calendar.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            currentDateTime = formatter.format(today);
        } catch (Exception e) {

        }
        return currentDateTime;
    }

    public long getIDate(Activity activity) {
        String[] date = new String[3];
        long currentIDate = 0;
        String scurrentIDate = "";
        String currentDate = getCurrentDateTime(activity).substring(0, 10);
        date = currentDate.split("/");
        scurrentIDate = date[2] + date[1] + date[0] + "000000";

        return Long.parseLong(scurrentIDate);
    }
    public long getLastSexMonthIDate(Activity activity) {
        String[] date = new String[3];
        long currentIDate = 0;
        String scurrentIDate = "";
        String currentDate = getLastSexMonthDateTime(activity).substring(0, 10);
        date = currentDate.split("/");
        scurrentIDate = date[2] + date[1] + date[0] + "000000";

        return Long.parseLong(scurrentIDate);
    }



    public Transaction_Result showTransactionResult(String transactionResponse, Activity activity) {

        HashMap<String, String> result = MPOSService.getInstance(activity).parseTransactionResponse(transactionResponse);

        String PANAR = "";
        String curPANAR = "";
        Transaction_Result transaction_result = new Transaction_Result();
        String terminalStatusCode = result.get("TerminalStatusCode");
        if (terminalStatusCode != null) {
            TerminalStatus status = TerminalStatus.getStatus(terminalStatusCode);
            transaction_result.setterminalStatusCode(terminalStatusCode);
            String sysDate = getCurrentDateTime(activity);

            transaction_result.setDateEN(sysDate);
            transaction_result.setDateAR(getArabicNumbers(sysDate));

            transaction_result.setamount(result.get("Amount"));
            transaction_result.setamountAR(getArabicNumbers(result.get("Amount")));
            transaction_result.setperformanceStartDateTime(result.get("PerformanceStartDateTime"));
            transaction_result.setapprovalCode(result.get("ApprovalCode"));
            transaction_result.setrrn(result.get("RRN"));
            transaction_result.setresponseCode(result.get("ResponseCode"));
// from here
            transaction_result.setRetailerNameEng(result.get("RetailerRetailerNameEng"));
            // transaction_result.setRetailerNameArb ( result.get("RetailerNameArb")); messing
            transaction_result.setBankId(result.get("BankId"));
            transaction_result.setMerchantID(result.get("MerchantID"));
            transaction_result.setTerminalID(result.get("TerminalID"));
            transaction_result.setMCC(result.get("MCC"));
            transaction_result.setSTAN(result.get("STAN"));
            transaction_result.setVersion(result.get("Version"));
            transaction_result.setApplicationLabelArabic(result.get("ApplicationLabelArabic"));
            transaction_result.setApplicationLabelEnglish(result.get("ApplicationLabelEnglish"));
            transaction_result.setTransactionTypeEnglish(result.get("TransactionTypeEnglish"));
            transaction_result.setTransactionTypeArabic(result.get("TransactionTypeArabic"));
            transaction_result.setPAN(result.get("PAN"));

            curPANAR = result.get("PAN");
            PANAR = getArabicNumbers(curPANAR.substring(curPANAR.length() - 4, curPANAR.length()));
            PANAR += "******";
            PANAR += getArabicNumbers(curPANAR.substring(0, 6));

            transaction_result.setPAAR(PANAR);

            transaction_result.setCardExpiryDate(result.get("CardExpiryDate"));
            transaction_result.setCardExpiryDateAr(getArabicNumbers(result.get("CardExpiryDate")));
            transaction_result.setCVR(result.get("CVR"));
            transaction_result.setAC(result.get("AC"));
            transaction_result.setTIS(result.get("TSI"));
            transaction_result.setTVR(result.get("TVR"));
            transaction_result.setACI(result.get("ACI"));

            transaction_result.setRetaileraddress_eng_1(result.get("Retaileraddress_eng_1"));
            transaction_result.setRetaileraddress_arb_1(result.get("Retaileraddress_arb_1"));
            transaction_result.setRetailerNameArb(result.get("RetailerRetailerNameArb"));
            transaction_result.setPosEntryMode(result.get("PosEntryMode"));
            transaction_result.setResultEN(result.get("ResultEnglish"));
            transaction_result.setResultAR(result.get("ResultArabic"));
            String re = result.get("ResultEnglish");
            String check = "APPROVED";
            if (re.equals(check)) {
                transaction_result.setCardholderVerificationEnglish(result.get("CardholderVerificationEnglish"));
                transaction_result.setCardholderVerificationArabic(getArabicNumbers(result.get("CardholderVerificationArabic")));
            } else {
                transaction_result.setCardholderVerificationEnglish(result.get("ResultResponseMessageEnglish"));
                transaction_result.setCardholderVerificationArabic(getArabicNumbers(result.get("ResultResponseMessageArabic")));
            }


            if (transaction_result.performanceStartDateTime != null && transaction_result.performanceStartDateTime.length() >= 8) {
                String date = result.get("PerformanceStartDateTime");

                try {
                    String day = date.substring(0, 2);
                    String month = date.substring(2, 4);
                    String year = date.substring(4, 8);

                    transaction_result.setTRANSACTION_DATE(day + "/" + month + "/" + year);
                    transaction_result.setTRANSACTION_DATE_AR(getArabicNumbers(year) + "/" + getArabicNumbers(month) + "/" + getArabicNumbers(day));

                    String hour = date.substring(8, 10);
                    String minute = date.substring(10, 12);
                    String seconds = date.substring(12, 14);


                    transaction_result.setSTime(hour + ":" + minute + ":" + seconds);
                    transaction_result.setSTimeAR(getArabicNumbers(hour) + ":" + getArabicNumbers(minute) + ":" + getArabicNumbers(seconds));

                } catch (Exception e) {
                    Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
                }

            } else {
                transaction_result.setTRANSACTION_DATE("");
                transaction_result.setTRANSACTION_DATE_AR("");
                transaction_result.setSTime("");
                transaction_result.setSTimeAR("");
            }
        }
        return transaction_result;
    }

    public String getArabicNumbers(String Numbers) {
        String ArabicNumber = Numbers;
        try {

            ArabicNumber = ArabicNumber.replace('0', '٠');
            ArabicNumber = ArabicNumber.replace('1', '١');
            ArabicNumber = ArabicNumber.replace('2', '٢');
            ArabicNumber = ArabicNumber.replace('3', '٣');
            ArabicNumber = ArabicNumber.replace('4', '٤');
            ArabicNumber = ArabicNumber.replace('5', '٥');
            ArabicNumber = ArabicNumber.replace('6', '٦');
            ArabicNumber = ArabicNumber.replace('7', '٧');
            ArabicNumber = ArabicNumber.replace('8', '٨');
            ArabicNumber = ArabicNumber.replace('9', '٩');
        } catch (Exception e) {

        }

        return ArabicNumber;
    }

    public String getEnglishNumbers(String Numbers) {
        String ArabicNumber = Numbers;
        try {

            ArabicNumber = ArabicNumber.replace('٠', '0');
            ArabicNumber = ArabicNumber.replace('١', '1');
            ArabicNumber = ArabicNumber.replace('٢', '2');
            ArabicNumber = ArabicNumber.replace('٣', '3');
            ArabicNumber = ArabicNumber.replace('٤', '4');
            ArabicNumber = ArabicNumber.replace('٥', '5');
            ArabicNumber = ArabicNumber.replace('٦', '6');
            ArabicNumber = ArabicNumber.replace('٧', '7');
            ArabicNumber = ArabicNumber.replace('٨', '8');
            ArabicNumber = ArabicNumber.replace('٩', '9');
        } catch (Exception e) {

        }

        return ArabicNumber;
    }


    public boolean checkInternetConnection(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void setTerminalId(Activity activity) {
        try {
            final DeviceInfo deviceInfo = new DeviceInfo();
            MPOSService.getInstance(activity).getTerminalData(new MPOSServiceCallback() {
                @Override
                public void onComplete(int status, String message, Object result) {

                    TerminalData retailerData = (TerminalData) result;

                    deviceInfo.setTerminalId(retailerData.getTID());

                }

                @Override
                public void onFailed(int i, String s) {

                }
            });
        } catch (Exception ex) {

        }
    }
    public void setIsWifi(boolean _isWifi) {
        try {
            isWifi = _isWifi;
        } catch (Exception ex) {

        }
    }
    public boolean getIsWifi() {
        return isWifi;
    }



}



