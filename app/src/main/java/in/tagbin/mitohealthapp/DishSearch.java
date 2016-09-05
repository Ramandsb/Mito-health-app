package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.ExerciseFrag;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.model.ElasticSearchModel;
import in.tagbin.mitohealthapp.model.MustModel;

public class DishSearch extends AppCompatActivity {
    AutoCompleteTextView auto_tv;
    ArrayList<String> names, sql_ids, add_dish;
    ArrayAdapter<String> adapter;
    ListView food_list;
    CustomAdapter customAdapter;
    public static String unique_id = "";
    String back = "";
    SharedPreferences login_details;
    View layout;
    DatabaseOperations dop;
    private static int hour = 0, min = 0, day = 0;
    private static final String url = "https://search-mito-food-search-7gaq5di2z6edxakcecvnd7q34a.ap-southeast-1.es.amazonaws.com/mito/recipe/_search";

    ProgressBar loadingProgress;
    static String food_id = "";
    String searchUrl = "";
    String exerUrl = "http://api.mitoapp.com/v1/data/exercise?name=";
    static String dishName = "", time = "", quantity = "";
    TextView messageView;
    ProgressBar progressBar;
    android.app.AlertDialog alert;

    ImageView search_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        customDialog();

        back = getIntent().getStringExtra("back");
        layout = findViewById(R.id.layout);
        names = new ArrayList<String>();
        sql_ids = new ArrayList<String>();
        add_dish = new ArrayList<String>();
        login_details = getSharedPreferences(MainPage.LOGIN_DETAILS, MODE_PRIVATE);
        auto_tv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        auto_tv.setFocusable(true);
        auto_tv.setThreshold(1);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(auto_tv, InputMethodManager.SHOW_FORCED);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.GONE);
//        search_icon = (ImageView) findViewById(R.id.search_icon);
//        search_icon.setColorFilter(Color.parseColor("#cecece"));

        auto_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (auto_tv.getRight() - auto_tv.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        auto_tv.setText("");
                        return true;
                    }
                }
                return false;
            }
        });


        auto_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(DishSearch.this, i + " itemClicked" + "value got is" + adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();

                Log.d("Item Clicked", "true");

                unique_id = String.valueOf(System.currentTimeMillis());
                add_dish.add(adapterView.getItemAtPosition(i).toString());
                food_id = sql_ids.get(i);

                dishName = adapterView.getItemAtPosition(i).toString();
                dop = new DatabaseOperations(DishSearch.this);

                if (back.equals("food")) {
//                    Timepick();
                    startActivity(new Intent(DishSearch.this,FoodDetails.class).putExtra("food_id",food_id).putExtra("source","dish_search"));
                finish();
                } else if (back.equals("exercise")) {
//                    WheelDialog("Select Weight");
                    dop.putExerciseInformation(dop, unique_id, food_id, dishName, ExerciseFrag.selectedDate, "1", "1", "1");
//                    makeJsonObjReq(food_id,"");
                    Snackbar.make(layout, "Exercise Logged Successfuly", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }



            }
        });
        auto_tv.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
//                Toast.makeText(getActivity(), "changed", Toast.LENGTH_LONG).show();
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                Log.d("tagbhi", count + "");
                loadingProgress.setVisibility(View.VISIBLE);
                if (count==0){
                    loadingProgress.setVisibility(View.GONE);
                }
//                if (s.toString().length() <= 1) {
//                    Log.d("tagbhi", s.toString().length() + "");
//                } else {
                    if (back.equals("food")) {
                        searchUrl = url + "?q=" + s.toString();
                        names = new ArrayList<String>();
                        makeJsonObjReq(s.toString());
                        Log.d("length", s.toString().length() + "" + back);
                    } else if (back.equals("exercise")) {
                        names = new ArrayList<String>();
                        searchUrl = exerUrl + s.toString();
                        makeJsonArrayReq(searchUrl);
                        Log.d("length", s.toString().length() + "" + back);
                    }
//                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (back.equals("exercise")) {
                    startActivity(new Intent(DishSearch.this, CollapsableLogging.class).putExtra("selection", 2));
                    finish();

                } else if (back.equals("food")) {
                    startActivity(new Intent(DishSearch.this, CollapsableLogging.class).putExtra("selection", 0));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home: {
                startActivity(new Intent(DishSearch.this,CollapsableLogging.class).putExtra("selection",0));
                finish();
                break;
            }
        }
        return true;
    }

    RequestListener mRequestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {

            try{
            JSONArray ja = new JSONObject(responseObject.toString()).getJSONObject("hits").getJSONArray("hits");
            names.clear();
            sql_ids.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject c = ja.getJSONObject(i);
                String Restraunt = c.getJSONObject("_source").getString("recipe_name");
                String event_id = c.getJSONObject("_source").getString("sql_id");


                Log.d("description", Restraunt);
                names.add(Restraunt);
                sql_ids.add(event_id);

            }

            adapter = new ArrayAdapter<String>(
                    DishSearch.this, android.R.layout.simple_dropdown_item_1line, names) {
                @Override
                public View getView(int position,
                                    View convertView, ViewGroup parent) {
                    View view = super.getView(position,
                            convertView, parent);
                    TextView text = (TextView) view
                            .findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);
                    return view;
                }
            };
            auto_tv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
