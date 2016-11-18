package in.tagbin.mitohealthapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;

import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.SettingsModel;
import pl.droidsonroids.gif.GifImageView;

public class SettingsActivity extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Switch meals,water,sleep,exercise,weeklyProgress,healthTips,explore,chatNotifications;
    RangeBar age;
    DiscreteSeekBar distance;
    TextView ageSet,distanceSet,version,coins;
    SettingsModel settingsModel;
    GifImageView progressBar;
    Toolbar toolbar;
    TextView prefernce;
    int coinsFinal = 0;
    RelativeLayout preferenceLayout;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView logout = (TextView) findViewById(R.id.tvLogout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences loginDetails= getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=loginDetails.edit();
                editor.clear();
                editor.commit();
                PrefManager pref = new PrefManager(SettingsActivity.this);
                pref.clearSession();
                startActivity(new Intent(SettingsActivity.this,SplashActivity.class));
                finish();
            }
        });
        meals = (Switch) findViewById(R.id.switchFilterMeals);
        water = (Switch) findViewById(R.id.switchFilterWater);
        exercise = (Switch) findViewById(R.id.switchFilterExercise);
        weeklyProgress = (Switch) findViewById(R.id.switchFilterWeeklyProgress);
        healthTips = (Switch) findViewById(R.id.switchFilterHealthTips);
        sleep = (Switch) findViewById(R.id.switchFilterSleep);
        explore = (Switch) findViewById(R.id.switchFilterExplore);
        chatNotifications = (Switch) findViewById(R.id.switchFilterChatNotifications);
        distance = (DiscreteSeekBar) findViewById(R.id.rangebarDistance);
        age = (RangeBar) findViewById(R.id.rangebarAge);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        ageSet = (TextView) findViewById(R.id.tvSettingAgeSet);
        distanceSet = (TextView) findViewById(R.id.tvSettingDistanceSet);
        prefernce = (TextView) findViewById(R.id.tvSettingPreference);
        version = (TextView) findViewById(R.id.tvVersion);
        preferenceLayout = (RelativeLayout) findViewById(R.id.relativeSettingGender);
        settingsModel = new SettingsModel();
        progressBar.setVisibility(View.VISIBLE);
        Controller.getSettings(this,mSetingSetListener);
        try {
            String version_name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            version.setText("Version "+version_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        meals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setMeals_notification(true);
                }else{
                    settingsModel.setMeals_notification(false);
                }
            }
        });
        water.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setWater_notification(true);
                }else{
                    settingsModel.setWater_notification(false);
                }
            }
        });
        exercise.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setExercise_notification(true);
                }else{
                    settingsModel.setExercise_notification(false);
                }
            }
        });
        weeklyProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setWeekly_progress_notification(true);
                }else{
                    settingsModel.setWeekly_progress_notification(false);
                }
            }
        });
        healthTips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setDaily_health_tips_notification(true);
                }else{
                    settingsModel.setDaily_health_tips_notification(false);
                }
            }
        });
        sleep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setSleep_notification(true);
                }else{
                    settingsModel.setSleep_notification(false);
                }
            }
        });
        explore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setShow_me_on_explore(true);
                }else{
                    settingsModel.setShow_me_on_explore(false);
                }
            }
        });
        chatNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    settingsModel.setChat_notification(true);
                }else{
                    settingsModel.setChat_notification(false);
                }
            }
        });
