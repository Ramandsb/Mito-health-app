package in.tagbin.mitohealthapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.helper.FlowLayout;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.InterestModel;
import in.tagbin.mitohealthapp.model.UserInterestModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 14/8/16.
 */
public class InterestActivity extends AppCompatActivity {
    FlowLayout flowLayout;
    Toolbar toolbar;
    TextView next,addInterest,coins;
    InterestModel interestModel;
    List<Integer> idFinal;
    List<UserInterestModel> userInterestModels;
    PrefManager pref;
    int count1= 0, count2 = 0,coinsFinal = 0;
    GifImageView progressBar;
    android.widget.SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.activity_interests);
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        idFinal = new ArrayList<Integer>();
        pref = new PrefManager(this);
        getSupportActionBar().setTitle("Interests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String response = getIntent().getStringExtra("response");
        interestModel = JsonUtils.objectify(response, InterestModel.class);
        Type collectionType = new TypeToken<ArrayList<UserInterestModel>>() {}.getType();
        userInterestModels = (ArrayList<UserInterestModel>) new Gson().fromJson(getIntent().getStringExtra("userinterests"), collectionType);
        addInterest = (TextView) findViewById(R.id.tvSuggestInterst);
        if (userInterestModels != null && userInterestModels.size() > 0 ){
            for (int y = 0;y< userInterestModels.size();y++){
                idFinal.add(userInterestModels.get(y).getInterest().getId());
            }
        }
        setToggleButtons(interestModel);
//        next = (TextView) findViewById(R.id.tvNextInterest);
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("What interests you?");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                flowLayout.removeAllViews();
                count1 = 0;
                LayoutInflater inflater = LayoutInflater.from(InterestActivity.this);
                for (int i = 0; i < interestModel.getList().size(); i++) {
                    if (interestModel.getList().get(i).getName().toLowerCase().contains(query)) {
                        count1++;
                        addInterest.setVisibility(View.GONE);
                        View layout = inflater.inflate(R.layout.item_interests, flowLayout, false);
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
                if (count1 == 0){
                    addInterest.setVisibility(View.VISIBLE);
                    addInterest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(InterestActivity.this,AddInterestActivity.class);
                            i.putExtra("name",query);
                            startActivity(i);
                        }
                    });
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                flowLayout.removeAllViews();
                count2 = 0;
                LayoutInflater inflater = LayoutInflater.from(InterestActivity.this);
                for (int i = 0; i < interestModel.getList().size(); i++) {
                    if (interestModel.getList().get(i).getName().toLowerCase().contains(newText)) {
                        count2++;
                        addInterest.setVisibility(View.GONE);
                        View layout = inflater.inflate(R.layout.item_interests, flowLayout, false);
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
                if (count2 == 0){
                    addInterest.setVisibility(View.VISIBLE);
                    addInterest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(InterestActivity.this,AddInterestActivity.class);
                            i.putExtra("name",newText);
                            startActivity(i);
                        }
                    });
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }else if (item.getItemId() == R.id.action_next){
            progressBar.setVisibility(View.VISIBLE);
            Controller.setInterests(InterestActivity.this,idFinal,mSetInterestListener);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setToggleButtons(final InterestModel interestModel) {
        LayoutInflater inflater = LayoutInflater.from(this);
        if (interestModel.getList().size() >0) {
            addInterest.setVisibility(View.GONE);
            idFinal.clear();
            for (int i = 0; i < interestModel.getList().size(); i++) {
                View layout = inflater.inflate(R.layout.item_interests, flowLayout, false);
                final ToggleButton toggleButton = (ToggleButton) layout.findViewById(R.id.toggleButton);
                toggleButton.setTextOn(interestModel.getList().get(i).getName());
                toggleButton.setTextOff(interestModel.getList().get(i).getName());
                if (userInterestModels != null && userInterestModels.size() > 0) {
                    for (int y = 0; y < userInterestModels.size(); y++) {
                        if (interestModel.getList().get(i).getId() == userInterestModels.get(y).getInterest().getId()) {
                            toggleButton.setChecked(true);
                            break;
                        } else {
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
                } else {
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
                        Log.d("intersts",idFinal.toString());
                    }
                });
                flowLayout.addView(layout);
            }
        }else{
            addInterest.setVisibility(View.VISIBLE);
            addInterest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InterestActivity.this,AddInterestActivity.class);
                    startActivity(i);
                }
            });
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
            i.putExtra("selection",0);
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
