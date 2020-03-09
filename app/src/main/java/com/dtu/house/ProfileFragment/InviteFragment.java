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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dtu.house.Adapter.Profile_Invite_Adapter;
import com.dtu.house.Model.Upload_Personal_Invite;
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
public class InviteFragment extends Fragment {

    View v;
    RecyclerView mrecycler;
    SwipeRefreshLayout mswipe;
    private LinearLayoutManager mlinear;
    private List<Upload_Personal_Invite> mUploads;
    private Profile_Invite_Adapter madapter;
    private DatabaseReference mDatabaseRef;
    private RelativeLayout image;
    private ValueEventListener mlistener;
    private ProgressBar mprogress;

    public InviteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_invite, container, false);
        mrecycler = v.findViewById(R.id.recycler_view);
        mswipe = v.findViewById(R.id.swipe_refresh_layout);
        image = v.findViewById(R.id.fragment_relative_layout);
        mprogress = v.findViewById(R.id.ProgressBar);
        mrecycler.setHasFixedSize(true);
        mlinear = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinear);

        mUploads  = new ArrayList<>();

        madapter = new Profile_Invite_Adapter(getContext()  , mUploads);
        FillTheArray();
        mrecycler.setAdapter(madapter);

        mrecycler.setItemViewCacheSize(10000);
        mrecycler.setDrawingCacheEnabled(true);
        mrecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mrecycler.setDrawingCacheEnabled(true);


        mswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FillTheArray();


            }
        });


        return v;
    }



   private  void FillTheArray()
    {

       mlistener = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    Upload_Personal_Invite upload  = postSnapshot.getValue(Upload_Personal_Invite.class);

                    upload.setMkey(postSnapshot.getKey());
                    mUploads.add(upload);

                }

                if(mUploads.size() == 0)
                {
                    mprogress.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);

                }else
                {
                    mprogress.setVisibility(View.GONE);
                    image.setVisibility(View.GONE);

                }
                Collections.reverse(mUploads);

                madapter.notifyDataSetChanged();


                mswipe.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            //    Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
