package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;

import java.text.ParseException;
import java.util.List;

import in.tagbin.mitohealthapp.activity.ChatRequestActivity;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.ProfileActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.ConnectProfileModel;
import in.tagbin.mitohealthapp.model.ConnectUserModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 23/9/16.
 */
public class ChatRequestAdapter extends RecyclerView.Adapter<ChatRequestAdapter.ViewHolder> {
    static Context mContext;
    List<ConnectProfileModel> mModel;
    GifImageView mProgressBar;
    private static final int TYPE_ITEM = 1;

    public ChatRequestAdapter(Context pContext, List<ConnectProfileModel> pModel, GifImageView pProgressBar){
        mContext = pContext;
        mModel = pModel;
        mProgressBar = pProgressBar;
    }
    @Override
    public ChatRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_chat_requests,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ChatRequestAdapter.ViewHolder holder, int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        ((ChatRequestAdapter.ViewHolder) holder).populateData(mModel.get(position), mContext,mProgressBar,position);

    }
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        ConnectProfileModel mModel;
        TextView firendRequest;
        ImageView picture,confirm,decline;
        GifImageView mProgressBar;
        RelativeLayout view;
        int mPosition;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            firendRequest = (TextView) itemView.findViewById(R.id.tvFriendRequest);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            confirm = (ImageView) itemView.findViewById(R.id.ivRequestAccept);
            decline = (ImageView) itemView.findViewById(R.id.ivRequestDecline);
            view = (RelativeLayout) itemView.findViewById(R.id.relativeFriend);
            confirm.setOnClickListener(this);
            decline.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void populateData(ConnectProfileModel pModel, Context pContext,GifImageView pProgressBar,int pPosition){
            mModel = pModel;
            mContext = pContext;
            mProgressBar = pProgressBar;
            mPosition = pPosition;
            firendRequest.setText(pModel.getUser().getFirst_name() +" send you a request");
            if (mModel.getImages() != null){
                if (mModel.getImages().getMaster() != null){
                    ImageLoader.getInstance().loadImage(mModel.getImages().getMaster(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            //personCard.setImageResource(R.drawable.hotel);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            //personCard.setImageResource(R.drawable.hotel);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            picture.setImageBitmap(loadedImage);
                        }
                    });
                }else{
                    picture.setImageResource(R.drawable.person);
                }
            }else{
                picture.setImageResource(R.drawable.person);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ivRequestAccept:
                    ConnectUserModel connectUserModel = new ConnectUserModel();
                    connectUserModel.setId(mModel.getUser().getId());
                    connectUserModel.setActivity(1);
                    mProgressBar.setVisibility(View.VISIBLE);
                    Controller.connectToUser(mContext,connectUserModel,mConnectListener);
                    break;
                case R.id.ivRequestDecline:
                    ConnectUserModel connectUserModel1 = new ConnectUserModel();
                    connectUserModel1.setId(mModel.getUser().getId());
                    connectUserModel1.setActivity(2);
                    mProgressBar.setVisibility(View.VISIBLE);
                    Controller.connectToUser(mContext,connectUserModel1,mRejectistener);
                    break;
                case R.id.relativeFriend:
                    Intent i = new Intent(mContext,ProfileActivity.class);
                    i.putExtra("response", JsonUtils.jsonify(mModel));
                    mContext.startActivity(i);
                    break;
            }
        }
        RequestListener mConnectListener = new RequestListener() {
            @Override
            public void onRequestStarted() {

            }

            @Override
            public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
                Log.d("user connected",responseObject.toString());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, "Approved", Toast.LENGTH_LONG).show();
                        removePosition(mPosition);
//                        Intent i = new Intent(mContext, ChatRequestActivity.class);
//                        mContext.startActivity(i);
//                        ((Activity) mContext).finish();
                    }
                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("user con error",message);
                if (errorCode >= 400 && errorCode < 500) {
                    final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        };
        RequestListener mRejectistener = new RequestListener() {
            @Override
            public void onRequestStarted() {

            }

            @Override
            public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
                Log.d("user connected",responseObject.toString());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext, "Rejected", Toast.LENGTH_LONG).show();
                        removePosition(mPosition);
//                        Intent i = new Intent(mContext, ChatRequestActivity.class);
//                        mContext.startActivity(i);
//                        ((Activity) mContext).finish();
                    }
                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("user con error",message);
                if (errorCode >= 400 && errorCode < 500) {
                    final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        };
    }

}

