package in.tagbin.mitohealthapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.HitsArrayModel;
import in.tagbin.mitohealthapp.model.SearchFoodModel;

/**
 * Created by aasaqt on 15/9/16.
 */
public class FoodLoggerSearchAdapter extends ArrayAdapter<HitsArrayModel> {
    private Context mContext;
    private static final String TAG = "FoodLoggerSearchAdapter";

    private SearchFoodModel mResultList;
    public FoodLoggerSearchAdapter(Context context, int resource) {
        super(context,resource);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mResultList.getHits().size();
    }

    @Override
    public HitsArrayModel getItem(int position) {
        return mResultList.getHits().get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.item_search,null);
        }
        TextView textName = (TextView) v.findViewById(R.id.tvAutocomName);
        TextView textParent = (TextView) v.findViewById(R.id.tvAutocomParent);
        textParent.setVisibility(View.GONE);
        HitsArrayModel search = mResultList.getHits().get(position);
        textName.setText(search.getName());
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
                    String url = "https://api.mitoapp.com/v1/elastic_search/recipe_search?keyword="+constraint.toString();
                    url = url.replace(" ","%20");
                    JsonObjectRequest request = new JsonObjectRequest(url,null, future, future);
                    requestQueue.add(request);
                    try {
                        Log.d("values",future.get().toString());
                        mResultList = JsonUtils.objectify(future.get().toString(),SearchFoodModel.class);
                        //mResultList = JsonUtils.objectify(future.get().toString(),EventTypeModel.class);
                        results.values = mResultList.getHits();
                        results.count = mResultList.getHits().size();
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