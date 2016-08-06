package in.tagbin.mitohealthapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText email,pass,conPass;
    Button signup;
    String email_str,pass_str,con_pass_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        email= (EditText) findViewById(R.id.email_id);
                pass= (EditText) findViewById(R.id.pass_id);
        conPass= (EditText) findViewById(R.id.con_id);
        signup= (Button) findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_str=email.getText().toString();
                pass_str=pass.getText().toString();
                con_pass_str=conPass.getText().toString();

                if (pass_str.equals(con_pass_str)){
                    makeJsonObjReq(pass_str,email_str);
                }


            }
        });

    }

    private void makeJsonObjReq(String password,String email) {

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("password", password);
        postParam.put("first_name", "");
        postParam.put("last_name", "");
        postParam.put("email", email);
        postParam.put("phone_number", "");

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
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());


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

}
