package in.tagbin.mitohealthapp;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 7/9/16.
 */
public class AddInterestActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etInterestName;
    Button sendInterst;
    GifImageView progressBar;
    String response;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.actvity_addinterest);
        etInterestName = (EditText) findViewById(R.id.etInterestName);
        sendInterst = (Button) findViewById(R.id.buttonSendInterest);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        sendInterst.setOnClickListener(this);
        if (getIntent().getStringExtra("name") != null) {
            response = getIntent().getStringExtra("name");
            etInterestName.setText(response);
        }
        etInterestName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.setNewInterest(AddInterestActivity.this,etInterestName.getText().toString(),mSetInterstListener);
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
                Controller.setNewInterest(this,etInterestName.getText().toString(),mSetInterstListener);
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
                    Toast.makeText(AddInterestActivity.this, "Your interest is submitted successfully. We will process it and update it", Toast.LENGTH_LONG).show();
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
