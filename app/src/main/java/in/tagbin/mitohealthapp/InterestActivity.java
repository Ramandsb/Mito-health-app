package in.tagbin.mitohealthapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.InterestModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 14/8/16.
 */
public class InterestActivity extends AppCompatActivity {
    FlowLayout flowLayout;
    Toolbar toolbar;
    TextView next;
    InterestModel interestModel;
    List<Integer> idFinal;
    PrefManager pref;
    GifImageView progressBar;
    android.widget.SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.fragment_interests);
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        idFinal = new ArrayList<Integer>();
        pref = new PrefManager(this);
        getSupportActionBar().setTitle("Interests");
        String response = getIntent().getStringExtra("response");
        interestModel = JsonUtils.objectify(response, InterestModel.class);
        setToggleButtons(interestModel);
        next = (TextView) findViewById(R.id.tvNextInterest);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.setKeyInterests(idFinal);
                progressBar.setVisibility(View.VISIBLE);
                Controller.setInterests(InterestActivity.this,idFinal,mSetInterestListener);

            }
        });
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("What interests you?");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                flowLayout.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(InterestActivity.this);
                for (int i = 0; i < interestModel.getList().size(); i++) {
                    if (interestModel.getList().get(i).getName().toLowerCase().contains(query)) {
                        View layout = inflater.inflate(R.layout.interest_toggle, flowLayout, false);
                        final ToggleButton toggleButton = (ToggleButton) layout.findViewById(R.id.toggleButton);
                        toggleButton.setTextOn(interestModel.getList().get(i).getName());
                        toggleButton.setTextOff(interestModel.getList().get(i).getName());
                        if (idFinal.size() >0) {
                            for (int y = 0; y < idFinal.size(); y++) {
                                if (interestModel.getList().get(i).getId() == idFinal.get(y)){
                                    toggleButton.setChecked(true);
                                    break;
                                }else{
                                    toggleButton.setChecked(false);
                                }
                            }
                        }else{
                            toggleButton.setChecked(false);
                        }
                        if (toggleButton.isChecked()) {
                            toggleButton.setBackgroundResource(R.drawable.backtoggled);
                            toggleButton.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            toggleButton.setBackgroundResource(R.drawable.back);
                            toggleButton.setTextColor(Color.parseColor("#26446d"));
                        }
                        final int finalI = i;
                        toggleButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (toggleButton.isChecked()) {
                                    toggleButton.setBackgroundResource(R.drawable.backtoggled);
                                    toggleButton.setTextColor(Color.parseColor("#ffffff"));
                                    idFinal.add(interestModel.getList().get(finalI).getId());
                                } else {
                                    toggleButton.setBackgroundResource(R.drawable.back);
                                    toggleButton.setTextColor(Color.parseColor("#26446d"));
                                    for (Integer integer : new ArrayList<>(idFinal)) {
                                        if (integer == interestModel.getList().get(finalI).getId())
                                            idFinal.remove(integer);
                                    }
                                }
                            }
                        });
                        flowLayout.addView(layout);
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                flowLayout.removeAllViews();
                LayoutInflater inflater = LayoutInflater.from(InterestActivity.this);
                for (int i = 0; i < interestModel.getList().size(); i++) {
                    if (interestModel.getList().get(i).getName().toLowerCase().contains(newText)) {
                        View layout = inflater.inflate(R.layout.interest_toggle, flowLayout, false);
                        final ToggleButton toggleButton = (ToggleButton) layout.findViewById(R.id.toggleButton);
                        toggleButton.setTextOn(interestModel.getList().get(i).getName());
                        toggleButton.setTextOff(interestModel.getList().get(i).getName());
                        if (idFinal.size() >0) {
                            for (int y = 0; y < idFinal.size(); y++) {
                                if (interestModel.getList().get(i).getId() == idFinal.get(y)){
                                    toggleButton.setChecked(true);
                                    break;
                                }else{
                                    toggleButton.setChecked(false);
                                }
                            }
                        }else{
                            toggleButton.setChecked(false);
                        }
                        if (toggleButton.isChecked()) {
                            toggleButton.setBackgroundResource(R.drawable.backtoggled);
                            toggleButton.setTextColor(Color.parseColor("#ffffff"));
                        } else {
                            toggleButton.setBackgroundResource(R.drawable.back);
                            toggleButton.setTextColor(Color.parseColor("#26446d"));
                        }
                        final int finalI = i;
                        toggleButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (toggleButton.isChecked()) {
                                    toggleButton.setBackgroundResource(R.drawable.backtoggled);
                                    toggleButton.setTextColor(Color.parseColor("#ffffff"));
                                    idFinal.add(interestModel.getList().get(finalI).getId());
                                } else {
                                    toggleButton.setBackgroundResource(R.drawable.back);
                                    toggleButton.setTextColor(Color.parseColor("#26446d"));
                                    for (Integer integer : new ArrayList<>(idFinal)) {
                                        if (integer == interestModel.getList().get(finalI).getId())
                                            idFinal.remove(integer);
                                    }
                                }
                            }
                        });
                        flowLayout.addView(layout);
                    }
                }

                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(view, 0);
                    }
                }
            }
        });
    }

    public void setToggleButtons(final InterestModel interestModel) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < interestModel.getList().size(); i++) {
            View layout = inflater.inflate(R.layout.interest_toggle, flowLayout, false);
            final ToggleButton toggleButton = (ToggleButton) layout.findViewById(R.id.toggleButton);
            toggleButton.setTextOn(interestModel.getList().get(i).getName());
            toggleButton.setTextOff(interestModel.getList().get(i).getName());
            if (pref.getInterests() != null && pref.getInterests().size() >0){
                for (int y = 0; y < pref.getInterests().size(); y++) {
                    if (interestModel.getList().get(i).getId() == pref.getInterests().get(y)){
                        toggleButton.setChecked(true);
                        break;
                    }else{
                        toggleButton.setChecked(false);
                    }
                }
                if (toggleButton.isChecked()) {
                    toggleButton.setBackgroundResource(R.drawable.backtoggled);
                    toggleButton.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    toggleButton.setBackgroundResource(R.drawable.back);
                    toggleButton.setTextColor(Color.parseColor("#26446d"));
                }
            }else{
                toggleButton.setChecked(false);
            }

            final int finalI = i;
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toggleButton.isChecked()) {
                        toggleButton.setBackgroundResource(R.drawable.backtoggled);
                        toggleButton.setTextColor(Color.parseColor("#ffffff"));
                        idFinal.add(interestModel.getList().get(finalI).getId());
                    } else {
                        toggleButton.setBackgroundResource(R.drawable.back);
                        toggleButton.setTextColor(Color.parseColor("#26446d"));
                        for (Integer integer : new ArrayList<>(idFinal)) {
                            if (integer == interestModel.getList().get(finalI).getId())
                                idFinal.remove(integer);
                        }
                    }
                }
            });
            flowLayout.addView(layout);
        }
    }
    RequestListener mSetInterestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("iterest ",responseObject.toString());
            PrefManager pref = new PrefManager(InterestActivity.this);
            pref.setTutorial1(true);
            Intent i = new Intent(InterestActivity.this,BinderActivity.class);
            i.putExtra("interests","interests");
            startActivity(i);
            finish();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("interest error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(InterestActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(InterestActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

}
