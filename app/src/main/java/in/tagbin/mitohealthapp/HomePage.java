package in.tagbin.mitohealthapp;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.DateRangeDataModel;

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
    String last7Days="";
    String next7Days="";
    String myurl=Config.url + "logger/history/dates/";

    int a = 0, b = 0, c = 0;
    int mBgColor = 0;
    android.support.v7.widget.CardView food_card;
    android.support.v7.widget.CardView water_card;
    android.support.v7.widget.CardView exercise_card;
    android.support.v7.widget.CardView sleep_card;
    android.support.v7.widget.CardView feelings_card;
    public static boolean flagHome = false;
    public static String firstdate = "";
    Button plus_button,minus_button;
    TextView glasses,foodcard_recom,exercard_burnt;
    int j =4;
    DatabaseOperations dop;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("PREPDUG", "Setting options true");
        BinderActivity i = (BinderActivity) getActivity();
        setHasOptionsMenu(true);
        i.invalidateOptionsMenu();
        dop = new DatabaseOperations(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.content_home_page,container,false);
        Calendar  calendar = Calendar.getInstance();
        customDialog();
        int  year = calendar.get(Calendar.YEAR);
        int  month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar  current_cal = Calendar.getInstance();
        Date currentDate = current_cal.getTime();
        selectedDate=sdf.format(currentDate);
     //////////////////////////////////////////
        Calendar  p_cal = Calendar.getInstance();
        p_cal.add(Calendar.DATE,-7);
        Date p_Date = p_cal.getTime();
        last7Days=sdf.format(p_Date);
        ///////////////////////////////////////////
        Calendar  n_cal = Calendar.getInstance();
        n_cal.add(Calendar.DATE,+2);
        Date n_Date = n_cal.getTime();
        next7Days=sdf.format(n_Date);




        Log.d("last 7 date",last7Days);
        Log.d("normal date",selectedDate);
        Log.d("next 7 date",next7Days);



        Controller.getDateRangeData(getActivity(),String.valueOf(MyUtils.getUtcTimestamp(last7Days+" 00:00:00","s")),String.valueOf(MyUtils.getUtcTimestamp(next7Days+" 00:00:00","s")),mDateRangelistener);
        widget= (MaterialCalendarView) v.findViewById(R.id.calendarView);
        // Add a decorator to disable prime numbered days

        Log.d("start Date",MyUtils.getUtcTimestamp(last7Days+" 00:00:00","s")+"////"+MyUtils.getUtcTimestamp(next7Days+" 00:00:00","s"));

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
        feelings_card = (CardView) v.findViewById(R.id.feelings_card);
        plus_button = (Button) v.findViewById(R.id.plus_but);
        minus_button = (Button) v.findViewById(R.id.minus_but);
        glasses = (TextView) v.findViewById(R.id.glasses);
        foodcard_recom = (TextView) v.findViewById(R.id.foodcard_recomended);
        exercard_burnt = (TextView) v.findViewById(R.id.exercard_burnt);
        cal_consumed = (TextView) v.findViewById(R.id.cal_consumed);
        cal_left = (TextView) v.findViewById(R.id.cal_left);
        cal_burned = (TextView) v.findViewById(R.id.cal_burned);
        login_details=getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, Context.MODE_PRIVATE);

