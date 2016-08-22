package in.tagbin.mitohealthapp;



import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class InitActivity extends AppCompatActivity {


    static Fragment fra;
    FragmentTransaction fraTra;
    Toolbar toolbar;
    public AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        toolbar = (Toolbar) findViewById(R.id.toolbarInit);
        setSupportActionBar(toolbar);

        fra = new PartnerIntro();
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigater);

        fraTra = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentnew, fra);
        fraTra.commit();

// Create items

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("",R.drawable.partnet_final);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("",R.drawable.profile_final);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.big_mito);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.cart_final);

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
                toolbar.setTitle("Partner Connect");
                Toast.makeText(InitActivity.this, "clicked 1", Toast.LENGTH_SHORT).show();
                fra = new PartnerIntro();
                break;
            case 1:
                toolbar.setTitle("Profile");
                Toast.makeText(InitActivity.this, "clicked 2", Toast.LENGTH_SHORT).show();
                //fra = new ProfilePage();
                break;
            case 2:
                toolbar.setTitle("Mito");

                //fra = new HomePage();
                Toast.makeText(InitActivity.this, "clicked 3", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(InitActivity.this, "clicked 4", Toast.LENGTH_SHORT).show();
                break;
            default:
                //fra = new ProfilePage();
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
}
