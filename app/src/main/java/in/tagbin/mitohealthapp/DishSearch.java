package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.mauker.materialsearchview.MaterialSearchView;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.ExerciseFrag;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Pojo.DataItems;

public class DishSearch extends AppCompatActivity {
    AutoCompleteTextView auto_tv;
    ArrayList<String> names,sql_ids,add_dish;
    ArrayAdapter<String> adapter;
    ListView food_list;
    CustomAdapter customAdapter;
    MaterialSearchView searchView;
    public  static String unique_id="";
    String back="";
    View layout;
    DatabaseOperations dop;
    private static int hour=0, min=0, day=0;
    private static final String url = "https://search-mito-food-search-7gaq5di2z6edxakcecvnd7q34a.ap-southeast-1.es.amazonaws.com/mito/recipe/_search";

   static String food_id="";
    String searchUrl="";
    String exerUrl="http://api.mitoapp.com/v1/data/exercise?name=";
    static  String dishName="",time="",quantity="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       back= getIntent().getStringExtra("back");
        Log.d("intent",back);
        layout= findViewById(R.id.layout);
        names = new ArrayList<String>();
        sql_ids=new ArrayList<String>();
        add_dish=new ArrayList<String>();
        auto_tv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        auto_tv.setThreshold(1);
        auto_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(DishSearch.this, i + " itemClicked" + "value got is" + adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();

                Log.d("Item Clicked", "true");

                unique_id = String.valueOf(System.currentTimeMillis());
                add_dish.add(adapterView.getItemAtPosition(i).toString());
             food_id=    sql_ids.get(i);

                dishName=adapterView.getItemAtPosition(i).toString();
                dop = new DatabaseOperations(DishSearch.this);

                if (back.equals("food")){
                    Timepick();
                }else  if (back.equals("exercise")){
                    WheelDialog("Select Weight");

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
                if (s.toString().length() <= 2) {
                    Log.d("tagbhi", s.toString().length() + "");
                } else {
                    if (back.equals("food")){
                        searchUrl=url+"?q="+s.toString();
                        names = new ArrayList<String>();
                        makeJsonObjReq(searchUrl);
                        Log.d("length", s.toString().length() + ""+back);
                    }else if (back.equals("exercise")){
                        names = new ArrayList<String>();
                        searchUrl=exerUrl+s.toString();
                        makeJsonArrayReq(searchUrl);
                        Log.d("length", s.toString().length() + ""+back);
                    }
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (back.equals("exercise")){
                    startActivity(new Intent(DishSearch.this,CollapsableLogging.class).putExtra("selection",2));
                    finish();

                }else if (back.equals("food")){
                    startActivity(new Intent(DishSearch.this,CollapsableLogging.class).putExtra("selection",0));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPause() {
//        exitToBottomAnimation();
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
        super.onPause();
    }
    private void makeJsonObjReq(String s) {

//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("access_token",s);
//        postParam.put("source", "facebook");
//        postParam.put("is_nutritionist", "1");
//
//
//        JSONObject jsonObject = new JSONObject(postParam);
//        Log.d("postpar", jsonObject.toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
               s, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            if (back.equals("food")) {
                                JSONArray ja = response.getJSONObject("hits").getJSONArray("hits");
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
                            } else if (back.equals("exercise")){

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
                            Log.d("tag", "onERROR: "+e.toString());
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

                        Log.d("ArrayRequest Response",response.toString());
                        names.clear();
                        sql_ids.clear();
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject= response.getJSONObject(i);
                              String activity= jsonObject.getString("activity");
                              String id= jsonObject.getString("id");
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

    public  void quantity_dialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Enter Quantity");
        final View view= View.inflate(this,R.layout.quantity_getter, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        lp.setMargins(30, 0, 30, 10);

        final EditText input= (EditText) view.findViewById(R.id.get_quantity);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     quantity=input.getText().toString();
                        dop.putInformation(dop, unique_id, food_id, dishName, time, quantity, FoodFrag.selectedDate, "no");

                        Snackbar.make(layout, "Food Logged Successfuly", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }
    public void Timepick(){

        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        hour=hourOfDay;
                        min=minute;
                        if (hour >12){
                            hour=hour-12;
                            time=hour+":"+min;
                        }else {
                            time=hour+":"+min;
                        }
                        quantity_dialog();

                    }
                }, hour, min, false);
        tpd.show();
    }
    ArrayList<String> weightList,setsList,repsList;
    String weight="20",sets="4",reps="15";
    String currentId="",currentDate="";

    public void setUplists(){
        int weightint=0;
        for (int i=0;i<25;i++){
            weightint=i*5;
            weightList.add(String.valueOf(weightint));
        }
        for (int i =0; i<20;i++){
            setsList.add(String.valueOf(i));
        }
        for (int i =0; i<50;i++){
            repsList.add(String.valueOf(i));
        }

    }
    public void WheelDialog(final String source) {
        String[] feets = null, inches = null, unit = null;


        feets = new String[22];
        inches = new String[21];
        unit = new String[51];
        for (int i = 0; i <= 21; i++) {
            feets[i] = "" + (i * 5);
        }
        for (int i = 0; i <= 20; i++) {
            inches[i] = "" + (i);
        }
        for (int i = 0; i <= 50; i++) {
            unit[i] = "" + (i);
        }


        View outerView = View.inflate(this, R.layout.wheel_view, null);
        if (source.equals("Select Weight")) {
            WheelView wv1 = (WheelView) outerView.findViewById(R.id.wheel_view_wv1);
            wv1.setOffset(2);
            wv1.setItems(Arrays.asList(feets));
            wv1.setSeletion(5);
            wv1.setVisibility(View.VISIBLE);
            wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("feet", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    weight = item;
                }
            });
        }

        if (source.equals("Select Sets")) {
            WheelView wv2 = (WheelView) outerView.findViewById(R.id.wheel_view_wv2);
            wv2.setOffset(2);
            wv2.setItems(Arrays.asList(inches));
            wv2.setSeletion(4);
            wv2.setVisibility(View.VISIBLE);
            wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("inches", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);

                    sets = item;

                }
            });
        }


        if (source.equals("Select Reps")) {
            WheelView wv = (WheelView) outerView.findViewById(R.id.wheel_view_wv3);
            wv.setOffset(2);
            wv.setItems(Arrays.asList(unit));
            wv.setSeletion(5);
            wv.setVisibility(View.VISIBLE);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("Tag", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);

                    reps = item;
                }
            });
        }
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle(source)
                .setView(outerView)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (source.equals("Select Weight")) {

                            Log.d("val", weight);
//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(TableData.Tableinfo.WEIGHT, weight);
//                            dop.updateExerRow(dop, contentValues, currentId);
                            WheelDialog("Select Sets");


                        } else if (source.equals("Select Sets")) {
                            Log.d("val", sets);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(TableData.Tableinfo.SETS, sets);
//                            dop.updateExerRow(dop, contentValues, currentId);
                            WheelDialog("Select Reps");
                        } else if (source.equals("Select Reps")) {
                            Log.d("val", reps);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(TableData.Tableinfo.REPS, reps);
//                            dop.updateExerRow(dop, contentValues, currentId);
             dop.putExerciseInformation(dop,unique_id,food_id,dishName,ExerciseFrag.selectedDate,weight,sets,reps);

                        }

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }
    }
