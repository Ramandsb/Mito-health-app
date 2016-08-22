package in.tagbin.mitohealthapp;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
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

public class CollapsableLogging extends AppCompatActivity implements OnChartValueSelectedListener, SheetLayout.OnFabAnimationEndListener{

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

    CollapsingToolbarLayout appBarLayout;
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        dop = new DatabaseOperations(this);
        sendDate=new Intent(SENDDATE);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
                    appBarLayout.setBackgroundResource(R.color.bluegrey_pri);
                    tabLayout.setBackgroundResource(R.color.bluegrey_pri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.bluegrey_acc));

                    }


                }
                if (position==0){
                    fab.show();
                    appBarLayout.setBackgroundResource(R.color.colorPrimary);
                    tabLayout.setBackgroundResource(R.color.colorPrimary);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

                    }

                }
                if (position==2){
                    fab.show();
                    appBarLayout.setBackgroundResource(R.color.mdtp_red);
                    tabLayout.setBackgroundResource(R.color.mdtp_red);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.mdtp_red_focused));
                    }

                }
                if (position==3){
                    fab.hide();

                    appBarLayout.setBackgroundResource(R.color.grey_pri);
                    tabLayout.setBackgroundResource(R.color.grey_pri);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(getResources().getColor(R.color.grey_acc));
                    }
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
       int i = getIntent().getIntExtra("selection",0);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(i);

//        calenderTrans();

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
        mChart.getAxisLeft().setEnabled(true);
        mChart.getAxisRight().setDrawAxisLine(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setDrawAxisLine(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getXAxis().setTextColor(Color.parseColor("#ffffff"));
        mChart.setDescriptionColor(Color.parseColor("#ffffff"));
        mChart.getAxisLeft().setTextColor(Color.parseColor("#ffffff"));
        mChart.getAxisRight().setTextColor(Color.parseColor("#ffffff"));
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        Legend l = mChart.getLegend(); ////////////////////////////dataset values show hint
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART_INSIDE);
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
            case android.R.id.home: {
                startActivity(new Intent(CollapsableLogging.this,BinderActivity.class).putExtra("selection",2).putExtra("source","indirect"));
                finish();
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


        }
    }

    @Override
    public void onBackPressed() {
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




        }

        ArrayList<Entry> values = new ArrayList<Entry>();
        values.add(new Entry(0, (float) 30));
        values.add(new Entry(1, (float) 40));
        values.add(new Entry(2, (float) 70));
        values.add(new Entry(3, (float) 20));
        values.add(new Entry(4, (float) 100));
        values.add(new Entry(5, (float) 10));
        values.add(new Entry(6, (float) 150));

        ArrayList<Entry> values2 = new ArrayList<Entry>();
        values2.add(new Entry(0, (float) 40));
        values2.add(new Entry(1, (float) 50));
        values2.add(new Entry(2, (float) 80));
        values2.add(new Entry(3, (float) 10));
        values2.add(new Entry(4, (float) 120));
        values2.add(new Entry(5, (float) 30));
        values2.add(new Entry(6, (float) 50));
        LineDataSet d = new LineDataSet(values, "Required");
        d.setLineWidth(2.5f);
        d.setCircleRadius(4f);
        LineDataSet d1 = new LineDataSet(values2, "Consumed");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4f);

//        int color = mColors[z % mColors.length];
//        d.setColor(getResources().getColor(R.color.red));
//        d.setCircleColor(getResources().getColor(R.color.red));
        dataSets.add(d);
        dataSets.add(d1);
        int col_consumed=getResources().getColor(R.color.white);
        int col_reql=getResources().getColor(R.color.red);
        // make the first DataSet dashed
//        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
        ((LineDataSet) dataSets.get(0)).setColors(new int[]{col_consumed});
        ((LineDataSet) dataSets.get(0)).setCircleColors(new int[]{col_consumed});

        ((LineDataSet) dataSets.get(1)).setColors(new int[]{col_reql});
        ((LineDataSet) dataSets.get(1)).setCircleColors(new int[]{col_reql});

        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.invalidate();
    }
}
