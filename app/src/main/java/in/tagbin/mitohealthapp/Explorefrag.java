package in.tagbin.mitohealthapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CardsDataAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ConnectUserModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ExploreModel;
import pl.droidsonroids.gif.GifImageView;

public class Explorefrag  extends Fragment implements SwipeDeck.SwipeEventCallback {
    private SwipeDeck mCardStack;
    private CardsDataAdapter mCardAdapter;
    GifImageView imageView;
    ExploreModel data;
    RelativeLayout mainContent;
    TextView name,age,distance,noData;
    GifImageView progressBar;
    PrefManager pref;
    int page =1;
    double latitde=0.0,longitude=0.0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.explore, container, false);
        mCardStack = (SwipeDeck) viewGroup.findViewById(R.id.container);
        name = (TextView)(viewGroup.findViewById(R.id.tvExploreName));
        age = (TextView)(viewGroup.findViewById(R.id.tvExploreAge));
        distance = (TextView)(viewGroup.findViewById(R.id.tvExploreDistance));
        imageView = (GifImageView) viewGroup.findViewById(R.id.ivExploreSubmit);
        noData = (TextView) viewGroup.findViewById(R.id.tvNoNearbyUsersData);
        mainContent = (RelativeLayout) viewGroup.findViewById(R.id.relativeMainProfile);
        progressBar = (GifImageView) viewGroup.findViewById(R.id.progressBar);
        data = new ExploreModel();
        pref = new PrefManager(getActivity());
        mCardStack.setEventCallback(this);
        mCardStack.setHardwareAccelerationEnabled(true);
        //interests.setText(list.get(position).getInterests());
        mCardStack.setLeftImage(R.id.left_image);
        mCardStack.setRightImage(R.id.right_image);

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
                        mCardAdapter = new CardsDataAdapter(data,getContext());
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
                        distance.setText("less "+new DecimalFormat("##.#").format(result).toString() + " kms away");
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ConnectUserModel connectUserModel = new ConnectUserModel();
                                connectUserModel.setId(data.getNearby_user_list().get(0).getUser().getId());
                                progressBar.setVisibility(View.VISIBLE);
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

    RequestListener mConnectListener = new RequestListener() {
        @Override
        public void onRequestStarted() {

        }

        @Override
        public void onRequestCompleted(Object responseObject) throws JSONException, ParseException {
            Log.d("user connected",responseObject.toString());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Connection request send successfully", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("user con error",message);
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

    @Override
    public void cardSwipedLeft(int position) {
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
            distance.setText("less "+new DecimalFormat("##.#").format(result).toString() + " kms away");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectUserModel connectUserModel = new ConnectUserModel();
                    connectUserModel.setId(data.getNearby_user_list().get(position1).getUser().getId());
                    progressBar.setVisibility(View.VISIBLE);
                    Controller.connectToUser(getContext(),connectUserModel,mConnectListener);
                }
            });
        }
    }

    @Override
    public void cardSwipedRight(int position) {
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
            distance.setText("less "+new DecimalFormat("##.#").format(result).toString() + " kms away");
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectUserModel connectUserModel = new ConnectUserModel();
                    connectUserModel.setId(data.getNearby_user_list().get(position1).getUser().getId());
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
