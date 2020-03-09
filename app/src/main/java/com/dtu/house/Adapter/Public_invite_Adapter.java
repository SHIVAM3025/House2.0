package com.dtu.house.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dtu.house.Model.Upload_invitation;
import com.dtu.house.R;
import com.dtu.house.Show.Show_invitation_activity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

public class Public_invite_Adapter extends RecyclerView.Adapter<Public_invite_Adapter.HoldView> {

    private Context mcontext;
    private List<Upload_invitation> mUploads;

    public Public_invite_Adapter(Context mcontext, List<Upload_invitation> mUploads) {
        this.mcontext = mcontext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public HoldView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.invitation_layout, parent, false);
        return new Public_invite_Adapter.HoldView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final  HoldView holder, int position) {

        final Upload_invitation uploadCurrent = mUploads.get(position);

        Glide.with(mcontext)
                .load(uploadCurrent.getImageUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mcircle);

        holder.musername.setText(uploadCurrent.getMname());
        holder.memail.setText(uploadCurrent.getMheading());

        Glide.with(mcontext)
                .load(uploadCurrent.getMimage())
                .into(holder.mimage);


        holder.mimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent  intent  = new Intent(mcontext , Show_invitation_activity.class);
                intent.putExtra("mdate",uploadCurrent.getMdate());
                intent.putExtra("mtime",uploadCurrent.getMtime());
                intent.putExtra("mvenue", uploadCurrent.getMvenue());
                intent.putExtra("mhost",uploadCurrent.getMhost());
                intent.putExtra("key" , uploadCurrent.getMkey());
                intent.putExtra("pImage",uploadCurrent.getImageUrl());
                intent.putExtra("mimage", uploadCurrent.getMimage());
                intent.putExtra("publisher",uploadCurrent.getPublisherId());
                intent.putExtra("musername" ,uploadCurrent.getMname());
                intent.putExtra("mheading",uploadCurrent.getMheading());
                intent.putExtra("mdesc",uploadCurrent.getMdesc());
                intent.putExtra("mpre",uploadCurrent.getMpre());
                intent.putExtra("mbenefits" , uploadCurrent.getMbenefits());
                intent.putExtra("time_in_millis" , uploadCurrent.getTime_inmillis());

                mcontext.startActivity(intent);

            }
        });


        if(uploadCurrent.isVerification())
        {
            holder.mverified.setVisibility(View.VISIBLE);

        }else
        {
            holder.mverified.setVisibility(View.GONE);
        }





    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class HoldView extends RecyclerView.ViewHolder{

        CircleImageView mcircle;
        TextView musername;
        TextView memail;
        ImageView mimage , mverified;
        LinearLayout mlinear;


        public HoldView(@NonNull View itemView) {
            super(itemView);
            mcircle = itemView.findViewById(R.id.circle_image);
            musername = itemView.findViewById(R.id.username);
            memail = itemView.findViewById(R.id.email);
            mimage = itemView.findViewById(R.id.image_view);
            mverified = itemView.findViewById(R.id.verfify_tick);
            mlinear = itemView.findViewById(R.id.linear_layout);
        }

    }

}
