package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.ExerciseFrag;
import in.tagbin.mitohealthapp.Fragments.FoodDetailsFrag;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Fragments.RecipeDetailsFrag;
import in.tagbin.mitohealthapp.Fragments.SleepFrag;
import in.tagbin.mitohealthapp.Fragments.WaterFrag;
import in.tagbin.mitohealthapp.helper.ViewPagerAdapter;

public class FoodDetails extends AppCompatActivity {
    String url = "http://pngimg.com/upload/small/apple_PNG12458.png";
    ViewPager viewPager;
    PagerAdapter adapter;
    TabLayout tabLayout;
  public static String food_id,source;
    DatabaseOperations dop;
    int currentPosition=0;
    SharedPreferences login_details;
    String auth_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customDialog();
        login_details = getSharedPreferences(MainPage.LOGIN_DETAILS,MODE_PRIVATE);
        food_id=  getIntent().getStringExtra("food_id");
        source=  getIntent().getStringExtra("source");
dop= new DatabaseOperations(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Nutrition Value"));
        tabLayout.addTab(tabLayout.newTab().setText("Recipe"));
        viewPager = (ViewPager) findViewById(R.id.pager);

//        makeResetJsonObjReq(food_id);
        setupViewPager(viewPager);
//        adapter = new PagerAdapter
//                (getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
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
        Picasso.with(this).load(url).into(imageView);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    if (FoodDetails.source.equals("dish_search")){
                        if (currentPosition==0) {

                            if (FoodDetailsFrag.time.equals("") || FoodDetailsFrag.quantity.equals("")) {
                                Snackbar.make(view, "Set Time & Quantity", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {

                                String time_stamp = "";
                                try {
                                    Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(FoodFrag.selectedDate+" "+FoodDetailsFrag.time+":00");
                                    Log.d("converted time",startDate.toString()+"////"+startDate.getTime()+"////"+startDate.getDate());
                                   time_stamp=String.valueOf(startDate.getTime()/1000);
                                    makeJsonObjReq(food_id,time_stamp,FoodDetailsFrag.quantity);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//

                            }
                        }else {
                            Snackbar.make(view, "See Recipe", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }



                    }else if (FoodDetails.source.equals("food_frag")){
                        startActivity(new Intent(FoodDetails.this,CollapsableLogging.class).putExtra("selection",0));
                        finish();
                    }



            }
        });
    }
    private void makeJsonObjReq(String food_id,String time_stamp,String amount) {

        showDialog();

       auth_key=   login_details.getString("key", "");
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("ltype", "food");
        postParam.put("c_id", food_id);
        postParam.put("time_consumed", time_stamp);
        postParam.put("amount", amount);



        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.url+"logger/", jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());



                        unique_id = String.valueOf(System.currentTimeMillis());
                        dop.putInformation(dop, unique_id, FoodDetailsFrag.mParam2, FoodDetailsFrag.dishName, FoodDetailsFrag.time, FoodDetailsFrag.quantity, FoodFrag.selectedDate, "yes");
                        startActivity(new Intent(FoodDetails.this,DishSearch.class).putExtra("back","food"));
                        finish();
                        dismissDialog();




                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());

                displayErrors(error);

                Log.d("error", error.toString());
            }
        }) {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
                headers.put("Authorization","JWT "+auth_key);
                return headers;
            }



        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public static String unique_id = "";
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(FoodDetailsFrag.newInstance("food_id",food_id), "Nutrition Value");
        adapter.addFragment(RecipeDetailsFrag.newInstance("food_id",food_id), "Recipe");
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

    private void makeResetJsonObjReq(String food_id) {

        showDialog();
//
//http://api.mitoapp.com/v1/food/65/

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Config.url+"food/"+food_id, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());






                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());

                displayErrors(error);

                Log.d("error", error.toString());
            }
        }) {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
//                headers.put("Authorization","JWT "+auth_key);
                return headers;
            }



        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    TextView messageView;
    ProgressBar progressBar;
    android.app.AlertDialog alert;


    public void customDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }


    public void showDialog() {

        progressBar.setVisibility(View.VISIBLE);
        alert.show();
        messageView.setText("Loading");
    }

    public void dismissDialog() {
        alert.dismiss();
    }

    public void displayErrors(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Connection failed");
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("AuthFailureError");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ServerError");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }

}
