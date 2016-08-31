package in.tagbin.mitohealthapp;

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

import org.json.JSONException;

import java.text.ParseException;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CardsDataAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.helper.PrefManager;
import in.tagbin.mitohealthapp.model.ConnectUserModel;
import in.tagbin.mitohealthapp.model.ErrorResponseModel;
import in.tagbin.mitohealthapp.model.ExploreModel;
import link.fls.swipestack.SwipeStack;
import pl.droidsonroids.gif.GifImageView;

public class Explorefrag  extends Fragment implements SwipeStack.SwipeStackListener {
    private SwipeStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    ImageView imageView;
    ExploreModel data;
    RelativeLayout mainContent;
    TextView name,age,distance,noData;
    GifImageView progressBar;
    PrefManager pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.explore, container, false);
        mCardStack = (SwipeStack) viewGroup.findViewById(R.id.container);
        name = (TextView)(viewGroup.findViewById(R.id.tvExploreName));
        age = (TextView)(viewGroup.findViewById(R.id.tvExploreAge));
        distance = (TextView)(viewGroup.findViewById(R.id.tvExploreDistance));
        imageView = (ImageView) viewGroup.findViewById(R.id.ivExploreSubmit);
        noData = (TextView) viewGroup.findViewById(R.id.tvNoNearbyUsersData);
        mainContent = (RelativeLayout) viewGroup.findViewById(R.id.relativeMainProfile);
        progressBar = (GifImageView) viewGroup.findViewById(R.id.progressBar);
        data = new ExploreModel();
        pref = new PrefManager(getActivity());
        //interests.setText(list.get(position).getInterests());
        progressBar.setVisibility(View.VISIBLE);
        double latitde=0.0,longitude=0.0;
        if (pref.getCurrentLocationAsObject() != null){
            if (pref.getCurrentLocationAsObject().getLatitude() != 0.0 && pref.getCurrentLocationAsObject().getLongitude() != 0.0){
                latitde = pref.getCurrentLocationAsObject().getLatitude();
                longitude = pref.getCurrentLocationAsObject().getLongitude();
            }
        }
        Controller.getUsersNearBy(getContext(),longitude,latitde,mUsersListener);
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    mainContent.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    mCardAdapter = new CardsDataAdapter(data,getContext());
                    mCardStack.setAdapter(mCardAdapter);
                    mCardStack.setListener(Explorefrag.this);
                    if (data.getNearby_user_list().size() >0) {
                        mCardAdapter.notifyDataSetChanged();
                        name.setText(data.getNearby_user_list().get(0).getUser().getFirst_name());
                        age.setText(data.getNearby_user_list().get(0).getAge() + ", " + data.getNearby_user_list().get(0).getGender());
                        //distance.setText(data.getNearby_user_list().get(position).getDistance());
                        mCardStack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getContext(), ProfileActivity.class);
                                i.putExtra("response", JsonUtils.jsonify(data.getNearby_user_list().get(0)));
                                startActivity(i);
                            }
                        });
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
            final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message,ErrorResponseModel.class);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),errorResponseModel.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
    };


    @Override
    public void onViewSwipedToLeft(final int position) {
        final int position1 = mCardStack.getCurrentPosition()+1;
        if (position1 < data.getNearby_user_list().size()) {
            name.setText(data.getNearby_user_list().get(position1).getUser().getFirst_name());
            age.setText(data.getNearby_user_list().get(position1).getAge() + ", " + data.getNearby_user_list().get(position1).getGender());
            //distance.setText(data.getNearby_user_list().get(position1).getDistance());
            mCardStack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),ProfileActivity.class);
                    i.putExtra("response",JsonUtils.jsonify(data.getNearby_user_list().get(position1)));
                    startActivity(i);
                }
            });
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
        //interests.setText(list.get(position1).getInterests());
    }

    @Override
    public void onViewSwipedToRight(int position) {
        final int position1 = mCardStack.getCurrentPosition()+1;
        if (position1 < data.getNearby_user_list().size()) {
            name.setText(data.getNearby_user_list().get(position1).getUser().getFirst_name());
            age.setText(data.getNearby_user_list().get(position1).getAge() + ", " + data.getNearby_user_list().get(position1).getGender());
            //distance.setText(list.get(position1).getDistance());
            mCardStack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),ProfileActivity.class);
                    i.putExtra("response",JsonUtils.jsonify(data.getNearby_user_list().get(position1)));
                    startActivity(i);
                }
            });
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
        //interests.setText(list.get(position1).getInterests());
    }

    @Override
    public void onStackEmpty() {
        mCardStack.resetStack();
        name.setText(data.getNearby_user_list().get(0).getUser().getFirst_name());
        age.setText(data.getNearby_user_list().get(0).getAge()+", "+data.getNearby_user_list().get(0).getGender());
        //distance.setText(list.get(0).getDistance());
        mCardStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),ProfileActivity.class);
                i.putExtra("response",JsonUtils.jsonify(data.getNearby_user_list().get(0)));
                startActivity(i);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectUserModel connectUserModel = new ConnectUserModel();
                connectUserModel.setId(data.getNearby_user_list().get(0).getUser().getId());
                progressBar.setVisibility(View.VISIBLE);
                Controller.connectToUser(getContext(),connectUserModel,mConnectListener);
            }
        });
    }
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
                }
            });
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("user con error",message);
            final ErrorResponseModel errorResponseModel = JsonUtils.objectify(message,ErrorResponseModel.class);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),errorResponseModel.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }
    };

}
