package in.tagbin.mitohealthapp.Fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.helper.ProfileImage.GOTOConstants;
import in.tagbin.mitohealthapp.helper.ProfileImage.ImageCropActivity;
import in.tagbin.mitohealthapp.helper.ProfileImage.PicModeSelectDialogFragment;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.BinderActivity;
import in.tagbin.mitohealthapp.activity.InterestActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ConnectProfileModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.FacebookModel;
import in.tagbin.mitohealthapp.model.FileUploadModel;
import in.tagbin.mitohealthapp.model.ImageUploadResponseModel;
import in.tagbin.mitohealthapp.model.SetConnectProfileModel;
import in.tagbin.mitohealthapp.model.UserInterestModel;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PartnerConnectFragment extends Fragment implements View.OnClickListener {
    ImageView img1, img2, img3, img4, img5, img6, img7, delete, delete1, delete2, delete3, delete4, delete5, delete6,submit;
    EditText etGender, etOccupation, etHomeTwon;
    TextView name,coins,ageSet,distanceSet,interestSet;
    TextInputLayout inputOccupation,inputHomeTown,inputDescription;
    ConnectProfileModel connectProfileModel;
    LoginButton facebookConnect;
    CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    String fbAuthToken, fbUserID;
    PrefManager pref;
    GestureDetector gestureDetector1, gestureDetector2, gestureDetector3, gestureDetector4, gestureDetector5, gestureDetector6, gestureDetector7;
    Intent i;
    RangeBar age;
    DiscreteSeekBar distance;
    int coinsFinal = 0,distanceFinal = 2;
    boolean deleteVisible = false;
    LinearLayout interstsLinear;
    int[] ageFinal = {5,80};
    GifImageView progressBar, progressBar1, progressBar2, progressBar3, progressBar4, progressBar5, progressBar6, progressBar7;

    int SELECT_PICTURE1 = 0, SELECT_PICTURE2 = 1, SELECT_PICTURE3 = 2, SELECT_PICTURE4 = 3, SELECT_PICTURE5 = 4, SELECT_PICTURE6 = 5, SELECT_PICTURE7 = 6;

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
        View layout = inflater.inflate(R.layout.frag_partner_connect, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pref = new PrefManager(getActivity());
        img1 = (ImageView) layout.findViewById(R.id.userPic1);
        img2 = (ImageView) layout.findViewById(R.id.userPic2);
        img3 = (ImageView) layout.findViewById(R.id.userPic3);
        img4 = (ImageView) layout.findViewById(R.id.userPic4);
        img5 = (ImageView) layout.findViewById(R.id.userPic5);
        img6 = (ImageView) layout.findViewById(R.id.userPic6);
        img7 = (ImageView) layout.findViewById(R.id.userPic7);
        delete = (ImageView) layout.findViewById(R.id.deletePhoto);
        delete1 = (ImageView) layout.findViewById(R.id.deletePhoto1);
        delete2 = (ImageView) layout.findViewById(R.id.deletePhoto2);
        delete3 = (ImageView) layout.findViewById(R.id.deletePhoto3);
        delete4 = (ImageView) layout.findViewById(R.id.deletePhoto4);
        delete5 = (ImageView) layout.findViewById(R.id.deletePhoto5);
        delete6 = (ImageView) layout.findViewById(R.id.deletePhoto6);
        //coins = (TextView) layout.findViewById(R.id.tvCoins);
        etHomeTwon = (EditText) layout.findViewById(R.id.etPartnerHomeTown);
        gestureDetector1 = new GestureDetector(getActivity(), new GestureListener1());
        gestureDetector2 = new GestureDetector(getActivity(), new GestureListener2());
        gestureDetector3 = new GestureDetector(getActivity(), new GestureListener3());
        gestureDetector4 = new GestureDetector(getActivity(), new GestureListener4());
        gestureDetector5 = new GestureDetector(getActivity(), new GestureListener5());
        gestureDetector6 = new GestureDetector(getActivity(), new GestureListener6());
        gestureDetector7 = new GestureDetector(getActivity(), new GestureListener7());
        progressBar = (GifImageView) layout.findViewById(R.id.progressBar);
        progressBar1 = (GifImageView) layout.findViewById(R.id.progressBar1);
        progressBar2 = (GifImageView) layout.findViewById(R.id.progressBar2);
        progressBar3 = (GifImageView) layout.findViewById(R.id.progressBar3);
        progressBar4 = (GifImageView) layout.findViewById(R.id.progressBar4);
        progressBar5 = (GifImageView) layout.findViewById(R.id.progressBar5);
        progressBar6 = (GifImageView) layout.findViewById(R.id.progressBar6);
        progressBar7 = (GifImageView) layout.findViewById(R.id.progressBar7);
        submit = (ImageView) layout.findViewById(R.id.ivExploreSubmit);
        submit.setOnClickListener(this);
        inputDescription = (TextInputLayout) layout.findViewById(R.id.tvDescriptionHeading);
        inputOccupation = (TextInputLayout) layout.findViewById(R.id.tvOccupationHeading);
        inputHomeTown = (TextInputLayout) layout.findViewById(R.id.tvHomeTownHeading);
        facebookConnect = (LoginButton) layout.findViewById(R.id.facebook_people_connect);
        distance = (DiscreteSeekBar) layout.findViewById(R.id.rangebarDistance);
        age = (RangeBar) layout.findViewById(R.id.rangebarAge);
        ageSet = (TextView) layout.findViewById(R.id.tvSettingAgeSet);
        distanceSet = (TextView) layout.findViewById(R.id.tvSettingDistanceSet);
        interstsLinear = (LinearLayout) layout.findViewById(R.id.buttonInterests);
        interestSet = (TextView) layout.findViewById(R.id.tvInterests);
        interstsLinear.setOnClickListener(this);
        facebookConnect.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook, 0, 0, 0);
        facebookConnect.setText("Pick from my Facebook profile");
//        facebookConnect.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.facebook, 0, 0);
        facebookConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().logInWithReadPermissions(PartnerConnectFragment.this, Arrays.asList("user_about_me", "user_location", "user_hometown", "user_likes", "user_work_history"));

                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        if (AccessToken.getCurrentAccessToken() != null) {
                            Log.d("extended", loginResult.getAccessToken().getToken());
                            progressBar.setVisibility(View.VISIBLE);
                            Controller.getExtendedFacebook(getContext(), loginResult.getAccessToken().getToken(), mExtendedFacebookListener);
// JsonObjReq(loginResult.getAccessToken().getToken());
                        }

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.v("LoginFragment1", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.v("LoginFragment1", exception.getCause().toString());
                    }
                });
            }
        });
        //facebookConnect.setReadPermissions(Arrays.asList("user_photos ", "user_about_me", "user_location"));
        delete.setOnClickListener(this);
        delete1.setOnClickListener(this);
        delete2.setOnClickListener(this);
        delete3.setOnClickListener(this);
        delete4.setOnClickListener(this);
        delete5.setOnClickListener(this);
        delete6.setOnClickListener(this);
        if (pref.getKeyMasterImage() != null) {
            img1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector1.onTouchEvent(motionEvent);
                }
            });
        } else {
            img1.setOnClickListener(this);
        }
        if (pref.getKeyUserPic1() != null) {
            img2.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector2.onTouchEvent(motionEvent);
                }
            });
        } else {
            img2.setOnClickListener(this);
        }
        if (pref.getKeyUserPic2() != null) {
            img3.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector3.onTouchEvent(motionEvent);
                }
            });
        } else {
            img3.setOnClickListener(this);
        }
        if (pref.getKeyUserPic3() != null) {
            img4.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector4.onTouchEvent(motionEvent);
                }
            });
        } else {
            img4.setOnClickListener(this);
        }
        if (pref.getKeyUserPic4() != null) {
            img5.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector5.onTouchEvent(motionEvent);
                }
            });
        } else {
            img5.setOnClickListener(this);
        }
        if (pref.getKeyUserPic5() != null) {
            img6.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector6.onTouchEvent(motionEvent);
                }
            });
        } else {
            img6.setOnClickListener(this);
        }
        if (pref.getKeyUserPic6() != null) {
            img7.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector7.onTouchEvent(motionEvent);
                }
            });
        } else {
            img7.setOnClickListener(this);
        }
        //etLocation = (EditText) layout.findViewById(R.id.etPartnerLocation);
        etOccupation = (EditText) layout.findViewById(R.id.etPartnerOccupation);
        etGender = (EditText) layout.findViewById(R.id.etPartnerGender);
        name = (TextView) layout.findViewById(R.id.tvPartnerName);
        etOccupation.addTextChangedListener(new MyTextWatcher(etOccupation));
        etHomeTwon.addTextChangedListener(new MyTextWatcher(etHomeTwon));
        etGender.addTextChangedListener(new MyTextWatcher(etGender));
        progressBar.setVisibility(View.VISIBLE);
        Controller.getConnectProfile(getContext(), mConnectListener);
        distance.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                //settingsModel.setMaximum_distance(value);
                distanceFinal = value;
                distanceSet.setText(value+ " kms");
                return value;
            }
        });
        age.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                ageFinal = new int[]{Integer.parseInt(leftPinValue), Integer.parseInt(rightPinValue)};
                //settingsModel.setAge_range(age);
                ageSet.setText(leftPinValue+"-"+rightPinValue+" yrs");
            }
        });
        if (pref.getKeyUserDetails() != null) {
            if (pref.getKeyUserDetails().getProfile().getMaximum_distance() != 0) {
                distance.setProgress(pref.getKeyUserDetails().getProfile().getMaximum_distance());
                distanceSet.setText(pref.getKeyUserDetails().getProfile().getMaximum_distance() + " kms");
            }
            if (pref.getKeyUserDetails().getProfile().getAge_range() != null && pref.getKeyUserDetails().getProfile().getAge_range().length == 2) {
                age.setRangePinsByValue(pref.getKeyUserDetails().getProfile().getAge_range()[0], pref.getKeyUserDetails().getProfile().getAge_range()[1]);
                ageSet.setText(pref.getKeyUserDetails().getProfile().getAge_range()[0] + "-" + pref.getKeyUserDetails().getProfile().getAge_range()[1] + " yrs");
            }
        }
        return layout;
    }

    RequestListener mConnectListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("connect profile ", responseObject.toString());
            connectProfileModel = JsonUtils.objectify(responseObject.toString(), ConnectProfileModel.class);
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setProfileConnect(connectProfileModel);
                    progressBar.setVisibility(View.GONE);
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
                case R.id.etPartnerOccupation:
                    validateOccuption();
                    break;
