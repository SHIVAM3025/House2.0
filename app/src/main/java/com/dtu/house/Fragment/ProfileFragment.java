package com.dtu.house.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dtu.house.Explore_fragment.Invite_fragment;
import com.dtu.house.MainActivity;
import com.dtu.house.PostActivity;
import com.dtu.house.ProfileFragment.InviteFragment;
import com.dtu.house.ProfileFragment.PromoteFragment;
import com.dtu.house.ProfileSettingActivity;
import com.dtu.house.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    View v;
    ImageView setting;
    FloatingActionButton mpost;

    TextView mname, mroll_no;
    ImageView mimage;
    TabLayout mtabs;
    ViewPager mview;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setting = v.findViewById(R.id.setting);
        mname = v.findViewById(R.id.name);
        mroll_no = v.findViewById(R.id.roll_no);
        mimage = v.findViewById(R.id.circular_image);
        mpost = v.findViewById(R.id.floating_button);
        mtabs = v.findViewById(R.id.result_tabs);
        mview = v.findViewById(R.id.view_pager);


        SharedPreferences preferences = getContext().getSharedPreferences("prefs", MODE_PRIVATE);


        mname.setText(preferences.getString("first_name", ""));
        mroll_no.setText(preferences.getString("email", ""));

        Glide.with(this)
                .load(preferences.getString("image", ""))
                .apply(RequestOptions.circleCropTransform())
                .into(mimage);

        mtabs.addTab(mtabs.newTab());
        mtabs.addTab(mtabs.newTab());
        mview.setAdapter(new mPagerAdapter(getFragmentManager() , mtabs.getTabCount()));
        mview.addOnPageChangeListener((new TabLayout.TabLayoutOnPageChangeListener(mtabs)));
        mtabs.setupWithViewPager(mview);
        mtabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mview.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setting.setEnabled(false);

                Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
                startActivity(intent);

                setting.setEnabled(true);

            }
        });





        return v;
    }

    public class mPagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        private String[] tabTitels = new String[]{"INVITE" , "PROMOTE"};

        public mPagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
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
                    return new InviteFragment();
                case 1:

                    return new PromoteFragment();
                default:
                    return null;
            }


        }

        @Override
        public int getCount() {
            return 2;
        }
    }





    @Override
    public void onStart() {
        super.onStart();


    }
}
