package in.tagbin.mitohealthapp.helper;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import in.tagbin.mitohealthapp.ProfileActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.UserListModel;

/**
 * Created by aasaqt on 12/8/16.
 */
public class CardsDataAdapter extends ArrayAdapter<UserListModel> {
    public CardsDataAdapter(Context context) {
        super(context, R.layout.card_content);
    }
    @Override
    public View getView(final int position, final View contentView, ViewGroup parent){

        PersonCard personCard = (PersonCard) contentView.findViewById(R.id.card1);


        return contentView;
    }
}
