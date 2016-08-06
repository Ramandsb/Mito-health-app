package in.tagbin.mitohealthapp;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.Database.DatabaseOperations;
import in.tagbin.mitohealthapp.Database.TableData;
import in.tagbin.mitohealthapp.Fragments.SleepFrag;
import in.tagbin.mitohealthapp.Pojo.DataItems;
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by hp on 7/26/2016.
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MyviewHolder> {
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
    ArrayList<String> weightList,setsList,repsList;
    String weight="20",sets="4",reps="15";
    String currentId="",currentDate="";
    String url="http://pngimg.com/upload/small/apple_PNG12458.png";
    public ExerciseAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        inflater = LayoutInflater.from(context);
        dop = new DatabaseOperations(context);
        login_details=context.getSharedPreferences(MainPage.LOGIN_DETAILS, context.MODE_PRIVATE);
        weightList= new ArrayList<>();
        setsList= new ArrayList<>();
        repsList= new ArrayList<>();
        setUplists();

    }


    public void setData(ArrayList<DataItems> list) {
        this.result = list;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.exercise_list_layout, parent, false);
        MyviewHolder viewHolder = new MyviewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final MyviewHolder holder, final int position) {
        current_item = position;

        final DataItems dataItems = result.get(position);
      String[] name=  dataItems.getExerciseName().split(" ");
currentDate=dataItems.getExercise_date();
        holder.exercise_name.setText(name[0]);
        holder.weight.setText("Weight "+dataItems.getExercise_weight());
        holder.sets.setText("Sets "+dataItems.getExercise_sets());
        holder.reps.setText("Reps "+dataItems.getExercise_reps());
        currentId=dataItems.getExercise_uniq_id();
        holder.weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog("Select Weight");
            }
        });
        holder.sets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog("Select Sets");
            }
        });
        holder.reps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WheelDialog("Select Reps");
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


        View outerView = View.inflate(context, R.layout.wheel_view, null);
if (source.equals("Select Weight")){
    WheelView wv1 = (WheelView) outerView.findViewById(R.id.wheel_view_wv1);
    wv1.setOffset(2);
    wv1.setItems(Arrays.asList(feets));
    wv1.setSeletion(5);
    wv1.setVisibility(View.VISIBLE);
    wv1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
        @Override
        public void onSelected(int selectedIndex, String item) {
            Log.d("feet", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
            weight=item;
        }
    });
}

        if (source.equals("Select Sets")){
            WheelView wv2 = (WheelView) outerView.findViewById(R.id.wheel_view_wv2);
            wv2.setOffset(2);
            wv2.setItems(Arrays.asList(inches));
            wv2.setSeletion(4);
            wv2.setVisibility(View.VISIBLE);
            wv2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("inches", "[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);

                    sets=item;

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

                    reps=item;
                }
            });
        }
        new AlertDialog.Builder(context)
                .setTitle(source)
                .setView(outerView)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (source.equals("Select Weight")) {

                            Log.d("val",weight);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(TableData.Tableinfo.WEIGHT, weight);
                            dop.updateExerRow(dop, contentValues, currentId);
                            setData(dop.getExerciseInformation(dop,currentDate));

                        } else if (source.equals("Select Sets")) {
                            Log.d("val",sets);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(TableData.Tableinfo.SETS, sets);
                            dop.updateExerRow(dop, contentValues, currentId);
                            setData(dop.getExerciseInformation(dop,currentDate));
                        }else  if (source.equals("Select Reps")) {
                            Log.d("val",reps);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(TableData.Tableinfo.REPS, reps);
                            dop.updateExerRow(dop, contentValues, currentId);
                            setData(dop.getExerciseInformation(dop,currentDate));
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


    static class MyviewHolder extends AnimateViewHolder {
        TextView weight,sets,reps;
        TextView exercise_name;
        ImageView del;


        public MyviewHolder(View rowView) {
            super(rowView);
            exercise_name = (TextView) rowView.findViewById(R.id.exercise_name);
            weight = (TextView) rowView.findViewById(R.id.weight);
            sets = (TextView) rowView.findViewById(R.id.sets);
            reps = (TextView) rowView.findViewById(R.id.reps);
            del= (ImageView) rowView.findViewById(R.id.del);

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