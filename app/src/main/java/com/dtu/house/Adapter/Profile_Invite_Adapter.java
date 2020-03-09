package com.dtu.house.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dtu.house.Model.Upload_Personal_Invite;
import com.dtu.house.R;
import com.dtu.house.Show.Show_profile_invite_edit_activity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Profile_Invite_Adapter extends  RecyclerView.Adapter<Profile_Invite_Adapter.Hold_View>{

    private Context mcontext;
    private List<Upload_Personal_Invite> mUploads;

    public Profile_Invite_Adapter(Context mcontext, List<Upload_Personal_Invite> mUploads) {
        this.mcontext = mcontext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public Hold_View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.invite_profile_layout , parent , false);
        return new Profile_Invite_Adapter.Hold_View(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Hold_View holder, int position) {

        final Upload_Personal_Invite uploadCurrent = mUploads.get(position);

        Glide.with(mcontext)
                .load(uploadCurrent.getmImageUrl())
                .into(holder.mimage);


        holder.medit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Edit_Feature

               // Toast.makeText(mcontext , uploadCurrent.getMkey() , Toast.LENGTH_LONG).show();


                Intent intent = new Intent(mcontext , Show_profile_invite_edit_activity.class);
                intent.putExtra("mkey",uploadCurrent.getMkey());
                mcontext.startActivity(intent);


            }
        });

        String going_value  =" "+ Integer.toString(uploadCurrent.getGoing()) +  " GOING";

        holder.mlikes.setText(going_value);


        holder.mheading.setText(uploadCurrent.getMheading());

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class Hold_View extends RecyclerView.ViewHolder
    {
            ImageView mimage;
            ImageView medit;
            LinearLayout mlinear;
            TextView mheading;
            TextView mlikes;

            public Hold_View(@NonNull View itemView) {
            super(itemView);

            mlinear = itemView.findViewById(R.id.linear_layout);
            mimage = itemView.findViewById(R.id.image_view);
            mlikes = itemView.findViewById(R.id.Likes);
            mheading = itemView.findViewById(R.id.heading_text);
            medit = itemView.findViewById(R.id.edit_pencil);

        }
    }
}
