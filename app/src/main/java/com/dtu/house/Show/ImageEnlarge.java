package com.dtu.house.Show;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.dtu.house.R;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class ImageEnlarge extends AppCompatActivity {

    private Intent intent;
    private ImageView mimageView;
    private ProgressBar mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enlarge);

        mimageView = (ImageView) findViewById(R.id.imageView);
        mprogress = (ProgressBar) findViewById(R.id.progressBar);
        intent = getIntent();
        String imageUrl = intent.getStringExtra("imageurl");
            mprogress.setVisibility(View.VISIBLE);
        function(imageUrl , mimageView);




    }

    private void function(String imageUrl, ImageView mimageView) {
        Picasso.with(ImageEnlarge.this)
                .load(imageUrl)
                .placeholder(R.drawable.image_background)
                .fit()
                .into(mimageView);


    }
}
