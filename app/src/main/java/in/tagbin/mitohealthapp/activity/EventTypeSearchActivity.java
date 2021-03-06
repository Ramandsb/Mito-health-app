package in.tagbin.mitohealthapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import in.tagbin.mitohealthapp.adapter.EventTypeSearchAdapter;
import in.tagbin.mitohealthapp.adapter.ExerciseSearchAdapter;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CustomAutoCompleteTextView;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.EventTypeModel;
import in.tagbin.mitohealthapp.model.ExerciseModel;
import in.tagbin.mitohealthapp.model.HitsArrayModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 12/10/16.
 */

public class EventTypeSearchActivity extends AppCompatActivity {
    CustomAutoCompleteTextView auto_tv;
    TextView suggestExercise;
    GifImageView progressBar;
    EventTypeSearchAdapter adapter1;
    PrefManager pref;
    int day,month,year,hour,minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);
        pref = new PrefManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        suggestExercise = (TextView) findViewById(R.id.tvSuggestFood);
        suggestExercise.setText("Suggest Event Type");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //customDialog();
        getSupportActionBar().setTitle("Search Event Type");
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
        adapter1 = new EventTypeSearchAdapter(this, android.R.layout.simple_list_item_1);
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
                        suggestExercise.setVisibility(View.VISIBLE);
                        suggestExercise.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(EventTypeSearchActivity.this,AddInterestActivity.class);
                                i.putExtra("event","event");
                                i.putExtra("tv_name",editable.toString());
                                startActivity(i);
                            }
                        });
                    } else {
                        suggestExercise.setVisibility(View.GONE);
                    }
                }else{
                    suggestExercise.setVisibility(View.GONE);
                }
            }
        });

    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //HitsArrayModel hitsArrayModel = adapter1.getItem(position);
            //auto_tv.setText(hitsArrayModel.getName());
            EventTypeModel interestListModel = adapter1.getItem(position);
            auto_tv.setText(interestListModel.getTitle());
            Intent i = new Intent(EventTypeSearchActivity.this,CreateEventActivity.class);
            i.putExtra("eventtitle",interestListModel.getTitle());
            i.putExtra("eventid",interestListModel.getId());
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            progressBar.setVisibility(View.VISIBLE);
            //Controller.getExerciseDetails(ExerciseSearchActivity.this,hitsArrayModel.getExercise_id(),mFoodDetailListener);
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

}
