package in.tagbin.mitohealthapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.ProfileImage.PicModeSelectDialogFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class ProfilePage extends Fragment implements PicModeSelectDialogFragment.IPicModeSelectListener{
    private int year, month, day;
    private DatePicker datePicker;
    private Calendar calendar;
    TextView dob_tv,height_tv,weight_tv,waist_tv;
    String feet_val,inches_val,unit_val;
    String gender;
    String url="";
    String name ="default";
    ImageView  profile_pic;
    TextView profile_name;
    SharedPreferences login_details;
    public static String user_id,auth_key;
    String dob="";
    int height,weight,waist;
    Button choose_image;
    public static String myurl="";
    public static Bitmap profileImage;
    //////////////////////

    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;

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
        Toolbar toolbar = (Toolbar) Fragview.findViewById(R.id.toolbar);

        choose_image = (Button) Fragview.findViewById(R.id.choose_image);
        login_details=getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, Context.MODE_PRIVATE);
        user_id= login_details.getString("user_id", "");
        auth_key= login_details.getString("auth_key","");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dob=year+"-"+month+"-"+day;
        profile_pic= (ImageView) Fragview.findViewById(R.id.profile_pic);
        profile_name= (TextView) Fragview.findViewById(R.id.profile_name);
        name=getActivity().getIntent().getStringExtra("name");


        if (getActivity().getIntent().hasExtra("picture")){
            url=getActivity().getIntent().getStringExtra("picture");
        }
        Log.d("check url",""+myurl);
        if (url.equals("")){
            if (profileImage == null){

                profile_pic.setImageDrawable(getResources().getDrawable(R.drawable.profile_tabicon));

            }else {
                profile_pic.setImageBitmap(profileImage);
            }

        }else{
            new DownloadImage().execute(url);
        }
        profile_name.setText(name);
//        Picasso.with(this).load(url).into(profile_pic);
        View select_date= Fragview.findViewById(R.id.select_date);
        View select_height= Fragview.findViewById(R.id.select_height);
        View select_weight= Fragview.findViewById(R.id.select_weight);
        View select_waist= Fragview.findViewById(R.id.select_waist);
        final View male_view= Fragview.findViewById(R.id.male_view);
        final View female_view= Fragview.findViewById(R.id.female_view);
        dob_tv= (TextView) Fragview.findViewById(R.id.dob);
        height_tv= (TextView) Fragview.findViewById(R.id.height_tv);
        weight_tv= (TextView) Fragview.findViewById(R.id.weight_tv);
        waist_tv= (TextView) Fragview.findViewById(R.id.waist_tv);




        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProfilePicDialog();
            }
        });
        checkPermissions();
        male_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_m));
                female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_f));
                gender="M";
            }
        });
        female_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.grey_m));
                female_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_));
                gender="F";
            }
        });
        assert select_date != null;
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().showDialog(999);
            }
        });
        select_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog("weight");
            }
        });
        select_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog("height");
            }
        });
        select_waist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog("waist");
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
            profileImage=myBitmap;
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

    public void WheelDialog(String source){
        String[] feets=null,inches=null,unit=null;
        if (source.equals("weight")){
            feets = new String[101];
            inches = new String[11];
            unit = new String[]{"Kg"};
            for (int i= 0;i<=100;i++){
                feets[i]=""+(i+35);
            }
            for (int i= 0;i<=9;i++){
                inches[i]=""+(i+1);
            }

            feet_val="38";
            inches_val="4";
            unit_val="Kg";


        }else  if (source.equals("height")){
            feets = new String[]{"3","4","5","6","7","8"};
            inches = new String[]{"1","2","3","4","5","6","7","8","9","10","11"};
            unit = new String[]{"feets"};
            feet_val="6";
            inches_val="4";
            unit_val="feets";
        }else if (source.equals("waist")){

         inches = new String[]{"24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43","44","45"};
           unit = new String[]{"inches"};

            inches_val="27";
            unit_val="inches";

        }




        View outerView = View.inflate(getActivity(), R.layout.wheel_view, null);
        if (source.equals("waist")){


        }else {
            WheelView wv1 = (WheelView) outerView.findViewById(R.id.wheel_view_wv1);
            wv1.setOffset(2);
            wv1.setVisibility(View.VISIBLE);
            wv1.setItems(Arrays.asList(feets));
            wv1.setSeletion(3);
            wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("feet", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    feet_val=item;
                }
            });
        }
        WheelView wv2 = (WheelView) outerView.findViewById(R.id.wheel_view_wv2);
        wv2.setOffset(2);
        wv2.setVisibility(View.VISIBLE);
        wv2.setItems(Arrays.asList(inches));
        wv2.setSeletion(3);
        wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("inches", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);

                inches_val = item;

            }
        });
        WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv3);
        wv.setOffset(2);
        wv.setItems(Arrays.asList(unit));
        wv.setSeletion(3);
        wv.setVisibility(View.VISIBLE);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d("Tag", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);

                unit_val = item;
            }
        });

        if (source.equals("weight")){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Weight Tracker")
                    .setView(outerView)
                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        phyEd.setText(""+Sfeet+"."+Sinches+"  feets");
                            weight_tv.setText(feet_val+"."+inches_val+" "+unit_val);
                             weight= (Integer.valueOf(feet_val)*1000)+Integer.valueOf(inches_val);


                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }else   if (source.equals("height")){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Height Tracker")
                    .setView(outerView)
                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        height_tv.setText(feet_val+"'"+inches_val+"''");
                             height= (Integer.valueOf(feet_val)*12)+Integer.valueOf(inches_val);


                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }else   if (source.equals("waist")){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Waist Tracker")
                    .setView(outerView)
                    .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                        phyEd.setText(""+Sfeet+"."+Sinches+"  feets");
                            waist_tv.setText(inches_val+"''");
                            waist=Integer.valueOf(inches_val);

                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }


    }

    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(getActivity(), myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            dob_tv.setText(day+" "+month+" "+year);
             dob=year+"-"+month+"-"+day;

        }
    };



    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
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

            profileImage=result;
            profile_pic.setImageBitmap(profileImage);
            mProgressDialog.dismiss();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {

            makeJsonObjReq(name,gender,dob,String.valueOf(height),String.valueOf(waist),String.valueOf(weight));
            startActivity(new Intent(getActivity(),HomePage.class));
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void makeJsonObjReq(String name,String sex,String dob,String height,String waist,String weight) {

        Map<String, String> postParam = new HashMap<String, String>();

        /**
         * "username": "nairitya",
         "password": "nkman",
         "first_name": "Nairitya",
         "last_name": "Khilari",
         "email": "nairitya@gmail.com",
         "phone_number": "8778787878"
         */


        postParam.put("gender", sex);
        postParam.put("dob", dob);
        postParam.put("height", height);
        postParam.put("waist", waist);
        postParam.put("weight", weight);


        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                Config.url+"users/"+user_id+"/",  new JSONObject(postParam),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());


                Log.d("error", error.toString());
            }
        }) {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                headers.put("Authorization","JWT "+auth_key);
                return headers;
            }
//


        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
