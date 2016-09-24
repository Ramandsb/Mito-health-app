package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.ExerciseFrag;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CustomAutoCompleteTextView;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PlaceAutoCompleteAdapter1;
import in.tagbin.mitohealthapp.model.ElasticSearchModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.EventTypeModel;
import in.tagbin.mitohealthapp.model.HitsArrayModel;
import in.tagbin.mitohealthapp.model.MustModel;
import pl.droidsonroids.gif.GifImageView;

public class DishSearch extends AppCompatActivity {
    CustomAutoCompleteTextView auto_tv;
    String back = "";
    TextView suggestFood;
    GifImageView progressBar;
    PlaceAutoCompleteAdapter1 adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        suggestFood = (TextView) findViewById(R.id.tvSuggestFood);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //customDialog();

        back = getIntent().getStringExtra("back");
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
        adapter1 = new PlaceAutoCompleteAdapter1(this, android.R.layout.simple_list_item_1);
        auto_tv.setAdapter(adapter1);
        auto_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.length() > 2) {
                    if (!auto_tv.isPopupShowing()) {
                        suggestFood.setVisibility(View.VISIBLE);
                        suggestFood.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(DishSearch.this,AddInterestActivity.class);
                                i.putExtra("food","food");
                                i.putExtra("name",editable.toString());
                                startActivity(i);
                            }
                        });
                    } else {
                        suggestFood.setVisibility(View.GONE);
                    }
                }else{
                    suggestFood.setVisibility(View.GONE);
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (back.equals("exercise")) {
                    startActivity(new Intent(DishSearch.this, CollapsableLogging.class).putExtra("selection", 2));
                    finish();

                } else if (back.equals("food")) {
                    startActivity(new Intent(DishSearch.this, CollapsableLogging.class).putExtra("selection", 0));
                    finish();
                }
            }
        });
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            HitsArrayModel hitsArrayModel = adapter1.getItem(position);
            auto_tv.setText(hitsArrayModel.get_source().getName());
            progressBar.setVisibility(View.VISIBLE);
            Controller.getFoodDetails(DishSearch.this,hitsArrayModel.get_source().getRecipe_id(),mFoodDetailListener);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
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


    public void hideSoftKeyPad(){
        if(getCurrentFocus()!=null && getCurrentFocus() instanceof EditText)
        {
            InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        }
    }
    @Override
    protected void onPause() {
//        exitToBottomAnimation();
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
        super.onPause();
    }

    RequestListener mFoodDetailListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Intent i = new Intent(DishSearch.this, FoodDetails.class);
            i.putExtra("response", responseObject.toString());
            i.putExtra("foodsearch","foodsearch");
            startActivity(i);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DishSearch.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DishSearch.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
