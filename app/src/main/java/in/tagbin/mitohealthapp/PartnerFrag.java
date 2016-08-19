package in.tagbin.mitohealthapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by aasaqt on 13/8/16.
 */
public class PartnerFrag extends Fragment implements TabLayout.OnTabSelectedListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView tabOne,tabTwo,tabThree;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("PREPDUG","Setting options true");
        BinderActivity i = (BinderActivity) getActivity();
        setHasOptionsMenu(true);
        i.invalidateOptionsMenu();

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.d("par cone", "hereHOME");
        for (int i=0;i< menu.size();i++) {
            MenuItem itm = menu.getItem(i);
            itm.setVisible(false);
        }
//        menu.findItem(R.id.action_done).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
//                .setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.partnerfrag,container,false);
        tabLayout = (TabLayout) layout.findViewById(R.id.tabLayout);

        viewPager = (ViewPager) layout.findViewById(R.id.pager);
        viewpageradapter adapter = new viewpageradapter(getActivity().getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager.setCurrentItem(1,true);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setOffscreenPageLimit(3);

        setupTab();
        return layout;
    }
    private void setupTab() {
        try {
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab, tabLayout, false);


            relativeLayout.setGravity(RelativeLayout.CENTER_HORIZONTAL);

            tabOne = (TextView) relativeLayout.findViewById(R.id.tabtext);
            tabOne.setText("Explore");
            tabOne.setGravity(Gravity.CENTER);
            tabLayout.getTabAt(0).setCustomView(relativeLayout);

            RelativeLayout relativeLayout1 = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab, tabLayout, false);


            tabTwo = (TextView) relativeLayout1.findViewById(R.id.tabtext);
            tabTwo.setText("Lookup");
            tabTwo.setGravity(Gravity.CENTER);

            tabLayout.getTabAt(1).setCustomView(relativeLayout1);

            RelativeLayout relativeLayout2 = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab, tabLayout, false);
            tabThree = (TextView) relativeLayout2.findViewById(R.id.tabtext);

            tabThree.setText("Chat");
            tabThree.setGravity(Gravity.CENTER);
            tabLayout.getTabAt(2).setCustomView(relativeLayout2);




        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
