package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.ParticipantDetailfrag;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.ParticipantModel;

/**
 * Created by aasaqt on 9/8/16.
 */
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder>{
    Context mContext;
    List<ParticipantModel> mModel;
    FragmentManager mFragment;
    FrameLayout mFrameLayout;
    String dataObject;
    private static final int TYPE_ITEM = 1;

    public ParticipantAdapter(Context pContext, List<ParticipantModel> pModel, FragmentManager fragmentManager, FrameLayout frameLayout, String dataObject){
        mContext = pContext;
        mModel = pModel;
        mFragment = fragmentManager;
        mFrameLayout = frameLayout;
        this.dataObject = dataObject;
    }
    @Override
    public ParticipantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_participant,parent,false);
            ViewHolder vhItem = new ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ParticipantAdapter.ViewHolder holder, int position) {
        ((ParticipantAdapter.ViewHolder) holder).populateData(mModel.get(position),mContext,mModel,mFragment,mFrameLayout,dataObject,position);
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
        CircleImageView circleImageView;
        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.circleView);
            circleImageView.setBorderColor(Color.parseColor("#ffffff"));


        }

        public void populateData(ParticipantModel pModel, Context pContext, final List<ParticipantModel> data, final FragmentManager pFragment, final FrameLayout mFrameLayout, final String dataObj, final int position){
            mModel = pModel;
            mContext = pContext;
            if (mModel.getUser().getProfile().getImages() != null && mModel.getUser().getProfile().getImages().size() >0){
                ImageLoader.getInstance().loadImage(mModel.getUser().getProfile().getImages().get(0), new ImageLoadingListener() {
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

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFrameLayout.setVisibility(View.VISIBLE);

                    Bundle bundle = new Bundle();
                    Fragment fragment = new ParticipantDetailfrag();
                    String dataobject = JsonUtils.jsonify(mModel);
                    String allModels = JsonUtils.jsonify(data);
                    bundle.putString("participantModel",dataobject);
                    bundle.putString("allmodels",allModels);
                    bundle.putString("dataobject",dataObj);
                    bundle.putInt("position",position);
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = pFragment.beginTransaction();
                    transaction.add(R.id.frameAddActivity, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }
    }
}
