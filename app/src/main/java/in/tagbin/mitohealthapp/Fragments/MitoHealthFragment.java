package in.tagbin.mitohealthapp.Fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.tagbin.mitohealthapp.activity.EventDetailsActivity;
import in.tagbin.mitohealthapp.helper.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.BinderActivity;
import in.tagbin.mitohealthapp.activity.DailyDetailsActivity;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.Config;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.EnergyGetModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;

public class MitoHealthFragment extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    String user_id;
    MaterialCalendarView widget;
    RWeekCalendar rCalendarFragment;

    public static String selectedDate = "";
    SharedPreferences login_details;
    String auth_key;
    TextView cal_consumed,cal_left,cal_burned,coins;
    TextView messageView;
    ProgressBar progressBar;
    android.app.AlertDialog alert;
    String last7Days="";
    String next7Days="";
    String myurl= Config.url + "logger/history/dates/";
    PrefManager pref;
    int a = 0, b = 0, c = 0,coinsFinal = 0;
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
        View v = inflater.inflate(R.layout.frag_mito_health,container,false);
        pref=  new PrefManager(getContext());
        Calendar  calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar  current_cal = Calendar.getInstance();
        Date currentDate = current_cal.getTime();
        Log.d("weekValue",current_cal.get(Calendar.DAY_OF_WEEK)+"///");
        selectedDate=sdf.format(currentDate);

        Calendar calendar1 = Calendar.getInstance();
        int day = calendar1.get(Calendar.DAY_OF_MONTH);
        int year = calendar1.get(Calendar.YEAR);
        int month = calendar1.get(Calendar.MONTH);
        pref.setKeyDay(day);
        pref.setKeyMonth(month);
        pref.setKeyYear(year);
        //widget.setSelectedDate(calendar1.getTime());
        Date date1 = new Date(year-1900,month,day,0,0);
        long timestamp = date1.getTime()/1000L;
        //progressBar.setVisibility(View.VISIBLE);
        Log.d("timestamop",""+timestamp);
        Calendar[] dates = new Calendar[7];
        int i = 0;
        while (i < 7){
            Calendar selDate = Calendar.getInstance();
            selDate.add(Calendar.DAY_OF_MONTH, -i);
            dates[i] = selDate;
            i++;
        }
        Calendar startCalendar = dates[6];
        int dayStart = startCalendar.get(Calendar.DAY_OF_MONTH);
        int monthStart = startCalendar.get(Calendar.MONTH);
        int yearStart = startCalendar.get(Calendar.YEAR);
        Date dateStart = new Date(yearStart-1900,monthStart,dayStart);
        long timeStampStart = dateStart.getTime()/1000L;
        Calendar[] dates1 = new Calendar[7];
        int i1 = 0;
        while (i1 < 7){
            Calendar selDate = Calendar.getInstance();
            selDate.add(Calendar.DAY_OF_MONTH, i1);
            dates1[i1] = selDate;
            i1++;
        }
        Calendar endCalendar = dates1[6];
        int dayend = endCalendar.get(Calendar.DAY_OF_MONTH);
        int monthend = endCalendar.get(Calendar.MONTH);
        int yearend = endCalendar.get(Calendar.YEAR);
        Date dateend = new Date(yearend-1900,monthend,dayend);
        long timeStampEnd = dateend.getTime()/1000L;
        //Controller.getCaloriesDateWise(getActivity(),timeStampStart,timeStampEnd, mDatesCaloriesListener);
        Log.d("timestamp energy", String.valueOf(timestamp));
        Controller.getCaloriesDayWise(getActivity(),timestamp, mDayCaloriesListener);
        widget= (MaterialCalendarView) v.findViewById(R.id.calendarView);
        // Add a decorator to disable prime numbered days
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
                .setFirstDayOfWeek(current_cal.get(Calendar.DAY_OF_WEEK))
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
        login_details=getActivity().getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);


        //setHomepageDetails();

        food_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DailyDetailsActivity.class);
                intent.putExtra("selection",0);
                startActivity(intent);
            }
        });
        water_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DailyDetailsActivity.class);
                intent.putExtra("selection",1);
                startActivity(intent);
            }
        });
        exercise_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DailyDetailsActivity.class);
                intent.putExtra("selection",2);
                startActivity(intent);
            }
        });
        sleep_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DailyDetailsActivity.class);
                intent.putExtra("selection",3);
                startActivity(intent);
            }
        });
        feelings_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),DailyDetailsActivity.class);
                intent.putExtra("selection",4);
                startActivity(intent);
            }
        });

        return v;
    }

