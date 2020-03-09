package com.dtu.house;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Verification extends AppCompatActivity {

    private EditText mfull_name, memail, msoc_name, mphone_no;
    private TextView mattach;
    private ImageView mdisplay;
    private Button send_button;
    private Uri mImageUri;
    private String mimage, sender_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        mfull_name = findViewById(R.id.full_name);
        memail = findViewById(R.id.email_id);
        mattach = findViewById(R.id.text_proof);
        mdisplay = findViewById(R.id.imageView);
        send_button = findViewById(R.id.send_button);
        msoc_name = findViewById(R.id.soc_name_edit);
        mphone_no = findViewById(R.id.phone_no_edit);

        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mimage = preferences.getString("image", "");
        sender_name = preferences.getString("first_name", "");


        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(mfull_name.getText().toString()) && !TextUtils.isEmpty(memail.getText().toString()) && !TextUtils.isEmpty(msoc_name.getText().toString())
                        && !TextUtils.isEmpty(mphone_no.getText().toString())) {


                    final ProgressDialog mdialog = new ProgressDialog(Verification.this);
                    mdialog.setCancelable(false);
                    mdialog.setTitle("WAIT");
                    mdialog.setMessage("Checking....");
                    mdialog.show();

                    if (mImageUri == null) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("full_name", mfull_name.getText().toString());
                        hashMap.put("society_email_id", memail.getText().toString());
                        //hashMap.put("document", uri.toString());
                        hashMap.put("soc_name", msoc_name.getText().toString());
                        hashMap.put("phone_no", mphone_no.getText().toString());
                        hashMap.put("person_image", mimage);
                        hashMap.put("sender_name", sender_name);
                        hashMap.put("status" , "not_verified");

                        mdialog.setMessage("Data Uploading...");


                        FirebaseFirestore.getInstance().collection("VERIFICATION").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(hashMap).
                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toasty.success(Verification.this, "SENT", Toasty.LENGTH_SHORT, false).show();
                                        mdialog.dismiss();
                                        finish();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(Verification.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        File compressorFile = null;
                        File compressFilePath = new File(mImageUri.getPath());
                        try {
                            compressorFile = new Compressor(Verification.this)
                                    .setQuality(40)
                                    .compressToFile(compressFilePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        mdialog.setMessage("Image Uploading...");

                        final StorageReference thumbFilePath = FirebaseStorage.getInstance().getReference("Verification").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(System.currentTimeMillis()
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

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("full_name", mfull_name.getText().toString());
                                hashMap.put("society_email_id", memail.getText().toString());
                                hashMap.put("document", uri.toString());
                                hashMap.put("soc_name", msoc_name.getText().toString());
                                hashMap.put("phone_no", mphone_no.getText().toString());
                                hashMap.put("person_image", mimage);
                                hashMap.put("sender_name", sender_name);
                                hashMap.put("status" , "not_verified");

                                mdialog.setMessage("Data Uploading...");


                                FirebaseFirestore.getInstance().collection("VERIFICATION").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(hashMap).
                                        addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toasty.success(Verification.this, "SENT", Toasty.LENGTH_SHORT, false).show();
                                                mdialog.dismiss();
                                                finish();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(Verification.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });

                    }


                } else {
                    Toasty.error(Verification.this, "Enter Details", Toasty.LENGTH_SHORT, false).show();
                }


            }
        });

        mattach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFileChooser();


            }
        });


    }

    public void openFileChooser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(Verification.this, Manifest.permission.READ_EXTERNAL_STORAGE)) != (PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions((Activity) Verification.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Verification.this);
            }
        } else {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(Verification.this);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                mdisplay.setVisibility(View.VISIBLE);
                Picasso.with(this).load(mImageUri).into(mdisplay);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Retry" + error, Toast.LENGTH_SHORT).show();


            }
        }


    }
}
