package in.tagbin.mitohealthapp.activity;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

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

public class BinderActivity extends AppCompatActivity implements View.OnClickListener{

    static Fragment fra;
    FragmentTransaction fraTra;
    Toolbar toolbar;
    TextView toolbar_title,coins;
    Intent i;
    int coinsFinal = 0;
    PrefManager pref;
    ImageView iv1,iv2,iv3,iv4;
    FloatingActionButton fab;
    View overlay;
    private FloatingActionMenu topCenterMenu;
    //public SpaceNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title= (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title.setText("Mito");

        pref = new PrefManager(this);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        overlay = (View) findViewById(R.id.overlay);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        fab.setOnClickListener(this);
        overlay.setOnClickListener(this);
        //bottomNavigation = (SpaceNavigationView) findViewById(R.id.bottom_navigater);

        //bottomNavigation.initWithSaveInstanceState(savedInstanceState);
        if (!pref.isTOkenSend()) {
            Intent intent = new Intent(this, GCMIntentService.class);
            startService(intent);
        }
// Create items
        startService(new Intent(this,XmppChatService.class));
//        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.big_mito));
//        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.partnet_final));
//        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.chat_nutritionist));
//        bottomNavigation.addSpaceItem(new SpaceItem("", R.drawable.profile_final));
//        bottomNavigation.setCentreButtonIcon(R.drawable.plus_svg);
//        bottomNavigation.showIconOnly();
//        bottomNavigation.hideAllBudges();
        if (getIntent().getStringExtra("profile_connect") != null){
            fra = new UserDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("profile_connect", "profile");
            fra.setArguments(bundle);
            setImages();
            iv2.setImageResource(R.drawable.iv_partner_connect_clicked);
//            bottomNavigation.changeCurrentItem(1);
        }else {
            setImages();
            iv2.setImageResource(R.drawable.iv_partner_connect_clicked);
            fra = new UserDetailsFragment();

//            bottomNavigation.changeCurrentItem(1);
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
//                bottomNavigation.changeCurrentItem(2);
                change(2);
            }else if(getIntent().getIntExtra("selection", 1) == 1) {
//                bottomNavigation.changeCurrentItem(1);
                change(1);
            }else if (getIntent().getIntExtra("selection",1) == 0){
//                bottomNavigation.changeCurrentItem(0);
                change(0);
            }else if (getIntent().getIntExtra("selection",1) == 3){
//                bottomNavigation.changeCurrentItem(3);
                change(3);
            }
        }

