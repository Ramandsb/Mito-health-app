package in.tagbin.mitohealthapp.Fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.DailyDetailsActivity;
import in.tagbin.mitohealthapp.adapter.ExerciseAdapter;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ExerciseLogModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import pl.droidsonroids.gif.GifImageView;

public class ExerciseFragment extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    RecyclerView recyclerExercise;
    MaterialCalendarView widget;
    PrefManager pref;
    GifImageView progressBar;
    ExerciseAdapter adapter;
    List<ExerciseLogModel> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_exercise, container, false);

        recyclerExercise = (RecyclerView) view.findViewById(R.id.rvExercise);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        progressBar = DailyDetailsActivity.progressBar;
        pref = new PrefManager(getContext());
        recyclerExercise.setItemAnimator(new SlideInLeftAnimator());
        recyclerExercise.getItemAnimator().setRemoveDuration(1000);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
        data = new ArrayList<ExerciseLogModel>();
// or recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f));
        recyclerExercise.setItemAnimator(animator);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerExercise.setLayoutManager(linearLayoutManager);
        adapter = new ExerciseAdapter(getContext(),data,progressBar);
        recyclerExercise.setAdapter(adapter);
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
        Controller.getExerciseLogger(getContext(),timestamp,mExerciseLogListener);
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


        return view;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
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
        Controller.getExerciseLogger(getContext(),timestamp,mExerciseLogListener);
        //progressBar.setVisibility(View.VISIBLE);
    }
    RequestListener mExerciseLogListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("exercise log ",responseObject.toString());
            Type collectionType = new TypeToken<ArrayList<ExerciseLogModel>>() {}.getType();
            List<ExerciseLogModel> da = (ArrayList<ExerciseLogModel>) new Gson()
                    .fromJson(responseObject.toString(), collectionType);
            data.clear();
            for (int i=0;i<da.size();i++){
                data.add(da.get(i));
            }
            if(getActivity() == null)
                return;
            if (data.size() <= 0){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerExercise.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        //linearFoodLogger.setVisibility(View.GONE);
                    }
                });
            }else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerExercise.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        //linearFoodLogger.setVisibility(View.VISIBLE);
                        //setFoodLogger(loggerModel);
                    }
                });
            }
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerExercise.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                if(getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerExercise.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}