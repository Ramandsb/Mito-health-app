package in.tagbin.mitohealthapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.Database.ChatDatabase;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.helper.ItemClickSupport;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.ChatActivity;
import in.tagbin.mitohealthapp.activity.ChatRequestActivity;
import in.tagbin.mitohealthapp.adapter.ChatFriendsAdapter;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ChatAccounts;
import in.tagbin.mitohealthapp.service.XmppChatService;

public class Chatfragment extends Fragment {
    DatabaseOperations dop;
    TextView coins;
    int coinsFinal = 0;
    RecyclerView recyclerView;
    ChatFriendsAdapter adapter;
    private ArrayList<ChatAccounts> listContentArr= new ArrayList<>();
    String user="rman";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dop= new DatabaseOperations(getActivity());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.frag_chat, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ChatDatabase chatDatabase = new ChatDatabase(getContext());

        listContentArr=chatDatabase.getChatUsers();
        adapter=new ChatFriendsAdapter(getActivity(),listContentArr);
        recyclerView.setAdapter(adapter);
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
            ChatDatabase chatDatabase = new ChatDatabase(getContext());
            listContentArr=chatDatabase.getChatUsers();
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
    }
}