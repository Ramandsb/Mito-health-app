package in.tagbin.mitohealthapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.TrackWeightGraphModel;
import in.tagbin.mitohealthapp.model.UserGoalWeightModel;
import in.tagbin.mitohealthapp.model.UserModel;
import in.tagbin.mitohealthapp.model.UserWeightModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 9/11/16.
 */

public class TrackMyWeightActivity extends AppCompatActivity implements View.OnClickListener, OnChartValueSelectedListener {
    Toolbar toolbar;
    TextView heading,currentWeight,goalWeight,highlight;
    RelativeLayout relativeCurrent,relativeGoal;
    LineChart mChart;
    String goal_weight_new,weight_new,user_id;
    SharedPreferences login_details;
    float goal_weight,weight;
    int monthsValue = 8;
    PrefManager pref;
    GifImageView progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_weight);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = new PrefManager(this);
        getSupportActionBar().setTitle("Track My Weight");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        heading = (TextView) findViewById(R.id.tvTrackWeightHeading);
        highlight = (TextView) findViewById(R.id.tvTrackWeightHighlight);
        currentWeight = (TextView) findViewById(R.id.tvTrackWeightCurrent);
        goalWeight = (TextView) findViewById(R.id.tvTrackWeightGoal);
        mChart = (LineChart) findViewById(R.id.chart);
        relativeCurrent = (RelativeLayout) findViewById(R.id.relativeTrackWeightCurrent);
        relativeGoal = (RelativeLayout) findViewById(R.id.relativeTrackWeightGoal);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        login_details = getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);
        user_id = login_details.getString("user_id", "");
        relativeCurrent.setOnClickListener(this);
        relativeGoal.setOnClickListener(this);
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
        mChart.getXAxis().setTextColor(Color.parseColor("#111111"));
        mChart.setDescriptionColor(Color.parseColor("#111111"));
        mChart.getAxisLeft().setTextColor(Color.parseColor("#111111"));
        mChart.getAxisRight().setTextColor(Color.parseColor("#111111"));
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        Controller.getTrackWeight(this,mTrackWeightListener);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
        monthsSeekbar.setProgress(monthsValue);
        if (goal_weight != 0 && weight != 0){
            double goal = goal_weight/1000;
            double weightFinal = weight/1000;
            monthsHeading.setVisibility(View.VISIBLE);
            if (weightFinal - goal <0 ){
                monthsHeading.setText("Gaining "+new DecimalFormat("##.#").format((-(weightFinal-goal)/monthsValue)).toString()+" kgs/week");
            }else{
                monthsHeading.setText("Loosing "+new DecimalFormat("##.#").format(((weightFinal-goal)/monthsValue)).toString()+" kgs/week");
            }

        }else{
            monthsHeading.setVisibility(View.GONE);
        }
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
                    userDateModel.setGoal_time(monthsValue*7);
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.setUserGoalWeight(TrackMyWeightActivity.this,userDateModel,user_id,mSetUserDetailsListener);

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
                    UserWeightModel userDateModel = new UserWeightModel();
                    userDateModel.setWeight(String.valueOf(weight));
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.setUserWeight(TrackMyWeightActivity.this,userDateModel,user_id,mSetUserDetailsListener);
                    currentWeight.setText(weight_new);

                }
                    dialog.dismiss();

            }
        });
        dialog.show();
    }
    RequestListener mSetUserDetailsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            final UserModel userModel = JsonUtils.objectify(responseObject.toString(), UserModel.class);
            pref.setKeyUserDetails(userModel);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Controller.getTrackWeight(TrackMyWeightActivity.this,mTrackWeightListener);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {

            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TrackMyWeightActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TrackMyWeightActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mTrackWeightListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("response",responseObject.toString());
            final TrackWeightGraphModel trackWeightGraphModel = JsonUtils.objectify(responseObject.toString(),TrackWeightGraphModel.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    goal_weight = trackWeightGraphModel.getGoal_weight();
                    monthsValue = trackWeightGraphModel.getGoal_time()/7;
                    weight = trackWeightGraphModel.getCurrent_weight();
                    currentWeight.setText(new DecimalFormat("##.#").format(trackWeightGraphModel.getCurrent_weight()/1000).toString()+" kg");
                    goalWeight.setText(new DecimalFormat("##.#").format(trackWeightGraphModel.getGoal_weight()/1000).toString()+" kgs in "+trackWeightGraphModel.getGoal_time()/7+" weeks");
                    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                    List<Entry> entries = new ArrayList<Entry>();

                    for (int i =0; i < trackWeightGraphModel.getVariation().size();i++){
                        entries.add(new Entry(MyUtils.getTimeinMillis(trackWeightGraphModel.getVariation().get(i).getTime_consumed()),trackWeightGraphModel.getVariation().get(i).getComponent_id()/1000));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "Actual"); // add entries to dataset
                    dataSet.setColor(Color.parseColor("#111111"));
                    dataSet.setValueTextColor(Color.parseColor("#111111"));
                    dataSets.add(dataSet);

                    List<Entry> entries1 = new ArrayList<Entry>();
                    entries1.add(new Entry(MyUtils.getTimeinMillis1(trackWeightGraphModel.getStart_date()),trackWeightGraphModel.getCurrent_weight()/1000));
                    entries1.add(new Entry(MyUtils.getTimeinMillis1(trackWeightGraphModel.getEnd_date()),trackWeightGraphModel.getGoal_weight()/1000));
                    LineDataSet dataSet1 = new LineDataSet(entries1, "Ideal"); // add entries to dataset
                    dataSet1.setColor(Color.parseColor("#111111"));
                    dataSet1.setValueTextColor(Color.parseColor("#111111"));
                    dataSets.add(dataSet1);
                    LineData lineData = new LineData(dataSets);
                    mChart.setData(lineData);
                    mChart.invalidate();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TrackMyWeightActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TrackMyWeightActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
