package in.tagbin.mitohealthapp.activity;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Fragments.ChatDieticianFragment;
import in.tagbin.mitohealthapp.Fragments.HealthFragment;
import in.tagbin.mitohealthapp.Fragments.MitoHealthFragment;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.Fragments.UserConnectFragment;
import in.tagbin.mitohealthapp.Fragments.UserDetailsFragment;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.service.XmppChatService;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.UserInterestModel;
import in.tagbin.mitohealthapp.service.GCMIntentService;

public class BinderActivity extends AppCompatActivity{

    static Fragment fra;
    FragmentTransaction fraTra;
    Toolbar toolbar;
    TextView toolbar_title,coins;
    Intent i;
    int coinsFinal = 0;
    PrefManager pref;
    public SpaceNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title= (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Mito");
        bottomNavigation = (SpaceNavigationView) findViewById(R.id.bottom_navigater);

        bottomNavigation.initWithSaveInstanceState(savedInstanceState);
        Intent intent = new Intent(this, GCMIntentService.class);
        startService(intent);
// Create items
        startService(new Intent(this,XmppChatService.class));
        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.big_mito));
        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.partnet_final));
        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.chat_nutritionist));
        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.profile_final));
        pref = new PrefManager(this);
        bottomNavigation.setCentreButtonIcon(R.drawable.plus_svg);
        bottomNavigation.showIconOnly();
        bottomNavigation.hideAllBudges();
        if (getIntent().getStringExtra("profile_connect") != null){
            fra = new UserDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("profile_connect", "profile");
            fra.setArguments(bundle);
            bottomNavigation.changeCurrentItem(1);
        }else {
            fra = new UserDetailsFragment();
            bottomNavigation.changeCurrentItem(1);
        }
        fraTra =  getSupportFragmentManager().beginTransaction().replace(R.id.fragmentnew, fra);
        fraTra.commit();


//// Customize notification (title, background, typeface)
//        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.bottombar));
//
//// Add or remove notification for each item68822836
//        bottomNavigation.setNotification("4", 1);
//        bottomNavigation.setNotification("", 1);
        if (getIntent().hasExtra("selection")) {
            if (getIntent().getIntExtra("selection", 1) == 2) {
                bottomNavigation.changeCurrentItem(2);
                change(2);
            }else if(getIntent().getIntExtra("selection", 1) == 1) {
                bottomNavigation.changeCurrentItem(1);
                change(1);
            }else if (getIntent().getIntExtra("selection",1) == 0){
                bottomNavigation.changeCurrentItem(0);
                change(0);
            }else if (getIntent().getIntExtra("selection",1) == 3){
                bottomNavigation.changeCurrentItem(3);
                change(3);
            }
        }

        bottomNavigation.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                //Toast.makeText(BinderActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
                intent.putExtra("selection",0);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                change(itemIndex);
                //Toast.makeText(BinderActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //Toast.makeText(BinderActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
// Set listeners
//        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                // Do something cool here...
//                change(position);
//                return true;
//            }
//        });
//        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
//            @Override public void onPositionChange(int y) {
//                // Manage the new y position
//            }
//        });
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() != 0)
            getSupportFragmentManager().popBackStack();
        else
            closeAppDialog();
    }

    public void closeAppDialog(){
        AlertDialog.Builder dialog=  new AlertDialog.Builder(this);
        dialog.setTitle("Are you Sure!");
        dialog.setMessage("You want to Exit Mito");
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        AlertDialog alert = dialog.create();
        alert.show();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomNavigation.onSaveInstanceState(outState);
    }
    public void change (int position){

        switch (position){

            case 1:
                toolbar_title.setText("Partner Connect");
                toolbar.setTitle("");


                if (!pref.isTutorialShown()) {
                    Intent i = new Intent(this,SliderActivity.class);
                    startActivity(i);

                }else if(!pref.isTutorialShown1()){
                    Controller.getInterests(this,mInterestListener);
                }else{
                    fra = new UserConnectFragment();
                }

                //fra = new UserConnectFragment();
//                Toast.makeText(BinderActivity.this, "clicked 1", Toast.LENGTH_SHORT).show();

                break;
            case 3:
                toolbar_title.setText("Profile");
                toolbar.setTitle("");
                fra = new UserDetailsFragment();
                //bottomNavigation.setCurrentItem(1);
//                Toast.makeText(BinderActivity.this, "clicked 2", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                if (pref.getKeyUserDetails() != null && pref.getKeyUserDetails().getProfile().getHeight() != 0 && pref.getKeyUserDetails().getProfile().getWeight() != 0){
                    toolbar_title.setText("Mito");
                    toolbar.setTitle("");
                    fra = new MitoHealthFragment();
                }else {
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Enter Details");
                    alertDialog1.setMessage("Please enter your height and weight to proceed");
                    alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog1.show();
                }
                //bottomNavigation.setCurrentItem(2);
//                Toast.makeText(BinderActivity.this, "clicked 3", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                toolbar_title.setText("Dietician Chat");
                toolbar.setTitle("");
                fra = new ChatDieticianFragment();
                break;
//            case 4:
//                if (pref.getKeyUserDetails() != null && pref.getKeyUserDetails().getProfile().getHeight() != 0 && pref.getKeyUserDetails().getProfile().getWeight() != 0){
//                    toolbar_title.setText("Settings");
//                    toolbar.setTitle("");
//                    fra = new SettingsActivity();
//                }else {
//                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
//                    alertDialog1.setTitle("Enter Details");
//                    alertDialog1.setMessage("Please enter your height and weight to proceed");
//                    alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//                    alertDialog1.show();
//                }
//                //bottomNavigation.setCurrentItem(4);
//                break;
            default:
                if (getIntent().getStringExtra("interests") != null){
                    fra = new UserConnectFragment();
                }else {
                    fra = new HealthFragment();
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
            Controller.getUserInterests(BinderActivity.this,mUserInterestListener);
            i = new Intent(BinderActivity.this,InterestActivity.class);
            i.putExtra("response",responseObject.toString());

        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Fragment fragment = new CreateEventActivity();
//        fragment.onActivityResult(requestCode, resultCode, data);

    }
    RequestListener mUserInterestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("user interests ",responseObject.toString());
            Type collectionType = new TypeToken<ArrayList<UserInterestModel>>() {
            }.getType();
            List<UserInterestModel> userInterestModel = (ArrayList<UserInterestModel>) new Gson().fromJson(responseObject.toString(), collectionType);
            i.putExtra("userinterests",responseObject.toString());
            startActivity(i);
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("user interests",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(BinderActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BinderActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

}
