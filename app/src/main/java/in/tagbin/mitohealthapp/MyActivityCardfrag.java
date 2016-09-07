package in.tagbin.mitohealthapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.ParticipantAdapter;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ParticipantModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 9/8/16.
 */
public class MyActivityCardfrag extends Fragment implements View.OnClickListener {
    TextView title,type,time,people,location,date,heading,selectedPeople,interested,approved,left;
    DataObject data;
    ImageView backImage,edit;
    LinearLayout linearFriends;
    RecyclerView recyclerView;
    List<ParticipantModel> mModel,da;
    FrameLayout frameLayout;
    GifImageView progressBar;
    StaggeredGridLayoutManager mylayoutmanager;
    ParticipantAdapter mAdapter;
    RelativeLayout invite,join,interestedRelative,approvedRelative;
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.myactivitycard,container,false);
        type = (TextView) layout.findViewById(R.id.tvMyActivityType);
        title = (TextView) layout.findViewById(R.id.tvMyActivityTitle);
        time = (TextView) layout.findViewById(R.id.tvMyActivityTime);
        people = (TextView) layout.findViewById(R.id.tvMyActivityPeople);
        date = (TextView) layout.findViewById(R.id.tvMyActivityDate);
        location = (TextView) layout.findViewById(R.id.tvMyActivityLocation);
        recyclerView = (RecyclerView) layout.findViewById(R.id.rvParticipants);
        edit = (ImageView) layout.findViewById(R.id.ivMyActivityEdit);
        heading = (TextView) layout.findViewById(R.id.tvMyActivityHeading);
        frameLayout = (FrameLayout) layout.findViewById(R.id.frameParticipantDetail);
        backImage = (ImageView) layout.findViewById(R.id.ivMyActivityImage);
        linearFriends = (LinearLayout) layout.findViewById(R.id.lineaFriendRequsts);
        selectedPeople = (TextView) layout.findViewById(R.id.tvSelectedPeople);
        invite = (RelativeLayout) layout.findViewById(R.id.relativeInviteFriends);
        join = (RelativeLayout) layout.findViewById(R.id.relativeJoinFriends);
        interested = (TextView) layout.findViewById(R.id.tvInterestedFriends);
        interestedRelative = (RelativeLayout) layout.findViewById(R.id.relativeInterested);
        approvedRelative = (RelativeLayout) layout.findViewById(R.id.relativeApproved);
        approved = (TextView) layout.findViewById(R.id.tvApprovedFriends);
        left = (TextView) layout.findViewById(R.id.tvLeftFriends);
        interestedRelative.setOnClickListener(this);
        approvedRelative.setOnClickListener(this);
        final String dataobject = getArguments().getString("dataobject");
        progressBar = (GifImageView) layout.findViewById(R.id.progressBar);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Fragment fragment = new AddActivityfrag();
                bundle.putString("response",dataobject);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frameAddActivity, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        join.setOnClickListener(this);
        invite.setOnClickListener(this);
        data = JsonUtils.objectify(dataobject,DataObject.class);
        title.setText(data.getTitle());
        type.setText(data.getEvent_type().getTitle());
//        final String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(MyUtils.getTimeinMillis(data.getTime()), getCurrentTime(getContext()), DateUtils.MINUTE_IN_MILLIS));
//        time.setText(relativeTime);
        location.setText(MyUtils.getCityName(getContext(),data.getLocation()));
        people.setText(""+data.getCapacity());
        interested.setText(""+data.getTotal_request());
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
        mModel = new ArrayList<ParticipantModel>();
