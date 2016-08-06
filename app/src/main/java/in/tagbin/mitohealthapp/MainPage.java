package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
//import com.android.volley.Request;

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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
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


public class MainPage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,
        GoogleApiClient.ServerAuthCodeCallbacks, LoginFragment.OnFragmentInteractionListener, SignupFragment.OnFragmentInteractionListener {
    SharedPreferences loginDetails;
    public static String LOGIN_DETAILS = "login_details";
    private static final String TAG = "android-plus-quickstart";
    SharedPreferences signupDetailsSP;
    public static final String SIGNUPDETAILS = "signupDetails";
    String acountname;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;
    TextView messageView;
    ProgressBar progressBar;
    AlertDialog alert;


    private static final String SAVED_PROGRESS = "sign_in_progress";
    private static final String WEB_CLIENT_ID = "582683963208-k299a53osu89j6jfgk7a57iq9ntdl7cg.apps.googleusercontent.com";
    private static final String SERVER_BASE_URL = "urn:ietf:wg:oauth:2.0:oob";
    private static final String EXCHANGE_TOKEN_URL = SERVER_BASE_URL + "/exchangetoken";
    private static final String SELECT_SCOPES_URL = SERVER_BASE_URL + "/selectscopes";
    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;
    private int mSignInError;
    private boolean mRequestServerAuthCode = false;

    String profile_name, profile_picture;
    private boolean mServerHasToken = true;
    private boolean debug = true;

    private Button mSignInButton;

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
        UXCam.startWithKey("075a1785b64ccb2");
        customDialog();
        loginDetails = getSharedPreferences(LOGIN_DETAILS, MODE_PRIVATE);
        Fblogin = (Button) findViewById(R.id.login_button);

        Fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onFblogin();

            }
        });


        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }
        mGoogleApiClient = buildGoogleApiClient();


        callbackManager = CallbackManager.Factory.create();


        try {
            getInit();
        } catch (IOException e) {
            e.printStackTrace();
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
//                             JsonObjReq(loginResult.getAccessToken().getToken());

                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

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
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lytblue);
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
                        Intent intent = new Intent(MainPage.this, ProfilePage.class);
                        intent.putExtra("name", profile_name);
                        intent.putExtra("picture", profile_picture);
                        startActivity(intent);
                        finish();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN);

        if (mRequestServerAuthCode) {
            checkServerAuthConfiguration();
            builder = builder.requestServerAuthCode(WEB_CLIENT_ID, this);
        }

        return builder.build();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onStop() {
        super.onStop();
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
        AppIndex.AppIndexApi.end(client2, viewAction);

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        video_player_view.pause();
        video_player_view.hideControls();
        if (mGoogleApiClient.isConnected()) {
            signOutFromGplus();
        }

    }

    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            mSignInButton.setEnabled(true);
        }
    }

    /**
     * Revoking access from google
     */
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                        }

                    });
        }
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
            switch (v.getId()) {
                case R.id.sign_in_button:
//                    mStatus.setText(R.string.status_signing_in);
                    showDialog();
                    mSignInProgress = STATE_SIGN_IN;
                    mGoogleApiClient.connect();
                    break;
            }
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");

        // Update the user interface to reflect that the user is signed in.
        mSignInButton.setEnabled(false);


        // Retrieve some profile information to personalize our app for the user.
        Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        acountname = currentUser.getDisplayName();
        Log.i("Session id", mGoogleApiClient.getSessionId() + "");
        profile_name = currentUser.getDisplayName().toString();
        String dob = currentUser.getBirthday();
        profile_picture = currentUser.getImage().getUrl();


        Log.d("googlename", profile_name + "\n" + dob + "\n" + profile_picture);
        Log.d("Acces token", getAccessToken());

        Intent intent = new Intent(MainPage.this, ProfilePage.class);
        intent.putExtra("name", profile_name);
        intent.putExtra("picture", profile_picture);
        startActivity(intent);


        if (debug) {
//            Toast.makeText(SocialmediaActivity.this, "data got  " + currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
        }


