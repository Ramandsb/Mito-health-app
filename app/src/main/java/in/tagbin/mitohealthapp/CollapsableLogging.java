package in.tagbin.mitohealthapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.DatePicker;

import com.github.fabtransitionactivity.SheetLayout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.tagbin.mitohealthapp.CalenderView.CalenderListener;
import in.tagbin.mitohealthapp.Interfaces.ExerciseInterface;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.CalenderView.WeekFragment;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Fragments.ExerciseFrag;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Fragments.SleepFrag;
import in.tagbin.mitohealthapp.Fragments.WaterFrag;
import in.tagbin.mitohealthapp.Interfaces.FoodInterface;
import in.tagbin.mitohealthapp.Interfaces.SleepInterface;
import in.tagbin.mitohealthapp.Interfaces.WaterInterface;

public class CollapsableLogging extends AppCompatActivity implements OnChartValueSelectedListener, SheetLayout.OnFabAnimationEndListener, TimeRangePickerDialog.OnTimeRangeSelectedListener{

    private LineChart mChart;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    static SheetLayout mSheetLayout;
    DatabaseOperations dop;
    Drawable myDrawable;
    FloatingActionButton fab;
    String title;
    RWeekCalendar rCalendarFragment;
   public ExerciseInterface exerciseInterface;
public FoodInterface foodInterface;
    public WaterInterface waterInterface;
    public SleepInterface sleepInterface;
    public static String selectedDate="";

    int a=0,b=0,c=0;
    int i = 0;
    int mBgColor=0;
    Intent sendDate;
    public static String SENDDATE="sendDate";
    private int[] tabIcons= {
            R.drawable.food_tab,R.drawable.water_tab,R.drawable.exercise_tab,R.drawable.sleep_tab
    };

