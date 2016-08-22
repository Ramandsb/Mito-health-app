package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
//import com.newrelic.agent.android.NewRelic;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;

import com.squareup.okhttp.RequestBody;
import com.uxcam.UXCam;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import in.tagbin.mitohealthapp.VideoView.FullscreenVideoLayout;


public class MainPage extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    SharedPreferences loginDetails;
    public static String LOGIN_DETAILS = "login_details";
    TextView messageView;
    ProgressBar progressBar;
    AlertDialog alert;
    private static final String TAG = "IdTokenActivity";
    private static final int RC_GET_TOKEN = 9002;

    private GoogleApiClient mGoogleApiClient;
    private TextView mIdTokenTextView;



    String profile_name, profile_picture;


    ///////////////////////////////////
    FullscreenVideoLayout video_player_view;


///////////////////////////////////


    Button Fblogin;
    CallbackManager callbackManager;
    final int DIALOG_LOADING = 0;
    String url = "https://graph.facebook.com/me/feed";
    String FbToken = "";
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
        setContentView(R.layout.activity_landing);
//        NewRelic.withApplicationToken(
//
//                "AAab8a197df22ed375a8ad6f54fcb1c736ae09e5f2"
//        ).start(this.getApplication());
        UXCam.startWithKey("075a1785b64ccb2");
        customDialog();
        validateServerClientID();

        // [START configure_signin]
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        loginDetails = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        Fblogin = (Button) findViewById(R.id.login_button);

        Fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onFblogin();

            }
        });



        try {
            getInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (loginDetails.getString("login",null) != null){
            Intent intent = new Intent(MainPage.this, BinderActivity.class);
            intent.putExtra("name", profile_name);
            intent.putExtra("picture", profile_picture);
            intent.putExtra("selection", 1);
            intent.putExtra("source","indirect");
            startActivity(intent);
            finish();
        }
        TextView loginTv = (TextView) findViewById(R.id.login);
        loginTv.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           startActivity(new Intent(MainPage.this, LoginActivity.class));


                                       }
                                   }
        );
        TextView signup = (TextView) findViewById(R.id.singup);
        signup.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          startActivity(new Intent(MainPage.this, SignupActivity.class));
                                      }
                                  }
        );
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    private void onFblogin() {

        showDialog();
        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            Log.d("loginreslut", loginResult.getAccessToken().getToken());
                            FbToken = loginResult.getAccessToken().getToken();
                            RequestData();

                            makeJsonObjReq(FbToken,"facebook");
// JsonObjReq(loginResult.getAccessToken().getToken());

                        }
                    }

                    @Override
                    public void onCancel() {

                       progressBar.setVisibility(View.GONE);
                        messageView.setText("Check Internet Connection");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("bhai eror h",error.toString());

                    }
                });

    }

    public void getInit() throws IOException {
        video_player_view = (FullscreenVideoLayout) findViewById(R.id.video_view);
        video_player_view.setActivity(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) video_player_view.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        video_player_view.setLayoutParams(params);
        video_player_view.hideControls();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.working);
        video_player_view.setVideoURI(uri);
        video_player_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_player_view.hideControls();
            }
        });

        video_player_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        video_player_view.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        video_player_view.start();
        video_player_view.hideControls();

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
showDialog();
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        String text = "<b>Name :</b> " + json.getString("name") + "<br><br><b>Email :</b> " + json.getString("email") + "<br><br><b>Profile link :</b> " + json.getString("link");
                        String id = json.getString("id");
                        profile_name = json.getString("name");
                        String link = json.getString("link");
                        String email = json.getString("email");
                        JSONObject picture = json.getJSONObject("picture");
                        JSONObject data = picture.getJSONObject("data");
                        profile_picture = data.getString("url");
                        Log.d("Details", profile_name + "\n" + link + "\n" + email + "\n" + profile_picture + "\n" + id);
                        Log.d("GraphResponse", response.toString());
//                        Intent intent = new Intent(MainPage.this, ProfilePage.class);
//                        intent.putExtra("name", profile_name);
//                        intent.putExtra("picture", profile_picture);
//                        startActivity(intent);
//                        finish();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("some graph shit eeror",e.toString());

                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    @Override
    protected void onStart() {
        super.onStart();

       if (loginDetails.getString("key","").equals("")){



        }else {
           startActivity(new Intent(MainPage.this,BinderActivity.class).putExtra("source","direct"));
           finish();
       }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MainPage Page", // TODO: Define a title for the content shown.
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
        video_player_view.pause();
        video_player_view.hideControls();



    }

    private void getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GET_TOKEN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG, "signOut:onResult:" + status);

                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG, "revokeAccess:onResult:" + status);

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == RC_GET_TOKEN) {
            // [START get_id_token]
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "onActivityResult:GET_TOKEN:success:" + result.getStatus().isSuccess());

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String idToken = acct.getIdToken();
                showDialog();
                makeJsonObjReq(idToken,"google");

                // Show signed-in UI.
                Log.d(TAG, "idToken:" + idToken);
//                mIdTokenTextView.setText(getString(R.string.id_token_fmt, idToken));


                // TODO(user): send token to server and validate server-side
            } else {
                Log.d(TAG, "idToken:" + "some erroe");
            }
                // Show signed-out UI.
            }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

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





    private void makeJsonObjReq(String s,String source) {

        showDialog();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("access_token", s);
        postParam.put("source", source);
        postParam.put("is_nutritionist", "0");


        JSONObject jsonObject = new JSONObject(postParam);
        Log.d("postpar", jsonObject.toString());


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Config.url + "social/signin", jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        SharedPreferences.Editor editor1 = loginDetails.edit();
                        editor1.clear();
                        editor1.commit();
                        SharedPreferences.Editor editor = loginDetails.edit();
                        try {
                            editor.putString("user_id", response.getString("user_id"));
                            editor.putString("key", response.getString("key"));
                            editor.commit();
                            Intent intent = new Intent(MainPage.this, BinderActivity.class);
                            intent.putExtra("name", profile_name);
                            intent.putExtra("picture", profile_picture);
                            intent.putExtra("selection", 1);
                            intent.putExtra("source","indirect");
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());


                displayErrors(error);
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
            messageView.setText("ServerError");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                getIdToken();
                break;

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
