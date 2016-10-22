package in.tagbin.mitohealthapp.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
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
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.WaterWaveProgress.WaterWaveProgress;
import in.tagbin.mitohealthapp.helper.PourBeerTask;
import in.tagbin.mitohealthapp.R;
import uk.co.barbuzz.beerprogressview.BeerProgressView;

public class WaterFragment extends Fragment implements OnDateSelectedListener {
    WaterWaveProgress water1,water2,water3,water4,water5,water6,water7,water8;
    WaterWaveProgress water9,water10,water11,water12,water13,water14,water15,water16;
    WaterWaveProgress waterjug,waterjug2;
    View jug2;
    View otherglasses;
    Bitmap bitmapWater1,bitmapWater2,bitmapWater3,bitmapWater4,bitmapWater5,bitmapWater6,bitmapWater7,bitmapWater8,bitmapWater9;
    Canvas canvas;
    int count=0;

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
    public static String unique_id="";
    int glass_size=250;
    TextView ml;
    int a=0,b=0,c=0;
    int i = 0;
    int mBgColor=0;
    DatabaseOperations dop;
    int jugcount=0;
    public WaterFragment() {
        // Required empty public constructor
    }

    int pro=0;
    int pro2=0;
    int pro3=0;
    int pro4=0;
    int pro5=0;
    int pro6=0;
    int pro7=0;
    int pro8=0;
    int pro9=0;
    int pro10=0;
    int pro11=0;
    int pro12=0;
    int pro13=0;
    int pro14=0;
    int pro15=0;
    int pro16=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar  current_cal = Calendar.getInstance();
        Date currentDate = current_cal.getTime();
        selectedDate=sdf.format(currentDate);
        dop= new DatabaseOperations(getActivity());


    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_water, container, false);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        final View g1=view.findViewById(R.id.g1);
        final View g2=view.findViewById(R.id.g2);
        final View g3=view.findViewById(R.id.g3);
        final View g4=view.findViewById(R.id.g4);
        final View g5=view.findViewById(R.id.g5);
        final  View g6=view.findViewById(R.id.g6);
        final  View g7=view.findViewById(R.id.g7);
        final View g8=view.findViewById(R.id.g8);
        final View g9=view.findViewById(R.id.g9);
        final View g10=view.findViewById(R.id.g10);
        final View g11=view.findViewById(R.id.g11);
        final View g12=view.findViewById(R.id.g12);
        final View g13=view.findViewById(R.id.g13);
        final View g14=view.findViewById(R.id.g14);
        final View g15=view.findViewById(R.id.g15);
        final View g16=view.findViewById(R.id.g16);
         otherglasses=view.findViewById(R.id.otherglasses);
        otherglasses.setVisibility(View.GONE);
        ml= (TextView) view.findViewById(R.id.ml);
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
                .setFirstDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
                .commit();

        //////////////////
