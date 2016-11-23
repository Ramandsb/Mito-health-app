package in.tagbin.mitohealthapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import in.tagbin.mitohealthapp.R;
import in.tagbin.mitohealthapp.activity.BinderActivity;

/**
 * Created by chetan on 23/11/16.
 */

public class UserChatFragment extends Fragment implements TabLayout.OnTabSelectedListener {
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
        View layout = inflater.inflate(R.layout.frag_user_connect,container,false);
        tabLayout = (TabLayout) layout.findViewById(R.id.tabLayout);
//        viewPager = (ViewPager) layout.findViewById(R.id.pager);
//        viewpageradapter lookupAdapter = new viewpageradapter(getActivity().getSupportFragmentManager());
//
//        viewPager.setAdapter(lookupAdapter);
        //tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

//        viewPager.setCurrentItem(1,true);
        tabLayout.setOnTabSelectedListener(this);
        //tabLayout.setTabsFromPagerAdapter(lookupAdapter);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //viewPager.setOffscreenPageLimit(3);

        setupTab();
        return layout;
    }
    private void setupTab() {
        try {
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.item_tabs, tabLayout, false);


            relativeLayout.setGravity(RelativeLayout.CENTER_HORIZONTAL);

            tabOne = (TextView) relativeLayout.findViewById(R.id.tabtext);
            tabOne.setText("MitoCoach");
            tabOne.setGravity(Gravity.CENTER);
            tabLayout.addTab(tabLayout.newTab().setCustomView(relativeLayout));
            //tabLayout.getTabAt(0).setCustomView(relativeLayout);

            RelativeLayout relativeLayout1 = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.item_tabs, tabLayout, false);


            tabTwo = (TextView) relativeLayout1.findViewById(R.id.tabtext);
            tabTwo.setText("Explore");
            tabTwo.setGravity(Gravity.CENTER);
            tabLayout.addTab(tabLayout.newTab().setCustomView(relativeLayout1),true);
            //tabLayout.getTabAt(1).setCustomView(relativeLayout1);

            RelativeLayout relativeLayout2 = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.item_tabs, tabLayout, false);
            tabThree = (TextView) relativeLayout2.findViewById(R.id.tabtext);

            tabThree.setText("Social");
            tabThree.setGravity(Gravity.CENTER);
            tabLayout.addTab(tabLayout.newTab().setCustomView(relativeLayout2));
            //tabLayout.getTabAt(2).setCustomView(relativeLayout2);




        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        setCurrentTabFragment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(new ChatDieticianFragment());
                break;
            case 1 :
//                Fragment fragment = new Lookupfragment();
//                if (getArguments() != null){
//                    if (getArguments().getString("activity_create_event") != null){
//                        Bundle bundle = new Bundle();
//                        bundle.putString("activity_create_event","activity_create_event");
//                        fragment.setArguments(bundle);
//                    }
//                }
                replaceFragment(new Chatfragment());
                break;
            case 2 :
                replaceFragment(new SocialChatFragment());
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.pager, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
