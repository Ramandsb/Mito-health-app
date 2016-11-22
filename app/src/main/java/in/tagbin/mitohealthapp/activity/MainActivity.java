package in.tagbin.mitohealthapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
//import com.newrelic.agent.android.NewRelic;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.newrelic.agent.android.NewRelic;

import com.uxcam.UXCam;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import in.tagbin.mitohealthapp.Fragments.LoginFragment1;
import in.tagbin.mitohealthapp.Fragments.SignupFragment;
import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.LoginResponseModel;
import in.tagbin.mitohealthapp.model.SocialModel;
import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    SharedPreferences loginDetails;
    public static String LOGIN_DETAILS = "login_details";
    TextView messageView;
    public static GifImageView progressBar;
    AlertDialog alert;
    private static final String TAG = "IdTokenActivity";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 1;
    private String googleAccessToken,googleUserID;
    final int REQUEST_GETACCOUNTS = 1;
    private TextView mIdTokenTextView;
    CleverTapAPI cleverTap;
    String profile_name, profile_picture;
    Button Fblogin;
    CallbackManager callbackManager;
    final int DIALOG_LOADING = 0;
    String url = "https://graph.facebook.com/me/feed";
    String FbToken = "";
    TabLayout tabLayout;
    ViewPager viewPager;
    Button googleButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        NewRelic.withApplicationToken(

                "AAab8a197df22ed375a8ad6f54fcb1c736ae09e5f2"
        ).start(this.getApplication());
        UXCam.startWithKey("075a1785b64ccb2");
        validateServerClientID();
        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
            cleverTap.enablePersonalization();
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope("https://www.googleapis.com/auth/user.birthday.read"))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestServerAuthCode(getString(R.string.client_id))
                .requestIdToken(getString(R.string.client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
        loginDetails = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);
        progressBar = (GifImageView) findViewById(R.id.progressBar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        googleButton = (Button) findViewById(R.id.sign_in_button);
        googleButton.setOnClickListener(this);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //findViewById(R.id.sign_in_button).setOnClickListener(this);
        Fblogin = (Button) findViewById(R.id.login_button);

        Fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onFblogin();

            }
        });
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void onFblogin() {

        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"/*, "user_birthday"*/));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            Log.d("loginreslut", loginResult.getAccessToken().getToken());
                            FbToken = loginResult.getAccessToken().getToken();

                            RequestData();
                            SocialModel socialModel = new SocialModel();
                            socialModel.setAccess_token(FbToken);
                            socialModel.setSource("facebook");
                            progressBar.setVisibility(View.VISIBLE);
                            Controller.setFacebookLogin(MainActivity.this, socialModel, mFacebookListener);

                        }
                    }

                    @Override
                    public void onCancel() {

                        progressBar.setVisibility(View.GONE);
//                        messageView.setText("Check Internet Connection");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("bhai eror h", error.toString());

                    }
                });

    }

