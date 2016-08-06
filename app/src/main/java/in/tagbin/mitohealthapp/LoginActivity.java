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

public class LoginActivity extends AppCompatActivity {

    EditText username_ed,password_ed;
    String username_str,password_str;
    SharedPreferences loginDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login2);
        loginDetails= getSharedPreferences(MainPage.LOGIN_DETAILS,MODE_PRIVATE);
        username_ed= (EditText) findViewById(R.id.username);
        password_ed= (EditText) findViewById(R.id.login_password);
        Button login= (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_str=   username_ed.getText().toString();
                password_str=     password_ed.getText().toString();

                makeJsonObjReq(username_str, password_str);

            }
        });

    }

    private void makeJsonObjReq(String username,String password) {

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("username", username);
        postParam.put("password", password);




        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.url+"login/", jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());


                        SharedPreferences.Editor editor=loginDetails.edit();
                        editor.clear();

                        try {

                            editor.putString("user_id", response.getString("user_id"));
                            editor.putString("auth_key", response.getString("key"));
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, ProfilePage.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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
