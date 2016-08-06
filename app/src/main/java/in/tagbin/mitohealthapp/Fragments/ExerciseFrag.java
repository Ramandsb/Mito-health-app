package in.tagbin.mitohealthapp.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.joda.time.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.Interfaces.ExerciseInterface;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.CollapsableLogging;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.ExerciseAdapter;
import in.tagbin.mitohealthapp.ItemClickSupport;
import in.tagbin.mitohealthapp.MainPage;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.R;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class ExerciseFrag extends Fragment implements DatePickerDialog.OnDateSetListener,ExerciseInterface,OnDateSelectedListener {
    ArrayList<DataItems> database_list;
    RecyclerView food_list;
    ExerciseAdapter sleepAdapter;
    DatabaseOperations dop;
    RWeekCalendar rCalendarFragment;
    SharedPreferences login_details;
    String auth_key;
    ExerciseInterface exerciseInterface;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    public static String selectedDate = "";

    int a = 0, b = 0, c = 0;
    int i = 0;
    int mBgColor = 0;

    public ExerciseFrag() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dop = new DatabaseOperations(getActivity());
        login_details = getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, getActivity().MODE_PRIVATE);
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
        View view = inflater.inflate(R.layout.fragment_exer, container, false);

        food_list = (RecyclerView) view.findViewById(R.id.food_list);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        food_list.setItemAnimator(new SlideInLeftAnimator());
        food_list.getItemAnimator().setRemoveDuration(1000);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setInterpolator(new OvershootInterpolator());
// or recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f));
        food_list.setItemAnimator(animator);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        food_list.setLayoutManager(linearLayoutManager);
        database_list = new ArrayList<>();
        dop = new DatabaseOperations(getActivity());
        sleepAdapter = new ExerciseAdapter(getActivity());
        food_list.setAdapter(sleepAdapter);
        food_list.setHasFixedSize(true);
        database_list = dop.getExerciseInformation(dop, selectedDate);
        sleepAdapter.setData(database_list);
        sleepAdapter.notifyDataSetChanged();
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



        ItemClickSupport.addTo(food_list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

//                startActivity(new Intent(getActivity(), FoodDetails.class));
//
// akshayluthra12@

            }
        });
        if (database_list.isEmpty()) {

        } else {

        }
        Log.d("final selected date", selectedDate);

        return view;
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        ((CollapsableLogging)getActivity()).exerciseInterface = this;
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
    }

    @Override
    public void passDataToFragment(String selected) {
        Log.d("interfaceWorks",selected);
        database_list = dop.getExerciseInformation(dop, selected);
        sleepAdapter.setData(database_list);
        sleepAdapter.notifyDataSetChanged();
        selectedDate=selected;

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
    }
    private String getSelectedDatesString() {
        CalendarDay date = widget.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
}