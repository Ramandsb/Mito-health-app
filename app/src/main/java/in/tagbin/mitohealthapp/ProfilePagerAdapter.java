package in.tagbin.mitohealthapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Chetan on 11/08/16.
 */

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    String profile;
    Context mContext;

    public ProfilePagerAdapter(FragmentManager fm, String profile_connect, Context pContext) {
        super(fm);
        this.profile = profile_connect;
        mContext = pContext;
    }

    @Override
    public Fragment getItem(int position) {
        try {

            Fragment fm = null;

            switch (position) {
                case 0:
                    ProfilePage tab1=new ProfilePage();
                    return tab1;
                case 1:
                    if (ProfilePage.height == 0 && ProfilePage.weight == 0){
                        final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(mContext,R.style.AppCompatAlertDialogStyle);
                        alertDialog1.setTitle("Enter Details");
                        alertDialog1.setMessage("Please enter your height and weight to proceed");
                        alertDialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog1.show();
                    }else {
                        PartProfile tab2 = new PartProfile();
                        if (!profile.equals("") || !profile.isEmpty() || profile != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("profile_connect", "profile");
                            tab2.setArguments(bundle);
                        }
                        return tab2;
                    }
                default:
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ProfilePage();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
