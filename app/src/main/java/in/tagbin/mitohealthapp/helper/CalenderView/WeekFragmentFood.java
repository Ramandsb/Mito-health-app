package in.tagbin.mitohealthapp.helper.CalenderView;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Calendar;

import in.tagbin.mitohealthapp.app.AppController;
import in.tagbin.mitohealthapp.R;

/**
 * The MIT License (MIT)

 Copyright (c) 2015 Ramesh M Nair

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE..
 */
public class WeekFragmentFood extends Fragment {

    //Declaring Variables

    LocalDateTime mSelectedDate,startDate,mCurrentDate;

    TextView sundayTV, mondayTv,tuesdayTv, wednesdayTv, thursdayTv,fridayTv,saturdayTv,week_s,week_m,week_t,week_w,week_th,week_f,week_sa;
    TextView[] textViewArray = new TextView[7];

    int datePosition=0,selectorDateIndicatorValue=0,currentDateIndicatorValue=0;
    ArrayList<LocalDateTime>dateInWeekArray=new ArrayList<>();
    int currentDay=0;
    public static String click_hppn="";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_wek, container, false);

        sundayTV=(TextView)view.findViewById(R.id.sun_txt);
        mondayTv=(TextView)view.findViewById(R.id.mon_txt);
        tuesdayTv=(TextView)view.findViewById(R.id.tue_txt);
        wednesdayTv=(TextView)view.findViewById(R.id.wen_txt);
        thursdayTv=(TextView)view.findViewById(R.id.thu_txt);
        fridayTv=(TextView)view.findViewById(R.id.fri_txt);
        saturdayTv=(TextView)view.findViewById(R.id.sat_txt);

        week_s=(TextView)view.findViewById(R.id.week_s);
        week_m=(TextView)view.findViewById(R.id.week_m);
        week_t=(TextView)view.findViewById(R.id.week_t);
        week_w=(TextView)view.findViewById(R.id.week_w);
        week_th=(TextView)view.findViewById(R.id.week_th);
        week_f=(TextView)view.findViewById(R.id.week_f);
        week_sa=(TextView)view.findViewById(R.id.week_sa);


        /*Adding WeekViews to array for background changing purpose*/
        textViewArray[0]=sundayTV;
        textViewArray[1]=mondayTv;
        textViewArray[2]=tuesdayTv;
        textViewArray[3]=wednesdayTv;
        textViewArray[4]=thursdayTv;
        textViewArray[5]=fridayTv;
        textViewArray[6]=saturdayTv;



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*Setting the date info in the Application class*/

        startDate= AppController.getInstance().getDate();
        mCurrentDate=AppController.getInstance().getDate();


        /*Setting the Resources values and Customization values to the views*/

        Resources resources = getActivity().getResources();

        selectorDateIndicatorValue = resources.getIdentifier(getArguments().getString(RWeekCalendar.DATE_SELECTOR_BACKGROUND), "drawable",
                RWeekCalendar.PAKAGENAMEVALUE);

        currentDateIndicatorValue=getArguments().getInt(RWeekCalendar.CURRENT_DATE_BACKGROUND);


        datePosition=getArguments().getInt(RWeekCalendar.POSITIONKEY);
        int addDays=datePosition*7;

        startDate = startDate.plusDays(addDays);//Adding the 7days to the previous week

        mSelectedDate=AppController.getInstance().getSelected();


        textViewArray[0].setBackgroundResource(selectorDateIndicatorValue);
        textViewArray[0].setTextColor(getResources().getColor(R.color.bottombar));//Setting the first days of the week as selected


      /*Fetching the data's for the week to display*/

        for (int i = 0; i < 7; i++) {

            if(mSelectedDate!=null) {

                if (mSelectedDate.getDayOfMonth() == startDate.getDayOfMonth()) {

                   /*Indicate  if the day is selected*/

                    textViewArray[i].setBackgroundResource(selectorDateIndicatorValue);
                    textViewArray[i].setTextColor(getResources().getColor(R.color.bottombar));

                    mDateSelectedBackground(i);//Changing View selector background

                    AppController.getInstance().setSelected(null);//null the selected date

                }
            }

            dateInWeekArray.add(startDate);//Adding the days in the selected week to list


            startDate = startDate.plusDays(1); //Next day


        }

        /*Setting color in the week views*/

        sundayTV.setTextColor(getArguments().getInt(RWeekCalendar.PRIMARY_BACKGROUND));
        mondayTv.setTextColor(getArguments().getInt(RWeekCalendar.PRIMARY_BACKGROUND));
        tuesdayTv.setTextColor(getArguments().getInt(RWeekCalendar.PRIMARY_BACKGROUND));
        wednesdayTv.setTextColor(getArguments().getInt(RWeekCalendar.PRIMARY_BACKGROUND));
        thursdayTv.setTextColor(getArguments().getInt(RWeekCalendar.PRIMARY_BACKGROUND));
        fridayTv.setTextColor(getArguments().getInt(RWeekCalendar.PRIMARY_BACKGROUND));
        saturdayTv.setTextColor(getArguments().getInt(RWeekCalendar.PRIMARY_BACKGROUND));

        /*Displaying the days in the week views*/
