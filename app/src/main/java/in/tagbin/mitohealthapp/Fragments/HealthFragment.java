package in.tagbin.mitohealthapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.io.File;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.helper.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.helper.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.helper.ProfileImage.PicModeSelectDialogFragment;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.BinderActivity;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.activity.SelectCuisineActivity;
import in.tagbin.mitohealthapp.activity.SetGoalsActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.CuisineModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.FileUploadModel;
import in.tagbin.mitohealthapp.model.ImageUploadResponseModel;
import in.tagbin.mitohealthapp.model.PrefernceModel;
import in.tagbin.mitohealthapp.model.SendEditProfileModel;
import in.tagbin.mitohealthapp.model.UserDateModel;
import in.tagbin.mitohealthapp.model.UserGoalWeightModel;
import in.tagbin.mitohealthapp.model.UserHeightModel;
import in.tagbin.mitohealthapp.model.UserModel;
import in.tagbin.mitohealthapp.model.UserNameModel;
import in.tagbin.mitohealthapp.model.UserWaistModel;
import in.tagbin.mitohealthapp.model.UserWeightModel;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class HealthFragment extends Fragment implements PicModeSelectDialogFragment.IPicModeSelectListener, View.OnClickListener {
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    TextView dob_tv, height_tv, weight_tv, waist_tv, goal_weight_tv,profile_name,cusineSize,goal_tv,coins,monthsHeading;
    ImageView profile_pic;
    SharedPreferences login_details;
    static double height = 0.0,weight = 0.0,waist = 0.0,goal_weight = 0.0;
    String height_new = "",weight_new = "",goal_weight_new = "",waist_new = "",dob = "",user_id,url = "",name = "default",gender = "";
    Button choose_image;
    GifImageView progressBar;
    RelativeLayout cusines;
    public static String myurl = "";
    public static Bitmap profileImage;
    View male_view;
    PrefManager pref;
    int prefernce_final,coinsFinal = 0,monthsValue = 8;
    GifImageView progressBar1;
    DiscreteSeekBar monthsSeekbar;
    Spinner diet_preference;
    LinearLayout mygoals,editName;
    View female_view;
    List<CuisineModel> cuisineModels;
    String first_name = "",last_name = "",email = "";
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////            Window w = getWindow();
////            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//        }
        View Fragview = inflater.inflate(R.layout.frag_health, container, false);
        pref = new PrefManager(getActivity());
        progressBar = (GifImageView) Fragview.findViewById(R.id.progressBar);
        choose_image = (Button) Fragview.findViewById(R.id.choose_image);
        View select_date = Fragview.findViewById(R.id.select_date);
        View select_height = Fragview.findViewById(R.id.select_height);
        View select_weight = Fragview.findViewById(R.id.select_weight);
        View select_waist = Fragview.findViewById(R.id.select_waist);
        editName = (LinearLayout) Fragview.findViewById(R.id.linearEditName);
        editName.setOnClickListener(this);
        progressBar1 = (GifImageView) Fragview.findViewById(R.id.progressBar1);
        View select_goal_weight = Fragview.findViewById(R.id.select_goal_weight);
        male_view = Fragview.findViewById(R.id.male_view);
        female_view = Fragview.findViewById(R.id.female_view);
        dob_tv = (TextView) Fragview.findViewById(R.id.dob);
        height_tv = (TextView) Fragview.findViewById(R.id.height_tv);
        weight_tv = (TextView) Fragview.findViewById(R.id.weight_tv);
        goal_weight_tv = (TextView) Fragview.findViewById(R.id.goal_weight_tv);
        waist_tv = (TextView) Fragview.findViewById(R.id.waist_tv);
        diet_preference = (Spinner) Fragview.findViewById(R.id.spinner_diet_preference);
        cusines = (RelativeLayout) Fragview.findViewById(R.id.relativeCuisines);
        mygoals = (LinearLayout) Fragview.findViewById(R.id.select_goals);
        goal_tv = (TextView) Fragview.findViewById(R.id.goals_tv);
        cusineSize = (TextView) Fragview.findViewById(R.id.tvCuisinesSize);
        profile_pic = (ImageView) Fragview.findViewById(R.id.profile_pic);
        profile_name = (TextView) Fragview.findViewById(R.id.profile_name);
        //months = (TextView) Fragview.findViewById(R.id.tvMonths);
        monthsHeading = (TextView) Fragview.findViewById(R.id.tvMonthsHeading);
        monthsSeekbar = (DiscreteSeekBar) Fragview.findViewById(R.id.seekbarMonths);
        mygoals.setOnClickListener(this);
        login_details = getActivity().getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);
        user_id = login_details.getString("user_id", "");

        calendar = Calendar.getInstance();
        cuisineModels = new ArrayList<CuisineModel>();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //customDialog();

        if (pref.getKeyUserDetails() != null){
            updateProfile(pref.getKeyUserDetails());
        }else {
            progressBar.setVisibility(View.VISIBLE);
            Controller.getUserDetails(getContext(), user_id, mUserDetailsListener);
        }

        if (login_details.getFloat("height",0) != 0) {
            height_tv.setText(new DecimalFormat("##.#").format(height).toString() + " cms");
        }
        if (login_details.getFloat("weight",0) != 0) {
            double kg = weight / 1000;
            weight_tv.setText(new DecimalFormat("##.#").format(kg).toString() +" Kg");
        }
        if (login_details.getFloat("goal_weight",0) != 0) {
            double grams = goal_weight % 1000;
            double kg = goal_weight / 1000;
            goal_weight_tv.setText(new DecimalFormat("##.#").format(kg).toString() +" Kg");
        }
        if (login_details.getFloat("waist",0) != 0) {
            waist_tv.setText(new DecimalFormat("##.#").format(waist).toString()+ " cms");
        }

        //makeJsonObjReq();

        //name = getActivity().getIntent().getStringExtra("name");

        Log.d("check url", "" + myurl);
