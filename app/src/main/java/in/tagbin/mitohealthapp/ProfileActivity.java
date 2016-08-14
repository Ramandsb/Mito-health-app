package in.tagbin.mitohealthapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter;
import in.tagbin.mitohealthapp.model.UserListModel;

/**
 * Created by aasaqt on 12/8/16.
 */
public class ProfileActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPagerAdapter mAdapter;
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    private ViewPager intro_images;
    private int[] mImageResources = {
            R.drawable.hotel,
            R.drawable.hotel,
            R.drawable.hotel/*,
            R.drawable.intro4*/};
    ImageView back;
    TextView name,age,distance,time;
    GridLayout gridLayout;
    String response;
    UserListModel data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        response = getIntent().getStringExtra("response");
        data = JsonUtils.objectify(response,UserListModel.class);
        intro_images = (ViewPager) findViewById(R.id.pagerProfile);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        back = (ImageView) findViewById(R.id.ivProfileBack);
        name = (TextView) findViewById(R.id.tvProfileName);
        age = (TextView) findViewById(R.id.tvProfileAge);
        distance = (TextView) findViewById(R.id.tvProfileDistance);
        time = (TextView) findViewById(R.id.tvProfileTime);
        gridLayout = (GridLayout) findViewById(R.id.gridProfileHobbies);
        mAdapter = new ViewPagerAdapter(this, mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
        name.setText(data.getName());
        age.setText(data.getAge()+", "+data.getSex());
        distance.setText(data.getDistance());
        gridLayout.setRowCount(data.getHobbies().size()%3 +1);
        for (int i= 0;i<data.getHobbies().size();i++){
            TextView textView = new TextView(ProfileActivity.this);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setBackgroundResource(R.drawable.bg_hobby);
            int padding = MyUtils.dpToPx(this,5);
            int padding1 = MyUtils.dpToPx(this,10);
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.close,0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new GridLayoutManager.LayoutParams(GridLayoutManager.LayoutParams.WRAP_CONTENT, GridLayoutManager.LayoutParams.WRAP_CONTENT));
            params.setMargins(padding,padding,padding,padding);
            textView.setLayoutParams(params);
            textView.setCompoundDrawablePadding(padding1);
            textView.setPadding(padding,padding,padding,padding);
            textView.setText(data.getHobbies().get(i).toString());
            gridLayout.addView(textView);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
