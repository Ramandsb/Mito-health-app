package in.tagbin.mitohealthapp.service;

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
import android.util.Base64;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import in.tagbin.mitohealthapp.Database.ChatDatabase;
import in.tagbin.mitohealthapp.Database.ChatMessagesDatabase;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.ChatActivity;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ChatAccounts;
import in.tagbin.mitohealthapp.model.ChatLoginModel;
import in.tagbin.mitohealthapp.model.MessagesModel;

/**
 * Created by hp on 8/26/2016.
 */
public class XmppChatService extends Service {

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

    public static String RECIEVEDMSGS = "recievedmsgs";
    public static String RECEIVEROOSTER = "receiverooster";
    public static String SENTMSGS = "sentmsgs";
    public static String SENTREQUESTS = "sentrqsts";
    Intent RecivedmsgsIntent, ReceiveRooster;


    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(RecievePublish, new IntentFilter(SENTMSGS));
        registerReceiver(SendRequests, new IntentFilter(SENTREQUESTS));
        RecivedmsgsIntent = new Intent(RECIEVEDMSGS);
        ReceiveRooster = new Intent(RECEIVEROOSTER);
    }

    private BroadcastReceiver RecievePublish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username = intent.getStringExtra("user_name");
            String msg = intent.getStringExtra("message");
            Log.d("name",username+"  "+msg);
            sendMessages(username, msg);

        }
    };
    private BroadcastReceiver SendRequests = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

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
        Log.d("Service called", "///");