//        if (url.equals("")) {
//            if (profileImage == null) {
//
//                profile_pic.setImageDrawable(getResources().getDrawable(R.drawable.profile_tabicon));
//
//            } else {
//                profile_pic.setImageBitmap(profileImage);
//            }
//
//        } else {
//            new DownloadImage().execute(url);
//        }

//        Picasso.with(this).load(url).into(profile_pic);

        checkPermissions();
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProfilePicDialog();
            }
        });

        male_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
                female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_f));
                gender = "M";
                SharedPreferences.Editor saveGender = login_details.edit();
                saveGender.putString("gender", gender);
                saveGender.commit();
            }
        });
        female_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_m));
                female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_));
                gender = "F";

                SharedPreferences.Editor saveGender = login_details.edit();
                saveGender.putString("gender", gender);
                saveGender.commit();
            }
        });
        if (pref.getCurrentPreferenceAsObject() != null){
            final List<PrefernceModel> diet_options = pref.getCurrentPreferenceAsObject();
            final List<String> diet = new ArrayList<String>();
            for (int i= 0;i<diet_options.size();i++){
                diet.add(diet_options.get(i).getRecipe_type());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.item_spinner, diet);
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
                    Controller.setPreferences(getContext(),prefernce_final,user_id,mPreferenceListener);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else{
            Controller.getDietPrefernce(getContext(),mDietListener);
        }
        assert select_date != null;
        select_date.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        select_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WheelDialog("weight","select");
                showWeightDialog();
            }
        });
        select_goal_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WheelDialog("weight","select");
                showGoal_WeightDialog();
            }
        });
        select_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WheelDialog("height","select");
                showHeightDialog();
            }
        });
        select_waist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WheelDialog("waist","select");
                showWaistDialog();
            }
        });
        monthsSeekbar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                //settingsModel.setMaximum_distance(value);
                if (goal_weight != 0 && weight != 0){
                    double goal = goal_weight/1000;
                    double weightFinal = weight/1000;
                    monthsHeading.setVisibility(View.VISIBLE);
                    if (weightFinal - goal <0 ){
                        monthsHeading.setText("Gaining "+new DecimalFormat("##.#").format((-(weightFinal-goal)/value)).toString()+" kgs/week");
                    }else{
                        monthsHeading.setText("Loosing "+new DecimalFormat("##.#").format(((weightFinal-goal)/value)).toString()+" kgs/week");
                    }

                }else{
                    monthsHeading.setVisibility(View.GONE);
                }
                monthsValue = value;
                return value;
            }
        });
        return Fragview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == REQUEST_CODE_UPDATE_PIC) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar1.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener);
                showCroppedImage(imagePath);
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        }

    }
    RequestListener mUploadListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyMasterImage(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar1.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar1.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

    private void showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            profileImage = myBitmap;
            profile_pic.setImageBitmap(profileImage);

        }
    }


    private void showAddProfilePicDialog() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(this);
        dialogFragment.show(getActivity().getFragmentManager(), "picModeSelector");
    }

    private void actionProfilePic(String action) {
        Intent intent = new Intent(getActivity(), ImageCropActivity.class);
        intent.putExtra("ACTION", action);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
    }

    @SuppressLint("InlinedApi")
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);
        }
    }

    @Override
    public void onPicModeSelected(String mode) {
        String action = mode.equalsIgnoreCase(GOTOConstants.PicModes.CAMERA) ? GOTOConstants.IntentExtras.ACTION_CAMERA : GOTOConstants.IntentExtras.ACTION_GALLERY;
        actionProfilePic(action);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current item_tabs on orientation change.
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereProfile");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }

        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(true);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        menu.findItem(R.id.action_Settings).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0 ||first_name == null || first_name.equals("") || dob == null || dob.equals("")) {
                if (first_name == null || first_name.equals("")){
                    showNameDialog();
                }else if (dob == null || dob.equals("")){
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
                pref.setKeyUserDetails(null);
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
                if (pref.getKeyMasterImage() != null){
                    SendEditProfileModel.ImagesModel imagesModel = sendEditProfileModel.getImages();
                    imagesModel.setMaster(pref.getKeyMasterImage());
                    sendEditProfileModel.setImages(imagesModel);
                }
                progressBar.setVisibility(View.VISIBLE);
                Controller.setUserDetails(getContext(),user_id,sendEditProfileModel,mSendUserListener);
                //makeJsonObjReq(name, gender, dob, String.valueOf(height), String.valueOf(waist), String.valueOf(weight), String.valueOf(goal_weight));
            }

            return true;
        }else if (id == R.id.action_Settings) {
            if (pref.getKeyUserDetails() != null && pref.getKeyUserDetails().getProfile().getHeight() != 0 && pref.getKeyUserDetails().getProfile().getWeight() != 0){
                //toolbar_title.setText("Settings");
                //toolbar.setTitle("");
                //fra = new SettingsActivity();
                Intent i = new Intent(getContext(), SettingsActivity.class);
                startActivity(i);
            }else {
                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    RequestListener mSendUserListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("user set", responseObject.toString());
            pref.setKeyUserDetails(JsonUtils.objectify(responseObject.toString(),UserModel.class));
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    BinderActivity i = (BinderActivity) getActivity();
                    i.bottomNavigation.changeCurrentItem(0);
                    Toast.makeText(getActivity(), "Profile Updated Successfuly", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("send user error",message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public void  showDateDialog(){
        int j = 0, j1 = 0, j2 = 0;
        final DatePickerDialog dpd = new DatePickerDialog(getActivity(),
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
                        dob_tv.setText(dob);
                        UserDateModel userDateModel = new UserDateModel();
                        userDateModel.setDob(dob);
                        Controller.setUserDate(getContext(),userDateModel,user_id,mSetUserDetailsListener);
                        if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0 ||first_name == null || first_name.equals("") || dob == null || dob.equals("")) {
                            if (first_name == null || first_name.equals("")){
                                showNameDialog();
                            }else if (dob == null || dob.equals("")){
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

        final Dialog dialog = new Dialog(getActivity());
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, measuring_units);
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
                    Controller.setUserHeight(getContext(),userDateModel,user_id,mSetUserDetailsListener);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("height", (float) height);
                    height_tv.setText(height_new);
                    Log.d("height value", height_new);
                }
                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0 ||first_name == null || first_name.equals("") || dob == null || dob.equals("")) {
                    if (first_name == null || first_name.equals("")){
                        dialog.dismiss();
                        showNameDialog();
                    }else if (dob == null || dob.equals("")){
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
        final Dialog dialog = new Dialog(getActivity());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, measuring_units);
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
                    Controller.setUserWeight(getContext(),userDateModel,user_id,mSetUserDetailsListener);
                    weight_tv.setText(weight_new);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("weight", (float) weight);
                }
                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0 ||first_name == null || first_name.equals("") || dob == null || dob.equals("")) {
                    if (first_name == null || first_name.equals("")){
                        dialog.dismiss();
                        showNameDialog();
                    }else if (dob == null || dob.equals("")){
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

        final Dialog dialog = new Dialog(getActivity());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, measuring_units);
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
                    goal_weight_tv.setText(goal_weight_new);
                    UserGoalWeightModel userDateModel = new UserGoalWeightModel();
                    userDateModel.setGoal_weight(String.valueOf(goal_weight));
                    Controller.setUserGoalWeight(getContext(),userDateModel,user_id,mSetUserDetailsListener);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("goal_weight", (float) goal_weight);
                }
                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0 ||first_name == null || first_name.equals("") || dob == null || dob.equals("")) {
                    if (first_name == null || first_name.equals("")){
                        dialog.dismiss();
                        showNameDialog();
                    }else if (dob == null || dob.equals("")){
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

        final Dialog dialog = new Dialog(getActivity());
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, measuring_units);
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
                    waist_tv.setText(waist_new);
                    UserWaistModel userDateModel = new UserWaistModel();
                    userDateModel.setWaist(String.valueOf(waist));
                    Controller.setUserWaist(getContext(),userDateModel,user_id,mSetUserDetailsListener);
                    SharedPreferences.Editor editor = login_details.edit();
                    editor.putFloat("waist", (float) waist);
                }
                if (height == 0 || waist == 0 || weight == 0 || goal_weight == 0 ||first_name == null || first_name.equals("") || dob == null || dob.equals("")) {
                    if (first_name == null || first_name.equals("")){
                        dialog.dismiss();
                        showNameDialog();
                    }else if (dob == null || dob.equals("")){
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
        final Dialog dialog = new Dialog(getActivity());
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
                profile_name.setText(first_name + " " + last_name);
                UserNameModel userDateModel = new UserNameModel();
                userDateModel.setFirst_name(first_name);
                userDateModel.setLast_name(last_name);
                Controller.setUserName(getContext(),userDateModel,user_id,mSetUserDetailsListener);
                if (height == 0 || waist == 0 || weight == 0 ||first_name == null || first_name.equals("") || dob == null || dob.equals("")) {
                    if (first_name == null || first_name.equals("")){
                        dialog.dismiss();
                        showNameDialog();
                    }else if (dob == null || dob.equals("")){
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
    public void updateProfile(final UserModel userModel) {
        if (userModel.getProfile().getDob() != null) {
            dob = userModel.getProfile().getDob();
            dob_tv.setText(dob);
        }else {
            dob = "";
            dob_tv.setText("Set Date");
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
        monthsSeekbar.setProgress(monthsValue);
        if (goal_weight != 0 && weight != 0){
            double goal = goal_weight/1000;
            double weightFinal = weight/1000;
            monthsHeading.setVisibility(View.VISIBLE);
            if (weightFinal - goal <0 ){
                monthsHeading.setText("Gaining "+new DecimalFormat("##.#").format((-(weightFinal-goal)/monthsValue)).toString()+" kgs/week");
            }else{
                monthsHeading.setText("Loosing "+new DecimalFormat("##.#").format(((weightFinal-goal)/monthsValue)).toString()+" kgs/week");
            }

        }else{
            monthsHeading.setVisibility(View.GONE);
        }
        if (userModel.getUser().getFirst_name() != null) {
            if (userModel.getUser().getLast_name() != null) {
                profile_name.setText(userModel.getUser().getFirst_name() + " " + userModel.getUser().getLast_name());
            }else
                profile_name.setText(userModel.getUser().getFirst_name());
        }else{
            profile_name.setText("Your Name?");
        }
        if (gender.equals("M")) {
            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_f));
        } else if (gender.equals("F")) {
            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_m));
            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
        } else {
            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_f));
        }


        if (height == 0) {
            height_tv.setText("Set Height");
        } else {
            //double fee = height *0.03;
            //double inch = height % 12;

            height_tv.setText(new DecimalFormat("##.#").format(height).toString()+ " cms" );

        }
        if (weight == 0) {
            weight_tv.setText("Set Weight");
        } else {
            double kg = weight / 1000;
            weight_tv.setText(new DecimalFormat("##.#").format(kg).toString() +" Kg");
        }
        if (goal_weight == 0) {
            goal_weight_tv.setText("Set Weight");
        } else {
            double grams = goal_weight % 1000;
            double kg = goal_weight / 1000;
            goal_weight_tv.setText(new DecimalFormat("##.#").format(kg).toString() +" Kg");
        }
        if (waist == 0) {
            waist_tv.setText("Set Waist");
        } else {
            //double inv = waist % 0.39;
            waist_tv.setText(new DecimalFormat("##.#").format(waist/2.54).toString()+ " inches");
        }
        if (userModel.getProfile().getGoal() != null)
            goal_tv.setText(userModel.getProfile().getGoal().getGoal());
        if (userModel.getProfile().getCuisines() != null && userModel.getProfile().getCuisines().size() >0){
            cusineSize.setVisibility(View.VISIBLE);
            cusineSize.setText(""+userModel.getProfile().getCuisines().size());
            cusines.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),SelectCuisineActivity.class);
                    i.putExtra("response",JsonUtils.jsonify(userModel.getProfile().getCuisines()));
                    startActivity(i);
                }
            });
        }else{
            cusineSize.setVisibility(View.GONE);
            cusines.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),SelectCuisineActivity.class);
                    i.putExtra("response",JsonUtils.jsonify(userModel.getProfile().getCuisines()));
                    startActivity(i);
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
        if(pref.getKeyCoins() == 0){
            coinsFinal = userModel.getProfile().getTotal_coins();
            pref.setKeyCoins(userModel.getProfile().getTotal_coins());
        }
        if (userModel != null && userModel.getProfile().getImages() != null && userModel.getProfile().getImages().getMaster() != null) {
            pref.setKeyMasterImage(userModel.getProfile().getImages().getMaster());
            ImageLoader.getInstance().loadImage(userModel.getProfile().getImages().getMaster(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    profile_pic.setImageBitmap(loadedImage);
                }
            });
        }
    }

    RequestListener mUserDetailsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("edit profile",responseObject.toString());
            final UserModel userModel = JsonUtils.objectify(responseObject.toString(), UserModel.class);
            pref.setKeyUserDetails(userModel);
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
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
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_goals:
                Intent i = new Intent(getContext(),SetGoalsActivity.class);
                startActivity(i);
                break;
            case R.id.linearEditName:
                showNameDialog();
                break;
        }
    }
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

            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.item_spinner, diet);
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
                            Controller.setPreferences(getContext(),prefernce_final,user_id,mPreferenceListener);
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
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mPreferenceListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
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
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

}
