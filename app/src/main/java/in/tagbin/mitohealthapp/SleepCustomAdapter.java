package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by admin pc on 13-07-2016.
 */
public class SleepCustomAdapter extends RecyclerView.Adapter<SleepCustomAdapter.MyviewHolder> {
    ArrayList<DataItems> result;
    Context context;
    String quant="";
    int[] imageId;
    private int hour, min, day;
    private DatePicker datePicker;
    private Calendar calendar;
    DatabaseOperations dop;
    private static LayoutInflater inflater = null;
    SharedPreferences login_details;
    String auth_key;
    int current_item=0;
    String time_stamp="";
    String url="http://pngimg.com/upload/small/apple_PNG12458.png";
    public SleepCustomAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        inflater = LayoutInflater.from(context);
        dop = new DatabaseOperations(context);
        login_details=context.getSharedPreferences(MainPage.LOGIN_DETAILS, context.MODE_PRIVATE);
    }


    public void setData(ArrayList<DataItems> list) {
        this.result = list;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sleep_list_layout, parent, false);
        MyviewHolder viewHolder = new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {
        current_item=position;
        final DataItems dataItems= result.get(position);

        holder.start_time.setText(dataItems.getSleep_start());
        holder.end_tiem.setText(dataItems.getSleep_end());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class Holder {



    }


static class MyviewHolder extends AnimateViewHolder {
    TextView start_time;
    TextView end_tiem;
    Button del;


    public MyviewHolder(View rowView) {
        super(rowView);
        start_time = (TextView) rowView.findViewById(R.id.start_time);
        end_tiem = (TextView) rowView.findViewById(R.id.end_time);
        del = (Button) rowView.findViewById(R.id.del);
    }

    @Override
    public void animateAddImpl(ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(itemView)
                .translationY(0)
                .alpha(1)
                .setDuration(300)
                .setListener(listener)
                .start();
    }

    @Override
    public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(itemView)
                .translationY(-itemView.getHeight() * 0.3f)
                .alpha(0)
                .setDuration(300)
                .setListener(listener)
                .start();
    }

    @Override
    public void preAnimateAddImpl() {
        ViewCompat.setTranslationY(itemView, -itemView.getHeight() * 0.3f);
        ViewCompat.setAlpha(itemView, 0);
    }
}

    private void makeJsonObjReq(String food_id,String time_stamp,String amount) {

      auth_key=   login_details.getString("auth_key", "");
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







                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());


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




}