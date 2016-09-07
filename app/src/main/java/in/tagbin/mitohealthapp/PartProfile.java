package in.tagbin.mitohealthapp;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.ProfileImage.PicModeSelectDialogFragment;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.helper.UrlResolver;
import in.tagbin.mitohealthapp.model.ConnectProfileModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.FacebookModel;
import in.tagbin.mitohealthapp.model.FileUploadModel;
import in.tagbin.mitohealthapp.model.ImageUploadResponseModel;
import in.tagbin.mitohealthapp.model.InterestModel;
import in.tagbin.mitohealthapp.model.SetConnectProfileModel;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PartProfile extends Fragment implements View.OnClickListener {
    ImageView img1,img2,img3,img4,img5,img6,img7;
    EditText etLocation,etGender,etOccupation,etHomeTwon;
    TextView name;
    ConnectProfileModel connectProfileModel;
    LoginButton facebookConnect;
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    String fbAuthToken,fbUserID;
    PrefManager pref;
    boolean flag1 = false,flag2 = false,flag3 = false,flag4 = false,flag5 = false,flag6 = false,flag7 = false,flag = false;
    GifImageView progressBar,progressBar1,progressBar2,progressBar3,progressBar4,progressBar5,progressBar6,progressBar7;
    int SELECT_PICTURE1 =0,SELECT_PICTURE2 =1,SELECT_PICTURE3 =2,SELECT_PICTURE4 =3,SELECT_PICTURE5 =4,SELECT_PICTURE6 =5,SELECT_PICTURE7 =6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_part_profile, container, false);
        pref = new PrefManager(getActivity());
        img1 = (ImageView) layout.findViewById(R.id.userPic1);
        img2 = (ImageView) layout.findViewById(R.id.userPic2);
        img3 = (ImageView) layout.findViewById(R.id.userPic3);
        img4 = (ImageView) layout.findViewById(R.id.userPic4);
        img5 = (ImageView) layout.findViewById(R.id.userPic5);
        img6 = (ImageView) layout.findViewById(R.id.userPic6);
        img7 = (ImageView) layout.findViewById(R.id.userPic7);
        etHomeTwon = (EditText) layout.findViewById(R.id.etPartnerHomeTown);
        progressBar =(GifImageView) layout.findViewById(R.id.progressBar);
        progressBar1 =(GifImageView) layout.findViewById(R.id.progressBar1);
        progressBar2 =(GifImageView) layout.findViewById(R.id.progressBar2);
        progressBar3 =(GifImageView) layout.findViewById(R.id.progressBar3);
        progressBar4 =(GifImageView) layout.findViewById(R.id.progressBar4);
        progressBar5 =(GifImageView) layout.findViewById(R.id.progressBar5);
        progressBar6 =(GifImageView) layout.findViewById(R.id.progressBar6);
        progressBar7 =(GifImageView) layout.findViewById(R.id.progressBar7);
        facebookConnect = (LoginButton) layout.findViewById(R.id.facebook_people_connect);
        facebookConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook,0,0,0);
        facebookConnect.setText("Pick from my Facebook profile");
