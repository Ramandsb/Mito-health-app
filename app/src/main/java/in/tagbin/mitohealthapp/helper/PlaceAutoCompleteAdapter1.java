package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.EventTypeModel;
import in.tagbin.mitohealthapp.model.HitsArrayModel;
import in.tagbin.mitohealthapp.model.SearchFoodModel;

/**
 * Created by aasaqt on 15/9/16.
 */
public class PlaceAutoCompleteAdapter1 extends ArrayAdapter<HitsArrayModel> {
    private Context mContext;
    private static final String TAG = "PlaceAutocompleteAdapter1";

    private SearchFoodModel mResultList;
    public PlaceAutoCompleteAdapter1(Context context, int resource) {
        super(context,resource);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mResultList.getHits().getHits().size();
    }

    @Override
    public HitsArrayModel getItem(int position) {
        return mResultList.getHits().getHits().get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_autocomplete,null);
        }
        TextView textName = (TextView) v.findViewById(R.id.tvAutocomName);
        TextView textParent = (TextView) v.findViewById(R.id.tvAutocomParent);
        textParent.setVisibility(View.GONE);
        HitsArrayModel search = mResultList.getHits().getHits().get(position);
        textName.setText(search.get_source().getName());
        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint != null) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    RequestFuture<JSONObject> future = RequestFuture.newFuture();
                    String url = "https://search-mito-food-search-7gaq5di2z6edxakcecvnd7q34a.ap-southeast-1.es.amazonaws.com/recipe_data_2/recipe_data_2/_search";
                    String json = "{  \n" +
                            "   \"query\":{  \n" +
                            "      \"bool\":{  \n" +
                            "         \"must\":[  \n" +
                            "            {  \n" +
                            "               \"match_phrase_prefix\":{  \n" +
                            "                  \"name\":'" + constraint.toString() +
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
                    JSONObject jsonObject  =null;
                    try {
                        jsonObject = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request = new JsonObjectRequest(url,jsonObject, future, future);
                    requestQueue.add(request);
                    try {
                        Log.d("values",future.get().toString());
                        mResultList = JsonUtils.objectify(future.get().toString(),SearchFoodModel.class);
                        //mResultList = JsonUtils.objectify(future.get().toString(),EventTypeModel.class);
                        results.values = mResultList.getHits().getHits();
                        results.count = mResultList.getHits().getHits().size();
                        return results;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

}