//        mModel.add(new ParticipantModel(1,R.drawable.hotel,"Varun Dhawan","Actor","Swimming Watching movies",25,1));
//        mModel.add(new ParticipantModel(2,R.drawable.hotel,"Varun Dhawan","Indian Actor","Swimming Watching movies",23,2));
//        mModel.add(new ParticipantModel(3,R.drawable.hotel,"Varun Dhawan","Australia Actor","Swimming Watching movies",24,3));
//        mModel.add(new ParticipantModel(4,R.drawable.hotel,"Aasaqt","Engineer","Swimming Watching movies",26,4));
//        mModel.add(new ParticipantModel(5,R.drawable.hotel,"Chetan","IT Head","Swimming Watching movies",27,5));
//        mModel.add(new ParticipantModel(6,R.drawable.hotel,"Arun Jaitley","Minister","Swimming Watching movies",22,6));
//        mModel.add(new ParticipantModel(7,R.drawable.hotel,"Narendra Modi","PM","Swimming Watching movies",24,7));
//        mModel.add(new ParticipantModel(8,R.drawable.hotel,"Pranab Mukherjee","President","Swimming Watching movies",26,8));
//        mModel.add(new ParticipantModel(9,R.drawable.hotel,"Varun Dhawan","Actor","Swimming Watching movies",29,9));
//        mModel.add(new ParticipantModel(10,R.drawable.hotel,"Akshay","Actor","Swimming Watching movies",20,10));
//        mModel.add(new ParticipantModel(11,R.drawable.hotel,"Sonakshi","Indian Actress","Swimming Watching movies",21,11));
//        mModel.add(new ParticipantModel(12,R.drawable.hotel,"Brad Pitt","Actor","Swimming Watching movies",23,12));
//        mModel.add(new ParticipantModel(13,R.drawable.hotel,"Varun Dhawan","Actor","Swimming Watching movies",26,13));
//        mModel.add(new ParticipantModel(14,R.drawable.hotel,"Varun Dhawan","Actor","Swimming Watching movies",25,14));

        mAdapter = new ParticipantAdapter(getContext(),mModel,getActivity().getSupportFragmentManager(),frameLayout,dataobject);
        recyclerView.setLayoutManager(this.mylayoutmanager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(mAdapter);
        if (data.isAll()){
            date.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            linearFriends.setVisibility(View.VISIBLE);
            heading.setText("All Event");
            Controller.getParticipants(getContext(),data.getId(),mParticipantListener);
            selectedPeople.setVisibility(View.VISIBLE);
            date.setText(MyUtils.getValidDate(data.getTime()));
            linearFriends.setWeightSum(5);
            join.setVisibility(View.VISIBLE);
            approvedRelative.setClickable(false);
            interestedRelative.setClickable(false);
        }else{
            date.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            heading.setText("My Event");
            Controller.getParticipants(getContext(),data.getId(),mParticipantListener);
            selectedPeople.setVisibility(View.VISIBLE);
            selectedPeople.setText("Interested People");
            linearFriends.setVisibility(View.VISIBLE);
            join.setVisibility(View.GONE);
            linearFriends.setWeightSum(4);
            approvedRelative.setClickable(true);
            interestedRelative.setClickable(true);
        }
        return layout;
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
                mModel.add(da.get(i));
            }
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeJoinFriends:
                progressBar.setVisibility(View.VISIBLE);
                Controller.joinEvent(getContext(),data.getId(),mJoinEventListener);
                break;
            case R.id.relativeInviteFriends:
                break;
            case R.id.relativeApproved:
                selectedPeople.setText("Approved People");
                progressBar.setVisibility(View.VISIBLE);
                Controller.getParticipants(getContext(),data.getId(),mParticipantApprovedListener);
                break;
            case R.id.relativeInterested:
                selectedPeople.setText("Interested People");
                progressBar.setVisibility(View.VISIBLE);
                Controller.getParticipants(getContext(),data.getId(),mParticipantListener);
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
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
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
    RequestListener mJoinEventListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("join event", responseObject.toString());
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Event joined succcesfully",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("join event error", message);
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
    public long getCurrentTime(Context ctx){

        long sec = System.currentTimeMillis();
        return sec;
    }
}
