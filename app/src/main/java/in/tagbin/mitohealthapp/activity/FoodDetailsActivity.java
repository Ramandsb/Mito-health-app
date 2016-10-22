package in.tagbin.mitohealthapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import in.tagbin.mitohealthapp.model.SetFoodLoggerModel;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by aasaqt on 18/10/16.
 */

public class FoodDetailsActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
    String url = "http://pngimg.com/upload/small/apple_PNG12458.png";
    TextView calories,fats,protiens,carbs,name,recipeTime,recipeDetails,viewRecipeDetails;
    EditText quantity_ed,set_time;
    Spinner set_unit;
    RecommendationModel.MealsModel data;
    boolean logged = false;
    View view;
    float total;
    RelativeLayout relativeTop;
    ScrollView svrecipeDetails,svfoodDetails;
    GifImageView progressBar;
    DonutProgress fatsProgress,protiensProgress,carbsProgress;
    ImageView fab,mainImage,backImage;
    String response, quantity,time;
    PrefManager pref;
    int servingUnit,year,month,day,hour,minute;
    final List<String> measuring_units = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_food_details);
        calories = (TextView) findViewById(R.id.tvEnterCalories);
        fats = (TextView) findViewById(R.id.tvFatsValue);
        protiens = (TextView) findViewById(R.id.tvProtiensValue);
        carbs = (TextView) findViewById(R.id.tvCarbsValue);
        name = (TextView) findViewById(R.id.tvFoodNameDetails);
        set_time = (EditText) findViewById(R.id.set_time);
        recipeTime =  (TextView) findViewById(R.id.preparation_time);
        recipeDetails =  (TextView) findViewById(R.id.set_recipe);
        viewRecipeDetails =  (TextView) findViewById(R.id.tvViewRecipeDetails);
        quantity_ed = (EditText) findViewById(R.id.quantity_ed);
        set_unit = (Spinner) findViewById(R.id.measuring_type);
        mainImage = (ImageView) findViewById(R.id.seeFood);
        backImage = (ImageView) findViewById(R.id.ivFoodBack);
        fatsProgress = (DonutProgress) findViewById(R.id.progressFats);
        carbsProgress = (DonutProgress) findViewById(R.id.progressCarbs);
        protiensProgress = (DonutProgress) findViewById(R.id.progressProtiens);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        relativeTop = (RelativeLayout) findViewById(R.id.relativeTopFoodDetails);
        svfoodDetails = (ScrollView) findViewById(R.id.scrollViewFoodDetails);
        svrecipeDetails = (ScrollView) findViewById(R.id.scrollViewRecipeDetails);
        pref = new PrefManager(this);
        backImage.setOnClickListener(this);
        set_time.setOnClickListener(this);
        relativeTop.setOnClickListener(this);
        view=findViewById(R.id.cont);
        response = getIntent().getStringExtra("response");
        if (getIntent().getStringExtra("logger") != null)
            logged = true;
        Log.d("food details",response);
        data = JsonUtils.objectify(response, RecommendationModel.MealsModel.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        }
        name.setText(data.getComponent().getName());
        MaterialFavoriteButton toolbarFavorite = (MaterialFavoriteButton) findViewById(R.id.favorite_nice); //
        toolbarFavorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {

                        if (favorite){
                            progressBar.setVisibility(View.VISIBLE);
                            Controller.setFavourite(FoodDetailsActivity.this,"like",data.getComponent().getId(),mFavouriteListener);
                            Snackbar.make(buttonView, "Dish added to your Favorites",Snackbar.LENGTH_SHORT).show();
                        }else {
                            progressBar.setVisibility(View.VISIBLE);
                            Controller.setFavourite(FoodDetailsActivity.this,"unlike",data.getComponent().getId(),mFavouriteListener1);
                            Snackbar.make(buttonView, "Dish Removed from your Favorites",Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
        if (data.getComponent().getImage() != null)
            Picasso.with(this).load(data.getComponent().getImage()).into(mainImage);
        else
            Picasso.with(this).load(url).into(mainImage);
        fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        measuring_units.add(data.getComponent().getServing_type().getServing_type());
        for (int i=0;i<data.getComponent().getOther_serving_detail().size();i++){
            measuring_units.add(data.getComponent().getOther_serving_detail().get(i).getServing_type().getServing_type());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, measuring_units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        set_unit.setAdapter(adapter);
        set_unit.setSelection(0,false);
        final String[] unit = {data.getComponent().getServing_type().getServing_type()};
        servingUnit = data.getComponent().getServing_type().getId();
        total = data.getComponent().getTotal_protein()+data.getComponent().getTotal_fat()+data.getComponent().getTotal_carbohydrate();
        set_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("item selected", measuring_units.get(i));
                unit[0] = measuring_units.get(i);
                if (unit[0].equals(data.getComponent().getServing_type().getServing_type())){
                    protiens.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_protein()).toString()+" gm");
                    fats.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_fat()).toString()+" gm");
                    carbs.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_carbohydrate()).toString()+" gm");
                    calories.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_energy()/data.getComponent().getServing_unit()).toString()+" Cal");
                    float carbsPercentage = (data.getComponent().getTotal_carbohydrate()*100)/total;
                    float fatsPercentage = (data.getComponent().getTotal_fat()*100)/total;
                    float protiensPercentage = (data.getComponent().getTotal_protein()*100)/total;
                    carbsProgress.setProgress((int) carbsPercentage);
                    fatsProgress.setProgress((int) fatsPercentage);
                    protiensProgress.setProgress((int) protiensPercentage);
                    servingUnit = data.getComponent().getServing_type().getId();
                }else {
                    for (int y = 0; y < data.getComponent().getOther_serving_detail().size(); y++) {
                        if (unit[0].equals(data.getComponent().getOther_serving_detail().get(y).getServing_type().getServing_type())) {
                            total = (data.getComponent().getOther_serving_detail().get(y).getTotal_protein()+data.getComponent().getOther_serving_detail().get(y).getTotal_fat()+data.getComponent().getOther_serving_detail().get(y).getTotal_carbohydrate());
                            protiens.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_protein()).toString() + " gm");
                            fats.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_fat()).toString() + " gm");
                            carbs.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_carbohydrate()).toString() + " gm");
                            calories.setText(new DecimalFormat("##.#").format(data.getComponent().getOther_serving_detail().get(y).getTotal_energy()/data.getComponent().getOther_serving_detail().get(y).getServing_unit()).toString() + " Cal");
                            float carbsPercentage = (data.getComponent().getOther_serving_detail().get(y).getTotal_carbohydrate()*100)/total;
                            float fatsPercentage = (data.getComponent().getOther_serving_detail().get(y).getTotal_fat()*100)/total;
                            float protiensPercentage = (data.getComponent().getOther_serving_detail().get(y).getTotal_protein()*100)/total;
                            carbsProgress.setProgress((int) carbsPercentage);
                            fatsProgress.setProgress((int) fatsPercentage);
                            protiensProgress.setProgress((int) protiensPercentage);
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
            protiens.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_protein()).toString()+" gm");
            fats.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_fat()).toString()+" gm");
            carbs.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_carbohydrate()).toString()+" gm");
            calories.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_energy()/data.getComponent().getServing_unit()).toString()+" Cal");
            float carbsPercentage = (data.getComponent().getTotal_carbohydrate()*100)/total;
            float fatsPercentage = (data.getComponent().getTotal_fat()*100)/total;
            float protiensPercentage = (data.getComponent().getTotal_protein()*100)/total;
            carbsProgress.setProgress((int) carbsPercentage);
            fatsProgress.setProgress((int) fatsPercentage);
            protiensProgress.setProgress((int) protiensPercentage);
        }else{
            quantity_ed.setText(""+data.getAmount());
            quantity = String.valueOf(data.getAmount());
            protiens.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_protein()*data.getAmount()).toString()+" gm");
            fats.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_fat()*data.getAmount()).toString()+" gm");
            carbs.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_carbohydrate()*data.getAmount()).toString()+" gm");
            calories.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_energy()*data.getAmount()/data.getComponent().getServing_unit()).toString()+" Cal");
            float carbsPercentage = (data.getComponent().getTotal_carbohydrate()*100)/total;
            float fatsPercentage = (data.getComponent().getTotal_fat()*100)/total;
            float protiensPercentage = (data.getComponent().getTotal_protein()*100)/total;
            carbsProgress.setProgress((int) carbsPercentage);
            fatsProgress.setProgress((int) fatsPercentage);
            protiensProgress.setProgress((int) protiensPercentage);
        }
        quantity_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                quantity = charSequence.toString();
                if (!quantity.equals("")) {
                    protiens.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_protein() * Float.parseFloat(quantity)).toString() + " gm");
                    fats.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_fat() * Float.parseFloat(quantity)).toString() + " gm");
                    carbs.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_carbohydrate() * Float.parseFloat(quantity)).toString() + " gm");
                    calories.setText(new DecimalFormat("##.#").format(data.getComponent().getTotal_energy() * Float.parseFloat(quantity)/data.getComponent().getServing_unit()).toString() + " Cal");
                    float carbsPercentage = (data.getComponent().getTotal_carbohydrate()*100)/total;
                    float fatsPercentage = (data.getComponent().getTotal_fat()*100)/total;
                    float protiensPercentage = (data.getComponent().getTotal_protein()*100)/total;
                    carbsProgress.setProgress((int) carbsPercentage);
                    fatsProgress.setProgress((int) fatsPercentage);
                    protiensProgress.setProgress((int) protiensPercentage);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        recipeTime.setText(data.getComponent().getPreparation_time()+" mins");
        if (data.getComponent().getRecipe() != null)
            recipeDetails.setText(data.getComponent().getRecipe());
        else
            recipeDetails.setText("Coming Soon!");
    }
    public void sendRequest(){
        if (quantity.equals("")){
            Snackbar.make(view, "Please enter quantity", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else if (getIntent().getStringExtra("logger") != null){
            SetFoodLoggerModel setFoodLoggerModel = new SetFoodLoggerModel();
            setFoodLoggerModel.setLtype("food");
            setFoodLoggerModel.setC_id(data.getComponent().getId());
            setFoodLoggerModel.setServing_unit(servingUnit);
            Date date = new Date(year - 1900, month, day, hour, minute);
            long time = date.getTime()/1000L;
            Log.d("timesatmap",""+time);
            setFoodLoggerModel.setTime_consumed(time);
            setFoodLoggerModel.setAmount(Float.parseFloat(quantity));
            Log.d("model2",JsonUtils.jsonify(setFoodLoggerModel));
            progressBar.setVisibility(View.VISIBLE);
            Controller.updateLogFood(FoodDetailsActivity.this,setFoodLoggerModel,data.getId(),mFoodUpdateListener);
        }else if (getIntent().getStringExtra("foodsearch") != null){
            SetFoodLoggerModel setFoodLoggerModel = new SetFoodLoggerModel();
            setFoodLoggerModel.setLtype("food");
            setFoodLoggerModel.setServing_unit(servingUnit);
            setFoodLoggerModel.setC_id(data.getComponent().getId());
            Date date = new Date(year - 1900, month, day, hour, minute);
            long time = date.getTime()/1000L;
            Log.d("timesatmap",""+time);
            setFoodLoggerModel.setTime_consumed(time);
            setFoodLoggerModel.setFlag(1);
            setFoodLoggerModel.setAmount(Float.parseFloat(quantity));
            Log.d("model",JsonUtils.jsonify(setFoodLoggerModel));
            progressBar.setVisibility(View.VISIBLE);
            Controller.setLogger(FoodDetailsActivity.this,setFoodLoggerModel,mFoodLoggerListener);
        }else {
            SetFoodLoggerModel setFoodLoggerModel = new SetFoodLoggerModel();
            setFoodLoggerModel.setLtype("food");
            setFoodLoggerModel.setC_id(data.getComponent().getId());
            Date date = new Date(year - 1900, month, day, hour, minute);
            long time = date.getTime()/1000L;
            Log.d("timesatmap",""+time);
            setFoodLoggerModel.setTime_consumed(time);
            setFoodLoggerModel.setMeal_id(data.getMeal_id());
            setFoodLoggerModel.setFlag(1);
            setFoodLoggerModel.setServing_unit(servingUnit);
            setFoodLoggerModel.setAmount(Float.parseFloat(quantity));
            Log.d("model1",JsonUtils.jsonify(setFoodLoggerModel));
            progressBar.setVisibility(View.VISIBLE);
            Controller.setLogger(FoodDetailsActivity.this,setFoodLoggerModel,mFoodLoggerListener);
        }
    }
    RequestListener mFavouriteListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("favourite ",responseObject.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FoodDetailsActivity.this,"Dish added to your Favorites",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("favourite error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mFavouriteListener1 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("favourite ",responseObject.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FoodDetailsActivity.this,"Dish removed from your Favorites",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("favourite error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
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
    RequestListener mFoodLoggerListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("food logger",responseObject.toString());
            Intent i = new Intent(FoodDetailsActivity.this,DailyDetailsActivity.class);
            i.putExtra("selection",0);
            startActivity(i);
            finish();
            logged = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FoodDetailsActivity.this,"Food successfully logged",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("food logger error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mFoodUpdateListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("food logger",responseObject.toString());
            Intent i = new Intent(FoodDetailsActivity.this,DailyDetailsActivity.class);
            i.putExtra("selection",0);
            startActivity(i);
            finish();
            logged = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FoodDetailsActivity.this,"Food successfully updated",Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("food logger error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodDetailsActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivFoodBack:
                onBackPressed();
                break;
            case R.id.set_time:
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
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog tpd = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(this, hour, minute, false);
                tpd.show(getFragmentManager(), "TIME_PICKER_TAG");
                break;
            case R.id.relativeTopFoodDetails:
                if (svrecipeDetails.getVisibility() == View.VISIBLE){
                    svfoodDetails.setVisibility(View.VISIBLE);
                    svrecipeDetails.setVisibility(View.GONE);
                    viewRecipeDetails.setText("View Recipe Details");
                    fab.setVisibility(View.VISIBLE);
                }else {
                    svrecipeDetails.setVisibility(View.VISIBLE);
                    svfoodDetails.setVisibility(View.GONE);
                    viewRecipeDetails.setText("View Nutritive Details");
                    fab.setVisibility(View.GONE);
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (svrecipeDetails.getVisibility() == View.VISIBLE){
            svfoodDetails.setVisibility(View.VISIBLE);
            svrecipeDetails.setVisibility(View.GONE);
            viewRecipeDetails.setText("View Recipe Details");
            fab.setVisibility(View.VISIBLE);
        }else {
            if (!logged)
                closeAppDialog();
            else
                finish();
        }

    }
    public void closeAppDialog(){
        AlertDialog.Builder dialog=  new AlertDialog.Builder(this);
        dialog.setTitle("Save Changes");
        dialog.setMessage("Do you want to Log this food item?");
        dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendRequest();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();

    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        hour = hourOfDay;
        this.minute = minute;
        updateTime1(hour,minute);
    }
}
