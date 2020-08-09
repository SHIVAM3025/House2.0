package com.dtu.house;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dtu.house.Model.Upload_Personal_Invite;
import com.dtu.house.Model.Upload_Personal_promote;
import com.dtu.house.Model.Upload_invitation;
import com.dtu.house.Model.Upload_promo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public class PostActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private static String all = null;
    private static Uri mImageUri, promo_uri;
    private static ImageView event_image, pevent_image;
    private static Boolean verification_variable;
    private static String mname, mprofile;
    private TabLayout mtabs;
    private static Calendar time_millis;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mtabs = findViewById(R.id.result_tabs);
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mtabs.setupWithViewPager(mViewPager);
        mtabs.setTabGravity(TabLayout.GRAVITY_FILL);
        time_millis = Calendar.getInstance();


        SharedPreferences preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mname = preferences.getString("first_name", "");
        mprofile = preferences.getString("image", "");


    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        private static TextView mdate;
        private static TextView mtime;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            int number_to_check = getArguments().getInt(ARG_SECTION_NUMBER);

            if (number_to_check == 1) {
                View rootView = inflater.inflate(R.layout.fragment_invitation, container, false);
                mdate = rootView.findViewById(R.id.date_of_event);
                mtime = rootView.findViewById(R.id.time);


                final EditText mheading = rootView.findViewById(R.id.heading);
                final EditText mpre = rootView.findViewById(R.id.pre_desc);
                final EditText mvenue = rootView.findViewById(R.id.venue);
                final EditText mhost = rootView.findViewById(R.id.hosted_promo);
                event_image = rootView.findViewById(R.id.event_image);
                final EditText mdesc = rootView.findViewById(R.id.desc_invite);
                final EditText mbenefits = rootView.findViewById(R.id.bene_desc_invite);
                final TextView mpost_button = rootView.findViewById(R.id.post_button_invite);
                final TextView mverification = rootView.findViewById(R.id.text_verification);
                final Toolbar toolbar_verification = rootView.findViewById(R.id.verification);

                toolbar_verification.setVisibility(View.GONE);


                FirebaseDatabase.getInstance().getReference("verify").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                            mverification.setText("Verified");
                            verification_variable = true;
                            toolbar_verification.setVisibility(View.VISIBLE);
                        } else {
                            mverification.setText("Not Verified");
                            verification_variable = false;
                            toolbar_verification.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                mpost_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final ProgressDialog mdialog = new ProgressDialog(getContext());
                        mdialog.setCancelable(false);
                        mdialog.setTitle("WAIT");
                        mdialog.setMessage("Checking....");
                        mdialog.show();

                        if (!TextUtils.isEmpty(mtime.getText().toString()

                        ) && !TextUtils.isEmpty(mdate.getText().toString()) && !TextUtils.isEmpty(mheading.getText().toString()) && !TextUtils.isEmpty(mpre.getText().toString()) && !TextUtils.isEmpty(mdesc.getText().toString()) && !TextUtils.isEmpty(mbenefits.getText().toString()) && mImageUri != null &&
                                !TextUtils.isEmpty(mvenue.getText().toString()) && !TextUtils.isEmpty(mhost.getText().toString())) {


                            mdialog.setMessage("Posting....");

                            File compressorFile = null;
                            File compressFilePath = new File(mImageUri.getPath());
                            try {
                                compressorFile = new Compressor(getContext())
                                        .setQuality(40)
                                        .compressToFile(compressFilePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            final StorageReference thumbFilePath = FirebaseStorage.getInstance().getReference("Uploads").child("new Uploads").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(System.currentTimeMillis()
                                    + ".jpg");


                            Glide.with(getContext())
                                    .asBitmap()
                                    .load(Uri.fromFile(compressorFile))
                                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            event_image.setImageBitmap(resource);
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

                                    String uploadId = FirebaseDatabase.getInstance().getReference("invitation").push().getKey();

                                    Upload_invitation invite = new Upload_invitation(mdate.getText().toString(), mtime.getText().toString(), mvenue.getText().toString(), mhost.getText().toString()
                                            , mheading.getText().toString(), mpre.getText().toString(), mdesc.getText().toString(), mbenefits.getText().toString()
                                            , FirebaseAuth.getInstance().getCurrentUser().getUid(), uri.toString(), uploadId, 0, mname, mprofile, time_millis.getTimeInMillis(), verification_variable);

                                    Upload_Personal_Invite minvite = new Upload_Personal_Invite(uri.toString(), 0, mheading.getText().toString());

                                    FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("invitation").child(uploadId).setValue(minvite);


                                    FirebaseDatabase.getInstance().getReference("invitation").child(uploadId).setValue(invite);

                                    mdialog.dismiss();
                                    Toasty.success(getContext(), "Posted", Toasty.LENGTH_SHORT, false).show();
                                    mdate.setText("");
                                    mtime.setText("");
                                    mheading.setText("");
                                    mpre.setText("");
                                    mvenue.setText("");
                                    mhost.setText("");
                                    mdesc.setText("");
                                    mbenefits.setText("");
                                    Glide.with(getContext())
                                            .load(R.drawable.image_background)
                                            .into(event_image);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    mdialog.dismiss();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                        } else {

                            mdialog.dismiss();
                            Toasty.error(getContext(), "Enter Details", Toasty.LENGTH_SHORT, false).show();

                        }

                    }
                });

                event_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        all = "invitation";

                        openFileChooser(3, 2);

                    }
                });


                mtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogFragment timepicker = new SelectTimeFragment();
                        timepicker.show(getFragmentManager(), "Time Picker");

                    }
                });


                mdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogFragment datePicker = new SelectDateFragement();
                        datePicker.show(getFragmentManager(), "date picker");


                    }
                });


                return rootView;


            } else {

                View rootView = inflater.inflate(R.layout.fragment_promotion, container, false);

                TextView mpost = rootView.findViewById(R.id.post_button_promo);
                final EditText mtitle = rootView.findViewById(R.id.heading);
                pevent_image = rootView.findViewById(R.id.event_image_promo);
                final EditText mdesc = rootView.findViewById(R.id.desc_promo);
                RelativeLayout mrel = rootView.findViewById(R.id.relative_click);

                pevent_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        all = "promotion";

                        openFile();

                    }
                });

                mrel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        all = "promotion";

                        openFile();

                    }
                });


                mpost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final ProgressDialog mdialog = new ProgressDialog(getContext());
                        mdialog.setCancelable(false);
                        mdialog.setTitle("WAIT");
                        mdialog.setMessage("Checking....");
                        mdialog.show();

                        if (!TextUtils.isEmpty(mtitle.getText().toString()) && !TextUtils.isEmpty(mdesc.getText().toString()) && promo_uri != null) {

                            File compressorFile = null;
                            File compressFilePath = new File(promo_uri.getPath());
                            try {
                                compressorFile = new Compressor(getContext())
                                        .setQuality(60)
                                        .compressToFile(compressFilePath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            mdialog.setMessage("Posting ....");

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

                                    String uploadId = FirebaseDatabase.getInstance().getReference("promotion").push().getKey();

                                    Upload_promo promo = new Upload_promo(mtitle.getText().toString(), uri.toString(), mdesc.getText().toString(), uploadId, FirebaseAuth.getInstance().getCurrentUser().getUid(), 0, mname, mprofile);

                                    Upload_Personal_promote mpersonal = new Upload_Personal_promote(uri.toString(), 0);

                                    FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("promotion").child(uploadId).setValue(mpersonal);

                                    FirebaseDatabase.getInstance().getReference("promotion").child(uploadId).setValue(promo);

                                    mdialog.dismiss();
                                    Toasty.success(getContext(), "Posted", Toasty.LENGTH_SHORT, false).show();
                                    mtitle.setText("");
                                    mdesc.setText("");
                                        Glide.with(getContext())
                                                .load(R.drawable.image_background)
                                                .into(pevent_image);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    mdialog.dismiss();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                        } else {
                            mdialog.dismiss();
                            Toasty.error(getContext(), "Enter Details", Toasty.LENGTH_SHORT, false).show();
                        }


                    }
                });


                return rootView;
            }


        }


        public void openFile() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) != (PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start((Activity) getContext());
                }
            } else {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start((Activity) getContext());
            }


        }


        public void openFileChooser(int a, int b) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) != (PackageManager.PERMISSION_GRANTED)) {

                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(a, b)
                            .start((Activity) getContext());
                }
            } else {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(a, b)
                        .start((Activity) getContext());
            }


        }


        public static class SelectDateFragement extends DialogFragment implements DatePickerDialog.OnDateSetListener {

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int date_of_month = c.get(Calendar.DAY_OF_MONTH);
            private String date;

            @NonNull
            @Override
            public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
                return new DatePickerDialog(getActivity(), this, year, month, date_of_month);
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
                time_millis.set(Calendar.YEAR, i);
                time_millis.set(Calendar.MONTH, i1);
                time_millis.set(Calendar.DAY_OF_MONTH, i2);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                year = c.get(Calendar.YEAR);
                String month_anme = monthName[month];
                String current = month_anme + " " + day + ", " + year;
                mdate.setText(current);


            }


        }

        public static class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


            @NonNull
            @Override
            public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
            }

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                Calendar cat;
                cat = Calendar.getInstance();
                cat.set(Calendar.HOUR_OF_DAY, i);
                cat.set(Calendar.MINUTE, i1);
                time_millis.set(Calendar.HOUR_OF_DAY, i);
                time_millis.set(Calendar.MINUTE, i1);
                java.text.DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String formattedTime = dateFormat.format(cat.getTimeInMillis());
                mtime.setText(formattedTime);


            }
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = {"INVITATION", "PROMOTION"};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (all.equals("invitation")) {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {

                    mImageUri = result.getUri();
                    Picasso.with(this).load(mImageUri).into(event_image);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "Retry" + error, Toast.LENGTH_SHORT).show();


                }
            }
        }

        if (all.equals("promotion")) {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {

                    promo_uri = result.getUri();
                    Picasso.with(this).load(promo_uri).into(pevent_image);


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "Retry" + error, Toast.LENGTH_SHORT).show();

                }
            }


        }
    }


}

