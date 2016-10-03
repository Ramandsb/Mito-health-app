package in.tagbin.mitohealthapp.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
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
import in.tagbin.mitohealthapp.helper.PourBeerTask;
import in.tagbin.mitohealthapp.R;
import uk.co.barbuzz.beerprogressview.BeerProgressView;

public class WaterFragment extends Fragment implements OnDateSelectedListener {
    BeerProgressView water1,water2,water3,water4,water5,water6,water7,water8,water9;
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
//    private void animWave(WaveProgressView waveProgressView, long duration){
//        ObjectAnimator progressAnim = ObjectAnimator.ofInt(waveProgressView, "progress", 0, waveProgressView.getMax());
//        progressAnim.setDuration(duration);
////        progressAnim.setRepeatCount(ObjectAnimator.INFINITE);
////        progressAnim.setRepeatMode(ObjectAnimator.RESTART);
//        progressAnim.start();
//    }
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
        water1= (BeerProgressView) view.findViewById(R.id.DrawingwaterView1);
        water2= (BeerProgressView) view.findViewById(R.id.DrawingwaterView2);
        water3= (BeerProgressView) view.findViewById(R.id.DrawingwaterView3);
        water4= (BeerProgressView) view.findViewById(R.id.DrawingwaterView4);
        water5= (BeerProgressView) view.findViewById(R.id.DrawingwaterView5);
        water6= (BeerProgressView) view.findViewById(R.id.DrawingwaterView6);
        water7= (BeerProgressView) view.findViewById(R.id.DrawingwaterView7);
        water8= (BeerProgressView) view.findViewById(R.id.DrawingwaterView8);
        water9= (BeerProgressView) view.findViewById(R.id.complete_beer);
        Cursor cursor = dop.getWaterInformation(dop, selectedDate);
        if (cursor.getCount()==0){
            count=0;
            fillGlasses(count,"start");


        }else if (cursor.getCount()==1) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    count = Integer.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.GLASSES)));
                    fillGlasses(count,"start");

                    ml.setText(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ML))+" ml");
                    Log.d("count",count+"");

                } while (cursor.moveToNext());
            }
        }






        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water1,80,0);
                if (boo1){
                    pourBeerTask.cancel(true);
                    water1.setBeerProgress(0);
                    boo1=false;
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    Log.d("cancel","tr");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));


                }else {
                    pourBeerTask.execute(true);
                    boo1=true;
                    count++;

                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    Log.d("else","tr");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));
                }



            }
        });
        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask (getActivity(), water2,80,0);
                if (boo2){
                    pourBeerTask.cancel(true);
                    water2.setBeerProgress(0);
                    boo2=false;
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    Log.d("cancel","tr");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));
                }else {
                    pourBeerTask.execute(true);
                    count++;
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    boo2=true;

                    Log.d("jug",jugcount+"///");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));

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
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    Log.d("cancel","tr");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));
                }else {
                    pourBeerTask.execute(true);
                    count++;
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    boo3=true;

                    Log.d("jug",jugcount+"///");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));

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
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    Log.d("cancel","tr");
                    Log.d("count",count+"");
                }else {
                    pourBeerTask.execute(true);
                    count++;
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    boo4=true;

                    Log.d("jug",jugcount+"///");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));

                }            }
        });




        g5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water5,80,0);
                if (boo5){
                    pourBeerTask.cancel(true);
                    water5.setBeerProgress(0);
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    boo5=false;
                    Log.d("cancel","tr");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));
                }else {
                    pourBeerTask.execute(true);
                    count++;
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    boo5=true;

                    Log.d("jug",jugcount+"///");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));

                }                   }
        });
        g6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water6,80,0);
                if (boo6){
                    pourBeerTask.cancel(true);
                    water6.setBeerProgress(0);
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    boo6=false;
                    Log.d("cancel","tr");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));
                }else {
                    pourBeerTask.execute(true);
                    count++;
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    boo6=true;

                    Log.d("jug",jugcount+"///");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));

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
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    Log.d("cancel","tr");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));
                }else {
                    pourBeerTask.execute(true);
                    count++;
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    boo7=true;

                    Log.d("jug",jugcount+"///");
                    Log.d("count",count+"");
                    UpdateDataBase(String.valueOf(count));

                }                     }
        });
        g8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PourBeerTask pourBeerTask=new PourBeerTask(getActivity(), water8,80,0);
                if (boo8){
                    pourBeerTask.cancel(true);
                    water8.setBeerProgress(0);
                    count--;
                    water9.setBeerProgress(Returnjug(count));
                    jugcount=Returnjug(count);
                    Log.d("count",count+"");
                    boo8=false;
                    Log.d("cancel","tr");
                    UpdateDataBase(String.valueOf(count));
                }else {
                    pourBeerTask.execute(true);
                    boo8=true;
                    count++;
                    Log.d("else","tr");
                    PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(count),jugcount);
                    jugcount=Returnjug(count);
                    Task.execute(true);
                    Log.d("count",count+"");
                    Log.d("jug",jugcount+"///");
                    UpdateDataBase(String.valueOf(count));

                }
                    }
        });
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    public void fillGlasses(int i,String source){

        if (i==0){
            water1.setBeerProgress(0);
            water2.setBeerProgress(0);
            water3.setBeerProgress(0);
            water4.setBeerProgress(0);
            water5.setBeerProgress(0);
            water6.setBeerProgress(0);
            water7.setBeerProgress(0);
            water8.setBeerProgress(0);
            water9.setBeerProgress(0);
            ml.setText("00 ml");
            boo1=false;
            boo2=false;
            boo3=false;
            boo4=false;
            boo5=false;
            boo6=false;
            boo7=false;
            boo8=false;

        }else if (i>=1){
            water1.setBeerProgress(0);
            water2.setBeerProgress(0);
            water3.setBeerProgress(0);
            water4.setBeerProgress(0);
            water5.setBeerProgress(0);
            water6.setBeerProgress(0);
            water7.setBeerProgress(0);
            water8.setBeerProgress(0);
            boo1=false;
            boo2=false;
            boo3=false;
            boo4=false;
            boo5=false;
            boo6=false;
            boo7=false;
            boo8=false;
            for (int j = 1; j <= i; j++) {

                if (source.equals("start")){
                        PourBeerTask pourBeerTask = new PourBeerTask(getActivity(), ReturnImageView(j), 80, 0);
                        pourBeerTask.execute(true);

                }else if (source.equals("click")){
                    ReturnImageView(j).setBeerProgress(80);
                    water9.setBeerProgress(Returnjug(i));
                    jugcount=Returnjug(i);




                }
                PourBeerTask Task=new PourBeerTask(getActivity(), water9,Returnjug(i),jugcount);
                jugcount=Returnjug(i);
                Task.execute(true);
            }
        }


    }

    public BeerProgressView ReturnImageView(int i){

        BeerProgressView currentView=new BeerProgressView(getActivity());
        switch (i){
            case 1:
                currentView=water1;
                boo1=true;
                break;
            case 2:
                currentView=water2;
                boo2=true;
                break;
            case 3:
                currentView=water3;
                boo3=true;
                break;
            case 4:
                currentView=water4;
                boo4=true;
                break;
            case 5:
                currentView=water5;
                boo5=true;
                break;
            case 6:
                currentView=water6;
                boo6=true;
                break;
            case 7:
                currentView=water7;
                boo7=true;
                break;
            case 8:
                currentView=water8;
                boo8=true;
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
                value=8;
                break;
            case 2:
                value=16;
                break;
            case 3:
                value=24;
                break;
            case 4:
                value=35;
                break;
            case 5:
                value=45;
                break;
            case 6:
                value=55;
                break;
            case 7:
                value=65;;
                break;
            case 8:
                value=70;
                break;

        }

        return value;
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
            fillGlasses(count,"click");


        }else if (cursor.getCount()==1) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    count = Integer.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.GLASSES)));
                    fillGlasses(count,"click");
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
