package in.tagbin.mitohealthapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.PrefernceModel;
import in.tagbin.mitohealthapp.model.SendEditProfileModel;
import in.tagbin.mitohealthapp.model.SetGoalModel;
import in.tagbin.mitohealthapp.model.UserDateModel;
import in.tagbin.mitohealthapp.model.UserGoalWeightModel;
import in.tagbin.mitohealthapp.model.UserHeightModel;
import in.tagbin.mitohealthapp.model.UserModel;
import in.tagbin.mitohealthapp.model.UserNameModel;
import in.tagbin.mitohealthapp.model.UserWaistModel;
import in.tagbin.mitohealthapp.model.UserWeightModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by chetan on 16/11/16.
 */

public class SignUpDetailActivity extends AppCompatActivity implements View.OnClickListener {
    EditText tv_name, tv_dateOfBirth,tv_height,tv_waist,tv_weight,tv_goal_weight;
    TextInputLayout textInputName,textInputDob,textInputHeight,textInputWaist,textInputWeight,textInputGoalWeight;
    TextView goalTimeValue;
    static double height = 0.0,weight = 0.0,waist = 0.0,goal_weight = 0.0;
    private int year, month, day,monthsValue = 8,prefernce_final,goal_id;
    SharedPreferences login_details;
    private Calendar calendar;
    PrefManager pref;
    DiscreteSeekBar monthsSeekbar;
    GifImageView progressBar;
    RadioButton male,female;
    Button submit;
    Spinner diet_preference,goal_type;
    String height_new = "",weight_new = "",goal_weight_new = "",waist_new = "",dob = "",user_id,url = "",name = "default",gender = "",first_name = "",last_name = "",email = "";
    double bmi_lower_limit=18.5,bmi_upper_limit = 24.9;
    int weight_speed = 500;
    boolean changed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup_detail);
        tv_name = (EditText) findViewById(R.id.etProfileName);
        tv_dateOfBirth = (EditText) findViewById(R.id.etProfileDob);
        tv_height = (EditText) findViewById(R.id.etProfileHeight);
        tv_waist = (EditText) findViewById(R.id.etProfileWaist);
        tv_weight = (EditText) findViewById(R.id.etProfileWeight);
        tv_goal_weight = (EditText) findViewById(R.id.etProfileGoalWeight);
        textInputName = (TextInputLayout) findViewById(R.id.profileName);
        textInputDob = (TextInputLayout) findViewById(R.id.profileDob);
        textInputHeight = (TextInputLayout) findViewById(R.id.profileHeight);
        textInputWaist = (TextInputLayout) findViewById(R.id.profileWaist);
        textInputWeight = (TextInputLayout) findViewById(R.id.profileWeight);
        textInputGoalWeight = (TextInputLayout) findViewById(R.id.profileGoalWeight);
        diet_preference = (Spinner) findViewById(R.id.spinnerDietPreference);
        goal_type = (Spinner) findViewById(R.id.spinnerGoal);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        monthsSeekbar = (DiscreteSeekBar) findViewById(R.id.seekbarMonths);
        goalTimeValue = (TextView) findViewById(R.id.tvGoalTimeValue);
        male = (RadioButton) findViewById(R.id.radioMale);
        female = (RadioButton) findViewById(R.id.radioFemale);
        submit = (Button) findViewById(R.id.buttonEnterProfile);
        submit.setOnClickListener(this);
        tv_dateOfBirth.setOnClickListener(this);
        tv_height.setOnClickListener(this);
        tv_goal_weight.setOnClickListener(this);
        tv_weight.setOnClickListener(this);
        tv_waist.setOnClickListener(this);
        pref = new PrefManager(this);
        login_details = getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);
        user_id = login_details.getString("user_id", "");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        progressBar.setVisibility(View.VISIBLE);
        Controller.getUserDetails(SignUpDetailActivity.this, user_id, mUserDetailsListener);
        if (pref.getCurrentPreferenceAsObject() != null){
            final List<PrefernceModel> diet_options = pref.getCurrentPreferenceAsObject();
            final List<String> diet = new ArrayList<String>();
            for (int i= 0;i<diet_options.size();i++){
                diet.add(diet_options.get(i).getRecipe_type());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_spinner1, diet);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            diet_preference.setAdapter(adapter);
            diet_preference.setSelection(0,false);
            diet_preference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("item selected", diet.get(i));
                    //unit[0] = diet_options.get(i);
                    for (int y= 0; y<diet_options.size();y++){
                        if (diet.get(i).equals(diet_options.get(y).getRecipe_type())){
                            prefernce_final = diet_options.get(y).getId();
                        }
                    }
                    Controller.setPreferences(SignUpDetailActivity.this,prefernce_final,user_id,mPreferenceListener);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else{
            Controller.getDietPrefernce(SignUpDetailActivity.this,mDietListener);
        }
        if (pref.getCurrentGoalAsObject() != null){
            final List<SetGoalModel> diet_options = pref.getCurrentGoalAsObject();
            final List<String> diet = new ArrayList<String>();
            for (int i= 0;i<diet_options.size();i++){
                diet.add(diet_options.get(i).getGoal());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item_spinner1, diet);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            goal_type.setAdapter(adapter);
            goal_type.setSelection(0,false);
            goal_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("item selected", diet.get(i));
                    changed = true;
                    //unit[0] = diet_options.get(i);
                    for (int y= 0; y<diet_options.size();y++){
                        if (diet.get(i).equals("Maintain weight")){
                            goal_weight = weight;
                            monthsValue = 0;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_goal_weight.setText(new DecimalFormat("##.#").format(goal_weight/1000).toString() +" Kg");
                                    goalTimeValue.setText(monthsValue+ " weeks");
                                    monthsSeekbar.setProgress(monthsValue);
                                }
                            });
                        }else if(diet.get(i).equals("Gain weight")){
                            goal_weight = 0;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_goal_weight.setText("");
                                }
                            });
                        }else if (diet.get(i).equals("Loose weight")){
                            goal_weight = 0;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_goal_weight.setText("");
                                }
                            });
                        }
                        if (diet.get(i).equals(diet_options.get(y).getGoal())){
                            goal_id = diet_options.get(y).getId();
                        }
                    }
                    Controller.setGoal(SignUpDetailActivity.this,goal_id,user_id,mPreferenceListener);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else {
            Controller.getGoals(this, mGoalsListener);
        }
        if (male.isChecked()){
            gender = "M";
            SharedPreferences.Editor saveGender = login_details.edit();
            saveGender.putString("gender", gender);
            saveGender.commit();
        }else if (female.isChecked()){
            gender = "F";
            SharedPreferences.Editor saveGender = login_details.edit();
            saveGender.putString("gender", gender);
            saveGender.commit();
        }
        monthsSeekbar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                //settingsModel.setMaximum_distance(value);
                changed = true;
                monthsValue = value;
                goalTimeValue.setText(monthsValue+ " weeks");
                return value;
            }
        });
        tv_name.addTextChangedListener(new MyTextWatcher(tv_name));
        tv_dateOfBirth.addTextChangedListener(new MyTextWatcher(tv_dateOfBirth));
        tv_height.addTextChangedListener(new MyTextWatcher(tv_height));
        tv_waist.addTextChangedListener(new MyTextWatcher(tv_waist));
        tv_weight.addTextChangedListener(new MyTextWatcher(tv_weight));
        tv_goal_weight.addTextChangedListener(new MyTextWatcher(tv_goal_weight));
    }
    public void  showDateDialog(){
        int j = 0, j1 = 0, j2 = 0;
        final DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        month = month + 1;
                        year = i;
                        month = i1 + 1;
                        day = i2;

                        if (month <= 9 && day <= 9) {
                            dob = year + "-" + "0" + month + "-" + "0" + day;
                            Log.d("dob", dob);
                        } else if (month <= 9 && day > 9) {
                            dob = year + "-" + "0" + month + "-" + day;
                            Log.d("dob", dob);
                        } else if (day <= 9 && month > 9) {
                            dob = year + "-" + month + "-" + "0" + day;
                            Log.d("dob", dob);
                        } else if (day > 9 && month > 9) {
                            dob = year + "-" + month + "-" + day;
                            Log.d("dob", dob);
                        }
                        SharedPreferences.Editor saveDob = login_details.edit();
                        saveDob.putString("dob", dob);
                        saveDob.commit();
                        tv_dateOfBirth.setText(dob);
                        UserDateModel userDateModel = new UserDateModel();
                        userDateModel.setDob(dob);
                        Controller.setUserDate(SignUpDetailActivity.this,userDateModel,user_id,mSetUserDetailsListener);
                        if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0  || dob == null || dob.equals("")) {
//                            if (first_name == null || first_name.equals("")){
//                                showNameDialog();
//                            }else
                            if (dob == null || dob.equals("")){
                                showDateDialog();
                            }else if (height == 0) {
                                //dialog.dismiss();
                                showHeightDialog();
                            } else if (weight == 0) {
                                //dialog.dismiss();
                                showWeightDialog();
                            } else if (waist == 0) {
                                //dialog.dismiss();
                                showWaistDialog();
                            } else if (goal_weight == 0) {
                                //dialog.dismiss();
                                showGoal_WeightDialog();
                            }
                        } else {
                            //dialog.dismiss();
                        }

                    }
                }, year, month, day);

        dpd.show();
    }
    public void showHeightDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_value_picker);
        final List<String> measuring_units = new ArrayList<>();
        //measuring_units.add("Feets");
        measuring_units.add("Centimeters");
        measuring_units.add("Inches");
        //measuring_units.add("Meters");
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        //final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Height");
        final EditText seekBar = (EditText) dialog.findViewById(R.id.height_seekbar);
        if (height != 0.0) {
            seekBar.setText(new DecimalFormat("##.#").format(height).toString());
        }
        seekBar.setSelectAllOnFocus(true);
        View done = dialog.findViewById(R.id.height_done);
        TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight);
        textInputLayout.setHint("Height");
        final String[] unit = {"Centimeters"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("item selected", measuring_units.get(i));
                unit[0] = measuring_units.get(i);
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()){
                    if (unit[0].equals("Inches")) {
                        seekBar.setSelectAllOnFocus(true);
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString())/2.54).toString());
                    } else if (unit[0].equals("Centimeters")) {
                        seekBar.setSelectAllOnFocus(true);
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString())*2.54).toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //seekBar.setMax(200);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    float i = Float.parseFloat(seekBar.getText().toString());
                    if (unit[0].equals("Inches")) {
                        height_new = new DecimalFormat("##.#").format(i).toString() + " Inches";
                        height = Float.parseFloat(seekBar.getText().toString()) * 2.54;
                    } else if (unit[0].equals("Centimeters")) {
                        height_new = new DecimalFormat("##.#").format(i).toString() + " cms";
                        height = Float.parseFloat(seekBar.getText().toString());
                    }
                    UserHeightModel userDateModel = new UserHeightModel();
                    userDateModel.setHeight(String.valueOf(height));
                    Controller.setUserHeight(SignUpDetailActivity.this,userDateModel,user_id,mSetUserDetailsListener);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("height", (float) height);
                    tv_height.setText(height_new);
                    Log.d("height value", height_new);
                    if(height != 0 && weight != 0 && !changed){
                        calculateDefaultValues(height,weight);
                    }
                }

                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0  || dob == null || dob.equals("")) {
//                    if (first_name == null || first_name.equals("")){
//                        dialog.dismiss();
//                        showNameDialog();
//                    }else
                    if (dob == null || dob.equals("")){
                        dialog.dismiss();
                        showDateDialog();
                    }else if (height == 0) {
                        dialog.dismiss();
                        showHeightDialog();
                    } else if (weight == 0) {
                        dialog.dismiss();
                        showWeightDialog();
                    } else if (waist == 0) {
                        dialog.dismiss();
                        showWaistDialog();
                    } else if (goal_weight == 0) {
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }
                } else {
                    dialog.dismiss();
                }

            }
        });
        dialog.show();


    }

    public void showWeightDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_value_picker);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Kg");
        measuring_units.add("Pounds");
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        //final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Weight");
        final EditText seekBar = (EditText) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight);
        textInputLayout.setHint("Weight");
        final String[] unit = {"Kg"};
        if (weight != 0.0) {
            seekBar.setText(new DecimalFormat("##.#").format(weight/1000).toString());
        }
        seekBar.setSelectAllOnFocus(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit[0] = measuring_units.get(i);
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    if (unit[0].equals("Kg")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 0.453592).toString());
                        seekBar.setSelectAllOnFocus(true);
                    } else if (unit[0].equals("Pounds")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 2.20462).toString());
                        seekBar.setSelectAllOnFocus(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    float i = Float.parseFloat(seekBar.getText().toString());
                    if (unit[0].equals("Kg")) {
                        weight_new = new DecimalFormat("##.#").format(i).toString() + " Kg";
                        weight = Float.parseFloat(seekBar.getText().toString()) * 1000;
                    } else if (unit[0].equals("Pounds")) {
                        weight_new = new DecimalFormat("##.#").format(i).toString() + " Pounds";
                        weight = Float.parseFloat(seekBar.getText().toString()) * 453.592;
                    }
                    UserWeightModel userDateModel = new UserWeightModel();
                    userDateModel.setWeight(String.valueOf(weight));
                    Controller.setUserWeight(SignUpDetailActivity.this,userDateModel,user_id,mSetUserDetailsListener);
                    tv_weight.setText(weight_new);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("weight", (float) weight);
                    if(height != 0 && weight != 0 && !changed){
                        calculateDefaultValues(height,weight);
                    }
                }

                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0  || dob == null || dob.equals("")) {
//                    if (first_name == null || first_name.equals("")){
//                        dialog.dismiss();
//                        showNameDialog();
//                    }else
                    if (dob == null || dob.equals("")){
                       dialog.dismiss();
                        showDateDialog();
                    }else if (height == 0) {
                        dialog.dismiss();
                        showHeightDialog();
                    } else if (weight == 0) {
                        dialog.dismiss();
                        showWeightDialog();
                    } else if (waist == 0) {
                        dialog.dismiss();
                        showWaistDialog();
                    } else if (goal_weight == 0) {
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }
                } else {
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    public void showGoal_WeightDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_value_picker);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Kg");
        measuring_units.add("Pounds");

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        //final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Goal Weight");

        final EditText seekBar = (EditText) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight);
        textInputLayout.setHint("Goal Weight");
        seekBar.setSelectAllOnFocus(true);
        final String[] unit = {"Kg"};
        if (goal_weight != 0.0) {
            seekBar.setText(new DecimalFormat("##.#").format(goal_weight/1000).toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit[0] = measuring_units.get(i);
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    if (unit[0].equals("Kg")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 0.453592).toString());
                        seekBar.setSelectAllOnFocus(true);
                    } else if (unit[0].equals("Pounds")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 2.20462).toString());
                        seekBar.setSelectAllOnFocus(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    float i = Float.parseFloat(seekBar.getText().toString());

                    if (unit[0].equals("Kg")) {
                        goal_weight_new = new DecimalFormat("##.#").format(i).toString() + " Kg";
                        goal_weight = Float.parseFloat(seekBar.getText().toString()) * 1000;
                    } else if (unit[0].equals("Pounds")) {
                        goal_weight_new = new DecimalFormat("##.#").format(i).toString() + " Pounds";
                        goal_weight = Float.parseFloat(seekBar.getText().toString()) * 453.592;
                    }
                    tv_goal_weight.setText(goal_weight_new);
                    UserGoalWeightModel userDateModel = new UserGoalWeightModel();
                    userDateModel.setGoal_weight(String.valueOf(goal_weight));
                    Controller.setUserGoalWeight(SignUpDetailActivity.this,userDateModel,user_id,mSetUserDetailsListener);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("goal_weight", (float) goal_weight);
                    if(goal_weight == weight){
                        goal_id = 3;
                    }else if (goal_weight < weight){
                        goal_id = 2;
                    }else if(goal_weight > weight){
                        goal_id = 1;
                    }
                    diet_preference.setSelection(goal_id - 1);
                }
                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0  || dob == null || dob.equals("")) {
//                    if (first_name == null || first_name.equals("")){
//                        dialog.dismiss();
//                        showNameDialog();
//                    }else
                    if (dob == null || dob.equals("")){
                        dialog.dismiss();
                        showDateDialog();
                    }else if (height == 0) {
                        dialog.dismiss();
                        showHeightDialog();
                    } else if (weight == 0) {
                        dialog.dismiss();
                        showWeightDialog();
                    } else if (waist == 0) {
                        dialog.dismiss();
                        showWaistDialog();
                    } else if (goal_weight == 0) {
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }
                } else {
                    dialog.dismiss();
                }

            }
        });


        dialog.show();


    }

    public void showWaistDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_value_picker);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Inches");
        measuring_units.add("Centimeters");

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        //final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Waist");
        final EditText seekBar = (EditText) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        seekBar.setSelectAllOnFocus(true);
        TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight);
        textInputLayout.setHint("Waist");
        final String[] unit = {"Inches"};
        if (waist != 0.0) {
            seekBar.setText(new DecimalFormat("##.#").format(waist/2.54).toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit[0] = measuring_units.get(i);
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    if (unit[0].equals("Inches")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) / 2.54).toString());
                        seekBar.setSelectAllOnFocus(true);
                    } else if (unit[0].equals("Centimeters")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 2.54).toString());
                        seekBar.setSelectAllOnFocus(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //seekBar.setMax(55);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    float i = Float.parseFloat(seekBar.getText().toString());
                    if (unit[0].equals("Inches")) {
                        waist_new = new DecimalFormat("##.#").format(i).toString() + " Inches";
                        waist = Float.parseFloat(seekBar.getText().toString()) * 2.54;
                    } else if (unit[0].equals("Centimeters")) {
                        waist_new = new DecimalFormat("##.#").format(i).toString() + " cms";
                        waist = Float.parseFloat(seekBar.getText().toString());
                    }
                    tv_waist.setText(waist_new);
                    UserWaistModel userDateModel = new UserWaistModel();
                    userDateModel.setWaist(String.valueOf(waist));
                    Controller.setUserWaist(SignUpDetailActivity.this,userDateModel,user_id,mSetUserDetailsListener);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("waist", (float) waist);
                    if(height != 0 && weight != 0 && !changed){
                        calculateDefaultValues(height,weight);
                    }
                }

                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0  || dob == null || dob.equals("")) {
//                    if (first_name == null || first_name.equals("")){
//                        dialog.dismiss();
//                        showNameDialog();
//                    }else
                    if (dob == null || dob.equals("")){
                        dialog.dismiss();
                        showDateDialog();
                    }else if (height == 0) {
                        dialog.dismiss();
                        showHeightDialog();
                    } else if (weight == 0) {
                        dialog.dismiss();
                        showWeightDialog();
                    } else if (waist == 0) {
                        dialog.dismiss();
                        showWaistDialog();
                    } else if (goal_weight == 0) {
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }
                } else {
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }
    public void showNameDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_value_picker);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        //final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        final TextView dialog_heading = (TextView) dialog.findViewById(R.id.dialogHeading);
        dialog_name.setText("Name");
        dialog_heading.setText("Enter Name");
        spinner.setVisibility(View.GONE);
        final EditText seekBar = (EditText) dialog.findViewById(R.id.height_seekbar);
        final EditText seekBar1 = (EditText) dialog.findViewById(R.id.height_seekbar1);
        if (first_name != null){
            seekBar.setText(first_name);
        }
        if (last_name != null){
            seekBar1.setText(last_name);
        }
        seekBar.setSelectAllOnFocus(true);
        seekBar.setInputType(InputType.TYPE_CLASS_TEXT);
        seekBar1.setSelectAllOnFocus(true);
        seekBar1.setInputType(InputType.TYPE_CLASS_TEXT);
        View done = dialog.findViewById(R.id.height_done);
        TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight);
        TextInputLayout textInputLayout1 = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight1);
        textInputLayout1.setVisibility(View.VISIBLE);
        textInputLayout.setHint("First Name");
        textInputLayout1.setHint("Last Name");

        //seekBar.setMax(200);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_name = seekBar.getText().toString();
                last_name = seekBar1.getText().toString();
                tv_name.setText(first_name + " " + last_name);
                UserNameModel userDateModel = new UserNameModel();
                userDateModel.setFirst_name(first_name);
                userDateModel.setLast_name(last_name);
                Controller.setUserName(SignUpDetailActivity.this,userDateModel,user_id,mSetUserDetailsListener);
                if (height == 0 || waist == 0 || weight == 0 || dob == null || dob.equals("")) {
//                    if (first_name == null || first_name.equals("")){
//                        dialog.dismiss();
//                        showNameDialog();
//                    }else
                    if (dob == null || dob.equals("")){
                        dialog.dismiss();
                        showDateDialog();
                    }else if (height == 0) {
                        dialog.dismiss();
                        showHeightDialog();
                    } else if (weight == 0) {
                        dialog.dismiss();
                        showWeightDialog();
                    } else if (waist == 0) {
                        dialog.dismiss();
                        showWaistDialog();
                    } else if (goal_weight == 0) {
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    RequestListener mSetUserDetailsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            final UserModel userModel = JsonUtils.objectify(responseObject.toString(), UserModel.class);
            pref.setKeyUserDetails(userModel);
        }

        @Override
        public void onRequestError(int errorCode, String message) {

            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    SharedPreferences loginDetails= getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=loginDetails.edit();
                    editor.clear();
                    editor.commit();
                    PrefManager pref = new PrefManager(SignUpDetailActivity.this);
                    pref.clearSession();
                    startActivity(new Intent(SignUpDetailActivity.this,SplashActivity.class));
                    finish();
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUserDetailsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("edit profile",responseObject.toString());
            final UserModel userModel = JsonUtils.objectify(responseObject.toString(), UserModel.class);
            pref.setKeyUserDetails(userModel);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
//                    SharedPreferences.Editor editor = login_details.edit();
//                    if (userModel != null && userModel.getProfile() != null && userModel.getUser() != null) {
//                        editor.putString("user_username", userModel.getUser().getUsername());
//                        editor.putString("dob", userModel.getProfile().getDob());
//                        editor.putInt("height", (int) userModel.getProfile().getHeight());
//                        editor.putString("gender", userModel.getProfile().getGender());
//                        editor.putString("user_first_name", userModel.getUser().getFirst_name());
//                        editor.putString("user_last_name", userModel.getUser().getLast_name());
//                        editor.putInt("weight", (int) userModel.getProfile().getWeight());
//                        editor.putInt("goal_weight", (int) userModel.getProfile().getGoal_weight());
//                        editor.putInt("waist", (int) userModel.getProfile().getWaist());
//                        editor.putString("user_email", userModel.getUser().getEmail());
//                    }
//                    if (userModel != null && userModel.getEnergy() != null && userModel.getEnergy().size() == 5) {
//                        editor.putString("water_amount", userModel.getEnergy().get(1));
//                        editor.putString("food_cal", userModel.getEnergy().get(2));
//                        editor.putString("calorie_burnt", userModel.getEnergy().get(3));
//                        editor.putString("total_calorie_required", userModel.getEnergy().get(4));
//                    }
//
//                    editor.commit();
                    updateProfile(userModel);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("edit profile error",message);

            if (pref.getKeyUserDetails() != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        updateProfile(pref.getKeyUserDetails());
                    }
                });
            }
            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    SharedPreferences loginDetails= getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=loginDetails.edit();
                    editor.clear();
                    editor.commit();
                    PrefManager pref = new PrefManager(SignUpDetailActivity.this);
                    pref.clearSession();
                    startActivity(new Intent(SignUpDetailActivity.this,SplashActivity.class));
                    finish();
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public void updateProfile(final UserModel userModel) {
        if (userModel.getProfile().getDob() != null) {
            dob = userModel.getProfile().getDob();
            tv_dateOfBirth.setText(dob);
        }
        gender = userModel.getProfile().getGender();
        email = userModel.getUser().getEmail();
        first_name = userModel.getUser().getFirst_name();
        last_name = userModel.getUser().getLast_name();
        height = userModel.getProfile().getHeight();
        weight = userModel.getProfile().getWeight();
        goal_weight = userModel.getProfile().getGoal_weight();
        waist = userModel.getProfile().getWaist();
        monthsValue = userModel.getProfile().getGoal_time()/7;
        goalTimeValue.setText(monthsValue+ " weeks");
        monthsSeekbar.setProgress(monthsValue);
        if (goal_weight != 0 && weight != 0){
            double goal = goal_weight/1000;
            double weightFinal = weight/1000;
//            monthsHeading.setVisibility(View.VISIBLE);
//            if (weightFinal - goal <0 ){
//                monthsHeading.setText("Gaining "+new DecimalFormat("##.#").format((-(weightFinal-goal)/monthsValue)).toString()+" kgs/week");
//            }else{
//                monthsHeading.setText("Loosing "+new DecimalFormat("##.#").format(((weightFinal-goal)/monthsValue)).toString()+" kgs/week");
//            }

        }else{
//            monthsHeading.setVisibility(View.GONE);
        }
        if (userModel.getUser().getFirst_name() != null) {
            if (userModel.getUser().getLast_name() != null) {
                tv_name.setText(userModel.getUser().getFirst_name() + " " + userModel.getUser().getLast_name());
            }else
                tv_name.setText(userModel.getUser().getFirst_name());
        }
        if (gender.equals("M")) {
            male.setChecked(true);
//            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
//            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_f));
        } else if (gender.equals("F")) {
            female.setChecked(true);
//            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_m));
//            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
        } else {
            male.setChecked(true);
//            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
//            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_f));
        }


        if (height != 0) {
            //double fee = height *0.03;
            //double inch = height % 12;

            tv_height.setText(new DecimalFormat("##.#").format(height).toString()+ " cms" );

        }
        if (weight != 0) {
            double kg = weight / 1000;
            tv_weight.setText(new DecimalFormat("##.#").format(kg).toString() +" Kg");
        }
        if(height != 0 && weight != 0 && !changed){
            calculateDefaultValues(height,weight);
        }
        if (goal_weight != 0) {
            double grams = goal_weight % 1000;
            double kg = goal_weight / 1000;
            //changed = true;
            tv_goal_weight.setText(new DecimalFormat("##.#").format(kg).toString() +" Kg");
        }
        if (waist != 0) {
            //double inv = waist % 0.39;
            tv_waist.setText(new DecimalFormat("##.#").format(waist/2.54).toString()+ " inches");
        }
        if (userModel.getProfile().getGoal() != null) {
            //changed = true;
            goal_type.post(new Runnable() {
                @Override
                public void run() {
                    goal_type.setSelection(userModel.getProfile().getGoal().getId() - 1);
                }
            });
        }

        if (userModel.getProfile().getPreferences() != null){
            diet_preference.post(new Runnable() {
                @Override
                public void run() {
                    diet_preference.setSelection(userModel.getProfile().getPreferences().getId()-1);
                }
            });

        }

    }
    RequestListener mPreferenceListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    SharedPreferences loginDetails= getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=loginDetails.edit();
                    editor.clear();
                    editor.commit();
                    PrefManager pref = new PrefManager(SignUpDetailActivity.this);
                    pref.clearSession();
                    startActivity(new Intent(SignUpDetailActivity.this,SplashActivity.class));
                    finish();
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mDietListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Type collectionType = new TypeToken<ArrayList<PrefernceModel>>() {
            }.getType();
            final List<PrefernceModel> diet_options = (ArrayList<PrefernceModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);

            pref.saveCurrentPrefernces(diet_options);
            final List<String> diet = new ArrayList<String>();
            for (int i= 0;i<diet_options.size();i++){
                diet.add(diet_options.get(i).getRecipe_type());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpDetailActivity.this,R.layout.item_spinner1, diet);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    diet_preference.setAdapter(adapter);
                    diet_preference.setSelection(0,false);
                    diet_preference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("item selected", diet.get(i));
                            //unit[0] = diet_options.get(i);
                            for (int y= 0; y<diet_options.size();y++){
                                if (diet.get(i).equals(diet_options.get(y).getRecipe_type())){
                                    prefernce_final = diet_options.get(y).getId();
                                }
                            }
                            Controller.setPreferences(SignUpDetailActivity.this,prefernce_final,user_id,mPreferenceListener);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    SharedPreferences loginDetails= getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=loginDetails.edit();
                    editor.clear();
                    editor.commit();
                    PrefManager pref = new PrefManager(SignUpDetailActivity.this);
                    pref.clearSession();
                    startActivity(new Intent(SignUpDetailActivity.this,SplashActivity.class));
                    finish();
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mGoalsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Type collectionType = new TypeToken<ArrayList<SetGoalModel>>() {
            }.getType();
            final List<SetGoalModel> diet_options = (ArrayList<SetGoalModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);

            pref.saveCurrentGoal(diet_options);
            final List<String> diet = new ArrayList<String>();
            for (int i= 0;i<diet_options.size();i++){
                diet.add(diet_options.get(i).getGoal());
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpDetailActivity.this,R.layout.item_spinner1, diet);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    goal_type.setAdapter(adapter);
                    goal_type.setSelection(0,false);
                    goal_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Log.d("item selected", diet.get(i));
                            changed = true;
                            //unit[0] = diet_options.get(i);
                            if (diet.get(i).equals("Maintain weight")){
                                goal_weight = weight;
                                monthsValue = 0;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_goal_weight.setText(new DecimalFormat("##.#").format(goal_weight/1000).toString() +" Kg");
                                        goalTimeValue.setText(monthsValue+ " weeks");
                                        monthsSeekbar.setProgress(monthsValue);
                                    }
                                });
                            }else if(diet.get(i).equals("Gain weight")){
                                goal_weight = 0;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_goal_weight.setText("");
                                    }
                                });
                            }else if (diet.get(i).equals("Loose weight")){
                                goal_weight = 0;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_goal_weight.setText("");
                                    }
                                });
                            }
                            for (int y= 0; y<diet_options.size();y++){
                                if (diet.get(i).equals(diet_options.get(y).getGoal())){
                                    goal_id = diet_options.get(y).getId();
                                }
                            }
                            Controller.setGoal(SignUpDetailActivity.this,goal_id,user_id,mPreferenceListener);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    SharedPreferences loginDetails= getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=loginDetails.edit();
                    editor.clear();
                    editor.commit();
                    PrefManager pref = new PrefManager(SignUpDetailActivity.this);
                    pref.clearSession();
                    startActivity(new Intent(SignUpDetailActivity.this,SplashActivity.class));
                    finish();
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.etProfileDob:
                showDateDialog();
                break;
            case R.id.etProfileHeight:
                showHeightDialog();
                break;
            case R.id.etProfileWeight:
                showWeightDialog();
                break;
            case R.id.etProfileWaist:
                showWaistDialog();
                break;
            case R.id.etProfileGoalWeight:
                changed = true;
                showGoal_WeightDialog();
                break;
            case R.id.buttonEnterProfile:
                pref.setKeyUserDetails(null);
                if (!validateName())
                    return;
                if(!validateHeight())
                    return;
                if (!validateWeight())
                    return;
                if (!validatedob())
                    return;
                if (!validateWaist())
                    return;
                if (!validateGoalWeight())
                    return;
                SendEditProfileModel sendEditProfileModel = new SendEditProfileModel();
                sendEditProfileModel.setDob(dob);
                sendEditProfileModel.setEmail(email);
                sendEditProfileModel.setFirst_name(first_name);
                sendEditProfileModel.setGender(gender);
                sendEditProfileModel.setLast_name(last_name);
                sendEditProfileModel.setGoal_weight(String.valueOf(goal_weight));
                sendEditProfileModel.setHeight(String.valueOf(height));
                sendEditProfileModel.setWaist(String.valueOf(waist));
                sendEditProfileModel.setWeight(String.valueOf(weight));
                sendEditProfileModel.setPreferences(prefernce_final);
                sendEditProfileModel.setGoal_time(monthsValue*7);
                sendEditProfileModel.setGoal(goal_id);
                progressBar.setVisibility(View.VISIBLE);
                Controller.setUserDetails(SignUpDetailActivity.this,user_id,sendEditProfileModel,mSendUserListener);
                break;
        }
    }
    RequestListener mSendUserListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("user set", responseObject.toString());
            pref.setKeyUserDetails(JsonUtils.objectify(responseObject.toString(),UserModel.class));
            pref.setSignup(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(SignUpDetailActivity.this, BinderActivity.class).putExtra("selection", 0).putExtra("source", "direct"));
                    finish();
                    //Toast.makeText(SignUpDetailActivity.this, "Profile Updated Successfuly", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("send user error",message);
            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    SharedPreferences loginDetails= getSharedPreferences(MainActivity.LOGIN_DETAILS,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=loginDetails.edit();
                    editor.clear();
                    editor.commit();
                    PrefManager pref = new PrefManager(SignUpDetailActivity.this);
                    pref.clearSession();
                    startActivity(new Intent(SignUpDetailActivity.this,SplashActivity.class));
                    finish();
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpDetailActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View view){
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.etProfileName:
                    validateName();
                    break;
                case R.id.etProfileDob:
                    validatedob();
                    break;
                case R.id.etProfileHeight:
                    validateHeight();
                    break;
                case R.id.etProfileWaist:
                    validateWaist();
                    break;
                case R.id.etProfileWeight:
                    validateWeight();
                    break;
                case R.id.etProfileGoalWeight:
                    validateGoalWeight();
                    break;
            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateName(){
        if(tv_name.getText().toString().trim().isEmpty()){
            textInputName.setError("Please enter name");
            requestFocus(tv_name);
            return false;
        }else {
            textInputName.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validatedob(){
        if(tv_dateOfBirth.getText().toString().trim().isEmpty()){
//            textInputDob.setError("Please enter date of birth");
//            requestFocus(tv_dateOfBirth);
            return false;
        }else {
            textInputDob.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validateHeight(){
        if(tv_height.getText().toString().trim().isEmpty()){
//            textInputHeight.setError("Please enter height");
//            requestFocus(tv_height);
            return false;
        }else {
            textInputHeight.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validateWeight(){
        if(tv_weight.getText().toString().trim().isEmpty()){
//            textInputWeight.setError("Please enter weight");
//            requestFocus(tv_weight);
            return false;
        }else {
            textInputWeight.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validateWaist(){
        if(tv_waist.getText().toString().trim().isEmpty()){
//            textInputWaist.setError("Please enter waist");
//            requestFocus(tv_waist);
            return false;
        }else {
            textInputWaist.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validateGoalWeight(){
        if(tv_goal_weight.getText().toString().trim().isEmpty()){
//            textInputGoalWeight.setError("Please enter goal weight");
//            requestFocus(tv_goal_weight);
            return false;
        }else {
            textInputGoalWeight.setErrorEnabled(false);

        }
        return true;
    }
    public void calculateDefaultValues(double height,double weight){
        double weight_delta;
        double curerentBmi = MyUtils.calculateBmi(height,weight);
        if (curerentBmi < bmi_lower_limit){
            goal_id = 1;
            goal_weight = MyUtils.calculateGoalWeight(bmi_lower_limit,height);
            weight_delta = goal_weight - weight;
        }else if(curerentBmi > bmi_upper_limit){
            goal_id = 2;
            goal_weight = MyUtils.calculateGoalWeight(bmi_upper_limit,height);
            weight_delta = weight - goal_weight;
        }else {
            goal_id = 3;
            goal_weight = weight;
            weight_delta = 0;
        }
        monthsValue = (int) (weight_delta/weight_speed);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_goal_weight.setText(new DecimalFormat("##.#").format(goal_weight/1000).toString() +" Kg");
                goalTimeValue.setText(monthsValue+ " weeks");
                monthsSeekbar.setProgress(monthsValue);
                diet_preference.setSelection(goal_id-1);
            }
        });
    }
}
