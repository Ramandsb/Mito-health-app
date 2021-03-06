package in.tagbin.mitohealthapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.MessagesModel;

public class ChatActivityAdapter extends  RecyclerView.Adapter<ChatActivityAdapter.MyViewHolder> {

    //Creating an arraylist of POJO objects
    private ArrayList<MessagesModel> list_members=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public ChatActivityAdapter(Context context,ArrayList<MessagesModel> list_memb){
        this.context=context;
        inflater=LayoutInflater.from(context);
        list_members=list_memb;
    }
    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.item_chat_activity, parent, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    MessagesModel list_items;
    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        list_items=list_members.get(position);
        String source= list_items.getSource();
        if (source.equals("to")){

            holder.setGravity.setGravity(Gravity.RIGHT);
            holder.message.setBackgroundResource(R.drawable.in_message_bg);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.setGravity.getLayoutParams();
            int margin = MyUtils.dpToPx(context,10);
            int margin1 = MyUtils.dpToPx(context,2);
            int margin2 = MyUtils.dpToPx(context,60);
            int margin3 = MyUtils.dpToPx(context,20);

            layoutParams.setMargins(margin2,margin1,margin,margin1);
            holder.setGravity.setLayoutParams(layoutParams);
            //holder.message.setPadding(margin3,margin3,margin3,margin2);
        }else if (source.equals("from")){

            holder.setGravity.setGravity(Gravity.LEFT);
            holder.message.setBackgroundResource(R.drawable.out_message_bg);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.setGravity.getLayoutParams();
            int margin = MyUtils.dpToPx(context,10);
            int margin1 = MyUtils.dpToPx(context,2);
            int margin2 = MyUtils.dpToPx(context,60);
            int margin3 = MyUtils.dpToPx(context,20);

            layoutParams.setMargins(margin,margin1,margin2,margin1);
            holder.setGravity.setLayoutParams(layoutParams);
            //holder.message.setPadding(margin3,margin3,margin3,margin2);
        }
        holder.message.setText(list_items.getMessages()+"              "+getDate(Long.parseLong(list_items.getTime())));
//        try {
//            holder.time.setText();
//        }catch (NumberFormatException e){
//            holder.time.setText(list_items.getTime());
//        }
    }
    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView message,time;
        LinearLayout setGravity;
        RelativeLayout setpatch;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            setGravity= (LinearLayout) itemView.findViewById(R.id.set_gravity);
            //setpatch= (RelativeLayout) itemView.findViewById(R.id.setpatch);
            message=(TextView)itemView.findViewById(R.id.set_message);
            time=(TextView)itemView.findViewById(R.id.set_time);


        }
        @Override
        public void onClick(View v) {

//Intent in=new Intent(context,Main2Activity.class);
//            in.putExtra("username",list_items.getName());
//            context.startActivity(in);
        }
    }
    public void removeAt(int position) {
        list_members.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, list_members.size());
    }
}
