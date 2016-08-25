package in.tagbin.mitohealthapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.tagbin.mitohealthapp.ProfileActivity;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.UserListModel;

/**
 * Created by aasaqt on 12/8/16.
 */
public class CardsDataAdapter extends BaseAdapter {
    List<UserListModel> mData;
    Context mContext;

    public CardsDataAdapter(List<UserListModel> data,Context pContext){
        mData = data;
        mContext = pContext;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public UserListModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup parent){
        contentView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.card_content, parent, false);
        CircleImageView personCard = (CircleImageView) contentView.findViewById(R.id.card1);
        personCard.setImageResource(R.drawable.hotel);
        personCard.setBorderWidth(MyUtils.dpToPx(mContext,10));
        personCard.setBorderColor(Color.parseColor("#22ff23"));
        return contentView;
    }
}
