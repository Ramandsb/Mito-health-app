package in.tagbin.mitohealthapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.helper.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.helper.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.helper.ProfileImage.PicModeSelectDialogFragment;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.adapter.EventTypeSearchAdapter;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.CreateEventSendModel;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.EventTypeModel;
import in.tagbin.mitohealthapp.model.FileUploadModel;
import in.tagbin.mitohealthapp.model.ImageUploadResponseModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 9/8/16.
 */
public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    ImageView addImage;
    EditText title,description,location,type;
    TextView time,memberValue,activityDate,activityTime,coins;
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
    CountDownTimer countDownTimer;
    Toolbar toolbar;
    boolean decesionTimer = false,eventDate = false;
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    double latitude,longitude;
    int hour,minute,year,month,day,hour1,minute1,year1,month1,day1,memberValueFinal,event_type = 0,coinsFinal;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_create_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addImage = (ImageView) findViewById(R.id.ivAddActivityImage);
        title = (EditText) findViewById(R.id.etAddTitle);
        description = (EditText) findViewById(R.id.etAddDesciption);
        location = (EditText) findViewById(R.id.etAddLocation);
        type = (EditText) findViewById(R.id.etAddType);
        time = (TextView) findViewById(R.id.tvAddDecisionTime);
        createActivity = (Button) findViewById(R.id.buttonCreateActivity);
        relativeTime = (RelativeLayout) findViewById(R.id.relativeAddDecisionTimer);
        relativeDate = (RelativeLayout) findViewById(R.id.relativeTime);
        rangeBar = (SeekBar) findViewById(R.id.rangebar);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        fabAddImage = (FloatingActionButton) findViewById(R.id.fabAddImage);
        activityDate = (TextView) findViewById(R.id.tvAddActivityDate);
        activityTime = (TextView) findViewById(R.id.tvAddActivitytime);
        memberValue = (TextView) findViewById(R.id.tvAddMemberValue);
        createActivity.setOnClickListener(this);
        fabAddImage.setOnClickListener(this);
        relativeTime.setOnClickListener(this);
        relativeDate.setOnClickListener(this);
        type.setOnClickListener(this);
        location.setOnClickListener(this);
        rangeBar.setProgress(0);
        pref = new PrefManager(this);
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

        Calendar calendar = Calendar.getInstance();
        year1 = calendar.get(Calendar.YEAR);
        month1 = calendar.get(Calendar.MONTH);
        day1 = calendar.get(Calendar.DAY_OF_MONTH);
        hour1 = calendar.get(Calendar.HOUR_OF_DAY);
        minute1 = calendar.get(Calendar.MINUTE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        updateTime(hour,minute);
        updateTime1(hour1,minute1);
        if (pref.getCurrentLocationAsObject() != null){
            if (pref.getCurrentLocationAsObject().getLongitude() != 0.0 && pref.getCurrentLocationAsObject().getLatitude() != 0.0){
                location.setText(MyUtils.getCityNameFromLatLng(CreateEventActivity.this,pref.getCurrentLocationAsObject().getLatitude(),pref.getCurrentLocationAsObject().getLongitude()));
            }
        }
        if (getIntent().getStringExtra("response") != null){
            response = getIntent().getStringExtra("response");
            getSupportActionBar().setTitle("Edit Event");
            Log.d("edit event",response);
            dataObject = JsonUtils.objectify(response,DataObject.class);
            title.setText(dataObject.getTitle());
            type.setText(dataObject.getEvent_type().getTitle());
            description.setText(dataObject.getDescription());
            location.setText(MyUtils.getCityName(CreateEventActivity.this,dataObject.getLocation()));
            latitude = MyUtils.getLatitude(CreateEventActivity.this,dataObject.getLocation());
            longitude = MyUtils.getLongitude(CreateEventActivity.this,dataObject.getLocation());
            memberValueFinal = dataObject.getCapacity();
            rangeBar.setProgress(dataObject.getCapacity());
            if (dataObject.getEvent_time() != null) {
                year1 = Integer.parseInt(MyUtils.getYear(dataObject.getEvent_time()));
                month1 = Integer.parseInt(MyUtils.getMonth(dataObject.getEvent_time()))-1;
                day1 = Integer.parseInt(MyUtils.getDay(dataObject.getEvent_time()));
                hour1 = Integer.parseInt(MyUtils.getHour(dataObject.getEvent_time()));
                minute1 = Integer.parseInt(MyUtils.getMinute(dataObject.getEvent_time()));
                activityDate.setText(MyUtils.getValidDate1(dataObject.getEvent_time()));
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
            }else {
                addImage.setImageResource(R.drawable.hotel);
            }
            long endTime = MyUtils.getTimeinMillis(dataObject.getTime());
            year = Integer.parseInt(MyUtils.getYear(dataObject.getTime()));
            month = Integer.parseInt(MyUtils.getMonth(dataObject.getTime()))-1;
            day = Integer.parseInt(MyUtils.getDay(dataObject.getTime()));
            hour = Integer.parseInt(MyUtils.getHour(dataObject.getTime()));
            minute = Integer.parseInt(MyUtils.getMinute(dataObject.getTime()));
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
                    time.setText(text.toString());
                }

                @Override
                public void onFinish() {

                }
            };
            countDownTimer.start();
            createActivity.setText("Update Event");
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
                    createEventSendModel.capacity = memberValueFinal;