//                case R.id.etPartnerHomeTown:
//                    validateHomeTown();
//                    break;
                case R.id.etPartnerGender:
                    validateDescription();
                    break;

            }
        }
    }
    private boolean validateDescription(){
        String hometown = etGender.getText().toString().trim();
        if (hometown.isEmpty()){
            inputDescription.setError("Please enter something about yourself");
            requestFocus(etGender);
            return false;
        }else {
            inputDescription.setErrorEnabled(false);
        }
        return true;
    }
//    private boolean validateHomeTown(){
//        String hometown = etHomeTwon.getText().toString().trim();
//        if (hometown.isEmpty()){
//            inputHomeTown.setError("Please enter home town");
//            requestFocus(etHomeTwon);
//            return false;
//        }else {
//            inputHomeTown.setErrorEnabled(false);
//        }
//        return true;
//    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateOccuption(){
        String hometown = etOccupation.getText().toString().trim();
        if (hometown.isEmpty()){
            inputOccupation.setError("Please enter occupation");
            requestFocus(etOccupation);
            return false;
        }else {
            inputOccupation.setErrorEnabled(false);
        }
        return true;
    }
    public void setProfileConnect(final ConnectProfileModel data) {
        if (data.getImages() != null) {
            if (data.getImages().getMaster() != null) {
                if (!data.getImages().getMaster().isEmpty()) {
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
                            pref.setKeyMasterImage(data.getImages().getMaster());
                            img1.setImageBitmap(loadedImage);
                        }
                    });
                }
            }
            if (data.getImages().getOthers() != null) {

                try {
                    if (!data.getImages().getOthers()[0].isEmpty()) {
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
                                pref.setKeyUserPic1(data.getImages().getOthers()[0]);
                                img2.setImageBitmap(loadedImage);
                            }
                        });
                    }
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }

            }
            if (data.getImages().getOthers() != null) {

                try {
                    if (!data.getImages().getOthers()[1].isEmpty()) {
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
                                pref.setKeyUserPic2(data.getImages().getOthers()[1]);
                                img3.setImageBitmap(loadedImage);
                            }
                        });
                    }
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }

            }
            if (data.getImages().getOthers() != null) {

                try {
                    if (!data.getImages().getOthers()[2].isEmpty()) {
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
                                pref.setKeyUserPic3(data.getImages().getOthers()[2]);
                                img4.setImageBitmap(loadedImage);
                            }
                        });
                    }
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }

            }
            if (data.getImages().getOthers() != null) {

                try {
                    if (!data.getImages().getOthers()[3].isEmpty()) {
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
                                pref.setKeyUserPic4(data.getImages().getOthers()[3]);
                                img5.setImageBitmap(loadedImage);
                            }
                        });
                    }
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }

            }
            if (data.getImages().getOthers() != null) {

                try {
                    if (!data.getImages().getOthers()[4].isEmpty()) {
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
                                pref.setKeyUserPic5(data.getImages().getOthers()[4]);
                                img6.setImageBitmap(loadedImage);
                            }
                        });
                    }
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }

            }
            if (data.getImages().getOthers() != null) {

                try {
                    if (!data.getImages().getOthers()[5].isEmpty()) {
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
                                pref.setKeyUserPic6(data.getImages().getOthers()[5]);
                                img7.setImageBitmap(loadedImage);
                            }
                        });
                    }
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }

            }
        }
        if (data.getUser() != null) {
            if (data.getUser().getFirst_name() != null) {
                if (data.getUser().getLast_name() != null) {
                    if (data.getAge() > 0)
                        name.setText(data.getUser().getFirst_name() + " " + data.getUser().getLast_name() + ", " + data.getAge());
                    else
                        name.setText(data.getUser().getFirst_name() + " " + data.getUser().getLast_name());
                } else {
                    if (data.getAge() > 0)
                        name.setText(data.getUser().getFirst_name() + ", " + data.getAge());
                    else
                        name.setText(data.getUser().getFirst_name());
                }
            }
            if (data.getOccupation() != null) {
                etOccupation.setText(data.getOccupation());
            }
            if (data.getDescription() != null) {
                etGender.setText(data.getDescription());
            }
            if (data.getHome_town() != null) {
                etHomeTwon.setText(data.getHome_town());
            }
//            if (pref.getCurrentLocationAsObject() != null) {
//                if (pref.getCurrentLocationAsObject().getLatitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0) {
//                    etLocation.setText(MyUtils.getCityNameFromLatLng(getContext(), pref.getCurrentLocationAsObject().getLatitude(), pref.getCurrentLocationAsObject().getLongitude()));
//                }
//            }
            if (data.getInterests().size() >0){
                if (data.getInterests().size() > 1){
                    interestSet.setText(data.getInterests().get(0).getInterest().getName()+"+ "+(data.getInterests().size()-1));
                }else if (data.getInterests().size() == 1){
                    interestSet.setText(data.getInterests().get(0).getInterest().getName());
                }
            }
            coinsFinal = data.getTotal_coins();
            pref.setKeyCoins(data.getTotal_coins());
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
//        if (id == R.id.action_next) {
//            SetConnectProfileModel setConnectProfileModel = new SetConnectProfileModel();
//            setConnectProfileModel.setDescription(etGender.getText().toString());
//            setConnectProfileModel.setOccupation(etOccupation.getText().toString());
//            setConnectProfileModel.setHometown(etHomeTwon.getText().toString());
//            PrefManager pref = new PrefManager(getActivity());
//            if (pref.getCurrentLocationAsObject() != null) {
//                double[] location = {pref.getCurrentLocationAsObject().getLongitude(), pref.getCurrentLocationAsObject().getLatitude()};
//                setConnectProfileModel.setLocation(location);
//            }
//            SetConnectProfileModel.Images1Model images1Model = setConnectProfileModel.getImages();
//
//                if (pref.getKeyMasterImage() != null || pref.getKeyMasterImage().isEmpty()) {
//                    images1Model.setMaster(pref.getKeyMasterImage());
//                    setConnectProfileModel.setImages(images1Model);
//                }
//
//
//                ArrayList<String> other = new ArrayList<String>();
//                    if (pref.getKeyUserPic1() != null) {
//                        other.add(pref.getKeyUserPic1());
//                    }
//                    if (pref.getKeyUserPic2() != null) {
//                        other.add(pref.getKeyUserPic2());
//                    }
//                    if (pref.getKeyUserPic3() != null) {
//                        other.add(pref.getKeyUserPic3());
//                    }
//                    if (pref.getKeyUserPic4() != null) {
//                        other.add(pref.getKeyUserPic4());
//                    }
//                    if (pref.getKeyUserPic5() != null) {
//                        other.add(pref.getKeyUserPic5());
//                    }
//
//
//                    if (pref.getKeyUserPic6() != null) {
//                        other.add(pref.getKeyUserPic6());
//                    }
//
//                images1Model.setOther(other);
//                setConnectProfileModel.setImages(images1Model);
//                if (!validateOccuption()) {
//                    return false;
//                }
//                if (!validateHomeTown()){
//                    return false;
//                }
//                if (!validateDescription()){
//                    return false;
//                }
//                Log.d("profile", JsonUtils.jsonify(setConnectProfileModel));
//                progressBar.setVisibility(View.VISIBLE);
//                Controller.setConnectProfile(getContext(), setConnectProfileModel, msetProfileListener);
//
////            InitActivity.change(2);
//
//
//            //i.change(2);
//
////            getFragmentManager().beginTransaction().replace(R.id.fragmentnew,new MitoHealthFragment()).commit();
//            //finish();
//            return true;
//        }else
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.deletePhoto:
                pref.setKeyMasterImage("");
                delete.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.userpic);
                break;
            case R.id.deletePhoto1:
                pref.setKeyUserPic1("");
                delete1.setVisibility(View.GONE);
                img2.setImageResource(R.drawable.userpic1);
                break;
            case R.id.deletePhoto2:
                pref.setKeyUserPic2("");
                delete2.setVisibility(View.GONE);
                img3.setImageResource(R.drawable.userpic1);
                break;
            case R.id.deletePhoto3:
                pref.setKeyUserPic3("");
                delete3.setVisibility(View.GONE);
                img4.setImageResource(R.drawable.userpic1);
                break;
            case R.id.deletePhoto4:
                pref.setKeyUserPic4("");
                delete4.setVisibility(View.GONE);
                img5.setImageResource(R.drawable.userpic1);
                break;
            case R.id.deletePhoto5:
                pref.setKeyUserPic5("");
                delete5.setVisibility(View.GONE);
                img6.setImageResource(R.drawable.userpic1);
                break;
            case R.id.deletePhoto6:
                pref.setKeyUserPic6("");
                delete6.setVisibility(View.GONE);
                img7.setImageResource(R.drawable.userpic1);
                break;
            case R.id.buttonInterests:
                Controller.getInterests(getContext(), mInterestListener);
                break;
            case R.id.ivExploreSubmit:
                SetConnectProfileModel setConnectProfileModel = new SetConnectProfileModel();
                setConnectProfileModel.setDescription(etGender.getText().toString());
                setConnectProfileModel.setOccupation(etOccupation.getText().toString());
                setConnectProfileModel.setHometown(etHomeTwon.getText().toString());
                PrefManager pref = new PrefManager(getActivity());
                if (pref.getCurrentLocationAsObject() != null) {
                    double[] location = {pref.getCurrentLocationAsObject().getLongitude(), pref.getCurrentLocationAsObject().getLatitude()};
                    setConnectProfileModel.setLocation(location);
                }
                SetConnectProfileModel.Images1Model images1Model = setConnectProfileModel.getImages();
                setConnectProfileModel.setAge_range(ageFinal);
                setConnectProfileModel.setMaximum_distance(distanceFinal);
                if (pref.getKeyMasterImage() != null) {
                    images1Model.setMaster(pref.getKeyMasterImage());
                    setConnectProfileModel.setImages(images1Model);
                }else{
                    Toast.makeText(getContext(),"Please enter top image",Toast.LENGTH_LONG).show();
                    return;
                }


                ArrayList<String> other = new ArrayList<String>();
                if (pref.getKeyUserPic1() != null) {
                    other.add(pref.getKeyUserPic1());
                }
                if (pref.getKeyUserPic2() != null) {
                    other.add(pref.getKeyUserPic2());
                }
                if (pref.getKeyUserPic3() != null) {
                    other.add(pref.getKeyUserPic3());
                }
                if (pref.getKeyUserPic4() != null) {
                    other.add(pref.getKeyUserPic4());
                }
                if (pref.getKeyUserPic5() != null) {
                    other.add(pref.getKeyUserPic5());
                }


                if (pref.getKeyUserPic6() != null) {
                    other.add(pref.getKeyUserPic6());
                }

                images1Model.setOther(other);
                setConnectProfileModel.setImages(images1Model);
                if (!validateOccuption()) {
                    return;
                }
