package com.dtu.house.Show;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dtu.house.Model.Upload_Personal_promote;
import com.dtu.house.Model.Upload_promo;
import com.dtu.house.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

public class Show_profile_edit_promote_activity extends AppCompatActivity {

    private TextView mpost;
    private EditText mheading;
    private EditText mdesc;
    private ImageView mimage, mdelete;
    private String mkey;
    private Uri mImage_uri;
    private String Image_string;
    private String mname , mprofile;
    private RelativeLayout mrelative;
    private Intent intent;
    private int no_of_likes;

    public Show_profile_edit_promote_activity() {

        //empty consructor

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_profile_edit_promote_activity);

        intent = getIntent();
        mkey = intent.getStringExtra("mkey");
        Image_string = intent.getStringExtra("imageUrl");

        String get_likes  =  intent.getStringExtra("no_of_likes");
        no_of_likes = Integer.parseInt(get_likes);
        mpost = findViewById(R.id.post_button_promo);
        mheading = findViewById(R.id.heading);
        mrelative  = findViewById(R.id.relative_click);
        mdesc = findViewById(R.id.desc_promo);
        mimage = findViewById(R.id.event_image_promo);
        mdelete = findViewById(R.id.delete_forever);

        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mname = preferences.getString("first_name", "");
        mprofile = preferences.getString("image", "");



        mdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Show_profile_edit_promote_activity.this);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete");




                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseDatabase.getInstance().getReference("promotion").child(mkey).removeValue();
                        FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("promotion").child(mkey).removeValue();
                        finish();

                    }
                });


                builder.show();


            }
        });


        mrelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFile();

            }
        });



        mpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(!TextUtils.isEmpty(mheading.getText().toString()) && !TextUtils.isEmpty(mdesc.getText().toString()) )
               {

                if(mImage_uri == null)
                {
                    Upload_promo promo = new Upload_promo(mheading.getText().toString() ,Image_string , mdesc.getText().toString() , mkey , FirebaseAuth.getInstance().getCurrentUser().getUid() , 0 , mname , mprofile);

                    Upload_Personal_promote mpersonal = new Upload_Personal_promote(Image_string, no_of_likes);

                    FirebaseDatabase.getInstance().getReference("promotion").child(mkey).setValue(promo);
                    FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("promotion").child(mkey).setValue(mpersonal);

                    finish();


                }else
                {
                    ProgressDialog mprogres  =new ProgressDialog(Show_profile_edit_promote_activity.this);
                    mprogres.setTitle("Editing");
                    mprogres.setCancelable(false);
                    mprogres.setMessage("ImageUploading");
                    mprogres.show();

                    File compressorFile = null;
                    File compressFilePath = new File(mImage_uri.getPath());
                    try {
                        compressorFile = new Compressor(Show_profile_edit_promote_activity.this)
                                .setQuality(55)
                                .compressToFile(compressFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final StorageReference thumbFilePath = FirebaseStorage.getInstance().getReference("Uploads").child("new Uploads").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(System.currentTimeMillis()
                            + ".jpg");

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


                            Upload_promo promo = new Upload_promo(mheading.getText().toString() ,uri.toString() , mdesc.getText().toString() , mkey , FirebaseAuth.getInstance().getCurrentUser().getUid() , 0 , mname , mprofile);

                            Upload_Personal_promote mpersonal = new Upload_Personal_promote(uri.toString(), no_of_likes);

                            FirebaseDatabase.getInstance().getReference("promotion").child(mkey).setValue(promo);
                            FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("promotion").child(mkey).setValue(mpersonal);

                            mprogres.dismiss();

                            Toasty.success(Show_profile_edit_promote_activity.this, "Posted", Toasty.LENGTH_SHORT, false).show();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toasty.error(Show_profile_edit_promote_activity.this, e.getMessage(), Toasty.LENGTH_SHORT, false).show();
                            mprogres.dismiss();

                        }
                    });



                }

               }else
               {

                   Toast.makeText(Show_profile_edit_promote_activity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();

               }


            }
        });

        FirebaseDatabase.getInstance().getReference("promotion").orderByChild("mkey").equalTo(mkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Upload_promo upload = post.getValue(Upload_promo.class);
                    mheading.setText(upload.getMtitle());
                    mdesc.setText(upload.getMdesc());
                    Glide.with(Show_profile_edit_promote_activity.this)
                            .load(upload.getmImage())
                            .into(mimage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.info(Show_profile_edit_promote_activity.this, "Connection Error", Toasty.LENGTH_SHORT, true).show();
            }
        });


    }


    public void openFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(Show_profile_edit_promote_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) != (PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions((Activity) Show_profile_edit_promote_activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(Show_profile_edit_promote_activity.this);
            }
        } else {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(Show_profile_edit_promote_activity.this);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImage_uri = result.getUri();
                Picasso.with(this).load(mImage_uri).into(mimage);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Retry" + error, Toast.LENGTH_SHORT).show();

            }
        }


    }
}
