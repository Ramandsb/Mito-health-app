package in.tagbin.mitohealthapp;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.LocationModel;
import in.tagbin.mitohealthapp.service.GCMIntentService;

/**
 * Created by aasaqt on 25/8/16.
 */
public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    double latitude,longitude;
    GoogleApiClient googleApiClient;
    public static String LOGIN_DETAILS = "login_details";
    SharedPreferences loginDetails;
    private Location mLastLocation;
    PrefManager pref;
    private LocationRequest mLocationRequest;
    private static int SPLASH_TIME_OUT = 2000;
    final int REQUEST_LOCATION = 1;
    private static String[] PERMISSIONS_LOCATION = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        loginDetails = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);
        pref = new PrefManager(this);
        Intent service = new Intent(this, GCMIntentService.class);
        startService(service);
        if (hasLocationPermissionGranted()) {
            // you can do whatever you want
            getLocation();
        } else {
            requestLocationPermission();
        }

    }
    public void getLocation(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(2500);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().

                            status.startResolutionForResult(
                                    SplashActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:

                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        //          nextIntent();
                        LocationModel locationModel = new LocationModel();
                        locationModel.setLatitude(0.0);
                        locationModel.setLongitude(0.0);
                        pref.saveCurrentLocation(locationModel);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                nextIntent();
                            }
                        },SPLASH_TIME_OUT);
//                        Intent i = new Intent(this,CategoryActivity.class);
//                        i.putExtra("instart","instart");
//                        startActivity(i);
//                        finish();
                        break;
                }
                break;

        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
    }
    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        // Displaying the new location on UI
        displayLocation();
    }
    private void displayLocation() {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
            Log.d("location",latitude+"\n"+longitude);
            LocationModel locationModel = new LocationModel();
            locationModel.setLatitude(latitude);
            locationModel.setLongitude(longitude);
            pref.saveCurrentLocation(locationModel);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextIntent();
                }
            },SPLASH_TIME_OUT);
//            Intent i = new Intent(this,CategoryActivity.class);
//            i.putExtra("instart","instart");
//            startActivity(i);
//            finish();
            //lblLocation.setText(latitude + ", " + longitude);
            //locationModel.setLatitude(latitude);
            //locationModel.setLongitude(longitude);
            //pref.saveCurrentLocation(locationModel);
            //nextIntent();
        } else {

            //lblLocation
            //      .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }
    public boolean hasLocationPermissionGranted(){
        return  ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestLocationPermission() {
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS_LOCATION,
                    REQUEST_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    getLocation();
                } else if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        && ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //User has deny from permission dialog
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Location Permission Denied");
                    alertDialog1.setMessage("Hate2Wait uses this permission to detect your current location and show you nearby restaurants around you. Are you sure you want to deny this permission?");
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
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setMessage("It looks like you have turned off permission required for this feature. It can be enabled under Phone Settings > Apps > Hate2Wait > Permissions");
                    alertDialog1.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", SplashActivity.this.getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    alertDialog1.show();
                }
                break;
        }
    }
    public void nextIntent(){
        if (loginDetails.getString("key","").equals("")){
            startActivity(new Intent(SplashActivity.this,MainPage.class));
            finish();
        }else {
            startActivity(new Intent(SplashActivity.this,BinderActivity.class).putExtra("source","direct"));
            finish();
        }
    }
}
