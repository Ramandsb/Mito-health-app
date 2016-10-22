package in.tagbin.mitohealthapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 7/9/16.
 */
public class AddInterestActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etInterestName,etFoodCalories;
    Button sendInterst;
    TextView heading,foodCalories;
    ImageView image;
    GifImageView progressBar;
    String response,url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.actvity_addinterest);
        etInterestName = (EditText) findViewById(R.id.etInterestName);
        etFoodCalories= (EditText) findViewById(R.id.etFoodCalories);
        heading = (TextView) findViewById(R.id.tvAddIneterstNameHeading);
        foodCalories = (TextView) findViewById(R.id.tvAddFoodCaloriesHeading);

        sendInterst = (Button) findViewById(R.id.buttonSendInterest);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        image = (ImageView) findViewById(R.id.ivAddInterestimage);

        sendInterst.setOnClickListener(this);
        if (getIntent().getStringExtra("food") != null){
            etInterestName.setHint("Suggest Food");
            heading.setText("Food Name");
            etFoodCalories.setVisibility(View.VISIBLE);
            foodCalories.setVisibility(View.VISIBLE);
            //Picasso.with(this).load(url).into(image);
        }else if (getIntent().getStringExtra("exercise") != null){
            etInterestName.setHint("Suggest Exercise");
            heading.setText("Exercise Name");
            etFoodCalories.setVisibility(View.GONE);
            foodCalories.setVisibility(View.GONE);
            //Picasso.with(this).load(url).into(image);
        }else if (getIntent().getStringExtra("event") != null){
            etInterestName.setHint("Suggest Event Type");
            heading.setText("Event Type Name");
            etFoodCalories.setVisibility(View.GONE);
            foodCalories.setVisibility(View.GONE);
            //Picasso.with(this).load(url).into(image);
        }else{
            etInterestName.setHint("Suggest Interest");
            heading.setText("Interest Name");
            etFoodCalories.setVisibility(View.GONE);
            foodCalories.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("name") != null) {
            response = getIntent().getStringExtra("name");
            etInterestName.setText(response);
        }
        etInterestName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {

                    if (getIntent().getStringExtra("food") != null){

                    }else if (getIntent().getStringExtra("exercise") != null){
                        progressBar.setVisibility(View.VISIBLE);
                        Controller.setNewExercise(AddInterestActivity.this, etInterestName.getText().toString(), mSetInterstListener);
                    }else if (getIntent().getStringExtra("event") != null){
                        progressBar.setVisibility(View.VISIBLE);
                        Controller.setNewEventType(AddInterestActivity.this, etInterestName.getText().toString(), mSetInterstListener);
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        Controller.setNewInterest(AddInterestActivity.this, etInterestName.getText().toString(), mSetInterstListener);
                    }
                }
                return false;
            }
        });
        etFoodCalories.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    progressBar.setVisibility(View.VISIBLE);
                    float calories = 0 ;
                    if (!etFoodCalories.getText().toString().equals("") && etFoodCalories.getText().toString() != null && !etFoodCalories.getText().toString().isEmpty()){
                        calories = Float.parseFloat(etFoodCalories.getText().toString());
                    }
                    if (getIntent().getStringExtra("food") != null){
                        Controller.setNewFood(AddInterestActivity.this, etInterestName.getText().toString(), calories, mSetInterstListener);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSendInterest:
                progressBar.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("food") != null){
                    float calories = 0 ;
                    if (!etFoodCalories.getText().toString().equals("") && !etFoodCalories.getText().toString().isEmpty()){
                        calories = Float.parseFloat(etFoodCalories.getText().toString());
                    }
                    Controller.setNewFood(AddInterestActivity.this, etInterestName.getText().toString(),calories, mSetInterstListener);
                }else if (getIntent().getStringExtra("exercise") != null){
                    Controller.setNewExercise(AddInterestActivity.this, etInterestName.getText().toString(), mSetInterstListener);
                }else if (getIntent().getStringExtra("event") != null){
                    Controller.setNewEventType(AddInterestActivity.this, etInterestName.getText().toString(), mSetInterstListener);
                }else {
                    Controller.setNewInterest(AddInterestActivity.this, etInterestName.getText().toString(), mSetInterstListener);
                }
                break;
        }
    }
    RequestListener mSetInterstListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("set interest",responseObject.toString());
            finish();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    if (getIntent().getStringExtra("food") != null){
                        Toast.makeText(AddInterestActivity.this, "Your food item is submitted successfully. We will process it and update it", Toast.LENGTH_LONG).show();
                    }else if (getIntent().getStringExtra("exercise") != null){
                        Toast.makeText(AddInterestActivity.this, "Your exercise is submitted successfully. We will process it and update it", Toast.LENGTH_LONG).show();
                    }else if (getIntent().getStringExtra("event") != null){
                        Toast.makeText(AddInterestActivity.this, "Your event type is submitted successfully. We will process it and update it", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(AddInterestActivity.this, "Your interest is submitted successfully. We will process it and update it", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("set interest error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddInterestActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddInterestActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
