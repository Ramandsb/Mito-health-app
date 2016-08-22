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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CardsDataAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.UserListModel;
import link.fls.swipestack.SwipeStack;

public class Explorefrag  extends Fragment implements SwipeStack.SwipeStackListener {
    private SwipeStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    ImageView imageView;
    List<UserListModel> list;
    TextView name,age,distance,interests;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.explore, container, false);
        mCardStack = (SwipeStack) viewGroup.findViewById(R.id.container);
        name = (TextView)(viewGroup.findViewById(R.id.tvExploreName));
        age = (TextView)(viewGroup.findViewById(R.id.tvExploreAge));
        distance = (TextView)(viewGroup.findViewById(R.id.tvExploreDistance));
        interests = (TextView)(viewGroup.findViewById(R.id.tvExploreInterests));
        imageView = (ImageView) viewGroup.findViewById(R.id.ivExploreSubmit);
        list = new ArrayList<UserListModel>();
//        mCardStack.setStackMargin(20);
        list.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
        list.add(new UserListModel(22,"Aasaqt","Male","less 3 kms away","3 Common Interests"));
        list.add(new UserListModel(24,"Chetan","Male","less 4 kms away","4 Common Interests"));
        list.add(new UserListModel(25,"Raman","Male","less 5 kms away","5 Common Interests"));
        list.add(new UserListModel(26,"Girish","Male","less 6 kms away","6 Common Interests"));
        mCardAdapter = new CardsDataAdapter(list,getContext());
//        mCardAdapter.add("test2");
//        mCardAdapter.add("test3");
//        mCardAdapter.add("test4");
//        mCardAdapter.add("test5");
//
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("response", JsonUtils.jsonify(list.get(0)));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            }
        });
        mCardStack.setAdapter(mCardAdapter);
        mCardStack.setListener(this);
        int position = mCardStack.getCurrentPosition();
        name.setText(list.get(position).getName());
        age.setText(list.get(position).getAge()+", "+list.get(position).getSex());
        distance.setText(list.get(position).getDistance());
        interests.setText(list.get(position).getInterests());
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
        }

        @Override
        public void onRequestError(int errorCode, String message) {
            Log.d("users listener error",message);
        }
    };


    @Override
    public void onViewSwipedToLeft(int position) {
        int position1 = mCardStack.getCurrentPosition();
        name.setText(list.get(position1).getName());
        age.setText(list.get(position1).getAge()+", "+list.get(position1).getSex());
        distance.setText(list.get(position1).getDistance());
        interests.setText(list.get(position1).getInterests());
    }

    @Override
    public void onViewSwipedToRight(int position) {
        int position1 = mCardStack.getCurrentPosition();
        name.setText(list.get(position1).getName());
        age.setText(list.get(position1).getAge()+", "+list.get(position1).getSex());
        distance.setText(list.get(position1).getDistance());
        interests.setText(list.get(position1).getInterests());
    }

    @Override
    public void onStackEmpty() {
        mCardStack.resetStack();
    }
}
