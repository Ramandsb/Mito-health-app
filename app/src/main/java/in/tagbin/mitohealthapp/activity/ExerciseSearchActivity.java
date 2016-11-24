package in.tagbin.mitohealthapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Line;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.adapter.ExerciseSearchAdapter;
import in.tagbin.mitohealthapp.adapter.FoodLoggerSearchAdapter;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CustomAutoCompleteTextView;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ExerciseModel;
import in.tagbin.mitohealthapp.model.HitsArrayModel;
import in.tagbin.mitohealthapp.model.SendExerciseLogModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 3/10/16.
 */

public class ExerciseSearchActivity extends AppCompatActivity{
    CustomAutoCompleteTextView auto_tv;
    String back = "";
    TextView suggestExercise;
    GifImageView progressBar;
    ExerciseSearchAdapter adapter1;
    PrefManager pref;
    int day,month,year,hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);
        pref = new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        suggestExercise = (TextView) findViewById(R.id.tvSuggestFood);
        suggestExercise.setText("Suggest Exercise");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //customDialog();
        getSupportActionBar().setTitle("Search Exercise");
        back = getIntent().getStringExtra("back");
        auto_tv = (CustomAutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        auto_tv.setFocusable(true);
        auto_tv.setThreshold(1);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(auto_tv, InputMethodManager.SHOW_FORCED);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        auto_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (auto_tv.getRight() - auto_tv.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        auto_tv.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        auto_tv.setOnItemClickListener(mAutocompleteClickListener);
        adapter1 = new ExerciseSearchAdapter(this, android.R.layout.simple_list_item_1);
        auto_tv.setAdapter(adapter1);
        auto_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.length() > 2) {
                    if (!auto_tv.isPopupShowing()) {
                        suggestExercise.setVisibility(View.VISIBLE);
                        suggestExercise.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(ExerciseSearchActivity.this,AddInterestActivity.class);
                                i.putExtra("exercise","exercise");
                                i.putExtra("tv_name",editable.toString());
                                startActivity(i);
                            }
                        });
                    } else {
                        suggestExercise.setVisibility(View.GONE);
                    }
                }else{
                    suggestExercise.setVisibility(View.GONE);
                }
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (back.equals("exercise")) {
//                    startActivity(new Intent(FoodSearchActivity.this, DailyDetailsActivity.class).putExtra("selection", 2));
//                    finish();
//
//                } else if (back.equals("food")) {
//                    startActivity(new Intent(FoodSearchActivity.this, DailyDetailsActivity.class).putExtra("selection", 0));
//                    finish();
//                }
//            }
//        });
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            HitsArrayModel hitsArrayModel = adapter1.getItem(position);
            auto_tv.setText(hitsArrayModel.getName());
            progressBar.setVisibility(View.VISIBLE);
            Controller.getExerciseDetails(ExerciseSearchActivity.this,hitsArrayModel.getExercise_id(),mFoodDetailListener);
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onPause() {
//        exitToBottomAnimation();
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
        super.onPause();
    }

    RequestListener mFoodDetailListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("exercise data",responseObject.toString());
            final ExerciseModel data = JsonUtils.objectify(responseObject.toString(),ExerciseModel.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    showExerciseDialog(data);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExerciseSearchActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExerciseSearchActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public void showExerciseDialog(final ExerciseModel data){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        if (Build.VERSION.SDK_INT >= 22) {
            dialog = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        } else {
            dialog = new AlertDialog.Builder(this);
        }
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_exercise_dialog,linearLayout,false);
        //TextView heading = (TextView) view.findViewById(R.id.tvExerciseDialogName);
        //heading.setText(data.getName());
        final EditText time = (EditText) view.findViewById(R.id.etExerciseTime);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinnerExerciseIntensity);
        final List<String> intensities = new ArrayList<String>();
        intensities.add("Light");
        intensities.add("Moderate");
        intensities.add("Heavy");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, intensities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1,false);
        final String[] unit = {"Moderate"};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unit[0] = intensities.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final EditText calories = (EditText) view.findViewById(R.id.etExerciseCalories);
        LinearLayout linearSpeed = (LinearLayout) view.findViewById(R.id.linearExerciseSpeed);
        LinearLayout linearGradient = (LinearLayout) view.findViewById(R.id.linearExerciseGradient);
        LinearLayout linearAllDetails = (LinearLayout) view.findViewById(R.id.linearExerciseDialogDetails);
        if (data.getMETS_RMR() == null){
            spinner.setVisibility(View.VISIBLE);
            linearAllDetails.setWeightSum(2);
        }else{
            spinner.setVisibility(View.GONE);
            linearAllDetails.setWeightSum(1);
        }
        linearLayout.addView(view);
        dialog.setView(linearLayout);
        dialog.setMessage(null);
        dialog.setTitle(data.getName());
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
                    day = pref.getKeyDay();
                    month = pref.getKeyMonth();
                    year = pref.getKeyYear();
                }else {
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                }
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                Date date = new Date(year-1900,month,day,hour,minute);
                long timestamp = date.getTime()/1000L;
                if (calories.getText().toString().equals("") || calories.getText().toString().equals("0") || calories.getText().toString() == null){
                    if (time.getText().toString().equals("") || time.getText().toString() == null){
                        Toast.makeText(ExerciseSearchActivity.this,"Please enter minutes to do this exercise",Toast.LENGTH_LONG).show();
                    }else {
                        SendExerciseLogModel sendExerciseLogModel = new SendExerciseLogModel();
                        sendExerciseLogModel.setAmount(Float.parseFloat(time.getText().toString()));
                        if(spinner.getVisibility() == View.VISIBLE) {
                            if (unit[0].equals("Light")) {
                                sendExerciseLogModel.setMets(data.getMETS_LI_BMR());
                            } else if (unit[0].equals("Moderate")) {
                                sendExerciseLogModel.setMets(data.getMETS_MI_BMR());
                            } else if (unit[0].equals("Heavy")) {
                                sendExerciseLogModel.setMets(data.getMETS_HI_BMR());
                            }
                        }else {
                            if (data.getMETS_RMR() != null) {
                                sendExerciseLogModel.setMets(Float.parseFloat(data.getMETS_RMR()));
                            }
                        }
                        sendExerciseLogModel.setC_id(data.getId());
                        sendExerciseLogModel.setCalorie(-1);
                        sendExerciseLogModel.setLtype("exercise");
                        sendExerciseLogModel.setTimestamp(String.valueOf(timestamp));
                        progressBar.setVisibility(View.VISIBLE);
                        Controller.setExerciseLogger(ExerciseSearchActivity.this,sendExerciseLogModel,mExerciseLoggerListener);
                    }
                }else{
                    SendExerciseLogModel sendExerciseLogModel = new SendExerciseLogModel();
                    sendExerciseLogModel.setAmount(-1);
                    sendExerciseLogModel.setMets(-1);
                    sendExerciseLogModel.setCalorie(Float.parseFloat(calories.getText().toString()));
                    sendExerciseLogModel.setC_id(data.getId());
                    sendExerciseLogModel.setTimestamp(String.valueOf(timestamp));
                    sendExerciseLogModel.setLtype("exercise");
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.setExerciseLogger(ExerciseSearchActivity.this,sendExerciseLogModel,mExerciseLoggerListener);
                }
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    RequestListener mExerciseLoggerListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("exercise logged",responseObject.toString());
            Intent i = new Intent(ExerciseSearchActivity.this,DailyDetailsActivity.class);
            i.putExtra("selection",2);
            startActivity(i);
            finish();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("exercise log error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExerciseSearchActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExerciseSearchActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
