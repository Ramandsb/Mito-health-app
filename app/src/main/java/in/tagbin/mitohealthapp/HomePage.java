package in.tagbin.mitohealthapp;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDateTime;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.CalenderView.CalUtil;
import in.tagbin.mitohealthapp.CalenderView.CalenderListener;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.CalenderView.WeekFragment;
import in.tagbin.mitohealthapp.Pojo.DataItems;

public class HomePage extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    RWeekCalendar rCalendarFragment;

    public static String selectedDate = "";
    SharedPreferences login_details;
    String auth_key;
    TextView cal_consumed,cal_left,cal_burned;

    int a = 0, b = 0, c = 0;
    int mBgColor = 0;
    android.support.v7.widget.CardView food_card;
    android.support.v7.widget.CardView water_card;
    android.support.v7.widget.CardView exercise_card;
    android.support.v7.widget.CardView sleep_card;
    public static boolean flagHome = false;
    public static String firstdate = "";
    Button plus_button,minus_button;
    TextView glasses;
    int j =4;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PREPDUG","Setting options true");
        InitActivity i = (InitActivity) getActivity();
        setHasOptionsMenu(true);
        i.invalidateOptionsMenu();




//        rCalendarFragment = new RWeekCalendar();

//        CalenderListener listener = new CalenderListener() {
//            @Override
//            public void onSelectPicker() {
//
//                //User can use any type of pickers here the below picker is only Just a example
//
////                DatePickerDialog.newInstance(Sample.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
//
//
//            }
//
//            @Override
//            public void onSelectDate(LocalDateTime mSelectedDate) {
//
//                //callback when a date is selcted
//
//                a = mSelectedDate.getDayOfMonth();
//                b = mSelectedDate.getMonthOfYear();
//                c = mSelectedDate.getYear();
////                 + mSelectedDate.getDayOfMonth() + "-" + mSelectedDate.getMonthOfYear() + "-" + mSelectedDate.getYear());
////                mDateSelectedTv.setText(""+mSelectedDate.getDayOfMonth()+"-"+mSelectedDate.getMonthOfYear()+"-"+mSelectedDate.getYear());
//
////                if (flagHome){
////                    selectedDate=a+":"+b+":"+c;
////                    Log.d("true", "///////////////////////" + selectedDate+"/////////////");
////
////                }else {
////                    firstdate=a+":"+b+":"+c;
//////                    flagHome=true;
////                    Log.d("Home_else_firstdate", "" + firstdate+"$$$$$$$$$$$$$$$$$$$$$");
////                   ///////////////////////////////////////////////////
////                    /**
////                     * breakthrough : use boolean to detect the touch events make bool true when clicked in weekFragment
////                     * and check other wise ignore all values
////                     */
////
////                }
//                if (WeekFragment.click_hppn.equals("yes")) {
//                    selectedDate = c + "-" + b + "-" + a;
//                    Log.d("Click_happend", "-------------" + selectedDate + "----------------");
//                    if (a==1){
//
//                        cal_consumed.setText("240");
//                        cal_burned.setText("659");
//                        cal_left.setText("1330");
//                    }else if (a==2){
//
//                        cal_consumed.setText("1456");
//                        cal_burned.setText("900");
//                        cal_left.setText("0070");
//                    }else if (a==3){
//                        cal_consumed.setText("780");
//                        cal_burned.setText("400");
//                        cal_left.setText("0700");
//
//                    }else if (a==4){
//
//                        cal_consumed.setText("700");
//                        cal_burned.setText("1200");
//                        cal_left.setText("800");
//                    }
//                    WeekFragment.click_hppn = "no";
//                }
//
//
//            }
//        };
//
//        //setting the listener
//        rCalendarFragment.setCalenderListener(listener);
//
//
//        Bundle args = new Bundle();
//
//       /*Should add this attribute if you adding  the NOW_BACKGROUND or DATE_SELECTOR_BACKGROUND Attribute*/
//        args.putString(RWeekCalendar.PACKAGENAME, getApplicationContext().getPackageName());
//
//       /* IMPORTANT: Customization for the calender commenting or un commenting any of the attribute below will reflect change in calender*/
//
////---------------------------------------------------------------------------------------------------------------------//
//
////      args.putInt(RWeekCalender.CALENDER_BACKGROUND, ContextCompat.getColor(this,R.color.md_pink_700));//set background color to calender
//
//        args.putString(RWeekCalendar.DATE_SELECTOR_BACKGROUND, "bg_select");//set background to the selected dates
//
//        args.putInt(RWeekCalendar.WEEKCOUNT, 1000);//add N weeks from the current week (53 or 52 week is one year)
//
////        args.putString(RWeekCalender.NOW_BACKGROUND,"bg_now");//set background to nowView
//
//        args.putInt(RWeekCalendar.CURRENT_DATE_BACKGROUND, ContextCompat.getColor(this, R.color.md_black_1000));//set color to the currentdate
//
////        args.putInt(RWeekCalender.PRIMARY_BACKGROUND, ContextCompat.getColor(this,R.color.md_white_1000));//Set color to the primary views (Month name and dates)
//
////        args.putInt(RWeekCalender.SECONDARY_BACKGROUND, ContextCompat.getColor(this,R.color.md_green_500));//Set color to the secondary views (now view and week names)
//
////---------------------------------------------------------------------------------------------------------------------//
//
//        rCalendarFragment.setArguments(args);
//
//        // Attach to the activity
////        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
////        t.replace(R.id.container, rCalendarFragment);
////        t.commit();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.content_home_page,container,false);
        LocalDateTime mSelectedDate = LocalDateTime.now();
        int day = mSelectedDate.getDayOfMonth();
        int month = mSelectedDate.getMonthOfYear();
        int year = mSelectedDate.getYear();
        selectedDate = year + "-" + month + "-" + day;
        widget= (MaterialCalendarView) v.findViewById(R.id.calendarView);
        // Add a decorator to disable prime numbered days

        Log.d("start Date",selectedDate);
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
                .setFirstDayOfWeek(Calendar.MONDAY)
                .commit();


        mBgColor = getResources().getColor(R.color.sample_bg);
        food_card = (CardView) v.findViewById(R.id.food_card);
        water_card = (CardView) v.findViewById(R.id.water_card);
        exercise_card = (CardView) v.findViewById(R.id.exercise_card);
        sleep_card = (CardView) v.findViewById(R.id.sleep_card);
        plus_button = (Button) v.findViewById(R.id.plus_but);
        minus_button = (Button) v.findViewById(R.id.minus_but);
        glasses = (TextView) v.findViewById(R.id.glasses);
        cal_consumed = (TextView) v.findViewById(R.id.cal_consumed);
        cal_left = (TextView) v.findViewById(R.id.cal_left);
        cal_burned = (TextView) v.findViewById(R.id.cal_burned);
        login_details=getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, Context.MODE_PRIVATE);
        plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j <=9 && j>=2) {
                    j++;
                    glasses.setText(j+"/9");
                    makeJsonObjReq();
                }

                if (j==1){
                    j++;
                    glasses.setText(j+"/9");
                    makeJsonObjReq();
                }



            }
        });
        minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (j < 10 && j>=2) {
                    j--;
                    glasses.setText(j+"/9");
                    makeJsonObjReq();
                }

            }
        });
        food_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),CollapsableLogging.class);
                intent.putExtra("selection",0);
                startActivity(intent);
            }
        });
        water_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),CollapsableLogging.class);
                intent.putExtra("selection",1);
                startActivity(intent);
            }
        });
        exercise_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),CollapsableLogging.class);
                intent.putExtra("selection",2);
                startActivity(intent);
            }
        });
        sleep_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),CollapsableLogging.class);
                intent.putExtra("selection",3);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereHOME");
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
        menu.findItem(R.id.action_done).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(true);
        //InitActivity.toolbar.setTitle("Mito");
        super.onPrepareOptionsMenu(menu);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getActivity().getWindow().setEnterTransition(fade);
    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, monthOfYear, dayOfMonth);
        rCalendarFragment.setDateWeek(calendar);
        //Sets the selected date from Picker
    }
    private void makeJsonObjReq() {

        auth_key=   login_details.getString("auth_key", "");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("ltype", "water");
        postParam.put("c_id", "1");




        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.url+"logger/", jsonObject,
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
                headers.put( "charset", "utf-8");
                headers.put("Authorization","JWT "+auth_key);
                return headers;
            }



        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        int day=   date.getDay();
        int month=   date.getMonth()+1;
        int year=   date.getYear();
        selectedDate=year+"-"+month+"-"+day;
        Log.d("date",getSelectedDatesString());
    }
}