////        makeJsonObjGETReq();
//            String water_amount=   login_details.getString("water_amount","");
//            String food_cal=   login_details.getString("food_cal","");
//            String calorie_burnt=   login_details.getString("calorie_burnt","");
//            String total_calorie_required=   login_details.getString("total_calorie_required","");
//
////            Float left= Float.valueOf(total_calorie_required)-Float.valueOf(food_cal);
//            if (water_amount.equals("") ||food_cal.equals("")||calorie_burnt.equals("")||total_calorie_required.equals("")){
//
//            }else {
//                double wat_int=(Double.valueOf(water_amount));
//                int i=Integer.valueOf((int) wat_int);
//                double wat_int1=(Double.valueOf(food_cal));
//                int j=Integer.valueOf((int) wat_int1);
//                double wat_int2=(Double.valueOf(calorie_burnt));
//                int k=Integer.valueOf((int) wat_int2);
//                double wat_int3=(Double.valueOf(total_calorie_required));
//                int l=Integer.valueOf((int) wat_int3);
//                int fcal_int=j;
//                int calbur_int=k;
//                int totcal_int=l;
////                cal_consumed.setText(fcal_int+"");
////                cal_left.setText(fcal_int+"/"+totcal_int+"");
////                cal_burned.setText(calbur_int+"");
//
//            }


        setHomepageDetails();

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
        feelings_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),CollapsableLogging.class);
                intent.putExtra("selection",4);
                startActivity(intent);
            }
        });

        return v;
    }

    private void setHomepageDetails() {

        Cursor cursor =dop.getCompleteChartInfo(dop,selectedDate);
        if (cursor != null && cursor.moveToFirst()) {
            do {
  String      f_con=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_FOODCAL_CONSUMED));
  String   e_bur=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_EXERCAL_BURNED));
  String  water=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_WATER_CONSUMED));
  String    f_req=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_FOODCAL_REQ));
                double wat_int=(Double.valueOf(water));
                int i=Integer.valueOf((int) wat_int);
                double wat_int1=(Double.valueOf(f_con));
                int j=Integer.valueOf((int) wat_int1);
                double wat_int2=(Double.valueOf(e_bur));
                int k=Integer.valueOf((int) wat_int2);
                double wat_int3=(Double.valueOf(f_req));
                int l=Integer.valueOf((int) wat_int3);
                int fcal_int=j;
                int calbur_int=k;
                int totcal_int=l;
                cal_consumed.setText(fcal_int+"");
                cal_left.setText(fcal_int+"/"+totcal_int);
                cal_burned.setText(calbur_int+"");
                String w= String.valueOf(i/100);
                glasses.setText(w+"/8");
                foodcard_recom.setText(fcal_int + " Kcal");
                exercard_burnt.setText(calbur_int+"");
            }
            while (cursor.moveToNext());
        }

    }

    RequestListener mDateRangelistener= new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("dateRange Request",responseObject.toString());

//            DateRangeDataModel dateRangeDataModel = JsonUtils.objectify(responseObject.toString(),DateRangeDataModel.class);

//            dateRangeDataModel.getChart().get(0).

            try {

                dop.ClearChartTable(dop);

                JSONObject object = new JSONObject(responseObject.toString());
                JSONArray chart = object.getJSONArray("chart");
                for (int i = 0; i < chart.length(); i++) {
                    JSONObject obj = chart.getJSONObject(i);
                    String water = obj.getString("water");
                    String date = obj.getString("date");
                    String fcal_con = obj.getString("calorie_consumed");
                    String fcal_req = obj.getString("calorie_required");
                    String exer_bur = obj.getString("calorie_burnt");
                    long  time= MyUtils.getUtcTimestamp(date,"m");

                    Log.d("check response prob",water+date+""+fcal_con+fcal_req+exer_bur);
                    dop.putChartInformation(dop, date, String.valueOf(time), fcal_req, fcal_con, "2000", water, "100", exer_bur, "8", "6");

                    int count =dop.getCount(dop, TableData.Tableinfo.TABLE_NAME_CHART, TableData.Tableinfo.CHART_DATE,selectedDate+" 00:00:00");

//                  if (count==0){
//                      dop.putChartInformation(dop, date, String.valueOf(time), fcal_req, fcal_con, "8", water, "100", exer_bur, "8", "6");
//                      Log.d("count with put",count+"/////");
//                  }else {
//                      Log.d("count",count+"///  count without");
//                  }

                }
            }catch (Exception e){

                Log.d("exception",e.toString());

            }


        }

        @Override
        public void onRequestError(int errorCode, String message) {

        }
    };

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereHOME");
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
//        menu.findItem(R.id.action_done).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
//                .setVisible(true);
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


    public  String convertTimestamp(String selectedDate) throws ParseException {


        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = (Date)formatter.parse(selectedDate+" 00:00:00");
       String timestamp=String.valueOf(date.getTime());
        Log.d("converted date",timestamp);



        return timestamp;
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
        setHomepageDetails();
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
