package in.tagbin.mitohealthapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wenchao.cardstack.CardStack;

import in.tagbin.mitohealthapp.Interfaces.RequestListener;
import in.tagbin.mitohealthapp.app.Controller;
import in.tagbin.mitohealthapp.helper.CardsDataAdapter;
import in.tagbin.mitohealthapp.model.UserListModel;

public class Explorefrag  extends Fragment {
    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.explore, container, false);
        mCardStack = (CardStack) viewGroup.findViewById(R.id.container);

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
