package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.MyActivityCardfrag;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.model.DataObject;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context mycontext;
    FrameLayout pFrameLayout;
    FragmentManager mFragmentManager;
    ArrayList<DataObject> newlist=new ArrayList<DataObject>();
    int year,month,day;

    public MyAdapter(Context context, ArrayList<DataObject> mylist, FrameLayout mFrameLayout, FragmentManager fragmentManager) {
        mycontext=context;
        pFrameLayout = mFrameLayout;
        mFragmentManager = fragmentManager;
        newlist=mylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.mycontext).inflate(R.layout.eventdataitem, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.eventtitle.setText(newlist.get(position).etitle);
        holder.title.setText(newlist.get(position).getTitle());
        holder.time.setText(MyUtils.getValidTime(newlist.get(position).getTime()));
        holder.capacity.setText(""+newlist.get(position).getCapacity());
        //holder.location.setText(MyUtils.getCityName(mycontext,newlist.get(position).getLocation()));
        if (newlist.get(position).isAll()){
            holder.confirm.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.GONE);
            holder.date.setText(MyUtils.getValidDate(newlist.get(position).getTime()));
            holder.edit.setVisibility(View.GONE);
        }else{
            holder.confirm.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
            holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
        }
        holder.linearCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pFrameLayout.setVisibility(View.VISIBLE);

                    Bundle bundle = new Bundle();
                    Fragment fragment = new MyActivityCardfrag();
                    String dataobject = JsonUtils.jsonify(newlist.get(position));
                if (newlist.get(position).isAll()){
                    bundle.putString("myact","myact");
                }else{
                    bundle.putString("myact","all");
                }
                    bundle.putString("dataobject",dataobject);
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.add(R.id.frameAddActivity, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

            }
        });
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.joinEvent(mycontext,newlist.get(position).getId(),mJoinEventListener);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.time.setClickable(true);
                holder.time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });
        if (newlist.get(position).getPicture() != null){

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

        }else{
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
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("join event error", message);
        }
    };

}
