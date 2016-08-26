package in.tagbin.mitohealthapp;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import java.util.Timer;

import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.LocationModel;

/**
 * Created by aasaqt on 25/8/16.
 */
public class SplashActiity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    double latitude,longitude;
    GoogleApiClient googleApiClient;
    private Location mLastLocation;
    PrefManager pref;
    private LocationRequest mLocationRequest;
    private final static String TAG = SplashActiity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                                    SplashActiity.this, 1000);
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
            LocationModel locationModel = new LocationModel();
            locationModel.setLatitude(latitude);
            locationModel.setLongitude(longitude);
            pref.saveCurrentLocation(locationModel);
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
}
