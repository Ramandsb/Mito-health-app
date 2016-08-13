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
        TextView name = (TextView)(contentView.findViewById(R.id.tvExploreName));
        TextView age = (TextView)(contentView.findViewById(R.id.tvExploreAge));
        TextView distance = (TextView)(contentView.findViewById(R.id.tvExploreDistance));
        TextView interests = (TextView)(contentView.findViewById(R.id.tvExploreInterests));
        PersonCard personCard = (PersonCard) contentView.findViewById(R.id.card1);
        name.setText(getItem(position).getName());
        age.setText(getItem(position).getAge()+", "+getItem(position).getSex());
        distance.setText(getItem(position).getDistance());
        interests.setText(getItem(position).getInterests());
        personCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("response",JsonUtils.jsonify(getItem(position)));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            }
        });
        return contentView;
    }
}
