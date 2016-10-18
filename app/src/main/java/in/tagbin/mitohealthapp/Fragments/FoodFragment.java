package in.tagbin.mitohealthapp.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopeer.itemtouchhelperextension.ItemTouchHelperExtension;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import in.tagbin.mitohealthapp.activity.DailyDetailsActivity;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.adapter.FoodLoggerAdapter;
import in.tagbin.mitohealthapp.helper.ItemTouchHelperCallback;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.adapter.RecommendationAdapter;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import pl.droidsonroids.gif.GifImageView;


public class FoodFragment extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener{
    MaterialCalendarView widget;
    Spinner spinner;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvRecommendations;
    RecommendationAdapter mAdapter;
    LinearLayout linearFoodLogger;
    RelativeLayout relativeFoodRecommended,relativeFoodLogged;
    List<RecommendationModel> data,loggerModel;
    List<String> measuring_units;
    int pos;
    PrefManager pref;
    public ItemTouchHelperExtension mItemTouchHelper;
    public ItemTouchHelperExtension.Callback mCallback;
    GifImageView progressBar;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.frag_food, container, false);
        pref = new PrefManager(getContext());
        rvRecommendations = (RecyclerView) fragView.findViewById(R.id.rvRecommendation);
        spinner = (Spinner) fragView.findViewById(R.id.spinnerRecommended);
        relativeFoodRecommended = (RelativeLayout) fragView.findViewById(R.id.relativeRecommended);
        relativeFoodLogged = (RelativeLayout) fragView.findViewById(R.id.relativeLoggedFood);
        linearFoodLogger = (LinearLayout) fragView.findViewById(R.id.linearFoodLogger);
        //progressBar = (GifImageView) fragView.findViewById(R.id.progressBar);
        progressBar = DailyDetailsActivity.progressBar;
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecommendations.setLayoutManager(linearLayoutManager);
        rvRecommendations.setHasFixedSize(true);
        //recyclerExercise = (RecyclerView) fragView.findViewById(R.id.recyclerExercise);
        widget= (MaterialCalendarView) fragView.findViewById(R.id.calendarView);
        data = new ArrayList<RecommendationModel>();
        loggerModel = new ArrayList<RecommendationModel>();
        Calendar calendar = Calendar.getInstance();
        int day,month,year;
        if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
            day = pref.getKeyDay();
            month = pref.getKeyMonth();
            year = pref.getKeyYear();
        }else {
            day = calendar.get(Calendar.DAY_OF_MONTH);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
        }

        Date date1 = new Date(year-1900,month,day,0,0);
        widget.setSelectedDate(date1);
        long timestamp = date1.getTime()/1000L;
        progressBar.setVisibility(View.VISIBLE);
        Log.d("timestamop",""+timestamp);
        Controller.getLogger(getContext(),timestamp,mGetFoodLoggerListener);
        progressBar.setVisibility(View.VISIBLE);
        Controller.getFoodRecommendation(getContext(),timestamp,mRecommendationListener);
        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);
        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR) + 2, Calendar.OCTOBER, 31);
        widget.setOnDateChangedListener(this);
        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .setFirstDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
                .commit();
        measuring_units = new ArrayList<>();
