package in.tagbin.mitohealthapp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import com.github.fabtransitionactivity.SheetLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.LocalDateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.tagbin.mitohealthapp.CalenderView.CalenderListener;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.FeelingsFrag;
import in.tagbin.mitohealthapp.Interfaces.ExerciseInterface;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.CalenderView.WeekFragment;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Fragments.ExerciseFrag;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Fragments.SleepFrag;
import in.tagbin.mitohealthapp.Fragments.WaterFrag;
import in.tagbin.mitohealthapp.Interfaces.FoodInterface;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.Interfaces.SleepInterface;
import in.tagbin.mitohealthapp.Interfaces.WaterInterface;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.DateRangeDataModel;
import in.tagbin.mitohealthapp.model.FeelingLogModel;
import in.tagbin.mitohealthapp.model.FeelingTimeConsumed;
import in.tagbin.mitohealthapp.model.SleepLogModel;
import in.tagbin.mitohealthapp.model.SleepTimeconsumed;
import in.tagbin.mitohealthapp.model.Timeconsumed;
import in.tagbin.mitohealthapp.model.WaterLogModel;
import pl.droidsonroids.gif.GifImageView;

public class CollapsableLogging extends AppCompatActivity implements OnChartValueSelectedListener, SheetLayout.OnFabAnimationEndListener{

    private LineChart mChart;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    static SheetLayout mSheetLayout;
    DatabaseOperations dop;
    FloatingActionButton fab;
    public static String selectedDate="";

    CollapsingToolbarLayout appBarLayout;
    int a=0,b=0,c=0;
    int i = 0;
    Intent sendDate;
    public static String SENDDATE="sendDate";
    private int[] tabIcons= {
            R.drawable.foodicon_svg,R.drawable.watericon_svg,R.drawable.exericon_svg,R.drawable.sleep_menu,R.drawable.feeling
    };
    public static GifImageView progressBar;
    int currentFrag = 0;
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsable_logging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        dop = new DatabaseOperations(this);
        sendDate=new Intent(SENDDATE);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        int i = getIntent().getIntExtra("selection",0);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(i);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFrag = position;
               setChartBackground(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        calenderTrans();

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setDrawBorders(false);
        mChart.getAxisLeft().setEnabled(true);
        mChart.getAxisRight().setEnabled(false);

        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setDrawAxisLine(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
        mChart.setDescriptionColor(Color.parseColor("#ffffff"));
        mChart.getAxisLeft().setTextColor(Color.parseColor("#ffffff"));
        mChart.getAxisRight().setTextColor(Color.parseColor("#ffffff"));
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new AxisValueFormatter() {


            private SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mFormat.format(new Date((long) value));
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(false);
        Legend l = mChart.getLegend(); ////////////////////////////dataset values show hint
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);



        setChartBackground(i);



        mSheetLayout.setFab(fab);
        mSheetLayout.setFabAnimationEndListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetLayout.expandFab();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    public void setChartBackground(int position){
        if (position==1){
            fab.hide();
            appBarLayout.setBackgroundResource(R.color.water);
            tabLayout.setBackgroundResource(R.color.water);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.wateracc));

            }

            addData(dop.getChartInformation(dop,HomePage.selectedDate),"water");

        }
        if (position==0){
            fab.show();
            appBarLayout.setBackgroundResource(R.color.food);
            tabLayout.setBackgroundResource(R.color.food);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.foodacc));

            }
            addData(dop.getChartInformation(dop,HomePage.selectedDate),"food");

        }
        if (position==2){
            fab.show();
            appBarLayout.setBackgroundResource(R.color.exercise);
            tabLayout.setBackgroundResource(R.color.exercise);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.exerciseacc));
            }
            addData(dop.getChartInformation(dop,HomePage.selectedDate),"exercise");
        }
        if (position==3){
            fab.hide();

            appBarLayout.setBackgroundResource(R.color.sleep);
            tabLayout.setBackgroundResource(R.color.sleep);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.sleepacc));
            }
            addData(dop.getChartInformation(dop,HomePage.selectedDate),"sleep");
        }

        if (position==4){
            fab.hide();

            appBarLayout.setBackgroundResource(R.color.feelings);
            tabLayout.setBackgroundResource(R.color.feelings);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.feelingsaacc));
            }
