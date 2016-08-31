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

import in.tagbin.mitohealthapp.Fragments.CartFrag;
import in.tagbin.mitohealthapp.Fragments.Settings_frag;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter1;

public class BinderActivity extends AppCompatActivity{

    static Fragment fra;
    FragmentTransaction fraTra;
    Toolbar toolbar;
    public AHBottomNavigation bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Profile");

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigater);



// Create items
        startService(new Intent(this,XamppChatService.class));

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("",R.drawable.partnet_final);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("",R.drawable.profile_final);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.big_mito);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("", R.drawable.settings_final);

// Add itemsF63D2B
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item5);

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
        String source =getIntent().getStringExtra("source");
// Set current item programmatically
        if (getIntent().getStringExtra("interests") != null){
            fra = new PartnerFrag();
            bottomNavigation.setCurrentItem(0);

        }else if (getIntent().getStringExtra("source") != null) {

            fra = new HomePage();
            bottomNavigation.setCurrentItem(2);


        }else if (getIntent().getStringExtra("profile_connect") != null){
            fra = new ProfileFragMent();
            Bundle bundle = new Bundle();
            bundle.putString("profile_connect", "profile");
            fra.setArguments(bundle);
            bottomNavigation.setCurrentItem(1);
        }else {
            fra = new ProfileFragMent();
            bottomNavigation.setCurrentItem(1);
        }
        fraTra =  getSupportFragmentManager().beginTransaction().replace(R.id.fragmentnew, fra);
        fraTra.commit();

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


    public void change (int position){

        switch (position){
            case 0:
                PrefManager pref = new PrefManager(this);

                if (!pref.isTutorialShown()) {
                    Intent i = new Intent(this,SliderActivity.class);
                    startActivity(i);

                }else if(!pref.isTutorialShown1()){
                    Controller.getInterests(this,mInterestListener);
                }else{
                    fra = new PartnerFrag();
                }

                //fra = new PartnerFrag();
//                Toast.makeText(BinderActivity.this, "clicked 1", Toast.LENGTH_SHORT).show();

                break;
            case 1:
                toolbar.setTitle("Profile");
                fra = new ProfileFragMent();
                //bottomNavigation.setCurrentItem(1);
//                Toast.makeText(BinderActivity.this, "clicked 2", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                toolbar.setTitle("Mito");
                fra = new HomePage();
                //bottomNavigation.setCurrentItem(2);
//                Toast.makeText(BinderActivity.this, "clicked 3", Toast.LENGTH_SHORT).show();
                break;
//            case 3:
//
//                toolbar.setTitle("Cart");
//                fra = new CartFrag();
//                //bottomNavigation.setCurrentItem(3);
//                break;
            case 3:
                toolbar.setTitle("Settings");
                fra = new Settings_frag();
                //bottomNavigation.setCurrentItem(4);
                break;
            default:
                if (getIntent().getStringExtra("interests") != null){
                    fra = new PartnerFrag();
                }else {
                    fra = new ProfilePage();
                }
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
    RequestListener mInterestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
//            interestModel = JsonUtils.objectify(responseObject.toString(),InterestModel.class);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    setToggleButtons(interestModel);
//                }
//            });
            Intent i = new Intent(BinderActivity.this,InterestActivity.class);
            i.putExtra("response",responseObject.toString());
            startActivity(i);
        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = new AddActivityfrag();
        fragment.onActivityResult(requestCode, resultCode, data);

    }


}
