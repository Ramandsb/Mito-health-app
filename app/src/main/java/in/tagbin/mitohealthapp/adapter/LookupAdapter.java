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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    GifImageView mProgressBar;
    List<DataObject> newlist = new ArrayList<DataObject>();
    int year, month, day;

    public LookupAdapter(Context context, List<DataObject> mylist, GifImageView progressBar) {
        mycontext = context;
        newlist = mylist;
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
        ((LookupAdapter.MyViewHolder) holder).populateData(newlist.get(position), mycontext,mProgressBar);
    }

    @Override
    public int getItemCount() {
        return newlist.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title/*,eventtitle,join*/;
        ImageView backimage/*,edit,delete*/;
        RelativeLayout linearCard,housefull,expired;
        TextView time;
        TextView capacity,description;
        //LinearLayout bottomBar;
        //TextView location;
        Context mycontext;
        DataObject newlist;
        GifImageView mProgressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.time);
            capacity= (TextView) itemView.findViewById(R.id.capacity);
            title= (TextView) itemView.findViewById(R.id.maintitle);
            description= (TextView) itemView.findViewById(R.id.tvEventDescription);
            //eventtitle= (TextView) itemView.findViewById(R.id.event_title);
            //join = (TextView) itemView.findViewById(R.id.joinEvent);
            linearCard = (RelativeLayout) itemView.findViewById(R.id.linearLookupCard);
            housefull = (RelativeLayout) itemView.findViewById(R.id.relativeHousefull);
            expired = (RelativeLayout) itemView.findViewById(R.id.relativeExpired);
            //bottomBar = (LinearLayout) itemView.findViewById(R.id.linearBottomBar);
            backimage= (ImageView) itemView.findViewById(R.id.setimage);
            //delete = (ImageView) itemView.findViewById(R.id.ivDelete);
            //edit = (ImageView) itemView.findViewById(R.id.ivEdit);
            //location= (TextView) itemView.findViewById(R.id.myloc);
        }
        public void populateData(DataObject dataObject, Context context, GifImageView progressBar){
            newlist = dataObject;
            mycontext = context;
            mProgressBar = progressBar;
//            eventtitle.setText(newlist.getEvent_type().getTitle());
            String output1 = newlist.getTitle().substring(0, 1).toUpperCase() + newlist.getTitle().substring(1);
            title.setText(output1);
            final String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(MyUtils.getTimeinMillis(newlist.getTime()), getCurrentTime(mycontext), DateUtils.MINUTE_IN_MILLIS));
            time.setText(MyUtils.getValidDateForLookup(newlist.getTime()));
            description.setText(newlist.getDescription());
            capacity.setText("" +(newlist.getTotal_approved())+"/"+newlist.getCapacity());
//            location.setText(MyUtils.getCityName(mycontext,newlist.getLocation()));
            Calendar calendar = Calendar.getInstance();
            long output = calendar.getTimeInMillis();
            if (newlist.getTotal_approved() == newlist.getCapacity()){
//                join.setTextColor(Color.parseColor("#9b9b9b"));
//                join.setText("Join Now");
                housefull.setVisibility(View.VISIBLE);
                expired.setVisibility(View.GONE);
//                join.setClickable(false);
            }else if (MyUtils.getTimeinMillis(newlist.getTime()) < output){
//                join.setTextColor(Color.parseColor("#9b9b9b"));
//                join.setText("Join Now");
                housefull.setVisibility(View.GONE);
                expired.setVisibility(View.GONE);
//                join.setClickable(false);
            }else if (newlist.getMapper().getId() != 0 && !newlist.getMapper().isConfirm()){
//                join.setTextColor(Color.parseColor("#ffffff"));
//                join.setText("Pending");
                expired.setVisibility(View.GONE);
                housefull.setVisibility(View.GONE);
//                join.setClickable(false);
            }else if (newlist.getMapper().getId() != 0 && newlist.getMapper().isConfirm()){
//                join.setTextColor(Color.parseColor("#ffffff"));
//                join.setText("Accepted");
                expired.setVisibility(View.GONE);
                housefull.setVisibility(View.GONE);
//                join.setClickable(false);
            }else{
//                join.setTextColor(Color.parseColor("#ffffff"));
//                join.setText("Join Now");
                expired.setVisibility(View.GONE);
//                join.setClickable(true);
                housefull.setVisibility(View.GONE);
//                join.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mProgressBar.setVisibility(View.VISIBLE);
//                        Controller.joinEvent(mycontext, newlist.getId(), mJoinEventListener);
//                    }
//                });
            }
