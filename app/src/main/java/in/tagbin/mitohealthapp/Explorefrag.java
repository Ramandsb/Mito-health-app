package in.tagbin.mitohealthapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wenchao.cardstack.CardStack;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CardsDataAdapter;
import in.tagbin.mitohealthapp.helper.JsonUtils;
import in.tagbin.mitohealthapp.model.UserListModel;

public class Explorefrag  extends Fragment {
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.explore, container, false);
        mCardStack = (CardStack) viewGroup.findViewById(R.id.container);
        TextView name = (TextView)(viewGroup.findViewById(R.id.tvExploreName));
        TextView age = (TextView)(viewGroup.findViewById(R.id.tvExploreAge));
        TextView distance = (TextView)(viewGroup.findViewById(R.id.tvExploreDistance));
        TextView interests = (TextView)(viewGroup.findViewById(R.id.tvExploreInterests));

        mCardStack.setContentResource(R.layout.card_content);
//        mCardStack.setStackMargin(20);
        mCardAdapter = new CardsDataAdapter(getActivity().getApplicationContext());
        mCardAdapter.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
        mCardAdapter.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
        mCardAdapter.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
        mCardAdapter.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
        mCardAdapter.add(new UserListModel(23,"Varun","Male","less 2 kms away","2 Common Interests"));
//        mCardAdapter.add("test2");
//        mCardAdapter.add("test3");
//        mCardAdapter.add("test4");
//        mCardAdapter.add("test5");
//        name.setText(getItem(position).getName());
//        age.setText(getItem(position).getAge()+", "+getItem(position).getSex());
//        distance.setText(getItem(position).getDistance());
//        interests.setText(getItem(position).getInterests());
        mCardStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(getContext(), ProfileActivity.class);
//                i.putExtra("response", JsonUtils.jsonify();
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getContext().startActivity(i);
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