//        facebookConnect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.facebook, 0, 0);
        facebookConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(PartProfile.this, Arrays.asList("user_about_me", "user_location","user_hometown","user_likes","user_work_history"));

                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        if (AccessToken.getCurrentAccessToken() != null) {
                            Log.d("extended", loginResult.getAccessToken().getToken());
                            progressBar.setVisibility(View.VISIBLE);
                            Controller.getExtendedFacebook(getContext(),loginResult.getAccessToken().getToken(),mExtendedFacebookListener);
// JsonObjReq(loginResult.getAccessToken().getToken());
                        }

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.v("LoginActivity", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });
            }
        });
        //facebookConnect.setReadPermissions(Arrays.asList("user_photos ", "user_about_me", "user_location"));
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        img7.setOnClickListener(this);
        etLocation = (EditText) layout.findViewById(R.id.etPartnerLocation);
        etOccupation = (EditText) layout.findViewById(R.id.etPartnerOccupation);
        etGender = (EditText) layout.findViewById(R.id.etPartnerGender);
        name = (TextView) layout.findViewById(R.id.tvPartnerName);
        progressBar.setVisibility(View.VISIBLE);
        Controller.getConnectProfile(getContext(),mConnectListener);
        return layout;
    }
    RequestListener mConnectListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("connect profile ",responseObject.toString());
            connectProfileModel = JsonUtils.objectify(responseObject.toString(),ConnectProfileModel.class);
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProfileConnect(connectProfileModel);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    public void setProfileConnect(ConnectProfileModel data){
        if (data.getImages() != null) {
            if (data.getImages().getMaster()!= null){
                ImageLoader.getInstance().loadImage(data.getImages().getMaster(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar1.setVisibility(View.GONE);
                        img1.setImageBitmap(loadedImage);
                    }
                });
            }
            if (data.getImages().getOthers() != null ) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[0], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar2.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar2.setVisibility(View.GONE);
                            img2.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null ) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[1], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar3.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar3.setVisibility(View.GONE);
                            img3.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[2], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar4.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar4.setVisibility(View.GONE);
                            img4.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null ) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[3], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar5.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar5.setVisibility(View.GONE);
                            img5.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[4], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar6.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar6.setVisibility(View.GONE);
                            img6.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
                try {
                    ImageLoader.getInstance().loadImage(data.getImages().getOthers()[5], new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar7.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar7.setVisibility(View.GONE);
                            img7.setImageBitmap(loadedImage);
                        }
                    });
                }catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getUser() != null){
                if (data.getUser().getFirst_name() != null){
                    if (data.getUser().getLast_name() != null){
                        if (data.getAge() > 0 )
                            name.setText(data.getUser().getFirst_name()+" "+data.getUser().getLast_name()+", "+data.getAge());
                        else
                            name.setText(data.getUser().getFirst_name()+" "+data.getUser().getLast_name());
                    }else {
                        if (data.getAge() > 0)
                            name.setText(data.getUser().getFirst_name()+", "+data.getAge());
                        else
                            name.setText(data.getUser().getFirst_name());
                    }
                }
                if (data.getOccupation() != null){
                    etOccupation.setText(data.getOccupation());
                }
                if (data.getDescription() != null){
                    etGender.setText(data.getDescription());
                }
                if (data.getHome_town() != null){
                    etHomeTwon.setText(data.getHome_town());
                }
                if (pref.getCurrentLocationAsObject() != null){
                    if (pref.getCurrentLocationAsObject().getLatitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0){
                        etLocation.setText(MyUtils.getCityNameFromLatLng(getContext(),pref.getCurrentLocationAsObject().getLatitude(),pref.getCurrentLocationAsObject().getLongitude()));
                    }
                }
            }
        }

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereProfile");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
        //InitActivity i = (InitActivity) getActivity();
        //i.getActionBar().setTitle("Profile");
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(true);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            SetConnectProfileModel setConnectProfileModel = new SetConnectProfileModel();
            setConnectProfileModel.setDescription(etGender.getText().toString());
            setConnectProfileModel.setOccupation(etOccupation.getText().toString());
            setConnectProfileModel.setHometown(etHomeTwon.getText().toString());
            PrefManager pref = new PrefManager(getActivity());
            if (pref.getCurrentLocationAsObject() != null){
                double[] location = {pref.getCurrentLocationAsObject().getLongitude(),pref.getCurrentLocationAsObject().getLatitude()};
                setConnectProfileModel.setLocation(location);
            }
            SetConnectProfileModel.Images1Model images1Model = setConnectProfileModel.getImages();
            if (flag1) {
                if (pref.getKeyMasterImage() != null || pref.getKeyMasterImage().isEmpty()) {
                    images1Model.setMaster(pref.getKeyMasterImage());
                    setConnectProfileModel.setImages(images1Model);
                }
            }
            if (flag) {
                ArrayList<String> other = new ArrayList<String>();
                if (flag2) {
                    if (pref.getKeyUserPic1() != null) {
                        other.add(pref.getKeyUserPic1());
                    }
                }
                if (flag3) {
                    if (pref.getKeyUserPic2() != null) {
                        other.add(pref.getKeyUserPic2());
                    }
                }
                if (flag4) {
                    if (pref.getKeyUserPic3() != null) {
                        other.add(pref.getKeyUserPic3());
                    }
                }
                if (flag5) {
                    if (pref.getKeyUserPic4() != null) {
                        other.add(pref.getKeyUserPic4());
                    }
                }
                if (flag6) {
                    if (pref.getKeyUserPic5() != null) {
                        other.add(pref.getKeyUserPic5());
                    }
                }
                if (flag7) {
                    if (pref.getKeyUserPic6() != null) {
                        other.add(pref.getKeyUserPic6());
                    }
                }
                images1Model.setOther(other);
                setConnectProfileModel.setImages(images1Model);
            }
            if (etGender.getText().toString().equals("") || etGender.getText().toString().isEmpty()){
                Toast.makeText(getContext(),"Please enter the description",Toast.LENGTH_LONG).show();
            }else if(etOccupation.getText().toString().equals("") || etOccupation.getText().toString().isEmpty()){
                Toast.makeText(getContext(),"Please enter the occupation",Toast.LENGTH_LONG).show();
            }else if(etHomeTwon.getText().toString().equals("") || etHomeTwon.getText().toString().isEmpty()){
                Toast.makeText(getContext(),"Please enter the home town",Toast.LENGTH_LONG).show();
            }else {
                Log.d("profile", JsonUtils.jsonify(setConnectProfileModel));
                progressBar.setVisibility(View.VISIBLE);
                Controller.setConnectProfile(getContext(), setConnectProfileModel, msetProfileListener);
            }
//            InitActivity.change(2);


            //i.change(2);

