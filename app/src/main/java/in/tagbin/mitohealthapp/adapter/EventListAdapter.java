package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.BinderActivity;
import in.tagbin.mitohealthapp.activity.EventDetailsActivity;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ParticipantModel;
import in.tagbin.mitohealthapp.model.SetGoalModel;
import in.tagbin.mitohealthapp.model.UserModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 21/10/16.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    Context mContext;
    List<DataObject> mModel;
    GifImageView mProgressBar;
    private static final int TYPE_ITEM = 1;

    public EventListAdapter(Context pContext, List<DataObject> pModel, GifImageView progressBar) {
        mContext = pContext;
        mModel = pModel;
        mProgressBar = progressBar;
    }

    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_event_list, parent, false);
            EventListAdapter.ViewHolder vhItem = new EventListAdapter.ViewHolder(v, viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(EventListAdapter.ViewHolder holder, int position) {
        ((EventListAdapter.ViewHolder) holder).populateData(mModel.get(position),mContext,mProgressBar);
    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        DataObject mModel;
        GifImageView mProgressBar;
        RelativeLayout main;
        TextView eventDate,eventTitle,eventDescription,eventLocationTime,eventUserNumber;
        CircleImageView img1,img2,img3,img4,img5;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            eventDate = (TextView) itemView.findViewById(R.id.tvEventListDate);
            eventTitle = (TextView) itemView.findViewById(R.id.tvEventListTitle);
            eventDescription = (TextView) itemView.findViewById(R.id.tvEventListDescription);
            eventLocationTime = (TextView) itemView.findViewById(R.id.tvEventListTimeLocation);
            eventUserNumber = (TextView) itemView.findViewById(R.id.tvUsersNumber);
            main = (RelativeLayout) itemView.findViewById(R.id.relativeMain);
            main.setOnClickListener(this);
            img1 = (CircleImageView) itemView.findViewById(R.id.cv1);
            img2 = (CircleImageView) itemView.findViewById(R.id.cv2);
            img3 = (CircleImageView) itemView.findViewById(R.id.cv3);
            img4 = (CircleImageView) itemView.findViewById(R.id.cv4);
            img5 = (CircleImageView) itemView.findViewById(R.id.cv5);
        }

        public void populateData(DataObject pModel, Context pContext,GifImageView progressBar) {
            mModel = pModel;
            mProgressBar = progressBar;
            mContext = pContext;
            eventTitle.setText(mModel.getTitle());
            eventDescription.setText(mModel.getDescription());
            eventDate.setText(getValidDate(mModel.getEvent_time()));
            final String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(MyUtils.getTimeinMillis(mModel.getTime()), getCurrentTime(mContext), DateUtils.MINUTE_IN_MILLIS));
            eventLocationTime.setText(relativeTime+" "+getValidTime(mModel.getTime())+", "+MyUtils.getStateName(mContext,mModel.getLocation()));
            Controller.getParticipants(mContext,mModel.getId(),mParticipantListener);
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
                //mModel.clear();
                final List<ParticipantModel> da = (List<ParticipantModel>) new Gson()
                        .fromJson(responseObject.toString(), collectionType);

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (da.size() <= 0){
                            img1.setVisibility(View.GONE);
                            img2.setVisibility(View.GONE);
                            img3.setVisibility(View.GONE);
                            img4.setVisibility(View.GONE);
                            img5.setVisibility(View.GONE);
                            eventUserNumber.setVisibility(View.GONE);
                        }else if (da.size() > 0){
                            img1.setVisibility(View.VISIBLE);
                            img2.setVisibility(View.GONE);
                            img3.setVisibility(View.GONE);
                            img4.setVisibility(View.GONE);
                            img5.setVisibility(View.GONE);
                            eventUserNumber.setVisibility(View.GONE);
                            if (da.get(0).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(0).getUser().getProfile().getImages().getMaster()).into(img1);

                        }else if (da.size() >1){
                            img1.setVisibility(View.VISIBLE);
                            img2.setVisibility(View.VISIBLE);
                            img3.setVisibility(View.GONE);
                            img4.setVisibility(View.GONE);
                            img5.setVisibility(View.GONE);
                            eventUserNumber.setVisibility(View.GONE);
                            if (da.get(0).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(0).getUser().getProfile().getImages().getMaster()).into(img1);
                            if (da.get(1).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(1).getUser().getProfile().getImages().getMaster()).into(img2);
                        }else if (da.size() > 2){
                            img1.setVisibility(View.VISIBLE);
                            img2.setVisibility(View.VISIBLE);
                            img3.setVisibility(View.VISIBLE);
                            img4.setVisibility(View.GONE);
                            img5.setVisibility(View.GONE);
                            eventUserNumber.setVisibility(View.GONE);
                            if (da.get(0).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(0).getUser().getProfile().getImages().getMaster()).into(img1);
                            if (da.get(1).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(1).getUser().getProfile().getImages().getMaster()).into(img2);
                            if (da.get(2).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(2).getUser().getProfile().getImages().getMaster()).into(img3);
                        }else if (da.size() > 3){
                            img1.setVisibility(View.VISIBLE);
                            img2.setVisibility(View.VISIBLE);
                            img3.setVisibility(View.VISIBLE);
                            img4.setVisibility(View.VISIBLE);
                            img5.setVisibility(View.GONE);
                            eventUserNumber.setVisibility(View.GONE);
                            if (da.get(0).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(0).getUser().getProfile().getImages().getMaster()).into(img1);
                            if (da.get(1).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(1).getUser().getProfile().getImages().getMaster()).into(img2);
                            if (da.get(2).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(2).getUser().getProfile().getImages().getMaster()).into(img3);
                            if (da.get(3).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(3).getUser().getProfile().getImages().getMaster()).into(img4);
                        }else if (da.size()  >4){
                            img1.setVisibility(View.VISIBLE);
                            img2.setVisibility(View.VISIBLE);
                            img3.setVisibility(View.VISIBLE);
                            img4.setVisibility(View.VISIBLE);
                            img5.setVisibility(View.VISIBLE);
                            eventUserNumber.setVisibility(View.GONE);
                            if (da.get(0).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(0).getUser().getProfile().getImages().getMaster()).into(img1);
                            if (da.get(1).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(1).getUser().getProfile().getImages().getMaster()).into(img2);
                            if (da.get(2).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(2).getUser().getProfile().getImages().getMaster()).into(img3);
                            if (da.get(3).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(3).getUser().getProfile().getImages().getMaster()).into(img4);
                            if (da.get(4).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(4).getUser().getProfile().getImages().getMaster()).into(img5);
                        }else if (da.size() > 5){
                            img1.setVisibility(View.VISIBLE);
                            img2.setVisibility(View.VISIBLE);
                            img3.setVisibility(View.VISIBLE);
                            img4.setVisibility(View.VISIBLE);
                            img5.setVisibility(View.VISIBLE);
                            eventUserNumber.setVisibility(View.VISIBLE);
                            if (da.get(0).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(0).getUser().getProfile().getImages().getMaster()).into(img1);
                            if (da.get(1).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(1).getUser().getProfile().getImages().getMaster()).into(img2);
                            if (da.get(2).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(2).getUser().getProfile().getImages().getMaster()).into(img3);
                            if (da.get(3).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(3).getUser().getProfile().getImages().getMaster()).into(img4);
                            if (da.get(4).getUser().getProfile().getImages().getMaster() != null)
                                Picasso.with(mContext).load(da.get(4).getUser().getProfile().getImages().getMaster()).into(img5);
                            eventUserNumber.setText(""+(da.size() - 5));
                        }
                    }
                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("All participants error",message);
                if (errorCode >= 400 && errorCode < 500) {
                    final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //progressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.relativeMain:
                    Intent i1 = new Intent(mContext,EventDetailsActivity.class);
                    String dataobject = JsonUtils.jsonify(mModel);
                    i1.putExtra("dataobject", dataobject);
                    mContext.startActivity(i1);
                    break;
            }
        }
    }
    public static String getValidDate(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
            String da = dateFormat.format(date);
            return da;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getValidTime(String validDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getDisplayName()));
            Date date = simpleDateFormat.parse(validDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            String da = dateFormat.format(date);
            return da;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public long getCurrentTime(Context ctx){

        long sec = System.currentTimeMillis();
        return sec;
    }

}
