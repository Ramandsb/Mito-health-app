package in.tagbin.mitohealthapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter;
import in.tagbin.mitohealthapp.model.ConnectProfileModel;

/**
 * Created by aasaqt on 12/8/16.
 */
public class ProfileActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPagerAdapter mAdapter;
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    private ViewPager intro_images;
    private ArrayList<String> mImageResources;
    ImageView back;
    TextView name,age,distance,time,occupation,description,blankSpace;
    FlowLayout flowLayout;
    String response;
    RelativeLayout allInfo;
    CoordinatorLayout coordinatorLayout;
    ConnectProfileModel data;
    PrefManager pref;
    double latitde = 0.0,longitude = 0.0;
    NestedScrollView nestedScrollView;
    boolean checkod = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        response = getIntent().getStringExtra("response");
        data = JsonUtils.objectify(response,ConnectProfileModel.class);
        intro_images = (ViewPager) findViewById(R.id.pagerProfile);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        back = (ImageView) findViewById(R.id.ivProfileBack);
        name = (TextView) findViewById(R.id.tvProfileName);
        age = (TextView) findViewById(R.id.tvProfileAge);
        distance = (TextView) findViewById(R.id.tvProfileDistance);
        time = (TextView) findViewById(R.id.tvProfileTime);
        occupation = (TextView) findViewById(R.id.tvProfileOccupation);
        description = (TextView) findViewById(R.id.tvProfileDescription);
        flowLayout = (FlowLayout) findViewById(R.id.flowLayoutProfile);
        blankSpace = (TextView) findViewById(R.id.tvProfileBlank);
        allInfo = (RelativeLayout) findViewById(R.id.relativeProfileAllInfo);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scrollView);
        pref = new PrefManager(this);
        if (pref.getCurrentLocationAsObject() != null){
            if (pref.getCurrentLocationAsObject().getLatitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0){
                latitde = pref.getCurrentLocationAsObject().getLatitude();
                longitude = pref.getCurrentLocationAsObject().getLongitude();
            }
        }
        blankSpace.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) coordinatorLayout.getLayoutParams();
                layoutParams.addRule(RelativeLayout.BELOW,0);
                layoutParams.addRule(RelativeLayout.ALIGN_TOP,0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,1);
                layoutParams.width= layoutParams.MATCH_PARENT;
                layoutParams.height = MyUtils.dpToPx(ProfileActivity.this,250);
                coordinatorLayout.setLayoutParams(layoutParams);
                coordinatorLayout.setFitsSystemWindows(true);
                nestedScrollView.setFitsSystemWindows(false);
                blankSpace.setVisibility(View.GONE);
                checkod = true;
                return false;
            }
        });
        if (!checkod) {
            allInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.addRule(RelativeLayout.BELOW, R.id.relativePagerHeading);
                    layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.pagerProfile);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                    coordinatorLayout.setLayoutParams(layoutParams);
                    coordinatorLayout.setFitsSystemWindows(true);
                    blankSpace.setVisibility(View.VISIBLE);
                    checkod = true;
                }
            });
        }
        mImageResources = new ArrayList<String>();
        if (data.getImages() != null){
            if (data.getImages().getMaster() != null){
                mImageResources.add(data.getImages().getMaster());
            }
            if (data.getImages().getOthers() != null){
                for (int i = 0;i<data.getImages().getOthers().length;i++){
                    mImageResources.add(data.getImages().getOthers()[i]);
                }
            }
        }else {
            mImageResources.add(null);
        }
        mAdapter = new ViewPagerAdapter(this, mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();
        name.setText(data.getUser().getFirst_name());
        age.setText(data.getAge()+", "+data.getGender());
        occupation.setText(data.getOccupation());
        description.setText(data.getDescription());
        double lat2 = MyUtils.getLatitude(this,data.getLocation());
        double long2 = MyUtils.getLongitude(this,data.getLocation());
        double result = MyUtils.calculateDistance(latitde, longitude, lat2, long2, "M");
        distance.setText(new DecimalFormat("##").format(result).toString() + " miles away");
        //distance.setText(data.getDistance());
        LayoutInflater inflater = LayoutInflater.from(this);
        if (data.getInterests().size() >0) {
            flowLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < data.getInterests().size(); i++) {
                View layout = inflater.inflate(R.layout.profile_toggle, flowLayout, false);
                final ToggleButton toggleButton = (ToggleButton) layout.findViewById(R.id.toggleButton1);
                toggleButton.setTextOn(data.getInterests().get(i).getInterest().getName());
                toggleButton.setChecked(true);
                toggleButton.setClickable(false);
                flowLayout.addView(layout);
            }
        }else{
            flowLayout.setVisibility(View.GONE);
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
