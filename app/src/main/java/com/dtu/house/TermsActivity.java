package com.dtu.house;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dtu.house.Adapter.Terms_Adapter;
import com.dtu.house.Model.Upload_terms;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TermsActivity extends AppCompatActivity {


    RecyclerView mrecycler_View;
    private LinearLayoutManager mlinear;
    private List<Upload_terms> mUploads;
    private DatabaseReference mDatabaseRef;
    private Terms_Adapter adapetr;
    private ProgressBar mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);


        mrecycler_View = findViewById(R.id.recycler_view);

        mrecycler_View.setHasFixedSize(true);
        mlinear = new LinearLayoutManager(this);
        mrecycler_View.setLayoutManager(mlinear);
        mprogress = findViewById(R.id.progressBar);


        mUploads = new ArrayList<>();

        adapetr = new Terms_Adapter(this, mUploads);
        mrecycler_View.setAdapter(adapetr);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Terms");

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload_terms upload = postSnapshot.getValue(Upload_terms.class);
                    mUploads.add(upload);
                }
                adapetr.notifyDataSetChanged();

                mprogress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}

