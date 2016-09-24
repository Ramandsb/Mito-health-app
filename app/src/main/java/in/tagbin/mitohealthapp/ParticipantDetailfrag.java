package in.tagbin.mitohealthapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.AllParticipantAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ParticipantModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 10/8/16.
 */
public class ParticipantDetailfrag extends Fragment implements ViewPager.OnPageChangeListener, View.OnClickListener {
    TextView title,time,location,people,name,profession,hobbies;
    DataObject dataObject;
    List<ParticipantModel> participantModels;
    ParticipantModel data;
    private ViewPagerAdapter mAdapter;
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    private ViewPager intro_images;
    private ArrayList<String> mImageResources;
    LinearLayoutManager mLayoutManager;
    RecyclerView rvAllParticipants;
    AllParticipantAdapter allAdapter;
    FrameLayout frameLayout,wholeLayout;
    ImageView addParticipant;
    GifImageView progressBar;
    CountDownTimer countDownTimer;
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    public ParticipantDetailfrag(){

    }
    @SuppressLint("ValidFragment")
    public ParticipantDetailfrag(FrameLayout whole){
        wholeLayout = whole;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.participantdetail,container,false);
        title = (TextView) layout.findViewById(R.id.tvParticipantDetailTitle);
        time = (TextView) layout.findViewById(R.id.tvParticipantDetailTime);
        location = (TextView) layout.findViewById(R.id.tvParticipantDetailLocation);
        people = (TextView) layout.findViewById(R.id.tvParticipantDetailPeople);
        name = (TextView) layout.findViewById(R.id.tvParticipantDetailName);
        profession = (TextView) layout.findViewById(R.id.tvParticipantDetailProfession);
        hobbies = (TextView) layout.findViewById(R.id.tvParticipantDetailHobbies);
        intro_images = (ViewPager) layout.findViewById(R.id.pagerParticipant);
        pager_indicator = (LinearLayout) layout.findViewById(R.id.viewPagerCountDots);
        frameLayout = (FrameLayout) layout.findViewById(R.id.frameAllParticiapnts);
        addParticipant = (ImageView) layout.findViewById(R.id.ivPArticipantApproved);
        progressBar = (GifImageView) layout.findViewById(R.id.progressBar);
        addParticipant.setOnClickListener(this);
        dataObject = JsonUtils.objectify(getArguments().getString("dataobject"),DataObject.class);
        Type collectionType = new TypeToken<List<ParticipantModel>>() {
        }.getType();
        participantModels = (List<ParticipantModel>) new Gson()
                .fromJson(getArguments().getString("allmodels"), collectionType);
        data = JsonUtils.objectify(getArguments().getString("participantModel"),ParticipantModel.class);
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
        title.setText(dataObject.getTitle());
        location.setText(MyUtils.getCityName(getContext(),dataObject.getLocation()));
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
            String finalInterests = "";
            for (int i=0;i<data.getUser().getInterests().size();i++){
                finalInterests.concat(data.getUser().getInterests().get(i)+", ");
            }
            hobbies.setText(finalInterests);

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
        mAdapter = new ViewPagerAdapter(getActivity(), mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
        rvAllParticipants = (RecyclerView) layout.findViewById(R.id.rvAllParticipants);
        rvAllParticipants.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAllParticipants.setLayoutManager(this.mLayoutManager);
        if (getArguments().getInt("position") >2){
            rvAllParticipants.scrollToPosition(getArguments().getInt("position")-2);
        }

        allAdapter = new AllParticipantAdapter(getActivity(),participantModels,getArguments().getString("dataobject"),getActivity().getSupportFragmentManager(),wholeLayout);
        rvAllParticipants.setAdapter(allAdapter);
        return layout;

    }
    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
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
                Controller.confirmParticipant(getContext(),dataObject.getId(),data.getId(),mParticipantApproved);
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
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Participant approved succesfully",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("approved error",message);
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
