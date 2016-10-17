package in.tagbin.mitohealthapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.CuisineModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.UserModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 18/4/16.
 */
public class SelectCuisineActivity extends AppCompatActivity implements View.OnClickListener {
    List<CuisineModel> selectedCuisines;
    ImageView close,up,down;
    TextView apply;
    ScrollView scrollview;
    CheckBox checkbox;
    LinearLayout linearLayout;
    SharedPreferences login_details;
    String user_id;
    GifImageView progressBar;
    LayoutInflater mInflater;
    //ApplyFilterModel applyFilterModel;
    List<Integer> selectedTags,preselectedTags;
    private static final String TAG = SelectCuisineActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine);
        selectedCuisines = new ArrayList<CuisineModel>();
        String response = getIntent().getStringExtra("response");
        Type collectionType = new TypeToken<List<CuisineModel>>() {
        }.getType();
        selectedCuisines = (List<CuisineModel>) new Gson()
                .fromJson(response, collectionType);
        linearLayout = (LinearLayout) findViewById(R.id.ll_checkbox);
        apply = (TextView) findViewById(R.id.tvCuisinesApply);
        close = (ImageView) findViewById(R.id.ivCuisinesClose);
        up = (ImageView) findViewById(R.id.ivCuisineUp);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        down = (ImageView) findViewById(R.id.ivCuisineDown);
        scrollview = (ScrollView) findViewById(R.id.svCuisine);
        progressBar.setVisibility(View.VISIBLE);
        Controller.getCuisines(this,mCuisineListener);
        apply.setOnClickListener(this);
        close.setOnClickListener(this);
        up.setOnClickListener(this);

        down.setOnClickListener(this);
        mInflater = LayoutInflater.from(this);
        selectedTags = new ArrayList<Integer>();
        preselectedTags = new ArrayList<Integer>();

        //createCheckboxButton(mInflater, linearLayout);
    }
    public void createCheckboxButton(LayoutInflater inflater, LinearLayout linearLayout,List<CuisineModel> data){
        linearLayout.removeAllViews();
        preselectedTags.clear();
        selectedTags.removeAll(selectedTags);
        int i = 0;
        if (selectedCuisines != null && selectedCuisines.size() >0){
            for (int y = 0;y<selectedCuisines.size();y++){
                preselectedTags.add(selectedCuisines.get(y).getId());
            }
            Log.d(TAG,preselectedTags.toString());

        }
        if (!(linearLayout.getChildCount() > 0)) {
            while (i < data.size()) {
                View checkItem = inflater.inflate(R.layout.item_checkbox, linearLayout, false);
                checkbox = (CheckBox) checkItem.findViewById(R.id.chk_option);
                checkbox.setText(data.get(i).getCuisine_name());
                checkbox.setTag(data.get(i).getId());
                checkbox.setOnClickListener(this);
                checkbox.setTextColor(Color.parseColor("#b7b7b7"));
                checkbox.setButtonDrawable(new StateListDrawable());
                checkbox.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        0, 0);
                if (preselectedTags != null) {
                    for (int y = 0; y < preselectedTags.size(); y++) {
                        if (preselectedTags.get(y).toString().equals(checkbox.getTag().toString())) {
                            Log.d(TAG,checkbox.getTag().toString());
                            checkbox.setChecked(true);
                            checkbox.setTextColor(Color.parseColor("#26446d"));
                        }
                    }
                }
                linearLayout.addView(checkItem);
                i++;
            }
            if (preselectedTags != null)
                selectedTags.addAll(preselectedTags);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCuisinesApply:
                login_details = getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);
                user_id = login_details.getString("user_id", "");
                progressBar.setVisibility(View.VISIBLE);
                Controller.setCuisines(this,selectedTags,user_id,mSendCuisineListener);
                break;
            case R.id.ivCuisinesClose:
                finish();
                break;
            case R.id.ivCuisineDown:
                scrollview.fullScroll(View.FOCUS_DOWN);
                break;
            case R.id.ivCuisineUp:
                scrollview.fullScroll(View.FOCUS_UP);
                break;
            default:
                if (((CheckBox) v).isChecked()){
                    ((CheckBox) v).setTextColor(Color.parseColor("#26446d"));
                    selectedTags.add((Integer) ((CheckBox) v).getTag());
                }else {
                    ((CheckBox) v).setTextColor(Color.parseColor("#b7b7b7"));
                    selectedTags.remove((Integer) ((CheckBox) v).getTag());
                }
        }
    }
    RequestListener mCuisineListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(final Object responseObject) throws JSONException, ParseException {
            Type collectionType = new TypeToken<ArrayList<CuisineModel>>() {
            }.getType();
            final List<CuisineModel> cuisne = (ArrayList<CuisineModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    createCheckboxButton(mInflater, linearLayout,cuisne);
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
                        Toast.makeText(SelectCuisineActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectCuisineActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mSendCuisineListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("send goal",responseObject.toString());
            PrefManager pref = new PrefManager(SelectCuisineActivity.this);
            pref.setKeyUserDetails(JsonUtils.objectify(responseObject.toString(), UserModel.class));
            Intent intent = new Intent(SelectCuisineActivity.this, BinderActivity.class);
            intent.putExtra("selection", 3);
            startActivity(intent);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("send goal error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectCuisineActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SelectCuisineActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
