package in.tagbin.mitohealthapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.tagbin.mitohealthapp.helper.Config;

/**
 * Created by chetan on 23/11/16.
 */

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);
                    if (!senderAddress.toLowerCase().contains(Config.SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    String verificationCode = getVerificationCode(message);
                    Log.e(TAG, "OTP received: " + verificationCode);
                    if(verificationCode !=null && isInteger(verificationCode)){
//                        PrefManager pref = new PrefManager(context);
//                        if (pref.getCurrentUserAsObject() == null) {
//                            Intent hhtpIntent = new Intent(context, HttpService.class);
//                            hhtpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            hhtpIntent.putExtra("otp", verificationCode);
//                            context.startService(hhtpIntent);
//                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
    private String getVerificationCode(String message) {
        String code = null;
        //String delimiter = "\d+";
        Pattern pattern = Pattern.compile("\\s(\\d{4,6})\\.");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find())
        {
            code = matcher.group(1);
        }
        /*int index = message.indexOf(Config.OTP_DELIMITER);
        int end = message.indexOf(Config.OTP_END);
        if (index != -1) {
            int start = index + 3;
            int length = 6;
            code = message.substring(start, start+length);
            return code;
        }*/
        return code;
    }
}
