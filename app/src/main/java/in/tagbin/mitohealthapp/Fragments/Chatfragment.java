package in.tagbin.mitohealthapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import in.tagbin.mitohealthapp.Database.ChatDatabase;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.ItemClickSupport;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.ChatActivity;
import in.tagbin.mitohealthapp.activity.ChatRequestActivity;
import in.tagbin.mitohealthapp.adapter.ChatFriendsAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ChatAccounts;
import in.tagbin.mitohealthapp.model.DieticainModel;
import in.tagbin.mitohealthapp.service.XmppChatService;

public class Chatfragment extends Fragment {
    DatabaseOperations dop;
    TextView coins;
    int coinsFinal = 0;
    RecyclerView recyclerView;
    ChatFriendsAdapter adapter;
    private ArrayList<ChatAccounts> listContentArr= new ArrayList<>();
    String user="rman";
    PrefManager pref;
    DieticainModel dieticainModel;
    ChatDatabase chatDatabase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_chat, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pref = new PrefManager(getContext());

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                ChatAccounts pojo = listContentArr.get(position);
                startActivity(new Intent(getActivity(),ChatActivity.class).putExtra("user_name",pojo.getUser()).putExtra("name",pojo.getName()));
                Log.d("click details",""+pojo.getName()+"//////"+pojo.getUser()+"/////"+pojo.getPresence_status());
            }
        });
        return view;
    }

    private BroadcastReceiver ReceiveRoosters = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            chatDatabase = new ChatDatabase(getContext());
            listContentArr=chatDatabase.getChatUsers(dieticainModel.getChat_username()+"@"+dieticainModel.getChat_server());
            adapter.notifyDataSetChanged();
        }
    };
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
        PrefManager pref = new PrefManager(getContext());
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

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(ReceiveRoosters);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(ReceiveRoosters,new IntentFilter(XmppChatService.RECEIVEROOSTER));
        if (pref.getDietician() != null) {
            dieticainModel = pref.getDietician();
            chatDatabase = new ChatDatabase(getContext());
            listContentArr = chatDatabase.getChatUsers(dieticainModel.getChat_username() + "@" + dieticainModel.getChat_server());
            adapter = new ChatFriendsAdapter(getActivity(), listContentArr);
            recyclerView.setAdapter(adapter);
        }else{
            Controller.getDietician(getContext(),mDieticianListener);
        }
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
                    listContentArr=chatDatabase.getChatUsers(dieticainModel.getChat_username()+"@"+dieticainModel.getChat_server());
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
}