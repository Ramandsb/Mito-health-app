package in.tagbin.mitohealthapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CustomAutoCompleteTextView;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.adapter.FoodLoggerSearchAdapter;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.HitsArrayModel;
import pl.droidsonroids.gif.GifImageView;

public class FoodSearchActivity extends AppCompatActivity {
    CustomAutoCompleteTextView auto_tv;
    String back = "";
    TextView suggestFood;
    GifImageView progressBar;
    FoodLoggerSearchAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        suggestFood = (TextView) findViewById(R.id.tvSuggestFood);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //customDialog();
        getSupportActionBar().setTitle("Search Food");
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
        adapter1 = new FoodLoggerSearchAdapter(this, android.R.layout.simple_list_item_1);
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
                                Intent i = new Intent(FoodSearchActivity.this,AddInterestActivity.class);
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
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (back.equals("exercise")) {
//                    startActivity(new Intent(FoodSearchActivity.this, DailyDetailsActivity.class).putExtra("selection", 2));
//                    finish();
//
//                } else if (back.equals("food")) {
//                    startActivity(new Intent(FoodSearchActivity.this, DailyDetailsActivity.class).putExtra("selection", 0));
//                    finish();
//                }
//            }
//        });
    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            HitsArrayModel hitsArrayModel = adapter1.getItem(position);
            auto_tv.setText(hitsArrayModel.getName());
            progressBar.setVisibility(View.VISIBLE);
            Controller.getFoodDetails(FoodSearchActivity.this,hitsArrayModel.getRecipe_id(),mFoodDetailListener);
        }
    };


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
            Log.d("food detials",responseObject.toString());
            Intent i = new Intent(FoodSearchActivity.this, FoodDetailsActivity.class);
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
                        Toast.makeText(FoodSearchActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodSearchActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
