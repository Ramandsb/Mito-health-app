package in.tagbin.mitohealthapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.Profile;
import in.tagbin.mitohealthapp.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.ProfileImage.L;
import in.tagbin.mitohealthapp.ProfileImage.PicModeSelectDialogFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ProfilePage extends Fragment implements PicModeSelectDialogFragment.IPicModeSelectListener {
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    TextView dob_tv, height_tv, weight_tv, waist_tv,goal_weight_tv;
    String feet_val, inches_val, unit_val;
    String gender;
    String url = "", name = "default";
    ImageView profile_pic;
    TextView profile_name;
    SharedPreferences login_details;
    String user_id, auth_key;
    String dob = "";
   static int height=0, weight=0, waist=0,goal_weight=0;
    String height_new="";
    String height_unit="";
    String weight_new="";
    String goal_weight_new="";
    String weight_unit="";
    String waist_new="";
    String waist_unit="";
    Button choose_image;
    public static String myurl = "";
    public static Bitmap profileImage;
    String username;
    String first_name="";
    String last_name="";
    String email="";
    View male_view;
    View female_view;
    //////////////////////

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
        View Fragview = inflater.inflate(R.layout.content_profile_page, container, false);


        choose_image = (Button) Fragview.findViewById(R.id.choose_image);
        login_details = getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, Context.MODE_PRIVATE);
        user_id = login_details.getString("user_id", "");
        auth_key = login_details.getString("key", "");

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dob = year + "-" + month + "-" + day;
        customDialog();
        makeJsonObjReq();
        profile_pic = (ImageView) Fragview.findViewById(R.id.profile_pic);
        profile_name = (TextView) Fragview.findViewById(R.id.profile_name);
        name = getActivity().getIntent().getStringExtra("name");


//
//        if (getActivity().getIntent().hasExtra("picture")) {
//            url = getActivity().getIntent().getStringExtra("picture");
//        }
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
        profile_name.setText(name);
//        Picasso.with(this).load(url).into(profile_pic);
        View select_date = Fragview.findViewById(R.id.select_date);
        View select_height = Fragview.findViewById(R.id.select_height);
        View select_weight = Fragview.findViewById(R.id.select_weight);
        View select_waist = Fragview.findViewById(R.id.select_waist);
        View select_goal_weight = Fragview.findViewById(R.id.select_goal_weight);
        final View male_view = Fragview.findViewById(R.id.male_view);
        final View female_view = Fragview.findViewById(R.id.female_view);
        dob_tv = (TextView) Fragview.findViewById(R.id.dob);
        height_tv = (TextView) Fragview.findViewById(R.id.height_tv);
        weight_tv = (TextView) Fragview.findViewById(R.id.weight_tv);
        goal_weight_tv = (TextView) Fragview.findViewById(R.id.goal_weight_tv);
        waist_tv = (TextView) Fragview.findViewById(R.id.waist_tv);
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
                SharedPreferences.Editor saveGender= login_details.edit();
                saveGender.putString("gender",gender);
                saveGender.commit();
            }
        });
        female_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_m));
                female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_));
                gender = "F";

                SharedPreferences.Editor saveGender= login_details.edit();
                saveGender.putString("gender",gender);
                saveGender.commit();
            }
        });

        assert select_date != null;
        select_date.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
//                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//
//                                String time=hourOfDay + ":" + minute;
//
//                            }
//                        }, hour, min, false);
//                tpd.show();
                int j=0, j1=0, j2=0;
                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                month = month + 1;
                                year=i;
                                month=i1+1;
                                day=i2;

                                if (month<=9 && day <=9){
                                    dob = year + "-" + "0"+month + "-" + "0"+day;
                                    Log.d("dob",dob);
                                }else  if (month<=9 && day >9){
                                    dob = year + "-" + "0"+month + "-" + day;
                                    Log.d("dob",dob);
                                }else  if (day <=9 && month >9){
                                    dob = year + "-" +month + "-" + "0"+day;
                                    Log.d("dob",dob);
                                }else if (day >9 && month >9){
                                    dob = year + "-" + month + "-" + day;
                                    Log.d("dob", dob);
                                }
                                SharedPreferences.Editor saveDob= login_details.edit();
                                saveDob.putString("dob",dob);
                                saveDob.commit();
                                dob_tv.setText(dob);

                            }
                        }, year, month, day);

                dpd.show();
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
        return Fragview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == REQUEST_CODE_UPDATE_PIC) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                showCroppedImage(imagePath);
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        }

    }


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
        // lose the current tab on orientation change.
    }


