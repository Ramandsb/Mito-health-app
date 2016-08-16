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

import com.wenchao.cardstack.CardStack;

import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CardsDataAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.UserListModel;

public class Explorefrag  extends Fragment  {
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    ImageView imageView;
    List<UserListModel> list;
    TextView name,age,distance,interests;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.explore, container, false);
        mCardStack = (CardStack) viewGroup.findViewById(R.id.container);
        name = (TextView)(viewGroup.findViewById(R.id.tvExploreName));
        age = (TextView)(viewGroup.findViewById(R.id.tvExploreAge));
        distance = (TextView)(viewGroup.findViewById(R.id.tvExploreDistance));
        interests = (TextView)(viewGroup.findViewById(R.id.tvExploreInterests));
        imageView = (ImageView) viewGroup.findViewById(R.id.ivExploreSubmit);
        list = new ArrayList<UserListModel>();
        mCardStack.setContentResource(R.layout.card_content);
//        mCardStack.setStackMargin(20);
        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext());

        list.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
        list.add(new UserListModel(23,"Aasaqt","Male","less 2 kms away","2 Common Interests"));
        list.add(new UserListModel(23,"Chetan","Male","less 2 kms away","2 Common Interests"));
        list.add(new UserListModel(23,"Raman","Male","less 2 kms away","2 Common Interests"));
        list.add(new UserListModel(23,"Girish","Male","less 2 kms away","2 Common Interests"));
        for (int i= 0;i<list.size();i++){
            mCardAdapter.add(list.get(i));
        }
//        mCardAdapter.add("test2");
//        mCardAdapter.add("test3");
//        mCardAdapter.add("test4");
//        mCardAdapter.add("test5");
//        name.setText(getItem(position).getName());
//        age.setText(getItem(position).getAge()+", "+getItem(position).getSex());
//        distance.setText(getItem(position).getDistance());
//        interests.setText(getItem(position).getInterests());
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
        Controller.getUsersNearBy(getContext(),mUsersListener);
        if(mCardStack.getAdapter() != null) {
            Log.i("MyActivity", "Card Stack size: " + mCardStack.getAdapter().getCount());
        }
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


}
