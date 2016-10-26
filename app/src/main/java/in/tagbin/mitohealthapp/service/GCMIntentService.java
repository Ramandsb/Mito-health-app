package in.tagbin.mitohealthapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.PrefManager;

/**
 * Created by aasaqt on 18/2/16.
 */
public class GCMIntentService extends IntentService implements RequestListener {
    private static final String TAG = "RegIntentService";
    PrefManager pref;
    private String android_id;

    public GCMIntentService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        android_id= Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        InstanceID instanceID = InstanceID.getInstance(this);
        pref = new PrefManager(getApplicationContext());
        String senderId = "56770976470";
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.d("token",token);
            //pref.setGcmToken(token);
            // pass along this data
            sendRegistrationToServer(token);
        } catch (IOException e) {
            e.printStackTrace();
            //pref.setSentTokenToServer(false);
        }
    }
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        PrefManager pref = new PrefManager(getApplicationContext());
        String auth = null;
//        if (pref.getCurrentUserAsObject() != null){
//            ResponseModel user = pref.getCurrentUserAsObject();
//            auth = user.getAuth_token();
//        }
        String version_name = null;
        try {
            version_name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Controller.sendToken(getApplicationContext(),token,this);
    }

    @Override
    public void onRequestStarted() {

    }

    @Override
    public void onRequestCompleted(Object responseObject) {
        pref.sendTokenToServer(true);
    }

    @Override
    public void onRequestError(int errorCode, String message) {
        Log.d(TAG,message);
    }
}
