package in.tagbin.mitohealthapp;

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
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;
import in.tagbin.mitohealthapp.CalenderView.RWeekCalendar;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
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
    private static final String url = "https://search-mito-food-search-7gaq5di2z6edxakcecvnd7q34a.ap-southeast-1.es.amazonaws.com/mito/recipe/_search";

    String searchUrl="";
    String exerUrl="http://api.mitoapp.com/v1/data/exercise?name=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       back= getIntent().getStringExtra("back");
        Log.d("intent",back);

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
            String food_id=    sql_ids.get(i);
                DatabaseOperations dop = new DatabaseOperations(DishSearch.this);

                if (back.equals("food")){
                    dop.putInformation(dop, unique_id, food_id, adapterView.getItemAtPosition(i).toString(), "2:30", "3", FoodFrag.selectedDate, "yes");

                }else  if (back.equals("exercise")){
                    dop.putExerciseInformation(dop,unique_id,food_id,adapterView.getItemAtPosition(i).toString(),ExerciseFrag.selectedDate,"20","5","3");


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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

}
