package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.StreamHandler;

import in.tagbin.mitohealthapp.Fragments.FoodFrag;

public class LoginActivity extends AppCompatActivity {

    EditText username_ed,password_ed;
    String username_str,password_str;
    SharedPreferences loginDetails;
    TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);
        customDialog();
        loginDetails= getSharedPreferences(MainPage.LOGIN_DETAILS,MODE_PRIVATE);
        username_ed= (EditText) findViewById(R.id.username);
        password_ed= (EditText) findViewById(R.id.login_password);
        forgotPassword= (TextView) findViewById(R.id.forgotpassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quantity_dialog();
            }
        });
        Button login= (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_str=   username_ed.getText().toString();
                password_str=     password_ed.getText().toString();

                makeJsonObjReq(username_str, password_str,"login");

            }
        });

    }
    public void quantity_dialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Reset Password");
        alertDialog.setMessage("Please enter your email to Reset Password");
        final View view = View.inflate(this, R.layout.forgot_dialog, null);
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

        if (source.equals("login")) {
            postParam.put("username", username);
            postParam.put("password", password);
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
                            SharedPreferences.Editor editor1 = loginDetails.edit();
                            editor1.clear();
                            editor1.commit();
                            SharedPreferences.Editor editor = loginDetails.edit();
                            try {
                                editor.putString("user_id", response.getString("user_id"));
                                editor.putString("key", response.getString("key"));
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, BinderActivity.class);
                                intent.putExtra("selection", 1);
                                startActivity(intent);
                                finish();
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
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());

                progressBar.setVisibility(View.GONE);

                messageView.setText("Check Email to change password");

//                displayErrors(error);

                Log.d("error", error.toString());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

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
