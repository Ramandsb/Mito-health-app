package in.tagbin.mitohealthapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.adapter.SetGoalAdpater;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.SetGoalModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 8/9/16.
 */
public class SetGoalsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView back;
    RecyclerView recyclerView;
    List<SetGoalModel> data;
    SetGoalAdpater adapter;
    GifImageView progressBar;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_goals);
        back = (TextView) findViewById(R.id.tvBackSetGoals);
        recyclerView = (RecyclerView) findViewById(R.id.rvSetGoals);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        data = new ArrayList<SetGoalModel>();
        adapter = new SetGoalAdpater(this,data,progressBar);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        Controller.getGoals(this,mGoalsListener);
        back.setText("<");
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvBackSetGoals:
                finish();
                break;
        }
    }
    RequestListener mGoalsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("set goals",responseObject.toString());
            Type collectionType = new TypeToken<ArrayList<SetGoalModel>>() {
            }.getType();
            List<SetGoalModel> da = (ArrayList<SetGoalModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            for (int i=0;i<da.size();i++){
                data.add(da.get(i));
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("set goal error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SetGoalsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SetGoalsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
