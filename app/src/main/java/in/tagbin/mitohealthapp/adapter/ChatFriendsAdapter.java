package in.tagbin.mitohealthapp.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.tagbin.mitohealthapp.Database.ChatMessagesDatabase;
import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.helper.MyUtils;
import in.tagbin.mitohealthapp.model.ChatAccounts;
import in.tagbin.mitohealthapp.model.MessagesModel;

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
        holder.content.setVisibility(View.VISIBLE);
        //holder.content.setText(list_items.getName());
        //holder.time.setText(list_items.getPresence());
        if (list_items.getImage() != null) {
            byte[] encodeByte = Base64.decode(list_items.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            holder.imageView.setImageBitmap(bitmap);
        }
        ChatMessagesDatabase chatMessagesDatabase = new ChatMessagesDatabase(context);
        List<MessagesModel> list=chatMessagesDatabase.getChatUsers(list_items.getUser());
        if (list.size() > 0 ){
            holder.content.setText(list.get(list.size() - 1).getMessages());
            final String relativeTime = String.valueOf(DateUtils.getRelativeTimeSpanString(Long.parseLong(list.get(list.size() - 1).getTime()), getCurrentTime(context), DateUtils.MINUTE_IN_MILLIS));
            holder.time.setText(relativeTime);
        }else{
            holder.content.setText("You can start the chat");
            holder.time.setText("");
        }
        if (list_items.getPresence().equals("unavailable")){
            holder.status.setImageResource(R.drawable.dot);
        }else{
            holder.status.setImageResource(R.drawable.iv_online);
        }
    }


    @Override
    public int getItemCount() {
        return list_members.size();
    }
    public long getCurrentTime(Context ctx){

        long sec = System.currentTimeMillis();
        return sec;
    }
    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView user_name,content,time;
        ImageView imageView,status;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            user_name=(TextView)itemView.findViewById(R.id.user_name);
            content=(TextView)itemView.findViewById(R.id.content);
            time=(TextView)itemView.findViewById(R.id.time);
            imageView=(ImageView)itemView.findViewById(R.id.picture);
            status=(ImageView)itemView.findViewById(R.id.ivDot);
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