//
//        sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//        mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//        tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//        wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//        thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//        fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//        saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));

        setDayofWeek();
        /*if the selected week is the current week indicates the current day*/

        if(datePosition==0)
       {


           for(int i=0;i<7;i++)
           {

               if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)==dateInWeekArray.get(i).getDayOfMonth())
               {
                   textViewArray[i].setTextColor(currentDateIndicatorValue);
               }
           }

       }

        /**
         * Click listener of all week days with the indicator change and passing listener info.
         */

        sundayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                click_hppn="yes";
                mSelectedDateInfo(0);
                sundayTV.setBackgroundResource(selectorDateIndicatorValue);
                sundayTV.setTextColor(getResources().getColor(R.color.white));
                week_s.setTextColor(getResources().getColor(R.color.bottombar));
                week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
                mDateSelectedBackground(0);
                currentDay=0;


            }
        });

        mondayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                click_hppn="yes";
                mSelectedDateInfo(1);
                mondayTv.setBackgroundResource(selectorDateIndicatorValue);
                mondayTv.setTextColor(getResources().getColor(R.color.white));
                week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_m.setTextColor(getResources().getColor(R.color.bottombar));
                week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
                mDateSelectedBackground(1);
                currentDay=1;


            }
        });

        tuesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_hppn="yes";
                mSelectedDateInfo(2);
                tuesdayTv.setBackgroundResource(selectorDateIndicatorValue);
                tuesdayTv.setTextColor(getResources().getColor(R.color.white));
                week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_t.setTextColor(getResources().getColor(R.color.bottombar));
                week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
                mDateSelectedBackground(2);

                currentDay=2;

            }
        });


        wednesdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_hppn="yes";
                mSelectedDateInfo(3);
                wednesdayTv.setBackgroundResource(selectorDateIndicatorValue);
                wednesdayTv.setTextColor(getResources().getColor(R.color.white));
                week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_w.setTextColor(getResources().getColor(R.color.bottombar));
                week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
                mDateSelectedBackground(3);
                currentDay=3;


            }
        });


        thursdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_hppn="yes";
                mSelectedDateInfo(4);
                thursdayTv.setBackgroundResource(selectorDateIndicatorValue);
                thursdayTv.setTextColor(getResources().getColor(R.color.white));
                week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_th.setTextColor(getResources().getColor(R.color.bottombar));
                week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
                mDateSelectedBackground(4);
                currentDay=4;


            }
        });


        fridayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_hppn="yes";
                mSelectedDateInfo(5);
                fridayTv.setBackgroundResource(selectorDateIndicatorValue);
                fridayTv.setTextColor(getResources().getColor(R.color.white));
                week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_f.setTextColor(getResources().getColor(R.color.bottombar));
                week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
                mDateSelectedBackground(5);
                currentDay=5;


            }
        });


        saturdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_hppn="yes";
                mSelectedDateInfo(6);
                saturdayTv.setBackgroundResource(selectorDateIndicatorValue);
                saturdayTv.setTextColor(getResources().getColor(R.color.white));
                week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
                week_sa.setTextColor(getResources().getColor(R.color.bottombar));
                mDateSelectedBackground(6);
                currentDay=6;



            }
        });