//        WaveProgressView waveProgressView = (WaveProgressView) view.findViewById(R.id.DrawingwaterView1);
//        waveProgressView.setMax(60);
//        animWave(waveProgressView, 2 * 1000);
       Init(view);
        Cursor cursor = dop.getWaterInformation(dop, selectedDate);
        if (cursor.getCount()==0){
            count=0;
            fillGlasses(count);


        }else if (cursor.getCount()==1) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    count = Integer.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.GLASSES)));
                    fillGlasses(count);
                    if (count>7){
                        otherglasses.setVisibility(View.VISIBLE);
                        jug2.setVisibility(View.VISIBLE);
                    }

                    ml.setText(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ML))+" ml");
                    Log.d("count",count+"");

                } while (cursor.moveToNext());
            }
        }





        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=1;
                UpdateDataBase(String.valueOf(count));

                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);

                Log.d("Val of Pro",pro+"");
                if (pro==80){
                    water1.setProgress(0);
                    water2.setProgress(0);
                    water3.setProgress(0);
                    water4.setProgress(0);
                    water5.setProgress(0);
                    water6.setProgress(0);
                    water7.setProgress(0);
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro=0;
                    pro2=0;
                    pro3=0;
                    pro4=0;
                    pro5=0;
                    pro6=0;
                    pro7=0;
                    pro8=0;
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    count=0;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(00);
                    updateJug2(00);
                }else if (pro==0){
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                water1.setProgress(finalA);
                                pro=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(10);
                    updateJug2(00);
                }

            }
        });

        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=2;
                UpdateDataBase(String.valueOf(count));

                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);
                Log.d("Val of Pro",pro+"");
                if (pro2==80){
                    water2.setProgress(0);
                    water3.setProgress(0);
                    water4.setProgress(0);
                    water5.setProgress(0);
                    water6.setProgress(0);
                    water7.setProgress(0);
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    pro2=0;
                    pro3=0;
                    pro4=0;
                    pro5=0;
                    pro6=0;
                    pro7=0;
                    pro8=0;
                    count=1;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(10);
                    updateJug2(00);
                }else if (pro2==0){
                    pro=80;
                    water1.setProgress(80);
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                water2.setProgress(finalA);
                                pro2=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(20);
                    updateJug2(00);
                }

            }
        });
        g3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);
                count=3;
                UpdateDataBase(String.valueOf(count));


                Log.d("Val of Pro",pro3+"");
                if (pro3==80){
                    water3.setProgress(0);
                    water4.setProgress(0);
                    water5.setProgress(0);
                    water6.setProgress(0);
                    water7.setProgress(0);
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    pro3=0;
                    pro4=0;
                    pro5=0;
                    pro6=0;
                    pro7=0;
                    pro8=0;
                    count=2;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(20);
                    updateJug2(00);
                }else if (pro3==0){
                    pro=80;
                    pro2=80;
                    water1.setProgress(80);
                    water2.setProgress(80);
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                water3.setProgress(finalA);

                                pro3=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(30);
                    updateJug2(00);
                }

            }
        });
        g4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=4;
                UpdateDataBase(String.valueOf(count));

                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);
                Log.d("Val of Pro",pro+"");
                if (pro4==80){
                    water4.setProgress(0);
                    water5.setProgress(0);
                    water6.setProgress(0);
                    water7.setProgress(0);
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    pro4=0;
                    pro5=0;
                    pro6=0;
                    pro7=0;
                    pro8=0;
                    count=3;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(30);
                    updateJug2(00);
                }else if (pro4==0){
                    pro=80;
                    pro2=80;
                    pro3=80;
                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                water4.setProgress(finalA);
                                pro4=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(40);
                    updateJug2(00);
                }


            }
        });
        g5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);
                count=5;
                UpdateDataBase(String.valueOf(count));

                Log.d("Val of Pro",pro5+"");
                if (pro5==80){
                    water5.setProgress(0);
                    water6.setProgress(0);
                    water7.setProgress(0);
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    pro5=0;
                    pro6=0;
                    pro7=0;
                    pro8=0;
                    count=4;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(40);
                    updateJug2(00);
                }else if (pro5==0){
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                water5.setProgress(finalA);

                                pro5=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(50);
                    updateJug2(00);
                }

            }
        });
        g6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=6;
                UpdateDataBase(String.valueOf(count));

                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);
                Log.d("Val of Pro",pro6+"");
                if (pro6==80){
                    water6.setProgress(0);
                    water7.setProgress(0);
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    pro6=0;
                    pro7=0;
                    pro8=0;
                    count=5;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(50);
                    updateJug2(00);
                }else if (pro6==0){
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water6.setProgress(finalA);
                                pro6=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(60);
                    updateJug2(00);
                }

            }
        });
        g7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=7;
                UpdateDataBase(String.valueOf(count));
                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);

                Log.d("Val of Pro",pro7+"");
                if (pro7==80){
                    water7.setProgress(0);
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    pro7=0;
                    pro8=0;
                    count=6;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(60);
                    updateJug2(00);
                    jug2.setVisibility(View.GONE);
                }else if(pro7==0) {
                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water7.setProgress(finalA);
                                pro7=finalA;
                            }
                        }, 5 * a);
                    }
                }
                otherglasses.setVisibility(View.VISIBLE);
                updateJug1(70);
                updateJug2(00);
                jug2.setVisibility(View.VISIBLE);

            }
        });
        g8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);
                count=8;
                UpdateDataBase(String.valueOf(count));

                Log.d("Val of Pro",pro+"");
                if (pro8==80){
                    water8.setProgress(0);
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    pro8=0;
                    count=7;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(70);
                    updateJug2(00);
                }else if(pro8==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water8.setProgress(finalA);
                                pro8=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(80);
                    updateJug2(00);

                }

            }
        });
        g9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=9;
                UpdateDataBase(String.valueOf(count));
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);

                Log.d("Val of Pro",pro+"");
                if (pro9==80){
                    water9.setProgress(0);
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro9=0;
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    count=8;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(00);
                }else if(pro9==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;


                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water9.setProgress(finalA);
                                pro9=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(80);
                    updateJug2(10);
                }

            }
        });
        g10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=10;
                UpdateDataBase(String.valueOf(count));

                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);

                Log.d("Val of Pro",pro+"");
                if (pro10==80){
                    water10.setProgress(0);
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro10=0;
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    count=9;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(10);
                }else if(pro10==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    water9.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;
                    pro9=80;


                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water10.setProgress(finalA);
                                pro10=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(80);
                    updateJug2(20);
                }

            }
        });
        g11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=11;
                UpdateDataBase(String.valueOf(count));
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);


                Log.d("Val of Pro",pro+"");
                if (pro11==80){
                    water11.setProgress(0);
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro11=0;
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    count=10;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(20);
                }else if(pro11==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    water9.setProgress(80);
                    water10.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;
                    pro9=80;
                    pro10=80;


                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water11.setProgress(finalA);
                                pro11=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(80);
                    updateJug2(30);

                }

            }
        });
        g12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=12;
                UpdateDataBase(String.valueOf(count));
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);


                Log.d("Val of Pro",pro+"");
                if (pro12==80){
                    water12.setProgress(0);
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro12=0;
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;

                    count=11;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(30);
                }else if(pro12==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    water9.setProgress(80);
                    water10.setProgress(80);
                    water11.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;
                    pro9=80;
                    pro10=80;
                    pro11=80;


                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water12.setProgress(finalA);
                                pro12=finalA;
                            }
                        }, 5 * a);
                    }

                    updateJug1(80);
                    updateJug2(40);
                }

            }
        });
        g13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);

                count=13;
                UpdateDataBase(String.valueOf(count));

                Log.d("Val of Pro",pro+"");
                if (pro13==80){
                    water13.setProgress(0);
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro13=0;
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    count=12;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(40);
                }else if(pro13==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    water9.setProgress(80);
                    water10.setProgress(80);
                    water11.setProgress(80);
                    water12.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;
                    pro9=80;
                    pro10=80;
                    pro11=80;
                    pro12=80;


                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water13.setProgress(finalA);
                                pro13=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(80);
                    updateJug2(50);
                 }

            }
        });
        g14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=14;
                UpdateDataBase(String.valueOf(count));
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);


                Log.d("Val of Pro",pro+"");
                if (pro14==80){
                    water14.setProgress(0);
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro14=0;
                    pro15=0;
                    pro16=0;
                    count=13;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(50);
                }else if(pro14==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    water9.setProgress(80);
                    water10.setProgress(80);
                    water11.setProgress(80);
                    water12.setProgress(80);
                    water13.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;
                    pro9=80;
                    pro10=80;
                    pro11=80;
                    pro12=80;
                    pro13=80;


                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water14.setProgress(finalA);
                                pro14=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(80);
                    updateJug2(60);

                }

            }
        });

        g15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count=15;
                UpdateDataBase(String.valueOf(count));
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);


                Log.d("Val of Pro",pro+"");
                if (pro15==80){
                    water15.setProgress(0);
                    water16.setProgress(0);
                    pro15=0;
                    pro16=0;
                    count=14;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(60);
                }else if(pro15==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    water9.setProgress(80);
                    water10.setProgress(80);
                    water11.setProgress(80);
                    water12.setProgress(80);
                    water13.setProgress(80);
                    water14.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;
                    pro9=80;
                    pro10=80;
                    pro11=80;
                    pro12=80;
                    pro13=80;
                    pro14=80;

                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water15.setProgress(finalA);
                                pro15=finalA;
                            }
                        }, 5 * a);
                    }
                    updateJug1(80);
                    updateJug2(70);

                }

            }
        });


        g16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count=16;
                UpdateDataBase(String.valueOf(count));

                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);


                Log.d("Val of Pro",pro+"");
                if (pro16==80){
                    water16.setProgress(0);
                    pro16=0;
                    count=15;
                    UpdateDataBase(String.valueOf(count));
                    updateJug1(80);
                    updateJug2(70);
                }else if(pro16==0){

                    water1.setProgress(80);
                    water2.setProgress(80);
                    water3.setProgress(80);
                    water4.setProgress(80);
                    water5.setProgress(80);
                    water6.setProgress(80);
                    water7.setProgress(80);
                    water8.setProgress(80);
                    water9.setProgress(80);
                    water10.setProgress(80);
                    water11.setProgress(80);
                    water12.setProgress(80);
                    water13.setProgress(80);
                    water14.setProgress(80);
                    water15.setProgress(80);
                    pro=80;
                    pro2=80;
                    pro3=80;
                    pro4=80;
                    pro5=80;
                    pro6=80;
                    pro7=80;
                    pro8=80;
                    pro9=80;
                    pro10=80;
                    pro11=80;
                    pro12=80;
                    pro13=80;
                    pro14=80;
                    pro15=80;
                    for (int a = 0; a<=80 ;a++) {
                        final int finalA = a;
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                water16.setProgress(finalA);
                                pro16=finalA;
                            }
                        }, 5 * a);
                    }

                    updateJug1(80);
                    updateJug2(80);

                }

            }
        });


