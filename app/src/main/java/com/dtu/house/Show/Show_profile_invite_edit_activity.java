package com.dtu.house.Show;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import com.dtu.house.Fragment.DatePickerFragment;
import com.dtu.house.Fragment.TimePickFragment;
import com.dtu.house.Model.Upload_Personal_Invite;
import com.dtu.house.Model.Upload_invitation;
import com.dtu.house.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Show_profile_invite_edit_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private Intent  mintent;
    private String mkey;
    private TextView mpost_button;
    private TextView mdate , mtime ;
    private EditText mvenue , mhost;
    private EditText pre_desc;
    private EditText mheading;
    private ImageView mclick;
    private ImageView mevent_image;
    private EditText mdesc;
    private EditText mbenefits;
    private Uri mImageUri;
    private String mImage = null;
    private Calendar c;
    private ImageView mparent;
    private int going;
    private String mname , mprofile;
    private ImageView mdelete;
    private long Time_in_millis;
    private Boolean mverification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post_activity);

        mintent = getIntent();
        mkey = mintent.getStringExtra("mkey");
        mparent = findViewById(R.id.parent);
        mpost_button = findViewById(R.id.post_button_invite);
        mdate = findViewById(R.id.date_of_event);
        mtime = findViewById(R.id.time);
        mvenue = findViewById(R.id.venue);
        mdelete = findViewById(R.id.mmdelete);
        mhost = findViewById(R.id.hosted_promo);
        mheading = findViewById(R.id.heading);
        pre_desc = findViewById(R.id.pre_desc);
        mclick = findViewById(R.id.click);
        mevent_image = findViewById(R.id.event_image);
        mdesc = findViewById(R.id.desc_invite);
        mbenefits = findViewById(R.id.bene_desc_invite);
        c= Calendar.getInstance();


        SharedPreferences preferences = getSharedPreferences("prefs"  , Context.MODE_PRIVATE);
        mname =  preferences.getString("first_name", "");
        mprofile = preferences.getString("image", "");

        mdelete.setVisibility(View.VISIBLE);

        mdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Show_profile_invite_edit_activity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to cancel the event");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                       // Toast.makeText(Show_profile_invite_edit_activity.this, "DELETE", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("invitation").child(mkey).removeValue();
                        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitation").child(mkey).removeValue();
                        finish();
                    }
                });

                builder.show();


            }
        });

        mparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        mdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment mfrag = new DatePickerFragment();
                mfrag.show(getSupportFragmentManager() , "Date Picker");

            }
        });

        mtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment mttime = new TimePickFragment();
                mttime.show(getSupportFragmentManager() , "time picker");

            }
        });



        mpost_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final ProgressDialog mdialog = new ProgressDialog(Show_profile_invite_edit_activity.this);
                mdialog.setCancelable(false);
                mdialog.setTitle("WAIT");
                mdialog.setMessage("Checking....");
                mdialog.show();

                if(mImage != null  && !TextUtils.isEmpty(mdate.getText().toString()) && !TextUtils.isEmpty(mtime.getText().toString())  && !TextUtils.isEmpty(mvenue.getText().toString()) && !TextUtils.isEmpty(mhost.getText().toString() )
                   && !TextUtils.isEmpty(mheading.getText().toString())  && !TextUtils.isEmpty(pre_desc.getText().toString()) && !TextUtils.isEmpty(mdesc.getText().toString()) && !TextUtils.isEmpty(mbenefits.getText().toString()) )
                {
                    Time_in_millis  =c.getTimeInMillis();

                    Upload_invitation invite = new Upload_invitation(mdate.getText().toString(), mtime.getText().toString(), mvenue.getText().toString(), mhost.getText().toString()
                            , mheading.getText().toString(), pre_desc.getText().toString(), mdesc.getText().toString(), mbenefits.getText().toString()
                            , FirebaseAuth.getInstance().getCurrentUser().getUid(), mImage, mkey, going, mname  ,mprofile , Time_in_millis ,mverification);

                    Upload_Personal_Invite minvite = new Upload_Personal_Invite(mImage ,going , mheading.getText().toString());

                    FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitation").child(mkey).setValue(minvite);


                    FirebaseDatabase.getInstance().getReference("invitation").child(mkey).setValue(invite);
                    mdialog.dismiss();
                    finish();
                }else
                { mdialog.dismiss();
                    Toasty.error(Show_profile_invite_edit_activity.this  , "Details Incompleted" , Toasty.LENGTH_SHORT).show();

                }


            }
        });

        FirebaseDatabase.getInstance().getReference("invitation").orderByChild("mkey").equalTo(mkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot post : dataSnapshot.getChildren())
                {
                    Upload_invitation  upload = post.getValue(Upload_invitation.class);
                    upload.setMkey(post.getKey());

                    Glide.with(Show_profile_invite_edit_activity.this)
                            .load(upload.getMimage())
                            .into(mevent_image);
                    mImage  = upload.getMimage();
                    mdate.setText("");
                    mtime.setText("");
                    mvenue.setText(upload.getMvenue());
                    mhost.setText( upload.getMhost());
                    mheading.setText(upload.getMheading());
                    pre_desc.setText(upload.getMpre());
                    mdesc.setText(upload.getMdesc());
                    mbenefits.setText(upload.getMbenefits());
                    going = upload.getGoing();
                    Time_in_millis = upload.getTime_inmillis();
                    mverification = upload.isVerification();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Show_profile_invite_edit_activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        FirebaseDatabase.getInstance().getReference("invitation").orderByChild("mkey").equalTo(mkey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot post : dataSnapshot.getChildren())
                {
                    Upload_invitation  upload = post.getValue(Upload_invitation.class);

                    going = upload.getGoing();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Show_profile_invite_edit_activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        mevent_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();


            }
        });



    }

    private void openFileChooser() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(Show_profile_invite_edit_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) != (PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions( Show_profile_invite_edit_activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(3,2)
                        .start(Show_profile_invite_edit_activity.this);
            }
        } else {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(3,2)
                    .start(Show_profile_invite_edit_activity.this);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mpost_button.setEnabled(false);
                mpost_button.setVisibility(View.VISIBLE);
                mpost_button.setText("Loading ...");

                mImageUri = result.getUri();

                File compressorFile = null;
                File compressFilePath = new File(mImageUri.getPath());
                try {
                    compressorFile = new Compressor(Show_profile_invite_edit_activity.this)
                            .setQuality(40)
                            .compressToFile(compressFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                final StorageReference thumbFilePath = FirebaseStorage.getInstance().getReference("Uploads").child("new Uploads").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(System.currentTimeMillis()
                        + ".jpg");

                Glide.with(Show_profile_invite_edit_activity.this)
                        .asBitmap()
                        .load(Uri.fromFile(compressorFile))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mevent_image.setImageBitmap(resource);
                            }
                        });


                thumbFilePath.putFile(Uri.fromFile(compressorFile)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return thumbFilePath.getDownloadUrl();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                       mImage = uri.toString();
                       mpost_button.setText("POST");
                       mpost_button.setEnabled(true);


                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Retry" + error, Toast.LENGTH_SHORT).show();
                mpost_button.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        int year, month, day;
        String[] monthName = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};

        c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);

        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        year = c.get(Calendar.YEAR);
        String month_anme = monthName[month];
        String currentDAte = month_anme + " " + day + ", " + year;
        mdate.setText(currentDAte);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        Calendar cat;
        cat = Calendar.getInstance();
        cat.set(Calendar.HOUR_OF_DAY, i);
        cat.set(Calendar.MINUTE, i1);

        java.text.DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = dateFormat.format(cat.getTimeInMillis());
        mtime.setText(formattedTime);


    }
}
