package in.tagbin.mitohealthapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    RWeekCalendar rCalendarFragment;
    SharedPreferences login_details;
    String auth_key;
    int hour,min;
    DataItems dataItems;
    ArrayList<DataItems> arrayList;
    TextView start_time,end_time,no_of_hours;
RatingBar ratingBar;
    String test="";
    public static String selectedDate="";
    String unique;
    int a=0,b=0,c=0;
    int i = 0;
    int mBgColor=0;
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
        LocalDateTime mSelectedDate = LocalDateTime.now();

        int day = mSelectedDate.getDayOfMonth();
        int month = mSelectedDate.getMonthOfYear();
        int year = mSelectedDate.getYear();
        selectedDate = year + "-" + month + "-" + day;
        Log.d("food startdate", selectedDate);



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_sleep, container, false);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        start_time= (TextView) view.findViewById(R.id.start_time);
        end_time= (TextView) view.findViewById(R.id.end_time);
        no_of_hours= (TextView) view.findViewById(R.id.set_no_hours);
        ratingBar= (RatingBar) view.findViewById(R.id.rating);
        setDatafromdatabase(selectedDate);
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

                                String time=hourOfDay + ":" + minute;
                                start_time.setText(time);
                                ContentValues cv = new ContentValues();
                                cv.put(TableData.Tableinfo.START_TIME, time);
                                dop.updateSleepRow(dop, cv, test);
                                Log.d("sleep date",SleepFrag.selectedDate);
                            }
                        }, hour, min, false);
                tpd.show();

            }
        });
        end_time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // holder.end_tiem.setText(hourOfDay + ":" + minute);
                                String time=hourOfDay + ":" + minute;
                                end_time.setText(time);
                                ContentValues cv = new ContentValues();
                                cv.put(TableData.Tableinfo.END_TIME, time);
                                dop.updateSleepRow(dop, cv, test);
                            }
                        }, hour, min, false);
                tpd.show();

            }
        });
//        food_list= (RecyclerView) view.findViewById(R.id.food_list);
//        food_list.setItemAnimator(new SlideInLeftAnimator());
//        food_list.getItemAnimator().setRemoveDuration(1000);
//        SlideInLeftAnimator animator = new SlideInLeftAnimator();
//        animator.setInterpolator(new OvershootInterpolator());
//// or recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f));
//        food_list.setItemAnimator(animator);
////        calenderTrans();
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
//        food_list.setLayoutManager(linearLayoutManager);
//        database_list=new ArrayList<>();
//        dop=new DatabaseOperations(getActivity());
//        sleepAdapter=new SleepAdapter(getActivity());
//
//        food_list.setAdapter(sleepAdapter);
//        food_list.setHasFixedSize(true);
//        database_list=  dop.getsleepInformation(dop, CollapsableLogging.selectedDate);
//        sleepAdapter.setData(database_list);
//        sleepAdapter.notifyDataSetChanged();
//
//
//        ItemClickSupport.addTo(food_list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//
////                startActivity(new Intent(getActivity(), FoodDetails.class));
////
//// akshayluthra12@
//
//            }
//        });
//        if (database_list.isEmpty()){
//
//        }else {
//
//        }
//        Log.d("final selected date", CollapsableLogging.selectedDate);

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
        Calendar calendar = Calendar.getInstance();

        calendar.set(year, monthOfYear, dayOfMonth);
        rCalendarFragment.setDateWeek(calendar);
    }

    @Override
    public void onResume() {
        super.onResume();
//        calenderTrans();
    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        int day=   date.getDay();
        int month=   date.getMonth();
        int year=   date.getYear();
        selectedDate=year+"-"+month+"-"+day;
        database_list = dop.getInformation(dop, selectedDate);
        sleepAdapter.setData(database_list);
        sleepAdapter.notifyDataSetChanged();
        Log.d("date",selectedDate);
        String s[]= selectedDate.split("-");
        test=s[0]+s[1]+s[2];
        setDatafromdatabase(selectedDate);
        int c=dop.getProfilesCount(dop,test);
        if (c==0) {
            dop.putSleepInformation(dop, test, "13:56", "22:34", selectedDate);
            Log.d("Sleeep c=0", String.valueOf(c));

        }else {
            setDatafromdatabase(selectedDate);
            Log.d("Sleeep c", String.valueOf(c));

        }
        Log.d("Sleeep interface",selectedDate);
    }
    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
}
