package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.Fragments.Lookupfragment;
import in.tagbin.mitohealthapp.activity.CreateEventActivity;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.EventDetailsActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import pl.droidsonroids.gif.GifImageView;

public class LookupAdapter extends RecyclerView.Adapter<LookupAdapter.MyViewHolder> {

    Context mycontext;
    FrameLayout pFrameLayout,mWholeLayout;
    FragmentManager mFragmentManager;
    GifImageView mProgressBar;
    ArrayList<DataObject> newlist = new ArrayList<DataObject>();
    int year, month, day;

    public LookupAdapter(Context context, ArrayList<DataObject> mylist, FrameLayout mFrameLayout, FragmentManager fragmentManager, GifImageView progressBar, FrameLayout wholeLayout) {
        mycontext = context;
        pFrameLayout = mFrameLayout;
        mFragmentManager = fragmentManager;
        newlist = mylist;
        mWholeLayout = wholeLayout;
        mProgressBar = progressBar;
    }

    @Override
    public LookupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.mycontext).inflate(R.layout.item_lookup, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final LookupAdapter.MyViewHolder holder, final int position) {
        holder.eventtitle.setText(newlist.get(position).getEvent_type().getTitle());
        holder.title.setText(newlist.get(position).getTitle());
        final String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(MyUtils.getTimeinMillis(newlist.get(position).getTime()), getCurrentTime(mycontext), DateUtils.MINUTE_IN_MILLIS));
        holder.time.setText(relativeTime);

        holder.capacity.setText("" +(newlist.get(position).getTotal_approved())+"/"+newlist.get(position).getCapacity());
        holder.location.setText(MyUtils.getCityName(mycontext,newlist.get(position).getLocation()));
        Calendar calendar = Calendar.getInstance();
        long output = calendar.getTimeInMillis();
        if (newlist.get(position).getTotal_approved() == newlist.get(position).getCapacity()){
            holder.join.setTextColor(Color.parseColor("#9b9b9b"));
            holder.join.setText("Join Now");
            holder.housefull.setVisibility(View.VISIBLE);
            holder.expired.setVisibility(View.GONE);
            holder.join.setClickable(false);
        }else if (MyUtils.getTimeinMillis(newlist.get(position).getTime()) < output){
            holder.join.setTextColor(Color.parseColor("#9b9b9b"));
            holder.join.setText("Join Now");
            holder.housefull.setVisibility(View.GONE);
            holder.expired.setVisibility(View.VISIBLE);
            holder.join.setClickable(false);
        }else if (newlist.get(position).getMapper().getId() != 0 && !newlist.get(position).getMapper().isConfirm()){
            holder.join.setTextColor(Color.parseColor("#ffffff"));
            holder.join.setText("Pending");
            holder.expired.setVisibility(View.GONE);
            holder.housefull.setVisibility(View.GONE);
            holder.join.setClickable(false);
        }else if (newlist.get(position).getMapper().getId() != 0 && newlist.get(position).getMapper().isConfirm()){
            holder.join.setTextColor(Color.parseColor("#ffffff"));
            holder.join.setText("Accepted");
            holder.expired.setVisibility(View.GONE);
            holder.housefull.setVisibility(View.GONE);
            holder.join.setClickable(false);
        }else{
            holder.join.setTextColor(Color.parseColor("#ffffff"));
            holder.join.setText("Join Now");
            holder.expired.setVisibility(View.GONE);
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
            holder.delete.setVisibility(View.VISIBLE);
            holder.bottomBar.setWeightSum(1);
            //holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
        }

        holder.linearCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mWholeLayout.setVisibility(View.VISIBLE);
//                Bundle bundle = new Bundle();
//                Fragment fragment = new EventDetailsActivity(mWholeLayout);
//                String dataobject = JsonUtils.jsonify(newlist.get(position));
//                bundle.putString("dataobject", dataobject);
//                fragment.setArguments(bundle);
//                FragmentTransaction transaction = mFragmentManager.beginTransaction();
//                transaction.add(R.id.frameLayoutWhole, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                Intent i1 = new Intent(mycontext,EventDetailsActivity.class);
                String dataobject = JsonUtils.jsonify(newlist.get(position));
                i1.putExtra("dataobject", dataobject);
                mycontext.startActivity(i1);

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pFrameLayout.setVisibility(View.VISIBLE);
//                Bundle bundle = new Bundle();
//                Fragment fragment = new CreateEventActivity();
//
//                bundle.putString("response",dataobject);
//                fragment.setArguments(bundle);
//                FragmentTransaction transaction = mFragmentManager.beginTransaction();
//                transaction.add(R.id.frameAddActivity, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                Intent i = new Intent(mycontext,CreateEventActivity.class);
                String dataobject = JsonUtils.jsonify(newlist.get(position));
                i.putExtra("response",dataobject);
                mycontext.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(mycontext,R.style.AppCompatAlertDialogStyle);
                alertDialog1.setTitle("Delete Event");
                alertDialog1.setMessage(" Are you sure you want to delete this event?");
                alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        if (newlist.get(position).isAll())
                            Controller.archiveEvent(mycontext,newlist.get(position).getId(),mArchiveListener);
                        else
                            Controller.deleteEvent(mycontext,newlist.get(position).getId(),mArchiveListener);
                    }
                });
                alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog1.show();

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
            if ((Activity) mycontext != null){
                return;
            }
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
            if ((Activity) mycontext != null){
                return;
            }
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
    RequestListener mArchiveListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("archive event", responseObject.toString());
            Fragment fragment = new Lookupfragment();
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.add(R.id.frameAddActivity, fragment);
            transaction.commit();
            if ((Activity) mycontext != null){
                return;
            }
            ((Activity)mycontext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(View.GONE);

                    Toast.makeText(mycontext,"Event removed successfully",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("archive event error", message);
            if ((Activity) mycontext != null){
                return;
            }
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
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title,eventtitle,join;
        ImageView backimage,edit,delete;
        RelativeLayout linearCard,housefull,expired;
        TextView time;
        TextView capacity;
        LinearLayout bottomBar;
        TextView location;

        public MyViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.time);
            capacity= (TextView) itemView.findViewById(R.id.capacity);
            title= (TextView) itemView.findViewById(R.id.maintitle);
            eventtitle= (TextView) itemView.findViewById(R.id.event_title);
            join = (TextView) itemView.findViewById(R.id.joinEvent);
            linearCard = (RelativeLayout) itemView.findViewById(R.id.linearLookupCard);
            housefull = (RelativeLayout) itemView.findViewById(R.id.relativeHousefull);
            expired = (RelativeLayout) itemView.findViewById(R.id.relativeExpired);
            bottomBar = (LinearLayout) itemView.findViewById(R.id.linearBottomBar);
            backimage= (ImageView) itemView.findViewById(R.id.setimage);
            delete = (ImageView) itemView.findViewById(R.id.ivDelete);
            edit = (ImageView) itemView.findViewById(R.id.ivEdit);
            location= (TextView) itemView.findViewById(R.id.myloc);
        }
    }

}