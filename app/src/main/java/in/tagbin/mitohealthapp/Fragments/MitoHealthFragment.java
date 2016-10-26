package in.tagbin.mitohealthapp.Fragments;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import in.tagbin.mitohealthapp.activity.SettingsActivity;
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
    MaterialCalendarView widget;
    public static String selectedDate = "";
    TextView cal_consumed,cal_left,cal_burned,coins;
    PrefManager pref;
    int coinsFinal = 0,mBgColor = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("PREPDUG", "Setting options true");
        BinderActivity i = (BinderActivity) getActivity();
        setHasOptionsMenu(true);
        i.invalidateOptionsMenu();


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

        cal_consumed = (TextView) v.findViewById(R.id.tvCaloriesConsumed);
        cal_left = (TextView) v.findViewById(R.id.tvCaloriesLeft);
        cal_burned = (TextView) v.findViewById(R.id.tvCaloriesBurnt);
        mBgColor = getResources().getColor(R.color.sample_bg);
        return v;
    }

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
                    cal_left.setText(new DecimalFormat("##").format(energyGetModel.getCalorie_required()-energyGetModel.getCalorie_consumed()).toString());
                    cal_burned.setText(new DecimalFormat("##").format(energyGetModel.getCalorie_burnt()).toString());

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
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
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
        menu.findItem(R.id.action_Settings).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        PrefManager pref = new PrefManager(getContext());
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Settings) {
            if (pref.getKeyUserDetails() != null && pref.getKeyUserDetails().getProfile().getHeight() != 0 && pref.getKeyUserDetails().getProfile().getWeight() != 0){
                    //toolbar_title.setText("Settings");
                    //toolbar.setTitle("");
                    //fra = new SettingsActivity();
                    Intent i = new Intent(getContext(), SettingsActivity.class);
                    startActivity(i);
                }else {
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Enter Details");
                    alertDialog1.setMessage("Please enter your height and weight to proceed");
                    alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog1.show();
                }
            return true;
        }

        return super.onOptionsItemSelected(item);
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
