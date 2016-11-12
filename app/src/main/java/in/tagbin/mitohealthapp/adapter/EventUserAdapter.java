package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.EventDetailsActivity;
import in.tagbin.mitohealthapp.activity.EventsUserDetailsActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ParticipantModel;

/**
 * Created by aasaqt on 9/8/16.
 */
public class EventUserAdapter extends RecyclerView.Adapter<EventUserAdapter.ViewHolder>{
    Context mContext;
    List<ParticipantModel> mModel;
    FragmentManager mFragment;
    FrameLayout mFrameLayout;
    String dataObject;
    int finalPosition;
    private static final int TYPE_ITEM = 1;

    public EventUserAdapter(Context pContext, List<ParticipantModel> pModel, FragmentManager fragmentManager, FrameLayout frameLayout, String dataObject){
        mContext = pContext;
        mModel = pModel;
        mFragment = fragmentManager;
        mFrameLayout = frameLayout;
        this.dataObject = dataObject;
    }
    @Override
    public EventUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_event_user,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(EventUserAdapter.ViewHolder holder, final int position) {

        final DataObject dataObject1 = JsonUtils.objectify(dataObject,DataObject.class);
        holder.approveParticipant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalPosition = position;
                Controller.confirmParticipant(mContext,dataObject1.getId(),mModel.get(position).getId(),mParticipantApproved);
            }
        });
        ((EventUserAdapter.ViewHolder) holder).populateData(mModel.get(position),mContext,mModel,mFragment,mFrameLayout,dataObject,position);
    }
    RequestListener mParticipantApproved = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("participant approved",responseObject.toString());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,"Participant approved succesfully",Toast.LENGTH_LONG).show();
                    //EventDetailsActivity.onResume1();
                    removePosition(finalPosition);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("approved error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public void removePosition(int adapterPosition) {
        mModel.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        //notifyItemRangeChanged(adapterPosition, getItemCount());

    }
    @Override
    public int getItemCount() {
        return mModel.size();
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        Context mContext;
        ParticipantModel mModel;
        ImageView circleImageView;
        TextView age,name,approveParticipant;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            circleImageView = (ImageView) itemView.findViewById(R.id.circleView);
            approveParticipant = (TextView) itemView.findViewById(R.id.ivApproveParticipant);
            //userApproved = (ImageView) itemView.findViewById(R.id.ivUserApproved);
            age = (TextView) itemView.findViewById(R.id.tvUserAge);
            name= (TextView) itemView.findViewById(R.id.tvUserName);
            //circleImageView.setBorderColor(Color.parseColor("#ffffff"));


        }

        public void populateData(ParticipantModel pModel, Context pContext, final List<ParticipantModel> data, final FragmentManager pFragment, final FrameLayout mFrameLayout, final String dataObj, final int position){
            mModel = pModel;
            mContext = pContext;
            if (mModel.getUser().getProfile().getImages() != null && mModel.getUser().getProfile().getImages().getMaster() != null){
                ImageLoader.getInstance().loadImage(mModel.getUser().getProfile().getImages().getMaster(), new ImageLoadingListener() {
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
                         circleImageView.setImageBitmap(loadedImage);
                    }
                });

            }else{
                circleImageView.setImageResource(R.drawable.hotel);
            }
            String output = mModel.getUser().getFirst_name().substring(0, 1).toUpperCase() + mModel.getUser().getFirst_name().substring(1);
            name.setText(output);
            //Log.d("location",MyUtils.getStateName(mContext,mModel.getUser().getProfile().getLocation()));
            String gender;
            if (mModel.getUser().getProfile().getGender().toLowerCase().equals("m")){
                gender = "Male";
            }else{
                gender = "Female";
            }
            final DataObject dataObject = JsonUtils.objectify(dataObj,DataObject.class);
            if (dataObject.isAll() || mModel.isConfirm()){
                approveParticipant.setVisibility(View.GONE);
            }else{
                approveParticipant.setVisibility(View.VISIBLE);
            }

            age.setText(mModel.getUser().getProfile().getAge()+", "+ gender);
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mFrameLayout.setVisibility(View.VISIBLE);
//
//                    Bundle bundle = new Bundle();
//                    Fragment fragment = new EventsUserDetailsActivity(mFrameLayout);
//                    String dataobject = JsonUtils.jsonify(mModel);
//                    String allModels = JsonUtils.jsonify(data);
//                    bundle.putString("participantModel",dataobject);
//                    bundle.putString("allmodels",allModels);
//                    bundle.putString("dataobject",dataObj);
//                    bundle.putInt("position",position);
//                    fragment.setArguments(bundle);
//                    FragmentTransaction transaction = pFragment.beginTransaction();
//                    transaction.add(R.id.frameLayoutWhole, fragment);
//                    transaction.commit();
                    Intent i = new Intent(mContext,EventsUserDetailsActivity.class);
                    String dataobject = JsonUtils.jsonify(mModel);
                    String allModels = JsonUtils.jsonify(data);
                    i.putExtra("participantModel",dataobject);
                    i.putExtra("allmodels",allModels);
                    i.putExtra("dataobject",dataObj);
                    i.putExtra("positon",position);
                    mContext.startActivity(i);

                }
            });
        }
    }
}
