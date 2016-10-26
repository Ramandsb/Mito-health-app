package in.tagbin.mitohealthapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.Database.ChatDatabase;
import in.tagbin.mitohealthapp.Database.ChatMessagesDatabase;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.ChatActivity;
import in.tagbin.mitohealthapp.activity.ChatRequestActivity;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.adapter.ChatActivityAdapter;
import in.tagbin.mitohealthapp.adapter.ChatFriendsAdapter;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.ItemClickSupport;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ChatAccounts;
import in.tagbin.mitohealthapp.model.DieticainModel;
import in.tagbin.mitohealthapp.model.MessagesModel;
import in.tagbin.mitohealthapp.service.XmppChatService;

/**
 * Created by aasaqt on 6/10/16.
 */

public class ChatDieticianFragment  extends Fragment {
    RecyclerView recyclerView;
    ChatActivityAdapter adapter;
    private ArrayList<MessagesModel> customPojos_list= new ArrayList<>();
    private ArrayList<MessagesModel> list= new ArrayList<>();
    String user="rman";
    private EditText textMessage;
    Intent SendMessages;
    TextView coins;
    int coinsFinal = 10;
    PrefManager pref;
    LinearLayout chatLinear;
    AppBarLayout appBarLayout;
    String user_name="",user_content="",name;
    Button send;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.content_chat, container, false);
        pref = new PrefManager(getContext());
        recyclerView=(RecyclerView)view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //lookupAdapter=new ChatActivityAdapter(this);
        textMessage = (EditText) view.findViewById(R.id.chatET);
        send = (Button) view.findViewById(R.id.sendBtn);
        chatLinear = (LinearLayout) view.findViewById(R.id.linearChat);
        Controller.getDietician(getContext(),mDieticianListener);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) chatLinear.getLayoutParams();
        int bottom = MyUtils.dpToPx(getContext(),40);
        layoutParams.setMargins(0,0,0,bottom);
        chatLinear.setLayoutParams(layoutParams);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereProfile");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
        //InitActivity i = (InitActivity) getActivity();
        //i.getActionBar().setTitle("Profile");
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        menu.findItem(R.id.action_requests).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        menu.findItem(R.id.action_Settings).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_requests) {
            Intent i = new Intent(getContext(),ChatRequestActivity.class);
            startActivity(i);
        }else if (id == R.id.action_Settings) {
            if (pref.getKeyUserDetails() != null && pref.getKeyUserDetails().getProfile().getHeight() != 0 && pref.getKeyUserDetails().getProfile().getWeight() != 0){
                //toolbar_title.setText("Settings");
                //toolbar.setTitle("");
                //fra = new SettingsActivity();
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
            }else {
                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle);
                alertDialog1.setTitle("Enter Details");
                alertDialog1.setMessage("Please enter your height and weight to proceed");
                alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog1.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    RequestListener mDieticianListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            final DieticainModel dieticainModel = JsonUtils.objectify(responseObject.toString(),DieticainModel.class);
            pref.saveDietician(dieticainModel);
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getDieticianChat(dieticainModel);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
    public void  getDieticianChat(DieticainModel dieticainModel){

        SendMessages= new Intent(XmppChatService.SENTMSGS);
        user_name = dieticainModel.getChat_username()+"@"+dieticainModel.getChat_server();
        ChatMessagesDatabase chatMessagesDatabase = new ChatMessagesDatabase(getContext());
        list=chatMessagesDatabase.getChatUsers(user_name);
        if (list.isEmpty()){
        }else {
            customPojos_list=list;
        }
        adapter = new ChatActivityAdapter(getContext(),customPojos_list);
        recyclerView.setAdapter(adapter);
        if (adapter.getItemCount() >2)
            recyclerView.scrollToPosition(adapter.getItemCount()-1);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String text = textMessage.getText().toString();
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minutee = calendar.get(Calendar.MINUTE);
                MessagesModel pojo = new MessagesModel(user_name,text,String.valueOf(getCurrentTime(getContext())),"to");
                ChatMessagesDatabase chatMessagesDatabase = new ChatMessagesDatabase(getContext());
                chatMessagesDatabase.addChat(pojo);
                customPojos_list.add(pojo);
                //lookupAdapter.setListContent(customPojos_list);
                adapter.notifyDataSetChanged();
                textMessage.setText(null);
                recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                SendMessages.putExtra("user_name",user_name);
                SendMessages.putExtra("message",text);
                getActivity().sendBroadcast(SendMessages);

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
    private BroadcastReceiver Recievemsgs = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String username=intent.getStringExtra("user_name");
            String msg=intent.getStringExtra("message");
            Log.d("Message Recieved","From"+ username+" Message :"+msg);
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutee = calendar.get(Calendar.MINUTE);
            if (username.equals(user_name)) {
                MessagesModel pojo = new MessagesModel(username, msg, String.valueOf(getCurrentTime(getContext())), "from");
                customPojos_list.add(pojo);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                });
            }


        }
    };


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(Recievemsgs, new IntentFilter(XmppChatService.RECIEVEDMSGS));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(Recievemsgs);
    }
}