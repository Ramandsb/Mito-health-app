package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.AddActivityfrag;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.MyActivityCardfrag;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import pl.droidsonroids.gif.GifImageView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context mycontext;
    FrameLayout pFrameLayout;
    FragmentManager mFragmentManager;
    GifImageView mProgressBar;
    ArrayList<DataObject> newlist = new ArrayList<DataObject>();
    int year, month, day;

    public MyAdapter(Context context, ArrayList<DataObject> mylist, FrameLayout mFrameLayout, FragmentManager fragmentManager, GifImageView progressBar) {
        mycontext = context;
        pFrameLayout = mFrameLayout;
        mFragmentManager = fragmentManager;
        newlist = mylist;
        mProgressBar = progressBar;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.mycontext).inflate(R.layout.eventdataitem, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.eventtitle.setText(newlist.get(position).getEvent_type().getTitle());
        holder.title.setText(newlist.get(position).getTitle());
        final String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(MyUtils.getTimeinMillis(newlist.get(position).getTime()), getCurrentTime(mycontext), DateUtils.MINUTE_IN_MILLIS));
        holder.time.setText(relativeTime);

        holder.capacity.setText("" +(newlist.get(position).getTotal_approved())+"/"+newlist.get(position).getCapacity());
        holder.location.setText(MyUtils.getCityName(mycontext,newlist.get(position).getLocation()));
        if (newlist.get(position).getTotal_approved() == newlist.get(position).getCapacity()){
            holder.join.setTextColor(Color.parseColor("#9b9b9b"));
            holder.join.setText("Join Now");
            holder.housefull.setVisibility(View.VISIBLE);
            holder.join.setClickable(false);
        }else if (newlist.get(position).getMapper().getId() != 0 && !newlist.get(position).getMapper().isConfirm()){
            holder.join.setTextColor(Color.parseColor("#ffffff"));
            holder.join.setText("Pending");
            holder.housefull.setVisibility(View.GONE);
            holder.join.setClickable(false);
        }else if (newlist.get(position).getMapper().getId() != 0 && newlist.get(position).getMapper().isConfirm()){
            holder.join.setTextColor(Color.parseColor("#ffffff"));
            holder.join.setText("Accepted");
            holder.housefull.setVisibility(View.GONE);
            holder.join.setClickable(false);
        }else{
            holder.join.setTextColor(Color.parseColor("#ffffff"));
            holder.join.setText("Join Now");
            holder.join.setClickable(true);
            holder.housefull.setVisibility(View.GONE);
            holder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Controller.joinEvent(mycontext, newlist.get(position).getId(), mJoinEventListener);
                }
            });
        }
        if (newlist.get(position).isAll()) {
            holder.join.setVisibility(View.VISIBLE);
            //holder.date.setVisibility(View.VISIBLE);
            //holder.delete.setVisibility(View.GONE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.bottomBar.setWeightSum(2);
            //holder.date.setText(MyUtils.getValidDate(newlist.get(position).getTime()));
            holder.edit.setVisibility(View.GONE);
        } else {
            holder.join.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            holder.bottomBar.setWeightSum(1);
            //holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
        }
        holder.linearCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pFrameLayout.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                Fragment fragment = new MyActivityCardfrag();
                String dataobject = JsonUtils.jsonify(newlist.get(position));
                bundle.putString("dataobject", dataobject);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.add(R.id.frameAddActivity, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Fragment fragment = new AddActivityfrag();
                String dataobject = JsonUtils.jsonify(newlist.get(position));
                bundle.putString("response",dataobject);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.add(R.id.frameAddActivity, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        if (newlist.get(position).getPicture() != null) {

            ImageLoader.getInstance().loadImage(newlist.get(position).getPicture(), new ImageLoadingListener() {
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


                    holder.backimage.setImageBitmap(loadedImage);

                }
            });

        } else {
            holder.backimage.setBackgroundResource(R.drawable.hotel);
        }

    }

    @Override
    public int getItemCount() {
        return newlist.size();
    }

    RequestListener mJoinEventListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("join event", responseObject.toString());
            ((Activity)mycontext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(mycontext,"Event joined successfully",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("join event error", message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) mycontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mycontext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) mycontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mycontext, "Internet connection error", Toast.LENGTH_LONG).show();
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
