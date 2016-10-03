package in.tagbin.mitohealthapp.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.R;


/**
 * Created by aasaqt on 10/8/16.
 */
public class ImagesSlideAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> mResources;

    public ImagesSlideAdapter(Context mContext, ArrayList<String> mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    public ImagesSlideAdapter(FragmentManager supportFragmentManager) {
    }

    @Override
    public int getCount() {
        if (mResources.get(0) != null)
            return mResources.size();
        else
            return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_images_slider, container, false);
        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        if (mResources.get(0) != null){
            ImageLoader.getInstance().loadImage(mResources.get(position), new ImageLoadingListener() {
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

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
