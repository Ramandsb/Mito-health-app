package in.tagbin.mitohealthapp.Fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.AppController;
import in.tagbin.mitohealthapp.Config;
import in.tagbin.mitohealthapp.FoodDetails;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.RecommendationModel;

public class FoodDetailsFrag extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    String response;
    RecommendationModel.MealsModel data;
    public static String time,quantity;
    LinearLayout foodTime;
    EditText quantity_ed;
    public static int year,month,day,hour,minute;
    TextView set_time,set_protien,set_fat,set_carbo,set_energy,set_unit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_food_details, container, false);
        quantity_ed= (EditText) view.findViewById(R.id.quantity_ed);
        set_unit= (TextView) view.findViewById(R.id.measuring_type);
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

            set_unit.setText(data.getComponent().getServing_type().getServing_type());
            if (data.getTime() != null){
                set_time.setText(MyUtils.getValidTime(data.getTime()));
                year = Integer.parseInt(MyUtils.getYear(data.getTime()));
                month = Integer.parseInt(MyUtils.getMonth(data.getTime()));
                day = Integer.parseInt(MyUtils.getDay(data.getTime()));
                hour = Integer.parseInt(MyUtils.getHour(data.getTime()));
                minute = Integer.parseInt(MyUtils.getMinute(data.getTime()));
            }else {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
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
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(this, year, month, day);
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
