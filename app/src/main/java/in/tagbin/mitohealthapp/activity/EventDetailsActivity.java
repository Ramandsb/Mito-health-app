package in.tagbin.mitohealthapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.adapter.EventUserAdapter;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ParticipantModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 9/8/16.
 */
public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView type,time,location,date,selectedPeople,heading,joinText,coins,edit;
    public static TextView people,interested,approved,left;
    DataObject data;
    ImageView backImage;
    LinearLayout linearFriends;
    RecyclerView recyclerView;
    List<ParticipantModel> mModel,da;
    FrameLayout frameLayout;
    GifImageView progressBar;
    StaggeredGridLayoutManager mylayoutmanager;
    EventUserAdapter mAdapter;
    RelativeLayout invite,join,interestedRelative,approvedRelative,housefull,expired;
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    int coinsFinal;
    CountDownTimer countDownTimer;
    Toolbar toolbar;
    String dataobject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        type = (TextView) findViewById(R.id.tvMyActivityType);
        //title = (TextView) findViewById(R.id.tvMyActivityTitle);
        time = (TextView) findViewById(R.id.tvMyActivityTime);
        people = (TextView) findViewById(R.id.tvMyActivityPeople);
        date = (TextView) findViewById(R.id.tvMyActivityDate);
        location = (TextView) findViewById(R.id.tvMyActivityLocation);
        recyclerView = (RecyclerView) findViewById(R.id.rvParticipants);
        edit = (TextView) findViewById(R.id.ivMyActivityEdit);
        housefull = (RelativeLayout) findViewById(R.id.relativeHousefull);
        expired = (RelativeLayout) findViewById(R.id.relativeExpired);
        //heading = (TextView) findViewById(R.id.tvMyActivityHeading);
        joinText = (TextView) findViewById(R.id.tvJoinText);
        frameLayout = (FrameLayout) findViewById(R.id.frameParticipantDetail);
        backImage = (ImageView) findViewById(R.id.ivMyActivityImage);
        linearFriends = (LinearLayout) findViewById(R.id.lineaFriendRequsts);
        selectedPeople = (TextView) findViewById(R.id.tvSelectedPeople);
        invite = (RelativeLayout) findViewById(R.id.relativeInviteFriends);
        join = (RelativeLayout) findViewById(R.id.relativeJoinFriends);
        interested = (TextView) findViewById(R.id.tvInterestedFriends);
        interestedRelative = (RelativeLayout) findViewById(R.id.relativeInterested);
        approvedRelative = (RelativeLayout) findViewById(R.id.relativeApproved);
        approved = (TextView) findViewById(R.id.tvApprovedFriends);
        left = (TextView) findViewById(R.id.tvLeftFriends);
        interestedRelative.setOnClickListener(this);
        approvedRelative.setOnClickListener(this);
        dataobject = getIntent().getStringExtra("dataobject");
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                frameLayout.setVisibility(View.VISIBLE);
//                Bundle bundle = new Bundle();
//                Fragment fragment = new CreateEventActivity();
//                bundle.putString("response",dataobject);
//                fragment.setArguments(bundle);
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.add(R.id.frameAddActivity, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                Intent i = new Intent(EventDetailsActivity.this,CreateEventActivity.class);
                i.putExtra("response",dataobject);
                startActivity(i);
            }
        });
