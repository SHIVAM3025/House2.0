package com.dtu.house.Show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dtu.house.Model.Upload_invitation;
import com.dtu.house.Model.Upload_my_events;
import com.dtu.house.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.ref.ReferenceQueue;

public class ShowInvitationActivity extends AppCompatActivity {

    private Intent mintent;
    private String mpostId;
    private LinearLayout mmlinear;
    private ProgressBar mprogress;
    private DatabaseReference mDatabaseRef;
    private CircleImageView mcircle;
    private Toolbar mtoolbar;
    private SmallBangView mticket;
    private ImageView mparent;
    private TextView musername, mdate, mtime, mvenue, mhost , mgoing;
    private ImageView mimage, mshare;
    private String mmimageUrl;
    private TextView mcancel;
    private TextView mheading , mfont_text;
    private TextView mdesc_invite, mpre_desc, mbenefits_desc;
    private String Auth_of_sender;
    private String getImageUrl;
    private long time;

    private ValueEventListener mlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invitation);

        mintent = getIntent();
        mpostId = mintent.getStringExtra("postKey");
        mprogress = findViewById(R.id.progressBar);
        mcancel = findViewById(R.id.cancel_textView);
        mmlinear = findViewById(R.id.mlinear);
        mtoolbar = findViewById(R.id.new_toolbar);
        mmlinear.setVisibility(View.GONE);
        mcircle = findViewById(R.id.profile_image);
        mparent = findViewById(R.id.parent);
        musername = findViewById(R.id.username);
        mbenefits_desc = findViewById(R.id.bene_desc_invite);
        mdate = findViewById(R.id.date_of_event);
        mgoing = findViewById(R.id.Likes_hi);
        mtime = findViewById(R.id.time);
        mfont_text  = findViewById(R.id.font_text);
        mheading = findViewById(R.id.heading);
        mvenue = findViewById(R.id.venue);
        mpre_desc = findViewById(R.id.pre_desc);
        mimage = findViewById(R.id.event_image);
        mdesc_invite = findViewById(R.id.desc_invite);
        mhost = findViewById(R.id.hosted_promo);
        mshare = findViewById(R.id.share);
        mticket = findViewById(R.id.ticket_small);


        mparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        Typeface myCustom  = Typeface.createFromAsset(getAssets() , "fonts/book_an_evnt.ttf");
        mfont_text.setTypeface(myCustom);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("invitation");

       mlistener= mDatabaseRef.orderByChild("mkey").equalTo(mpostId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot post : dataSnapshot.getChildren()) {


                    Upload_invitation minvite = post.getValue(Upload_invitation.class);

                    mgoing.setText( " "+ minvite.getGoing() + " GOING");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       mdesc_invite.setText(" ");


        mDatabaseRef.orderByChild("mkey").equalTo(mpostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot post : dataSnapshot.getChildren()) {

                    Upload_invitation minvite = post.getValue(Upload_invitation.class);
                    mprogress.setVisibility(View.INVISIBLE);

                    mdate.setText(minvite.getMdate());
                    mtime.setText(minvite.getMtime());
                    mvenue.setText(minvite.getMvenue());
                    mhost.setText("Hosted By : " + minvite.getMhost());
                    getImageUrl = minvite.getMimage();

                    Glide.with(ShowInvitationActivity.this)
                            .load(minvite.getImageUrl())
                            .apply(RequestOptions.circleCropTransform())
                            .into(mcircle);

                    time = minvite.getTime_inmillis();

                    musername.setText(minvite.getMname());
                    mheading.setText(minvite.getMheading());

                    mmimageUrl = minvite.getMimage();

                    Glide.with(ShowInvitationActivity.this)
                            .load(minvite.getMimage())
                            .into(mimage);
                    Auth_of_sender = minvite.getMkey();

                    mdesc_invite.setText(minvite.getMdesc());
                    mpre_desc.setText(minvite.getMpre());
                    mbenefits_desc.setText(minvite.getMbenefits());
                    mmlinear.setVisibility(View.VISIBLE);

                    Upload_my_events notification = new Upload_my_events(mintent.getStringExtra("mAuth"),minvite.getMheading() ,getImageUrl ,minvite.getMvenue() ,minvite.getMdate() ,minvite.getMtime() ,minvite.getMdesc() ,minvite.getTime_inmillis());
                    FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mintent.getStringExtra("postKey")).setValue(notification);


                }

                if(mdesc_invite.getText().toString().equals(" "))
                {

                    mcancel.setVisibility(View.VISIBLE);
                    mprogress.setVisibility(View.INVISIBLE);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(mpostId);
                    FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mintent.getStringExtra("postKey")).removeValue();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                mprogress.setVisibility(View.INVISIBLE);
                Toast.makeText(ShowInvitationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });





        mticket.setSelected(true);

        mshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent send_intent = new Intent(Intent.ACTION_SEND);
                send_intent.setType("text/plain");
                String head = mheading.getText().toString() + "\n\n" + mmimageUrl + "\n";
                String date = " Date : " + mdate.getText().toString() + "\n";
                String Time = "Time : " + mtime.getText().toString() + "\n";
                String venue = "Venue : " + mvenue.getText().toString() + "\n";
                String host = mhost.getText().toString() + "\n\n" + "PREREQUISITES" + "\n";
                String pre = mpre_desc.getText().toString() + "\n" + "DESCRIPTION" + "\n";
                String desc = mdesc_invite.getText().toString() + "\n" + "BENEFITS" + "\n";
                String Benefits = mbenefits_desc.getText().toString();
                send_intent.putExtra(Intent.EXTRA_TEXT, head + date + Time + venue + host + pre + desc + Benefits + "\n\n");
                startActivity(Intent.createChooser(send_intent, "Share Using"));

            }
        });


        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowInvitationActivity.this, ImageEnlarge.class);
                intent.putExtra("imageurl", mmimageUrl);
                startActivity(intent);

            }
        });

        mtoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Removed from Booked Events", Snackbar.LENGTH_LONG).show();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Auth_of_sender);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mpostId);
                FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mintent.getStringExtra("postKey")).removeValue();
                mtoolbar.setVisibility(View.GONE);



            }
        });

        mticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Removed from Booked Events", Snackbar.LENGTH_LONG).show();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Auth_of_sender);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mpostId);
                FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(mintent.getStringExtra("postKey")).removeValue();
                mtoolbar.setVisibility(View.GONE);


            }
        });


    }

    @Override
    protected void onDestroy() {

        mDatabaseRef.removeEventListener(mlistener);
        super.onDestroy();
    }
}