//        bottomNavigation.setSpaceOnClickListener(new SpaceOnClickListener() {
//            @Override
//            public void onCentreButtonClick() {
//                //Toast.makeText(BinderActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
//                intent.putExtra("selection",0);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemClick(int itemIndex, String itemName) {
//                change(itemIndex);
//                //Toast.makeText(BinderActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemReselected(int itemIndex, String itemName) {
//                //Toast.makeText(BinderActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
//            }
//        });
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
        int blueSubActionButtonSize = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_size);
        int blueSubActionButtonContentMargin = getResources().getDimensionPixelSize(R.dimen.blue_sub_action_button_content_margin);
        int redActionMenuRadius = getResources().getDimensionPixelSize(R.dimen.red_action_menu_radius);
        SubActionButton.Builder tCSubBuilder = new SubActionButton.Builder(this);
        tCSubBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_action_blue));


        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        blueContentParams.setMargins(blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin,
                blueSubActionButtonContentMargin);

        // Set custom layout params
        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize, blueSubActionButtonSize);
        tCSubBuilder.setLayoutParams(blueParams);

        ImageView tcIcon1 = new ImageView(this);
        ImageView tcIcon2 = new ImageView(this);
        ImageView tcIcon3 = new ImageView(this);
        ImageView tcIcon4 = new ImageView(this);
        ImageView tcIcon5 = new ImageView(this);

        tcIcon1.setImageDrawable(getResources().getDrawable(R.drawable.foodicon_svg));
        tcIcon2.setImageDrawable(getResources().getDrawable(R.drawable.watericon_svg));
        tcIcon3.setImageDrawable(getResources().getDrawable(R.drawable.exericon_svg));
        tcIcon4.setImageDrawable(getResources().getDrawable(R.drawable.sleep_menu));
        tcIcon5.setImageDrawable(getResources().getDrawable(R.drawable.feeling));

        SubActionButton tcSub1 = tCSubBuilder.setContentView(tcIcon1, blueContentParams).build();
        SubActionButton tcSub2 = tCSubBuilder.setContentView(tcIcon2, blueContentParams).build();
        SubActionButton tcSub3 = tCSubBuilder.setContentView(tcIcon3, blueContentParams).build();
        SubActionButton tcSub4 = tCSubBuilder.setContentView(tcIcon4, blueContentParams).build();
        SubActionButton tcSub5 = tCSubBuilder.setContentView(tcIcon5, blueContentParams).build();


        // Build another menu with custom options
        topCenterMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(tcSub1, tcSub1.getLayoutParams().width, tcSub1.getLayoutParams().height)
                .addSubActionView(tcSub2, tcSub2.getLayoutParams().width, tcSub2.getLayoutParams().height)
                .addSubActionView(tcSub3, tcSub3.getLayoutParams().width, tcSub3.getLayoutParams().height)
                .addSubActionView(tcSub4, tcSub4.getLayoutParams().width, tcSub4.getLayoutParams().height)
                .addSubActionView(tcSub5, tcSub5.getLayoutParams().width, tcSub5.getLayoutParams().height)
                .setRadius(redActionMenuRadius)
                .setStartAngle(180)
                .setEndAngle(360)
                .attachTo(fab)
                .build();

        topCenterMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu menu) {
                overlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuClosed(FloatingActionMenu menu) {
                overlay.setVisibility(View.GONE);
            }
        });
        tcSub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
                intent.putExtra("selection",0);
                startActivity(intent);
            }
        });
        tcSub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
                intent.putExtra("selection",1);
                startActivity(intent);
            }
        });
        tcSub3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
                intent.putExtra("selection",2);
                startActivity(intent);
            }
        });
        tcSub4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
                intent.putExtra("selection",3);
                startActivity(intent);
            }
        });
        tcSub5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
                intent.putExtra("selection",4);
                startActivity(intent);
            }
        });
        // make the red button terminate the service

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
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
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
        //bottomNavigation.onSaveInstanceState(outState);
    }
    public void setImages(){
        iv1.setImageResource(R.drawable.iv_mito);
        iv2.setImageResource(R.drawable.iv_partner_connect);
        iv3.setImageResource(R.drawable.iv_nutritionist_chat);
        iv4.setImageResource(R.drawable.iv_edit_profile);
    }
    public void change (int position){

        switch (position){

            case 1:
                setImages();
                iv2.setImageResource(R.drawable.iv_partner_connect_clicked);
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
                setImages();
                iv4.setImageResource(R.drawable.iv_edit_profile_clicked);
                toolbar_title.setText("Profile");
                toolbar.setTitle("");
                fra = new UserDetailsFragment();
                //bottomNavigation.setCurrentItem(1);
//                Toast.makeText(BinderActivity.this, "clicked 2", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                setImages();
                iv1.setImageResource(R.drawable.iv_mito_clicked);
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
                setImages();
                iv3.setImageResource(R.drawable.iv_nutritionist_chat_clicked);
                if (pref.getDietician() != null){
                    if (pref.getDietician().getFirst_name() != null){
                        toolbar_title.setText(pref.getDietician().getFirst_name());
                        if (pref.getDietician().getLast_name() != null){
                            toolbar_title.setText(pref.getDietician().getFirst_name()+" "+pref.getDietician().getLast_name());
                        }
                    }
                }else {
                    toolbar_title.setText("Dietician Chat");
                }
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
                setImages();
                iv4.setImageResource(R.drawable.iv_edit_profile_clicked);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv1:
                change(0);
                break;
            case R.id.iv2:
                change(1);
                break;
            case R.id.iv3:
                change(2);
                break;
            case R.id.iv4:
                change(3);
                break;
            case R.id.overlay:
                topCenterMenu.close(true);
                overlay.setVisibility(View.GONE);
                break;
//            case R.id.fab:
//                Intent intent = new Intent(BinderActivity.this,DailyDetailsActivity.class);
//                intent.putExtra("selection",0);
//                startActivity(intent);
//                break;
        }
    }


}
