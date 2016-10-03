package in.tagbin.mitohealthapp.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.model.ChatAccounts;

public class ChatFriendsAdapter extends  RecyclerView.Adapter<ChatFriendsAdapter.MyViewHolder> {

    //Creating an arraylist of POJO objects
    private ArrayList<ChatAccounts> list_members=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public ChatFriendsAdapter(Context context, ArrayList<ChatAccounts> list_members){
        this.context=context;
        this.list_members=list_members;
        inflater=LayoutInflater.from(context);
    }
    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.item_chat_friends, parent, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    ChatAccounts list_items;
    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        list_items=list_members.get(position);
        holder.user_name.setText(list_items.getName());
        holder.content.setVisibility(View.GONE);
        //holder.content.setText(list_items.getName());
        holder.time.setText(list_items.getPresence());
        if (list_items.getImage() != null) {
            byte[] encodeByte = Base64.decode(list_items.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            holder.imageView.setImageBitmap(bitmap);
        }
    }


    @Override
    public int getItemCount() {
        return list_members.size();
    }

    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView user_name,content,time;
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            user_name=(TextView)itemView.findViewById(R.id.user_name);
            content=(TextView)itemView.findViewById(R.id.content);
            time=(TextView)itemView.findViewById(R.id.time);
            imageView=(ImageView)itemView.findViewById(R.id.picture);
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
