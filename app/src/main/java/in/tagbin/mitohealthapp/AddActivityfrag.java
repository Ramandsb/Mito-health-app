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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.ProfileImage.PicModeSelectDialogFragment;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PlaceAutoCompleteAdapter;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.CreateEventSendModel;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.EventTypeModel;
import in.tagbin.mitohealthapp.model.FileUploadModel;
import in.tagbin.mitohealthapp.model.ImageUploadResponseModel;
import in.tagbin.mitohealthapp.model.InterestModel;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by aasaqt on 9/8/16.
 */
public class AddActivityfrag extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    ImageView addImage;
    EditText title,description,location;
    AutoCompleteTextView type;
    TextView time,memberValue,activityDate,activityTime;
    Button createActivity;
    SeekBar rangeBar;
    FloatingActionButton fabAddImage;
    String editType,editTitle,editDescription,editLocation,response;
    RelativeLayout relativeTime,relativeDate;
    private static int SELECT_PICTURE = 1;
    final int REQUEST_LOCATION = 2;
    GifImageView progressBar;
    PrefManager pref;
    DataObject dataObject;
    PlaceAutoCompleteAdapter adapter;
    CountDownTimer countDownTimer;
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    int hour,minute,year,month,day,hour1,minute1,year1,month1,day1,memberValueFinal;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.addactivity,container,false);
        addImage = (ImageView) layout.findViewById(R.id.ivAddActivityImage);
        title = (EditText) layout.findViewById(R.id.etAddTitle);
        description = (EditText) layout.findViewById(R.id.etAddDesciption);
        location = (EditText) layout.findViewById(R.id.etAddLocation);
        type = (AutoCompleteTextView) layout.findViewById(R.id.etAddType);
        time = (TextView) layout.findViewById(R.id.tvAddDecisionTime);
        createActivity = (Button) layout.findViewById(R.id.buttonCreateActivity);
        relativeTime = (RelativeLayout) layout.findViewById(R.id.relativeAddDecisionTimer);
        relativeDate = (RelativeLayout) layout.findViewById(R.id.relativeTime);
        rangeBar = (SeekBar) layout.findViewById(R.id.rangebar);
        progressBar = (GifImageView) layout.findViewById(R.id.progressBar);
        fabAddImage = (FloatingActionButton) layout.findViewById(R.id.fabAddImage);
        activityDate = (TextView) layout.findViewById(R.id.tvAddActivityDate);
        activityTime = (TextView) layout.findViewById(R.id.tvAddActivitytime);
        memberValue = (TextView) layout.findViewById(R.id.tvAddMemberValue);
        createActivity.setOnClickListener(this);
        fabAddImage.setOnClickListener(this);
        relativeTime.setOnClickListener(this);
        relativeDate.setOnClickListener(this);


        rangeBar.setProgress(0);
        pref = new PrefManager(getActivity());
        rangeBar.incrementProgressBy(1);
        rangeBar.setMax(100);
        rangeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                memberValue.setText(String.valueOf(i));
                memberValueFinal = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (getArguments() != null){
            response = getArguments().getString("response");
            dataObject = JsonUtils.objectify(response,DataObject.class);
            title.setText(dataObject.getTitle());
            type.setText(dataObject.getEvent_type().getTitle());
            description.setText(dataObject.getDescription());
            rangeBar.setProgress(dataObject.getCapacity());
            if (dataObject.getEvent_time() != null) {
                activityDate.setText(MyUtils.getValidDate(dataObject.getEvent_time()));
                activityTime.setText(MyUtils.getValidTime(dataObject.getEvent_time()));
            }
            if (dataObject.getPicture() != null){
                ImageLoader.getInstance().loadImage(dataObject.getPicture(), new ImageLoadingListener() {
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
                        addImage.setImageBitmap(loadedImage);
                    }
                });
                long endTime = MyUtils.getTimeinMillis(dataObject.getTime());
                long currentTime = System.currentTimeMillis();
                countDownTimer = new CountDownTimer(endTime - currentTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long ms = millisUntilFinished;
                        StringBuffer text = new StringBuffer("");
                        if (ms > DAY) {
                            text.append(ms / DAY).append("d ");
                            ms %= DAY;
                        }
                        if (ms > HOUR) {
                            text.append(ms / HOUR).append("hr ");
                            ms %= HOUR;
                        }
                        if (ms > MINUTE) {
                            text.append(ms / MINUTE).append("min ");
                            ms %= MINUTE;
                        }
                        time.setText(text.toString()+"left");
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                countDownTimer.start();
            }else {
                addImage.setImageResource(R.drawable.hotel);
            }
            createActivity.setText("Update Activity");
            createActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editType = type.getText().toString();
                    editTitle = title.getText().toString();
                    editDescription = description.getText().toString();
                    editLocation = location.getText().toString();
                    CreateEventSendModel createEventSendModel = new CreateEventSendModel();
                    if (editTitle != dataObject.getTitle())
                        createEventSendModel.title = editTitle;
                    if (editDescription != dataObject.getDescription())
                        createEventSendModel.description = editDescription;
                    if (editType != dataObject.getEvent_type().getTitle())
                        createEventSendModel.type = editType;
                    if (memberValueFinal != dataObject.getCapacity())
                        createEventSendModel.capacity = memberValueFinal;
                    Date date1 = new Date(year - 1900, month, day, hour, minute);
                    long output = date1.getTime() / 1000L;
                    Date date2 =new Date(year1-1900,month1,day1,hour1,minute1);
                    long output1 = date2.getTime()/1000L;
                    if (pref.getKeyMasterCreate() != null){
                        createEventSendModel.picture = pref.getKeyMasterCreate();
                    }
                    createEventSendModel.event_time = output1;
                    createEventSendModel.timer = String.valueOf(output);
                    createEventSendModel.event_type = "1";
                    Log.d("createventmodel", JsonUtils.jsonify(createEventSendModel));
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.updateEvent(getActivity(),createEventSendModel,dataObject.getId(),mUpdateListener);
                }
            });
        }
        type.setOnItemClickListener(mAutocompleteClickListener);
        adapter = new PlaceAutoCompleteAdapter(getContext(), android.R.layout.simple_list_item_1);
        type.setAdapter(adapter);
        return layout;
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EventTypeModel.InterestListModel interestListModel = adapter.getItem(position);
            type.setText(interestListModel.getTitle());
        }
    };
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
                createEventSendModel.capacity = memberValueFinal;
                Date date1 = new Date(year - 1900, month, day, hour, minute);
                long output = date1.getTime() / 1000L;
                Date date2 =new Date(year1-1900,month1,day1,hour1,minute1);
                long output1 = date2.getTime()/1000L;
                if (pref.getKeyMasterCreate() != null){
                    createEventSendModel.picture = pref.getKeyMasterCreate();
                }
                createEventSendModel.event_time = output1;
                createEventSendModel.timer = String.valueOf(output);
                createEventSendModel.event_type = "1";
                Log.d("createventmodel", JsonUtils.jsonify(createEventSendModel));
                progressBar.setVisibility(View.VISIBLE);
                Controller.createEvent(getContext(),createEventSendModel,mCreateListener);

                break;
            case R.id.fabAddImage:
                showAddProfilePicDialog1();
                break;
            case R.id.relativeAddDecisionTimer:
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this, year, month, day);
                dpd.show(getActivity().getFragmentManager(), "DATE_PICKER_TAG");
                break;
            case R.id.relativeTime:
                Calendar calendar1 = Calendar.getInstance();
                year1 = calendar1.get(Calendar.YEAR);
                month1 = calendar1.get(Calendar.MONTH);
                day1 = calendar1.get(Calendar.DAY_OF_MONTH);
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd1 = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        year1 = year;
                        month1 = monthOfYear;
                        day1 = dayOfMonth;
                        com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                               updateTime1(hourOfDay,minute);
                            }
                        }, 0, 0, false);
                        tpd.show(getActivity().getFragmentManager(), "TIME_PICKER_TAG");
                    }
                }, year1, month1, day1);
                dpd1.show(getActivity().getFragmentManager(), "DATE_PICKER_TAG");
                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if (countDownTimer != null)
            countDownTimer.cancel();
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
        Date date = new Date(year-1900,month,day,hour,minute);
        long endTime = date.getTime();
        long currentTime = System.currentTimeMillis();
        countDownTimer = new CountDownTimer(endTime - currentTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long ms = millisUntilFinished;
                StringBuffer text = new StringBuffer("");
                if (ms > DAY) {
                    text.append(ms / DAY).append("d ");
                    ms %= DAY;
                }
                if (ms > HOUR) {
                    text.append(ms / HOUR).append("hr ");
                    ms %= HOUR;
                }
                if (ms > MINUTE) {
                    text.append(ms / MINUTE).append("min ");
                    ms %= MINUTE;
                }
                time.setText(text.toString()+"left");
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
        //time.setText(aTime);
    }
    private void updateTime1(int hours, int mins) {
        if (mins >= 60) {
            hour1 = hours + 1;
            hours = hours + 1;
            minute1 = mins - 60;
            mins = mins - 60;
        } else {
            hour1 = hours;
            minute1 = mins;
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
                .append(minute1).append(" ").append(timeSet).toString();
        Date date = new Date(year1-1900,month1,day1,hour1,minute1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String value = dateFormat.format(date);
        activityDate.setText(value);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a");
        String value1 = dateFormat1.format(date);
        activityTime.setText(value1);

        //time.setText(aTime);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE ) {
            if (resultCode == RESULT_OK) {
                String imagePath = data.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener);
                addImage.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = data.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }


    }
    private void showAddProfilePicDialog1() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(new PicModeSelectDialogFragment.IPicModeSelectListener() {
            @Override
            public void onPicModeSelected(String mode) {
                String action = mode.equalsIgnoreCase(GOTOConstants.PicModes.CAMERA) ? GOTOConstants.IntentExtras.ACTION_CAMERA : GOTOConstants.IntentExtras.ACTION_GALLERY;
                Intent intent = new Intent(getActivity(), ImageCropActivity.class);
                intent.putExtra("ACTION", action);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
        dialogFragment.show(getActivity().getFragmentManager(), "picModeSelector");
    }
    private Bitmap showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            return myBitmap;

        }
        return null;
    }
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
            final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message,ErrorResponseModel.class);
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),errorResponseModel.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    RequestListener mUpdateListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("Event Updated",responseObject.toString());
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
            Log.d("event updated error",message);
            final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message,ErrorResponseModel.class);
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),errorResponseModel.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    RequestListener mUploadListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyMasterCreate(imageUploadResponseModel.getUrl());
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message,ErrorResponseModel.class);
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),errorResponseModel.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}