//    public void WheelDialog(String source, final String init) {
//
//        String[] feets = null, inches = null, unit = null;
//        if (source.equals("weight")) {
//            feets = new String[101];
//            inches = new String[11];
//            unit = new String[]{"Kg"};
//            for (int i = 0; i <= 100; i++) {
//                feets[i] = "" + (i + 35);
//            }
//            for (int i = 0; i <= 9; i++) {
//                inches[i] = "" + (i + 1);
//            }
//
//            feet_val = "38";
//            inches_val = "4";
//            unit_val = "Kg";
//
//
//        } else if (source.equals("height")) {
//            feets = new String[]{"3", "4", "5", "6", "7", "8"};
//            inches = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
//            unit = new String[]{"feets"};
//            feet_val = "6";
//            inches_val = "4";
//            unit_val = "feets";
//        } else if (source.equals("waist")) {
//
//            inches = new String[]{"24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45"};
//            unit = new String[]{"inches"};
//
//            inches_val = "27";
//            unit_val = "inches";
//
//        }
//
//
//        View outerView = View.inflate(getActivity(), R.layout.wheel_view, null);
//        if (source.equals("waist")) {
//
//
//        } else {
//            WheelView wv1 = (WheelView) outerView.findViewById(R.id.wheel_view_wv1);
//            wv1.setOffset(2);
//            wv1.setVisibility(View.VISIBLE);
//            wv1.setItems(Arrays.asList(feets));
//            wv1.setSeletion(3);
//            wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//                @Override
//                public void onSelected(int selectedIndex, String item) {
//                    Log.d("feet", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//                    feet_val = item;
//                }
//            });
//        }
//        WheelView wv2 = (WheelView) outerView.findViewById(R.id.wheel_view_wv2);
//        wv2.setOffset(2);
//        wv2.setVisibility(View.VISIBLE);
//        wv2.setItems(Arrays.asList(inches));
//        wv2.setSeletion(3);
//        wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                Log.d("inches", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//
//                inches_val = item;
//
//            }
//        });
//        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv3);
//        wv.setOffset(2);
//        wv.setItems(Arrays.asList(unit));
//        wv.setSeletion(3);
//        wv.setVisibility(View.VISIBLE);
//        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
//            @Override
//            public void onSelected(int selectedIndex, String item) {
//                Log.d("Tag", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
//
//                unit_val = item;
//            }
//        });
//
//        if (source.equals("weight")) {
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Weight Tracker")
//                    .setView(outerView)
//                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                        phyEd.setText(""+Sfeet+"."+Sinches+"  feets");
//                            weight_tv.setText(feet_val + "." + inches_val + " " + unit_val);
//                            weight = (Integer.valueOf(feet_val) * 1000) + Integer.valueOf(inches_val);
//                            final SharedPreferences.Editor saveDetails=login_details.edit();
//                            saveDetails.putInt("weight",weight);
//                            saveDetails.commit();
//                            if (height==0 || waist==0 || weight==0 ){
//
//                                if (height==0){
//                                    WheelDialog("height",init);
//
//                                }else if(weight==0){
//                                    WheelDialog("weight",init);
//                                }else if(waist==0){
//                                    WheelDialog("waist",init);
//                                }
//
//                            }else {
//
//                                if (init.equals("save")){
//                                    makeJsonObjReq(name, gender, dob, String.valueOf(height), String.valueOf(waist), String.valueOf(weight));
//
//                                }else {
//
//                                }
//
//                            }
//
//
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .show();
//        } else if (source.equals("height")) {
//            new AlertDialog.Builder(getActivity())
//
//                    .setTitle("Height Tracker")
//                    .setView(outerView)
//                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            height_tv.setText(feet_val + "'" + inches_val + "''");
//                            height = (Integer.valueOf(feet_val) * 12) + Integer.valueOf(inches_val);
//                            final SharedPreferences.Editor saveDetails=login_details.edit();
//                            saveDetails.putInt("height",height);
//                            saveDetails.commit();
//                            if (height==0 || waist==0 || weight==0 ){
//
//
//                                if (height==0){
//                                    WheelDialog("height",init);
//
//                                }else if(weight==0){
//                                    WheelDialog("weight",init);
//                                }else if(waist==0){
//                                    WheelDialog("waist",init);
//                                }
//
//                            }else {
//                                if (init.equals("save")){
//                                    makeJsonObjReq(name, gender, dob, String.valueOf(height), String.valueOf(waist), String.valueOf(weight));
//
//                                }else {
//
//                                }
//                            }
//
//
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .show();
//        } else if (source.equals("waist")) {
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Waist Tracker")
//                    .setView(outerView)
//                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                        phyEd.setText(""+Sfeet+"."+Sinches+"  feets");
//                            waist_tv.setText(inches_val + "''");
//                            waist = Integer.valueOf(inches_val);
//                            final SharedPreferences.Editor saveDetails=login_details.edit();
//                            saveDetails.putInt("waist",waist);
//                            saveDetails.commit();
//                            if (height==0 || waist==0 || weight==0 ){
//
//                                if (height==0){
//                                    WheelDialog("height",init);
//
//                                }else if(weight==0){
//                                    WheelDialog("weight",init);
//                                }else if(waist==0){
//                                    WheelDialog("waist",init);
//                                }
//
//                            }else {
//                                if (init.equals("save")){
//                                    makeJsonObjReq(name, gender, dob, String.valueOf(height), String.valueOf(waist), String.valueOf(weight));
//
//                                }else {
//
//                                }
//                            }
//
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .show();
//        }
//
//
//    }




    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereProfile");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
        //InitActivity i = (InitActivity) getActivity();
        //i.getActionBar().setTitle("Profile");
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(true);
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

            if (height==0 || waist==0 || weight==0 ){

                if (height==0){
                    showHeightDialog();

                }else if(weight==0){
                    showWeightDialog();
                }else if(waist==0){
                    showWaistDialog();
                }else if(goal_weight==0){
                    showGoal_WeightDialog();
                }

            }else {

                makeJsonObjReq(name, gender, dob, String.valueOf(height), String.valueOf(waist), String.valueOf(weight),String.valueOf(goal_weight));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void makeJsonObjReq(String name, String sex, String dob, String height, String waist, String weight,String goal_weight) {
        showDialog();
        Map<String, String> postParam = new HashMap<String, String>();
        auth_key=login_details.getString("key","");
        user_id=login_details.getString("user_id","");

        Log.d("details", user_id + "//" + auth_key);



        postParam.put("first_name", first_name);
        postParam.put("last_name", last_name);
        postParam.put("email", email);
        postParam.put("dob", dob);
        postParam.put("gender", sex);
        postParam.put("height", height);
        postParam.put("waist", waist);
        postParam.put("weight", weight);
        postParam.put("goal_weight", goal_weight);


        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Config.url + "users/" + user_id + "/", new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d("response", res.toString());

                        dismissDialog();
                        Toast.makeText(getActivity(),"Profile Updated Successfuly",Toast.LENGTH_LONG).show();
                        BinderActivity i = (BinderActivity) getActivity();
                        i.bottomNavigation.setCurrentItem(2);
                        //startActivity(new Intent(ProfilePage.this, HomePage.class));
                        //finish();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());

                displayErrors(error);
                Log.d("error", error.toString());
            }
        }) {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", "JWT " + auth_key);
                return headers;
            }