//                            Toast.makeText(getActivity(), "try vala expe" + e.toString(), Toast.LENGTH_LONG).show();
            Log.d("tag", "onERROR: " + e.toString());
        }

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("tag", "onERROR: " + message);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DishSearch.this,CollapsableLogging.class).putExtra("selection",0));
        finish();
    }

    @Override
    protected void onPause() {
//        exitToBottomAnimation();
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
        super.onPause();
    }

    private void makeJsonObjReq(String s) {


        Log.d("checkString", s.toString());

        String json="{  \n" +
                "   \"query\":{  \n" +
                "      \"bool\":{  \n" +
                "         \"must\":[  \n" +
                "            {  \n" +
                "               \"match_phrase_prefix\":{  \n" +
                "                  \"recipe_name\":'"+s+
                "'               }\n" +
                "            }\n" +
                "         ],\n" +
                "         \"must_not\":[  \n" +
                "\n" +
                "         ],\n" +
                "         \"should\":[  \n" +
                "\n" +
                "         ]\n" +
                "      }\n" +
                "   },\n" +
                "   \"from\":0,\n" +
                "   \"size\":10,\n" +
                "   \"sort\":[  \n" +
                "\n" +
                "   ],\n" +
                "   \"aggs\":{  \n" +
                "\n" +
                "   }\n" +
                "}";

//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("access_token",s);
//        postParam.put("source", "facebook");
//        postParam.put("is_nutritionist", "1");
//
JSONObject jsonObject = null;
        try {
             jsonObject = new JSONObject(json);
        }catch (JSONException e){
            Log.d("JsonObject",e.toString());

        }
//        Log.d("postpar", jsonObject.toString());
//
        JsonObjectRequest jsonObjReq = null;
            jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("response", response.toString());
                            try {
                                if (back.equals("food")) {
                                    JSONArray ja = response.getJSONObject("hits").getJSONArray("hits");
                                    if (ja.length()==0){
                                        loadingProgress.setVisibility(View.GONE);
                                    }
                                    names.clear();
                                    sql_ids.clear();
                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject c = ja.getJSONObject(i);
                                        String Restraunt = c.getJSONObject("_source").getString("recipe_name");
                                        String event_id = c.getJSONObject("_source").getString("sql_id");


                                        Log.d("description", Restraunt);
                                        names.add(Restraunt);
                                        sql_ids.add(event_id);
                                        loadingProgress.setVisibility(View.GONE);

                                    }
                                } else if (back.equals("exercise")) {

                                }
    //                            Toast.makeText(getActivity(), names.toString(), Toast.LENGTH_LONG).show();
                                adapter = new ArrayAdapter<String>(
                                        DishSearch.this, android.R.layout.simple_dropdown_item_1line, names) {
                                    @Override
                                    public View getView(int position,
                                                        View convertView, ViewGroup parent) {
                                        View view = super.getView(position,
                                                convertView, parent);
                                        TextView text = (TextView) view
                                                .findViewById(android.R.id.text1);
                                        text.setTextColor(Color.BLACK);
                                        return view;
                                    }
                                };
                                auto_tv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
    //                            Toast.makeText(getActivity(), "try vala expe" + e.toString(), Toast.LENGTH_LONG).show();
                                Log.d("tag", "onERROR: " + e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("error", "Error: " + error.getMessage());

                    Log.d("error", error.toString());
                }
            }) {

    //


            };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void makeJsonArrayReq(String s) {

//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("access_token",s);
//        postParam.put("source", "facebook");
//        postParam.put("is_nutritionist", "1");
//
//
//        JSONObject jsonObject = new JSONObject(postParam);
//        Log.d("postpar", jsonObject.toString());

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                s,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("ArrayRequest Response", response.toString());
                        names.clear();
                        sql_ids.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String activity = jsonObject.getString("activity");
                                String id = jsonObject.getString("id");
                                Log.d("activity", activity);
                                names.add(activity);
                                sql_ids.add(id);
                                adapter = new ArrayAdapter<String>(
                                        DishSearch.this, android.R.layout.simple_dropdown_item_1line, names) {
                                    @Override
                                    public View getView(int position,
                                                        View convertView, ViewGroup parent) {
                                        View view = super.getView(position,
                                                convertView, parent);
                                        TextView text = (TextView) view
                                                .findViewById(android.R.id.text1);
                                        text.setTextColor(Color.BLACK);
                                        return view;
                                    }
                                };
                                auto_tv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());

                Log.d("error", error.toString());
            }
        }) {

//


        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }








    public void customDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
