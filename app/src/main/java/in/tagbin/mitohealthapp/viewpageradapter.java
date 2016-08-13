package in.tagbin.mitohealthapp;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class viewpageradapter extends FragmentStatePagerAdapter {

    public viewpageradapter(FragmentManager fm) {
        super(fm);

    }


    @Override
    public Fragment getItem(int position) {
        try {

            Fragment fm = null;

            switch (position) {
                case 0:
                    Explorefrag tab1=new Explorefrag();
                    return tab1;
                case 1:
                    Lookupfrag tab2 = new Lookupfrag();
                    return tab2;
                case 2:
                    Chatfrag tab3=new Chatfrag();
                    return tab3;
                default:
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Lookupfrag();
    }

    @Override
    public int getCount() {
        return 3;
    }

}