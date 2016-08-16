package in.tagbin.mitohealthapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.InterestModel;

/**
 * Created by aasaqt on 14/8/16.
 */
public class InterestActivity extends AppCompatActivity {
    FlowLayout flowLayout;
    Toolbar toolbar;
    TextView next;
    List<Integer> idFinal;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_interests);
        flowLayout = (FlowLayout) findViewById(R.id.flowLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        idFinal = new ArrayList<Integer>();
        getSupportActionBar().setTitle("Interests");
        next = (TextView) findViewById(R.id.tvNextInterest);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InterestActivity.this,SliderActivity.class);
                startActivity(i);
            }
        });
        Controller.getInterests(this,mInterestListener);
    }
    RequestListener mInterestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            final InterestModel interestModel = JsonUtils.objectify(responseObject.toString(),InterestModel.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setToggleButtons(interestModel);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };
    public void setToggleButtons(final InterestModel interestModel){
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i=0;i<interestModel.getList().size();i++){
            View layout = inflater.inflate(R.layout.interest_toggle,flowLayout,false);
            final ToggleButton toggleButton = (ToggleButton) layout.findViewById(R.id.toggleButton);
            toggleButton.setTextOn(interestModel.getList().get(i).getName());
            toggleButton.setTextOff(interestModel.getList().get(i).getName());
            toggleButton.setChecked(false);
            final int finalI = i;
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toggleButton.isChecked()){
                        toggleButton.setBackgroundResource(R.drawable.backtoggled);
                        toggleButton.setTextColor(Color.parseColor("#ffffff"));
                        idFinal.add(interestModel.getList().get(finalI).getId());
                    }else{
                        toggleButton.setBackgroundResource(R.drawable.back);
                        toggleButton.setTextColor(Color.parseColor("#000000"));
                        idFinal.remove(interestModel.getList().get(finalI).getId());
                    }
                }
            });
            flowLayout.addView(layout);
        }
    }
}
