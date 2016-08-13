package in.tagbin.mitohealthapp;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter1;

public class BinderActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    static Fragment fra;
    FragmentTransaction fraTra;
    Toolbar toolbar;
    public AHBottomNavigation bottomNavigation;
    private ViewPagerAdapter1 mAdapter;
    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout pager_indicator;
    private ViewPager intro_images;
    RelativeLayout pager;
    private int[] mImageResources = {
            R.drawable.profile_intro1,
            R.drawable.profile_intro2,
            R.drawable.profile_intro3,
            R.drawable.profile_intro4/*,
            R.drawable.intro4*/};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        pager = (RelativeLayout) findViewById(R.id.relativeProfileIntro);
//        intro_images = (ViewPager) findViewById(R.id.pagerProfileIntro);
//        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
//        mAdapter = new ViewPagerAdapter1(this, mImageResources,getSupportFragmentManager());
//        intro_images.setAdapter(mAdapter);
//        intro_images.setCurrentItem(0);
//        intro_images.setOnPageChangeListener(this);
//        setUiPageViewController();
        toolbar.setTitle("Profile");
        fra = new ProfileFragMent();
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigater);

        fraTra =  getSupportFragmentManager().beginTransaction().replace(R.id.fragmentnew, fra);
        fraTra.commit();

// Create items

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("",R.drawable.big_partner);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("",R.drawable.big_profile);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.big_mito);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.big_cart);

// Add itemsF63D2B
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#ffffff"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

// Change colors
//        bottomNavigation.setAccentColor(R.color.colorAccent);
//        bottomNavigation.setInactiveColor(R.color.sample_bg);


// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);
//        bottomNavigation.setInactiveColor(getResources().getColor(R.color.bottombar));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.bottombar));

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(false);

// Set current item programmatically
        bottomNavigation.setCurrentItem(1);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.bottombar));

// Add or remove notification for each item68822836
        bottomNavigation.setNotification("4", 1);
        bottomNavigation.setNotification("", 1);
        if (getIntent().hasExtra("selection")) {
            if (getIntent().getIntExtra("selection", 1) == 2) {
                change(2);

            }else if(getIntent().getIntExtra("selection", 1) == 1) {
                fraTra =  getSupportFragmentManager().beginTransaction().replace(R.id.fragmentnew, fra);
                fraTra.commit();
            }
        }

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                change(position);
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
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

    public void change (int position){

        switch (position){
            case 0:
                PrefManager pref = new PrefManager(this);
//                if (!pref.isTutorialShown()){
//                    pref.setTutorial(true);
//                    pager.setVisibility(View.VISIBLE);
//                }else{
//                    pager.setVisibility(View.GONE);
//                    fra = new PartnerFrag();
//                }
                fra = new PartnerFrag();
                Toast.makeText(BinderActivity.this, "clicked 1", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                toolbar.setTitle("Profile");
                Toast.makeText(BinderActivity.this, "clicked 2", Toast.LENGTH_SHORT).show();
                fra = new ProfileFragMent();
                break;
            case 2:
                toolbar.setTitle("Mito");
                fra = new HomePage();
                Toast.makeText(BinderActivity.this, "clicked 3", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(BinderActivity.this, "clicked 4", Toast.LENGTH_SHORT).show();
                break;
            default:
                fra = new ProfilePage();
                break;

        }

        fraTra = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentnew,fra);
        fraTra.commit();
        invalidateOptionsMenu();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = new AddActivityfrag();
        fragment.onActivityResult(requestCode, resultCode, data);

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
