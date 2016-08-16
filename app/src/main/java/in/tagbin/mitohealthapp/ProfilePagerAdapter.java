package in.tagbin.mitohealthapp;

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

    public ProfilePagerAdapter(FragmentManager fm,String profile_connect) {
        super(fm);
        this.profile = profile_connect;
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
                    PartProfile tab2 = new PartProfile();
                    if (!profile.equals("") && !profile.isEmpty() && profile != null){
                        Bundle bundle = new Bundle();
                        bundle.putString("profile_connect","profile");
                        tab2.setArguments(bundle);
                    }
                    return tab2;
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
