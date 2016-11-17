package in.tagbin.mitohealthapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.adapter.EventTypeSearchAdapter;
import in.tagbin.mitohealthapp.adapter.LocationAdapter;
import in.tagbin.mitohealthapp.helper.CustomAutoCompleteTextView;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.EventTypeModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 12/10/16.
 */

public class LocationSearchActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    CustomAutoCompleteTextView auto_tv;
    TextView suggestExercise;
    GifImageView progressBar;
    PrefManager pref;
    Double lat, lon;
    String placeName;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(23.0, 72.0), new LatLng(32.733333, 74.9));
    protected GoogleApiClient mGoogleApiClient;
    private LocationAdapter adapter1;
    int day,month,year,hour,minute;
    public static String TAG = "LocationActivity";
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);
            LatLng latlng = place.getLatLng();
            lat = latlng.latitude;
            lon = latlng.longitude;
            place.getName();
            placeName = (String) place.getName();
            Intent i = new Intent(LocationSearchActivity.this,CreateEventActivity.class);
            i.putExtra("latitude",lat);
            i.putExtra("longitude",lon);
            i.putExtra("placename",placeName);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            //Controller.getFlats(LocationActivity.this, lat, lon, LocationActivity.this);

            Log.i(TAG, "Place details received: " + place.getName());
            places.release();
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final LocationAdapter.PlaceAutocomplete item = adapter1.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Autocomplete item selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);
        pref = new PrefManager(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        suggestExercise = (TextView) findViewById(R.id.tvSuggestFood);
        suggestExercise.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //customDialog();
        getSupportActionBar().setTitle("Search Location");
        auto_tv = (CustomAutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        auto_tv.setFocusable(true);
        auto_tv.setThreshold(1);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(auto_tv, InputMethodManager.SHOW_FORCED);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        auto_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (auto_tv.getRight() - auto_tv.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        auto_tv.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        auto_tv.setOnItemClickListener(mAutocompleteClickListener);
        adapter1 = new LocationAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient,
                BOUNDS_GREATER_SYDNEY, null);
        auto_tv.setAdapter(adapter1);
//        auto_tv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(final Editable editable) {
//                if (editable.length() > 2) {
//                    if (!auto_tv.isPopupShowing()) {
//                        suggestExercise.setVisibility(View.VISIBLE);
//                        suggestExercise.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent i = new Intent(LocationSearchActivity.this,AddInterestActivity.class);
//                                i.putExtra("event","event");
//                                i.putExtra("tv_name",editable.toString());
//                                startActivity(i);
//                            }
//                        });
//                    } else {
//                        suggestExercise.setVisibility(View.GONE);
//                    }
//                }else{
//                    suggestExercise.setVisibility(View.GONE);
//                }
//            }
//        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
        //TODO(Developer):Check error code and notify the user of error state and resolution.
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
//        exitToBottomAnimation();
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
        super.onPause();
    }
}
