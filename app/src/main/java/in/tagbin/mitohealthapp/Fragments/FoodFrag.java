package in.tagbin.mitohealthapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.tagbin.mitohealthapp.AppController;
import in.tagbin.mitohealthapp.FoodDetails;
import in.tagbin.mitohealthapp.Interfaces.ExerciseInterface;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.CollapsableLogging;
import in.tagbin.mitohealthapp.Config;
import in.tagbin.mitohealthapp.CustomAdapter;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Interfaces.FoodInterface;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.ItemClickSupport;
import in.tagbin.mitohealthapp.MainPage;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.StickyHeaders.exposed.StickyLayoutManager;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.RecommendationAdapter;
import in.tagbin.mitohealthapp.model.DataObject;
import in.tagbin.mitohealthapp.model.DateRangeDataModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class FoodFrag extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    MaterialCalendarView widget;
    Spinner spinner;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvRecommendations;
    RecommendationAdapter mAdapter;
    LinearLayout tvTopRecommended,linearFoodLogger;
    List<RecommendationModel> data;
    List<String> measuring_units;
    int hour1,minute1;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_food, container, false);
        rvRecommendations = (RecyclerView) fragView.findViewById(R.id.rvRecommendation);
        spinner = (Spinner) fragView.findViewById(R.id.spinnerRecommended);
        tvTopRecommended = (LinearLayout) fragView.findViewById(R.id.linearTopRecommended);
        linearFoodLogger = (LinearLayout) fragView.findViewById(R.id.linearFoodLogger);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecommendations.setLayoutManager(linearLayoutManager);
        rvRecommendations.setHasFixedSize(true);
        //food_list = (RecyclerView) fragView.findViewById(R.id.food_list);
        widget= (MaterialCalendarView) fragView.findViewById(R.id.calendarView);
        data = new ArrayList<RecommendationModel>();

        Calendar calendar = Calendar.getInstance();
        hour1 = calendar.get(Calendar.HOUR);
        minute1 = calendar.get(Calendar.MINUTE);
        widget.setSelectedDate(calendar.getTime());
        Date date1 = calendar.getTime();
        long timestamp = date1.getTime()/1000L;
        Controller.getLogger(getContext(),timestamp,mGetFoodLoggerListener);
        String dateFormat = MyUtils.getDateFormat(date1,"yyyy-MM-dd");
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
                .commit();
        measuring_units = new ArrayList<>();
//        measuring_units.add("Breakfast");
//        measuring_units.add("Lunch");
//        measuring_units.add("Dinner");
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        final String[] unit = {"Kg"};
        mAdapter = new RecommendationAdapter(getContext(), data, "",0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(measuring_units.size() >0 && data.size() > 0) {
                    Log.d("item selected", measuring_units.get(i));
                    unit[0] = measuring_units.get(i);
                    mAdapter = new RecommendationAdapter(getContext(), data, measuring_units.get(i),i);
                    rvRecommendations.setAdapter(mAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //////////////////


//        ItemClickSupport.addTo(food_list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//
//             DataItems dataItems=   dop.getInformation(dop,selectedDate).get(position);
//                startActivity(new Intent(getActivity(), FoodDetails.class).putExtra("food_id",dataItems.getFood_id()).putExtra("source","food_frag"));
//
//
////
//// akshayluthra12@
//
//            }
//        });

        return fragView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, monthOfYear, dayOfMonth);
        //Sets the selected date from Picker
    }

    public void setFoodLogger(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View loggerView = inflater.inflate(R.layout.item_food_logger,linearFoodLogger,false);
        TextView mealType = (TextView) loggerView.findViewById(R.id.tvRecommendedMealType);
        TextView mealTime = (TextView) loggerView.findViewById(R.id.tvRecommendedMealTime);
        TextView mealCalories = (TextView) loggerView.findViewById(R.id.tvRecommendedMealTotalCalories);
        RecyclerView rvLogger = (RecyclerView) loggerView.findViewById(R.id.rvFoodLogger);

    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        Calendar calendar = date.getCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH)+1;
        int hours = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        Date date1 = new Date(year-1900,month,day,hours,minute);
//        Date date1 = date.g;
//        //date1.setTime(43200000);
//        date1.setDate(date1.getDay()+1);
        long timestamp = date1.getTime()/1000L;
        Log.d("timestamp",String.valueOf(timestamp));
        Controller.getLogger(getContext(),timestamp,mGetFoodLoggerListener);

        long timestamp1 = date.getDate().getTime()/1000L;
        Log.d("dateformat", String.valueOf(timestamp1));
        Controller.getFoodRecommendation(getContext(),timestamp1,mRecommendationListener);
//        if (month<=9 && day <=9){
//            selectedDate = year + "-" + "0"+month + "-" + "0"+day;
//            Log.d("date",selectedDate);
//        }else  if (month<=9 && day >9){
//            selectedDate = year + "-" + "0"+month + "-" + day;
//            Log.d("date",selectedDate);
//        }else  if (day <=9 && month >9){
//            selectedDate = year + "-" +month + "-" + "0"+day;
//            Log.d("date",selectedDate);
//        }else if (day >9 && month >9){
//            selectedDate = year + "-" + month + "-" + day;
//            Log.d("date", selectedDate);
//        }
//        makeJsonArrayReq(selectedDate);
//        database_list = dop.getInformation(dop, selectedDate);
//        customAdapter.setData(database_list);
//        customAdapter.notifyDataSetChanged();
//        Log.d("date",selectedDate);
    }

    RequestListener mGetFoodLoggerListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("get food",responseObject.toString());

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("get food error",message);
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
            if (data.size() <= 0){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTopRecommended.setVisibility(View.GONE);
                    }
                });
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTopRecommended.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("recommended food error",message);
        }
    };


}