//    public void getInit() throws IOException {
//        video_player_view = (FullscreenVideoLayout) findViewById(R.id.video_view);
//        video_player_view.setActivity(this);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) video_player_view.getLayoutParams();
//        params.width = metrics.widthPixels;
//        params.height = metrics.heightPixels;
//        video_player_view.setLayoutParams(params);
//        video_player_view.hideControls();
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.working);
//        video_player_view.setVideoURI(uri);
//        video_player_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                video_player_view.hideControls();
//            }
//        });
//
//        video_player_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.start();
//            }
//        });
//        video_player_view.start();
//    }


    @Override
    protected void onResume() {
        super.onResume();
//        video_player_view.start();
//        video_player_view.hideControls();

    }

    /**
     * social media below
     */


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_LOADING:
                final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(true);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                return dialog;

            default:
                return null;
        }
    }

    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        Log.d("json", json.toString());
                        String text = "<b>Name :</b> " + json.getString("tv_name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                        String id = json.getString("id");
                        profile_name = json.getString("tv_name");
                        String link = json.getString("link");
                        String email = json.getString("email");
                        JSONObject picture = json.getJSONObject("picture");
                        JSONObject data = picture.getJSONObject("data");
                        profile_picture = data.getString("url");
                        HashMap<String, Object> eventHashmap = new HashMap<String, Object>();
                        eventHashmap.put("type", "Social Signup");
                        eventHashmap.put("source", "facebook");
                        eventHashmap.put("tv_name", json.getString("tv_name"));
                        //eventHashmap.put("first_name",json.getString("first_name"));
                        //eventHashmap.put("last_name",json.getString("last_name"));
                        eventHashmap.put("email", json.getString("email"));
                        //eventHashmap.put("age",json.getString("age_range"));
                        //eventHashmap.put("gender",json.getString("gender"));
                        eventHashmap.put("user_id", json.getString("id"));
                        //eventHashmap.put("picture",json.getJSONObject("picture"));
                        MyUtils.sendEvent(MainActivity.this, "facebook_signup", eventHashmap);
                        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
                        profileUpdate.put("Name", profile_name);
                        profileUpdate.put("Email2", email);
                        profileUpdate.put("Identity", email);
                        cleverTap.profile.push(profileUpdate);
                        Log.d("Details", profile_name + "\n" + link + "\n" + email + "\n" + profile_picture + "\n" + id);
                        Log.d("GraphResponse", response.toString());
//                        Intent intent = new Intent(MainActivity.this, HealthFragment.class);
//                        intent.putExtra("tv_name", profile_name);
//                        intent.putExtra("picture", profile_picture);
//                        startActivity(intent);
//                        finish();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("some graph shit eeror", e.toString());

                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,tv_name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (loginDetails.getString("key", "").equals("")) {


        } else {
            startActivity(new Intent(MainActivity.this, BinderActivity.class).putExtra("selection", 3).putExtra("source", "direct"));
            finish();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainActivity Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://in.tagbin.mitohealthapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        video_player_view.pause();
//        video_player_view.hideControls();


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            Log.d("code result",""+statusCode);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }

    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            googleAccessToken= acct.getServerAuthCode();
            SocialModel socialModel = new SocialModel();
            socialModel.setAccess_token(googleAccessToken);
            socialModel.setSource("plus");
            progressBar.setVisibility(View.VISIBLE);
            Controller.setFacebookLogin(MainActivity.this, socialModel, mFacebookListener);
            Log.d("google token",googleAccessToken);
        } else {
            // Signed out, show unauthenticated UI.
            Log.d("failed","failed");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GETACCOUNTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Storage permission is enabled
                    signIn();

                } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.GET_ACCOUNTS)) {
                    //User has deny from permission dialog
                    final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setTitle("Contacts Permission Denied");
                    alertDialog1.setMessage("This permission enables you to create an account on Mito using Google+ account, or use an existing connection to sign in. Are you sure you want to deny this permission?");
                    alertDialog1.setPositiveButton("I'M SURE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog1.setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_GETACCOUNTS);
                        }
                    });
                    alertDialog1.show();
                } else {
                    // User has deny permission and checked never show permission dialog so you can redirect to Application settings page
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
                    alertDialog1.setMessage("It looks like you have turned off permission required for this feature. It can be enabled under Phone Settings > Apps > Mito > Permissions");
                    alertDialog1.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    alertDialog1.show();
                }
                break;

        }
    }
    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }



    RequestListener mFacebookListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            LoginResponseModel loginModel = JsonUtils.objectify(responseObject.toString(), LoginResponseModel.class);
            PrefManager pref = new PrefManager(MainActivity.this);
            pref.saveLoginModel(loginModel);
            SharedPreferences.Editor editor1 = loginDetails.edit();
            editor1.clear();
            editor1.commit();
            SharedPreferences.Editor editor = loginDetails.edit();
            editor.putString("user_id", loginModel.getUser_id());
            editor.putString("key", loginModel.getKey());
            editor.commit();
            if (loginModel.isSignup()){
                Intent intent = new Intent(MainActivity.this, SignUpDetailActivity.class);
                startActivity(intent);
                finish();
            }else{
                pref.setSignup(true);
                startActivity(new Intent(MainActivity.this,BinderActivity.class).putExtra("selection",0).putExtra("source","direct"));
                finish();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                if (errorCode == 403){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "UnAuthorised!Please enter valid details", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                checkStoragePermissions();
                break;

        }

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void checkStoragePermissions() {
        if (hasStoragePermissionGranted()) {
            // you can do whatever you want
            signIn();
        } else {
            requestStoragePermission();
        }
    }
    public boolean hasStoragePermissionGranted(){
        return  ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestStoragePermission() {
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS},
                    REQUEST_GETACCOUNTS);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoginFragment1(), "LOGIN");
        adapter.addFragment(new SignupFragment(), "SIGNUP");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
