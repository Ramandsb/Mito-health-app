package in.tagbin.mitohealthapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.AppController;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.Config;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.helper.ServerStatusRequestObject;
import in.tagbin.mitohealthapp.model.LoginModel;
import in.tagbin.mitohealthapp.model.SignUpModel;
import pl.droidsonroids.gif.GifImageView;

public class SignupFragment extends Fragment {

    private static final int MODE_PRIVATE = 1;
    EditText email, pass, conPass, name;
    Button signup;
    String email_str = "", pass_str = "", con_pass_str = "", name_str;
    String first_name = "", last_name = "";
    GifImageView progressBar;
    SharedPreferences loginDetails;
    public static String LOGIN_DETAILS = "login_details";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signup, container, false);
        //customDialog();
        email = (EditText) view.findViewById(R.id.email_id);
        pass = (EditText) view.findViewById(R.id.pass_id);
        //conPass= (EditText) view.findViewById(R.id.con_id);
        name = (EditText) view.findViewById(R.id.name);
        signup = (Button) view.findViewById(R.id.sign_up);
        loginDetails = getActivity().getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);

        progressBar = MainActivity.progressBar;
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_str = name.getText().toString();
                email_str = email.getText().toString();
                pass_str = pass.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                SignUpModel signUpModel = new SignUpModel();
                signUpModel.setEmail(email_str);
                signUpModel.setFirst_name(name_str);
                signUpModel.setPassword(pass_str);
                Controller.signup(getContext(),signUpModel,mSignUpListener);
                //makeServerStatusRequestObject(pass_str, email_str, first_name, last_name);
            }
        });
        return view;
    }


//    public void makeServerStatusRequestObject(String password, String email, String first_name, String last_name) {
//        showDialog();
//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("password", password);
//        postParam.put("first_name", first_name);
//        postParam.put("last_name", last_name);
//        postParam.put("email", email);
//        postParam.put("phone_number", "");
//
//        JSONObject o = new JSONObject(postParam);
//        Log.d("postparam", o.toString());
//        ServerStatusRequestObject requestObject = new ServerStatusRequestObject(Request.Method.POST, Config.url + "users/", null, o.toString(), new Response.Listener() {
//            @Override
//            public void onResponse(Object o) {
//                Log.d("response", o.toString());
//                dismissDialog();
//
////                startActivity(new Intent(SignupFragment.this,LoginFragment1.class).putExtra("signup",true));
////finish();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//
//                VolleyError error = null;
//                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
//                    error = new VolleyError(new String(volleyError.networkResponse.data));
//                    Log.d("parsed volley error", error.getMessage());
//                    try {
//                        JSONObject object = new JSONObject(error.getMessage());
//                        String finalerror = object.getString("message");
//                        Log.d("final error", finalerror);
//                        progressBar.setVisibility(View.GONE);
//                        messageView.setText(finalerror);
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    displayErrors(volleyError);
//                    //Log.d("normal errors", volleyError.getMessage());
//                }
//
//
//            }
//        }) {
//
//            @Override
//            protected Response parseNetworkResponse(NetworkResponse response) {
//                int statusCode = response.statusCode;
//                Log.d("status Code", statusCode + "/////");
//                return super.parseNetworkResponse(response);
//            }
//
//            @Override
//            protected void deliverResponse(Integer statusCode) {
//                Log.d("deliverResponse Code", statusCode + "/////");
//
//                progressBar.setVisibility(View.GONE);
//
//                messageView.setText(statusCode + "/////");
//                super.deliverResponse(statusCode);
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(requestObject);
//
//    }
    RequestListener mSignUpListener = new RequestListener() {
    @Override
    public void onRequestStarted() {

    }

    @Override
    public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
        if (getActivity() != null)
            return;
        LoginModel loginModel = JsonUtils.objectify(responseObject.toString(), LoginModel.class);
        PrefManager pref = new PrefManager(getContext());
        pref.saveLoginModel(loginModel);
        SharedPreferences.Editor editor1 = loginDetails.edit();
        editor1.clear();
        editor1.commit();
        SharedPreferences.Editor editor = loginDetails.edit();
        editor.putString("user_id", loginModel.getUser_id());
        editor.putString("key", loginModel.getKey());
        editor.commit();
        Intent intent = new Intent(getContext(), SetGoalsActivity.class);
        startActivity(intent);
        getActivity().finish();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRequestError(int errorCode, String message) {

    }
};

}