//        distance.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
//            @Override
//            public int transform(int value) {
//                settingsModel.setMaximum_distance(value);
//                distanceSet.setText(value+ " kms");
//                return value;
//            }
//        });
//        age.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
//                int[] age = {Integer.parseInt(leftPinValue), Integer.parseInt(rightPinValue)};
//                settingsModel.setAge_range(age);
//                ageSet.setText(leftPinValue+"-"+rightPinValue+" yrs");
//            }
//        });
//        preferenceLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(SettingsActivity.this);
//                if (Build.VERSION.SDK_INT >= 22) {
//                    alertDialog2 = new AlertDialog.Builder(SettingsActivity.this, R.style.AppCompatAlertDialogStyle);
//                } else {
//                    alertDialog2 = new AlertDialog.Builder(SettingsActivity.this);
//                }
//                LinearLayout linearLayout = new LinearLayout(SettingsActivity.this);
//                linearLayout.setOrientation(LinearLayout.VERTICAL);
//                int padding = MyUtils.dpToPx(SettingsActivity.this,10);
//                int padding1 = MyUtils.dpToPx(SettingsActivity.this,30);
//                int padding2 = MyUtils.dpToPx(SettingsActivity.this,20);
//                TextView textView = new TextView(SettingsActivity.this);
//                textView.setText("Please select your preference");
//                textView.setTextColor(Color.parseColor("#000000"));
//
//                textView.setPadding(padding1,padding,padding1,padding);
//                linearLayout.addView(textView);
//                final RadioGroup etRadioGroup = new RadioGroup(SettingsActivity.this);
//                //etNickName.setPadding(padding2,0,padding2,padding);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                );
//                params.setMargins(padding2, 0, padding2, padding);
//                etRadioGroup.setLayoutParams(params);
//                etRadioGroup.setPadding(0,padding,0,padding);
//                RadioButton radioButton = new RadioButton(SettingsActivity.this);
//                radioButton.setText("Only Men");
//                //radioButton.setId(1);
//                radioButton.setTextSize(18);
//                radioButton.setTextColor(Color.parseColor("#26446d"));
//                radioButton.setPadding(2, 10, 0, 10);
//                etRadioGroup.addView(radioButton);
//                RadioButton radioButton1 = new RadioButton(SettingsActivity.this);
//                radioButton1.setText("Only Women");
//                radioButton1.setTextSize(18);
//                //radioButton1.setId(2);
//                radioButton1.setTextColor(Color.parseColor("#26446d"));
//                radioButton1.setPadding(2, 10, 0, 10);
//                etRadioGroup.addView(radioButton1);
//                RadioButton radioButton2 = new RadioButton(SettingsActivity.this);
//                radioButton2.setText("Both");
//                //radioButton2.setId(0);
//                radioButton2.setTextSize(18);
//                radioButton2.setTextColor(Color.parseColor("#26446d"));
//                radioButton2.setPadding(2, 10, 0, 10);
//                etRadioGroup.addView(radioButton2);
//                linearLayout.addView(etRadioGroup);
//                alertDialog2.setView(linearLayout);
//                alertDialog2.setTitle("Preference");
//
//                alertDialog2.setMessage(null);
//                alertDialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        int radioButtonID = etRadioGroup.getCheckedRadioButtonId();
//                        View radioButton4 = etRadioGroup.findViewById(radioButtonID);
//                        int idx = etRadioGroup.indexOfChild(radioButton4);
//                        RadioButton r = (RadioButton)  etRadioGroup.getChildAt(idx);
//                        if (r != null) {
//                            String selectedtext = r.getText().toString();
//                            settingsModel.setPeople_connect_preference(r.getId());
//                            prefernce.setText(selectedtext + "    >");
//                        }
//                    }
//                });
//                alertDialog2.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                alertDialog2.show();
//            }
//        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_requests).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(false);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(false);
        menu.findItem(R.id.action_Settings).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(false);
//        View view = menu.findItem(R.id.action_coin).getActionView();
//        coins = (TextView) view.findViewById(R.id.tvCoins);
//        PrefManager pref = new PrefManager(this);
//        coinsFinal = pref.getKeyCoins();
//        coins.setText(""+coinsFinal);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_save) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d("settig send model",JsonUtils.jsonify(settingsModel));
            Controller.setSettings(SettingsActivity.this,settingsModel,mSettingPutListener);
            return true;
        }else */if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("settig send model",JsonUtils.jsonify(settingsModel));
        Controller.setSettings(SettingsActivity.this,settingsModel,mSettingPutListener);
        this.finish();
    }

    RequestListener mSettingPutListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("setting response",responseObject.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(SettingsActivity.this,"Settings succesfully saved",Toast.LENGTH_LONG).show();
                    //finish();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("settings error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SettingsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SettingsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mSetingSetListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("setting get",responseObject.toString());
            final SettingsModel settingsModel1 = JsonUtils.objectify(responseObject.toString(),SettingsModel.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    setSettings(settingsModel1);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("settings  fetch error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SettingsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SettingsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public void setSettings(SettingsModel setting){
        water.setChecked(setting.isWater_notification());
        sleep.setChecked(setting.isSleep_notification());
        exercise.setChecked(setting.isExercise_notification());
        healthTips.setChecked(setting.isDaily_health_tips_notification());
        meals.setChecked(setting.isMeals_notification());
        weeklyProgress.setChecked(setting.isWeekly_progress_notification());
        chatNotifications.setChecked(setting.isChat_notification());
        explore.setChecked(setting.isShow_me_on_explore());
//        if (setting.getMaximum_distance() != 0) {
//            distance.setProgress(setting.getMaximum_distance());
//            distanceSet.setText(setting.getMaximum_distance()+" kms");
//        }
//        if (setting.getAge_range() != null && setting.getAge_range().length == 2) {
//            age.setRangePinsByValue(setting.getAge_range()[0], setting.getAge_range()[1]);
//            ageSet.setText(setting.getAge_range()[0]+"-"+setting.getAge_range()[1]+" yrs");
//        }
//        if (setting.getPeople_connect_preference() == 0){
//            prefernce.setText("Both    >");
//        }else if (setting.getPeople_connect_preference() == 1){
//            prefernce.setText("Only Men    >");
//        }else if (setting.getPeople_connect_preference() == 2){
//            prefernce.setText("Only Women    >");
//        }
        coinsFinal = setting.getTotal_coins();
        PrefManager pref = new PrefManager(SettingsActivity.this);
        pref.setKeyCoins(coinsFinal);
    }
}
