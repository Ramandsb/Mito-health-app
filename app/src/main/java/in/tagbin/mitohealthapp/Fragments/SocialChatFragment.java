package in.tagbin.mitohealthapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.ChatRequestActivity;
import in.tagbin.mitohealthapp.activity.SettingsActivity;
import in.tagbin.mitohealthapp.helper.PrefManager;

/**
 * Created by chetan on 23/11/16.
 */

public class SocialChatFragment extends Fragment {
    TextView coins;
    int coinsFinal;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_chat,container,false);
        return view;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("PREPDUG", "hereProfile");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
        //InitActivity i = (InitActivity) getActivity();
        //i.getActionBar().setTitle("Profile");
        menu.findItem(R.id.action_next).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_save).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setVisible(false);
        menu.findItem(R.id.action_coin).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        menu.findItem(R.id.action_requests).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        menu.findItem(R.id.action_Settings).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS).setVisible(true);
        View view = menu.findItem(R.id.action_coin).getActionView();
        coins = (TextView) view.findViewById(R.id.tvCoins);
        PrefManager pref = new PrefManager(getContext());
        coinsFinal = pref.getKeyCoins();
        coins.setText(""+coinsFinal);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_requests) {
            Intent i = new Intent(getContext(),ChatRequestActivity.class);
            startActivity(i);
        }else if (id == R.id.action_Settings) {
            //if (pref.getKeyUserDetails() != null && pref.getKeyUserDetails().getProfile().getHeight() != 0 && pref.getKeyUserDetails().getProfile().getWeight() != 0){
            //toolbar_title.setText("Settings");
            //toolbar.setTitle("");
            //fra = new SettingsActivity();
            Intent i = new Intent(getContext(), SettingsActivity.class);
            startActivity(i);
//            }else {
//                final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(getContext(),R.style.AppCompatAlertDialogStyle);
//                alertDialog1.setTitle("Enter Details");
//                alertDialog1.setMessage("Please enter your height and weight to proceed");
//                alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                alertDialog1.show();
//            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
