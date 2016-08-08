package in.tagbin.mitohealthapp;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.CalenderView.CalUtil;
import in.tagbin.mitohealthapp.CalenderView.CalenderListener;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.CalenderView.WeekFragment;
import in.tagbin.mitohealthapp.Pojo.DataItems;

public class HomePage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    String user_id;
    MaterialCalendarView widget;
    RWeekCalendar rCalendarFragment;

    public static String selectedDate = "";
    SharedPreferences login_details;
    String auth_key;
    TextView cal_consumed,cal_left,cal_burned;
    TextView messageView;
    ProgressBar progressBar;
    android.app.AlertDialog alert;
    String myurl=Config.url + "logger/history/dates/";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        creatBottombar();
//        rCalendarFragment = new RWeekCalendar();
        LocalDateTime mSelectedDate = LocalDateTime.now();
        int day = mSelectedDate.getDayOfMonth();
        int month = mSelectedDate.getMonthOfYear();
        int year = mSelectedDate.getYear();
        selectedDate = year + "-" + month + "-" + day;
        widget= (MaterialCalendarView) findViewById(R.id.calendarView);
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
        food_card = (CardView) findViewById(R.id.food_card);
        water_card = (CardView) findViewById(R.id.water_card);
        exercise_card = (CardView) findViewById(R.id.exercise_card);
        sleep_card = (CardView) findViewById(R.id.sleep_card);
        plus_button = (Button) findViewById(R.id.plus_but);
        minus_button = (Button) findViewById(R.id.minus_but);
        glasses = (TextView) findViewById(R.id.glasses);
        cal_consumed = (TextView) findViewById(R.id.cal_consumed);
        cal_left = (TextView) findViewById(R.id.cal_left);
        cal_burned = (TextView) findViewById(R.id.cal_burned);
        login_details=getSharedPreferences(MainPage.LOGIN_DETAILS, MODE_PRIVATE);

        makeJsonObjGETReq();

        int water_amount=   login_details.getInt("water_amount",0);
        int food_cal=   login_details.getInt("food_cal",0);
        int calorie_burnt=   login_details.getInt("calorie_burnt",0);
        int total_calorie_required=   login_details.getInt("total_calorie_required",0);
        cal_consumed.setText(food_cal);
        Float left= Float.valueOf(total_calorie_required)-Float.valueOf(food_cal);
        cal_left.setText(food_cal+"/"+total_calorie_required);
        cal_burned.setText(calorie_burnt);
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
        customDialog();
        food_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePage.this,CollapsableLogging.class);
                intent.putExtra("selection",0);
                startActivity(intent);
            }
        });
        water_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePage.this,CollapsableLogging.class);
                intent.putExtra("selection",1);
                startActivity(intent);
            }
        });
        exercise_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePage.this,CollapsableLogging.class);
                intent.putExtra("selection",2);
                startActivity(intent);
            }
        });
        sleep_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomePage.this,CollapsableLogging.class);
                intent.putExtra("selection",3);
                startActivity(intent);
            }
        });
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

    private void makeJsonObjGETReq() {

        Map<String, String> postParam = new HashMap<String, String>();
        final SharedPreferences.Editor editor= login_details.edit();

        auth_key=   login_details.getString("key","");
        user_id=   login_details.getString("user_id","");
        Log.d("details", user_id + "//" + auth_key);
        /**
         *  "first_name": "Nairitya",
         "last_name": "Khilari",
         "email": "nairitya@gmail.com",
         "phone_number": "4512356578",
         "weight": 66,
         "waist": 35,
         "height": 179,
         "dob": "1994-12-18"
         */

//
//        postParam.put("gender", sex);
//        postParam.put("dob", dob);
//        postParam.put("height", height);
//        postParam.put("waist", waist);
//        postParam.put("weight", weight);

//
//        JSONObject jsonObject = new JSONObject(postParam);
//        Log.d("postpar", jsonObject.toString());
//


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Config.url + "users/" + user_id + "/", null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject res) {
                        Log.d("response", res.toString());

                        try {

                            dismissDialog();
                            JSONObject obj = res.getJSONObject("user");
                          String  username = obj.getString("username");
                          String  first_name = obj.getString("first_name");
                           String last_name = obj.getString("last_name");
                          String   email = obj.getString("email");
                            JSONObject profile = res.getJSONObject("profile");
                            String dob = profile.getString("dob");
                            String gender = profile.getString("gender");
                            String height = profile.getString("height");
                            String weight = profile.getString("weight");
                            String waist = profile.getString("waist");


                            int wei=Integer.valueOf(weight);
                            int  grams= wei%1000;
                            int kg =wei/1000;
                            int hei=Integer.valueOf(height);
                            int  fee= hei/12;
                            int inv =wei%1000;

                            editor.putString("weight",weight);
                            editor.putString("waist",waist);
                            editor.putString("height",height);
                            editor.putString("gender",gender);
                            editor.putString("dob",dob);
                            editor.commit();


                            try {




//                                JSONObject images=     res.getJSONObject("images");
//                                String master=   images.getString("master");
//                                editor.putString("master_image",master);
//

                                JSONArray energy=    res.getJSONArray("energy");
                                Log.d("energy details",energy.toString());

                                int[] energyi=new int[5];
                                for (int i =0;i<energy.length();i++){

                                    energyi[i]=energy.getInt(i);

                                    Log.d("energy val",energyi[i]+"");
                                }
                                editor.putInt("water_amount",energyi[1]);
                                editor.putInt("food_cal",energyi[2]);
                                editor.putInt("calorie_burnt",energyi[3]);
                                editor.putInt("total_calorie_required",energyi[4]);
                                Log.d("energy details",energyi[1]+"///"+energyi[2]+"///"+energyi[3]+"///"+energyi[4]+"///");

                                JSONObject user =   res.getJSONObject("user");
                                String user_username=   user.getString("username");
                                String user_first_name=   user.getString("first_name");
                                String user_last_name=  user.getString("last_name");
                                String user_email=   user.getString("email");

                                editor.putString("user_username",user_username);
                                editor.putString("user_first_name",user_first_name);
                                editor.putString("user_last_name",user_last_name);
                                editor.putString("user_email",user_email);

                                editor.commit();
                                Log.d("all details",login_details.getAll().toString())   ;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                displayErrors(error);

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
//


        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }
    public void creatBottombar(){
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("",R.drawable.big_partner);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("",R.drawable.big_profile);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.big_mito);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.big_cart);

// Add itemsF63D2B
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#ffffff"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

// Change colors
//        bottomNavigation.setAccentColor(R.color.colorAccent);
//        bottomNavigation.setInactiveColor(R.color.sample_bg);


// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);
//        bottomNavigation.setInactiveColor(getResources().getColor(R.color.bottombar));
        bottomNavigation.setAccentColor(getResources().getColor(R.color.bottombar));

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(false);

// Set current item programmatically
        bottomNavigation.setCurrentItem(2);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.bottombar));

