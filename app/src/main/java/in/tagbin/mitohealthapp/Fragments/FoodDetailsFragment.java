package in.tagbin.mitohealthapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.RecommendationModel;

public class FoodDetailsFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    String response;
    RecommendationModel.MealsModel data;
    public static String time,quantity;
    public static int servingUnit;
    LinearLayout foodTime;
    EditText quantity_ed;
    PrefManager pref;
    public static int year,month,day,hour,minute;
    TextView set_time,set_protien,set_fat,set_carbo,set_energy;
    Spinner set_unit;
    final List<String> measuring_units = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.frag_food_details, container, false);
        pref = new PrefManager(getContext());
        quantity_ed= (EditText) view.findViewById(R.id.quantity_ed);
        set_unit= (Spinner) view.findViewById(R.id.measuring_type);
        set_time= (TextView) view.findViewById(R.id.set_time);
        //set_ampm= (TextView) view.findViewById(R.id.set_ampm);
        set_protien= (TextView) view.findViewById(R.id.set_protien);
        set_fat= (TextView) view.findViewById(R.id.set_fat);
        set_carbo= (TextView) view.findViewById(R.id.set_carbo);
        foodTime = (LinearLayout) view.findViewById(R.id.linearTimeFood);
        set_energy= (TextView) view.findViewById(R.id.set_energy);
        foodTime.setOnClickListener(this);
        //setTime(hour,min);
        if (getArguments().getString("response") != null) {
            response = getArguments().getString("response");
            Log.d("response",response);
            data = JsonUtils.objectify(response, RecommendationModel.MealsModel.class);
            measuring_units.add(data.getComponent().getServing_type().getServing_type());
            for (int i=0;i<data.getComponent().getOther_serving_detail().size();i++){
                measuring_units.add(data.getComponent().getOther_serving_detail().get(i).getServing_type().getServing_type());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, measuring_units);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            set_unit.setAdapter(adapter);
            set_unit.setSelection(0,false);
            final String[] unit = {data.getComponent().getServing_type().getServing_type()};
            servingUnit = data.getComponent().getServing_type().getId();
            set_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("item selected", measuring_units.get(i));
                    unit[0] = measuring_units.get(i);
                    if (unit[0].equals(data.getComponent().getServing_type().getServing_type())){
                        set_protien.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_protein()).toString()+"g");
                        set_fat.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_fat()).toString()+"g");
                        set_carbo.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_carbohydrate()).toString()+"g");
                        set_energy.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_energy()).toString()+"calories");
                        servingUnit = data.getComponent().getServing_type().getId();
                    }else {
                        for (int y = 0; y < data.getComponent().getOther_serving_detail().size(); y++) {
                            if (unit[0].equals(data.getComponent().getOther_serving_detail().get(y).getServing_type().getServing_type())) {
                                set_protien.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_protein()).toString() + "g");
                                set_fat.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_fat()).toString() + "g");
                                set_carbo.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_carbohydrate()).toString() + "g");
                                set_energy.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_energy()).toString() + "calories");
                                servingUnit = data.getComponent().getOther_serving_detail().get(y).getServing_type().getId();
                            }
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            //set_unit.setText(data.getComponent().getServing_type().getServing_type());
            if (data.getTime() != null){
                set_time.setText(MyUtils.getValidTime(data.getTime()));
                year = Integer.parseInt(MyUtils.getYear(data.getTime()));
                month = Integer.parseInt(MyUtils.getMonth(data.getTime()))-1;
                day = Integer.parseInt(MyUtils.getDay(data.getTime()));
                hour = Integer.parseInt(MyUtils.getHour(data.getTime()));
                minute = Integer.parseInt(MyUtils.getMinute(data.getTime()));
            }else {
                Calendar calendar = Calendar.getInstance();
                if (pref.getKeyDay() != 0 && pref.getKeyMonth() != 0 && pref.getKeyYear() != 0){
                    day = pref.getKeyDay();
                    month = pref.getKeyMonth();
                    year = pref.getKeyYear();
                }else {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                }
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                updateTime1(hour, minute);
            }
            if (data.getAmount() == 0){
                quantity = "";
                quantity_ed.setText("");
            }else{
                quantity_ed.setText(""+data.getAmount());
                quantity = String.valueOf(data.getAmount());
            }

            set_protien.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_protein()).toString()+"g");
            set_fat.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_fat()).toString()+"g");
            set_carbo.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_carbohydrate()).toString()+"g");
            set_energy.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_energy()).toString()+"calories");
        }
        quantity_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quantity = charSequence.toString();
                if (!quantity.equals("")) {
                    set_protien.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_protein() * Float.parseFloat(quantity)).toString() + "g");
                    set_fat.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_fat() * Float.parseFloat(quantity)).toString() + "g");
                    set_carbo.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_carbohydrate() * Float.parseFloat(quantity)).toString() + "g");
                    set_energy.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_energy() * Float.parseFloat(quantity)).toString() + "calories");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        return view;
    }
    private void updateTime1(int hours, int mins) {
        Log.d("time",hours+"\n"+mins);
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String aTime = new StringBuilder().append(hours).append(':')
                .append(mins).append(" ").append(timeSet).toString();
        time = aTime;
        set_time.setText(aTime);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linearTimeFood:
                Calendar[] dates = new Calendar[4];
                int i = 0;
                while (i < 4){
                    Calendar selDate = Calendar.getInstance();
                    selDate.add(Calendar.DAY_OF_MONTH, -i);
                    dates[i] = selDate;
                    i++;
                }

//                while (i < 35) {
//                    Calendar selDate = Calendar.getInstance();
//                    selDate.add(Calendar.DAY_OF_MONTH, i-3);
//                    dates[i] = selDate;
//                    i++;
//                }
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this, year, month, day);
                dpd.setSelectableDays(dates);
                dpd.setHighlightedDays(dates);
                dpd.show(getActivity().getFragmentManager(), "DATE_PICKER_TAG");
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(this, hour, minute, false);
        tpd.show(getActivity().getFragmentManager(), "TIME_PICKER_TAG");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        hour = hourOfDay;
        this.minute = minute;
        updateTime1(hour,minute);
    }
}
