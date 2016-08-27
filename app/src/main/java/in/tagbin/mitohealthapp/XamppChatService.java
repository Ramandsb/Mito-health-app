package in.tagbin.mitohealthapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;

/**
 * Created by hp on 8/26/2016.
 */
public class XamppChatService extends Service {

    public static final String HOST = "chat.eazevent.in";
    public static final int PORT = 5222;
    public static final String SERVICE = "chat.eazevent.in";
    public static final String USERNAME = "ankit";
    public static final String PASSWORD = "1234";
    DatabaseOperations dop;

    private XMPPConnection connection;
    private ArrayList<String> messages = new ArrayList<String>();
    private Handler mHandler = new Handler();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static String RECIEVEDMSGS="recievedmsgs";
    public static String SENTMSGS="sentmsgs";
    Intent RecivedmsgsIntent;


    @Override
    public void onCreate() {
        super.onCreate();
        dop=new DatabaseOperations(getApplicationContext());
        registerReceiver(RecievePublish, new IntentFilter(SENTMSGS));
        RecivedmsgsIntent= new Intent(RECIEVEDMSGS);
    }

    private BroadcastReceiver RecievePublish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username=intent.getStringExtra("user_name");
            String msg=intent.getStringExtra("message");
            sendMessages(username,msg);

        }
    };


            private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Log.d("is connected",activeNetworkInfo.isConnected()+"///");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service called","///");

        Toast.makeText(getBaseContext(),"Service Started",Toast.LENGTH_LONG).show();

        if (isNetworkAvailable()) {
            Connector asyncTask = new Connector();
            asyncTask.execute();
            Log.d("Service network","");
        }else {
            Log.d("No Network Available","");
        }
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public  void sendMessages(String user_name,String message){
        Message msg = new Message(user_name, Message.Type.chat);
        msg.setBody(message);
        if (connection != null && connection.isConnected()) {

            connection.sendPacket(msg);
            Log.d("sent Msgs",user_name+"///"+message);
//            pojo.setMessages(text);
//            textMessage.setText("");
////                    messages.add(connection.getUser() + ":");
////                    messages.add(text);
////
////                    setListAdapter();
//            customPojos_list.add(pojo);
//            adapter.setListContent(customPojos_list);

        }
    }
    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = StringUtils.parseBareAddress(message
                                .getFrom());
                        Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
                                + " from " + fromName );
                        messages.add(fromName + ":");
                        messages.add(message.getBody());
                        dop.putCMInformation(dop,"from",String.valueOf(System.currentTimeMillis()),message.getBody(),fromName);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                        mBuilder.setSmallIcon(R.drawable.mito_logo);
                        mBuilder.setContentTitle(fromName);
                        mBuilder.setContentText(message.getBody());
                        mBuilder.setAutoCancel(true);

                        Intent resultIntent = new Intent(getBaseContext(), ChatActivity.class);
                        resultIntent.putExtra("user_name",fromName);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                        stackBuilder.addParentStack(ChatActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
                        mNotificationManager.notify(999, mBuilder.build());
                        RecivedmsgsIntent.putExtra("user_name",fromName);
                        RecivedmsgsIntent.putExtra("message",message.getBody());
                        sendBroadcast(RecivedmsgsIntent);

                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {

//                                XMPPChatDemoActivity xmppConnection=new XMPPChatDemoActivity();
//                                xmppConnection.setListAdapter();
                            }
                        });
                    }
                }
            }, filter);
        }
    }
    class Connector extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {

            ConnectionConfiguration connConfig = new ConnectionConfiguration(
                    HOST, PORT, SERVICE);
            XMPPConnection connection = new XMPPConnection(connConfig);


            try {
                connection.connect();
                Log.i("XMPPChatDemoActivity",
                        "Connected to " + connection.getHost());
            } catch (XMPPException ex) {
                Log.e("XMPPChatDemoActivity", "Failed to connect to "
                        + connection.getHost());
                Log.e("XMPPChatDemoActivity", ex.toString());
                setConnection(null);
            }
            try {
                // SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                connection.login(USERNAME, PASSWORD);
                Log.i("XMPPChatDemoActivity",
                        "Logged in as " + connection.getUser());

                // Set the status to available
                Presence presence = new Presence(Presence.Type.available);
                connection.sendPacket(presence);
                setConnection(connection);

                Roster roster = connection.getRoster();
                Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {
                    Log.d("XMPPChatDemoActivity",
                            "--------------------------------------");
                    Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
                    Log.d("XMPPChatDemoActivity",
                            "User: " + entry.getUser());
                    Log.d("XMPPChatDemoActivity",
                            "Name: " + entry.getName());

                    String name = entry.getName();
                    String user = entry.getUser();
                    String status="";
                    if (entry.getStatus()!=null){
                        status = entry.getStatus().toString();
                    }else{
                        status="not available";
                    }

                    String typee="null";
//                    if (entry.getType()!=null){
//                        typee = entry.getStatus().toString();
//                    }else{
//                        typee="not available";
//                    }



                    Log.d("XMPPChatDemoActivity",
                            "Status: " + entry.getStatus());
                    Log.d("XMPPChatDemoActivity",
                            "Type: " + entry.getType());
                    Presence entryPresence = roster.getPresence(entry
                            .getUser());

                    Log.d("XMPPChatDemoActivity", "Presence Status: "
                            + entryPresence.getStatus());


                    Log.d("XMPPChatDemoActivity", "Presence Type: "
                            + entryPresence.getType());
                    Presence.Type type = entryPresence.getType();
                    String pres_type="";

                    if (type!=null){

                        pres_type=type.toString();
                    }
                    if (type == Presence.Type.available)
                        Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
                    Log.d("XMPPChatDemoActivity", "Presence : "
                            + entryPresence);

                    try {
                        dop.putChatInformation(dop,name,user,status,"null","not def",pres_type);

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }

//
            } catch (XMPPException ex) {
                Log.e("XMPPChatDemoActivity", "Failed to log in as "
                        + USERNAME);
                Log.e("XMPPChatDemoActivity", ex.toString());
                setConnection(null);
            }

            //
            // dialog.dismiss();
            return null;
        }
    }
}