//            addDatatoFeelingsChart();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.actionToggleValues: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//
//                    set.setDrawValues(!set.isDrawValuesEnabled());
//                }
//
//                mChart.invalidate();
//                break;
//            }
            case android.R.id.home: {
                startActivity(new Intent(CollapsableLogging.this,BinderActivity.class).putExtra("selection",2).putExtra("source","indirect"));
                finish();
                break;
            }
//            case R.id.actionTogglePinch: {
//                if (mChart.isPinchZoomEnabled())
//                    mChart.setPinchZoom(false);
//                else
//                    mChart.setPinchZoom(true);
//
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleAutoScaleMinMax: {
//                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
//                mChart.notifyDataSetChanged();
//                break;
//            }
//            case R.id.actionToggleHighlight: {
//                if (mChart.getData() != null) {
//                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
//                    mChart.invalidate();
//                }
//                break;
//            }
//            case R.id.actionToggleFilled: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    if (set.isDrawFilledEnabled())
//                        set.setDrawFilled(false);
//                    else
//                        set.setDrawFilled(true);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionToggleCircles: {
//                List<ILineDataSet> sets = mChart.getData()
//                        .getDataSets();
//
//                for (ILineDataSet iSet : sets) {
//
//                    LineDataSet set = (LineDataSet) iSet;
//                    if (set.isDrawCirclesEnabled())
//                        set.setDrawCircles(false);
//                    else
//                        set.setDrawCircles(true);
//                }
//                mChart.invalidate();
//                break;
//            }
//            case R.id.actionSave: {
//                // mChart.saveToGallery("title"+System.currentTimeMillis());
//                mChart.saveToPath("title" + System.currentTimeMillis(), "");
//                break;
//            }
//            case R.id.animateX: {
//                mChart.animateX(3000);
//                break;
//            }
//            case R.id.animateY: {
//                mChart.animateY(3000);
//                break;
//            }
//            case R.id.animateXY: {
//
//                mChart.animateXY(3000, 3000);
//                break;
//            }
        }
        return true;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FoodFrag(), "");
        adapter.addFragment(new WaterFrag(), "");
        adapter.addFragment(new ExerciseFrag(), "");
        adapter.addFragment(new SleepFrag(), "");
        adapter.addFragment(new FeelingsFrag(), "");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onFabAnimationEnd() {
//        startActivity(new Intent(CollapsableLogging.this,DishSearch.class));
        if (currentFrag == 0) {
            Intent intent = new Intent(this, DishSearch.class);
            intent.putExtra("back","food");
            startActivityForResult(intent, REQUEST_CODE);
        } else if (currentFrag == 1) {

        } else if (currentFrag == 2) {
            Intent intent = new Intent(this, DishSearch.class);
            intent.putExtra("back","exercise");
            startActivityForResult(intent, REQUEST_CODE);

        } else if (currentFrag == 3) {


        }
    }

    @Override
    public void onBackPressed() {


//        try {
//            Log.d("waterDetails",addDbValuetoJsonArray().toString());
//        } catch (ParseException e) {
//            Log.e("waterDetailsErrors",e.toString());
//        }
//        try {
//            Controller.getWaterLog(this,addDbValuetoJsonArray(),mWaterLogListener);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        try {

            Cursor cursor= dop.getCompleteWaterInformation(dop,"no");
            Cursor cursorF= dop.getCompleteFeelingInformation(dop,"no");
            Log.d("Countwater",cursor.getCount()+"//");

            if (cursor.getCount()!=0) {
                Controller.getWaterLog(this, addDbValuetoJsonArray(), mWaterLogListener);
            }
            Controller.getSleepLog(this,addDbSleepValuetoJsonArray(),mSleepLogListener);
            if (cursorF.getCount()!=0) {
                Controller.getFeelingLog(this, addFeelingDbValuetoJsonArray(), mFeelingLogListener);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("count error","true");
        }
        startActivity(new Intent(CollapsableLogging.this,BinderActivity.class).putExtra("selection",2).putExtra("source","indirect"));
        finish();
        super.onBackPressed();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mSheetLayout.contractFab();
        }
    }

    public  WaterLogModel addDbValuetoJsonArray() throws ParseException {
       Cursor cursor= dop.getCompleteWaterInformation(dop,"no");
        Log.d("Countwater",cursor.getCount()+"//");

        WaterLogModel waterLogModel = new WaterLogModel();
        List<Timeconsumed> timeconsumeds = new ArrayList<Timeconsumed>();
        if (cursor != null && cursor.moveToFirst()) {

            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                Timeconsumed timeconsumed = new Timeconsumed();
                timeconsumed.setAmount(Integer.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.GLASSES))));
                timeconsumed.setC_id(1);
                timeconsumed.setTime_consumed(Double.valueOf(MyUtils.getUtcTimestamp(((cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.WATER_DATE)))+" 00:00:00"),"s")));
                timeconsumeds.add(timeconsumed);
            }
            while (cursor.moveToNext());
        }
        waterLogModel.setList(timeconsumeds);
        return waterLogModel;
    }

    public FeelingLogModel addFeelingDbValuetoJsonArray() throws ParseException {
        Cursor cursor= dop.getCompleteFeelingInformation(dop,"no");
        Log.d("Countwater",cursor.getCount()+"//");

        FeelingLogModel feelingLogModel = new FeelingLogModel();
        List<FeelingTimeConsumed> feelingTimeConsumeds = new ArrayList<FeelingTimeConsumed>();
        if (cursor != null && cursor.moveToFirst()) {

            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                FeelingTimeConsumed feelingTime = new FeelingTimeConsumed();
                feelingTime.setStress(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.STRESS)));
                feelingTime.setHappiness(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.HAPPINESS)));
                feelingTime.setEnergy(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ENERGY)));
                feelingTime.setConfidence(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CONFIDENCE)));
                feelingTime.setTime(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.FEELINGS_DATE)));
                feelingTimeConsumeds.add(feelingTime);
                  }
            while (cursor.moveToNext());
        }
        feelingLogModel.setList(feelingTimeConsumeds);
        return feelingLogModel;
    }

    public  SleepLogModel addDbSleepValuetoJsonArray() throws ParseException {
        Cursor cursor= dop.getCompleteSleepInformation(dop);

        SleepLogModel sleepLogModel = new SleepLogModel();
        List<SleepTimeconsumed> timeconsumeds = new ArrayList<SleepTimeconsumed>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                SleepTimeconsumed timeconsumed = new SleepTimeconsumed();
                timeconsumed.setEnd(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.END_TIME_STAMP)));
                timeconsumed.setStart(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.START_TIME_STAMP)));
                timeconsumed.setQos(Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.SLEEP_QUALITY))));
                timeconsumeds.add(timeconsumed);
            }
            while (cursor.moveToNext());
        }
        sleepLogModel.setList(timeconsumeds);
        return sleepLogModel;
    }

