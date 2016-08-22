package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.StreamHandler;

import in.tagbin.mitohealthapp.ProfileImage.L;

public class SignupActivity extends AppCompatActivity {

    EditText email,pass,conPass,name;
    Button signup;
    String email_str="",pass_str="",con_pass_str="",name_str;
    String first_name="",last_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        customDialog();
        email= (EditText) findViewById(R.id.email_id);
                pass= (EditText) findViewById(R.id.pass_id);
        conPass= (EditText) findViewById(R.id.con_id);
        name= (EditText) findViewById(R.id.name);
        signup= (Button) findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_str=name.getText().toString();
//              String[] split= name_str.split(" ");
//                first_name=split[0];
//                last_name=split[1];
                email_str=email.getText().toString();
                pass_str=pass.getText().toString();
                con_pass_str=conPass.getText().toString();
                showDialog();

                if (pass_str.equals(con_pass_str)){
                    makeServerStatusRequestObject(pass_str,email_str,first_name,last_name);
                }


            }
        });

    }

    private void makeJsonObjReq(String password,String email,String first_name, String last_name) {
        showDialog();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("password", password);
        postParam.put("first_name", first_name);
        postParam.put("last_name", last_name);
        postParam.put("email", email);
        postParam.put("phone_number", "");

        /**
         * "password": "nkman",
         "first_name": "Nairitya",
         "last_name": "Khilari",
         "email": "nairitya1@tagbin.in",
         "phone_number": "8778787878"
         */


        /**
         * "password": "nkman",
         "first_name": "Nairitya",
         "last_name": "Khilari",
         "email": "niritya@gmail.com",
         "phone_number": "8778787878"
         */


        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.url+"users/", jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
Toast.makeText(SignupActivity.this,"Sign up Success",Toast.LENGTH_LONG).show();




                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());


                displayErrors(error);

                Log.d("error", error.toString());
            }
        }) {

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                if (response.data == null || response.data.length == 0) {
                    return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    return super.parseNetworkResponse(response);
                }
            }

            //
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put( "charset", "utf-8");
//                headers.put("Authorization","Authkey");
                return headers;
            }
//


        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void makeServerStatusRequestObject(String password,String email,String first_name, String last_name){
        showDialog();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("password", password);
        postParam.put("first_name", first_name);
        postParam.put("last_name", last_name);
        postParam.put("email", email);
        postParam.put("phone_number", "");

        JSONObject o= new JSONObject(postParam);
        Log.d("postparam",o.toString());
        ServerStatusRequestObject requestObject = new ServerStatusRequestObject(Request.Method.POST, Config.url+"users/", null, o.toString(), new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Log.d("response",o.toString());
                showDialog();
                progressBar.setVisibility(View.GONE);

                messageView.setText("Sign up Success");


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

    TextView messageView;
    ProgressBar progressBar;
    AlertDialog alert;
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
            messageView.setText("AuthFailureError");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Username Already Exist");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }
}
