package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.ProfileActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.ConnectProfileModel;
import in.tagbin.mitohealthapp.model.ExploreModel;

/**
 * Created by aasaqt on 12/8/16.
 */
public class CardsDataAdapter extends BaseAdapter {
    ExploreModel mData;
    Context mContext;

    public CardsDataAdapter(ExploreModel data,Context pContext){
        mData = data;
        mContext = pContext;
    }
    @Override
    public int getCount() {
        return mData.getNearby_user_list().size();
    }

    @Override
    public ConnectProfileModel getItem(int position) {
        return mData.getNearby_user_list().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup parent){
        contentView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.card_content, parent, false);
        final CircleImageView personCard = (CircleImageView) contentView.findViewById(R.id.card1);
        TextView compatibility = (TextView) contentView.findViewById(R.id.tvProfileCompatibility);
        double compatibilty1 = 0.0;
        if (mData.getNearby_user_list().get(position).getCompatibility() != 0){
            compatibilty1 = mData.getNearby_user_list().get(position).getCompatibility();
        }else{
            compatibilty1 = 0.0;
        }
        compatibility.setText(""+compatibilty1);
        personCard.setBorderWidth(MyUtils.dpToPx(mContext,3));
        personCard.setBorderColor(Color.parseColor("#a0c894"));
        if (mData.getNearby_user_list().get(position).getImages() != null){
            if (mData.getNearby_user_list().get(position).getImages().getMaster() != null){
                ImageLoader.getInstance().loadImage(mData.getNearby_user_list().get(position).getImages().getMaster(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        personCard.setImageResource(R.drawable.hotel);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        personCard.setImageResource(R.drawable.hotel);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        personCard.setImageBitmap(loadedImage);
                    }
                });
            }else{
                personCard.setImageResource(R.drawable.hotel);
            }
        }else{
            personCard.setImageResource(R.drawable.hotel);
        }

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,ProfileActivity.class);
                i.putExtra("response",JsonUtils.jsonify(mData.getNearby_user_list().get(position)));
                mContext.startActivity(i);
            }
        });
        return contentView;
    }
}
