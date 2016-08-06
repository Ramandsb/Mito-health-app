package in.tagbin.mitohealthapp;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.FoodFrag;
import in.tagbin.mitohealthapp.Fragments.SleepFrag;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by hp on 7/26/2016.
 */
public class SleepAdapter extends RecyclerView.Adapter<SleepAdapter.MyviewHolder> {
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
    public SleepAdapter(Context context) {
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
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        current_item=position;
        final DataItems dataItems= result.get(position);

      String[] strt= dataItems.getSleep_start().split(":");

        Log.d("start time",strt[0]+"//"+strt[1]);
        if (Integer.valueOf(strt[0])<=12){
            holder.start_time.setText(strt[0]+":"+strt[1]+" AM");
            Log.d("if",strt[0]+":"+strt[1]+" AM");
        }else  if (Integer.valueOf(strt[0])>12){
            Log.d("else",(Integer.valueOf(strt[0])-12)+":"+strt[1]+" PM");
            holder.start_time.setText((Integer.valueOf(strt[0])-12)+":"+strt[1]+" PM");
        }else if (Integer.valueOf(strt[0])==0){
            holder.start_time.setText("12 :"+ strt[1]+" AM");
        }

        String[] end= dataItems.getSleep_end().split(":");
        Log.d("end time",end[0]+"//"+end[1]);
        if (Integer.valueOf(end[0])<=12){
            Log.d("if",end[0]+":"+end[1]+" AM");
            holder.end_tiem.setText(end[0]+":"+end[1]+" AM");

        }else  if (Integer.valueOf(end[0])>12){
            holder.end_tiem.setText((Integer.valueOf(end[0])-12)+":"+end[1]+" PM");
            Log.d("end_else",(Integer.valueOf(end[0])-12)+":"+end[1]+" PM");
        }



        holder.start_time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                               // holder.start_time.setText(hourOfDay + ":" + minute);
                                String time=hourOfDay + ":" + minute;
                                ContentValues cv = new ContentValues();
                                cv.put(TableData.Tableinfo.START_TIME, time);
                                notifyDataSetChanged();
                                dop.updateSleepRow(dop, cv, dataItems.getSleep_unique());
                                Log.d("sleep date",SleepFrag.selectedDate);
                                setData(dop.getsleepInformation(dop, SleepFrag.selectedDate));
                            }
                        }, hour, min, false);
                tpd.show();

            }
        });
        holder.end_tiem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                               // holder.end_tiem.setText(hourOfDay + ":" + minute);
                                String time=hourOfDay + ":" + minute;
                                ContentValues cv = new ContentValues();
                                cv.put(TableData.Tableinfo.END_TIME, time);
                                notifyDataSetChanged();
                                dop.updateSleepRow(dop, cv, dataItems.getSleep_unique());
                                setData(dop.getsleepInformation(dop, SleepFrag.selectedDate));
                            }
                        }, hour, min, false);
                tpd.show();

            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dop.deleteRow(dop,dataItems.getSleep_unique());
                result.remove(current_item);
                notifyItemRemoved(current_item);
                setData(dop.getsleepInformation(dop, SleepFrag.selectedDate));
//                notifyDataSetChanged();
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
        ImageView del;


        public MyviewHolder(View rowView) {
            super(rowView);
            start_time = (TextView) rowView.findViewById(R.id.start_time);
            end_tiem = (TextView) rowView.findViewById(R.id.end_time);
            del = (ImageView) rowView.findViewById(R.id.del);
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