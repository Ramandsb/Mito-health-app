package in.tagbin.mitohealthapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.adapter.EventUserImageAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.adapter.ImagesSlideAdapter;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ParticipantModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 10/8/16.
 */
public class EventsUserDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    TextView title,time,location,people,name,profession,hobbies,coins;
    DataObject dataObject;
    List<ParticipantModel> participantModels;
    ParticipantModel data;
    private ImagesSlideAdapter mAdapter;
    private int dotsCount,coinsFinal;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    private ViewPager intro_images;
    private ArrayList<String> mImageResources;
    LinearLayoutManager mLayoutManager;
    RecyclerView rvAllParticipants;
    EventUserImageAdapter allAdapter;
    FrameLayout frameLayout,wholeLayout;
    ImageView addParticipant;
    GifImageView progressBar;
    CountDownTimer countDownTimer;
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_user_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //title = (TextView) findViewById(R.id.tvParticipantDetailTitle);
        time = (TextView) findViewById(R.id.tvParticipantDetailTime);
        location = (TextView) findViewById(R.id.tvParticipantDetailLocation);
        people = (TextView) findViewById(R.id.tvParticipantDetailPeople);
        name = (TextView) findViewById(R.id.tvParticipantDetailName);
        profession = (TextView) findViewById(R.id.tvParticipantDetailProfession);
        hobbies = (TextView) findViewById(R.id.tvParticipantDetailHobbies);
        intro_images = (ViewPager) findViewById(R.id.pagerParticipant);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        frameLayout = (FrameLayout) findViewById(R.id.frameAllParticiapnts);
        addParticipant = (ImageView) findViewById(R.id.ivPArticipantApproved);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        addParticipant.setOnClickListener(this);
        dataObject = JsonUtils.objectify(getIntent().getStringExtra("dataobject"),DataObject.class);
        Type collectionType = new TypeToken<List<ParticipantModel>>() {
        }.getType();
        participantModels = (List<ParticipantModel>) new Gson()
                .fromJson(getIntent().getStringExtra("allmodels"), collectionType);
        data = JsonUtils.objectify(getIntent().getStringExtra("participantModel"),ParticipantModel.class);
        long endTime = MyUtils.getTimeinMillis(dataObject.getTime());

        long currentTime = System.currentTimeMillis();
        countDownTimer = new CountDownTimer(endTime - currentTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long ms = millisUntilFinished;
                StringBuffer text = new StringBuffer("");
                if (ms > DAY) {
                    text.append(ms / DAY).append(":");
                    ms %= DAY;
                }
                if (ms > HOUR) {
                    text.append(ms / HOUR).append(":");
                    ms %= HOUR;
                }
                if (ms > MINUTE) {
                    text.append(ms / MINUTE).append(":");
                    ms %= MINUTE;
                }
                if (ms > SECOND){
                    text.append(ms/SECOND);
                    ms %= SECOND;
                }
                time.setText(text.toString());
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        getSupportActionBar().setTitle(dataObject.getTitle());
        location.setText(MyUtils.getStateName(EventsUserDetailsActivity.this,dataObject.getLocation()));
        people.setText(""+dataObject.getCapacity());
        if (data.getUser().getLast_name() != null) {
            name.setText(data.getUser().getFirst_name()+" "+data.getUser().getLast_name()+", "+data.getUser().getProfile().getAge());
        }else{
            name.setText(data.getUser().getFirst_name()+", "+data.getUser().getProfile().getAge());
        }
        if (dataObject.isAll()){
            addParticipant.setVisibility(View.GONE);
        }else{

            addParticipant.setVisibility(View.VISIBLE);
        }
        //profession.setText(data.getUser().getProfession());
        if (data.getUser().getInterests() != null && data.getUser().getInterests().size() >0) {
            List<String> finalInterests = new ArrayList<String>();
            for (int i=0;i<data.getUser().getInterests().size();i++){
                finalInterests.add(data.getUser().getInterests().get(i).getInterest().getName());
                Log.d("name",data.getUser().getInterests().get(i).getInterest().getName());
            }
            hobbies.setText(finalInterests.toString().replace("[","").replace("]",""));

        }
        mImageResources = new ArrayList<String>();
        if (data.getUser().getProfile().getImages() != null){
            if (data.getUser().getProfile().getImages().getMaster() != null)
                mImageResources.add(data.getUser().getProfile().getImages().getMaster());
            else{
                mImageResources.add(null);
            }
            if (data.getUser().getProfile().getImages().getOthers() != null && data.getUser().getProfile().getImages().getOthers().length >0){
                for (int i= 0;i<data.getUser().getProfile().getImages().getOthers().length;i++){
                    mImageResources.add(data.getUser().getProfile().getImages().getOthers()[i]);
                }
            }
        }else{
            mImageResources.add(null);
        }
        profession.setText(MyUtils.getStateName(this,data.getUser().getProfile().getLocation()));
        mAdapter = new ImagesSlideAdapter(EventsUserDetailsActivity.this, mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
        rvAllParticipants = (RecyclerView) findViewById(R.id.rvAllParticipants);
        rvAllParticipants.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(EventsUserDetailsActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAllParticipants.setLayoutManager(this.mLayoutManager);
        if (getIntent().getIntExtra("position",0) >2){
            rvAllParticipants.scrollToPosition(getIntent().getIntExtra("position",0)-2);
        }

        allAdapter = new EventUserImageAdapter(EventsUserDetailsActivity.this,participantModels,getIntent().getStringExtra("dataobject"),getSupportFragmentManager(),null);
        rvAllParticipants.setAdapter(allAdapter);
    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(EventsUserDetailsActivity.this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_unactive));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_active));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_unactive));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_active));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivPArticipantApproved:
                progressBar.setVisibility(View.VISIBLE);
                Controller.confirmParticipant(EventsUserDetailsActivity.this,dataObject.getId(),data.getId(),mParticipantApproved);
                break;
        }
    }
    RequestListener mParticipantApproved = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("participant approved",responseObject.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    addParticipant.setVisibility(View.GONE);
                    Toast.makeText(EventsUserDetailsActivity.this,"Participant approved succesfully",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("approved error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventsUserDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(EventsUserDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
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
}
