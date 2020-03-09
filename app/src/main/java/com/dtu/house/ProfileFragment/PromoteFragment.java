package com.dtu.house.ProfileFragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dtu.house.Adapter.Profile_Promote_Adapter;
import com.dtu.house.Model.Upload_Personal_promote;
import com.dtu.house.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromoteFragment extends Fragment {

    View v;
    RecyclerView mrecycler;
    SwipeRefreshLayout mswipe;
    private LinearLayoutManager mlinear;
    private List<Upload_Personal_promote> mUploads;
    private Profile_Promote_Adapter madapter;
    private DatabaseReference mDatabaseRef;
    private RelativeLayout mimage;
    private ProgressBar mprogress;

    private int currentItems, scrollOutItems, totalItems;

    private Boolean isScrolling = false;

    private String LASTKEY;

    private ValueEventListener mlistener;

    private List<Upload_Personal_promote> mTemporary;


    public PromoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =inflater.inflate(R.layout.fragment_promote2, container, false);
        mrecycler = v.findViewById(R.id.recycler_view);
        mswipe = v.findViewById(R.id.swipe_refresh_layout);
        mprogress  = v.findViewById(R.id.ProgressBar);
        mrecycler.setHasFixedSize(true);
        mlinear = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinear);

        mimage = v.findViewById(R.id.promote_relative_layout);

        mUploads = new ArrayList<>();
        mTemporary = new ArrayList<>();

        madapter = new Profile_Promote_Adapter(getContext() , mUploads);

        mrecycler.setItemViewCacheSize(10000);
        mrecycler.setDrawingCacheEnabled(true);
        mrecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mrecycler.setDrawingCacheEnabled(true);

        mrecycler.setAdapter(madapter);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("promotion");
        mDatabaseRef.keepSynced(true);

        FillTheArray();

        mswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FillTheArray();

                mswipe.setRefreshing(false);

            }


        });


        return v;
    }

    @Override
    public void onDestroy() {

        mDatabaseRef.removeEventListener(mlistener);

        super.onDestroy();
    }

    private void FillTheArray() {

        mlistener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();

                for(DataSnapshot postSnapshot  : dataSnapshot.getChildren())
                {
                    Upload_Personal_promote upload = postSnapshot.getValue(Upload_Personal_promote.class);
                    upload.setMkey(postSnapshot.getKey());
                    mUploads.add(upload);

                }

                if(mUploads.size() == 0)
                {

                    mprogress.setVisibility(View.GONE);
                    mimage.setVisibility(View.VISIBLE);

                }else {
                    mprogress.setVisibility(View.GONE);
                    mimage.setVisibility(View.GONE);

                }
                Collections.reverse(mUploads);

                madapter.notifyDataSetChanged();

                mswipe.setRefreshing(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

              //  Toast.makeText(getContext() , databaseError.getMessage() , Toast.LENGTH_SHORT).show();


            }
        });

    }

}
