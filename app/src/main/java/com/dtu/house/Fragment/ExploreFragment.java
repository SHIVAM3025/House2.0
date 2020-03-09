package com.dtu.house.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.dtu.house.Explore_fragment.Invite_fragment;
import com.dtu.house.Explore_fragment.Promote_Fragment;
import com.dtu.house.R;
import com.google.android.material.tabs.TabLayout;


public class ExploreFragment extends Fragment {

    View v;
    TabLayout tablayout;
    ViewPager mview;
    Toolbar mtoolbar;


    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_explore, container, false);
        tablayout = v.findViewById(R.id.tabs);
        mview = v.findViewById(R.id.container);
        mtoolbar = v.findViewById(R.id.only_toolbar);
        mview.setAdapter(new PagerAdapter(getFragmentManager()));
        mview.addOnPageChangeListener((new TabLayout.TabLayoutOnPageChangeListener(tablayout)));
        tablayout.setupWithViewPager(mview);
        tablayout.setTabGravity(tablayout.GRAVITY_FILL);
        return v;
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {


        private String[] tabTitels = new String[]{"  INVITE  " , "  PROMOTE  "};

        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitels[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Invite_fragment();
                case 1:
                    return new Promote_Fragment();

                default:
                    return null;
            }


        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
