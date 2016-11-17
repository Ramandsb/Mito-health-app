package in.tagbin.mitohealthapp.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.activity.SetGoalsActivity;
import in.tagbin.mitohealthapp.activity.SignUpDetailActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.LoginResponseModel;
import in.tagbin.mitohealthapp.model.SignUpModel;
import pl.droidsonroids.gif.GifImageView;

import static android.content.Context.MODE_PRIVATE;

public class SignupFragment extends Fragment {

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


    RequestListener mSignUpListener = new RequestListener() {
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
        Intent intent = new Intent(getContext(), SignUpDetailActivity.class);
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
        }else{
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
