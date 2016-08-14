package in.tagbin.mitohealthapp.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.tagbin.mitohealthapp.R;


public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title,eventtitle,date;
    ImageView backimage,decline,confirm,delete,edit;
    RelativeLayout linearCard;
    TextView time;
    TextView capacity;
    TextView location;

    public MyViewHolder(View itemView) {
        super(itemView);
        time= (TextView) itemView.findViewById(R.id.time);
        capacity= (TextView) itemView.findViewById(R.id.capacity);
        title= (TextView) itemView.findViewById(R.id.maintitle);
        eventtitle= (TextView) itemView.findViewById(R.id.event_title);
        date = (TextView) itemView.findViewById(R.id.event_date);
        linearCard = (RelativeLayout) itemView.findViewById(R.id.linearLookupCard);
        backimage= (ImageView) itemView.findViewById(R.id.setimage);
        confirm = (ImageView) itemView.findViewById(R.id.ivConfirm);
        delete = (ImageView) itemView.findViewById(R.id.ivBin);
        edit = (ImageView) itemView.findViewById(R.id.ivEdit);
        location= (TextView) itemView.findViewById(R.id.myloc);
    }
}