//    public long convertDateToTimeStamp(String date,String time) throws ParseException {
//
//        long time_stamp;
//
//        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date+" "+time+":00");
//        Log.d("convertedTimeStamp",startDate.toString()+"////"+startDate.getTime()+"////"+startDate.getDate());
//        time_stamp=startDate.getTime()/1000;
//
//        return time_stamp;
//    }


//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.set(year, monthOfYear, dayOfMonth);
//        rCalendarFragment.setDateWeek(calendar);
//    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }

    private int[] mColors = new int[]{
            ColorTemplate.VORDIPLOM_COLORS[0],
            ColorTemplate.VORDIPLOM_COLORS[1],
            ColorTemplate.VORDIPLOM_COLORS[2]
    };

    public void addData(ArrayList<DateRangeDataModel> list,String source) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        Log.d("addData",list.toString());




        ArrayList<Entry> values = new ArrayList<Entry>();
//        values.add(new Entry(1471890600000L, 40));
//        values.add(new Entry(1471977000000L, 10));
//        values.add(new Entry(1472063400000L, 80));
//        values.add(new Entry(1472149800000L, 0));
//        values.add(new Entry(1472236200000L, 90));
//        values.add(new Entry(1472322600000L, 10));
//        values.add(new Entry(1472409000000L, 50));
//        values.add(new Entry(1472495400000L, 33));

        ArrayList<Entry> values2 = new ArrayList<Entry>();
