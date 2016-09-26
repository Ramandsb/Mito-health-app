package in.tagbin.mitohealthapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import in.tagbin.mitohealthapp.Database.ChatDatabase;
import in.tagbin.mitohealthapp.Database.ChatMessagesDatabase;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ChatAccounts;
import in.tagbin.mitohealthapp.model.ChatLoginModel;
import in.tagbin.mitohealthapp.model.MessagesModel;

/**
 * Created by hp on 8/26/2016.
 */
public class XamppChatService extends Service {

    public static String HOST = "chat2.eazevent.in";
    public static final int PORT = 5222;
    public static String SERVICE = "chat2.eazevent.in";
    public static String USERNAME = "ankit";
    public static String PASSWORD = "1234";

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
    public static String SENTREQUESTS="sentrqsts";
    Intent RecivedmsgsIntent;


    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(RecievePublish, new IntentFilter(SENTMSGS));
        registerReceiver(SendRequests,new IntentFilter(SENTREQUESTS));
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
    private BroadcastReceiver SendRequests = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String username=intent.getStringExtra("user_name");
//            String msg=intent.getStringExtra("message");
            //sendMessages(username,msg);

        }
    };

            private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service called","///");

//        Toast.makeText(getBaseContext(),"Service Started",Toast.LENGTH_LONG).show();
        PrefManager pref = new PrefManager(getBaseContext());
        if (pref.getLoginModel() != null){
            byte[] data = Base64.decode(pref.getLoginModel().getChat_credential(), Base64.DEFAULT);
            try {
                String text = new String(data, "UTF-8");
                ChatLoginModel chatLoginModel = JsonUtils.objectify(text,ChatLoginModel.class);
                String username = chatLoginModel.getUsername().replace("u'","");
                username = username.replace("'","");
                String password = chatLoginModel.getPassword().replace("u'","");
                password = password.replace("'","");
                String server = chatLoginModel.getServer().replace("u'","");
                server = server.replace("'","");
                USERNAME = username;
                PASSWORD = password;
                SERVICE = server;
                HOST = server;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
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
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutee = calendar.get(Calendar.MINUTE);
                        MessagesModel messagesModel = new MessagesModel(fromName,message.getBody(),updateTime1(hour,minutee),"from");
                        ChatMessagesDatabase chatMessagesDatabase = new ChatMessagesDatabase(getBaseContext());
                        chatMessagesDatabase.addChat(messagesModel);
                        //dop.putCMInformation(dop,"from",String.valueOf(System.currentTimeMillis()),message.getBody(),fromName);
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
    private String updateTime1(int hours, int mins) {
        if (mins >= 60) {
            //hour = hours + 1;
            hours = hours + 1;
            //minute1 = mins - 60;
            mins = mins - 60;
        } else {
            hours = hours;
            mins = mins;
        }

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(mins).append(" ").append(timeSet).toString();

        return aTime;
        //time.setText(aTime);
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
                ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
                        new VCardProvider());
                VCard card = null;
                for (RosterEntry entry : entries) {
                    card = new VCard();
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
                    String avatarPath = null;
                    try {
                        card.load(connection,entry.getUser());
                        card.getExtensions();
                        byte[] imgs = card.getAvatar();

                        if (imgs != null) {
                            int len = imgs.length;
                            Bitmap img = BitmapFactory.decodeByteArray(imgs, 0, len);
                            //avatarPath = savaAvatar()
                            avatarPath = Base64.encodeToString(imgs, Base64.DEFAULT);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (type == Presence.Type.available)
                        Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
                    Log.d("XMPPChatDemoActivity", "Presence : "
                            + entryPresence);
                    ChatAccounts chatModel = new ChatAccounts(name,user,status,entry.getType().toString(),entryPresence.toString(),pres_type,avatarPath);
                    ChatDatabase chatDatabase = new ChatDatabase(getBaseContext());
                    chatDatabase.addChat(chatModel);
//                    try {
//                        dop.putChatInformation(dop,name,user,status,"null","not def",pres_type);
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }


                }
//                Presence presence1 = new Presence(Presence.Type.subscribe);
//                connection.sendPacket(presence1);
//                setConnection(connection);
//
//                Roster roster1 = connection.getRoster();
//                Collection<RosterEntry> entries1 = roster1.getEntries();
//                for (RosterEntry entry : entries1) {
//                    Log.d("XMPPChatDemoActivity",
//                            "--------------------------------------");
//                    Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
//                    Log.d("XMPPChatDemoActivity",
//                            "User: " + entry.getUser());
//                    Log.d("XMPPChatDemoActivity",
//                            "Name: " + entry.getName());
//
//                    String name = entry.getName();
//                    String user = entry.getUser();
//                    String status="";
//                    if (entry.getStatus()!=null){
//                        status = entry.getStatus().toString();
//                    }else{
//                        status="not available";
//                    }
//
//                    String typee="null";
////                    if (entry.getType()!=null){
////                        typee = entry.getStatus().toString();
////                    }else{
////                        typee="not available";
////                    }
//
//
//                    Log.d("XMPPChatDemoActivity",
//                            "Status: " + entry.getStatus());
//                    Log.d("XMPPChatDemoActivity",
//                            "Type: " + entry.getType());
//                    Presence entryPresence = roster1.getPresence(entry
//                            .getUser());
//
//                    Log.d("XMPPChatDemoActivity", "Presence Status: "
//                            + entryPresence.getStatus());
//
//
//                    Log.d("XMPPChatDemoActivity", "Presence Type: "
//                            + entryPresence.getType());
//                    Presence.Type type = entryPresence.getType();
//                    String pres_type="";
//
//                    if (type!=null){
//
//                        pres_type=type.toString();
//                    }
//                    if (type == Presence.Type.available)
//                        Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
//                    Log.d("XMPPChatDemoActivity", "Presence : "
//                            + entryPresence);
//                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
//                    mBuilder.setSmallIcon(R.drawable.mito_logo);
//                    mBuilder.setContentTitle(name);
//                    mBuilder.setContentText(name +" sends you a request");
//                    mBuilder.setAutoCancel(true);
//
//                    Intent resultIntent = new Intent(getBaseContext(), BinderActivity.class);
//                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//                    stackBuilder.addParentStack(BinderActivity.class);
//
//// Adds the Intent that starts the Activity to the top of the stack
//                    stackBuilder.addNextIntent(resultIntent);
//                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//                    mBuilder.setContentIntent(resultPendingIntent);
//                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//// notificationID allows you to update the notification later on.
//                    mNotificationManager.notify(999, mBuilder.build());
//                    ChatAccounts chatModel = new ChatAccounts(name,user,status,entry.getType().toString(),entryPresence.toString(),pres_type);
//                    ChatRequestsDatabase chatDatabase = new ChatRequestsDatabase(getBaseContext());
//                    chatDatabase.addChat(chatModel);
////                    try {
////                        dop.putChatInformation(dop,name,user,status,"null","not def",pres_type);
////
////                    }catch (Exception e){
////                        e.printStackTrace();
////                    }
//
//
//                }

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
