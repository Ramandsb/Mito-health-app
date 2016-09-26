package in.tagbin.mitohealthapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.Database.ChatMessagesDatabase;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.model.MessagesModel;

public class ChatActivity extends AppCompatActivity {
    public static final String HOST = "chat.eazevent.in";
    public static final int PORT = 5222;
    public static final String SERVICE = "chat.eazevent.in";
    public static final String USERNAME = "ankit";
    public static final String PASSWORD = "1234";
    RecyclerView recyclerView;
    ChatActivityAdapter adapter;
    private ArrayList<MessagesModel> customPojos_list= new ArrayList<>();
    private ArrayList<MessagesModel> list= new ArrayList<>();
    String user="rman";
    private EditText textMessage;
    Intent SendMessages;



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
        textMessage = (EditText) this.findViewById(R.id.chatET);
        user_name=getIntent().getExtras().getString("user_name");
        getSupportActionBar().setTitle(user_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ChatMessagesDatabase chatMessagesDatabase = new ChatMessagesDatabase(this);
        list=chatMessagesDatabase.getChatUsers(user_name);
        if (list.isEmpty()){
        }else {
            customPojos_list=list;
        }
        adapter = new ChatActivityAdapter(this,customPojos_list);
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() >2)
            recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
        toolbar.setTitle(user_name);
//        recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);


        // Set a listener to send a chat text message
        final Button send = (Button) this.findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String text = textMessage.getText().toString();
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutee = calendar.get(Calendar.MINUTE);
                MessagesModel pojo = new MessagesModel(user_name,text,updateTime1(hour,minutee),"to");
//                pojo.setTime_mess(time);
//                pojo.setSource("to");
//                pojo.setUser_id(user_name);
//                pojo.setMessages(text);
                ChatMessagesDatabase chatMessagesDatabase = new ChatMessagesDatabase(ChatActivity.this);
                chatMessagesDatabase.addChat(pojo);
                customPojos_list.add(pojo);
                //adapter.setListContent(customPojos_list);
                adapter.notifyDataSetChanged();
                textMessage.setText(null);
                recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                SendMessages.putExtra("user_name",user_name);
                SendMessages.putExtra("message",text);
                sendBroadcast(SendMessages);

                //dop.putCMInformation(dop,"to",time,text,user_name);

                }

        });
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

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
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutee = calendar.get(Calendar.MINUTE);
            MessagesModel pojo = new MessagesModel(username,msg,updateTime1(hour,minutee),"from");
//            pojo.setTime_mess(time);
//            pojo.setSource("from");
//            pojo.setUser_id(username);
//            pojo.setMessages(msg);
            //dop.putCMInformation(dop,"from",time,msg,user_name);
            //adapter.notifyDataSetChanged();
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
//                    MessagesModel pojo = new MessagesModel();
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


    @Override
    protected void onStop() {
        super.onStop();

    }
}
