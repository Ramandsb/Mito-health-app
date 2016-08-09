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

public class HomePage extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
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
    TextView glasses,foodcard_recom,exercard_burnt;
    int j =4;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PREPDUG","Setting options true");
        InitActivity i = (InitActivity) getActivity();
        setHasOptionsMenu(true);
        i.invalidateOptionsMenu();

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

                                String[] energyi=new String[5];
                                for (int i =0;i<energy.length();i++){

                                    energyi[i]=energy.get(i).toString();

                                    Log.d("energy val",energyi[i]);
                                }
                                editor.putString("water_amount",energyi[1]);
                                editor.putString("food_cal",energyi[2]);
                                editor.putString("calorie_burnt",energyi[3]);
                                editor.putString("total_calorie_required",energyi[4]);
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



// Create items

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

        makeJsonObjGETReq();

            String water_amount=   login_details.getString("water_amount","");
            String food_cal=   login_details.getString("food_cal","");
            String calorie_burnt=   login_details.getString("calorie_burnt","");
            String total_calorie_required=   login_details.getString("total_calorie_required","");

//            Float left= Float.valueOf(total_calorie_required)-Float.valueOf(food_cal);
            if (water_amount.equals("") ||food_cal.equals("")||calorie_burnt.equals("")||total_calorie_required.equals("")){

            }else {
                double wat_int=(Double.valueOf(water_amount));
                int i=Integer.valueOf((int) wat_int);
                double wat_int1=(Double.valueOf(food_cal));
                int j=Integer.valueOf((int) wat_int1);
                double wat_int2=(Double.valueOf(calorie_burnt));
                int k=Integer.valueOf((int) wat_int2);
                double wat_int3=(Double.valueOf(total_calorie_required));
                int l=Integer.valueOf((int) wat_int3);
                int fcal_int=j;
                int calbur_int=k;
                int totcal_int=l;
                cal_consumed.setText(fcal_int+"");
                cal_left.setText(fcal_int+"/"+totcal_int+"");
                cal_burned.setText(calbur_int+"");
//            foodcard_recom.setText(totcal_int+"");
//            exercard_burnt.setText(calbur_int);
            }


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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

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