//                    if (pref.getCurrentLocationAsObject() != null){
//                        if (pref.getCurrentLocationAsObject().getLongitude() != 0.0 && pref.getCurrentLocationAsObject().getLatitude() != 0.0){
//                            latitude = pref.getCurrentLocationAsObject().getLatitude();
//                            longitude = pref.getCurrentLocationAsObject().getLongitude();
//                            double[] location = {longitude,latitude};
//                            createEventSendModel.setLocation(location);
//                        }
//                    }
                    double[] location = {longitude,latitude};
                    createEventSendModel.setLocation(location);
                    Date date1 = new Date(year - 1900, month, day, hour, minute);
                    long output = date1.getTime() / 1000L;
                    Date date2 =new Date(year1-1900,month1,day1,hour1,minute1);
                    long output1 = date2.getTime()/1000L;
                    if (pref.getKeyMasterCreate() != null){
                        createEventSendModel.picture = pref.getKeyMasterCreate();
                    }
                    createEventSendModel.event_time = output1;
                    createEventSendModel.timer = String.valueOf(output);
                    event_type = dataObject.getEvent_type().getId();
                    createEventSendModel.event_type = event_type;
                    Log.d("createventmodel", JsonUtils.jsonify(createEventSendModel));
                    if (output > output1 ){
                        Toast.makeText(CreateEventActivity.this,"Please enter the valid decession timer",Toast.LENGTH_LONG).show();
                    }else if (memberValueFinal == 0){
                        Toast.makeText(CreateEventActivity.this,"Please enter the capacity above 0",Toast.LENGTH_LONG).show();
                    }else if (latitude == 0 || longitude == 0){
                        Toast.makeText(CreateEventActivity.this,"Please click on location to enter location",Toast.LENGTH_LONG).show();
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        Controller.updateEvent(CreateEventActivity.this, createEventSendModel, dataObject.getId(), mUpdateListener);
                    }
                }
            });

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
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
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        PrefManager pref = new PrefManager(this);
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
        return super.onCreateOptionsMenu(menu);
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
                createEventSendModel.event_type = event_type;
                createEventSendModel.capacity = memberValueFinal;
