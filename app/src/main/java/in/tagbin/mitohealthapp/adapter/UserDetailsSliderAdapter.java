package in.tagbin.mitohealthapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.tagbin.mitohealthapp.Fragments.HealthFragment;
import in.tagbin.mitohealthapp.Fragments.PartnerConnectFragment;

/**
 * Created by Chetan on 11/08/16.
 */

public class UserDetailsSliderAdapter extends FragmentStatePagerAdapter {
    String profile;
    Context mContext;

    public UserDetailsSliderAdapter(FragmentManager fm, String profile_connect, Context pContext) {
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
                    HealthFragment tab1=new HealthFragment();
                    return tab1;
                case 1:

                        PartnerConnectFragment tab2 = new PartnerConnectFragment();
                        if (!profile.equals("") || !profile.isEmpty() || profile != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("profile_connect", "profile");
                            tab2.setArguments(bundle);
                        }
                        return tab2;
                default:
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new HealthFragment();
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
