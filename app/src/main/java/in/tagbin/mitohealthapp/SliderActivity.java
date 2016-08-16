package in.tagbin.mitohealthapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import in.tagbin.mitohealthapp.helper.ViewPagerAdapter1;

/**
 * Created by aasaqt on 15/8/16.
 */
public class SliderActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPagerAdapter1 mAdapter;
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    private ViewPager intro_images;
    RelativeLayout pager;
    private int[] mImageResources = {
            R.drawable.hotel,
            R.drawable.hotel,
            R.drawable.hotel,
            R.drawable.hotel/*,
            R.drawable.intro4*/};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        pager = (RelativeLayout) findViewById(R.id.relativeProfileIntro);
        intro_images = (ViewPager) findViewById(R.id.pagerProfileIntro);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAdapter = new ViewPagerAdapter1(this, mImageResources,getSupportFragmentManager());
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
    }
    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_unactive));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_active));
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_unactive));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.pointer_page_active));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
