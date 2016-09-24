package in.tagbin.mitohealthapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivityAdapter extends  RecyclerView.Adapter<ChatActivityAdapter.MyViewHolder> {

    //Creating an arraylist of POJO objects
    private ArrayList<CustomPojo> list_members=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public ChatActivityAdapter(Context context,ArrayList<CustomPojo> list_memb){
        this.context=context;
        inflater=LayoutInflater.from(context);
        list_members=list_memb;
    }
    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.left, parent, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    CustomPojo list_items;
    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        list_items=list_members.get(position);

        Log.d("sourceeeeeee",list_items.getSource());
        String source= list_items.getSource();
        if (source.equals("to")){

            holder.setGravity.setGravity(Gravity.RIGHT);
            holder.setpatch.setBackgroundResource(R.drawable.in_message_bg);

        }else if (source.equals("from")){

            holder.setGravity.setGravity(Gravity.LEFT);
            holder.setpatch.setBackgroundResource(R.drawable.out_message_bg);

        }
        holder.message.setText(list_items.getMessages());

    }


    @Override
    public int getItemCount() {
        return list_members.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView message,time;
        LinearLayout setGravity,setpatch;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            setGravity= (LinearLayout) itemView.findViewById(R.id.set_gravity);
            setpatch= (LinearLayout) itemView.findViewById(R.id.setpatch);
            message=(TextView)itemView.findViewById(R.id.set_message);
            time=(TextView)itemView.findViewById(R.id.time);


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