//---------------------------------------------------------------------------------------//


    }

    public void setDayofWeek(){

        String dayName= CalUtil.getDayOfweek();
        if (dayName.equals("sun")) {
//            SpannableString ss1=  new SpannableString("M\n"+Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            ss1.setSpan(new RelativeSizeSpan(0.5f), 0,1, 0); // set size
            sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            week_s.setText("M");
//            SpannableString ss2=  new SpannableString("T\n" + Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            ss2.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            week_m.setText("T");
//            SpannableString ss3=  new SpannableString("W\n" + Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            ss3.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            week_t.setText("W");
//            SpannableString ss4=  new SpannableString("Th\n" + Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            ss4.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            week_w.setText("Th");
//            SpannableString ss5=  new SpannableString("F\n" + Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            ss5.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            week_th.setText("F");
//            SpannableString ss6=  new SpannableString("Sa\n" + Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            ss6.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            week_f.setText("Sa");
//            SpannableString ss7=  new SpannableString("S\n"+Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            ss7.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            week_sa.setText("S");
            mSelectedDateInfo(0);
            sundayTV.setBackgroundResource(selectorDateIndicatorValue);
            sundayTV.setTextColor(getResources().getColor(R.color.white));
            week_s.setTextColor(getResources().getColor(R.color.bottombar));
            week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
            mDateSelectedBackground(0);
            currentDay=0;
        } else if (dayName.equals("mon")) {
//            SpannableString ss1=  new SpannableString("M\n"+Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            ss1.setSpan(new RelativeSizeSpan(0.5f), 0,1, 0); // set size
            sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            week_s.setText("M");
//            SpannableString ss2=  new SpannableString("T\n" + Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            ss2.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            week_m.setText("T");
//            SpannableString ss3=  new SpannableString("W\n" + Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            ss3.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            week_t.setText("W");
//            SpannableString ss4=  new SpannableString("Th\n" + Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            ss4.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            week_w.setText("Th");
//            SpannableString ss5=  new SpannableString("F\n" + Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            ss5.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            week_th.setText("F");
//            SpannableString ss6=  new SpannableString("Sa\n" + Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            ss6.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            week_f.setText("Sa");
//            SpannableString ss7=  new SpannableString("S\n"+Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            ss7.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            week_sa.setText("S");
            mSelectedDateInfo(1);
            mondayTv.setBackgroundResource(selectorDateIndicatorValue);
            mondayTv.setTextColor(getResources().getColor(R.color.white));
            week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_m.setTextColor(getResources().getColor(R.color.bottombar));
            week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
            mDateSelectedBackground(1);
            currentDay=1;
        } else if (dayName.equals("tue")) {
//            SpannableString ss1=  new SpannableString("T\n"+Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            ss1.setSpan(new RelativeSizeSpan(0.5f), 0,1, 0); // set size
            sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            week_s.setText("T");
//            SpannableString ss2=  new SpannableString("W\n" + Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            ss2.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            week_m.setText("W");
//            SpannableString ss3=  new SpannableString("Th\n" + Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            ss3.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            week_t.setText("Th");
//            SpannableString ss4=  new SpannableString("F\n" + Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            ss4.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            week_w.setText("F");
//            SpannableString ss5=  new SpannableString("Sa\n" + Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            ss5.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            week_th.setText("Sa");
//            SpannableString ss6=  new SpannableString("S\n" + Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            ss6.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            week_f.setText("S");
//            SpannableString ss7=  new SpannableString("M\n"+Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            ss7.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            week_sa.setText("M");
            mSelectedDateInfo(2);
            tuesdayTv.setBackgroundResource(selectorDateIndicatorValue);
            tuesdayTv.setTextColor(getResources().getColor(R.color.white));
            week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_t.setTextColor(getResources().getColor(R.color.bottombar));
            week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
            mDateSelectedBackground(2);
            currentDay=2;
        } else if (dayName.equals("wed")) {
//            SpannableString ss1=  new SpannableString("W\n"+Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            ss1.setSpan(new RelativeSizeSpan(0.5f), 0,1, 0); // set size
            sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            week_s.setText("W");
//            SpannableString ss2=  new SpannableString("Th\n" + Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            ss2.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            week_m.setText("Th");
//            SpannableString ss3=  new SpannableString("F\n" + Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            ss3.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            week_t.setText("F");
//            SpannableString ss4=  new SpannableString("Sa\n" + Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            ss4.setSpan(new RelativeS/izeSpan(0.5f), 0, 2, 0);
            wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            week_w.setText("Sa");
//            SpannableString ss5=  new SpannableString("S\n" + Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            ss5.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            week_th.setText("S");
//            SpannableString ss6=  new SpannableString("M\n" + Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            ss6.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            week_f.setText("M");
//            SpannableString ss7=  new SpannableString("T\n"+Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            ss7.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            week_sa.setText("T");

            mSelectedDateInfo(3);
            wednesdayTv.setBackgroundResource(selectorDateIndicatorValue);
            wednesdayTv.setTextColor(getResources().getColor(R.color.white));
            week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_w.setTextColor(getResources().getColor(R.color.bottombar));
            week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
            mDateSelectedBackground(3);
            currentDay=3;
        } else if (dayName.equals("thu")) {
//            SpannableString ss1=  new SpannableString("Th\n"+Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            ss1.setSpan(new RelativeSizeSpan(0.5f), 0,2, 0); // set size
            sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            week_s.setText("Th");
//            SpannableString ss2=  new SpannableString("F\n" + Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            ss2.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            week_m.setText("F");
//            SpannableString ss3=  new SpannableString("Sa\n" + Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            ss3.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            week_t.setText("Sa");
//            SpannableString ss4=  new SpannableString("S\n" + Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            ss4.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            week_w.setText("S");
//            SpannableString ss5=  new SpannableString("M\n" + Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            ss5.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            week_th.setText("M");
//            SpannableString ss6=  new SpannableString("T\n" + Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            ss6.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            week_f.setText("T");
//            SpannableString ss7=  new SpannableString("W\n"+Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            ss7.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            week_sa.setText("W");
            mSelectedDateInfo(4);
            thursdayTv.setBackgroundResource(selectorDateIndicatorValue);
            thursdayTv.setTextColor(getResources().getColor(R.color.white));
            week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_th.setTextColor(getResources().getColor(R.color.bottombar));
            week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
            mDateSelectedBackground(4);
            currentDay=4;
        } else if (dayName.equals("fri")) {
//            SpannableString ss1=  new SpannableString("F\n"+Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            ss1.setSpan(new RelativeSizeSpan(0.5f), 0,1, 0); // set size
            sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            week_s.setText("F");
//            SpannableString ss2=  new SpannableString("Sa\n" + Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            ss2.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            week_m.setText("Sa");
//            SpannableString/ ss3=  new SpannableString("S\n" + Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            ss3.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            week_t.setText("S");
//            SpannableString ss4=  new SpannableString("M\n" + Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            ss4.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            week_w.setText("M");
//            SpannableString ss5=  new SpannableString("T\n" + Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            ss5.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            week_th.setText("T");
//            SpannableString ss6=  new SpannableString("W\n" + Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            ss6.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            week_f.setText("W");
//            SpannableString ss7=  new SpannableString("Th\n"+Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            ss7.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            week_sa.setText("Th");
            mSelectedDateInfo(5);
            fridayTv.setBackgroundResource(selectorDateIndicatorValue);
            fridayTv.setTextColor(getResources().getColor(R.color.white));
            week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_f.setTextColor(getResources().getColor(R.color.bottombar));
            week_sa.setTextColor(getResources().getColor(R.color.md_black_1000));
            mDateSelectedBackground(5);
            currentDay=5;
        } else if (dayName.equals("sat")) {
//            SpannableString ss1=  new SpannableString("Sa\n"+Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            ss1.setSpan(new RelativeSizeSpan(0.5f), 0,2, 0); // set size
            sundayTV.setText(Integer.toString(dateInWeekArray.get(0).getDayOfMonth()));
//            week_s.setText("Sa");
//            SpannableString ss2=  new SpannableString("S\n" + Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            ss2.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            mondayTv.setText(Integer.toString(dateInWeekArray.get(1).getDayOfMonth()));
//            week_m.setText("S");
//            SpannableString ss3=  new SpannableString("M\n" + Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            ss3.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            tuesdayTv.setText(Integer.toString(dateInWeekArray.get(2).getDayOfMonth()));
//            week_t.setText("M");
//            SpannableString ss4=  new SpannableString("T\n" + Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            ss4.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            wednesdayTv.setText(Integer.toString(dateInWeekArray.get(3).getDayOfMonth()));
//            week_w.setText("T");
//            SpannableString ss5=  new SpannableString("W\n" + Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            ss5.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            thursdayTv.setText(Integer.toString(dateInWeekArray.get(4).getDayOfMonth()));
//            week_th.setText("W");
//            SpannableString ss6=  new SpannableString("Th\n" + Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            ss6.setSpan(new RelativeSizeSpan(0.5f), 0, 2, 0);
            fridayTv.setText(Integer.toString(dateInWeekArray.get(5).getDayOfMonth()));
//            week_f.setText("Th");
//            SpannableString ss7=  new SpannableString("F\n"+Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            ss7.setSpan(new RelativeSizeSpan(0.5f), 0, 1, 0);
            saturdayTv.setText(Integer.toString(dateInWeekArray.get(6).getDayOfMonth()));
//            week_sa.setText("F");
            mSelectedDateInfo(6);
            saturdayTv.setBackgroundResource(selectorDateIndicatorValue);
            saturdayTv.setTextColor(getResources().getColor(R.color.white));
            week_s.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_m.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_t.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_w.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_th.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_f.setTextColor(getResources().getColor(R.color.md_black_1000));
            week_sa.setTextColor(getResources().getColor(R.color.bottombar));
            mDateSelectedBackground(6);
            currentDay=6;
        }






    }



    /**
     * Set Values including customizable info
     *
     * @param position
     * @param selectorDateIndicatorValue
     * @param currentDateIndicatorValue
     * @param primaryTextColor
     */
    public static WeekFragmentFood newInstance(int position, String selectorDateIndicatorValue, int currentDateIndicatorValue, int primaryTextColor) {

        WeekFragmentFood f = new WeekFragmentFood();
        Bundle b = new Bundle();

        b.putInt(RWeekCalendar.POSITIONKEY, position);
        b.putString(RWeekCalendar.DATE_SELECTOR_BACKGROUND, selectorDateIndicatorValue);
        b.putInt(RWeekCalendar.CURRENT_DATE_BACKGROUND, currentDateIndicatorValue);
        b.putInt(RWeekCalendar.PRIMARY_BACKGROUND, primaryTextColor);
        f.setArguments(b);

        return f;
    }

    /**Passing the selected date info
     *
     * @param position
     */
    public void mSelectedDateInfo(int position)
    {
        RWeekCalendar.getInstance().getSelectedDate(dateInWeekArray.get(position));

        mSelectedDate=dateInWeekArray.get(position);

        AppController.getInstance().setSelected(mSelectedDate);


    }

    /**Changing backgrounds of unselected views
     *
     * @param position
     */
    public void mDateSelectedBackground(int position)
    {
        for(int i=0;i<textViewArray.length;i++)
        {
            if(position!=i)
            {

                    textViewArray[i].setBackgroundColor(Color.TRANSPARENT);
                textViewArray[i].setTextColor(Color.parseColor("#111111"));



            }

        }

    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        /**
         * Reset date to first day of week when week goes from the view
         */

        if (isVisibleToUser) {

            if(dateInWeekArray.size()>0)
            RWeekCalendar.getInstance().getSelectedDate(dateInWeekArray.get(0));

        }


            if(mSelectedDate!=null) {

//                    textViewArray[0].setBackgroundResource(selectorDateIndicatorValue);
//                textViewArray[0].setTextColor(getResources().getColor(R.color.white));

                for (int i=0;i<7;i++){
                    if (i!=currentDay){
                        textViewArray[i].setBackgroundColor(Color.TRANSPARENT);
                        textViewArray[i].setTextColor(getResources().getColor(R.color.md_black_1000));
                    }else {
                        textViewArray[i].setBackgroundResource(selectorDateIndicatorValue);
                        textViewArray[i].setTextColor(getResources().getColor(R.color.white));
                    }

                }
                }


    }


    /**
     * Setting date when selected form picker
     *
     * @param mSelectedDate
     */

    public void ChangeSelector(LocalDateTime mSelectedDate) {

        LocalDateTime startDate=AppController.getInstance().getDate();
        int addDays=datePosition*7;


        startDate = startDate.plusDays(addDays);

        for (int i = 0; i < 7; i++) {

            if (mSelectedDate.getDayOfMonth() == startDate.getDayOfMonth()) {
                textViewArray[i].setBackgroundResource(selectorDateIndicatorValue);
                textViewArray[i].setTextColor(getResources().getColor(R.color.white));
                mDateSelectedBackground(i);


            }
            startDate=startDate.plusDays(1);

        }
    }


}