//
//        values2.add(new Entry(1471977000000L, 70));
//        values2.add(new Entry(1472063400000L, 30));
//        values2.add(new Entry(1472149800000L, 100));
//        values2.add(new Entry(1472236200000L, 10));
//        values2.add(new Entry(1472322600000L, 100));
//        values2.add(new Entry(1472409000000L, 40));
//        values2.add(new Entry(1472495400000L, 33));
        DateRangeDataModel dm;
        for (int i =0;i<list.size();i++){
            dm= list.get(i);
            if (source.equals("food")){
                Log.d("value to chartReq", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getFood_req()));
                Log.d("value to chartCons", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getFood_con()));
                values.add(new Entry(Long.valueOf(dm.getTimestamp()),Float.valueOf(dm.getFood_req())));
                values2.add(new Entry(Long.valueOf(dm.getTimestamp()), Float.valueOf(dm.getFood_con())));
            }else if (source.equals("water")){
                Log.d("value to chartReq", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getWater_req()));
                Log.d("value to chartCons", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getWater_con()));
                values.add(new Entry(Long.valueOf(dm.getTimestamp()),Float.valueOf(dm.getWater_req())));
                values2.add(new Entry(Long.valueOf(dm.getTimestamp()), Float.valueOf(dm.getWater_con())));
            }else if (source.equals("exercise")){
                Log.d("value to chartReq", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getExer_req()));
                Log.d("value to chartCons", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getExer_con()));
                values.add(new Entry(Long.valueOf(dm.getTimestamp()),Float.valueOf(dm.getExer_req())));
                values2.add(new Entry(Long.valueOf(dm.getTimestamp()), Float.valueOf(dm.getExer_con())));
            }else if (source.equals("sleep")){
                Log.d("value to chartReq", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getSleep_req()));
                Log.d("value to chartCons", Long.valueOf(dm.getTimestamp())+"///////"+Float.valueOf(dm.getSleep_con()));
                values.add(new Entry(Long.valueOf(dm.getTimestamp()),Float.valueOf(dm.getSleep_req())));
                values2.add(new Entry(Long.valueOf(dm.getTimestamp()), Float.valueOf(dm.getSleep_con())));
            }

        }

        LineDataSet d = new LineDataSet(values, "Required");
        d.setLineWidth(2.5f);
        d.setCircleRadius(4f);
        d.setValueTextColor(Color.WHITE);
        d.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineDataSet d1 = new LineDataSet(values2, "Consumed");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4f);
        d1.setDrawFilled(true);
        d1.setValueTextColor(Color.WHITE);
        d1.setDrawFilled(true);
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);


//        int color = mColors[z % mColors.length];
//        d.setColor(getResources().getColor(R.color.red));
//        d.setCircleColor(getResources().getColor(R.color.red));
        dataSets.add(d);
        dataSets.add(d1);
        int col_consumed=0;int col_reql=0;
        col_consumed =getResources().getColor(R.color.white);
        if (source.equals("food")){

            col_reql =getResources().getColor(R.color.foodline);

        }else if (source.equals("water")){
            col_reql =getResources().getColor(R.color.waterline);

        }else if (source.equals("exercise")){
            col_reql =getResources().getColor(R.color.exerciseline);
        }else if (source.equals("sleep")){

            col_reql =getResources().getColor(R.color.sleepline);
        }

        // make the first DataSet dashed
