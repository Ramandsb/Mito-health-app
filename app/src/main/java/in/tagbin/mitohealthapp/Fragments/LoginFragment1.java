package in.tagbin.mitohealthapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.BinderActivity;
import in.tagbin.mitohealthapp.activity.ForgotPasswordActivity;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.activity.SetGoalsActivity;
import in.tagbin.mitohealthapp.activity.SignUpDetailActivity;
import in.tagbin.mitohealthapp.activity.SplashActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.ServerStatusRequestObject;
import in.tagbin.mitohealthapp.app.AppController;
import in.tagbin.mitohealthapp.helper.Config;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.LoginModel;
import in.tagbin.mitohealthapp.model.LoginResponseModel;
import pl.droidsonroids.gif.GifImageView;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment1 extends Fragment {

    EditText username_ed, password_ed;
    String username_str, password_str;
    SharedPreferences loginDetails;
    TextView forgotPassword;
    int hour, minute, day, month, year;
    boolean signup = false;
    GifImageView progressBar;

    public LoginFragment1() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);

        View view1 = view.findViewById(R.id.loginCont);

        loginDetails = getActivity().getSharedPreferences(MainActivity.LOGIN_DETAILS, MODE_PRIVATE);
        username_ed = (EditText) view.findViewById(R.id.username);
        password_ed = (EditText) view.findViewById(R.id.login_password);
        forgotPassword = (TextView) view.findViewById(R.id.forgotpassword);
        progressBar = MainActivity.progressBar;
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), ForgotPasswordActivity.class);
                startActivity(i);
            }
        });
        Button login = (Button) view.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_str = username_ed.getText().toString();
                password_str = password_ed.getText().toString();
                LoginModel loginModel = new LoginModel();
                loginModel.setPassword(password_str);
                loginModel.setUsername(username_str);
                progressBar.setVisibility(View.VISIBLE);
                Controller.loginByEmail(getContext(), loginModel, mLoginListener);
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
                        String email = input.getText().toString();

                        //makeServerStatusRequestObject(email);

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


    RequestListener mLoginListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            if (getActivity() == null)
                return;
            LoginResponseModel loginModel = JsonUtils.objectify(responseObject.toString(), LoginResponseModel.class);
            PrefManager pref = new PrefManager(getContext());
            pref.saveLoginModel(loginModel);
            SharedPreferences.Editor editor1 = loginDetails.edit();
            editor1.clear();
            editor1.commit();
            SharedPreferences.Editor editor = loginDetails.edit();
            editor.putString("user_id", loginModel.getUser_id());
            editor.putString("key", loginModel.getKey());
            editor.commit();
            if (loginModel.isSignup()){
                Intent intent = new Intent(getContext(), SignUpDetailActivity.class);
                startActivity(intent);
                getActivity().finish();
            }else{
                pref.setSignup(true);
                startActivity(new Intent(getContext(),BinderActivity.class).putExtra("selection",0).putExtra("source","direct"));
                getActivity().finish();
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "UnAuthorised! Please enter valid details", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

}
