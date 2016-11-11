package in.tagbin.mitohealthapp.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.model.UserGoalWeightModel;
import in.tagbin.mitohealthapp.model.UserWeightModel;

/**
 * Created by aasaqt on 9/11/16.
 */

public class TrackMyWeightActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView heading,currentWeight,goalWeight,highlight;
    RelativeLayout relativeCurrent,relativeGoal;
    LineChart chart;
    String goal_weight_new,weight_new;
    float goal_weight,weight;
    int monthsValue = 8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_weight);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Track My Weight");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        heading = (TextView) findViewById(R.id.tvTrackWeightHeading);
        highlight = (TextView) findViewById(R.id.tvTrackWeightHighlight);
        currentWeight = (TextView) findViewById(R.id.tvTrackWeightCurrent);
        goalWeight = (TextView) findViewById(R.id.tvTrackWeightGoal);
        relativeCurrent = (RelativeLayout) findViewById(R.id.relativeTrackWeightCurrent);
        relativeGoal = (RelativeLayout) findViewById(R.id.relativeTrackWeightGoal);
        relativeCurrent.setOnClickListener(this);
        relativeGoal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relativeTrackWeightGoal:
                showGoal_WeightDialog();
                break;
            case R.id.relativeTrackWeightCurrent:
                showWeightDialog();
                break;
        }
    }
    public void showGoal_WeightDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_value_picker);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Kg");
        measuring_units.add("Pounds");

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        //final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Goal Weight");

        final EditText seekBar = (EditText) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        RelativeLayout looseWeight = (RelativeLayout) dialog.findViewById(R.id.relativeLooseWeight);
        looseWeight.setVisibility(View.VISIBLE);
        DiscreteSeekBar monthsSeekbar = (DiscreteSeekBar) dialog.findViewById(R.id.seekbarMonths);
        final TextView monthsHeading = (TextView) dialog.findViewById(R.id.tvMonthsHeading);
        TextView doneButton = (TextView) dialog.findViewById(R.id.tvDialogButton);
        doneButton.setText("Update");
        TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight);
        textInputLayout.setHint("Goal Weight");
        seekBar.setSelectAllOnFocus(true);
        final String[] unit = {"Kg"};
        if (goal_weight != 0.0) {
            seekBar.setText(new DecimalFormat("##.#").format(goal_weight/1000).toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit[0] = measuring_units.get(i);
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    if (unit[0].equals("Kg")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 0.453592).toString());
                        seekBar.setSelectAllOnFocus(true);
                    } else if (unit[0].equals("Pounds")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 2.20462).toString());
                        seekBar.setSelectAllOnFocus(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        monthsSeekbar.setMax(52);
        monthsSeekbar.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                //settingsModel.setMaximum_distance(value);
                if (goal_weight != 0 && weight != 0){
                    double goal = goal_weight/1000;
                    double weightFinal = weight/1000;
                    monthsHeading.setVisibility(View.VISIBLE);
                    if (weightFinal - goal <0 ){
                        monthsHeading.setText("Gaining "+new DecimalFormat("##.#").format((-(weightFinal-goal)/value)).toString()+" kgs/week");
                    }else{
                        monthsHeading.setText("Loosing "+new DecimalFormat("##.#").format(((weightFinal-goal)/value)).toString()+" kgs/week");
                    }

                }else{
                    monthsHeading.setVisibility(View.GONE);
                }
                monthsValue = value;
                return value;
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    float i = Float.parseFloat(seekBar.getText().toString());
                    if (unit[0].equals("Kg")) {
                        goal_weight_new = new DecimalFormat("##.#").format(i).toString() + " Kg";
                        goal_weight = Float.parseFloat(seekBar.getText().toString()) * 1000;
                    } else if (unit[0].equals("Pounds")) {
                        goal_weight_new = new DecimalFormat("##.#").format(i).toString() + " Pounds";
                        goal_weight = (float) (Float.parseFloat(seekBar.getText().toString()) * 453.592);
                    }
                    goalWeight.setText(goal_weight_new+" in "+monthsValue+" weeks");
                    UserGoalWeightModel userDateModel = new UserGoalWeightModel();
                    userDateModel.setGoal_weight(String.valueOf(goal_weight));
                    //Controller.setUserGoalWeight(this,userDateModel,user_id,mSetUserDetailsListener);

                }
                dialog.dismiss();

            }
        });
        dialog.show();
    }
    public void showWeightDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.item_value_picker);
        final List<String> measuring_units = new ArrayList<>();
        measuring_units.add("Kg");
        measuring_units.add("Pounds");
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.height_spinner);
        //final TextView value = (TextView) dialog.findViewById(R.id.height_value);
        final TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
        dialog_name.setText("Weight");
        TextView doneButton = (TextView) dialog.findViewById(R.id.tvDialogButton);
        doneButton.setText("Update");
        final EditText seekBar = (EditText) dialog.findViewById(R.id.height_seekbar);
        View done = dialog.findViewById(R.id.height_done);
        TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.textLayoutHeight);
        textInputLayout.setHint("Weight");
        final String[] unit = {"Kg"};
        if (weight != 0.0) {
            seekBar.setText(new DecimalFormat("##.#").format(weight/1000).toString());
        }
        seekBar.setSelectAllOnFocus(true);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit[0] = measuring_units.get(i);
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    if (unit[0].equals("Kg")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 0.453592).toString());
                        seekBar.setSelectAllOnFocus(true);
                    } else if (unit[0].equals("Pounds")) {
                        seekBar.setText(new DecimalFormat("##.#").format(Float.parseFloat(seekBar.getText().toString()) * 2.20462).toString());
                        seekBar.setSelectAllOnFocus(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (seekBar.getText().toString() != null && !seekBar.getText().toString().isEmpty()) {
                    float i = Float.parseFloat(seekBar.getText().toString());
                    if (unit[0].equals("Kg")) {
                        weight_new = new DecimalFormat("##.#").format(i).toString() + " Kg";
                        weight = Float.parseFloat(seekBar.getText().toString()) * 1000;
                    } else if (unit[0].equals("Pounds")) {
                        weight_new = new DecimalFormat("##.#").format(i).toString() + " Pounds";
                        weight = (float) (Float.parseFloat(seekBar.getText().toString()) * 453.592);
                    }
//                    UserWeightModel userDateModel = new UserWeightModel();
//                    userDateModel.setWeight(String.valueOf(weight));
//                    //Controller.setUserWeight(this,userDateModel,user_id,mSetUserDetailsListener);
                    currentWeight.setText(weight_new);

                }
                    dialog.dismiss();

            }
        });
        dialog.show();
    }
}
