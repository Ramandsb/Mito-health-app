package in.tagbin.mitohealthapp.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.CreateEventActivity;
import in.tagbin.mitohealthapp.activity.ChatRequestActivity;
import in.tagbin.mitohealthapp.activity.EventsListActivity;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.adapter.UpcomingEventsAdapter;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.adapter.LookupAdapter;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import pl.droidsonroids.gif.GifImageView;

public class Lookupfragment extends Fragment implements View.OnClickListener {
    TextView coins,createevent;
    List<DataObject> myEventsList=new ArrayList<DataObject>();
    List<DataObject> upcomingEventsList=new ArrayList<DataObject>();
    //FloatingActionButton fabCreateEvent;
    int coinsFinal = 0;
    GifImageView progressBar;
    PrefManager pref;
    ImageView addMoreMyEvents,seeMoreUpcomingEvents,seeMoreGoingEvents;
    LinearLayoutManager linearLayoutManager1,linearLayoutManager2,linearLayoutManager3;
    LookupAdapter lookupAdapter;
    UpcomingEventsAdapter upcomingEventsAdapter;
    RecyclerView goingEvents,upcomingEvents,myEvents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.frag_lookup, container, false);
        goingEvents = (RecyclerView) viewGroup.findViewById(R.id.rvGoingEvents);
        myEvents = (RecyclerView) viewGroup.findViewById(R.id.rvMyEvents);
        upcomingEvents = (RecyclerView) viewGroup.findViewById(R.id.rvUpcomingEvents);
        //fabCreateEvent = (FloatingActionButton) viewGroup.findViewById(R.id.createevent);
        progressBar = (GifImageView) viewGroup.findViewById(R.id.progressBar);
        createevent = (TextView) viewGroup.findViewById(R.id.tvCreateEvent);
        addMoreMyEvents = (ImageView) viewGroup.findViewById(R.id.ivSeeMoreMyEvents);
        seeMoreGoingEvents = (ImageView) viewGroup.findViewById(R.id.ivSeeMoreGoingEvents);
        seeMoreUpcomingEvents = (ImageView) viewGroup.findViewById(R.id.ivSeeMoreUpcomingEvents);
        createevent.setOnClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        pref = new PrefManager(getContext());
        progressBar.setVisibility(View.VISIBLE);
        linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        goingEvents.setLayoutManager(linearLayoutManager1);
        upcomingEvents.setLayoutManager(linearLayoutManager3);
        myEvents.setLayoutManager(linearLayoutManager2);
        myEvents.setHasFixedSize(true);
        lookupAdapter = new LookupAdapter(getContext(),myEventsList,progressBar);
        myEvents.setAdapter(lookupAdapter);
        upcomingEvents.setHasFixedSize(true);
        upcomingEventsAdapter = new UpcomingEventsAdapter(getContext(),upcomingEventsList,progressBar);
        upcomingEvents.setAdapter(upcomingEventsAdapter);
        Controller.getEventsByMe(getContext(),mNearbyEvents);
        Controller.getAllEventsNearby(getContext(),pref.getCurrentLocationAsObject().getLatitude(),pref.getCurrentLocationAsObject().getLongitude(),mAllEventsListener);

        return viewGroup;
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
        //menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCreateEvent:
//                frameLayout.setVisibility(View.VISIBLE);
//                Fragment fragment = new CreateEventActivity();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.add(R.id.frameAddActivity, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                Intent i = new Intent(getContext(),CreateEventActivity.class);
                startActivity(i);
                break;
        }
    }

    RequestListener mNearbyEvents = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("nearby events",responseObject.toString());

            myEventsList.clear();
            Type collectionType = new TypeToken<ArrayList<DataObject>>() {
            }.getType();
            final List<DataObject> da = (ArrayList<DataObject>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            if (da.size() > 4) {
                for (int i = 0; i < 4; i++) {
                    da.get(i).all = false;
                    myEventsList.add(da.get(i));
                    pref.setKeyCoins(myEventsList.get(0).getTotal_coins());
                }
            }else{
                for (int i = 0; i < da.size(); i++) {
                    da.get(i).all = false;
                    myEventsList.add(da.get(i));
                    pref.setKeyCoins(myEventsList.get(0).getTotal_coins());
                }
            }
            for (int i= 0;i<da.size();i++){
                da.get(i).all = false;
            }
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (da.size() > 4){
                        addMoreMyEvents.setVisibility(View.VISIBLE);
                        addMoreMyEvents.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getContext(), EventsListActivity.class);
                                i.putExtra("response",JsonUtils.jsonify(da));
                                i.putExtra("name","My Events");
                                startActivity(i);
                            }
                        });
                    }else{
                        addMoreMyEvents.setVisibility(View.GONE);
                    }
                    lookupAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("nearby events error",message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mAllEventsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("all events",responseObject.toString());
            upcomingEventsList.clear();

            Type collectionType = new TypeToken<ArrayList<DataObject>>() {
            }.getType();
            final List<DataObject> da = (ArrayList<DataObject>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            if (da.size() > 4) {
                for (int i = 0; i < 4; i++) {
                    da.get(i).all = true;
                    upcomingEventsList.add(da.get(i));
                    pref.setKeyCoins(upcomingEventsList.get(0).getTotal_coins());
                }
            }else{
                for (int i = 0; i < da.size(); i++) {
                    da.get(i).all = true;
                    upcomingEventsList.add(da.get(i));
                    pref.setKeyCoins(upcomingEventsList.get(0).getTotal_coins());
                }
            }
            for (int i= 0;i<da.size();i++){
                da.get(i).all = true;
            }
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (da.size() > 4){
                        seeMoreUpcomingEvents.setVisibility(View.VISIBLE);
                        seeMoreUpcomingEvents.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getContext(), EventsListActivity.class);
                                i.putExtra("response",JsonUtils.jsonify(da));
                                i.putExtra("name","All Events");
                                startActivity(i);
                            }
                        });
                    }else{
                        seeMoreUpcomingEvents.setVisibility(View.GONE);
                    }
                    upcomingEventsAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("all events error",message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