//                if (!validateHomeTown()){
//                    return;
//                }
                if (!validateDescription()){
                    return;
                }
                if (interestSet.getText().toString().equals("Select interests")){
                    Toast.makeText(getContext(),"Please select interests",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("profile", JsonUtils.jsonify(setConnectProfileModel));
                progressBar.setVisibility(View.VISIBLE);
                Controller.setConnectProfile(getContext(), setConnectProfileModel, msetProfileListener);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (requestCode == SELECT_PICTURE1) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar1.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(), fileUploadModel, mUploadListener);
                img1.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == SELECT_PICTURE2) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar2.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(), fileUploadModel, mUploadListener1);
                img2.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == SELECT_PICTURE3) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar3.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(), fileUploadModel, mUploadListener2);
                img3.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == SELECT_PICTURE4) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar4.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(), fileUploadModel, mUploadListener3);
                img4.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == SELECT_PICTURE5) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar5.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(), fileUploadModel, mUploadListener4);
                img5.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == SELECT_PICTURE6) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar6.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(), fileUploadModel, mUploadListener5);
                img6.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == SELECT_PICTURE7) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                FileUploadModel fileUploadModel = new FileUploadModel();
                fileUploadModel.setFile(new File(imagePath));
                progressBar7.setVisibility(View.VISIBLE);
                Controller.upoadPhot(getContext(), fileUploadModel, mUploadListener6);
                img7.setImageBitmap(showCroppedImage(imagePath));
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, result);
        }


    }

    RequestListener msetProfileListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("partner connect", responseObject.toString());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Profile submitted succesfully", Toast.LENGTH_LONG).show();
                }
            });

            PrefManager pref = new PrefManager(getContext());
            pref.setTutorial1(true);
            pref.setTutorial(true);
                Intent i = new Intent(getContext(),BinderActivity.class);
                i.putExtra("selection",1);
                startActivity(i);

        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("erro", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
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
            Log.d("intersts", responseObject.toString());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            });
            Controller.getUserInterests(getContext(), mUserInterestListener);
            i = new Intent(getContext(), InterestActivity.class);
            i.putExtra("response", responseObject.toString());
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("intersts", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
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
    RequestListener mUploadListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("uploaded file", responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(), ImageUploadResponseModel.class);
            pref.setKeyMasterImage(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar1.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar1.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            Log.d("uploaded file", responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(), ImageUploadResponseModel.class);
            pref.setKeyUserPic1(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar2.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            Log.d("uploaded file", responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(), ImageUploadResponseModel.class);
            pref.setKeyUserPic2(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar3.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar3.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            Log.d("uploaded file", responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(), ImageUploadResponseModel.class);
            pref.setKeyUserPic3(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar4.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar4.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            Log.d("uploaded file", responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(), ImageUploadResponseModel.class);
            pref.setKeyUserPic4(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar5.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar5.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            Log.d("uploaded file", responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(), ImageUploadResponseModel.class);
            pref.setKeyUserPic5(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar6.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar6.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            Log.d("uploaded file", responseObject.toString());
            ImageUploadResponseModel imageUploadResponseModel = JsonUtils.objectify(responseObject.toString(), ImageUploadResponseModel.class);
            pref.setKeyUserPic6(imageUploadResponseModel.getUrl());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar7.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("uploaded file error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar7.setVisibility(View.GONE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
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
            Log.d("facebook ", responseObject.toString());
            final FacebookModel facebookModel = JsonUtils.objectify(responseObject.toString(), FacebookModel.class);
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    setProfileConnect(facebookModel);
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("facebook connect error", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
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
    RequestListener mUserInterestListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("user interests ", responseObject.toString());
            i.putExtra("userinterests", responseObject.toString());
            startActivity(i);
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("user interests", message);
            if (getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    };

    public void setProfileConnect(final FacebookModel data) {
        if (data.getImages() != null) {
            if (data.getImages().getMaster() != null) {

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
                        pref.setKeyMasterImage(data.getImages().getMaster());
                        img1.setImageBitmap(loadedImage);
                    }
                });
            }
            if (data.getImages().getOthers() != null) {
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
                            pref.setKeyUserPic1(data.getImages().getOthers()[0]);
                            img2.setImageBitmap(loadedImage);
                        }
                    });
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
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
                            pref.setKeyUserPic2(data.getImages().getOthers()[1]);
                            img3.setImageBitmap(loadedImage);
                        }
                    });
                } catch (Exception e) {
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
                            pref.setKeyUserPic3(data.getImages().getOthers()[2]);
                            img4.setImageBitmap(loadedImage);
                        }
                    });
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
            if (data.getImages().getOthers() != null) {
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
                            pref.setKeyUserPic4(data.getImages().getOthers()[3]);
                            img5.setImageBitmap(loadedImage);
                        }
                    });
                } catch (Exception e) {
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
                            pref.setKeyUserPic5(data.getImages().getOthers()[4]);
                            img6.setImageBitmap(loadedImage);
                        }
                    });
                } catch (Exception e) {
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
                            pref.setKeyUserPic6(data.getImages().getOthers()[5]);
                            img7.setImageBitmap(loadedImage);
                        }
                    });
                } catch (Exception e) {
                    //Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.raasta_gurgaon);
                    //restImage.setImageBitmap(icon);
                }
            }
        }
        if (data != null) {
//                if (data.getUser().getFirst_name() != null){
//                    if (data.getUser().getLast_name() != null){
//                        if (data.getAge() > 0 )
//                            name.setText(data.getUser().getFirst_name()+" "+data.getUser().getLast_name()+", "+data.getAge());
//                        else
//                            name.setText(data.getUser().getFirst_name()+" "+data.getUser().getLast_name());
//                    }else {
//                        if (data.getAge() > 0)
//                            name.setText(data.getUser().getFirst_name()+", "+data.getAge());
//                        else
//                            name.setText(data.getUser().getFirst_name());
//                    }
//                }
            if (data.getOccupation() != null) {
                etOccupation.setText(data.getOccupation());
            }
            if (data.getDescription() != null) {
                etGender.setText(data.getDescription());
            }
            if (data.getHome_town() != null) {
                etHomeTwon.setText(data.getHome_town());
            }
//            if (pref.getCurrentLocationAsObject() != null) {
//                if (pref.getCurrentLocationAsObject().getLatitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0) {
//                    etLocation.setText(MyUtils.getCityNameFromLatLng(getContext(), pref.getCurrentLocationAsObject().getLatitude(), pref.getCurrentLocationAsObject().getLongitude()));
//                }
//            }

        }
    }

    private class GestureListener1 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAddProfilePicDialog1(SELECT_PICTURE1);
            delete.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (pref.getKeyMasterImage() != null && !pref.getKeyMasterImage().equals("")) {
                deleteVisible = true;
                delete.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }
    }

    private class GestureListener2 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAddProfilePicDialog1(SELECT_PICTURE2);
            delete1.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (pref.getKeyUserPic1() != null && !pref.getKeyUserPic1().equals("")) {
                deleteVisible = true;
                delete1.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!pref.getKeyMasterImage().equals("") && !pref.getKeyUserPic1().equals("")) {
                delete1.setVisibility(View.GONE);
                String a = pref.getKeyMasterImage();
                pref.setKeyMasterImage(pref.getKeyUserPic1());
                pref.setKeyUserPic1(a);
                Picasso.with(getContext()).load(pref.getKeyMasterImage()).into(img1);
                Picasso.with(getContext()).load(pref.getKeyUserPic1()).into(img2);
            }
            return true;
        }
    }

    private class GestureListener3 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAddProfilePicDialog1(SELECT_PICTURE3);
            delete2.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (pref.getKeyUserPic2() != null && !pref.getKeyUserPic2().equals("")) {
                deleteVisible = true;
                delete2.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!pref.getKeyMasterImage().equals("") && !pref.getKeyUserPic2().equals("")) {
                delete2.setVisibility(View.GONE);
                String a = pref.getKeyMasterImage();
                pref.setKeyMasterImage(pref.getKeyUserPic2());
                pref.setKeyUserPic2(a);
                Picasso.with(getContext()).load(pref.getKeyMasterImage()).into(img1);
                Picasso.with(getContext()).load(pref.getKeyUserPic2()).into(img3);
            }
            return true;
        }
    }

    private class GestureListener4 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAddProfilePicDialog1(SELECT_PICTURE4);
            delete3.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (pref.getKeyUserPic3() != null && !pref.getKeyUserPic3().equals("")) {
                deleteVisible = true;
                delete3.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!pref.getKeyMasterImage().equals("") && !pref.getKeyUserPic3().equals("")) {
                delete3.setVisibility(View.GONE);
                String a = pref.getKeyMasterImage();
                pref.setKeyMasterImage(pref.getKeyUserPic3());
                pref.setKeyUserPic3(a);
                Picasso.with(getContext()).load(pref.getKeyMasterImage()).into(img1);
                Picasso.with(getContext()).load(pref.getKeyUserPic3()).into(img4);
            }
            return true;
        }
    }

    private class GestureListener5 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAddProfilePicDialog1(SELECT_PICTURE5);
            delete4.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (pref.getKeyUserPic4() != null && !pref.getKeyUserPic4().equals("")) {
                deleteVisible = true;
                delete4.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!pref.getKeyMasterImage().equals("") && !pref.getKeyUserPic4().equals("")) {
                delete4.setVisibility(View.GONE);
                String a = pref.getKeyMasterImage();
                pref.setKeyMasterImage(pref.getKeyUserPic4());
                pref.setKeyUserPic4(a);
                Picasso.with(getContext()).load(pref.getKeyMasterImage()).into(img1);
                Picasso.with(getContext()).load(pref.getKeyUserPic4()).into(img5);
            }
            return true;
        }
    }

    private class GestureListener6 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAddProfilePicDialog1(SELECT_PICTURE6);
            delete5.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (pref.getKeyUserPic5() != null && !pref.getKeyUserPic5().equals("")) {
                deleteVisible = true;
                delete5.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!pref.getKeyMasterImage().equals("") && !pref.getKeyUserPic5().equals("")) {
                delete5.setVisibility(View.GONE);
                String a = pref.getKeyMasterImage();
                pref.setKeyMasterImage(pref.getKeyUserPic5());
                pref.setKeyUserPic5(a);
                Picasso.with(getContext()).load(pref.getKeyMasterImage()).into(img1);
                Picasso.with(getContext()).load(pref.getKeyUserPic5()).into(img6);
            }
            return true;
        }
    }

    private class GestureListener7 extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        // event when double tap occurs

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            showAddProfilePicDialog1(SELECT_PICTURE7);
            delete6.setVisibility(View.GONE);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (pref.getKeyUserPic6() != null && !pref.getKeyUserPic6().equals("")) {
                deleteVisible = true;
                delete6.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!pref.getKeyMasterImage().equals("") && !pref.getKeyUserPic6().equals("")) {
                delete6.setVisibility(View.GONE);
                String a = pref.getKeyMasterImage();
                pref.setKeyMasterImage(pref.getKeyUserPic6());
                pref.setKeyUserPic6(a);
                Picasso.with(getContext()).load(pref.getKeyMasterImage()).into(img1);
                Picasso.with(getContext()).load(pref.getKeyUserPic6()).into(img7);
            }
            return true;
        }
    }
}