// Add or remove notification for each item68822836
        bottomNavigation.setNotification("4", 1);
        bottomNavigation.setNotification("", 1);

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                if (position==2){
                    startActivity(new Intent(HomePage.this,HomePage.class));
                }
                if (position==1){
                    startActivity(new Intent(HomePage.this,ProfilePage.class));
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


    }
    private void makeJsonObjReq() {

        auth_key=   login_details.getString("key", "");
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

    public  String convertTimestamp(String selectedDate) throws ParseException {

        String str_date="2016-08-04 15:15:15";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = (Date)formatter.parse(selectedDate+" 15:15:15");
       String timestamp=String.valueOf(date.getTime()/1000);
        Log.d("converted date",timestamp);



        return timestamp;
    }
    private void makeJsonObjReq(String start,String end) {

        auth_key=   login_details.getString("key", "");
        Map<String, String> postParam = new HashMap<String, String>();

        Log.d("check auth",auth_key);

        postParam.put("start", start);
        postParam.put("end", end);




        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                myurl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());







                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());



                Log.d("error", error.toString()+"//////////"+error.getMessage());
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
        selectedDate=year+"-0"+month+"-0"+day;
        Log.d("date",selectedDate);
//        String selectedDate=year+"-"+"0"+day+"-"+"0"+month;

        int nextdays=day+7;
         try {
             String start_date=convertTimestamp(selectedDate);
             String end_date=convertTimestamp(year+"-0"+month+"-0"+nextdays);
makeJsonObjReq(start_date,end_date);
         } catch (ParseException e) {
                e.printStackTrace();

        }
    }
    public void customDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }

    public void showDialog() {

        progressBar.setVisibility(View.VISIBLE);
        alert.show();
        messageView.setText("Loading");
    }

    public void dismissDialog() {
        alert.dismiss();
    }

    public void displayErrors(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Connection failed");
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("AuthFailureError");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ServerError");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }
}
