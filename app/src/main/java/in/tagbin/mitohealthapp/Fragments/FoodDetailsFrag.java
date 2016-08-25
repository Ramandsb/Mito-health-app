package in.tagbin.mitohealthapp.Fragments;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.AppController;
import in.tagbin.mitohealthapp.Config;
import in.tagbin.mitohealthapp.FoodDetails;
import in.tagbin.mitohealthapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FoodDetailsFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FoodDetailsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDetailsFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "food_id";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
   public static String mParam1;
   public static String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText quantity_ed;
    TextView set_time,set_ampm,set_protien,set_fat,set_carbo,set_energy,set_unit;
    Intent  sendDishData;
      static  String Broadcastsend="senddishdetails";


    public FoodDetailsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodDetailsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodDetailsFrag newInstance(String param1, String param2) {
        FoodDetailsFrag fragment = new FoodDetailsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            customDialog();

        }
        sendDishData= new Intent(Broadcastsend);


    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_food_details, container, false);
        quantity_ed= (EditText) view.findViewById(R.id.quantity_ed);
        set_unit= (TextView) view.findViewById(R.id.measuring_type);
        set_time= (TextView) view.findViewById(R.id.set_time);
        set_ampm= (TextView) view.findViewById(R.id.set_ampm);
        set_protien= (TextView) view.findViewById(R.id.set_protien);
        set_fat= (TextView) view.findViewById(R.id.set_fat);
        set_carbo= (TextView) view.findViewById(R.id.set_carbo);
        set_energy= (TextView) view.findViewById(R.id.set_energy);
        View qvieq=view.findViewById(R.id.qview);

        if (FoodDetails.source.equals("dish_search")){
            qvieq.setVisibility(View.VISIBLE);

        }else if (FoodDetails.source.equals("food_frag")){
            qvieq.setVisibility(View.GONE);
        }

        makeResetJsonObjReq(mParam2);
        Log.d("check param",mParam2);
        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timepick();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void makeResetJsonObjReq(String food_id) {

        showDialog();
//
//http://api.mitoapp.com/v1/food/65/

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Config.url+"food/"+food_id, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());

                        try {
                         JSONObject serving_type  = response.getJSONObject("serving_type");
                            String serving_=serving_type.getString("serving_type");
                            set_unit.setText(serving_);
                         recipe= response.getString("recipe");
                            sendDishData.putExtra("recipe",recipe);
                            getActivity().sendBroadcast(sendDishData);

                            DecimalFormat df = new DecimalFormat("#.##");
                           dishName=response.getString( "name");
                         Double p=   response.getDouble("total_protein");
                            Double f=  response.getDouble("total_fat");
                            Double c=   response.getDouble("total_carbohydrate");
                            Double e=  response.getDouble("total_energy");
                            String protien   =  df.format(p);
                            String fat   =  df.format(f);
                            String carbohydrate   =  df.format(c);
                            String energy   =  df.format(e);
                            set_protien.setText(protien);
                            set_fat.setText(fat);
                            set_carbo.setText(carbohydrate);
                            set_energy.setText(energy);

                            dismissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());

                displayErrors(error);

                Log.d("error", error.toString());
            }
        }) {

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
//                headers.put("Authorization","JWT "+auth_key);
                return headers;
            }



        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    TextView messageView;
    ProgressBar progressBar;
    android.app.AlertDialog alert;


    public void customDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }


    public void showDialog() {

        progressBar.setVisibility(View.VISIBLE);
        alert.show();
        messageView.setText("Loading");
    }

    public void dismissDialog() {
        alert.dismiss();
    }

    public void displayErrors(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Connection failed");
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("AuthFailureError");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ServerError");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }
    private static int hour = 0, min = 0, day = 0;
   public static String dishName = "", time = "", quantity = "",recipe="";
    public void Timepick() {

        quantity = quantity_ed.getText().toString();
        if (quantity.equals("")) {
            Snackbar.make(view, "Set Quantity", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        } else {
            TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            hour = hourOfDay;
                            min = minute;
                            if (hour > 12) {
                                hour = hour - 12;
                                time = hour + ":" + min;
                                set_time.setText(time);
                                set_ampm.setText("pm");
                            } else {
                                time = hour + ":" + min;
                                set_time.setText(time);
                                set_ampm.setText("am");
                            }


                        }
                    }, hour, min, false);
            tpd.show();
        }
    }
}