//        heading.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                final int DRAWABLE_LEFT = 0;
//                final int DRAWABLE_TOP = 1;
//                final int DRAWABLE_RIGHT = 2;
//                final int DRAWABLE_BOTTOM = 3;
//
//
//                if (event.getRawX() <= (heading.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
//                    finish();
//                    return true;
//                }
//                return false;
//            }
//        });
        //join.setOnClickListener(this);
        invite.setOnClickListener(this);
        data = JsonUtils.objectify(dataobject,DataObject.class);
        mModel = new ArrayList<ParticipantModel>();
        Calendar calendar = Calendar.getInstance();
        long output = calendar.getTimeInMillis();
        if (data.getTotal_approved() == data.getCapacity()){
            //holder.join.setTextColor(Color.parseColor("#9b9b9b"));
            joinText.setText("Join");
            housefull.setVisibility(View.VISIBLE);
            expired.setVisibility(View.GONE);
            //holder.housefull.setVisibility(View.VISIBLE);
            join.setClickable(false);
        }else if (MyUtils.getTimeinMillis(data.getTime()) < output){
            joinText.setText("Join");
            housefull.setVisibility(View.GONE);
            expired.setVisibility(View.VISIBLE);
            join.setClickable(false);
        }else if (data.getMapper().getId() != 0 && !data.getMapper().isConfirm()){
            //holder.join.setTextColor(Color.parseColor("#ffffff"));
            joinText.setText("Pending");
            housefull.setVisibility(View.GONE);
            expired.setVisibility(View.GONE);
            join.setClickable(false);
        }else if (data.getMapper().getId() != 0 && data.getMapper().isConfirm()){
            //holder.join.setTextColor(Color.parseColor("#ffffff"));
            joinText.setText("Accepted");
            housefull.setVisibility(View.GONE);
            expired.setVisibility(View.GONE);
            join.setClickable(false);
        }else{
            //holder.join.setTextColor(Color.parseColor("#ffffff"));
            joinText.setText("Join");
            join.setClickable(true);
            housefull.setVisibility(View.GONE);
            expired.setVisibility(View.GONE);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.joinEvent(EventDetailsActivity.this,data.getId(),mJoinEventListener);
                }
            });
        }
        if (data.isAll()){
            date.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            linearFriends.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("All Event");
            selectedPeople.setVisibility(View.VISIBLE);
            linearFriends.setWeightSum(5);
            join.setVisibility(View.VISIBLE);
            approvedRelative.setClickable(false);
            interestedRelative.setClickable(false);
        }else{
            //date.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("My Event");
            selectedPeople.setVisibility(View.VISIBLE);
            selectedPeople.setText("Pending Approvals");
            linearFriends.setVisibility(View.VISIBLE);
            join.setVisibility(View.GONE);
            linearFriends.setWeightSum(4);
            approvedRelative.setClickable(true);
            interestedRelative.setClickable(true);
        }
        Controller.getParticipants(EventDetailsActivity.this,data.getId(),mParticipantListener);
        setData(data);
    }
    @Override
    public void onResume() {
        super.onResume();
        Controller.getParticipants(EventDetailsActivity.this,data.getId(),mParticipantListener);
    }
    public void onResume1() {
        Controller.getParticipants(this,data.getId(),mParticipantListener);
    }
    public void setData(final DataObject data){
        //title.setText(data.getTitle());
        String output = data.getTitle().substring(0, 1).toUpperCase() + data.getTitle().substring(1);
        type.setText(output);
//        final String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(MyUtils.getTimeinMillis(data.getTime()), getCurrentTime(getContext()), DateUtils.MINUTE_IN_MILLIS));
//        time.setText(relativeTime);
        location.setText(MyUtils.getCityName(EventDetailsActivity.this,data.getLocation()));
        people.setText(""+data.getTotal_approved()+"/"+data.getCapacity());
        interested.setText(""+(data.getTotal_request()-data.getTotal_approved()));
        approved.setText(""+data.getTotal_approved());
        left.setText(""+(data.getCapacity()-data.getTotal_approved()));
        long endTime = MyUtils.getTimeinMillis(data.getTime());
        long currentTime = System.currentTimeMillis();
        countDownTimer = new CountDownTimer(endTime - currentTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long ms = millisUntilFinished;
                StringBuffer text = new StringBuffer("");
                if (ms > DAY) {
                    text.append(ms / DAY).append(" Days, ");
                    ms %= DAY;
                }
                if (ms > HOUR) {
                    text.append(ms / HOUR).append(":");
                    ms %= HOUR;
                }
                if (ms > MINUTE) {
                    text.append(ms / MINUTE).append(" Hrs");
                    ms %= MINUTE;
                }
                if (ms > SECOND){
                    //text.append(ms/SECOND);
                    ms %= SECOND;
                }
                time.setText(text.toString());
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();

        if (data.getPicture() != null){

            ImageLoader.getInstance().loadImage(data.getPicture(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


                    backImage.setImageBitmap(loadedImage);

                }
            });

        }else{
            backImage.setBackgroundResource(R.drawable.hotel);
        }
        mylayoutmanager = new StaggeredGridLayoutManager(4, 1);
        mAdapter = new EventUserAdapter(EventDetailsActivity.this,mModel,getSupportFragmentManager(),null,dataobject);
        recyclerView.setLayoutManager(this.mylayoutmanager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(mAdapter);

        date.setText(MyUtils.getValidDateForLookupDetails(data.getEvent_time()));


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
    RequestListener mParticipantListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("All participants",responseObject.toString());
            Type collectionType = new TypeToken<List<ParticipantModel>>() {
            }.getType();
            mModel.clear();
            da = (List<ParticipantModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            for (int i=0;i<da.size();i++){
                if (!data.isAll()) {
                    if (!da.get(i).isConfirm())
                        mModel.add(da.get(i));
                }else{
                    mModel.add(da.get(i));
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mModel.size() > 0) {
                        if (data.isAll()){
                            data = mModel.get(0).getEvent();
                            data.all = true;
                        }else{
                            data = mModel.get(0).getEvent();
                            data.all = false;
                        }
                        setData(data);
                    }
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("All participants error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeInviteFriends:
                break;
            case R.id.relativeApproved:
                selectedPeople.setText("Approved People");
                progressBar.setVisibility(View.VISIBLE);
                Controller.getParticipants(EventDetailsActivity.this,data.getId(),mParticipantApprovedListener);
                break;
            case R.id.relativeInterested:
                selectedPeople.setText("Pending Approvals");
                progressBar.setVisibility(View.VISIBLE);
                Controller.getParticipants(EventDetailsActivity.this,data.getId(),mParticipantListener);
                break;
        }
    }
    RequestListener mParticipantApprovedListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("All participants",responseObject.toString());
            Type collectionType = new TypeToken<List<ParticipantModel>>() {
            }.getType();
            mModel.clear();
            da = (List<ParticipantModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            for (int i=0;i<da.size();i++){
                if(da.get(i).isConfirm())
                    mModel.add(da.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mModel.size() > 0) {
                        if (data.isAll()){
                            data = mModel.get(0).getEvent();
                            data.all = true;
                        }else{
                            data = mModel.get(0).getEvent();
                            data.all = false;
                        }

                        setData(data);
                    }
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mJoinEventListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("join event", responseObject.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    joinText.setText("Pending");
                    join.setClickable(false);
                    Toast.makeText(EventDetailsActivity.this,"Event joined succcesfully",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("join event error", message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public long getCurrentTime(Context ctx){

        long sec = System.currentTimeMillis();
        return sec;
    }
}
