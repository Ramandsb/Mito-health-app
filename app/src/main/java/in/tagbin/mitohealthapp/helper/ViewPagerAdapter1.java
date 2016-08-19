package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import in.tagbin.mitohealthapp.BinderActivity;
import in.tagbin.mitohealthapp.PartnerFrag;
import in.tagbin.mitohealthapp.R;

/**
 * Created by aasaqt on 13/8/16.
 */
public class ViewPagerAdapter1 extends PagerAdapter {

    private Context mContext;
    private int[] mResources;
    FragmentManager fragmentManager;

    public ViewPagerAdapter1(Context mContext, int[] mResources, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.mResources = mResources;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        imageView.setImageResource(mResources[position]);
        if (position == 3){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext,BinderActivity.class);
                    i.putExtra("interests","interests");
                    mContext.startActivity(i);
                    ((Activity) mContext).finish();
                }
            });
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

