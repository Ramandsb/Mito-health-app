package in.tagbin.mitohealthapp.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.tagbin.mitohealthapp.CollapsableLogging;
import in.tagbin.mitohealthapp.Interfaces.ExerciseInterface;
import in.tagbin.mitohealthapp.Interfaces.WaterInterface;
import in.tagbin.mitohealthapp.PourBeerTask;
import in.tagbin.mitohealthapp.R;
import uk.co.barbuzz.beerprogressview.BeerProgressView;

public class WaterFrag extends Fragment implements WaterInterface,OnDateSelectedListener {
    BeerProgressView water1,water2,water3,water4,water5,water6,water7,water8,water9;
    Bitmap bitmapWater1,bitmapWater2,bitmapWater3,bitmapWater4,bitmapWater5,bitmapWater6,bitmapWater7,bitmapWater8,bitmapWater9;
    Canvas canvas;
    Handler handler;
    PourBeerTask mPourBeerTask;
    boolean boo1=false;
    boolean boo2=false;
    boolean boo3=false;
    boolean boo4=false;
    boolean boo5=false;
    boolean boo6=false;
    boolean boo7=false;
    boolean boo8=false;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    public static String selectedDate="";

    int a=0,b=0,c=0;
    int i = 0;
    int mBgColor=0;
    public WaterFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       handler = new Handler();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_water, container, false);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        final View g1=view.findViewById(R.id.g1);
        final View g2=view.findViewById(R.id.g2);
        final View g3=view.findViewById(R.id.g3);
        final View g4=view.findViewById(R.id.g4);
        final View g5=view.findViewById(R.id.g5);
        final  View g6=view.findViewById(R.id.g6);
        final  View g7=view.findViewById(R.id.g7);
        final View g8=view.findViewById(R.id.g8);
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

        water1= (BeerProgressView) view.findViewById(R.id.DrawingwaterView1);
        water2= (BeerProgressView) view.findViewById(R.id.DrawingwaterView2);
        water3= (BeerProgressView) view.findViewById(R.id.DrawingwaterView3);
        water4= (BeerProgressView) view.findViewById(R.id.DrawingwaterView4);
        water5= (BeerProgressView) view.findViewById(R.id.DrawingwaterView5);
        water6= (BeerProgressView) view.findViewById(R.id.DrawingwaterView6);
        water7= (BeerProgressView) view.findViewById(R.id.DrawingwaterView7);
        water8= (BeerProgressView) view.findViewById(R.id.DrawingwaterView8);
        water9= (BeerProgressView) view.findViewById(R.id.complete_beer);

//        bitmapWater = Bitmap.createBitmap((int) getActivity().getWindowManager()
//                .getDefaultDisplay().getWidth(), (int) getActivity().getWindowManager()
//                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);


        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water1,80,0);
                if (boo1){
                    pourBeerTask.cancel(true);
                    water1.setBeerProgress(0);
                    boo1=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    boo1=true;
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,8,0);
                    Task.execute(true);
                    Log.d("else","tr");

                }



            }
        });
        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water2,80,0);
                if (boo2){
                    pourBeerTask.cancel(true);
                    water2.setBeerProgress(0);
                    boo2=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,16,8);
                    Task.execute(true);
                    boo2=true;
                    Log.d("else","tr");

                }
            }
        });


        g3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water3,80,0);
                if (boo3){
                    pourBeerTask.cancel(true);
                    water3.setBeerProgress(0);
                    boo3=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,24,16);
                    Task.execute(true);
                    boo3=true;
                    Log.d("else","tr");

                }            }
        });

        g4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water4,80,0);
                if (boo4){
                    pourBeerTask.cancel(true);
                    water4.setBeerProgress(0);
                    boo4=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,32,24);
                    Task.execute(true);
                    boo4=true;
                    Log.d("else","tr");

                }            }
        });




        g5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water5,80,0);
                if (boo5){
                    pourBeerTask.cancel(true);
                    water5.setBeerProgress(0);

                    boo5=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,40,32);
                    Task.execute(true);
                    boo5=true;
                    Log.d("else","tr");

                }                   }
        });
        g6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water6,80,0);
                if (boo6){
                    pourBeerTask.cancel(true);
                    water6.setBeerProgress(0);

                    boo6=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,50,40);
                    Task.execute(true);
                    boo6=true;
                    Log.d("else","tr");

                }                       }
        });
        g7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water7,80,0);
                if (boo7){
                    pourBeerTask.cancel(true);
                    water7.setBeerProgress(0);
                    boo7=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,60,50);
                    Task.execute(true);
                    boo7=true;
                    Log.d("else","tr");

                }                     }
        });
        g8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water8,80,0);
                if (boo8){
                    pourBeerTask.cancel(true);
                    water8.setBeerProgress(0);


                    boo8=false;
                    Log.d("cancel","tr");
                }else {
                    pourBeerTask.execute(true);
                    boo8=true;
                    Log.d("else","tr");
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,70,60);
                    Task.execute(true);

                }
                    }
        });
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

    }




    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        ((CollapsableLogging)getActivity()).waterInterface = this;
    }

    @Override
    public void passDataToWaterFragment(String selectedDate) {
        Log.d("Water frag",selectedDate);
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
    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
}