    int currentFrag = 0;
    public static final String TIMERANGEPICKER_TAG = "timerangepicker";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsable_logging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        dop = new DatabaseOperations(this);
        sendDate=new Intent(SENDDATE);
        if (savedInstanceState != null) {
            TimeRangePickerDialog tpd = (TimeRangePickerDialog) this.getSupportFragmentManager()
                    .findFragmentByTag(TIMERANGEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeRangeSetListener(this);
            }
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFrag = position;
                if (position==1){
                    fab.hide();
                }
                if (position==0){
                    fab.show();
                }
                if (position==2){
                    fab.show();
                }
                if (position==3){
                    fab.hide();
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       int i = getIntent().getIntExtra("selection",0);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setCurrentItem(i);

//        calenderTrans();
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setDrawBorders(false);

        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setDrawAxisLine(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
        mChart.setDescriptionColor(Color.parseColor("#ffffff"));

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(false);
//        Legend l = mChart.getLegend(); ////////////////////////////dataset values show hint
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        addData();

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
//    public void calenderTrans(){
//        rCalendarFragment = new RWeekCalendar();
//        mBgColor = getResources().getColor(R.color.sample_bg);
//        LocalDateTime mSelectedDate = LocalDateTime.now();
//        int day = mSelectedDate.getDayOfMonth();
//        int month = mSelectedDate.getMonthOfYear();
//        int year = mSelectedDate.getYear();
//
//        if (String.valueOf(month).length()<2){
//            selectedDate=year+"-"+"0"+month+"-"+day;
//
//        }else {
//            selectedDate = year + "-" + month + "-" + day;
//        }
//        CalenderListener listener = new CalenderListener() {
//            @Override
//            public void onSelectPicker() {
//
//                //User can use any type of pickers here the below picker is only Just a example
//
////                DatePickerDialog.newInstance(Sample.this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
//
//
//            }
//
//            @Override
//            public void onSelectDate(LocalDateTime mSelectedDate) {
//
//                //callback when a date is selcted
//                a=mSelectedDate.getDayOfMonth();
//                b=mSelectedDate.getMonthOfYear();
//                c=mSelectedDate.getYear();
//
//                if (WeekFragment.click_hppn.equals("yes")){
//                    if (String.valueOf(b).length()<2){
//                        selectedDate=c+"-"+"0"+b+"-"+a;
//
//
//                    }else {
//                        selectedDate=c+"-"+b+"-"+a;
//
//                    }
//                    if (currentFrag==0){
//                        foodInterface.passDataToFoodFragment(selectedDate);
//                    }else if (currentFrag==1){
//waterInterface.passDataToWaterFragment(selectedDate);
//                    }else if (currentFrag==2){
//                        exerciseInterface.passDataToFragment(selectedDate);
//                    }else if (currentFrag==3){
//sleepInterface.passDataToSleepFragment(selectedDate);
//                    }
//
//
//                    Log.d("Click_happend", "--------only -----" + selectedDate+"----------------");
//                    WeekFragment.click_hppn="no";
//                }
//
//
////                 + mSelectedDate.getDayOfMonth() + "-" + mSelectedDate.getMonthOfYear() + "-" + mSelectedDate.getYear());
////                mDateSelectedTv.setText(""+mSelectedDate.getDayOfMonth()+"-"+mSelectedDate.getMonthOfYear()+"-"+mSelectedDate.getYear());
//
//
//
//            }
//        };
//
//        //setting the listener
//        rCalendarFragment.setCalenderListener(listener);
//
//
//        Bundle args = new Bundle();
//
//       /*Should add this attribute if you adding  the NOW_BACKGROUND or DATE_SELECTOR_BACKGROUND Attribute*/
//        args.putString(RWeekCalendar.PACKAGENAME, this.getPackageName());
//
//       /* IMPORTANT: Customization for the calender commenting or un commenting any of the attribute below will reflect change in calender*/
//
////---------------------------------------------------------------------------------------------------------------------//
//
////      args.putInt(RWeekCalender.CALENDER_BACKGROUND, ContextCompat.getColor(this,R.color.md_pink_700));//set background color to calender
//
//        args.putString(RWeekCalendar.DATE_SELECTOR_BACKGROUND, "bg_select");//set background to the selected dates
//
//        args.putInt(RWeekCalendar.WEEKCOUNT, 1000);//add N weeks from the current week (53 or 52 week is one year)
//
////        args.putString(RWeekCalender.NOW_BACKGROUND,"bg_now");//set background to nowView
//
//        args.putInt(RWeekCalendar.CURRENT_DATE_BACKGROUND, ContextCompat.getColor(this, R.color.md_black_1000));//set color to the currentdate
//
////        args.putInt(RWeekCalender.PRIMARY_BACKGROUND, ContextCompat.getColor(this,R.color.md_white_1000));//Set color to the primary views (Month name and dates)
//
////        args.putInt(RWeekCalender.SECONDARY_BACKGROUND, ContextCompat.getColor(this,R.color.md_green_500));//Set color to the secondary views (now view and week names)
//
////---------------------------------------------------------------------------------------------------------------------//
//
//        rCalendarFragment.setArguments(args);
//
//        // Attach to the activity
//        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//        t.replace(R.id.container_colaps, rCalendarFragment);
//        t.commit();
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionSave: {
                // mChart.saveToGallery("title"+System.currentTimeMillis());
                mChart.saveToPath("title" + System.currentTimeMillis(), "");
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000);
                break;
            }
            case R.id.animateXY: {

                mChart.animateXY(3000, 3000);
                break;
            }
        }
        return true;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FoodFrag(), "Food");
        adapter.addFragment(new WaterFrag(), "Water");
        adapter.addFragment(new ExerciseFrag(), "Exercise");
        adapter.addFragment(new SleepFrag(), "Sleep");
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
            final TimeRangePickerDialog timePickerDialog = TimeRangePickerDialog.newInstance(
                    CollapsableLogging.this, false);
            timePickerDialog.show(this.getSupportFragmentManager(), TIMERANGEPICKER_TAG);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mSheetLayout.contractFab();
        }
    }

    @Override
    public void onTimeRangeSelected(int startHour, int startMin, int endHour, int endMin) {
        String start_time = startHour + ":" + startMin + ":00";
        String end_time = endHour + ":" + endMin + ":00";


        Log.d("sleep date", SleepFrag.selectedDate);
        dop.putSleepInformation(dop, String.valueOf(System.currentTimeMillis()), start_time, end_time, SleepFrag.selectedDate);
    }

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

    public void addData() {
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        for (int z = 0; z < 2; z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 0; i < 10; i++) {
                double val = (Math.random() * 100) + 3;
                values.add(new Entry(i, (float) val));
            }

            LineDataSet d = new LineDataSet(values, "DataSet " + (z + 1));
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);

            int color = mColors[z % mColors.length];
            d.setColor(color);
            d.setCircleColor(color);
            dataSets.add(d);
        }

        // make the first DataSet dashed
        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }
}
