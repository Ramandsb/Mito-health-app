package in.tagbin.mitohealthapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyAdapter;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import pl.droidsonroids.gif.GifImageView;

public class Lookupfrag extends Fragment implements View.OnClickListener {

    RecyclerView myview;

    StaggeredGridLayoutManager mylayoutmanager;
    FrameLayout frameLayout;
    TextView allActivity,myActivity;
    ArrayList<DataObject> mylist=new ArrayList<DataObject>();
    ArrayList<DataObject> da=new ArrayList<DataObject>();
    FloatingActionButton fabCreateEvent;
    GifImageView progressBar;
    MyAdapter adapter;
    PrefManager pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.lookup, container, false);

        myview= (RecyclerView) viewGroup.findViewById(R.id.myrecycler);

        frameLayout = (FrameLayout) viewGroup.findViewById(R.id.frameAddActivity);
        fabCreateEvent = (FloatingActionButton) viewGroup.findViewById(R.id.createevent);
        allActivity = (TextView) viewGroup.findViewById(R.id.buttonAllActivity);
        myActivity = (TextView) viewGroup.findViewById(R.id.buttonMyActivity);
        progressBar = (GifImageView) viewGroup.findViewById(R.id.progressBar);
        allActivity.setOnClickListener(this);
        myActivity.setOnClickListener(this);
        fabCreateEvent.setOnClickListener(this);
        mylayoutmanager = new StaggeredGridLayoutManager(2, 1);
        adapter=new MyAdapter(getContext(),mylist,frameLayout,getActivity().getSupportFragmentManager(),progressBar);
        myview.setLayoutManager(this.mylayoutmanager);
        myview.setHasFixedSize(true);
        myview.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        pref = new PrefManager(getContext());
        if (pref.getCurrentLocationAsObject() != null){
            if (pref.getCurrentLocationAsObject().getLongitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0){
                Controller.getAllEventsNearby(getContext(),pref.getCurrentLocationAsObject().getLatitude(),pref.getCurrentLocationAsObject().getLongitude(),mAllEventsListener);
            }else{
                Controller.getAllEvents(getContext(),mAllEventsListener);
            }
        }

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createevent:
                frameLayout.setVisibility(View.VISIBLE);
                Fragment fragment = new AddActivityfrag();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frameAddActivity, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.buttonAllActivity:
                allActivity.setTextColor(Color.parseColor("#ffffff"));
                allActivity.setBackgroundResource(R.drawable.bg_filter_change);
                myActivity.setTextColor(Color.parseColor("#26446d"));
                myActivity.setBackgroundResource(R.drawable.bg_filter);
                progressBar.setVisibility(View.VISIBLE);
                if (pref.getCurrentLocationAsObject() != null){
                    if (pref.getCurrentLocationAsObject().getLongitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0){
                        Controller.getAllEventsNearby(getContext(),pref.getCurrentLocationAsObject().getLatitude(),pref.getCurrentLocationAsObject().getLongitude(),mAllEventsListener);
                    }else{
                        Controller.getAllEvents(getContext(),mAllEventsListener);
                    }
                }
                break;
            case R.id.buttonMyActivity:
                myActivity.setTextColor(Color.parseColor("#ffffff"));
                myActivity.setBackgroundResource(R.drawable.bg_filter_change);
                allActivity.setTextColor(Color.parseColor("#26446d"));
                allActivity.setBackgroundResource(R.drawable.bg_filter);
                progressBar.setVisibility(View.VISIBLE);
                Controller.getEventsByMe(getContext(),mNearbyEvents);
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
            mylist.clear();
            Type collectionType = new TypeToken<ArrayList<DataObject>>() {
            }.getType();
            da = (ArrayList<DataObject>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            for (int i=0;i<da.size();i++){
                da.get(i).all = false;
                mylist.add(da.get(i));
            }
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("nearby events error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
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
            mylist.clear();
            Type collectionType = new TypeToken<ArrayList<DataObject>>() {
            }.getType();
            da = (ArrayList<DataObject>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            for (int i=0;i<da.size();i++){
                da.get(i).all = true;
                mylist.add(da.get(i));
            }
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("all events error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
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