//            if (newlist.isAll()) {
////                join.setVisibility(View.VISIBLE);
//                //holder.date.setVisibility(View.VISIBLE);
//                //holder.delete.setVisibility(View.GONE);
////                delete.setVisibility(View.VISIBLE);
////                bottomBar.setWeightSum(2);
////                //holder.date.setText(MyUtils.getValidDate(newlist.get(position).getTime()));
////               edit.setVisibility(View.GONE);
//            } else {
////                join.setVisibility(View.GONE);
////                delete.setVisibility(View.VISIBLE);
////                bottomBar.setWeightSum(1);
////                //holder.delete.setVisibility(View.VISIBLE);
////                edit.setVisibility(View.VISIBLE);
//            }

            linearCard.setOnClickListener(new View.OnClickListener() {
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
                    String dataobject = JsonUtils.jsonify(newlist);
                    i1.putExtra("dataobject", dataobject);
                    mycontext.startActivity(i1);

                }
            });

//            edit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                pFrameLayout.setVisibility(View.VISIBLE);
////                Bundle bundle = new Bundle();
////                Fragment fragment = new CreateEventActivity();
////
////                bundle.putString("response",dataobject);
////                fragment.setArguments(bundle);
////                FragmentTransaction transaction = mFragmentManager.beginTransaction();
////                transaction.add(R.id.frameAddActivity, fragment);
////                transaction.addToBackStack(null);
////                transaction.commit();
//                    Intent i = new Intent(mycontext,CreateEventActivity.class);
//                    String dataobject = JsonUtils.jsonify(newlist);
//                    i.putExtra("response",dataobject);
//                    mycontext.startActivity(i);
//                }
//            });
//            delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(mycontext,R.style.AppCompatAlertDialogStyle);
//                    alertDialog1.setTitle("Delete Event");
//                    alertDialog1.setMessage(" Are you sure you want to delete this event?");
//                    alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            mProgressBar.setVisibility(View.VISIBLE);
//                            if (newlist.isAll())
//                                Controller.archiveEvent(mycontext,newlist.getId(),mArchiveListener);
//                            else
//                                Controller.deleteEvent(mycontext,newlist.getId(),mArchiveListener);
//                        }
//                    });
//                    alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    alertDialog1.show();
//
//                }
//            });
            if (newlist.getPicture() != null) {
                ImageLoader.getInstance().loadImage(newlist.getPicture(), new ImageLoadingListener() {
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


                        backimage.setImageBitmap(loadedImage);

                    }
                });


            } else {
                backimage.setBackgroundResource(R.drawable.hotel);
            }
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
//                        join.setText("Pending");
//                        join.setTextColor(Color.parseColor("#ffffff"));
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
        RequestListener mArchiveListener = new RequestListener() {
            @Override
            public void onRequestStarted() {

            }

            @Override
            public void onRequestCompleted(Object responseObject) {
                Log.d("archive event", responseObject.toString());
//                Fragment fragment = new Lookupfragment();
//                FragmentTransaction transaction = mFragmentManager.beginTransaction();
//                transaction.add(R.id.frameAddActivity, fragment);
//                transaction.commit();
//                ((Activity)mycontext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mProgressBar.setVisibility(View.GONE);
//                        Toast.makeText(mycontext,"Event removed successfully",Toast.LENGTH_LONG).show();
//                    }
//                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("archive event error", message);
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

}
