package in.tagbin.mitohealthapp.Fragments;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseException;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.MainPage;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.Timeconsumed;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeelingsFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeelingsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeelingsFrag extends Fragment  implements OnDateSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView stress_tv,happiness_tv,energy_tv,confidence_tv;
    double stress=0.0,happiness=0.0,energy=0.0,confidence=0.0;

    String dateTimeStamp="";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseOperations dop;
    SharedPreferences login_details;
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    MaterialCalendarView widget;
    public static String selectedDate = "";
    private OnFragmentInteractionListener mListener;
    DiscreteSeekBar stressbar ;DiscreteSeekBar happinessbar; DiscreteSeekBar energybar;  DiscreteSeekBar confidencebar;

    public FeelingsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeelingsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FeelingsFrag newInstance(String param1, String param2) {
        FeelingsFrag fragment = new FeelingsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dop = new DatabaseOperations(getActivity());

        login_details = getActivity().getSharedPreferences(MainPage.LOGIN_DETAILS, getActivity().MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();

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



        Log.d("date",selectedDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feelings, container, false);
        widget= (MaterialCalendarView) view.findViewById(R.id.calendarView);
        stress_tv= (TextView) view.findViewById(R.id.stresstv);
        happiness_tv= (TextView) view.findViewById(R.id.hapinesstv);
        energy_tv= (TextView) view.findViewById(R.id.energytv);
        confidence_tv= (TextView) view.findViewById(R.id.confidencetv);
        dateTimeStamp=String.valueOf(MyUtils.getUtcTimestamp(selectedDate+" 00:00:00","s"));

        try{
            dop.putFeelingsInformation(dop, dateTimeStamp,"0.0","0.0","0.0","0.0","0.0","no");
        }catch (Exception e){
            Log.d("DatabaseException",e.toString());
        }

        final Calendar calendar = Calendar.getInstance();
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

       Cursor cursor= dop.getFeelingsInformationByDate(dop,dateTimeStamp);
        if (cursor != null && cursor.moveToFirst()) {

            do {
                //create a new movie object and retrieve the data from the cursor to be stored in this movie object
              stress=  Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.STRESS)));
            happiness=    Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.HAPPINESS)));
             energy=   Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ENERGY)));
            confidence    =Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CONFIDENCE)));
            }
            while (cursor.moveToNext());
        }

         stressbar = (DiscreteSeekBar) view.findViewById(R.id.stressbar);
        stressbar.setProgress((int) stress);
        stress_tv.setText(stress+"");
        stressbar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {

                stress=value;
                stress_tv.setText(stress+"");
                calculate(stress,happiness,energy,confidence);
                return value;
            }
        });
    happinessbar = (DiscreteSeekBar) view.findViewById(R.id.happinessbar);
        happinessbar.setProgress((int) happiness);
        happiness_tv.setText(happiness+"");
        happinessbar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {

                happiness=value;
                happiness_tv.setText(happiness+"");
                calculate(stress,happiness,energy,confidence);
                return value;
            }
        });
         energybar = (DiscreteSeekBar) view.findViewById(R.id.energybar);
        energybar.setProgress((int) energy);
        energy_tv.setText(energy+"");
        energybar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {

                energy=value;
                energy_tv.setText(energy+"");
                calculate(stress,happiness,energy,confidence);
                return value;
            }
        });
        confidencebar = (DiscreteSeekBar) view.findViewById(R.id.confidencebar);
        confidencebar.setProgress((int) confidence);
        confidence_tv.setText(confidence+"");
        confidencebar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {

                confidence=value;
                confidence_tv.setText(confidence+"");
                calculate(stress,happiness,energy,confidence);
                return value;
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void calculate(double stress,double happiness,double energy,double confidence){
        double add= (-stress)+happiness+energy+confidence;
        double result= add/4;
        Log.d("Calculate Result",result+"//");
        ContentValues cv = new ContentValues();
        cv.put(TableData.Tableinfo.STRESS,stress);
        cv.put(TableData.Tableinfo.HAPPINESS,happiness);
        cv.put(TableData.Tableinfo.ENERGY,energy);
        cv.put(TableData.Tableinfo.CONFIDENCE,confidence);
        cv.put(TableData.Tableinfo.AVERAGE,String.valueOf(result));
        cv.put(TableData.Tableinfo.FEELING_SYNCED,"no");

        dop.updateAnyRow(dop, TableData.Tableinfo.TABLE_NAME_FEELINGS,cv, TableData.Tableinfo.FEELINGS_DATE,dateTimeStamp);

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
        dateTimeStamp=String.valueOf(MyUtils.getUtcTimestamp(selectedDate+" 00:00:00","s"));
        Cursor cursor= dop.getFeelingsInformationByDate(dop,dateTimeStamp);

        if (cursor.getCount()==0){
            try{
                dop.putFeelingsInformation(dop, dateTimeStamp,"0.0","0.0","0.0","0.0","0.0","no");
            }catch (DatabaseException e){
                Log.d("DatabaseException",e.toString());
            }
        }

        cursor= dop.getFeelingsInformationByDate(dop,dateTimeStamp);
            if (cursor != null && cursor.moveToFirst()) {

                do {
                    //create a new movie object and retrieve the data from the cursor to be stored in this movie object
                    stress = Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.STRESS)));
                    happiness = Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.HAPPINESS)));
                    energy = Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.ENERGY)));
                    confidence = Double.valueOf(cursor.getString(cursor.getColumnIndex(TableData.Tableinfo.CONFIDENCE)));
                }
                while (cursor.moveToNext());
            }

        stressbar.setProgress((int) stress);
        happinessbar.setProgress((int) happiness);
        energybar.setProgress((int) energy);
        confidencebar.setProgress((int) confidence);
        stress_tv.setText(stress+"");
        happiness_tv.setText(happiness+"");
        energy_tv.setText(energy+"");
        confidence_tv.setText(confidence+"");


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void onFragmentInteraction(Uri uri);
    }
}
