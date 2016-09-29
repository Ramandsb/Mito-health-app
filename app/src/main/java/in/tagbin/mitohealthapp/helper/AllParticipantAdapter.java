package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import in.tagbin.mitohealthapp.ParticipantDetailfrag;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.ParticipantModel;

/**
 * Created by aasaqt on 10/8/16.
 */
public class AllParticipantAdapter extends RecyclerView.Adapter<AllParticipantAdapter.ViewHolder>  {
    Context mContext;
    List<ParticipantModel> mModel;
    FragmentManager mFragment;
    FrameLayout mFrameLayout;
    String dataObject;
    private static final int TYPE_ITEM = 1;

    public AllParticipantAdapter(Context pContext, List<ParticipantModel> pModel, String dataObject, FragmentManager fragmentManager, FrameLayout frameLayout){
        mContext = pContext;
        mModel = pModel;
        mFragment = fragmentManager;
        mFrameLayout = frameLayout;
        this.dataObject = dataObject;
    }

    @Override
    public AllParticipantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_allparticipant,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AllParticipantAdapter.ViewHolder holder, int position) {
        ((AllParticipantAdapter.ViewHolder) holder).populateData(mModel.get(position),mContext,mModel,mFragment,mFrameLayout,dataObject,position);
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
        ParticipantModel mModel;
        Context mContext;
        ImageView imageView;

        public ViewHolder(View itemView,int viewTpe) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivAllParticipants);
        }
        public void populateData(ParticipantModel pModel, Context pContext, final List<ParticipantModel> data, final FragmentManager pFragment, final FrameLayout mFrameLayout, final String dataObj, final int position){
            mModel = pModel;
            mContext = pContext;
            if (mModel.getUser().getProfile().getImages() != null && mModel.getUser().getProfile().getImages().getMaster() !=null){
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
                        imageView.setImageBitmap(loadedImage);
                    }
                });
            }else{
                imageView.setImageResource(R.drawable.hotel);
            }


            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mFrameLayout.setVisibility(View.VISIBLE);

//                    Bundle bundle = new Bundle();
//                    Fragment fragment = new ParticipantDetailfrag();
//
//                    bundle.putString("participantModel",dataobject);
//                    bundle.putString("allmodels",allModels);
//                    bundle.putString("dataobject",dataObj);
//                    bundle.putInt("positon",position);
//                    fragment.setArguments(bundle);
//                    FragmentTransaction transaction = pFragment.beginTransaction();
//                    transaction.add(R.id.frameLayoutWhole, fragment);
//                    transaction.commit();
                    Intent i = new Intent(mContext,ParticipantDetailfrag.class);
                    String dataobject = JsonUtils.jsonify(mModel);
                    String allModels = JsonUtils.jsonify(data);
                    i.putExtra("participantModel",dataobject);
                    i.putExtra("allmodels",allModels);
                    i.putExtra("dataobject",dataObj);
                    i.putExtra("positon",position);
                    mContext.startActivity(i);
                    ((Activity) mContext).finish();
                }
            });
        }
    }
}
