package com.dtu.house.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.house.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CalendarFragment extends Fragment {

    View v;

    ImageView left, right;
    TextView date_text;
    int day, month;
    RecyclerView mrecycler_view;
    Calendar c;
    private LinearLayoutManager mlinear;
    private DatabaseReference mDatabaseRef;
    private SwipeRefreshLayout mswipe;
    private String date_to_pass;
    private Boolean allow = false;


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         v = inflater.inflate(R.layout.fragment_calendar, container, false);

        left = v.findViewById(R.id.image_left);
        right = v.findViewById(R.id.image_right);
        date_text = v.findViewById(R.id.date_text_view);
        mrecycler_view = v.findViewById(R.id.recycler_view_hah);
        mswipe = v.findViewById(R.id.swipe_refresh_layout);

        mrecycler_view.setHasFixedSize(true);
        mlinear = new LinearLayoutManager(getContext());
        mrecycler_view.setLayoutManager(mlinear);
        //mrecycler_view.set;


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        c = Calendar.getInstance();
        text(0);

        mswipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allow = true;
                Fill_the_array(date_to_pass);
                mswipe.setRefreshing(false);

            }
        });


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text(-1);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text(1);
            }
        });




        return v;
    }


    public void text(int i) {

        allow = true;
        // mUploads.clear();
        //adapter.notifyDataSetChanged();

        int year;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        c.add(Calendar.DATE, i);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);
        String month_anme = monthName[month];
        String date = Integer.toString(day);
        String total = date + " " + month_anme;
        date_text.setText(total);

        // String currentDAte = DateFormat.getDateInstance().format(c.getTime());


        String currentDAte = month_anme + " " + day + ", " + year;
        date_to_pass  = currentDAte;
        Fill_the_array(currentDAte);

    }


    public void Fill_the_array(final String date_i) {
        //mUploads.clear();
        // adapter.notifyDataSetChanged();
        /*
        mUploads.clear();
        adapter.notifyDataSetChanged();
        mDatabaseRef.orderByChild("mDate").equalTo(date_i).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(allow) {

                    mUploads.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Upload_Calendar upload = postSnapshot.getValue(Upload_Calendar.class);

                        upload.setMkey(postSnapshot.getKey());
                        mUploads.add(upload);
                    }

                    Collections.reverse(mUploads);

                    adapter.notifyDataSetChanged();

                    mswipe.setRefreshing(false);
                }
                allow = false;

                // mrecycler_view.smoothScrollToPosition(mrecycler_view.getAdapter().getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

*/

    }





}