//        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
        ((LineDataSet) dataSets.get(0)).setColors(new int[]{col_consumed});
        ((LineDataSet) dataSets.get(0)).setCircleColors(new int[]{col_consumed});

        ((LineDataSet) dataSets.get(1)).setColors(new int[]{col_reql});
        ((LineDataSet) dataSets.get(1)).setCircleColors(new int[]{col_reql});

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.animateY(3000);
        mChart.invalidate();

    }

    public void addDatatoFeelingsChart(){
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();




        ArrayList<Entry> values2 = new ArrayList<Entry>();
      Cursor cursor=  dop.getFeelingsInformation(dop,"yes");
        if (cursor != null && cursor.moveToFirst()) {
            do {
               String  time=cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.FEELINGS_DATE));
               String  avg=cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.AVERAGE));
                values2.add(new Entry(Long.valueOf(time+"000"), Float.valueOf(avg)));


            }
            while (cursor.moveToNext());
        }




        LineDataSet d1 = new LineDataSet(values2, "Feelings");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4f);
        d1.setDrawFilled(true);
        d1.setValueTextColor(Color.WHITE);
        d1.setDrawFilled(true);
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);


//        int color = mColors[z % mColors.length];
//        d.setColor(getResources().getColor(R.color.red));
//        d.setCircleColor(getResources().getColor(R.color.red));

        dataSets.add(d1);
        int col_consumed=getResources().getColor(R.color.feelingsline);
        // make the first DataSet dashed
//        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
        ((LineDataSet) dataSets.get(0)).setColors(new int[]{col_consumed});
        ((LineDataSet) dataSets.get(0)).setCircleColors(new int[]{col_consumed});


        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.animateY(3000);
        mChart.invalidate();

    }
    RequestListener mWaterLogListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("WaterLogging response",responseObject.toString());
            Cursor cursor= dop.getCompleteWaterInformation(dop,"no");
            Log.d("Countwater",cursor.getCount()+"//");


            if (cursor != null && cursor.moveToFirst()) {

                do {
                    //create a new movie object and retrieve the data from the cursor to be stored in this movie object
              String unique_id=cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.WATER_UNIQUE_ID));
                    ContentValues cv = new ContentValues();
                    cv.put(TableData.Tableinfo.WATER_SYNCED,"yes");
                    dop.updateWaterRow(dop,cv,unique_id);

                }
                while (cursor.moveToNext());
            }


        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("WaterLogging Error",message.toString());

        }
    };

    RequestListener mSleepLogListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("Sleep  response",responseObject.toString());

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("Sleep Error",message.toString());

        }
    };

    RequestListener mFeelingLogListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("Feeling  response",responseObject.toString());
            Cursor cursor= dop.getCompleteFeelingInformation(dop,"no");
            Log.d("CountFeeling",cursor.getCount()+"//");

            if (cursor != null && cursor.moveToFirst()) {

                do {
                    //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                    String unique_id=cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.FEELINGS_DATE));
                    ContentValues cv = new ContentValues();
                    cv.put(TableData.Tableinfo.FEELING_SYNCED,"yes");
                    dop.updateAnyRow(dop, TableData.Tableinfo.TABLE_NAME_FEELINGS,cv, TableData.Tableinfo.FEELINGS_DATE,unique_id);

                }
                while (cursor.moveToNext());
            }



        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("Feeling Error",message.toString());

        }
    };

    public void getAllDataforchart() {

        ArrayList<DateRangeDataModel> list = new ArrayList<>();
        DateRangeDataModel dm = new DateRangeDataModel();
        list = dop.getChartInformation(dop, selectedDate + " 00:00:00");
        for (int i = 0; i < list.size(); i++) {
            dm = list.get(i);
            Log.d("detailsfromdb", dm.getDate() + "//" + dm.getFood_req() + "//" + dm.getFood_con() + "//" + dm.getExer_con() + "//" + dm.getTimestamp() + "//" + dm.getWater_con());
        }
    }
    }