//        Toast.makeText(getBaseContext(),"Service Started",Toast.LENGTH_LONG).show();
        PrefManager pref = new PrefManager(getBaseContext());
        if (pref.getLoginModel() != null) {
            byte[] data = Base64.decode(pref.getLoginModel().getChat_credential(), Base64.DEFAULT);
            try {
                String text = new String(data, "UTF-8");
                ChatLoginModel chatLoginModel = JsonUtils.objectify(text, ChatLoginModel.class);
                String username = chatLoginModel.getUsername().replace("u'", "");
                username = username.replace("'", "");
                String password = chatLoginModel.getPassword().replace("u'", "");
                password = password.replace("'", "");
                String server = chatLoginModel.getServer().replace("u'", "");
                server = server.replace("'", "");
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
            Log.d("Service network", "");
        } else {
            Log.d("No Network Available", "");
        }
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void sendMessages(String user_name, String message) {
        Message msg = new Message(user_name, Message.Type.chat);
        msg.setBody(message);
        if (connection != null && connection.isConnected()) {
            connection.sendPacket(msg);
            Log.d("sent Msgs", user_name + "///" + message);
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
                        String fromName = StringUtils.parseBareAddress(message.getFrom());
                        Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody() + " from " + fromName);
                        messages.add(fromName + ":");
                        messages.add(message.getBody());
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutee = calendar.get(Calendar.MINUTE);
                        MessagesModel messagesModel = new MessagesModel(fromName, message.getBody(), String.valueOf(getCurrentTime()), "from");
                        ChatMessagesDatabase chatMessagesDatabase = new ChatMessagesDatabase(getBaseContext());
                        chatMessagesDatabase.addChat(messagesModel);
                        ChatDatabase chatDatabase = new ChatDatabase(getBaseContext());
                        String name = null;
                        if (chatDatabase.getChatUser(fromName) != null) {
                            ChatAccounts chatAccounts = chatDatabase.getChatUser(fromName);
                            name = chatAccounts.getName();
                        }
                        //dop.putCMInformation(dop,"from",String.valueOf(System.currentTimeMillis()),message.getBody(),fromName);
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                        mBuilder.setSmallIcon(R.drawable.mito_logo);
                        if (name != null)
                            mBuilder.setContentTitle(name);
                        else
                            mBuilder.setContentTitle(fromName);
                        mBuilder.setContentText(message.getBody());
                        mBuilder.setAutoCancel(true);

                        Intent resultIntent = new Intent(getBaseContext(), ChatActivity.class);
                        resultIntent.putExtra("user_name", fromName);
                        if (name != null)
                            resultIntent.putExtra("name", name);
                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                        stackBuilder.addParentStack(ChatActivity.class);
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(resultPendingIntent);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(999, mBuilder.build());
                        RecivedmsgsIntent.putExtra("user_name", fromName);
                        RecivedmsgsIntent.putExtra("message", message.getBody());
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
    public long getCurrentTime(){

        long sec = System.currentTimeMillis();
        return sec;
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
    }

    class Connector extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            ConnectionConfiguration connConfig = new ConnectionConfiguration(HOST, PORT, SERVICE);
            final XMPPConnection connection = new XMPPConnection(connConfig);
            try {
                connection.connect();
                Log.i("XMPPChatDemoActivity", "Connected to " + connection.getHost());
            } catch (XMPPException ex) {
                Log.e("XMPPChatDemoActivity", "Failed to connect to " + connection.getHost());
                Log.e("XMPPChatDemoActivity", ex.toString());
                setConnection(null);
            }
            try {
                connection.login(USERNAME, PASSWORD);
                Log.i("XMPPChatDemoActivity", "Logged in as " + connection.getUser());
                ChatDatabase chatDatabase1 = new ChatDatabase(getBaseContext());
                chatDatabase1.deleteData();
                final Presence presence = new Presence(Presence.Type.available);
                connection.sendPacket(presence);
                setConnection(connection);
                final Roster roster = connection.getRoster();
//                connection.addPacketListener(new PacketListener() {
//                    @Override
//                    public void processPacket(Packet packet) {
//                        Presence pres = (Presence) packet;
//                        if (pres.getType() != null &&
//                                pres.getType().equals(Presence.Type.subscribed)) {
//                            // user with jid pres.getFrom() rejected your request.
//                            Log.d("presence listener","presence listener");
//                            ChatDatabase chatDatabase1 = new ChatDatabase(getBaseContext());
//                            chatDatabase1.deleteData();
//                            Collection<RosterEntry> entries = roster.getEntries();
//                            makeRoosterEntry(roster,entries);
//                        }
//                    }
//                },new PacketTypeFilter(Presence.class));
                final Collection<RosterEntry> entries = roster.getEntries();
                roster.addRosterListener(new RosterListener() {
                    @Override
                    public void entriesAdded(Collection<String> collection) {
                        Log.d("rooster added", "rooster added");
                        ChatDatabase chatDatabase1 = new ChatDatabase(getBaseContext());
                        chatDatabase1.deleteData();
                        makeRoosterEntry(roster, entries);
                    }

                    @Override
                    public void entriesUpdated(Collection<String> collection) {
                        Log.d("rooster updated", "rooster updated");
                        ChatDatabase chatDatabase1 = new ChatDatabase(getBaseContext());
                        chatDatabase1.deleteData();
                        makeRoosterEntry(roster, entries);
                    }

                    @Override
                    public void entriesDeleted(Collection<String> collection) {
                        Log.d("rooster deleted", "rooster deleted");
                        ChatDatabase chatDatabase1 = new ChatDatabase(getBaseContext());
                        chatDatabase1.deleteData();
                        makeRoosterEntry(roster, entries);
                    }

                    @Override
                    public void presenceChanged(Presence presence) {
                        Log.d("presence changed", "presence changed");
                        ChatDatabase chatDatabase1 = new ChatDatabase(getBaseContext());
                        chatDatabase1.deleteData();
                        makeRoosterEntry(roster, entries);
                    }
                });
                makeRoosterEntry(roster, entries);
            } catch (Exception ex) {
                Log.e("XMPPChatDemoActivity", "Failed to log in as "
                        + USERNAME);
                Log.e("XMPPChatDemoActivity", ex.toString());
                setConnection(null);
            }
            return null;
        }
    }

    public void makeRoosterEntry(Roster roster, Collection<RosterEntry> entries) {
        ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
                new VCardProvider());
        VCard card = null;
        for (RosterEntry entry : entries) {
            card = new VCard();
            Log.d("XMPPChatDemoActivity", "--------------------------------------");
            Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
            Log.d("XMPPChatDemoActivity", "User: " + entry.getUser());
            Log.d("XMPPChatDemoActivity", "Name: " + entry.getName());
            String name = entry.getName();
            String user = entry.getUser();
            String status = "";
            if (entry.getStatus() != null) {
                status = entry.getStatus().toString();
            } else {
                status = "not available";
            }
            Log.d("XMPPChatDemoActivity", "Status: " + entry.getStatus());
            Log.d("XMPPChatDemoActivity", "Type: " + entry.getType());
            Presence entryPresence = roster.getPresence(entry.getUser());
            Log.d("XMPPChatDemoActivity", "Presence Status: " + entryPresence.getStatus());
            Log.d("XMPPChatDemoActivity", "Presence Type: " + entryPresence.getType());
            Presence.Type type = entryPresence.getType();
            String pres_type = "";
            if (type != null) {
                pres_type = type.toString();
            }
            String avatarPath = null;
            try {
                card.load(connection, entry.getUser());
                card.getExtensions();
                byte[] imgs = card.getAvatar();

                if (imgs != null) {
                    int len = imgs.length;
                    avatarPath = Base64.encodeToString(imgs, Base64.DEFAULT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (entry.getType().toString().equals("both")) {
                if (type == Presence.Type.available)
                    Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
                Log.d("XMPPChatDemoActivity", "Presence : "+ entryPresence);
                ChatAccounts chatModel = new ChatAccounts(name, user, status, entry.getType().toString(), entryPresence.toString(), pres_type, avatarPath);
                ChatDatabase chatDatabase = new ChatDatabase(getBaseContext());
                chatDatabase.addChat(chatModel);
                sendBroadcast(ReceiveRooster);
            }

        }
    }
}
