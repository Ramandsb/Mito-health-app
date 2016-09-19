package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.EventTypeModel;
import in.tagbin.mitohealthapp.model.InterestModel;

/**
 * Created by aasaqt on 27/8/16.
 */
public class PlaceAutoCompleteAdapter extends ArrayAdapter<EventTypeModel> {
    private Context mContext;
    private static final String TAG = "PlaceAutocompleteAdapter";

    private List<EventTypeModel> mResultList;
    public PlaceAutoCompleteAdapter(Context context, int resource) {
        super(context,resource);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mResultList.size();
    }

    @Override
    public EventTypeModel getItem(int position) {
        return mResultList.get(position);
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
        EventTypeModel search = mResultList.get(position);
        textName.setText(search.getTitle());
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
                    RequestFuture<JSONArray> future = RequestFuture.newFuture();
                    String url = "https://api.mitoapp.com/v1/eventtype/search/?title="+constraint.toString();
                    JsonArrayRequest request = new JsonArrayRequest(url, future, future);
                    requestQueue.add(request);
                    try {
                        Log.d("values",future.get().toString());
                        Type collectionType = new TypeToken<List<EventTypeModel>>() {}.getType();
                        mResultList = (List<EventTypeModel>) new Gson().fromJson( future.get().toString(), collectionType);
                        //mResultList = JsonUtils.objectify(future.get().toString(),EventTypeModel.class);
                        results.values = mResultList;
                        results.count = mResultList.size();
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