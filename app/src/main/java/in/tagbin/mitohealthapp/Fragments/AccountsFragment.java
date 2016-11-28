package in.tagbin.mitohealthapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.activity.SplashActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.SetConnectProfileModel;
import in.tagbin.mitohealthapp.model.UserChangePasswordModel;
import in.tagbin.mitohealthapp.model.UserModel;
import in.tagbin.mitohealthapp.model.UserNumberModel;


/**
 * Created by chetan on 21/11/16.
 */

public class AccountsFragment extends Fragment implements View.OnClickListener {
    EditText email,phoneNumber,newPassword,confirmPassowrd;
    TextInputLayout textInputEmail,textInputPhoneNumber,textInputNewPassword,textInputConfirmPassword;
    TextView logout,coins;
    int coinsFinal;
    String user_id;
    SharedPreferences login_details;
    RelativeLayout relativeChangePassword;
    Button changePassword;
    PrefManager pref;
    RelativeLayout relativeOtp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_accounts,container,false);
        email = (EditText) view.findViewById(R.id.username);
        phoneNumber = (EditText) view.findViewById(R.id.etPhoneNumberAccount);
        newPassword = (EditText) view.findViewById(R.id.etNewPassword);
        confirmPassowrd = (EditText) view.findViewById(R.id.etConfirmNewPassword);
        textInputEmail = (TextInputLayout) view.findViewById(R.id.usernameWrapper);
        textInputPhoneNumber = (TextInputLayout) view.findViewById(R.id.phoneNumber);
        textInputNewPassword = (TextInputLayout) view.findViewById(R.id.newPassword);
        textInputConfirmPassword = (TextInputLayout) view.findViewById(R.id.confirmNewPassword);
        relativeChangePassword = (RelativeLayout) view.findViewById(R.id.relativeChangePassword);
        relativeOtp = (RelativeLayout) view.findViewById(R.id.relativeOtp);
        logout = (TextView) view.findViewById(R.id.tvLogout);
        changePassword = (Button) view.findViewById(R.id.buttonSaveAccount);
        login_details = getActivity().getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);
        user_id = login_details.getString("user_id", "");
        changePassword.setOnClickListener(this);
        logout.setOnClickListener(this);
        email.addTextChangedListener(new MyTextWatcher(email));
        phoneNumber.addTextChangedListener(new MyTextWatcher(phoneNumber));
        newPassword.addTextChangedListener(new MyTextWatcher(newPassword));
        confirmPassowrd.addTextChangedListener(new MyTextWatcher(confirmPassowrd));
        pref = new PrefManager(getContext());
        if (pref.getKeyUserDetails() != null){
            email.setText(pref.getKeyUserDetails().getUser().getEmail());
            if (pref.getKeyUserDetails().getProfile().getPhone_number() != null){
                phoneNumber.setText(pref.getKeyUserDetails().getProfile().getPhone_number());
            }
            if (!pref.getKeyUserDetails().getProfile().getSource().toLowerCase().equals("email")){
                relativeChangePassword.setVisibility(View.GONE);
            }else {
                relativeChangePassword.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLogout:
                SharedPreferences loginDetails= getActivity().getSharedPreferences(MainActivity.LOGIN_DETAILS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=loginDetails.edit();
                editor.clear();
                editor.commit();
                PrefManager pref = new PrefManager(getContext());
                pref.clearSession();
                startActivity(new Intent(getContext(),SplashActivity.class));
                getActivity().finish();
                break;
            case R.id.buttonSaveAccount:
                if (!validateConfirmNewPassword())
                    return;
                if (!validateNewPassword())
                    return;
                UserChangePasswordModel changePasswordModel = new UserChangePasswordModel();
                changePasswordModel.setOld_password(newPassword.getText().toString());
                changePasswordModel.setNew_password(confirmPassowrd.getText().toString());
                Controller.setUserPassword(getContext(),changePasswordModel,user_id,mSetUserDetailsListener);
                break;
        }
    }
    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        Log.d("PREPDUG", "hereProfile");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
        //InitActivity i = (InitActivity) getActivity();
        //i.getActionBar().setTitle("Profile");
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        menu.findItem(R.id.action_Settings).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        final Menu m = menu;
//        final MenuItem item = menu.findItem(R.id.action_coin);
//        item.getActionView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                m.performIdentifierAction(item.getItemId(), 0);
//            }
//        });
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Settings) {
            //if (pref.getKeyUserDetails() != null && pref.getKeyUserDetails().getProfile().getHeight() != 0 && pref.getKeyUserDetails().getProfile().getWeight() != 0){
            //toolbar_title.setText("Settings");
            //toolbar.setTitle("");
            //fra = new SettingsActivity();
            Intent i = new Intent(getContext(), SettingsActivity.class);
            startActivity(i);
//            }else {
//                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle);
//                alertDialog1.setTitle("Enter Details");
//                alertDialog1.setMessage("Please enter your height and weight to proceed");
//                alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                alertDialog1.show();
//            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class MyTextWatcher implements TextWatcher {
        private View view;
        private MyTextWatcher(View view){
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.username:
                    validateEmail();
                    break;
                case R.id.etPhoneNumberAccount:
                    validatePhoneNumber();
                    break;
                case R.id.etNewPassword:
                    validateNewPassword();
                    break;
                case R.id.etConfirmNewPassword:
                    validateConfirmNewPassword();
                    break;
            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }
    private boolean validateEmail(){
        if(email.getText().toString().trim().isEmpty() && !isValidEmail(email.getText().toString())){
            textInputEmail.setError("Please enter valid email");
            requestFocus(email);
            return false;
        }else {
            textInputEmail.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validatePhoneNumber(){
        if(phoneNumber.getText().toString().trim().isEmpty() && !isvalidMobileNumber(phoneNumber.getText().toString())){
            textInputPhoneNumber.setError("Please enter valid phone number");
            requestFocus(phoneNumber);
            phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            relativeOtp.setVisibility(View.GONE);
            return false;
        }else {
            if (phoneNumber.getText().toString().length() == 10){
                phoneNumber.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.food_accept,0);
            }
            phoneNumber.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (phoneNumber.getRight() - phoneNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            UserNumberModel userNumberModel = new UserNumberModel();
                            userNumberModel.setPhone_number(phoneNumber.getText().toString());
                            Controller.setUserPhoneNumber(getContext(),userNumberModel,user_id,mPhoneListener);
                            relativeOtp.setVisibility(View.VISIBLE);
                            return true;
                        }
                    }
                    return false;
                }
            });
            textInputPhoneNumber.setErrorEnabled(false);

        }
        return true;
    }
    private boolean validateNewPassword(){
        if(newPassword.getText().toString().trim().isEmpty()){
            textInputNewPassword.setError("Please enter old password");
            requestFocus(newPassword);
            return false;
        }else {
            textInputNewPassword.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateConfirmNewPassword(){
        if(confirmPassowrd.getText().toString().trim().isEmpty()){
            textInputConfirmPassword.setError("Please enter new password");
            requestFocus(confirmPassowrd);
            return false;
        }else{
            textInputConfirmPassword.setErrorEnabled(false);
        }
        return true;
    }
    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private static boolean isvalidMobileNumber(String mobile) {
        String regEx = "[7-9]{1}[0-9]{9}";
        return mobile.matches(regEx);
    }
    RequestListener mSetUserDetailsListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            final UserModel userModel = JsonUtils.objectify(responseObject.toString(), UserModel.class);
            pref.setKeyUserDetails(userModel);
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"Password changed successfully",Toast.LENGTH_LONG).show();
                    newPassword.setText("");
                    confirmPassowrd.setText("");
                    textInputConfirmPassword.setFocusable(false);
                    textInputNewPassword.setFocusable(false);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mPhoneListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            final UserModel userModel = JsonUtils.objectify(responseObject.toString(), UserModel.class);
            pref.setKeyUserDetails(userModel);
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"OTP has been send on your entered number",Toast.LENGTH_LONG).show();
                    phoneNumber.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            final int DRAWABLE_LEFT = 0;
                            final int DRAWABLE_TOP = 1;
                            final int DRAWABLE_RIGHT = 2;
                            final int DRAWABLE_BOTTOM = 3;

                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                if (event.getRawX() >= (phoneNumber.getRight() - phoneNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                                    return false;
                                }
                            }
                            return false;
                        }
                    });
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
}
