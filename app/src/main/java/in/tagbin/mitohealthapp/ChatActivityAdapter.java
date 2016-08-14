package in.tagbin.mitohealthapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatActivityAdapter extends  RecyclerView.Adapter<ChatActivityAdapter.MyViewHolder> {

    //Creating an arraylist of POJO objects
    private ArrayList<CustomPojo> list_members=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public ChatActivityAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
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

        holder.message.setText(list_items.getMessages());

    }

    //Setting the arraylist
    public   void setListContent(ArrayList<CustomPojo> list_memb){
        list_members=list_memb;
//        list_members(0,list_members.size());
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return list_members.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView message,time;
        View setGravity;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            setGravity=itemView.findViewById(R.id.set_gravity);
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