//                if (pref.getCurrentLocationAsObject() != null){
//                    if (pref.getCurrentLocationAsObject().getLongitude() != 0.0 && pref.getCurrentLocationAsObject().getLatitude() != 0.0){
//                        latitude = pref.getCurrentLocationAsObject().getLatitude();
//                        longitude = pref.getCurrentLocationAsObject().getLongitude();
//                        double[] location = {longitude,latitude};
//                        createEventSendModel.setLocation(location);
//                    }
//                }
                double[] location = {longitude,latitude};
                createEventSendModel.setLocation(location);
                Date date1 = new Date(year - 1900, month, day, hour, minute);
                long output = date1.getTime() / 1000L;
                Date date2 =new Date(year1-1900,month1,day1,hour1,minute1);
                long output1 = date2.getTime()/1000L;

                if (pref.getKeyMasterCreate() != null){
                    createEventSendModel.picture = pref.getKeyMasterCreate();
                }
                createEventSendModel.event_time = output1;
                createEventSendModel.timer = String.valueOf(output);
                Log.d("createventmodel", JsonUtils.jsonify(createEventSendModel));
                if (output > output1 ){
                    Toast.makeText(CreateEventActivity.this,"Please enter the valid decission timer",Toast.LENGTH_LONG).show();
                }else if (memberValueFinal == 0){
                    Toast.makeText(CreateEventActivity.this,"Please enter the capacity above 0",Toast.LENGTH_LONG).show();
                }else if (!eventDate){
                    Toast.makeText(CreateEventActivity.this,"Please enter the event date and time",Toast.LENGTH_LONG).show();
                }else if (!decesionTimer){
                    Toast.makeText(CreateEventActivity.this,"Please enter the decession timer of the event",Toast.LENGTH_LONG).show();
                }else if(event_type == 0){
                    Toast.makeText(CreateEventActivity.this,"Please click on event type to search event type",Toast.LENGTH_LONG).show();
                }else if (latitude == 0 || longitude == 0){
                    Toast.makeText(CreateEventActivity.this,"Please click on location to enter location",Toast.LENGTH_LONG).show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.createEvent(CreateEventActivity.this, createEventSendModel, mCreateListener);
                }

                break;
            case R.id.fabAddImage:
                showAddProfilePicDialog1();
                break;
            case R.id.relativeAddDecisionTimer:
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                Calendar[] dates = new Calendar[31];
                int i = 0;
                while (i < 31) {
                    Calendar selDate = Calendar.getInstance();
                    selDate.add(Calendar.DAY_OF_MONTH, i);
                    dates[i] = selDate;
                    i++;
                }
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this, year, month, day);
                dpd.setHighlightedDays(dates);
                dpd.setSelectableDays(dates);
                dpd.show(getFragmentManager(), "DATE_PICKER_TAG");
                break;
            case R.id.relativeTime:
                Calendar calendar1 = Calendar.getInstance();
                year1 = calendar1.get(Calendar.YEAR);
                month1 = calendar1.get(Calendar.MONTH);
                day1 = calendar1.get(Calendar.DAY_OF_MONTH);
                Calendar[] dates1 = new Calendar[31];
                int i1 = 0;
                while (i1 < 31) {
                    Calendar selDate = Calendar.getInstance();
                    selDate.add(Calendar.DAY_OF_MONTH, i1);
                    dates1[i1] = selDate;
                    i1++;
                }
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd1 = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        year1 = year;
                        month1 = monthOfYear;
                        day1 = dayOfMonth;
                        com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                               eventDate = true;
                                updateTime1(hourOfDay,minute);
                            }
                        }, 0, 0, false);
                        tpd.show(getFragmentManager(), "TIME_PICKER_TAG");
                    }
                }, year1, month1, day1);
                dpd1.setSelectableDays(dates1);
                dpd1.setHighlightedDays(dates1);
                dpd1.show(getFragmentManager(), "DATE_PICKER_TAG");
                break;
            case R.id.etAddType:
                Intent p = new Intent(this,EventTypeSearchActivity.class);
                startActivity(p);
                break;
            case R.id.etAddLocation:
                Intent l = new Intent(this,LocationSearchActivity.class);
                startActivity(l);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("new intent","new i   ntent");
        if (intent.getStringExtra("eventtitle") != null) {
            String name = intent.getStringExtra("eventtitle");
            int id = intent.getIntExtra("eventid", 0);
            type.setText(name);
            event_type = id;
        }else if (intent.getStringExtra("placename") != null){
            String name = intent.getStringExtra("placename");
            latitude = intent.getDoubleExtra("latitude",0);
            longitude = intent.getDoubleExtra("longitude",0);
            location.setText(name);
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if (countDownTimer != null)
            countDownTimer.cancel();
        decesionTimer = true;
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
                time.setText(text.toString()+"item_chat_activity");
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
                Controller.upoadPhot(CreateEventActivity.this,fileUploadModel,mUploadListener);
                addImage.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = data.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(CreateEventActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }


    }
    private void showAddProfilePicDialog1() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(new PicModeSelectDialogFragment.IPicModeSelectListener() {
            @Override
            public void onPicModeSelected(String mode) {
                String action = mode.equalsIgnoreCase(GOTOConstants.PicModes.CAMERA) ? GOTOConstants.IntentExtras.ACTION_CAMERA : GOTOConstants.IntentExtras.ACTION_GALLERY;
                Intent intent = new Intent(CreateEventActivity.this, ImageCropActivity.class);
                intent.putExtra("ACTION", action);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
        dialogFragment.show(getFragmentManager(), "picModeSelector");
    }
    private Bitmap showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            return myBitmap;

        }
        return null;
    }
    public boolean hasLocationPermissionGranted(){
        return  ContextCompat.checkSelfPermission(CreateEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission() {
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(CreateEventActivity.this, PERMISSIONS_LOCATION,
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

                } else if (ActivityCompat.shouldShowRequestPermissionRationale(CreateEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //User has deny from permission dialog
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(CreateEventActivity.this,R.style.AppCompatAlertDialogStyle);
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
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(CreateEventActivity.this,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setMessage("It looks like you have turned off permission required for this feature. It can be enabled under Phone Settings > Apps > MitoHealthApp > Permissions");
                    alertDialog1.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
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
        tpd.show(getFragmentManager(), "TIME_PICKER_TAG");
    }
    RequestListener mCreateListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("Event Created",responseObject.toString());
            Intent intent = new Intent(CreateEventActivity.this, BinderActivity.class);
            intent.putExtra("selection", 1);
            intent.putExtra("activity_create_event","activity_create_event");
            startActivity(intent);
            //getFragmentManager().popBackStack();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("event created error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateEventActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateEventActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUpdateListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("Event Updated",responseObject.toString());
//            Fragment fragment = new Lookupfragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("activity_create_event","activity_create_event");
//            fragment.setArguments(bundle);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.add(R.id.frameAddActivity, fragment);
//            transaction.commit();
            Intent intent = new Intent(CreateEventActivity.this, BinderActivity.class);
            intent.putExtra("selection", 1);
            intent.putExtra("activity_create_event","activity_create_event");
            startActivity(intent);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("event updated error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateEventActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateEventActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
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
            //final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message,ErrorResponseModel.class);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateEventActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateEventActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
