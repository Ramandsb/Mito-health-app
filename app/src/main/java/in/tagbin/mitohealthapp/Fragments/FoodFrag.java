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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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
import in.tagbin.mitohealthapp.ItemClickSupport;
import in.tagbin.mitohealthapp.MainPage;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.StickyHeaders.exposed.StickyLayoutManager;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.DateRangeDataModel;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class FoodFrag extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    ArrayList<DataItems> database_list;
    RecyclerView food_list;
    CustomAdapter customAdapter;
    DatabaseOperations dop;
    SharedPreferences login_details;
    String auth_key;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    public static String selectedDate = "";



    public FoodFrag() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dop = new DatabaseOperations(getActivity());
        login_details = getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, getActivity().MODE_PRIVATE);
      Calendar  calendar = Calendar.getInstance();

      int  year = calendar.get(Calendar.YEAR);
      int  month = calendar.get(Calendar.MONTH);
       int day = calendar.get(Calendar.DAY_OF_MONTH);
        month = month+1;
        if (month<=9 && day <=9){
            selectedDate = year + "-" + "0"+month + "-" + "0"+day;
            Log.d("date",selectedDate);
        }else  if (month<=9 && day >9){
            selectedDate = year + "-" + "0"+month + "-" + day;
            Log.d("date",selectedDate);
        }else  if (day <=9 && month >9){
            selectedDate = year + "-" +month + "-" + "0"+day;
            Log.d("date",selectedDate);
        }else if (day >9 && month >9){
            selectedDate = year + "-" + month + "-" + day;
            Log.d("date", selectedDate);

        }
        makeJsonArrayReq(selectedDate);
        Log.d("date",selectedDate);
       String dateTimeStamp=String.valueOf(MyUtils.getUtcTimestamp(selectedDate+" 00:00:00","s"));
//        dop.putFeelingsInformation(dop, dateTimeStamp,"0.0","0.0","0.0","0.0","0.0","no");


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_food, container, false);

        food_list = (RecyclerView) fragView.findViewById(R.id.food_list);
        widget= (MaterialCalendarView) fragView.findViewById(R.id.calendarView);
        food_list.setItemAnimator(new SlideInLeftAnimator());
        food_list.getItemAnimator().setRemoveDuration(1000);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
// or recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f));
        food_list.setItemAnimator(animator);
        getAllDataforchart();

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        customAdapter = new CustomAdapter(getActivity());
        StickyLayoutManager layoutManager = new StickyLayoutManager(getContext(), customAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders(10);
        food_list.setLayoutManager(layoutManager);
        database_list = new ArrayList<>();
        dop = new DatabaseOperations(getActivity());


        food_list.setAdapter(customAdapter);
        food_list.setHasFixedSize(true);
        database_list = dop.getInformation(dop, selectedDate);
        customAdapter.setData(database_list);
        customAdapter.notifyDataSetChanged();
/////////////////////

        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar.getTime());

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

        //////////////////


        ItemClickSupport.addTo(food_list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

             DataItems dataItems=   dop.getInformation(dop,selectedDate).get(position);
                startActivity(new Intent(getActivity(), FoodDetails.class).putExtra("food_id",dataItems.getFood_id()).putExtra("source","food_frag"));


//
// akshayluthra12@

            }
        });
        if (database_list.isEmpty()) {

        } else {

        }
        Log.d("final selected date", selectedDate);

        return fragView;
    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, monthOfYear, dayOfMonth);
        //Sets the selected date from Picker
    }

    private void makeJsonObjReq(String date) {

        auth_key = login_details.getString("key", "");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("day", date);


        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Config.url + "logger/food/", jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());


                Log.d("error", error.toString());
            }
        }) {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", "JWT " + auth_key);
                return headers;
            }


        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }



    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

//
     int day=   date.getDay();
     int month=   date.getMonth()+1;
     int year=   date.getYear();
        if (month<=9 && day <=9){
            selectedDate = year + "-" + "0"+month + "-" + "0"+day;
            Log.d("date",selectedDate);
        }else  if (month<=9 && day >9){
            selectedDate = year + "-" + "0"+month + "-" + day;
            Log.d("date",selectedDate);
        }else  if (day <=9 && month >9){
            selectedDate = year + "-" +month + "-" + "0"+day;
            Log.d("date",selectedDate);
        }else if (day >9 && month >9){
            selectedDate = year + "-" + month + "-" + day;
            Log.d("date", selectedDate);
        }
        makeJsonArrayReq(selectedDate);
        database_list = dop.getInformation(dop, selectedDate);
        customAdapter.setData(database_list);
        customAdapter.notifyDataSetChanged();
        Log.d("date",selectedDate);
    }
    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
    private void makeJsonArrayReq(String s) {
        auth_key = login_details.getString("key", "");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("day", s);


        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Config.url + "logger/food/?day="+s,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("ArrayRequest Response",response.toString());



                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());

                Log.d("error", error.toString());
            }
        })
        {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("Authorization", "JWT " + auth_key);
                return headers;
            }


//


        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void getAllDataforchart(){

        ArrayList<DateRangeDataModel> list= new ArrayList<>();
        DateRangeDataModel dm= new DateRangeDataModel();
      list= dop.getChartInformation(dop,selectedDate+" 00:00:00");
        for (int i=0;i<list.size();i++){
            dm=list.get(i);
            Log.d("detailsfromdb",dm.getDate()+"//"+dm.getFood_req()+"//"+dm.getFood_con()+"//"+dm.getExer_con()+"//"+dm.getTimestamp()+"//"+dm.getWater_con());
        }



    }

}
