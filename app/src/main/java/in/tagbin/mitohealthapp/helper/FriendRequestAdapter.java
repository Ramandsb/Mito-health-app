package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.CollapsableLogging;
import in.tagbin.mitohealthapp.FoodDetails;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 23/9/16.
 */
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    static Context mContext;
    List<RecommendationModel.MealsModel> mModel;
    GifImageView mProgressBar;
    private static final int TYPE_ITEM = 1;

    public FriendRequestAdapter(Context pContext, List<RecommendationModel.MealsModel> pModel,GifImageView pProgressBar){
        mContext = pContext;
        mModel = pModel;
        mProgressBar = pProgressBar;
    }
    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_friend_requests,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(FriendRequestAdapter.ViewHolder holder, int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        ((FriendRequestAdapter.ViewHolder) holder).populateData(mModel.get(position), mContext,mProgressBar);

    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        RecommendationModel.MealsModel mModel;
        TextView firendRequest;
        ImageView picture,confirm,decline;
        GifImageView mProgressBar;
        RelativeLayout view;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            firendRequest = (TextView) itemView.findViewById(R.id.tvFriendRequest);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            confirm = (ImageView) itemView.findViewById(R.id.ivRequestAccept);
            decline = (ImageView) itemView.findViewById(R.id.ivRequestDecline);
        }

        public void populateData(RecommendationModel.MealsModel pModel, Context pContext,GifImageView pProgressBar){
            mModel = pModel;
            mContext = pContext;
            mProgressBar = pProgressBar;

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){

            }
        }

    }

}