//        g2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PourBeerTask pourBeerTask=new PourBeerTask (getActivity(), water2,80,0);
//                if (boo2){
//                    pourBeerTask.cancel(true);
//                    water2.setBeerProgress(0);
//                    boo2=false;
//                    count--;
//                    water9.setBeerProgress(Returnjug(count));
//                    jugcount=Returnjug(count);
//                    Log.d("cancel","tr");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//                }else {
//                    pourBeerTask.execute(true);
//                    count++;
//                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
//                    jugcount=Returnjug(count);
//                    Task.execute(true);
//                    boo2=true;
//
//                    Log.d("jug",jugcount+"///");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//
//                }
//            }
//        });
//
//
//        g3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water3,80,0);
//                if (boo3){
//                    pourBeerTask.cancel(true);
//                    water3.setBeerProgress(0);
//                    boo3=false;
//                    count--;
//                    water9.setBeerProgress(Returnjug(count));
//                    jugcount=Returnjug(count);
//                    Log.d("cancel","tr");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//                }else {
//                    pourBeerTask.execute(true);
//                    count++;
//                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
//                    jugcount=Returnjug(count);
//                    Task.execute(true);
//                    boo3=true;
//
//                    Log.d("jug",jugcount+"///");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//
//                }            }
//        });
//
//        g4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water4,80,0);
//                if (boo4){
//                    pourBeerTask.cancel(true);
//                    water4.setBeerProgress(0);
//                    boo4=false;
//                    count--;
//                    water9.setBeerProgress(Returnjug(count));
//                    jugcount=Returnjug(count);
//                    Log.d("cancel","tr");
//                    Log.d("count",count+"");
//                }else {
//                    pourBeerTask.execute(true);
//                    count++;
//                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
//                    jugcount=Returnjug(count);
//                    Task.execute(true);
//                    boo4=true;
//
//                    Log.d("jug",jugcount+"///");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//
//                }            }
//        });
//
//
//
//
//        g5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water5,80,0);
//                if (boo5){
//                    pourBeerTask.cancel(true);
//                    water5.setBeerProgress(0);
//                    count--;
//                    water9.setBeerProgress(Returnjug(count));
//                    jugcount=Returnjug(count);
//                    boo5=false;
//                    Log.d("cancel","tr");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//                }else {
//                    pourBeerTask.execute(true);
//                    count++;
//                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
//                    jugcount=Returnjug(count);
//                    Task.execute(true);
//                    boo5=true;
//
//                    Log.d("jug",jugcount+"///");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//
//                }                   }
//        });
//        g6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water6,80,0);
//                if (boo6){
//                    pourBeerTask.cancel(true);
//                    water6.setBeerProgress(0);
//                    count--;
//                    water9.setBeerProgress(Returnjug(count));
//                    jugcount=Returnjug(count);
//                    boo6=false;
//                    Log.d("cancel","tr");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//                }else {
//                    pourBeerTask.execute(true);
//                    count++;
//                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
//                    jugcount=Returnjug(count);
//                    Task.execute(true);
//                    boo6=true;
//
//                    Log.d("jug",jugcount+"///");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//
//                }                       }
//        });
//        g7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water7,80,0);
//                if (boo7){
//                    pourBeerTask.cancel(true);
//                    water7.setBeerProgress(0);
//                    boo7=false;
//                    count--;
//                    water9.setBeerProgress(Returnjug(count));
//                    jugcount=Returnjug(count);
//                    Log.d("cancel","tr");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//                }else {
//                    pourBeerTask.execute(true);
//                    count++;
//                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
//                    jugcount=Returnjug(count);
//                    Task.execute(true);
//                    boo7=true;
//
//                    Log.d("jug",jugcount+"///");
//                    Log.d("count",count+"");
//                    UpdateDataBase(String.valueOf(count));
//
//                }                     }
//        });
//        g8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water8,80,0);
//                if (boo8){
//                    pourBeerTask.cancel(true);
//                    water8.setBeerProgress(0);
//                    count--;
//                    water9.setBeerProgress(Returnjug(count));
//                    jugcount=Returnjug(count);
//                    Log.d("count",count+"");
//                    boo8=false;
//                    Log.d("cancel","tr");
//                    UpdateDataBase(String.valueOf(count));
//                }else {
//                    pourBeerTask.execute(true);
//                    boo8=true;
//                    count++;
//                    Log.d("else","tr");
//                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
//                    jugcount=Returnjug(count);
//                    Task.execute(true);
//                    Log.d("count",count+"");
//                    Log.d("jug",jugcount+"///");
//                    UpdateDataBase(String.valueOf(count));
//
//                }
//                    }
//        });
        return view;
    }

    private void Init(View view) {
        water1= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView1);
        water2= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView2);
        water3= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView3);
        water4= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView4);
        water5= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView5);
        water6= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView6);
        water7= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView7);
        water8= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView8);
        water9= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView9);
        water10= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView10);
        water11= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView11);
        water12= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView12);
        water13= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView13);
        water14= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView14);
        water15= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView15);
        water16= (WaterWaveProgress) view.findViewById(R.id.DrawingwaterView16);
        waterjug= (WaterWaveProgress) view.findViewById(R.id.complete_beer);
        waterjug2= (WaterWaveProgress) view.findViewById(R.id.complete_beer2);
        jug2= view.findViewById(R.id.jug2);
        jug2.setVisibility(View.GONE);


        water1.setShowProgress(true);
        water1.animateWave();
        water1.setShowProgress(false);
        water1.setShowNumerical(false);
        water2.setShowProgress(true);
        water2.animateWave();
        water2.setShowProgress(false);
        water2.setShowNumerical(false);
        water3.setShowProgress(true);
        water3.animateWave();
        water3.setShowProgress(false);
        water3.setShowNumerical(false);
        water4.setShowProgress(true);
        water4.animateWave();
        water4.setShowProgress(false);
        water4.setShowNumerical(false);
        water5.setShowProgress(true);
        water5.animateWave();
        water5.setShowProgress(false);
        water5.setShowNumerical(false);
        water6.setShowProgress(true);
        water6.animateWave();
        water6.setShowProgress(false);
        water6.setShowNumerical(false);
        water7.setShowProgress(true);
        water7.animateWave();
        water7.setShowProgress(false);
        water7.setShowNumerical(false);
        water8.setShowProgress(true);
        water8.animateWave();
        water8.setShowProgress(false);
        water8.setShowNumerical(false);
        water9.setShowProgress(true);
        water9.animateWave();
        water9.setShowProgress(false);
        water9.setShowNumerical(false);
        water10.setShowProgress(true);
        water10.animateWave();
        water10.setShowProgress(false);
        water10.setShowNumerical(false);
        water11.setShowProgress(true);
        water11.animateWave();
        water11.setShowProgress(false);
        water11.setShowNumerical(false);
        water12.setShowProgress(true);
        water12.animateWave();
        water12.setShowProgress(false);
        water12.setShowNumerical(false);
        water13.setShowProgress(true);
        water13.animateWave();
        water13.setShowProgress(false);
        water13.setShowNumerical(false);
        water14.setShowProgress(true);
        water14.animateWave();
        water14.setShowProgress(false);
        water14.setShowNumerical(false);
        water15.setShowProgress(true);
        water15.animateWave();
        water15.setShowProgress(false);
        water15.setShowNumerical(false);
        water16.setShowProgress(true);
        water16.animateWave();
        water16.setShowProgress(false);
        water16.setShowNumerical(false);
        waterjug.setShowProgress(true);
        waterjug.animateWave();
        waterjug.setShowProgress(false);
        waterjug.setShowNumerical(false);
        waterjug2.setShowProgress(true);
        waterjug2.animateWave();
        waterjug2.setShowProgress(false);
        waterjug2.setShowNumerical(false);
    }


    @Override
    public void onResume() {
        super.onResume();


    }
    public void resetGlasses(){
        for (int i = 1; i <= 16; i++) {
            WaterWaveProgress wa = returnGlasses(i);
            wa.setProgress(0);
        }
    }

    public void fillGlasses(int glasses){

        if (glasses==0){
            resetGlasses();
            waterjug.setProgress(0);
            waterjug2.setProgress(0);
            jug2.setVisibility(View.GONE);
            ml.setText("00 ml");



        }else {
            for (int i = 1; i <= glasses; i++) {
                WaterWaveProgress wa = returnGlasses(i);
                wa.setProgress(80);
            }

            if (glasses<7){

                waterjug.setProgress(Returnjug(glasses));
               waterjug2.setProgress(0);
                jug2.setVisibility(View.GONE);
                otherglasses.setVisibility(View.GONE);

            }else {
                waterjug.setProgress(80);
                waterjug2.setProgress(Returnjug2(glasses));
                jug2.setVisibility(View.VISIBLE);
                otherglasses.setVisibility(View.VISIBLE);

            }
        }

    }

    public WaterWaveProgress returnGlasses(int i){

        WaterWaveProgress currentView=new WaterWaveProgress(getActivity());
        switch (i){
            case 1:
                currentView=water1;
                pro=80;
                break;
            case 2:
                currentView=water2;
                pro2=80;
                break;
            case 3:
                currentView=water3;
                pro3=80;
                break;
            case 4:
                currentView=water4;
                pro4=80;
                break;
            case 5:
                currentView=water5;
                pro5=80;
                break;
            case 6:
                currentView=water6;
                pro6=80;

                break;
            case 7:
                currentView=water7;
                pro7=80;

                break;
            case 8:
                currentView=water8;
                pro8=80;

                break;
            case 9:
                currentView=water9;
                boo1=true;
                break;
            case 10:
                currentView=water10;
                pro10=80;

                break;
            case 11:
                currentView=water11;
                pro11=80;

                break;
            case 12:
                currentView=water12;
                pro12=80;

                break;
            case 13:
                currentView=water13;
                pro13=80;

                break;
            case 14:
                currentView=water14;
                pro14=80;

                break;
            case 15:
                currentView=water15;
                pro15=80;
                break;
            case 16:
                currentView=water16;
                pro16=80;
                break;

        }

        return currentView;
    }
    public int Returnjug(int i){

        int value=00;
        switch (i){
            case 0:
                value=0;
                break;
            case 1:
                value=10;
                break;
            case 2:
                value=20;
                break;
            case 3:
                value=30;
                break;
            case 4:
                value=40;
                break;
            case 5:
                value=50;
                break;
            case 6:
                value=60;
                break;
            case 7:
                value=70;;
                break;
            case 8:
                value=80;
                break;

        }

        return value;
    }

    public int Returnjug2(int i){

        int value=00;
        switch (i){
            case 8:
                value=0;
                break;
            case 9:
                value=10;
                break;
            case 10:
                value=20;
                break;
            case 11:
                value=30;
                break;
            case 12:
                value=40;
                break;
            case 13:
                value=50;
                break;
            case 14:
                value=60;
                break;
            case 15:
                value=70;;
                break;
            case 16:
                value=80;
                break;

        }

        return value;
    }


    public void updateJug1(int progress){
        waterjug.setProgress(progress);
    }

    public void updateJug2(int progress){
        waterjug2.setProgress(progress);
    }

    public void UpdateDataBase(String count){
    Cursor cursor = dop.getWaterInformation(dop, selectedDate);
    int co = cursor.getCount();
    int mli=Integer.valueOf(count)*glass_size;

    if (co == 1) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                unique_id = cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.WATER_UNIQUE_ID));
            } while (cursor.moveToNext());
            ContentValues cv = new ContentValues();
            cv.put(TableData.Tableinfo.GLASSES, String.valueOf(count));
            cv.put(TableData.Tableinfo.ML, String.valueOf(mli));
            cv.put(TableData.Tableinfo.WATER_SYNCED, "no");
            dop.updateWaterRow(dop, cv, unique_id);
            ml.setText(String.valueOf(mli)+" ml");

        }
    }else if (co == 0) {
        unique_id = String.valueOf(System.currentTimeMillis());
        dop.putWaterInformation(dop, unique_id,selectedDate,String.valueOf(count),String.valueOf(mli),String.valueOf(glass_size),"no");
        ml.setText(String.valueOf(mli)+" ml");
    }
}



    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        resetGlasses();
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
        Cursor cursor = dop.getWaterInformation(dop, selectedDate);
        if (cursor.getCount()==0){
            count=0;
            fillGlasses(count);


        }else if (cursor.getCount()==1) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    count = Integer.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.GLASSES)));
                    fillGlasses(count);
                    ml.setText(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ML))+" ml");
                } while (cursor.moveToNext());
            }
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
