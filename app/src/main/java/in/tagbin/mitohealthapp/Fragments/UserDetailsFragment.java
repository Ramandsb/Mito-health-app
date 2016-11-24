package in.tagbin.mitohealthapp.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.adapter.UserDetailsSliderAdapter;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.helper.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.helper.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.helper.ProfileImage.PicModeSelectDialogFragment;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.FileUploadModel;
import in.tagbin.mitohealthapp.model.ImageUploadResponseModel;
import in.tagbin.mitohealthapp.model.UserDateModel;
import in.tagbin.mitohealthapp.model.UserGenderModel;
import in.tagbin.mitohealthapp.model.UserModel;
import in.tagbin.mitohealthapp.model.UserNameModel;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static in.tagbin.mitohealthapp.Fragments.HealthFragment.REQUEST_CODE_UPDATE_PIC;


public class UserDetailsFragment extends Fragment implements TabLayout.OnTabSelectedListener, PicModeSelectDialogFragment.IPicModeSelectListener {

    private TabLayout tablayout;
    private ViewPager vPager;
    TextView tabOne,tabTwo,tabTwo1;
    static public TextView name,dob,gender;
    static public CircleImageView circleView;
    UserDetailsSliderAdapter adapter;
    GifImageView progressBar1;
    RelativeLayout relativeName,relativeDob,relativeGender;
    public static String first_name,last_name,user_id,dob1,genderSel;
    public static int year,month,day;
    Calendar calendar;
    Button choose_image;
    PrefManager pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ProfileView = inflater.inflate(R.layout.frag_user_details, container, false);

        tablayout = (TabLayout) ProfileView.findViewById(R.id.ProfileTabs);
        vPager = (ViewPager) ProfileView.findViewById(R.id.ProfilePager);
        name = (TextView) ProfileView.findViewById(R.id.tvMainProfileName);
        dob = (TextView) ProfileView.findViewById(R.id.tvMainProfileDob);
        gender = (TextView) ProfileView.findViewById(R.id.tvMainProfileGender);
        circleView = (CircleImageView) ProfileView.findViewById(R.id.cvMainImage);
        relativeName = (RelativeLayout) ProfileView.findViewById(R.id.linearName);
        relativeDob = (RelativeLayout) ProfileView.findViewById(R.id.linearDob);
        relativeGender = (RelativeLayout) ProfileView.findViewById(R.id.linearGender);
        choose_image = (Button) ProfileView.findViewById(R.id.choose_image);
        progressBar1 = (GifImageView) ProfileView.findViewById(R.id.progressBar1);
        pref = new PrefManager(getContext());
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        relativeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNameDialog();
            }
        });
        relativeDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        relativeGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGenderDialog();
            }
        });
        if ( getArguments() !=null && getArguments().getString("profile_connect") != null){
            adapter = new UserDetailsSliderAdapter(getActivity().getSupportFragmentManager(),"profile_connect",getContext());
            vPager.setAdapter(adapter);
            tablayout.setupWithViewPager(vPager);

            vPager.setCurrentItem(1,true);
        }else{
            adapter = new UserDetailsSliderAdapter(getActivity().getSupportFragmentManager(),"",getContext());
            vPager.setAdapter(adapter);
            tablayout.setupWithViewPager(vPager);

            vPager.setCurrentItem(0,true);
        }

        tablayout.setOnTabSelectedListener(this);
        tablayout.setTabsFromPagerAdapter(adapter);
        vPager.setOffscreenPageLimit(3);

        setupTab();
        checkPermissions();
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProfilePicDialog();
            }
        });

        return ProfileView;
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
            //profileImage = myBitmap;
            circleView.setImageBitmap(myBitmap);

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
    private void setupTab() {
        try {
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.item_tabs, tablayout, false);

            tabOne = (TextView) relativeLayout.findViewById(R.id.tabtext);
            tabOne.setText("Health");
            tabOne.setTextColor(Color.parseColor("#4c516d"));
            tabOne.setGravity(Gravity.CENTER);
            tablayout.getTabAt(0).setCustomView(relativeLayout);

            RelativeLayout relativeLayout1 = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.item_tabs, tablayout, false);


            tabTwo = (TextView) relativeLayout1.findViewById(R.id.tabtext);
            tabTwo.setText("MitoConnect");
            tabTwo.setTextColor(Color.parseColor("#4c516d"));
            tabTwo.setGravity(Gravity.CENTER);

            tablayout.getTabAt(1).setCustomView(relativeLayout1);
            RelativeLayout relativeLayout2 = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.item_tabs, tablayout, false);


            tabTwo1 = (TextView) relativeLayout2.findViewById(R.id.tabtext);
            tabTwo1.setText("Account");
            tabTwo1.setTextColor(Color.parseColor("#4c516d"));
            tabTwo1.setGravity(Gravity.CENTER);

            tablayout.getTabAt(2).setCustomView(relativeLayout2);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
                name.setText(first_name + " " + last_name);
                UserNameModel userDateModel = new UserNameModel();
                userDateModel.setFirst_name(first_name);
                userDateModel.setLast_name(last_name);
                Controller.setUserName(getContext(),userDateModel,user_id,mSetUserDetailsListener);

                    dialog.dismiss();

            }
        });
        dialog.show();
    }
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
                            dob1 = year + "-" + "0" + month + "-" + "0" + day;
                            Log.d("dob", dob1);
                        } else if (month <= 9 && day > 9) {
                            dob1 = year + "-" + "0" + month + "-" + day;
                            Log.d("dob", dob1);
                        } else if (day <= 9 && month > 9) {
                            dob1 = year + "-" + month + "-" + "0" + day;
                            Log.d("dob", dob1);
                        } else if (day > 9 && month > 9) {
                            dob1 = year + "-" + month + "-" + day;
                            Log.d("dob", dob1);
                        }

                        dob.setText(dob1);
                        UserDateModel userDateModel = new UserDateModel();
                        userDateModel.setDob(dob1);
                        Controller.setUserDate(getContext(),userDateModel,user_id,mSetUserDetailsListener);


                    }
                }, year, month, day);

        dpd.show();
    }
    public void showGenderDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.item_value_picker);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        LinearLayout dialogSelection = (LinearLayout) dialog.findViewById(R.id.linearDialogSelection);
        dialogSelection.setVisibility(View.GONE);
        RelativeLayout radioSelction = (RelativeLayout) dialog.findViewById(R.id.profileGender);
        radioSelction.setVisibility(View.VISIBLE);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        final TextView dialog_heading = (TextView) dialog.findViewById(R.id.dialogHeading);
        dialog_name.setText("Gender");
        dialog_heading.setText("Select gender");
        final RadioButton male = (RadioButton) dialog.findViewById(R.id.radioMale);
        final RadioButton female = (RadioButton) dialog.findViewById(R.id.radioFemale);
        if (genderSel.equals("M")){
            male.setChecked(true);
        }else if (genderSel.equals("F")){
            female.setChecked(true);
        }else{
            male.setChecked(true);
        }

        View done = dialog.findViewById(R.id.height_done);
        //seekBar.setMax(200);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (male.isChecked()){
                    genderSel = "M";
                    gender.setText("Male");
                }else if (female.isChecked()){
                    genderSel = "F";
                    gender.setText("Female");
                }

                UserGenderModel userGenderModel = new UserGenderModel();
                userGenderModel.setGender(genderSel);
                Controller.setUserGender(getContext(),userGenderModel,user_id,mSetUserDetailsListener);
                dialog.dismiss();

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
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
