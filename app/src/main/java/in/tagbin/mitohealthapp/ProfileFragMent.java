package in.tagbin.mitohealthapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class ProfileFragMent extends Fragment implements TabLayout.OnTabSelectedListener {

    private TabLayout tablayout;
    private ViewPager vPager;
    TextView tabOne,tabTwo;

    ProfilePagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ProfileView = inflater.inflate(R.layout.fragment_profile_frag_ment, container, false);

        tablayout = (TabLayout) ProfileView.findViewById(R.id.ProfileTabs);
        vPager = (ViewPager) ProfileView.findViewById(R.id.ProfilePager);
//<<<<<<< HEAD
//        adapter = new ProfilePagerAdapter(getActivity().getSupportFragmentManager());
//        vPager.setAdapter(adapter);
//        tablayout.setupWithViewPager(vPager);
//
//        vPager.setCurrentItem(0,true);
//=======
        if ( getArguments() !=null && getArguments().getString("profile_connect") != null){
            adapter = new ProfilePagerAdapter(getActivity().getSupportFragmentManager(),"profile_connect");
            vPager.setAdapter(adapter);
            tablayout.setupWithViewPager(vPager);

            vPager.setCurrentItem(1,true);
        }else{
            adapter = new ProfilePagerAdapter(getActivity().getSupportFragmentManager(),"");
            vPager.setAdapter(adapter);
            tablayout.setupWithViewPager(vPager);

            vPager.setCurrentItem(0,true);
        }
//>>>>>>> 740f03d59aa7e79487e8fa2b64a4aea0e65f5865
        tablayout.setOnTabSelectedListener(this);
        tablayout.setTabsFromPagerAdapter(adapter);
        vPager.setOffscreenPageLimit(2);

        setupTab();


        return ProfileView;
    }
    private void setupTab() {
        try {
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab, tablayout, false);

            tabOne = (TextView) relativeLayout.findViewById(R.id.tabtext);
            tabOne.setText("Health");
            tabOne.setTextColor(Color.parseColor("#4c516d"));
            tabOne.setGravity(Gravity.CENTER);
            tablayout.getTabAt(0).setCustomView(relativeLayout);

            RelativeLayout relativeLayout1 = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab, tablayout, false);


            tabTwo = (TextView) relativeLayout1.findViewById(R.id.tabtext);
            tabTwo.setText("Partner Connect");
            tabTwo.setTextColor(Color.parseColor("#4c516d"));
            tabTwo.setGravity(Gravity.CENTER);

            tablayout.getTabAt(1).setCustomView(relativeLayout1);


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
