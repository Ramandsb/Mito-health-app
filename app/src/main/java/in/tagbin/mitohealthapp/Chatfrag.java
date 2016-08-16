package in.tagbin.mitohealthapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class Chatfrag extends Fragment {

    public static final String HOST = "chat.eazevent.in";
    public static final int PORT = 5222;
    public static final String SERVICE = "chat.eazevent.in";
    public static final String USERNAME = "ankit";
    public static final String PASSWORD = "1234";

    private XMPPConnection connection;
    private ArrayList<String> messages = new ArrayList<String>();
    private Handler mHandler = new Handler();

    RecyclerView recyclerView;
    ChatAdapter adapter;
    private ArrayList<CustomPojo> listContentArr= new ArrayList<>();
    String user="rman";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.chat, container, false);
        customDialog();
        recyclerView=(RecyclerView)view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new ChatAdapter(getActivity());
        if (isNetworkAvailable()) {
            Connector asyncTask = new Connector();
            asyncTask.execute();
            Log.d("available network","");
        }else {
            Log.d("No Network Available","");
        }
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                CustomPojo pojo = listContentArr.get(position);
                startActivity(new Intent(getActivity(),ChatActivity.class).putExtra("user_name",pojo.getName()));
                Log.d("click details",""+pojo.getContent()+"//////"+pojo.getName()+"/////"+pojo.getTime());
            }
        });
        return view;
    }
    private boolean isNetworkAvailable() {
        showDialog();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//       Log.d("is connected",activeNetworkInfo.isConnected()+"///");
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void setConnection(XMPPConnection connection) {
        this.connection = connection;
        if (connection != null) {
            // Add a packet listener to get messages sent to us
            PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
            connection.addPacketListener(new PacketListener() {
                @Override
                public void processPacket(Packet packet) {
                    Message message = (Message) packet;
                    if (message.getBody() != null) {
                        String fromName = StringUtils.parseBareAddress(message
                                .getFrom());
                        Log.i("XMPPChatDemoActivity", "Text Recieved " + message.getBody()
                                + " from " + fromName );
                        messages.add(fromName + ":");
                        messages.add(message.getBody());
                        // Add the incoming message to the list view
                        mHandler.post(new Runnable() {
                            public void run() {

//                                XMPPChatDemoActivity xmppConnection=new XMPPChatDemoActivity();
//                                xmppConnection.setListAdapter();
                            }
                        });
                    }
                }
            }, filter);
        }
    }
    class Connector extends AsyncTask {



        @Override
        protected Object doInBackground(Object[] objects) {
            showDialog();
            ConnectionConfiguration connConfig = new ConnectionConfiguration(
                    HOST, PORT, SERVICE);
            XMPPConnection connection = new XMPPConnection(connConfig);

            try {
                connection.connect();
                Log.i("XMPPChatDemoActivity",
                        "Connected to " + connection.getHost());
            } catch (XMPPException ex) {
                Log.e("XMPPChatDemoActivity", "Failed to connect to "
                        + connection.getHost());
                Log.e("XMPPChatDemoActivity", ex.toString());
                setConnection(null);
            }
            try {
                // SASLAuthentication.supportSASLMechanism("PLAIN", 0);
                connection.login(USERNAME, PASSWORD);
                Log.i("XMPPChatDemoActivity",
                        "Logged in as " + connection.getUser());

                // Set the status to available
                Presence presence = new Presence(Presence.Type.available);
                connection.sendPacket(presence);
                setConnection(connection);

                Roster roster = connection.getRoster();
                Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {
                    Log.d("XMPPChatDemoActivity",
                            "--------------------------------------");
                    Log.d("XMPPChatDemoActivity", "RosterEntry " + entry);
                    Log.d("XMPPChatDemoActivity",
                            "User: " + entry.getUser());
                    Log.d("XMPPChatDemoActivity",
                            "Name: " + entry.getName());

                    String name = entry.getName();
                    String content = entry.getUser();


                    Log.d("XMPPChatDemoActivity",
                            "Status: " + entry.getStatus());
                    Log.d("XMPPChatDemoActivity",
                            "Type: " + entry.getType());
                    Presence entryPresence = roster.getPresence(entry
                            .getUser());

                    Log.d("XMPPChatDemoActivity", "Presence Status: "
                            + entryPresence.getStatus());
                    Log.d("XMPPChatDemoActivity", "Presence Type: "
                            + entryPresence.getType());
                    Presence.Type type = entryPresence.getType();
                    if (type == Presence.Type.available)
                        Log.d("XMPPChatDemoActivity", "Presence AVIALABLE");
                    Log.d("XMPPChatDemoActivity", "Presence : "
                            + entryPresence);

                    CustomPojo pojoObject = new CustomPojo();
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    Calendar calobj = Calendar.getInstance();

                    pojoObject.setName(content);
                    pojoObject.setContent(content);
                    pojoObject.setTime(df.format(calobj.getTime()));
                    listContentArr.add(pojoObject);

                }

//
            } catch (XMPPException ex) {
                Log.e("XMPPChatDemoActivity", "Failed to log in as "
                        + USERNAME);
                Log.e("XMPPChatDemoActivity", ex.toString());
                setConnection(null);
            }

            //
            // dialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            recyclerView.setAdapter(adapter);
            adapter.setListContent(listContentArr);
            dismissDialog();
        }

        //    }


    }

    TextView messageView;
    ProgressBar progressBar;
    android.app.AlertDialog alert;


    public void customDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View customView = inflater.inflate(R.layout.dialog, null);
        builder.setView(customView);
        messageView = (TextView) customView.findViewById(R.id.tvdialog);
        progressBar = (ProgressBar) customView.findViewById(R.id.progress);
        alert = builder.create();

    }

    public void showDialog() {

        progressBar.setVisibility(View.VISIBLE);
        alert.show();
        messageView.setText("Loading");
    }

    public void dismissDialog() {
        alert.dismiss();
    }

    public void displayErrors(VolleyError error) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("Connection failed");
        } else if (error instanceof AuthFailureError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("AuthFailureError");
        } else if (error instanceof ServerError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ServerError");
        } else if (error instanceof NetworkError) {
            messageView.setText("NetworkError");
        } else if (error instanceof ParseError) {
            progressBar.setVisibility(View.GONE);
            messageView.setText("ParseError");
        }
    }
    }