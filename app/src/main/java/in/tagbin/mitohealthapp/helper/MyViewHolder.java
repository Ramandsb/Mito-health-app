package in.tagbin.mitohealthapp.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.tagbin.mitohealthapp.R;


public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title,eventtitle,join;
    ImageView backimage,edit,delete;
    RelativeLayout linearCard;
    TextView time;
    TextView capacity;
    LinearLayout bottomBar;
    TextView location;

    public MyViewHolder(View itemView) {
        super(itemView);
        time= (TextView) itemView.findViewById(R.id.time);
        capacity= (TextView) itemView.findViewById(R.id.capacity);
        title= (TextView) itemView.findViewById(R.id.maintitle);
        eventtitle= (TextView) itemView.findViewById(R.id.event_title);
        join = (TextView) itemView.findViewById(R.id.joinEvent);
        linearCard = (RelativeLayout) itemView.findViewById(R.id.linearLookupCard);
        bottomBar = (LinearLayout) itemView.findViewById(R.id.linearBottomBar);
        backimage= (ImageView) itemView.findViewById(R.id.setimage);
        delete = (ImageView) itemView.findViewById(R.id.ivDelete);
        edit = (ImageView) itemView.findViewById(R.id.ivEdit);
        location= (TextView) itemView.findViewById(R.id.myloc);
    }
}
