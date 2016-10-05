package in.tagbin.mitohealthapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.tagbin.mitohealthapp.Fragments.FoodDetailsFragment;
import in.tagbin.mitohealthapp.Fragments.RecipeDetailsFragment;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.RecommendationModel;
import in.tagbin.mitohealthapp.model.SetFoodLoggerModel;
import pl.droidsonroids.gif.GifImageView;

public class FoodDetailsActivity extends AppCompatActivity {
    String url = "http://pngimg.com/upload/small/apple_PNG12458.png";
    ViewPager viewPager;
    TabLayout tabLayout;
    long time_stamp =0;
    int currentPosition=0;
    String response;
    GifImageView progressBar;
    RecommendationModel.MealsModel data;
    boolean logged = false;
    View view;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view=findViewById(R.id.cont);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        tabLayout.addTab(tabLayout.newTab().setText("Nutrition Value"));
        tabLayout.addTab(tabLayout.newTab().setText("Recipe"));
        viewPager = (ViewPager) findViewById(R.id.pager);
        response = getIntent().getStringExtra("response");
        if (getIntent().getStringExtra("logger") != null)
            logged = true;
        Log.d("food details",response);
        data = JsonUtils.objectify(response, RecommendationModel.MealsModel.class);
        getSupportActionBar().setTitle(data.getComponent().getName());
        MaterialFavoriteButton toolbarFavorite = (MaterialFavoriteButton) toolbar.findViewById(R.id.favorite_nice); //
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
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                currentPosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        ImageView imageView = (ImageView) findViewById(R.id.seeFood);
        if (data.getComponent().getImage() != null)
            Picasso.with(this).load(data.getComponent().getImage()).into(imageView);
        else
            Picasso.with(this).load(url).into(imageView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home: {
                if (!logged)
                    closeAppDialog();
                else
                    finish();
                break;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (!logged)
            closeAppDialog();
        else
            finish();

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
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment detailsFrag = new FoodDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("response",getIntent().getStringExtra("response"));
        detailsFrag.setArguments(bundle);
        Fragment recipeFrag = new RecipeDetailsFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("response",getIntent().getStringExtra("response"));
        recipeFrag.setArguments(bundle1);
        adapter.addFragment(detailsFrag, "Nutrition Value");
        adapter.addFragment(recipeFrag, "Recipe");
        viewPager.setAdapter(adapter);
    }

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
    public void sendRequest(){
        if (FoodDetailsFragment.quantity.equals("")){
            Snackbar.make(view, "Please enter quantity", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else if (getIntent().getStringExtra("logger") != null){
            SetFoodLoggerModel setFoodLoggerModel = new SetFoodLoggerModel();
            setFoodLoggerModel.setLtype("food");
            setFoodLoggerModel.setC_id(data.getComponent().getId());
            setFoodLoggerModel.setServing_unit(FoodDetailsFragment.servingUnit);
            Date date = new Date(FoodDetailsFragment.year - 1900, FoodDetailsFragment.month, FoodDetailsFragment.day, FoodDetailsFragment.hour, FoodDetailsFragment.minute);
            long time = date.getTime()/1000L;
            Log.d("timesatmap",""+time);
            setFoodLoggerModel.setTime_consumed(time);
            setFoodLoggerModel.setAmount(Float.parseFloat(FoodDetailsFragment.quantity));
            Log.d("model2",JsonUtils.jsonify(setFoodLoggerModel));
            progressBar.setVisibility(View.VISIBLE);
            Controller.updateLogFood(FoodDetailsActivity.this,setFoodLoggerModel,data.getId(),mFoodUpdateListener);
        }else if (getIntent().getStringExtra("foodsearch") != null){
             SetFoodLoggerModel setFoodLoggerModel = new SetFoodLoggerModel();
             setFoodLoggerModel.setLtype("food");
            setFoodLoggerModel.setServing_unit(FoodDetailsFragment.servingUnit);
             setFoodLoggerModel.setC_id(data.getComponent().getId());
             Date date = new Date(FoodDetailsFragment.year - 1900, FoodDetailsFragment.month, FoodDetailsFragment.day, FoodDetailsFragment.hour, FoodDetailsFragment.minute);
             long time = date.getTime()/1000L;
             Log.d("timesatmap",""+time);
             setFoodLoggerModel.setTime_consumed(time);
            setFoodLoggerModel.setFlag(1);
             setFoodLoggerModel.setAmount(Float.parseFloat(FoodDetailsFragment.quantity));
            Log.d("model",JsonUtils.jsonify(setFoodLoggerModel));
             progressBar.setVisibility(View.VISIBLE);
             Controller.setLogger(FoodDetailsActivity.this,setFoodLoggerModel,mFoodLoggerListener);
         }else {
            SetFoodLoggerModel setFoodLoggerModel = new SetFoodLoggerModel();
            setFoodLoggerModel.setLtype("food");
            setFoodLoggerModel.setC_id(data.getComponent().getId());
            Date date = new Date(FoodDetailsFragment.year - 1900, FoodDetailsFragment.month, FoodDetailsFragment.day, FoodDetailsFragment.hour, FoodDetailsFragment.minute);
            long time = date.getTime()/1000L;
            Log.d("timesatmap",""+time);
            setFoodLoggerModel.setTime_consumed(time);
             setFoodLoggerModel.setMeal_id(data.getMeal_id());
             setFoodLoggerModel.setFlag(1);
            setFoodLoggerModel.setServing_unit(FoodDetailsFragment.servingUnit);
            setFoodLoggerModel.setAmount(Float.parseFloat(FoodDetailsFragment.quantity));
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
}