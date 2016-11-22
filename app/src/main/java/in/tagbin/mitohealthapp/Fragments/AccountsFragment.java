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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.MainActivity;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.activity.SplashActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.SetConnectProfileModel;


/**
 * Created by chetan on 21/11/16.
 */

public class AccountsFragment extends Fragment implements View.OnClickListener {
    EditText email,phoneNumber,newPassword,confirmPassowrd;
    TextInputLayout textInputEmail,textInputPhoneNumber,textInputNewPassword,textInputConfirmPassword;
    TextView logout,coins;
    int coinsFinal;
    PrefManager pref;

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
        logout = (TextView) view.findViewById(R.id.tvLogout);
        logout.setOnClickListener(this);
        email.addTextChangedListener(new MyTextWatcher(email));
        phoneNumber.addTextChangedListener(new MyTextWatcher(phoneNumber));
        newPassword.addTextChangedListener(new MyTextWatcher(newPassword));
        confirmPassowrd.addTextChangedListener(new MyTextWatcher(confirmPassowrd));
        pref = new PrefManager(getContext());
        if (pref.getKeyUserDetails() != null){
            email.setText(pref.getKeyUserDetails().getUser().getEmail());

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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
            return false;
        }else {
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
}
