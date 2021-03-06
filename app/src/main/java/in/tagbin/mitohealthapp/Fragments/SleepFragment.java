package in.tagbin.mitohealthapp.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.DataItems;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.adapter.SleepAdapter;
import in.tagbin.mitohealthapp.helper.MyUtils;

public class SleepFragment extends Fragment implements DatePickerDialog.OnDateSetListener,OnDateSelectedListener {
    ArrayList<DataItems> database_list;
    RecyclerView food_list;
    SleepAdapter sleepAdapter;
   DatabaseOperations dop;
    SharedPreferences login_details;
    ArrayList<DataItems> arrayList;
    TextView start_time,end_time,no_of_hours;
    RatingBar ratingBar;
    RelativeLayout relativeOverlay;
    String start_t="",end_t="",test="",unique,auth_key;
    public static String selectedDate="";
    int a=0,b=0,c=0,i = 0,mBgColor=0,sth=0,stm=0,eth=0,etm=0,hour,min,hour1,min1;
    static String uniqueId="";
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    MaterialCalendarView widget;
    PrefManager pref;
    int day,year,month;
     public SleepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dop=new DatabaseOperations(getActivity());
        login_details=getActivity().getSharedPreferences(MainActivity.LOGIN_DETAILS, getActivity().MODE_PRIVATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        pref = new PrefManager(getContext());
        Calendar calendar = Calendar.getInstance();
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
        selectedDate=sdf.format(date1);



    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view=inflater.inflate(R.layout.frag_sleep, container, false);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        start_time= (TextView) view.findViewById(R.id.start_time);
        end_time= (TextView) view.findViewById(R.id.end_time);
        no_of_hours= (TextView) view.findViewById(R.id.set_no_hours);
        ratingBar= (RatingBar) view.findViewById(R.id.rating);
        relativeOverlay = (RelativeLayout) view.findViewById(R.id.relativeOverlayFeelings);
        ratingBar.setMax(5);
        ratingBar.setNumStars(5);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                Log.d("num stars",ratingBar.getNumStars()+"");
                Log.d("rating",ratingBar.getRating()+"");
                Cursor cursor = dop.getslInformation(dop, selectedDate);
                int count = cursor.getCount();
                if (count == 1) {
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            uniqueId = cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_UNIQUE_ID));
                            Log.d("ratings",uniqueId+"///"+ratingBar.getRating());
                        } while (cursor.moveToNext());

                        ContentValues cv = new ContentValues();
                        cv.put(TableData.Tableinfo.SLEEP_QUALITY, String.valueOf(ratingBar.getRating()));
                        dop.updateSleepRow(dop, cv, uniqueId);

                    }
                }

            }
        });
        setDatafromdatabase(selectedDate);
        /////////////////////

        pref = new PrefManager(getContext());
        Calendar calendar = Calendar.getInstance();
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

        hour = 22;
        min = 0;
        hour1 = 6;
        min1 = 0;
        Calendar[] dates = new Calendar[4];
        int i = 0;
        while (i < 4){
            Calendar selDate = Calendar.getInstance();
            selDate.add(Calendar.DAY_OF_MONTH, -i);
            dates[i] = selDate;
            i++;
        }
        int day,month,year;
        if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
            day = pref.getKeyDay();
            month = pref.getKeyMonth();
            year = pref.getKeyYear();
        }else{
            Calendar calendar2 = Calendar.getInstance();
            day = calendar2.get(Calendar.DAY_OF_MONTH);
            year = calendar2.get(Calendar.YEAR);
            month = calendar2.get(Calendar.MONTH);
        }
        Date date = new Date(year-1900,month,day);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        if (calendar2.getTimeInMillis() > dates[0].getTimeInMillis() || calendar2.getTimeInMillis() < dates[3].getTimeInMillis()){
            relativeOverlay.setVisibility(View.VISIBLE);
        }else{
            relativeOverlay.setVisibility(View.GONE);
        }
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
                                if (minute == 0){
                                    minute = Integer.parseInt("00");
                                }
                                    start_t=hourOfDay + ":" + minute;
                                    start_time.setText(start_t);

                                    end_time.setText("00:00");

                                Log.d("sleep date", SleepFragment.selectedDate);
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
                                    if (minute == 0){
                                        minute = Integer.parseInt("00");
                                    }
                                    end_t = hourOfDay + ":" + minute;

                                        end_time.setText(end_t);

                                    eth=hourOfDay;
                                    etm=minute;

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
                                                dop.putSleepInformation(dop, uniqueId, start_t, end_t, selectedDate,"","4.0","start","end");

                                            }
                                    try {
                                        calculateNohours(sth,stm,eth,etm);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, hour1, min1, false);
                    tpd.show();

                }
            }
        });
        return view;
    }

    public  void setDatafromdatabase(String dateRe){
        Cursor cursor =dop.getslInformation(dop,dateRe);
        if (cursor.getCount()==0){
            start_time.setText("00:00");
            end_time.setText("00:00");
            no_of_hours.setText("00:00");
            ratingBar.setRating(2);
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
         String starttim=       cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.START_TIME));
                String endtime=    cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.END_TIME));
                String noOfhours=    cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_HOURS));
                String qua=    cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_QUALITY));
                unique=     cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_UNIQUE_ID));
                start_time.setText(starttim);
                end_time.setText(endtime);
                no_of_hours.setText(noOfhours);
