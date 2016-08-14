package in.tagbin.mitohealthapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.CreateEventSendModel;

/**
 * Created by aasaqt on 9/8/16.
 */
public class AddActivityfrag extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    ImageView addImage;
    EditText title,description,location,type;
    TextView time;
    Button createActivity;
    RangeBar rangeBar;
    FloatingActionButton fabAddImage;
    String editType,editTitle,editDescription,editLocation;
    RelativeLayout relativeTime;
    private static int SELECT_PICTURE = 1;
    final int REQUEST_LOCATION = 2;
    ProgressBar progressBar;
    int hour,minute,year,month,day,memberLowerValue,memberMaxValue;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.addactivity,container,false);
        addImage = (ImageView) layout.findViewById(R.id.ivAddActivityImage);
        title = (EditText) layout.findViewById(R.id.etAddTitle);
        description = (EditText) layout.findViewById(R.id.etAddDesciption);
        location = (EditText) layout.findViewById(R.id.etAddLocation);
        type = (EditText) layout.findViewById(R.id.etAddType);
        time = (TextView) layout.findViewById(R.id.tvAddDecisionTime);
        createActivity = (Button) layout.findViewById(R.id.buttonCreateActivity);
        relativeTime = (RelativeLayout) layout.findViewById(R.id.relativeAddDecisionTimer);
        rangeBar = (RangeBar) layout.findViewById(R.id.rangebar);
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        fabAddImage = (FloatingActionButton) layout.findViewById(R.id.fabAddImage);
        createActivity.setOnClickListener(this);
        fabAddImage.setOnClickListener(this);
        relativeTime.setOnClickListener(this);
        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                memberLowerValue = Integer.parseInt(leftPinValue);
                memberMaxValue = Integer.parseInt(rightPinValue);
            }
        });
        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCreateActivity:
                editType = type.getText().toString();
                editTitle = title.getText().toString();
                editDescription = description.getText().toString();
                editLocation = location.getText().toString();
                CreateEventSendModel createEventSendModel = new CreateEventSendModel();
                createEventSendModel.title = editTitle;
                createEventSendModel.description = editDescription;
                createEventSendModel.type = editType;
                createEventSendModel.capacity = memberMaxValue - memberLowerValue;
                Date date1 = new Date(year - 1900, month, day, hour, minute);
                long output = date1.getTime() / 1000L;
                createEventSendModel.timer = String.valueOf(output);
                createEventSendModel.event_type = "1";
                Log.d("createventmodel", JsonUtils.jsonify(createEventSendModel));
                progressBar.setVisibility(View.VISIBLE);
                Controller.createEvent(getContext(),createEventSendModel,mCreateListener);

                break;
            case R.id.fabAddImage:
                if (hasLocationPermissionGranted()) {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_PICTURE);
                }else{
                    requestLocationPermission();
                }
                break;
            case R.id.relativeAddDecisionTimer:
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this, year, month, day);
                dpd.show(getActivity().getFragmentManager(), "DATE_PICKER_TAG");

                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        updateTime(hourOfDay, minute);
    }
    private void updateTime(int hours, int mins) {
        if (mins >= 60) {
            hour = hours + 1;
            hours = hours + 1;
            minute = mins - 60;
            mins = mins - 60;
        } else {
            hour = hours;
            minute = mins;
        }

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minute).append(" ").append(timeSet).toString();

        time.setText(aTime);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE ) {
            if (data != null) {
                Uri uri = data.getData();
                File myFile = new File(uri.getPath());
                Uri selectedImage=getImageContentUri(getContext(),myFile);
                try {
                   Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),selectedImage);
                    addImage.setImageBitmap(mBitmap);
                }catch(IOException ex) {
                    //Do something witht the exception
                }

            } else {
                Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                        .show();
            }

        }


    }
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }}
    public boolean hasLocationPermissionGranted(){
        return  ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission() {
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LOCATION,
                    REQUEST_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    //Storage permission is enabled
                    Intent i = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_PICTURE);

                } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //User has deny from permission dialog
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Location Permission Denied");
                    alertDialog1.setMessage("Are you sure you want to deny this permission?");
                    alertDialog1.setPositiveButton("I'M SURE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocationPermission();
                        }
                    });
                    alertDialog1.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            requestLocationPermission();
                        }
                    });
                    alertDialog1.show();
                } else {
                    // User has deny permission and checked never show permission dialog so you can redirect to Application settings page
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getActivity(),R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setMessage("It looks like you have turned off permission required for this feature. It can be enabled under Phone Settings > Apps > MitoHealthApp > Permissions");
                    alertDialog1.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    alertDialog1.show();
                }
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(this, 0, 0, false);
        tpd.show(getActivity().getFragmentManager(), "TIME_PICKER_TAG");
    }
    RequestListener mCreateListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("Event Created",responseObject.toString());
            /*DataObject dataObject = JsonUtils.objectify(responseObject.toString(),DataObject.class);
            dataObject.all = false;
            Bundle bundle = new Bundle();
            Fragment fragment = new MyActivityCardfrag();
            String dataobject = JsonUtils.jsonify(dataObject);
            bundle.putString("dataobject",dataobject);
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frameAddActivity, fragment);
            transaction.addToBackStack(null);
            transaction.commit();*/
            getFragmentManager().popBackStack();
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("event created error",message);
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Event creation error",Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}