//            getFragmentManager().beginTransaction().replace(R.id.fragmentnew,new HomePage()).commit();
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userPic1:
                showAddProfilePicDialog1(SELECT_PICTURE1);
                break;
            case R.id.userPic2:
                showAddProfilePicDialog1(SELECT_PICTURE2);
                break;
            case R.id.userPic3:
                showAddProfilePicDialog1(SELECT_PICTURE3);
                break;
            case R.id.userPic4:
                showAddProfilePicDialog1(SELECT_PICTURE4);
                break;
            case R.id.userPic5:
                showAddProfilePicDialog1(SELECT_PICTURE5);
                break;
            case R.id.userPic6:
                showAddProfilePicDialog1(SELECT_PICTURE6);
                break;
            case R.id.userPic7:
                showAddProfilePicDialog1(SELECT_PICTURE7);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (requestCode == SELECT_PICTURE1 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                flag1 = true;
                progressBar1.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener);
                img1.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == SELECT_PICTURE2 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar2.setVisibility(View.VISIBLE);
                flag2 = true;
                flag = true;
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener1);
                img2.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == SELECT_PICTURE3 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar3.setVisibility(View.VISIBLE);
                flag3 = true;
                flag = true;
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener2);
                img3.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE4 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar4.setVisibility(View.VISIBLE);
                flag4 = true;
                flag = true;
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener3);
                img4.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE5 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                flag5 = true;
                flag = true;
                progressBar5.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener4);
                img5.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE6 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                flag6  =true;
                flag = true;
                progressBar6.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener5);
                img6.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == SELECT_PICTURE7 ) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                flag7 = true;
                flag = true;
                progressBar7.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(),fileUploadModel,mUploadListener6);
                img7.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, result);
        }


    }

    RequestListener msetProfileListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("partner connect",responseObject.toString());
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),"Profile submitted succesfully",Toast.LENGTH_LONG).show();
                }
            });
            if (getArguments() != null && getArguments().getString("profile_connect") != null){
                PrefManager pref = new PrefManager(getContext());
                pref.setTutorial(true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
                Controller.getInterests(getContext(),mInterestListener);
            }else {
                BinderActivity i = (BinderActivity) getActivity();
                i.bottomNavigation.setCurrentItem(2);
            }
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("erro",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    private void showAddProfilePicDialog1(final int select_picture) {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(new PicModeSelectDialogFragment.IPicModeSelectListener() {
            @Override
            public void onPicModeSelected(String mode) {
                String action = mode.equalsIgnoreCase(GOTOConstants.PicModes.CAMERA) ? GOTOConstants.IntentExtras.ACTION_CAMERA : GOTOConstants.IntentExtras.ACTION_GALLERY;
                Intent intent = new Intent(getActivity(), ImageCropActivity.class);
                intent.putExtra("ACTION", action);
                startActivityForResult(intent, select_picture);
            }
        });
        dialogFragment.show(getActivity().getFragmentManager(), "picModeSelector");
    }
    private Bitmap showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mImagePath);
            return myBitmap;

        }
        return null;
    }
    RequestListener mInterestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
//            interestModel = JsonUtils.objectify(responseObject.toString(),InterestModel.class);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    setToggleButtons(interestModel);
//                }
//            });
            Log.d("intersts",responseObject.toString());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            Intent i = new Intent(getContext(),InterestActivity.class);
            i.putExtra("response",responseObject.toString());
            startActivity(i);
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("intersts",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUploadListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyMasterImage(imageUploadResponseModel.getUrl());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar1.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar1.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUploadListener1 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyUserPic1(imageUploadResponseModel.getUrl());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar2.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUploadListener2 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyUserPic2(imageUploadResponseModel.getUrl());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar3.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar3.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar3.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUploadListener3 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyUserPic3(imageUploadResponseModel.getUrl());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar4.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar4.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar4.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUploadListener4 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyUserPic4(imageUploadResponseModel.getUrl());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar5.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar5.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar5.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUploadListener5 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyUserPic5(imageUploadResponseModel.getUrl());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar6.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar6.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar6.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };
    RequestListener mUploadListener6 = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file",responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(),ImageUploadResponseModel.class);
            pref.setKeyUserPic6(imageUploadResponseModel.getUrl());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar7.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar7.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar7.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    };
    RequestListener mExtendedFacebookListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("facebook ",responseObject.toString());
            final FacebookModel facebookModel = JsonUtils.objectify(responseObject.toString(),FacebookModel.class);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    if (facebookModel.getHome_town() != null)
                        etHomeTwon.setText(facebookModel.getHome_town());
                    if (facebookModel.getDescription() != null)
                        etGender.setText(facebookModel.getDescription());
                    if (facebookModel.getOccupation() != null)
                        etOccupation.setText(facebookModel.getOccupation());
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("facebook connect error",message);
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                ((Activity) getContext()).runOnUiThread(new Runnable() {
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