//                ratingBar.setRating(Float.parseFloat(qua));

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
        int month1 = date.getMonth();
        pref.setKeyYear(year);
        pref.setKeyMonth(month1);
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
        Calendar[] dates = new Calendar[4];
        int i = 0;
        while (i < 4){
            Calendar selDate = Calendar.getInstance();
            selDate.add(Calendar.DAY_OF_MONTH, -i);
            dates[i] = selDate;
            i++;
        }
        int day1,month2,year1;
        if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
            day1 = pref.getKeyDay();
            month2 = pref.getKeyMonth();
            year1 = pref.getKeyYear();
        }else{
            Calendar calendar2 = Calendar.getInstance();
            day1 = calendar2.get(Calendar.DAY_OF_MONTH);
            year1 = calendar2.get(Calendar.YEAR);
            month2 = calendar2.get(Calendar.MONTH);
        }
        Date date1 = new Date(year1-1900,month2,day1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date1);
        if (calendar2.getTimeInMillis() > dates[0].getTimeInMillis() || calendar2.getTimeInMillis() < dates[3].getTimeInMillis()){
            relativeOverlay.setVisibility(View.VISIBLE);
        }else{
            relativeOverlay.setVisibility(View.GONE);
        }
        setDatafromdatabase(selectedDate);
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

            long start_time_stamp= MyUtils.getUtcTimestamp(selectedDate+" "+sth+":"+stm+":00","s");
            long end_time_stamp= MyUtils.getUtcTimestamp(newDate+" "+eh+":"+em+":00","s");
            long s=startTime.getTime();
            long e=endTime.getTime();
            long d =e-s;
            long hr =d/1000;
            int mn = (int) (d%60);
            int[] i=   splitToComponentTimes(hr);
            Log.d("split time",i[0]+"////////"+i[1]+"/////"+i[2]);

            no_of_hours.setText(i[0]+":"+i[1]);
            Log.d("endTimeelse",d+"");
            ContentValues cv = new ContentValues();
            cv.put(TableData.Tableinfo.SLEEP_HOURS, i[0]+":"+i[1]);
            cv.put(TableData.Tableinfo.START_TIME_STAMP, String.valueOf(start_time_stamp));
            cv.put(TableData.Tableinfo.END_TIME_STAMP, String.valueOf(end_time_stamp));
            dop.updateSleepRow(dop, cv, uniqueId);




        }else{
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(selectedDate+" "+sth+":"+stm+":00");
            Date endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(selectedDate+" "+eh+":"+em+":00");
            long start_time_stamp= MyUtils.getUtcTimestamp(selectedDate+" "+sth+":"+stm+":00","s");
            long end_time_stamp= MyUtils.getUtcTimestamp(selectedDate+" "+eh+":"+em+":00","s");


            long s=startTime.getTime();
            long e=endTime.getTime();
            long d =e-s;
            long hr =d/1000;
            int mn = (int) (d%60);
         int[] i=   splitToComponentTimes(hr);
            Log.d("split time",i[0]+"////////"+i[1]+"/////"+i[2]);
            no_of_hours.setText(i[0]+":"+i[1]);
            ContentValues cv = new ContentValues();
            cv.put(TableData.Tableinfo.SLEEP_HOURS, i[0]+":"+i[1]);
            cv.put(TableData.Tableinfo.START_TIME_STAMP, String.valueOf(start_time_stamp));
            cv.put(TableData.Tableinfo.END_TIME_STAMP, String.valueOf(end_time_stamp));
            dop.updateSleepRow(dop, cv, uniqueId);

            Log.d("endTimeelse",d+"");




        }




    }
    public static int[] splitToComponentTimes(long longVal)
    {

        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }

}
