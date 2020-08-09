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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dtu.house.Model.Upload_invitation;
import com.dtu.house.Model.Upload_my_events;
import com.dtu.house.R;
import com.dtu.house.Report.ReportActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Show_invitation_activity extends AppCompatActivity {


    private TextView mdate , mtime , mvenue , mhost;
    private ImageView mimage;
    private CircleImageView mprofile;
    private TextView musername ;
    private TextView mheading;
    private TextView mdesc , mpre ,mbenefits;
    private ImageView mshare , mreport;
    private SmallBangView mticket;
    private ImageView mparent;
    private TextView mlikes;
    private TextView mfont_text;
    private Toolbar mtoolbar;
    private long time_in_millis;

    private ValueEventListener mlistener;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invitation_activity);

        mdate = findViewById(R.id.date_of_event);
        mtime = findViewById(R.id.time);
        mvenue = findViewById(R.id.venue);
        mhost = findViewById(R.id.hosted_promo);
        mshare = findViewById(R.id.share);
        mreport = findViewById(R.id.report);
        mimage = findViewById(R.id.event_image);
        mprofile = findViewById(R.id.profile_image);
        musername = findViewById(R.id.username);
        mheading = findViewById(R.id.heading);
        mdesc = findViewById(R.id.desc_invite);
        mfont_text = findViewById(R.id.font_text);
        mpre = findViewById(R.id.pre_desc);
        mlikes = findViewById(R.id.Likes_hi);
        mbenefits = findViewById(R.id.bene_desc_invite);
        mticket =  findViewById(R.id.ticket_small);
        mtoolbar = findViewById(R.id.new_toolbar);
        mtoolbar.setVisibility(View.GONE);
        mparent = findViewById(R.id.parent);

        final Intent intent = getIntent();

        Glide.with(this)
                .load(intent.getStringExtra("mimage"))
                .into(mimage);

        mparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        mdate.setText(intent.getStringExtra("mdate"));
        mtime.setText(intent.getStringExtra("mtime"));
        mvenue.setText(intent.getStringExtra("mvenue"));
        String mmhost = "Hosted By : " + intent.getStringExtra("mhost");
        mhost.setText(mmhost);
        Typeface myCustom  = Typeface.createFromAsset(getAssets() , "fonts/book_an_evnt.ttf");
        mfont_text.setTypeface(myCustom);
        key = intent.getStringExtra("key");
        musername.setText(intent.getStringExtra("musername"));
        mheading.setText(intent.getStringExtra("mheading").trim());
        mdesc.setText(intent.getStringExtra("mdesc"));
        mpre.setText(intent.getStringExtra("mpre"));
        mbenefits.setText(intent.getStringExtra("mbenefits"));

        time_in_millis = intent.getLongExtra("time_in_millis" , 10);

       mlistener = FirebaseDatabase.getInstance().getReference("invitation").orderByChild("mkey").equalTo(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot post : dataSnapshot.getChildren())
                {
                    Upload_invitation upload =  post.getValue(Upload_invitation.class);

                     mlikes.setText( " "+ upload.getGoing() + " GOING");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent kk = new Intent(Show_invitation_activity.this  , ImageEnlarge.class);
                kk.putExtra("imageurl",intent.getStringExtra("mimage"));
                startActivity(kk);

            }
        });


       Glide.with(this)
               .load(intent.getStringExtra("pImage"))
                .apply(RequestOptions.circleCropTransform())
               .into(mprofile);


       mticket.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(mticket.isSelected())
               {

                   mfont_text.setText("Book An Event");
                   mticket.setSelected(false);
                   Snackbar.make(view , "Removed from Booked Events" , Snackbar.LENGTH_LONG).show();
                   FirebaseMessaging.getInstance().unsubscribeFromTopic(intent.getStringExtra("publisher"));
                   FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                   FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(intent.getStringExtra("key")).removeValue();
               }else
               {
                   mfont_text.setText("Click To Remove");
                   mticket.setSelected(true);
                   Snackbar.make(view , "Added To Booked Events" , Snackbar.LENGTH_LONG).show();

                   if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(intent.getStringExtra("publisher"))){
                   FirebaseMessaging.getInstance().subscribeToTopic(intent.getStringExtra("publisher"));
                       FirebaseMessaging.getInstance().subscribeToTopic(key);}

                   Upload_my_events notification = new Upload_my_events(intent.getStringExtra("publisher"),mheading.getText().toString() ,intent.getStringExtra("mimage") ,mvenue.getText().toString() ,mdate.getText().toString() ,mtime.getText().toString() ,mdesc.getText().toString() , time_in_millis );
                   FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(intent.getStringExtra("key")).setValue(notification);
               }


           }
       });

       mtoolbar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               if(mticket.isSelected())
               {
                   mfont_text.setText("Book An Event");
                   mticket.setSelected(false);
                   Snackbar.make(view , "Removed from Booked Events" , Snackbar.LENGTH_LONG).show();
                   FirebaseMessaging.getInstance().unsubscribeFromTopic(intent.getStringExtra("publisher"));
                   FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(intent.getStringExtra("key")).removeValue();
               }else
               {
                   mfont_text.setText("Click To Remove");
                   mticket.setSelected(true);
                   Snackbar.make(view , "Added To Booked Events" , Snackbar.LENGTH_LONG).show();
                   FirebaseMessaging.getInstance().subscribeToTopic(intent.getStringExtra("publisher"));
                   Upload_my_events notification = new Upload_my_events(intent.getStringExtra("publisher"),mheading.getText().toString() ,intent.getStringExtra("mimage") ,mvenue.getText().toString() ,mdate.getText().toString() ,mtime.getText().toString() ,mdesc.getText().toString() ,time_in_millis );
                   FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(intent.getStringExtra("key")).setValue(notification);
               }


           }
       });



        mreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(Show_invitation_activity.this , ReportActivity.class);
                it.putExtra("Imageurl" ,intent.getStringExtra("mimage"));
                it.putExtra("PostKey" , intent.getStringExtra("key"));
                it.putExtra("publisher",intent.getStringExtra("publisher"));
                it.putExtra("type" ,"I");
                startActivity(it);
            }

        });

        mshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent send_intent = new Intent(Intent.ACTION_SEND);
                send_intent.setType("text/plain");
                String head = mheading.getText().toString() + "\n\n" + intent.getStringExtra("mimage") + "\n\n" ;
                String date = " Date : "  + mdate.getText().toString()  + "\n\n";
                String Time = "Time : "  + mtime.getText().toString() + "\n\n";
                String venue = "Venue : " + mvenue.getText().toString() +  "\n\n";
                String host =  mhost.getText().toString() + "\n\n" + "PREREQUISITES  :" + "\n";
                String pre = mpre.getText().toString() + "\n\n" + "DESCRIPTION  :" + "\n";
                String desc = mdesc.getText().toString() +  "\n\n" + "BENEFITS  :" + "\n";
                String Benefits = mbenefits.getText().toString();
                send_intent.putExtra(Intent.EXTRA_TEXT, head + date + Time + venue + host + pre + desc + Benefits + "\n\n");
                startActivity(Intent.createChooser(send_intent , "Share Using"));

            }
        });


        check(intent.getStringExtra("key") , mticket);




    }

    private void check(final String key , final SmallBangView mmmticket) {



        FirebaseDatabase.getInstance().getReference("Going").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               if(dataSnapshot.child(key).exists())
               {
                   mfont_text.setText("Click To Remove");
                   mmmticket.setSelected(true);
                   mtoolbar.setVisibility(View.VISIBLE);

               }else
               {
                   mfont_text.setText("Book An Event");
                   mmmticket.setSelected(false);
                   mtoolbar.setVisibility(View.VISIBLE);
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    protected void onDestroy() {

        FirebaseDatabase.getInstance().getReference("invitation").removeEventListener(mlistener);

        super.onDestroy();
    }
}
