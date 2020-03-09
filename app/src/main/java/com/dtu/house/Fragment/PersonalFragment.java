package com.dtu.house.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dtu.house.Adapter.My_events_Adapter;
import com.dtu.house.Model.Upload_my_events;
import com.dtu.house.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {

    private View v;
    private RecyclerView mrecyclerView;
    private RelativeLayout mrelative;
    private LinearLayoutManager mlinear;
    private List<Upload_my_events> mUploads;
    private My_events_Adapter madapter;
    private DatabaseReference mDatabaseref;
    private SwipeRefreshLayout mswipe;
    private ProgressBar mprogress;
    Calendar c;
    private String current;


    public PersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_personal, container, false);
        mrecyclerView = v.findViewById(R.id.recycler_view_my_events);
        mrelative  = v.findViewById(R.id.no_view_relative_layout);
        mrelative.setVisibility(View.GONE);
        mswipe = v.findViewById(R.id.swipe_refresh);
        mprogress  = v.findViewById(R.id.progressBar);
        mrecyclerView.setHasFixedSize(true);
        mlinear = new LinearLayoutManager(getContext());
        mrecyclerView.setLayoutManager(mlinear);

        mUploads = new ArrayList<>();


        int year, month, day;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        String [] mday = {"01","02","03","04","5","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        String mdater = mday[day - 1];

        year = c.get(Calendar.YEAR);
        String month_anme = monthName[month];
        current = month_anme + " " + mdater + ", " + year;

        madapter = new My_events_Adapter(getContext() , mUploads);

        mrecyclerView.setDrawingCacheEnabled(true);
        mrecyclerView.setItemViewCacheSize(1000);
        mrecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mrecyclerView.setAdapter(madapter);

        mDatabaseref = FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FillTheArray();
        mswipe.setEnabled(false);

        mswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FillTheArray();

            mswipe.setRefreshing(false);

            }
        });


        return v;
    }

    private void FillTheArray()
    {

        mprogress.setVisibility(View.VISIBLE);
        mDatabaseref.orderByChild("time_in_millis").startAt(c.getTimeInMillis()-1800000).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();

                for(DataSnapshot post : dataSnapshot.getChildren())
                {
                        Upload_my_events  event = post.getValue(Upload_my_events.class);
                        event.setKey(post.getKey());
                        mUploads.add(event);
                }

                madapter.notifyDataSetChanged();
                mswipe.setRefreshing(false);

                mswipe.setEnabled(true);

                mprogress.setVisibility(View.GONE);

                if(mUploads.size() == 0)
                {
                    mrelative.setVisibility(View.VISIBLE);

                }else
                {
                    mrelative.setVisibility(View.GONE);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

             //   Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
