package in.tagbin.mitohealthapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.adapter.ChatActivityAdapter;
import in.tagbin.mitohealthapp.Database.ChatMessagesDatabase;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.service.XmppChatService;
import in.tagbin.mitohealthapp.model.MessagesModel;

public class ChatActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ChatActivityAdapter adapter;
    private ArrayList<MessagesModel> customPojos_list= new ArrayList<>();
    private ArrayList<MessagesModel> list= new ArrayList<>();
    String user="rman";
    private EditText textMessage;
    Intent SendMessages;



    String user_name="",user_content="",name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //customDialog();
        registerReceiver(Recievemsgs, new IntentFilter(XmppChatService.RECIEVEDMSGS));
        SendMessages= new Intent(XmppChatService.SENTMSGS);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter=new ChatActivityAdapter(this);
        textMessage = (EditText) this.findViewById(R.id.chatET);
        user_name=getIntent().getExtras().getString("user_name");
        name = getIntent().getStringExtra("tv_name");
        Log.d("tv_name",name);
        if (name != null) {
            getSupportActionBar().setTitle(name);
        }else {
            getSupportActionBar().setTitle(user_name);
        }

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
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
        final Button send = (Button) this.findViewById(R.id.sendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String text = textMessage.getText().toString();
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutee = calendar.get(Calendar.MINUTE);
                MessagesModel pojo = new MessagesModel(user_name,text,String.valueOf(getCurrentTime(ChatActivity.this)),"to");
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
    public long getCurrentTime(Context ctx){

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

    private BroadcastReceiver Recievemsgs = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username=intent.getStringExtra("user_name");
            String msg=intent.getStringExtra("message");
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutee = calendar.get(Calendar.MINUTE);
            if (username.equals(user_name)) {
                MessagesModel pojo = new MessagesModel(username, msg, String.valueOf(getCurrentTime(ChatActivity.this)), "from");
                customPojos_list.add(pojo);
                adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
            Log.d("Message Recieved","From"+ username+" Message :"+msg);


        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(Recievemsgs);
    }
}
