package in.tagbin.mitohealthapp.receiver;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by aasaqt on 3/9/16.
 */
public class GcmMessageHandler extends FirebaseMessagingService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private static final String TAG = GcmMessageHandler.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,remoteMessage.toString());
//        String message = data.getString("message");
//        String id = data.getString("id");
//        String visit_id = data.getString("visit_id");
//        String title = data.getString("title");
//        String restName = data.getString("rest_name");
//        String gspot= data.getString("gspot_id");
//        String date = data.getString("created_at");
//        String short_address = data.getString("short_address");
//        String image_url = data.getString("img_url");
//        String vtype = data.getString("vtype");
//        String uid = data.getString("uid");
//        String url = data.getString("url");

    }
}
