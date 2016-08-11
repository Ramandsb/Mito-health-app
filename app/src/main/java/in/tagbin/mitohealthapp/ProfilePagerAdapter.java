package in.tagbin.mitohealthapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Chetan on 11/08/16.
 */

public class ProfilePagerAdapter extends FragmentPagerAdapter {

    private String frags[] = {"Health", "Partner Connect"};

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProfilePage();
            case 1:
                return new PartProfile();
            default:
                return new ProfilePage();
        }
    }

    @Override
    public int getCount() {
        return frags.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return frags[position];
    }
}