//    private void setHomepageDetails() {
//
//        Cursor cursor =dop.getCompleteChartInfo(dop,selectedDate);
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//  String      f_con=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_FOODCAL_CONSUMED));
//  String   e_bur=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_EXERCAL_BURNED));
//  String  water=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_WATER_CONSUMED));
//  String    f_req=  cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CHART_FOODCAL_REQ));
//                double wat_int=(Double.valueOf(water));
//                int i=Integer.valueOf((int) wat_int);
//                double wat_int1=(Double.valueOf(f_con));
//                int j=Integer.valueOf((int) wat_int1);
//                double wat_int2=(Double.valueOf(e_bur));
//                int k=Integer.valueOf((int) wat_int2);
//                double wat_int3=(Double.valueOf(f_req));
//                int l=Integer.valueOf((int) wat_int3);
//                int fcal_int=j;
//                int calbur_int=k;
//                int totcal_int=l;
//                cal_consumed.setText(fcal_int+"");
//                cal_left.setText(fcal_int+"/"+totcal_int);
//                cal_burned.setText(calbur_int+"");
//                String w= String.valueOf(i/100);
//                glasses.setText(w+"/8");
//                foodcard_recom.setText(fcal_int + " Kcal");
//                exercard_burnt.setText(calbur_int+"");
//            }
//            while (cursor.moveToNext());
//        }
//
//    }

    RequestListener mDayCaloriesListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("dateRange Request",responseObject.toString());
            final EnergyGetModel energyGetModel = JsonUtils.objectify(responseObject.toString(),EnergyGetModel.class);
            if (getActivity() == null)
                return;
                getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    coinsFinal  = energyGetModel.getTotal_coins();
                    PrefManager pref = new PrefManager(getActivity());
                    pref.setKeyCoins(coinsFinal);
                    cal_consumed.setText(new DecimalFormat("##").format(energyGetModel.getCalorie_consumed()).toString());
                    cal_left.setText(new DecimalFormat("##").format(energyGetModel.getCalorie_required()-energyGetModel.getCalorie_consumed()).toString()+"/"+new DecimalFormat("##").format(energyGetModel.getCalorie_required()).toString());
                    cal_burned.setText(new DecimalFormat("##").format(energyGetModel.getCalorie_burnt()).toString());
                    String w= new DecimalFormat("##").format(energyGetModel.getWater()/250).toString();
                    glasses.setText(w+"/8");
                    foodcard_recom.setText(new DecimalFormat("##").format(energyGetModel.getCalorie_required()).toString() + " Kcal");
                    exercard_burnt.setText(new DecimalFormat("##").format(energyGetModel.getCalorie_burnt()).toString());
                }
            });

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mDatesCaloriesListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("data range calories",responseObject.toString());
//            DateRangeDataModel dateRangeDataModel = JsonUtils.objectify(responseObject.toString(),DateRangeDataModel.class);
//
//            dateRangeDataModel.getChart().get(0).
//
//            try {
//
//                dop.ClearChartTable(dop);
//
//                JSONObject object = new JSONObject(responseObject.toString());
//                JSONArray chart = object.getJSONArray("chart");
//                for (int i = 0; i < chart.length(); i++) {
//                    JSONObject obj = chart.getJSONObject(i);
//                    String water = obj.getString("water");
//                    String date = obj.getString("date");
//                    String fcal_con = obj.getString("calorie_consumed");
//                    String fcal_req = obj.getString("calorie_required");
//                    String exer_bur = obj.getString("calorie_burnt");
//                    long  time= MyUtils.getUtcTimestamp(date,"m");
//
//                    Log.d("check response prob",water+date+""+fcal_con+fcal_req+exer_bur);
//                    dop.putChartInformation(dop, date, String.valueOf(time), fcal_req, fcal_con, "2000", water, "100", exer_bur, "8", "6");
//
//                    int count =dop.getCount(dop, TableData.Tableinfo.TABLE_NAME_CHART, TableData.Tableinfo.CHART_DATE,selectedDate+" 00:00:00");
//
////                  if (count==0){
////                      dop.putChartInformation(dop, date, String.valueOf(time), fcal_req, fcal_con, "8", water, "100", exer_bur, "8", "6");
////                      Log.d("count with put",count+"/////");
////                  }else {
////                      Log.d("count",count+"///  count without");
////                  }
//
//                }
//            }catch (Exception e){
//
//                Log.d("exception",e.toString());
//
//            }
        }

        @Override
        public void onRequestError(int errorCode, String message) {
                Log.d("error",message);
//            if (errorCode >= 400 && errorCode < 500) {
//                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
//                if (getActivity() == null)
//                    return;
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }else{
//                if (getActivity() == null)
//                    return;
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
        }
    };
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
//        menu.findItem(R.id.action_done).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
//                .setVisible(true);
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        PrefManager pref = new PrefManager(getContext());
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
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


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        int day=   date.getDay();
        int month=   date.getMonth();
        int year=   date.getYear();
        pref.setKeyYear(year);
        pref.setKeyMonth(month);
        pref.setKeyDay(day);
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
        long timestamp = date.getDate().getTime()/1000L;
        Log.d("timestamp", String.valueOf(timestamp));
        //long timestamp1 = date.getDate().getTime()/1000L;
        Controller.getCaloriesDayWise(getActivity(),timestamp, mDayCaloriesListener);
        //setHomepageDetails();
    }

}
