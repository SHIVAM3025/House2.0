package com.dtu.house;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.dtu.house.Adapter.Profile_Promote_Adapter;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileSettingActivity extends AppCompatActivity {

    ImageView mparent;
    TextView mterms;
    RelativeLayout feed, about, sign_out , mverify , rel_terms;
    TextView mfeed, mabout, msign_out , text_terms;
    TextView mverification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        mparent = (ImageView) findViewById(R.id.parent);
        mterms = (TextView) findViewById(R.id.terms);
        feed = (RelativeLayout) findViewById(R.id.relative_feedback);
        mfeed = (TextView) findViewById(R.id.text_feedback);
        about = (RelativeLayout) findViewById(R.id.relative_about);
        mabout = (TextView) findViewById(R.id.text_about);
        sign_out = (RelativeLayout) findViewById(R.id.relative_out);
        msign_out = (TextView) findViewById(R.id.text_out);
        mverification = (TextView) findViewById(R.id.text_verify);
        mverify = (RelativeLayout) findViewById(R.id.relative_verify);
        rel_terms = (RelativeLayout) findViewById(R.id.relative_terms);
        text_terms = (TextView) findViewById(R.id.text_terms);


        rel_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileSettingActivity.this , TermsActivity.class);
                startActivity(intent);

            }
        });

        text_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileSettingActivity.this , TermsActivity.class);
                startActivity(intent);

            }
        });


        mverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileSettingActivity.this , Verification.class);
                startActivity(intent);

            }
        });

        mverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileSettingActivity.this , Verification.class);
                startActivity(intent);

            }
        });


        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("first_name", "");
                editor.putString("email", "");
                editor.putString("image", "");
                editor.commit();

                LoginManager.getInstance().logOut();
                Intent intent = new Intent(ProfileSettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();


            }
        });

        msign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();

                SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("first_name", "");
                editor.putString("email", "");
                editor.putString("image", "");
                editor.commit();

                LoginManager.getInstance().logOut();
                Intent intent = new Intent(ProfileSettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileSettingActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });


        mabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileSettingActivity.this, AboutActivity.class);
                startActivity(intent);

            }
        });

        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] recipient = {"shivamgupta1887@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipient);
                intent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack For House \n");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Email Using"));

            }
        });

        mfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] recipient = {"shivamgupta1887@gmail.com"};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipient);
                intent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack For House \n");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Email Using"));
            }
        });

        mparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        mterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(ProfileSettingActivity.this, TermsActivity.class);
                startActivity(intent);

            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            finish();

        }

    }
}
