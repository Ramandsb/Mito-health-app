package in.tagbin.mitohealthapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.ServerStatusRequestObject;
import in.tagbin.mitohealthapp.app.AppController;
import in.tagbin.mitohealthapp.helper.Config;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.LoginModel;

public class LoginFragment1 extends Fragment {

    private static final int MODE_PRIVATE = 1;
    EditText username_ed,password_ed;
    String username_str,password_str;
    SharedPreferences loginDetails;
    TextView forgotPassword;
    int hour,minute,day,month,year;
    boolean signup=false;

    public LoginFragment1(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login,container,false);

        View view1 = view.findViewById(R.id.loginCont);
        //customDialog();
//        signup= getIntent().getBooleanExtra("signup",false);
//        if (signup){
//            Snackbar.make(view,"Sign up Success",Snackbar.LENGTH_LONG).show();
//
//        }

        loginDetails= getActivity().getSharedPreferences(MainActivity.LOGIN_DETAILS,MODE_PRIVATE);
        username_ed= (EditText) view.findViewById(R.id.username);
        password_ed= (EditText) view.findViewById(R.id.login_password);
        forgotPassword= (TextView) view.findViewById(R.id.forgotpassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity_dialog();
            }
        });
        Button login= (Button) view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_str=   username_ed.getText().toString();
                password_str=     password_ed.getText().toString();

                makeJsonObjReq(username_str, password_str,"login");

            }
        });
        return view;

    }

    public void quantity_dialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Reset Password");
        alertDialog.setMessage("Please enter your email to Reset Password");
        final View view = View.inflate(getContext(), R.layout.item_forgot_password, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        lp.setMargins(30, 0, 30, 10);

        final EditText input = (EditText) view.findViewById(R.id.get_forgot);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                     String email= input.getText().toString();

                        makeServerStatusRequestObject(email);

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

    private void makeJsonObjReq(String username, String password, final String source) {
showDialog();
        String localUrl="";
        Map<String, String> postParam = new HashMap<String, String>();
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        Date date  = new Date(year-1900,month,day,hour,minute);
        long timestamp = date.getTime()/1000L;
        if (source.equals("login")) {
            postParam.put("username", username);
            postParam.put("password", password);
            postParam.put("time_delta", String.valueOf(timestamp));
         localUrl=   Config.url+"login/";
        }else if (source.equals("forgot")){
            postParam.put("username",username);
            localUrl="http://api.mitoapp.com/password/forgot/";
        }



        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                localUrl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());

                        if (source.equals("login")) {
                            LoginModel loginModel = JsonUtils.objectify(response.toString(),LoginModel.class);
                            PrefManager pref = new PrefManager(getContext());
                            pref.saveLoginModel(loginModel);
                            SharedPreferences.Editor editor1 = loginDetails.edit();
                            editor1.clear();
                            editor1.commit();
                            SharedPreferences.Editor editor = loginDetails.edit();
                            try {
                                editor.putString("user_id", response.getString("user_id"));
                                editor.putString("key", response.getString("key"));
                                editor.commit();
                                Intent intent = new Intent(getContext(), SetGoalsActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                dismissDialog();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if (source.equals("forgot")){

                            progressBar.setVisibility(View.GONE);

                            messageView.setText("Check Email to change password forgot");

                        }
                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("error", "Error: " + volleyError.getMessage());

                VolleyError error = null;
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                    error = new VolleyError(new String(volleyError.networkResponse.data));
                  Log.d("parsed volley error", error.getMessage());
                    try {
                        JSONObject object= new JSONObject(error.getMessage());
                        String finalerror=   object.getString("message");
                        Log.d("final error", finalerror);
                        progressBar.setVisibility(View.GONE);
                        messageView.setText(finalerror);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Log.d("else error", volleyError.toString());
                }

                displayErrors(volleyError);
//                progressBar.setVisibility(View.GONE);
//
//                messageView.setText(error.toString());

//                displayErrors(error);





            }
        }) {

//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put( "charset", "utf-8");
//                headers.put("Authorization","Authkey");
//                return headers;
//            }
//


        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void makeServerStatusRequestObject(String username){
        showDialog();
        Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("username",username);
         String   localUrl="http://api.mitoapp.com/password/forgot/";

        JSONObject o= new JSONObject(postParam);
        Log.d("postparam",o.toString());
        ServerStatusRequestObject requestObject = new ServerStatusRequestObject(Request.Method.POST, localUrl, null, o.toString(), new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.d("response",o.toString());
                showDialog();
                progressBar.setVisibility(View.GONE);

                messageView.setText("Visit Email to change your Password");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {


                showDialog();
                displayErrors(volleyError);
                Log.d("response",volleyError.toString());



            }
        }){

            @Override
            protected Response parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
                Log.d("status Code",statusCode+"/////");
                return super.parseNetworkResponse(response);
            }

            @Override
            protected void deliverResponse(Integer statusCode) {
                Log.d("deliverResponse Code",statusCode+"/////");
                showDialog();
                progressBar.setVisibility(View.GONE);

                messageView.setText(statusCode+"/////");
                super.deliverResponse(statusCode);
            }
        };

        AppController.getInstance().addToRequestQueue(requestObject);

    }


    public void customDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    TextView messageView;
    ProgressBar progressBar;
    AlertDialog alert;
    public void dismissDialog() {
        alert.dismiss();
    }

    public void displayErrors(VolleyError error) {
        showDialog();
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Connection failed");
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("User not found!!");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Sorry, Something went wrong!");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }



}