//        measuring_units.add("Breakfast");
//        measuring_units.add("Lunch");
//        measuring_units.add("Dinner");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final String[] unit = {"Kg"};
        mAdapter = new RecommendationAdapter(getContext(), data, "",measuring_units,progressBar);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(measuring_units.size() >0 && data.size() > 0) {
                    pos = i;
                    Log.d("item selected", measuring_units.get(i));
                    unit[0] = measuring_units.get(i);
                    mAdapter = new RecommendationAdapter(getContext(), data, measuring_units.get(i),measuring_units,progressBar);
                    rvRecommendations.setAdapter(mAdapter);
                    mCallback = new ItemTouchHelperCallback();
                    mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
                    mItemTouchHelper.attachToRecyclerView(rvRecommendations);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return fragView;
    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, monthOfYear, dayOfMonth);
        //Sets the selected date from Picker
    }

    public void setFoodLogger(List<RecommendationModel> foodLogger){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        linearFoodLogger.removeAllViews();
        View loggerView1 = inflater.inflate(R.layout.item_food_logger, linearFoodLogger, false);
        TextView mealType1 = (TextView) loggerView1.findViewById(R.id.tvRecommendedMealType);
        TextView mealTime1 = (TextView) loggerView1.findViewById(R.id.tvRecommendedMealTime);
        TextView mealCalories1 = (TextView) loggerView1.findViewById(R.id.tvRecommendedMealTotalCalories);
        RecyclerView rvLogger1 = (RecyclerView) loggerView1.findViewById(R.id.rvFoodLogger);
        ImageView foodImage1 = (ImageView) loggerView1.findViewById(R.id.ivFoodImage);
        ImageView dot1 = (ImageView) loggerView1.findViewById(R.id.ivDot);
        dot1.setVisibility(View.GONE);
        foodImage1.getLayoutParams().height = 45;
        foodImage1.getLayoutParams().width = 45;
        foodImage1.requestLayout();
        foodImage1.setImageResource(R.drawable.food_meal_image);
        linearFoodLogger.addView(loggerView1);
        mealType1.setText("WAKE UP !");
        mealTime1.setVisibility(View.GONE);
        mealCalories1.setVisibility(View.GONE);
        rvLogger1.setVisibility(View.GONE);
        for (int i=0;i<foodLogger.size();i++) {
            View loggerView = inflater.inflate(R.layout.item_food_logger, linearFoodLogger, false);
            TextView mealType = (TextView) loggerView.findViewById(R.id.tvRecommendedMealType);
            TextView mealTime = (TextView) loggerView.findViewById(R.id.tvRecommendedMealTime);
            TextView mealCalories = (TextView) loggerView.findViewById(R.id.tvRecommendedMealTotalCalories);
            RecyclerView rvLogger = (RecyclerView) loggerView.findViewById(R.id.rvFoodLogger);
            ImageView foodImage = (ImageView) loggerView.findViewById(R.id.ivFoodImage);
            ImageView dot = (ImageView) loggerView.findViewById(R.id.ivDot);
            dot.setVisibility(View.VISIBLE);
            linearFoodLogger.addView(loggerView);
            foodImage.getLayoutParams().height = 80;
            foodImage.getLayoutParams().width = 80;
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) foodImage.getLayoutParams();
            layoutParams.setMargins(MyUtils.dpToPx(getContext(),-18),MyUtils.dpToPx(getContext(),-3),0,0);
            foodImage.setLayoutParams(layoutParams);
            foodImage.requestLayout();
            mealType.setText(foodLogger.get(i).getMeal_type().getFood_time());
            if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().contains("early morning")){
                foodImage.setImageResource(R.drawable.icon_early_morning);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("breakfast")){
                foodImage.setImageResource(R.drawable.icon_breakfast);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("mid morning")){
                foodImage.setImageResource(R.drawable.icon_midmorning);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("lunch")){
                foodImage.setImageResource(R.drawable.icon_lunch);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("teatime")){
                foodImage.setImageResource(R.drawable.icon_teatime);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("evening snacks")){
                foodImage.setImageResource(R.drawable.icon_evening_snack);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("dinner")){
                foodImage.setImageResource(R.drawable.icon_dinner);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("post dinner")){
                foodImage.setImageResource(R.drawable.icon_post_dinner);
            }else if (foodLogger.get(i).getMeal_type().getFood_time().toLowerCase().equals("late night meals")){
                foodImage.setImageResource(R.drawable.icon_late_night);
            }else{
                foodImage.setImageResource(R.drawable.food_meal_image);
            }
            mealTime.setText(MyUtils.getValidTimeForMeal(foodLogger.get(i).getStart_time())+" - "+MyUtils.getValidTimeForMeal(foodLogger.get(i).getEnd_time()));
            float totalCalories = 0;
            for (int y=0;y<foodLogger.get(i).getMeals().size();y++){
                totalCalories += foodLogger.get(i).getMeals().get(y).getComponent().getTotal_energy()*foodLogger.get(i).getMeals().get(y).getAmount();
            }
            mealCalories.setText(new DecimalFormat("##").format(totalCalories).toString()+" Cals");
            LinearLayoutManager linear1 = new LinearLayoutManager(getContext());
            linear1.setOrientation(LinearLayoutManager.VERTICAL);
            rvLogger.setLayoutManager(linear1);
            rvLogger.setHasFixedSize(false);
            FoodLoggerAdapter adapter = new FoodLoggerAdapter(getContext(),foodLogger.get(i).getMeals(),progressBar);
            rvLogger.setAdapter(adapter);
            //rvLogger.setLayoutManager(linearLayoutManager);
        }

    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        int day=   date.getDay();
        int month=   date.getMonth();
        int year=   date.getYear();
        pref.setKeyYear(year);
        pref.setKeyMonth(month);
        pref.setKeyDay(day);
        long timestamp = date.getDate().getTime()/1000L;
        progressBar.setVisibility(View.VISIBLE);
        Controller.getLogger(getContext(),timestamp,mGetFoodLoggerListener);
        long timestamp1 = date.getDate().getTime()/1000L;
        measuring_units.clear();
        adapter.notifyDataSetChanged();
        data.clear();
        mAdapter.notifyDataSetChanged();
        Log.d("dateformat", String.valueOf(timestamp1));
        progressBar.setVisibility(View.VISIBLE);
        Controller.getFoodRecommendation(getContext(),timestamp1,mRecommendationListener);
    }

    RequestListener mGetFoodLoggerListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("get food",responseObject.toString());
            Type collectionType = new TypeToken<ArrayList<RecommendationModel>>() {}.getType();
            List<RecommendationModel> da = (ArrayList<RecommendationModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            loggerModel.clear();
            for (int i=0;i<da.size();i++){
                loggerModel.add(da.get(i));
            }
            if(getActivity() == null)
                return;
            if (loggerModel.size() <= 0){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodLogged.setVisibility(View.GONE);
                    }
                });
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodLogged.setVisibility(View.VISIBLE);
                        setFoodLogger(loggerModel);
                    }
                });
            }
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("get food error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodLogged.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodLogged.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mRecommendationListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("recommended food",responseObject.toString());
            Type collectionType = new TypeToken<ArrayList<RecommendationModel>>() {}.getType();
            List<RecommendationModel> da = (ArrayList<RecommendationModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            data.clear();
            measuring_units.clear();
            for (int i=0;i<da.size();i++){
                data.add(da.get(i));
                measuring_units.add(da.get(i).getMeal_type().getFood_time());
            }
            if (getActivity() == null)
                return;
            if (data.size() <= 0){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodRecommended.setVisibility(View.GONE);
                    }
                });
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodRecommended.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("recommended food error",message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodRecommended.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        relativeFoodRecommended.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };


}
