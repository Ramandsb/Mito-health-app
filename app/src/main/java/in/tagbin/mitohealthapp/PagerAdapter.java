package in.tagbin.mitohealthapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.tagbin.mitohealthapp.Fragments.FoodDetailsFrag;
import in.tagbin.mitohealthapp.Fragments.RecipeDetailsFrag;

/**
 * Created by hp on 8/19/2016.
 */
public class PagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FoodDetailsFrag tab1 = new FoodDetailsFrag();
                return tab1;
            case 1:
                RecipeDetailsFrag tab2 = new RecipeDetailsFrag();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}