//        Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
//                .setResultCallback(this);

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;
    }

    /* onConnectionFailed is called when our Activity could not connect to Google
     * Play services.  onConnectionFailed indicates that the user needs to select
     * an account, grant permissions or resolve an error in order to sign in.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getResolution());

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // An API requested for GoogleApiClient is not available. The device's current
            // configuration might not be supported with the requested API or a required component
            // may not be installed, such as the Android Wear application. You may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
            Log.w(TAG, "API Unavailable.");
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
                resolveSignInError();
            }
        }

        // In this sample we consider the user signed out whenever they do not have
        // a connection to Google Play services.
//        onSignedOut();
    }

    /* Starts an appropriate intent or dialog for user interaction to resolve
     * the current error preventing the user from being signed in.  This could
     * be a dialog allowing the user to select an account, an activity allowing
     * the user to consent to the permissions being requested by your app, a
     * setting to enable device networking, etc.
     */
    private void resolveSignInError() {
        if (mSignInIntent != null) {

            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());

                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {

            createErrorDialog().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {


        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {

                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {

                    mGoogleApiClient.connect();
                }
                break;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }


    private String getAccessToken() {
        final String[] accessToken = {""};
//        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
//
//            @Override
//            protected String doInBackground(Void... params) {
//
//
//                try {
//                    URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo");
//                    // get Access Token with Scopes.PLUS_PROFILE
//                    String sAccessToken;
//                    sAccessToken = GoogleAuthUtil.getToken(
//                            MainPage.this,
//                            Plus.AccountApi + "",
//                            "oauth2:"
//                                    + Plus.SCOPE_PLUS_PROFILE+ " "
//                                    + "https://www.googleapis.com/auth/plus.login" + " "
//                                    + "https://www.googleapis.com/auth/plus.profile.emails.read");
//                    accessToken[0] =sAccessToken;
//                } catch (UserRecoverableAuthException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    Intent recover = e.getIntent();
//                    startActivityForResult(recover, 125);
//                    return "";
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (GoogleAuthException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//return accessToken[0];
//            }
//
//            @Override
//            protected void onPostExecute(String info) {
//                // Store or use the user's email address
//            }
//
//        };

//        task.execute();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("grant_type", "authorization_code")
                .add("client_id", WEB_CLIENT_ID)
                .add("client_secret", "{clientSecret}")
                .add("redirect_uri", "")
                .add("code", "4/4-GMMhmHCXhWEzkobqIHGG_EnNYYsAkukHspeYUk9E8")
                .build();
        final Request request = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v4/token")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(com.squareup.okhttp.Request request, IOException e) {
                Log.e("Log", e.toString());
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final String message = jsonObject.toString(5);
                    Log.i("Response", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });


        return accessToken[0];
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
    }

    private Dialog createErrorDialog() {
        if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    mSignInError,
                    this,
                    RC_SIGN_IN,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.e(TAG, "Google Play services resolution cancelled");
                            mSignInProgress = STATE_DEFAULT;
//                            mStatus.setText(R.string.status_signed_out);
                        }
                    });
        } else {
            return new AlertDialog.Builder(this)
                    .setMessage("play_services_error")
                    .setPositiveButton("close",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e(TAG, "Google Play services error could not be "
                                            + "resolved: " + mSignInError);
                                    mSignInProgress = STATE_DEFAULT;
//                                    mStatus.setText(R.string.status_signed_out);
                                }
                            }).create();
        }
    }

    @Override
    public CheckResult onCheckServerAuthorization(String idToken, Set<Scope> scopeSet) {
        Log.i(TAG, "Checking if server is authorized.");
        Log.i(TAG, "Mocking server has refresh token: " + String.valueOf(mServerHasToken));

        if (!mServerHasToken) {
            // Server does not have a valid refresh token, so request a new
            // auth code which can be exchanged for one.  This will cause the user to see the
            // consent dialog and be prompted to grant offline access. This callback occurs on a
            // background thread so it is OK to do synchronous network access.

            // Ask the server which scopes it would like to have for offline access.  This
            // can be distinct from the scopes granted to the client.  By getting these values
            // from the server, you can change your server's permissions without needing to
            // recompile the client application.

            HttpClient httpClient = new DefaultHttpClient();
//            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(SELECT_SCOPES_URL);
//            HttpGet httpGet = new HttpGet(SELECT_SCOPES_URL);
            HashSet<Scope> serverScopeSet = new HashSet<Scope>();

            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                int responseCode = httpResponse.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(httpResponse.getEntity());

                if (responseCode == 200) {
                    String[] scopeStrings = responseBody.split(" ");
                    for (String scope : scopeStrings) {
                        Log.i("googletokeswillbelistd", "Server Scope: " + scope);
                        serverScopeSet.add(new Scope(scope));
                    }
                } else {
                    Log.e(TAG, "Error in getting server scopes: " + responseCode);
                }

            } catch (ClientProtocolException e) {
                Log.e(TAG, "Error in getting server scopes.", e);
            } catch (IOException e) {
                Log.e(TAG, "Error in getting server scopes.", e);
            }

            // This tells GoogleApiClient that the server needs a new serverAuthCode with
            // access to the scopes in serverScopeSet.  Note that we are not asking the server
            // if it already has such a token because this is a sample application.  In reality,
            // you should only do this on the first user sign-in or if the server loses or deletes
            // the refresh token.
            return CheckResult.newAuthRequiredResult(serverScopeSet);
        } else {
            // Server already has a valid refresh token with the correct scopes, no need to
            // ask the user for offline access again.
            return CheckResult.newAuthNotRequiredResult();
        }
    }

    @Override
    public boolean onUploadServerAuthCode(String idToken, String serverAuthCode) {
        // Upload the serverAuthCode to the server, which will attempt to exchange it for
        // a refresh token.  This callback occurs on a background thread, so it is OK
        // to perform synchronous network access.  Returning 'false' will fail the
        // GoogleApiClient.connect() call so if you would like the client to ignore
        // server failures, always return true.
        Log.i("onUploadServerAuthCode", "Resp: ");

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(EXCHANGE_TOKEN_URL);

        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("serverAuthCode", serverAuthCode));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            Log.d("serverAuthCode", serverAuthCode);
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            final String responseBody = EntityUtils.toString(response.getEntity());
            Log.i("Uploadvaleauthtoken", "Code: " + statusCode);
            Log.i("Uploadvaleauthtoken", "Resp: " + responseBody);

            // Show Toast on UI Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainPage.this, responseBody, Toast.LENGTH_LONG).show();
                }
            });
            return (statusCode == 200);
        } catch (ClientProtocolException e) {
            Log.e(TAG, "Error in auth code exchange.", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Error in auth code exchange.", e);
            return false;
        }
    }

    private void checkServerAuthConfiguration() {
        // Check that the server URL is configured before allowing this box to
        // be unchecked
        if ("WEB_CLIENT_ID".equals(WEB_CLIENT_ID) ||
                "SERVER_BASE_URL".equals(SERVER_BASE_URL)) {
            Log.w(TAG, "WEB_CLIENT_ID or SERVER_BASE_URL configured incorrectly.");
            Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage("configuration_error")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();

            dialog.show();
        }
    }

//    private void makeJsonObjReq(String s) {
//
//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("access_token", s);
//        postParam.put("source", "facebook");
//        postParam.put("is_nutritionist", "0");
//
//
//        JSONObject jsonObject = new JSONObject(postParam);
//        Log.d("postpar", jsonObject.toString());
//
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                Config.url + "social/signin", jsonObject,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("response", response.toString());
//                        SharedPreferences.Editor editor = loginDetails.edit();
//                        try {
//                            editor.putString("user_id", response.getString("user_id"));
//                            editor.putString("auth_key", response.getString("key"));
//                            editor.commit();
//                            Intent intent = new Intent(MainPage.this, ProfilePage.class);
//                            intent.putExtra("name", profile_name);
//                            intent.putExtra("picture", profile_picture);
//                            startActivity(intent);
//                            finish();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("error", "Error: " + error.getMessage());
//
//
//                Log.d("error", error.toString());
//            }
//        }) {
//
////
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                HashMap<String, String> headers = new HashMap<String, String>();
////                headers.put("Content-Type", "application/json");
////                headers.put( "charset", "utf-8");
////                headers.put("Authorization","Authkey");
////                return headers;
////            }
////
//
//
//        };
//
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("onFragmentInteraction", "" + uri);
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
}
