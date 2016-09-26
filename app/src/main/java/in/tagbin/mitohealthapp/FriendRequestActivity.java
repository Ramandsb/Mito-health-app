package in.tagbin.mitohealthapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.FriendRequestAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ConnectProfileModel;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ExploreModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 23/9/16.
 */
public class FriendRequestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    FriendRequestAdapter adapter;
    TextView coins;
    int coinsFinal = 0;
    GifImageView progressBar;
    List<ConnectProfileModel> data;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);
        recyclerView = (RecyclerView) findViewById(R.id.rvFriends);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connection Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        data = new ArrayList<ConnectProfileModel>();
        adapter = new FriendRequestAdapter(this,data,progressBar);
        recyclerView.setAdapter(adapter);
        Controller.getRequests(this,mRequestListener);

    }
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_requests).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(false);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        PrefManager pref = new PrefManager(this);
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
        return super.onCreateOptionsMenu(menu);
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
    RequestListener mRequestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Type collectionType = new TypeToken<ArrayList<ConnectProfileModel>>() {
            }.getType();
            List<ConnectProfileModel> da = (ArrayList<ConnectProfileModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            for(int i=0;i<da.size();i++){
                data.add(da.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
}
