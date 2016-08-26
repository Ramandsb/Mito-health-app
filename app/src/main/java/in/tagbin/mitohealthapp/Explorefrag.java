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

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CardsDataAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.ExploreModel;
import link.fls.swipestack.SwipeStack;

public class Explorefrag  extends Fragment implements SwipeStack.SwipeStackListener {
    private SwipeStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    ImageView imageView;
    ExploreModel data;
    RelativeLayout mainContent;
    TextView name,age,distance,noData;
    ProgressBar progressBar;

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
        progressBar = (ProgressBar) viewGroup.findViewById(R.id.progressBar);
        data = new ExploreModel();

//        mCardStack.setStackMargin(20);
//        list.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
//        list.add(new UserListModel(22,"Aasaqt","Male","less 3 kms away","3 Common Interests"));
//        list.add(new UserListModel(24,"Chetan","Male","less 4 kms away","4 Common Interests"));
//        list.add(new UserListModel(25,"Raman","Male","less 5 kms away","5 Common Interests"));
//        list.add(new UserListModel(26,"Girish","Male","less 6 kms away","6 Common Interests"));

//        mCardAdapter.add("test2");
//        mCardAdapter.add("test3");
//        mCardAdapter.add("test4");
//        mCardAdapter.add("test5");
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getContext(), ProfileActivity.class);
//                i.putExtra("response", JsonUtils.jsonify(list.get(0)));
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getContext().startActivity(i);
//            }
//        });

        //interests.setText(list.get(position).getInterests());
        progressBar.setVisibility(View.VISIBLE);
        Controller.getUsersNearBy(getContext(),mUsersListener);
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
    }

}