//


        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

  public void  showHeightDialog(){

      final Dialog dialog = new Dialog(getActivity());
      dialog.setContentView(R.layout.height_dialog);
      final List<String> measuring_units = new ArrayList<>();
      measuring_units.add("Feets");
      measuring_units.add("Inches");
      measuring_units.add("Centimeters");
      measuring_units.add("Meters");


      Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
      final TextView value = (TextView) dialog.findViewById(R.id.height_value);
      final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
      dialog_name.setText("Height");
      final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.height_seekbar);
      View done = dialog.findViewById(R.id.height_done);
      done.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              height_tv.setText(height_new);
              Log.d("height value",height_new);
              if (height==0 || waist==0 || weight==0 ){


                  if (height==0){
                      dialog.dismiss();
                      showHeightDialog();

                  }else if(weight==0){
                      dialog.dismiss();
                      showWeightDialog();
                  }else if(waist==0){
                      dialog.dismiss();
                      showWaistDialog();
                  }else if(goal_weight==0){
                      dialog.dismiss();
                      showGoal_WeightDialog();
                  }

              }else {

                  dialog.dismiss();
              }

          }
      });
      final String[] unit = {"Feets"};

      ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,measuring_units);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner.setAdapter(adapter);
      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              Log.d("item selected",measuring_units.get(i));

              unit[0] =measuring_units.get(i);

              if(unit[0].equals("Feets")){

                  seekBar.setProgress(0);
                 value.setText("0.0 Feets");

              }else if(unit[0].equals("Inches")){

                  seekBar.setProgress(0);
                  value.setText("0.0 Inches");
              }else if(unit[0].equals("Centimeters")){

                  seekBar.setProgress(0);
                  value.setText("0.0 Centimeters");
              }else if(unit[0].equals("Meters")){
                  seekBar.setProgress(0);
                  value.setText("0.0 Meters");

              }


          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });
      seekBar.setMax(200);
      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

              height=i;
             if(unit[0].equals("Feets")){
                 double feet = i*0.083;
                 DecimalFormat df = new DecimalFormat("#.##");
              String val   =  df.format(feet);
                 value.setText(val+" Feets");
                 height_new=val+" Feets";

             }else if(unit[0].equals("Inches")){
                 double inches=i;
                 height_new=inches+" Inches";
                 value.setText(inches+" Inches");

             }else if(unit[0].equals("Centimeters")){


                 double cm= i*2.53;
                 DecimalFormat df = new DecimalFormat("#.##");
                 String val   =  df.format(cm);
                 height_new=val+" Centimeters";

                 value.setText(val+" Centimeters");


             }else if(unit[0].equals("Meters")){

                 double meters= i*0.025;

                 DecimalFormat df = new DecimalFormat("#.##");
                 String val   =  df.format(meters);
                 height_new=val+" Meters";
                 value.setText(val+" Meters");


             }
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {

          }
      });






      dialog.show();


    }
    public void  showWeightDialog(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.height_dialog);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Kg");
        measuring_units.add("Pounds");


        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Weight");
        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("weight value",weight_new);
                weight_tv.setText(weight_new);
                if (height==0 || waist==0 || weight==0 ){


                    if (height==0){
                        dialog.dismiss();
                       showHeightDialog();

                    }else if(weight==0){
                        dialog.dismiss();
                        showWeightDialog();
                    }else if(waist==0){
                        dialog.dismiss();
                       showWaistDialog();
                    }else if(goal_weight==0){
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }

                }else {
                    dialog.dismiss();
                }

            }
        });
        final String[] unit = {"Kg"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("item selected",measuring_units.get(i));
                unit[0] =measuring_units.get(i);

                if(unit[0].equals("Kg")){

                    seekBar.setProgress(0);
                    value.setText("0.0 Kg");

                }else if(unit[0].equals("Pounds")){

                    seekBar.setProgress(0);
                    value.setText("0.0 Pounds");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        seekBar.setMax(200000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                weight =i;

                if(unit[0].equals("Kg")){

                    double kg = i*0.001;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String val   =  df.format(kg);
                    weight_new=val+" Kg";
                    value.setText(val+" Kg");

                }else if(unit[0].equals("Pounds")){

                    double meters= i*0.00220462;

                    DecimalFormat df = new DecimalFormat("#.##");
                    String val   =  df.format(meters);
                    weight_new=val+" Pounds";
                    value.setText(val+" Pounds");

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






        dialog.show();


    }
    public void  showGoal_WeightDialog(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.height_dialog);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Kg");
        measuring_units.add("Pounds");


        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Goal Weight");
        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("weight value",goal_weight_new);
                goal_weight_tv.setText(goal_weight_new);
                if (height==0 || waist==0 || weight==0 ){


                    if (height==0){
                        dialog.dismiss();
                        showHeightDialog();

                    }else if(weight==0){
                        dialog.dismiss();
                        showWeightDialog();
                    }else if(waist==0){
                        dialog.dismiss();
                        showWaistDialog();
                    }else if(goal_weight==0){
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }

                }else {
                    dialog.dismiss();
                }

            }
        });
        final String[] unit = {"Kg"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("item selected",measuring_units.get(i));
                unit[0] =measuring_units.get(i);

                if(unit[0].equals("Kg")){

                    seekBar.setProgress(0);
                    value.setText("0.0 Kg");

                }else if(unit[0].equals("Pounds")){

                    seekBar.setProgress(0);
                    value.setText("0.0 Pounds");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        seekBar.setMax(200000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                goal_weight =i;

                if(unit[0].equals("Kg")){

                    double kg = i*0.001;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String val   =  df.format(kg);
                    goal_weight_new=val+" Kg";
                    value.setText(val+" Kg");

                }else if(unit[0].equals("Pounds")){

                    double meters= i*0.00220462;

                    DecimalFormat df = new DecimalFormat("#.##");
                    String val   =  df.format(meters);
                    goal_weight_new=val+" Pounds";
                    value.setText(val+" Pounds");

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






        dialog.show();


    }

    public void  showWaistDialog(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.height_dialog);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Inches");
        measuring_units.add("Centimeters");


        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Waist");
        final SeekBar seekBar = (SeekBar) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("waist value",waist_new);
                waist_tv.setText(waist_new);
                if (height==0 || waist==0 || weight==0 ){


                    if (height==0){
                        dialog.dismiss();
                        showHeightDialog();

                    }else if(weight==0){
                        dialog.dismiss();
                        showWeightDialog();
                    }else if(waist==0){
                        dialog.dismiss();
                        showWaistDialog();
                    }else if(goal_weight==0){
                        dialog.dismiss();
                        showGoal_WeightDialog();
                    }

                }else {

                    dialog.dismiss();
                }

            }
        });
        final String[] unit = {"Inches"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("item selected",measuring_units.get(i));
                unit[0] =measuring_units.get(i);


                if(unit[0].equals("Inches")){

                    seekBar.setProgress(0);
                    value.setText("0.0 Inches");

                }else if(unit[0].equals("Centimeters")){

                    seekBar.setProgress(0);
                    value.setText("0.0 Centimeters");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        seekBar.setMax(55);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                 waist = i;

                if(unit[0].equals("Inches")){


                    value.setText(i+" Inches");
                    waist_new=i+" Inches";

                }else if(unit[0].equals("Centimeters")){

                    double meters= i*2.54;

                    DecimalFormat df = new DecimalFormat("#.##");
                    String val   =  df.format(meters);
                    waist_new=val+" Centimeters";
                    value.setText(val+" Centimeters");

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






        dialog.show();


    }

    private void makeJsonObjReq() {

        showDialog();
        Map<String, String> postParam = new HashMap<String, String>();
        final SharedPreferences.Editor editor = login_details.edit();
        auth_key= login_details.getString("key","");
        user_id= login_details.getString("user_id","");
        Log.d("details", user_id + "//" + auth_key);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Config.url + "users/" + user_id + "/", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d("response", res.toString());

                        try {


                            SharedPreferences.Editor editor= login_details.edit();

                            dismissDialog();
                            JSONObject obj = res.getJSONObject("user");
                            String  username = obj.getString("username");
                            String  first_name = obj.getString("first_name");
                            String last_name = obj.getString("last_name");
                            String   email = obj.getString("email");
                            JSONObject profile = res.getJSONObject("profile");
                            String dob = profile.getString("dob");
                            String gender = profile.getString("gender");
                            String height = profile.getString("height");
                            String weight = profile.getString("weight");
                            String goal_weight = profile.getString("goal_weight");
                            String waist = profile.getString("waist");

                            editor.putString("user_username",username);
                            editor.putString("dob",dob);
                            editor.putInt("height",Integer.valueOf(height));
                            editor.putString("gender",gender);
                            editor.putString("user_first_name",first_name);
                            editor.putString("user_last_name",last_name);
                            editor.putInt("weight",Integer.valueOf(weight));
                            editor.putInt("goal_weight",Integer.valueOf(goal_weight));
                            editor.putInt("waist",Integer.valueOf(waist));
                            editor.commit();

                            int wei = Integer.valueOf(weight);
                            int grams = wei % 1000;
                            int kg = wei / 1000;
                            int hei = Integer.valueOf(height);
                            int fee = hei / 12;
                            int inv = wei % 1000;
//

//                            weight_tv.setText("" + kg + "." + grams);

                            try {

                                SharedPreferences.Editor editor1= login_details.edit();

//


                                JSONArray energy = res.getJSONArray("energy");
                                Log.d("energy details", energy.toString());

                                String[] energyi = new String[5];
                                for (int i = 0; i < energy.length(); i++) {

                                    energyi[i] = energy.get(i).toString();

                                    Log.d("energy val", energyi[i]);
                                }
                                editor1.putString("water_amount", energyi[1]);
                                editor1.putString("food_cal", energyi[2]);
                                editor1.putString("calorie_burnt", energyi[3]);
                                editor1.putString("total_calorie_required", energyi[4]);
                                Log.d("energy details", energyi[1] + "///" + energyi[2] + "///" + energyi[3] + "///" + energyi[4] + "///");

                                JSONObject user = res.getJSONObject("user");
                                String user_username = user.getString("username");
                                String user_first_name = user.getString("first_name");
                                String user_last_name = user.getString("last_name");
                                String user_email = user.getString("email");
                                email=user_email;
                                first_name=user_first_name;
                                last_name=user_last_name;

                                editor1.putString("user_username", user_username);
                                editor1.putString("user_first_name", user_first_name);
                                editor1.putString("user_last_name", user_last_name);
                                editor1.putString("user_email", user_email);


                                editor1.commit();


                                SharedPreferences.Editor editor2= login_details.edit();

                                try {
                                    Log.d("console",profile.get("images")+"/////");
                                    if(profile.get("images")==null){


                                    }else {
                                        JSONObject images = profile.getJSONObject("images");
                                        String master = images.getString("master");
                                        editor2.putString("master_image", master);
                                        new DownloadImage().execute(master);
                                        editor2.commit();
                                    }
                                }catch (Exception e){
                                    Log.d("Images error",e.toString()+"/////");
                                }

//
                                Log.d("all details", login_details.getAll().toString());
                                updateProfile();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                displayErrors(error);

                Log.d("error", error.toString());
            }
        }) {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", "JWT " + auth_key);
                return headers;
            }
//


        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            profileImage = result;
            profile_pic.setImageBitmap(profileImage);
            mProgressDialog.dismiss();
        }
    }


    TextView messageView;
    ProgressBar progressBar;
    android.app.AlertDialog alert;


    public void customDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }

    public void showDialog() {

        progressBar.setVisibility(View.VISIBLE);
        alert.show();
        messageView.setText("Loading");
    }

    public void dismissDialog() {
        alert.dismiss();
    }

    public void displayErrors(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Connection failed");
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("AuthFailureError");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ServerError");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }
    public void updateProfile() {

        showDialog();
     dob=   login_details.getString("dob","");
     gender=   login_details.getString("gender","");
     height=   login_details.getInt("height",0);
     weight=   login_details.getInt("weight",0);
     goal_weight=   login_details.getInt("goal_weight",0);
     waist=   login_details.getInt("waist",0);

        dob_tv.setText(dob);
//        if (gender.equals("M")){
//            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
//            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_f));
//        }else  if (gender.equals("F")){
//            male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_m));
//            female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
//        }



        if (height==0){
            height_tv.setText("Set Height");
        }else {
            int fee = height / 12;
            int inch = height%12;
            height_tv.setText(fee+"'"+inch+"''");

        }
        if (weight==0){
            weight_tv.setText("Set Weight");
        }else {
            int grams = weight % 1000;
            int kg = weight / 1000;
            weight_tv.setText(kg+"."+grams+" Kg");
        }
        if (goal_weight==0){
            goal_weight_tv.setText("Set Weight");
        }else {
            int grams = goal_weight % 1000;
            int kg = goal_weight / 1000;
            goal_weight_tv.setText(kg+"."+grams+" Kg");
        }
        if (waist==0){
            waist_tv.setText("Set Waist");
        }else {

            int inv = waist % 1000;
            waist_tv.setText(inv+" ''");
        }


        dismissDialog();





    }

}
