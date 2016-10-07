package in.tagbin.mitohealthapp.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.DailyDetailsActivity;
import in.tagbin.mitohealthapp.activity.ExerciseSearchActivity;
import in.tagbin.mitohealthapp.activity.FoodDetailsActivity;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.helper.WheelView;
import in.tagbin.mitohealthapp.app.AppController;
import in.tagbin.mitohealthapp.helper.Config;
import in.tagbin.mitohealthapp.model.DataItems;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ExerciseLogModel;
import in.tagbin.mitohealthapp.model.ExerciseModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import in.tagbin.mitohealthapp.model.SendExerciseLogModel;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by hp on 7/26/2016.
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    static Context mContext;
    List<ExerciseLogModel> mModel;
    GifImageView mProgressBar;
    private static final int TYPE_ITEM = 1;

    public ExerciseAdapter(Context pContext, List<ExerciseLogModel> pModel,GifImageView pProgressBar){
        mContext = pContext;
        mModel = pModel;
        mProgressBar = pProgressBar;
    }
    @Override
    public ExerciseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_food_recommended,parent,false);
            ExerciseAdapter.ViewHolder vhItem = new ExerciseAdapter.ViewHolder(v,viewType);
            return vhItem;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ExerciseAdapter.ViewHolder holder, int position) {
        holder.itemView.setVisibility(View.VISIBLE);
        ((ExerciseAdapter.ViewHolder) holder).populateData(mModel.get(position), mContext,mProgressBar);

    }

    @Override
    public int getItemCount() {
        return mModel.size();
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        ExerciseLogModel mModel;
        TextView foodName,quantity,calories;
        ImageView accept,decline,refresh;
        String mType;
        CircleImageView circleImageView;
        GifImageView mProgressBar;
        RelativeLayout view;
        PrefManager pref;
        int day,month,year,hour,minute;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.tvFoodName);
            quantity = (TextView) itemView.findViewById(R.id.tvFoodQuantity);
            calories = (TextView) itemView.findViewById(R.id.tvFoodCalories);
            accept = (ImageView) itemView.findViewById(R.id.ivFoodAccept);
            decline = (ImageView) itemView.findViewById(R.id.ivFoodDecline);
            refresh = (ImageView) itemView.findViewById(R.id.ivFoodRefresh);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.civFoodLogger);
            view = (RelativeLayout) itemView.findViewById(R.id.relativeViewRecommend);

            view.setOnClickListener(this);
            decline.setOnClickListener(this);
        }

        public void populateData(ExerciseLogModel pModel, Context pContext,GifImageView pProgressBar){
            mModel = pModel;
            mContext = pContext;
            mProgressBar = pProgressBar;
            pref = new PrefManager(mContext);
            foodName.setText(pModel.getComponent().getName());
            quantity.setText(pModel.getAmount()+" mins");
            calories.setText(new DecimalFormat("##").format(pModel.getEnergy_burned()).toString()+" calories");
            //Picasso.with(mContext).load(mModel.getComponent().getImage()).into(circleImageView);
            Calendar[] dates = new Calendar[4];
            int i = 0;
            while (i < 4){
                Calendar selDate = Calendar.getInstance();
                selDate.add(Calendar.DAY_OF_MONTH, -i);
                dates[i] = selDate;
                i++;
            }
            int day,month,year;
            PrefManager pref = new PrefManager(mContext);
            if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
                day = pref.getKeyDay();
                month = pref.getKeyMonth();
                year = pref.getKeyYear();
            }else{
                Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
            }
            Date date = new Date(year-1900,month,day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (calendar.getTimeInMillis() > dates[0].getTimeInMillis() || calendar.getTimeInMillis() < dates[3].getTimeInMillis()){
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.GONE);
                refresh.setVisibility(View.GONE);
            }else {
                //mSheetLayout.expandFab();
                accept.setVisibility(View.GONE);
                decline.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.relativeViewRecommend:
//                    Intent i = new Intent(mContext, FoodDetailsActivity.class);
//                    i.putExtra("response", JsonUtils.jsonify(mModel));
//                    i.putExtra("logger","logger");
//                    mContext.startActivity(i);
                    showExerciseDialog(mModel);
                    break;
                case R.id.ivFoodDecline:
                    final android.app.AlertDialog.Builder alertDialog1 = new android.app.AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Delete logged exercise");
                    alertDialog1.setMessage(" Are you sure you want to delete this logged exercise?");
                    alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressBar.setVisibility(View.VISIBLE);
                            Controller.deleteLogFood(mContext,mModel.getId(),mDeleteListener);
                        }
                    });
                    alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog1.show();
                    break;
            }
        }
        RequestListener mDeleteListener = new RequestListener() {
            @Override
            public void onRequestStarted() {

            }

            @Override
            public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
                Log.d("exercise deleted",responseObject.toString());
                Intent i = new Intent(mContext,DailyDetailsActivity.class);
                i.putExtra("selection",2);
                mContext.startActivity(i);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext,"Exercise successfully deleted",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("exercise delete error",message);
                if (errorCode >= 400 && errorCode < 500) {
                    final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
        public void showExerciseDialog(final ExerciseLogModel data){
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(mContext);
            if (Build.VERSION.SDK_INT >= 22) {
                dialog = new android.app.AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
            } else {
                dialog = new android.app.AlertDialog.Builder(mContext);
            }
            LinearLayout linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_exercise_dialog,linearLayout,false);
            //TextView heading = (TextView) view.findViewById(R.id.tvExerciseDialogName);
            //heading.setText(data.getName());
            final EditText time = (EditText) view.findViewById(R.id.etExerciseTime);
            Spinner spinner = (Spinner) view.findViewById(R.id.spinnerExerciseIntensity);
            final List<String> intensities = new ArrayList<String>();
            intensities.add("Light");
            intensities.add("Moderate");
            intensities.add("Heavy");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, intensities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setSelection(1,false);
            final String[] unit = {"Moderate"};
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    unit[0] = intensities.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            final EditText calories = (EditText) view.findViewById(R.id.etExerciseCalories);
            LinearLayout linearSpeed = (LinearLayout) view.findViewById(R.id.linearExerciseSpeed);
            LinearLayout linearGradient = (LinearLayout) view.findViewById(R.id.linearExerciseGradient);
            LinearLayout linearAllDetails = (LinearLayout) view.findViewById(R.id.linearExerciseDialogDetails);
            if (data.getComponent().getMETS_RMR() == null){
                spinner.setVisibility(View.VISIBLE);
                linearAllDetails.setWeightSum(2);
            }else{
                spinner.setVisibility(View.GONE);
                linearAllDetails.setWeightSum(1);
            }
            linearLayout.addView(view);
            dialog.setView(linearLayout);
            dialog.setMessage(null);
            dialog.setTitle(data.getComponent().getName());
            if (data.getMets() == data.getComponent().getMETS_HI_BMR()){
                spinner.setSelection(2);
                if (data.getAmount() != 0){
                    time.setText(new DecimalFormat("##.#").format(data.getAmount()).toString());
                }
            }else if (data.getMets() == data.getComponent().getMETS_MI_BMR()){
                spinner.setSelection(1);
                if (data.getAmount() != 0){
                    time.setText(new DecimalFormat("##.#").format(data.getAmount()).toString());
                }
            }else if (data.getMets() == data.getComponent().getMETS_LI_BMR()){
                spinner.setSelection(0);
                if (data.getAmount() != 0){
                    time.setText(new DecimalFormat("##.#").format(data.getAmount()).toString());
                }
            }else{
                if (data.getEnergy_burned() != 0){
                    calories.setText(new DecimalFormat("##.#").format(data.getEnergy_burned()).toString());
                }
            }



            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Calendar calendar = Calendar.getInstance();
                    if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
                        day = pref.getKeyDay();
                        month = pref.getKeyMonth();
                        year = pref.getKeyYear();
                    }else {
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                    }
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minute = calendar.get(Calendar.MINUTE);
                    Date date = new Date(year-1900,month,day,hour,minute);
                    long timestamp = date.getTime()/1000L;
                    if (calories.getText().toString().equals("") || calories.getText().toString().equals("0") || calories.getText().toString() == null){
                        if (time.getText().toString().equals("") || time.getText().toString() == null){
                            Toast.makeText(mContext,"Please enter minutes to do this exercise",Toast.LENGTH_LONG).show();
                        }else {
                            SendExerciseLogModel sendExerciseLogModel = new SendExerciseLogModel();
                            sendExerciseLogModel.setAmount(Float.parseFloat(time.getText().toString()));
                            if (unit[0].equals("Light")){
                                sendExerciseLogModel.setMets(data.getComponent().getMETS_LI_BMR());
                            }else if (unit[0].equals("Moderate")){
                                sendExerciseLogModel.setMets(data.getComponent().getMETS_MI_BMR());
                            }else if (unit[0].equals("Heavy")){
                                sendExerciseLogModel.setMets(data.getComponent().getMETS_HI_BMR());
                            }
                            sendExerciseLogModel.setC_id(data.getComponent().getId());
                            sendExerciseLogModel.setCalorie(-1);
                            sendExerciseLogModel.setLtype("exercise");
                            sendExerciseLogModel.setTimestamp(String.valueOf(timestamp));
                            mProgressBar.setVisibility(View.VISIBLE);
                            Controller.updateLogExercise(mContext,sendExerciseLogModel,mModel.getId(),mExerciseLoggerListener);
                        }
                    }else{
                        SendExerciseLogModel sendExerciseLogModel = new SendExerciseLogModel();
                        sendExerciseLogModel.setAmount(-1);
                        sendExerciseLogModel.setMets(-1);
                        sendExerciseLogModel.setCalorie(Float.parseFloat(calories.getText().toString()));
                        sendExerciseLogModel.setC_id(data.getComponent().getId());
                        sendExerciseLogModel.setTimestamp(String.valueOf(timestamp));
                        sendExerciseLogModel.setLtype("exercise");
                        mProgressBar.setVisibility(View.VISIBLE);
                        Controller.updateLogExercise(mContext,sendExerciseLogModel,mModel.getId(),mExerciseLoggerListener);
                    }
                }
            });
            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            dialog.show();
        }
        RequestListener mExerciseLoggerListener = new RequestListener() {
            @Override
            public void onRequestStarted() {

            }

            @Override
            public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
                Log.d("exercise logged",responseObject.toString());
                Intent i = new Intent(mContext,DailyDetailsActivity.class);
                i.putExtra("selection",2);
                mContext.startActivity(i);
                ((Activity) mContext).finish();
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(mContext,"Exercise updated",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onRequestError(int errorCode, String message) {
                Log.d("exercise log error",message);
                if (errorCode >= 400 && errorCode < 500) {
                    final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                    ((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(mContext, "Internet connection error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        };
    }

}
