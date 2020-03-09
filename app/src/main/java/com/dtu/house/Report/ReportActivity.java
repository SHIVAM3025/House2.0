package com.dtu.house.Report;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dtu.house.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

    ImageView mimage;
    EditText reason;
    Button button_report_m;
    ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mimage = findViewById(R.id.image);
        reason = findViewById(R.id.edit_reason);
        button_report_m = findViewById(R.id.button_report);

        mdialog = new ProgressDialog(this);
        mdialog.setTitle("WAIT");

        final Intent intent = getIntent();

        Glide.with(this)
                .load(intent.getStringExtra("Imageurl"))
                .into(mimage);

        button_report_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mdialog.setMessage("Checking ....");

                if (reason.getText().toString().equals("")) {
                    Toast.makeText(ReportActivity.this, "Reason field is Empty", Toast.LENGTH_SHORT).show();
                    mdialog.dismiss();

                } else {
                        String reason_fireld = reason.getText().toString();

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("TYPE", intent.getStringExtra("type"));
                    hashMap.put("PostKey", intent.getStringExtra("PostKey"));
                    hashMap.put("publisherId", intent.getStringExtra("publisher"));
                    hashMap.put("auth_of_sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    hashMap.put("Reason", reason_fireld);

                    mdialog.setMessage("Posting ... ");

                    FirebaseDatabase.getInstance().getReference("Report").push().setValue(hashMap);
                    Toast.makeText(ReportActivity.this, "Report Posted", Toast.LENGTH_SHORT).show();

                    mdialog.dismiss();
                    finish();
                }


            }
        });


    }
}
