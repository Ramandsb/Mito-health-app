package in.tagbin.mitohealthapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.facebook.stetho.inspector.protocol.module.Database;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
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
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;

public class ChatActivity extends AppCompatActivity {
    public static final String HOST = "chat.eazevent.in";
    public static final int PORT = 5222;
    public static final String SERVICE = "chat.eazevent.in";
    public static final String USERNAME = "ankit";
    public static final String PASSWORD = "1234";
    RecyclerView recyclerView;
    ChatActivityAdapter adapter;
    private XMPPConnection connection;
    private ArrayList<String> messages = new ArrayList<String>();
    private Handler mHandler = new Handler();
    private ArrayList<CustomPojo> customPojos_list= new ArrayList<>();
    private ArrayList<CustomPojo> list= new ArrayList<>();
    String user="rman";
    private EditText textMessage;
    Intent SendMessages;
    DatabaseOperations dop;



    String user_name="",user_content="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //customDialog();
        registerReceiver(Recievemsgs, new IntentFilter(XamppChatService.RECIEVEDMSGS));
        SendMessages= new Intent(XamppChatService.SENTMSGS);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter=new ChatActivityAdapter(this);

        dop=new DatabaseOperations(this);


        textMessage = (EditText) this.findViewById(R.id.chatET);
        user_name=getIntent().getExtras().getString("user_name");
        getSupportActionBar().setTitle(user_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list=dop.getCMInformation(dop,user_name);
        if (list.isEmpty()){

        }else {
            customPojos_list=list;
            adapter = new ChatActivityAdapter(this,customPojos_list);
            recyclerView.setAdapter(adapter);
        }
        toolbar.setTitle(user_name);
//        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);


        // Set a listener to send a chat text message
        final Button send = (Button) this.findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String text = textMessage.getText().toString();
                CustomPojo pojo = new CustomPojo();
                String time= String.valueOf(System.currentTimeMillis());
                pojo.setTime_mess(time);
                pojo.setSource("to");
                pojo.setUser_id(user_name);
                pojo.setMessages(text);

                customPojos_list.add(pojo);
                //adapter.setListContent(customPojos_list);
                adapter.notifyDataSetChanged();
                textMessage.setText(null);
                recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                SendMessages.putExtra("user_name",user_name);
                SendMessages.putExtra("message",text);
                sendBroadcast(SendMessages);

                dop.putCMInformation(dop,"to",time,text,user_name);

                }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (connection != null)
                connection.disconnect();
        } catch (Exception e) {

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private BroadcastReceiver Recievemsgs = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username=intent.getStringExtra("user_name");
            String msg=intent.getStringExtra("message");
            String time= String.valueOf(System.currentTimeMillis());
            CustomPojo pojo = new CustomPojo();
            pojo.setTime_mess(time);
            pojo.setSource("from");
            pojo.setUser_id(username);
            pojo.setMessages(msg);
            dop.putCMInformation(dop,"from",time,msg,user_name);
            adapter.notifyDataSetChanged();
            customPojos_list.add(pojo);
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);

            Log.d("Message Recieved","From"+ username+" Message :"+msg);


        }
    };
//    private boolean isNetworkAvailable() {
//        showDialog();
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        Log.d("Ramandeep",activeNetworkInfo.isConnected()+"///");
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//    public void setConnection(XMPPConnection connection) {
//        this.connection = connection;
//        if (connection != null) {
//            // Add a packet listener to get messages sent to us
//            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
//            connection.addPacketListener(new PacketListener() {
//                @Override
//                public void processPacket(Packet packet) {
//                    Message message = (Message) packet;
//                    CustomPojo pojo = new CustomPojo();
//                    if (message.getBody() != null) {
//                        String fromName = StringUtils.parseBareAddress(message
//                                .getFrom());
//                        Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
//                                + " from " + fromName );
//
//                        pojo.setMessages(message.getBody());
//                        messages.add(fromName + ":");
//                        messages.add(message.getBody());
//                        customPojos_list.add(pojo);
//                        adapter.setListContent(customPojos_list);
//                        // Add the incoming message to the list view
//                        mHandler.post(new Runnable() {
//                            public void run() {
////setListAdapter();
//                            }
//                        });
//                    }
//                }
//            }, filter);
//        }
//    }


}
