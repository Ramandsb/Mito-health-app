package in.tagbin.mitohealthapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDateTime;
import org.joda.time.chrono.StrictChronology;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.CollapsableLogging;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Interfaces.SleepInterface;
import in.tagbin.mitohealthapp.ItemClickSupport;
import in.tagbin.mitohealthapp.MainPage;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.SleepAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class SleepFrag extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    ArrayList<DataItems> database_list;
    RecyclerView food_list;
    SleepAdapter sleepAdapter;
   DatabaseOperations dop;
    SharedPreferences login_details;
    String auth_key;
    int hour,min;
    DataItems dataItems;
    ArrayList<DataItems> arrayList;
    TextView start_time,end_time,no_of_hours;
RatingBar ratingBar;
    String start_t="";
    String end_t="";

    String test="";
    public static String selectedDate="";
    String unique;
    int a=0,b=0,c=0;
    int i = 0;
    int mBgColor=0;
   static String uniqueId="";
    int sth=0,stm=0,eth=0,etm=0;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
     public SleepFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dop=new DatabaseOperations(getActivity());
        login_details=getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, getActivity().MODE_PRIVATE);
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
        Log.d("food startdate", selectedDate);
        try {
            calculateNohours(20,25,3,25);
            calculateNohours(15,25,20,25);
            calculateNohours(1,25,15,25);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view=inflater.inflate(R.layout.fragment_sleep, container, false);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        start_time= (TextView) view.findViewById(R.id.start_time);
        end_time= (TextView) view.findViewById(R.id.end_time);
        no_of_hours= (TextView) view.findViewById(R.id.set_no_hours);
        ratingBar= (RatingBar) view.findViewById(R.id.rating);
//        setDatafromdatabase(selectedDate);
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

        start_time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {




                                sth=hourOfDay;
                                stm=minute;
                                    start_t=hourOfDay + ":" + minute;
                                    start_time.setText(start_t);
                                    end_time.setText("End Time");

                                Log.d("sleep date",SleepFrag.selectedDate);
                            }
                        }, hour, min, false);
                tpd.show();

            }
        });
        end_time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (start_t.equals("")) {

                    Snackbar.make(view, "Set Start Time", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();


                } else {
                    TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {
                                    // holder.end_tiem.setText(hourOfDay + ":" + minute);
                                    end_t = hourOfDay + ":" + minute;

                                        end_time.setTextSize(20);
                                        end_time.setText(end_t);

                                        Cursor cursor = dop.getslInformation(dop, selectedDate);
                                        int count = cursor.getCount();
                                        if (count == 1) {
                                            if (cursor != null && cursor.moveToFirst()) {
                                                do {
                                                    uniqueId = cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_UNIQUE_ID));
                                                } while (cursor.moveToNext());
                                                ContentValues cv = new ContentValues();
                                                cv.put(TableData.Tableinfo.START_TIME, start_t);
                                                cv.put(TableData.Tableinfo.END_TIME, end_t);
                                                dop.updateSleepRow(dop, cv, uniqueId);
                                            }
                                        }else if (count == 0) {
                                                uniqueId = String.valueOf(System.currentTimeMillis());
                                                dop.putSleepInformation(dop, uniqueId, start_t, end_t, selectedDate);

                                            }

                                }
                            }, hour, min, false);
                    tpd.show();

                }
            }
        });
        return view;
    }

    public  void setDatafromdatabase(String dateRe){
        Cursor cursor =dop.getslInformation(dop,dateRe);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
         String starttim=       cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.START_TIME));
                String endtime=    cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.END_TIME));
            String s[]=    starttim.split(":");
              String s1[]=  endtime.split(":");
                int h1=Integer.valueOf(s1[0]);
                int m1=Integer.valueOf(s1[1]);
                int h=Integer.valueOf(s[0]);
                int m=Integer.valueOf(s[1]);
                int hour=0;
                if (h1>h){
                   hour= h1-h;
                }else if (h>h1){
                    hour= h-h1;
                }
int min=0;
                if (m1>m){
                    min= m1-m;
                }else if (m>m1){
                    min= m-m1;
                }



                unique=     cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_UNIQUE_ID));
                start_time.setText(starttim);
                end_time.setText(endtime);
                no_of_hours.setText(hour+":"+min);

            }
            while (cursor.moveToNext());
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onResume() {
        super.onResume();
//        calenderTrans();
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

    }

    public void calculateNohours(int sth,int stm,int eh,int em) throws ParseException {
      String [] selectSplit=selectedDate.split("-");
     int year = Integer.valueOf(selectSplit[0]);
     int month = Integer.valueOf(selectSplit[1]);
     int day = Integer.valueOf(selectSplit[2])+1;


        String newDate= year + "-" + "0"+month + "-" + day;


        if (eh<sth){
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(selectedDate+" "+sth+":"+stm+":00");
            Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(newDate+" "+eh+":"+em+":00");

            long s=startTime.getTime();
            long e=endTime.getTime();
            Log.d("startTimeif",s+"");
            Log.d("endTimeif",e+"");
            long d =e-s;
            long h =d/3600000;
            Log.d("final no of hours",h+"");





        }else{
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(selectedDate+" "+sth+":"+stm+":00");
            Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(selectedDate+" "+eh+":"+em+":00");

            long s=startTime.getTime();
            long e=endTime.getTime();
            long d =e-s;
            long h =d/3600000;
            Log.d("startTimeelse",s+"");
            Log.d("endTimeelse",e+"");
            Log.d("final",h+"");
        }




    }
}
