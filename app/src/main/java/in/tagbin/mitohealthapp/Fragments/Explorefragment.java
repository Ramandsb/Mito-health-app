package in.tagbin.mitohealthapp.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.ChatRequestActivity;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.adapter.ExploreAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ConnectUserModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ExploreModel;
import pl.droidsonroids.gif.GifImageView;

public class Explorefragment extends Fragment implements SwipeDeck.SwipeEventCallback {
    private SwipeDeck mCardStack;
    private ExploreAdapter mCardAdapter;
    ImageView imageView;
    ExploreModel data;
    RelativeLayout noData,mainContent;
    TextView name,age,distance,coins;
    GifImageView progressBar;
    PrefManager pref;
    boolean connected = false;
    int page =1,coinsFinal = 0;
    double latitde=0.0,longitude=0.0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.frag_explore, container, false);
        mCardStack = (SwipeDeck) viewGroup.findViewById(R.id.container);
        name = (TextView)(viewGroup.findViewById(R.id.tvExploreName));
        age = (TextView)(viewGroup.findViewById(R.id.tvExploreAge));
        distance = (TextView)(viewGroup.findViewById(R.id.tvExploreDistance));
        imageView = (ImageView) viewGroup.findViewById(R.id.ivExploreSubmit);
        noData = (RelativeLayout) viewGroup.findViewById(R.id.relativeNoUsersNearby);
        mainContent = (RelativeLayout) viewGroup.findViewById(R.id.relativeMainProfile);
        progressBar = (GifImageView) viewGroup.findViewById(R.id.progressBar);
        data = new ExploreModel();
        pref = new PrefManager(getActivity());
        mCardStack.setEventCallback(this);
        mCardStack.setHardwareAccelerationEnabled(true);
        //interests.setText(list.get(position).getInterests());
        mCardStack.setLeftImage(R.id.right_image);
        mCardStack.setRightImage(R.id.left_image);

        progressBar.setVisibility(View.VISIBLE);

        if (pref.getCurrentLocationAsObject() != null){
            if (pref.getCurrentLocationAsObject().getLatitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0){
                latitde = pref.getCurrentLocationAsObject().getLatitude();
                longitude = pref.getCurrentLocationAsObject().getLongitude();
            }
        }

        Controller.getUsersNearBy(getContext(),longitude,latitde,page,mUsersListener);
        return viewGroup;
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
                .setVisible(false);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        menu.findItem(R.id.action_requests).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(false);
        menu.findItem(R.id.action_Settings).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_requests) {
            Intent i = new Intent(getContext(),ChatRequestActivity.class);
            startActivity(i);
        }else if (id == R.id.action_Settings) {
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
    RequestListener mUsersListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) {
            Log.d("users listener",responseObject.toString());
            data = JsonUtils.objectify(responseObject.toString(),ExploreModel.class);
            if(getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    mainContent.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    if (data.getNearby_user_list().size() >0) {
                        //mCardAdapter.notifyDataSetChanged();
                        mCardAdapter = new ExploreAdapter(data,getContext());
                        mCardStack.setAdapter(mCardAdapter);
                        if (data.getNearby_user_list().get(0).getUser().getLast_name() == null)
                            name.setText(data.getNearby_user_list().get(0).getUser().getFirst_name());
                        else
                            name.setText(data.getNearby_user_list().get(0).getUser().getFirst_name()+" "+data.getNearby_user_list().get(0).getUser().getLast_name());
                        String gender;
                        if (data.getNearby_user_list().get(0).getGender().toLowerCase().equals("m")){
                            gender = "Male";
                        }else{
                            gender = "Female";
                        }
                        age.setText(data.getNearby_user_list().get(0).getAge() + ", " + gender);
                        double lat2 = MyUtils.getLatitude(getContext(),data.getNearby_user_list().get(0).getLocation());
                        double long2 = MyUtils.getLongitude(getContext(),data.getNearby_user_list().get(0).getLocation());
                        double result = MyUtils.calculateDistance(latitde, longitude, lat2, long2, "K");
                        distance.setText("less than "+new DecimalFormat("##.#").format(result).toString() + " kms away");
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ConnectUserModel connectUserModel = new ConnectUserModel();
                                connectUserModel.setId(data.getNearby_user_list().get(0).getUser().getId());
                                connectUserModel.setActivity(1);
                                progressBar.setVisibility(View.VISIBLE);
                                connected = true;
                                Controller.connectToUser(getContext(),connectUserModel,mConnectListener);
                            }
                        });
                    }else{
                        mainContent.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("users listener error",message);
            if(getActivity() == null)
                return;
            if (errorCode >= 400 && errorCode < 500) {
                final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message, ErrorResponseModel.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        mainContent.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), errorResponseModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        mainContent.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    };

    RequestListener mConnectListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("user connected",responseObject.toString());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    mCardStack.swipeTopCardRight(0);
                    connected = false;
                    Toast.makeText(getContext(), "Connection request send successfully", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("user con error",message);
            if (getActivity() == null)
                return;
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
    RequestListener mRejectListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("user passed",responseObject.toString());
            if (getActivity() == null)
                return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "User passed", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("user pas error",message);
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
    @Override
    public void cardSwipedLeft(int position) {
        if (!connected) {
            ConnectUserModel connectUserModel = new ConnectUserModel();
            connectUserModel.setId(data.getNearby_user_list().get(position).getUser().getId());
            connectUserModel.setActivity(2);
            progressBar.setVisibility(View.VISIBLE);
            Controller.connectToUser(getContext(), connectUserModel, mRejectListener);
        }
        final int position1 = position+1;
        if (position1 < data.getNearby_user_list().size()) {
            if (data.getNearby_user_list().get(position1).getUser().getLast_name() == null)
                name.setText(data.getNearby_user_list().get(position1).getUser().getFirst_name());
            else
                name.setText(data.getNearby_user_list().get(position1).getUser().getFirst_name()+" "+data.getNearby_user_list().get(position1).getUser().getLast_name());
            String gender;
            if (data.getNearby_user_list().get(position1).getGender().toLowerCase().equals("m")){
                gender = "Male";
            }else{
                gender = "Female";
            }
            age.setText(data.getNearby_user_list().get(position1).getAge() + ", " + gender);
            double lat2 = MyUtils.getLatitude(getContext(),data.getNearby_user_list().get(position1).getLocation());
            double long2 = MyUtils.getLongitude(getContext(),data.getNearby_user_list().get(position1).getLocation());
            double result = MyUtils.calculateDistance(latitde, longitude, lat2, long2, "K");
            distance.setText("less than "+new DecimalFormat("##.#").format(result).toString() + " kms away");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectUserModel connectUserModel = new ConnectUserModel();
                    connectUserModel.setId(data.getNearby_user_list().get(position1).getUser().getId());
                    connectUserModel.setActivity(1);
                    connected = true;
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.connectToUser(getContext(),connectUserModel,mConnectListener);
                }
            });
        }
    }

    @Override
    public void cardSwipedRight(int position) {
        if (!connected) {
            ConnectUserModel connectUserModel = new ConnectUserModel();
            connectUserModel.setId(data.getNearby_user_list().get(position).getUser().getId());
            connectUserModel.setActivity(2);
            progressBar.setVisibility(View.VISIBLE);
            Controller.connectToUser(getContext(), connectUserModel, mRejectListener);
        }
        final int position1 = position+1;
        if (position1 < data.getNearby_user_list().size()) {
            if (data.getNearby_user_list().get(position1).getUser().getLast_name() == null)
                name.setText(data.getNearby_user_list().get(position1).getUser().getFirst_name());
            else
                name.setText(data.getNearby_user_list().get(position1).getUser().getFirst_name()+" "+data.getNearby_user_list().get(position1).getUser().getLast_name());
            String gender;
            if (data.getNearby_user_list().get(position1).getGender().toLowerCase().equals("m")){
                gender = "Male";
            }else{
                gender = "Female";
            }
            age.setText(data.getNearby_user_list().get(position1).getAge() + ", " + gender);
            double lat2 = MyUtils.getLatitude(getContext(),data.getNearby_user_list().get(position1).getLocation());
            double long2 = MyUtils.getLongitude(getContext(),data.getNearby_user_list().get(position1).getLocation());
            double result = MyUtils.calculateDistance(latitde, longitude, lat2, long2, "K");
            distance.setText("less than "+new DecimalFormat("##.#").format(result).toString() + " kms away");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectUserModel connectUserModel = new ConnectUserModel();
                    connectUserModel.setId(data.getNearby_user_list().get(position1).getUser().getId());
                    connectUserModel.setActivity(1);
                    connected = true;
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.connectToUser(getContext(),connectUserModel,mConnectListener);
                }
            });
        }
    }

    @Override
    public void cardsDepleted() {
        progressBar.setVisibility(View.VISIBLE);
        page++;
        Controller.getUsersNearBy(getContext(),longitude,latitde,page,mUsersListener);
    }

    @Override
    public void cardActionDown() {

    }

    @Override
    public void cardActionUp() {

    }
}
