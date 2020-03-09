package com.dtu.house.Explore_fragment;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dtu.house.Adapter.Public_invite_Adapter;
import com.dtu.house.MainActivity;
import com.dtu.house.Model.Upload_invitation;
import com.dtu.house.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Invite_fragment extends Fragment {

    View v;
    RecyclerView mrecycler;
    SwipeRefreshLayout mswipe;
    private LinearLayoutManager mlinear;
    private List<Upload_invitation> mUploads;
    private Public_invite_Adapter madapter;
    private DatabaseReference mDatabaseRef;
    private ProgressBar mprogress;

    private Boolean allow = false;

    private int currentItems, scrollOutItems, totalItems;

    private Boolean isScrolling = false;

    private String LASTKEY;

    private List<Upload_invitation> mTemporary;

    public MainActivity activity;


    public Invite_fragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v =inflater.inflate(R.layout.fragment_invite_fragment, container, false);
        mrecycler = v.findViewById(R.id.invite_recycler_view);
        mswipe = v.findViewById(R.id.swipe_refresh_layout);
        mprogress = v.findViewById(R.id.progress_bar);

        mrecycler.setHasFixedSize(true);
        mlinear = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinear);

        mUploads = new ArrayList<>();
        mTemporary = new ArrayList<>();

        madapter = new Public_invite_Adapter(getContext(), mUploads);


        mrecycler.setItemViewCacheSize(10000);
        mrecycler.setDrawingCacheEnabled(true);
        mrecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mrecycler.setAdapter(madapter);
        mswipe.setEnabled(false);



        mDatabaseRef = FirebaseDatabase.getInstance().getReference("invitation");
        mDatabaseRef.keepSynced(true);

        mUploads.clear();
        madapter.notifyDataSetChanged();
        allow = true;
        FillTheArray();

        mswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                allow = true;
                FillTheArray();


            }
        });




        mrecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = mlinear.getChildCount();
                totalItems = mlinear.getItemCount();
                scrollOutItems = mlinear.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems) && dy>0) {

                    isScrolling = false;

                    LASTKEY = mUploads.get(mUploads.size() - 1).getMkey();

                    addnewArray(LASTKEY);
                }

            }
        });


        return v;

    }

    private void addnewArray(String lastkey) {

        mTemporary.clear();
        mprogress.setVisibility(View.VISIBLE);
        mDatabaseRef.orderByChild("mkey").endAt(lastkey).limitToLast(7).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload_invitation upload = postSnapshot.getValue(Upload_invitation.class);

                    upload.setMkey(postSnapshot.getKey());
                    mTemporary.add(upload);
                }

                Collections.reverse(mTemporary);

                mTemporary.remove(0);
                mUploads.addAll(mTemporary);
                madapter.notifyDataSetChanged();

                mprogress.setVisibility(View.GONE);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void FillTheArray()
    {

        mUploads.clear();
        madapter.notifyDataSetChanged();

        mDatabaseRef.limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload_invitation upload = postSnapshot.getValue(Upload_invitation.class);

                    upload.setMkey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                Collections.reverse(mUploads);

                madapter.notifyDataSetChanged();

                mswipe.setRefreshing(false);

                mprogress.setVisibility(View.GONE);
                mswipe.setEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
             //   Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                //TODO check for context
            }


        });


    }


}
