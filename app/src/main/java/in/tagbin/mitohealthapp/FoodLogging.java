package in.tagbin.mitohealthapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.mauker.materialsearchview.MaterialSearchView;
import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Pojo.DataItems;

public class FoodLogging extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_logging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
//        food_list.setAdapter(customAdapter);

//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d("ontext change",newText);
//                return false;
//            }
//
//        });
//
//        searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewOpened() {
//                // Do something once the view is open.
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//                // Do something once the view is closed.
//            }
//        });

//        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Do something when the suggestion list is clicked.
//                TextView tv = (TextView) view.findViewById(R.id.tv_str);
//
//                if (tv != null) {
//                    searchView.setQuery(tv.getText().toString(), false);
//                }
//            }
//        });



//        if (auto_tv != null) {
//        auto_tv.setThreshold(1);
//        auto_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(FoodLogging.this, i + " itemClicked" + "value got is" + adapterView.getItemAtPosition(i), Toast.LENGTH_LONG).show();
//
//                Log.d("Item Clicked", "true");
//
//                add_dish.add(adapterView.getItemAtPosition(i).toString());
//                DatabaseOperations dop= new DatabaseOperations(FoodLogging.this);
//                dop.putInformation(dop, "1", "234", adapterView.getItemAtPosition(i).toString(), "2:30", "3", "22", "yes");
//                customAdapter.notifyDataSetChanged();
//
//
//
//            }
//        });
//        auto_tv.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
////                Toast.makeText(getActivity(), "changed", Toast.LENGTH_LONG).show();
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                if (s.toString().length() <= 2) {
//                    Log.d("tagbhi", s.toString().length() + "");
//                } else {
//                    names = new ArrayList<String>();
////                    makeJsonObjReq(s.toString());
//                }
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here. It'll
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                // Open the search view on the menu item click.

//                searchView.openSearch();
                startActivity(new Intent(FoodLogging.this,DishSearch.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
//    private void makeJsonObjReq(String s) {
//
////        Map<String, String> postParam = new HashMap<String, String>();
////        postParam.put("access_token",s);
////        postParam.put("source", "facebook");
////        postParam.put("is_nutritionist", "1");
////
////
////        JSONObject jsonObject = new JSONObject(postParam);
////        Log.d("postpar", jsonObject.toString());
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                url+"?q="+s, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("response", response.toString());
//                        try {
//                            JSONArray ja = response.getJSONObject("hits").getJSONArray("hits");
//                            names.clear();
//                            for (int i = 0; i < ja.length(); i++) {
//                                JSONObject c = ja.getJSONObject(i);
//                                String Restraunt = c.getJSONObject("_source").getString("recipe_name");
//                              String   event_id = c.getJSONObject("_source").getString("sql_id");
//
//
//                                Log.d("description", Restraunt);
//                                names.add(Restraunt);
//                                sql_ids.add(event_id);
//
//                            }
////                            Toast.makeText(getActivity(), names.toString(), Toast.LENGTH_LONG).show();
//                            adapter = new ArrayAdapter<String>(
//                                    FoodLogging.this, android.R.layout.simple_dropdown_item_1line, names) {
//                                @Override
//                                public View getView(int position,
//                                                    View convertView, ViewGroup parent) {
//                                    View view = super.getView(position,
//                                            convertView, parent);
//                                    TextView text = (TextView) view
//                                            .findViewById(android.R.id.text1);
//                                    text.setTextColor(Color.BLACK);
//                                    return view;
//                                }
//                            };
//                            auto_tv.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                        } catch (Exception e) {
////                            Toast.makeText(getActivity(), "try vala expe" + e.toString(), Toast.LENGTH_LONG).show();
//                            Log.d("tag", "onERROR: "+e.toString());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("error", "Error: " + error.getMessage());
//
//                Log.d("error", error.toString());
//            }
//        }) {
//
////
//
//
//        };
//
//
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (searchView.isOpen()) {
//            // Close the search on the back button press.
//            searchView.closeSearch();
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
//            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            if (matches != null && matches.size() > 0) {
//                String searchWrd = matches.get(0);
//                if (!TextUtils.isEmpty(searchWrd)) {
//                    searchView.setQuery(searchWrd, false);
//                }
//            }
//
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onPause() {
        super.onPause();
//        searchView.clearSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        searchView.activityResumed();
//        String[] arr = getResources().getStringArray(name);

//        searchView.addSuggestions(names);
    }

//    private void clearHistory() {
//        searchView.clearHistory();
//    }
//
//    private void clearSuggestions() {
//        searchView.clearSuggestions();
//    }
//
//    private void clearAll() {
//        searchView.clearAll();
//    }
}
