package com.dtu.house.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dtu.house.Model.Upload_Personal_promote;
import com.dtu.house.R;
import com.dtu.house.Show.Show_profile_edit_promote_activity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Profile_Promote_Adapter extends RecyclerView.Adapter<Profile_Promote_Adapter.HoldView> {

    private Context mcontext;
    private List<Upload_Personal_promote> mUploads;

    public Profile_Promote_Adapter(Context mcontext, List<Upload_Personal_promote> mUploads) {
        this.mcontext = mcontext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public HoldView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.personal_invite_layout , parent , false);
        return new Profile_Promote_Adapter.HoldView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HoldView holder, int position) {

        final Upload_Personal_promote uploadCurrent = mUploads.get(position);

        Glide.with(mcontext)
                .load(uploadCurrent.getmImageUrl())
                .into(holder.mimage);

        holder.mlikes.setText(" "+uploadCurrent.getLike() + " Likes");

        holder.medit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(mcontext , Show_profile_edit_promote_activity.class);
                intent.putExtra("mkey" , uploadCurrent.getMkey());
                intent.putExtra("imageUrl" , uploadCurrent.getmImageUrl());
                intent.putExtra("no_of_likes" , Integer.toString(uploadCurrent.getLike()));
                mcontext.startActivity(intent);


           }
       });



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class HoldView extends RecyclerView.ViewHolder
    {

        ImageView mimage;
        TextView mlikes;
        ImageView medit;

        public HoldView(@NonNull View itemView) {
            super(itemView);

            medit = itemView.findViewById(R.id.edit_pencil);
            mimage = itemView.findViewById(R.id.image_view);
            mlikes = itemView.findViewById(R.id.Likes);

        }
    }

}
