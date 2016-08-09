package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyviewHolder> {
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
    public CustomAdapter(Context context) {
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
        View view = inflater.inflate(R.layout.listview_layout, parent, false);
        MyviewHolder viewHolder = new MyviewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyviewHolder holder, int position) {
        current_item=position;
        final DataItems dataItems= result.get(position);

        holder.food_name.setText(dataItems.getFood_name());
        holder.time.setText(dataItems.getTime_consumed());
        holder.quantity.setText(dataItems.getAmount());
        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_dialog(dataItems.getId());
                notifyDataSetChanged();
            }
        });

        Picasso.with(context).load(url).into(holder.food_image);
        holder.select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                holder.time.setText(hourOfDay + ":" + minute);
                                String time=hourOfDay + ":" + minute;
                                ContentValues cv = new ContentValues();
                                cv.put(TableData.Tableinfo.TIME_CONSUMED, time);
                                notifyDataSetChanged();
                                dop.updateRow(dop, cv, dataItems.getId());
                                setData(dop.getInformation(dop, FoodFrag.selectedDate));
                            }
                        }, hour, min, false);
                tpd.show();
            }
        });
        holder.tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"tick"+dataItems.getFood_name() ,Toast.LENGTH_LONG).show();

                try {
                    Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dataItems.getDate()+" "+dataItems.getTime_consumed()+":00");
                    Log.d("converted time",startDate.toString()+"////"+startDate.getTime()+"////"+startDate.getDate());
                    time_stamp=String.valueOf(startDate.getTime()/1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                makeJsonObjReq(dataItems.getFood_id(),time_stamp,dataItems.getAmount());
            }
        });
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"cross"+dataItems.getFood_name(),Toast.LENGTH_LONG).show();
                Cross_dialog(dataItems.getFood_id());
            }
        });
        holder.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"reset"+dataItems.getFood_name(),Toast.LENGTH_LONG).show();
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


    public  void Cross_dialog(final String id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Did you have Something else?");
               alertDialog.setPositiveButton("Yes",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {

                               CollapsableLogging.mSheetLayout.expandFab();

                           }
                       });
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.remove(current_item);
                        notifyItemRemoved(current_item);
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    public  void quantity_dialog(final String id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Enter Quantity");
//        final EditText input = new EditText(context);
        View view= inflater.inflate(R.layout.quantity_getter, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        lp.setMargins(30, 0, 30, 10);
//        input.setLayoutParams(lp);
        final EditText input= (EditText) view.findViewById(R.id.get_quantity);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        quant=input.getText().toString();
                        ContentValues cv = new ContentValues();
                        cv.put(TableData.Tableinfo.AMOUNT, quant + "");
                        dop.updateRow(dop, cv, id);
                        Log.d("quant", quant + "");
                        setData(dop.getInformation(dop, FoodFrag.selectedDate));

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

static class MyviewHolder extends AnimateViewHolder {
    TextView food_name;
    TextView time;
    TextView quantity;
    ImageView tick,cross,reset;
    ImageView food_image;
    View select_time;


    public MyviewHolder(View rowView) {
        super(rowView);
        food_name = (TextView) rowView.findViewById(R.id.food_name);
        time = (TextView) rowView.findViewById(R.id.time_tv);
        quantity = (TextView) rowView.findViewById(R.id.quantity);
        tick = (ImageView) rowView.findViewById(R.id.done);
        cross = (ImageView) rowView.findViewById(R.id.cross);
        reset = (ImageView) rowView.findViewById(R.id.reset);
        select_time=rowView.findViewById(R.id.time_select);
        food_image = (ImageView) rowView.findViewById(R.id.food_image);

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