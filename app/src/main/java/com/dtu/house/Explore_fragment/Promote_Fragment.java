package com.dtu.house.Explore_fragment;


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

import com.dtu.house.Adapter.Public_promote_Adapter;
import com.dtu.house.Model.Upload_promo;
import com.dtu.house.R;
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
public class Promote_Fragment extends Fragment {

    RecyclerView mrecycler;
    View v;
    private DatabaseReference mDatabseRef;
    private Public_promote_Adapter madapter;
    private List<Upload_promo> mUploads;
    private LinearLayoutManager mlinear;
    private SwipeRefreshLayout mswipe;
    private ProgressBar mprogress;

    private int currentItems, scrollOutItems, totalItems;

    private Boolean isScrolling = false;

    private String LASTKEY;

    private List<Upload_promo> mTemporary;

    private Boolean allow = false;

    public Promote_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_promote, container, false);
        mrecycler = v.findViewById(R.id.promo_recycler_view);
        mswipe = v.findViewById(R.id.swipe_refresh_layout);
        mprogress = v.findViewById(R.id.promo_progress_bar);

        mrecycler.setHasFixedSize(false);
        mlinear = new LinearLayoutManager(getContext());
        mrecycler.setLayoutManager(mlinear);

        mUploads = new ArrayList<>();

        madapter = new Public_promote_Adapter(getContext() , mUploads);

        mrecycler.setItemViewCacheSize(10000);
        mrecycler.setDrawingCacheEnabled(true);
        mrecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mrecycler.setDrawingCacheEnabled(true);

        mrecycler.setAdapter(madapter);

        mDatabseRef = FirebaseDatabase.getInstance().getReference("promotion");
        mDatabseRef.keepSynced(true);

        mTemporary = new ArrayList<>();
       // firstTimerun();

        mUploads.clear();
        madapter.notifyDataSetChanged();
        allow = true;
        FillTheArray();

        mswipe.setEnabled(false);

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

        mDatabseRef.orderByChild("mkey").endAt(lastkey).limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload_promo upload = postSnapshot.getValue(Upload_promo.class);

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


    private void FillTheArray() {

        mUploads.clear();
        madapter.notifyDataSetChanged();

        mDatabseRef.limitToLast(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload_promo upload = postSnapshot.getValue(Upload_promo.class);

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
               // Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                //TODO check for event
            }
        });

    }


}
