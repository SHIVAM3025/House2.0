package com.dtu.house.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dtu.house.Model.Upload_my_events;
import com.dtu.house.R;
import com.dtu.house.Show.ShowInvitationActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class My_events_Adapter extends RecyclerView.Adapter<My_events_Adapter.HoldView> {

    private Context mcontext;
    private List<Upload_my_events> mUploads;

    public My_events_Adapter(Context mcontext, List<Upload_my_events> mUploads) {
        this.mcontext = mcontext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public HoldView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.notification_layout , parent , false);
        return new My_events_Adapter.HoldView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HoldView holder, int position) {
        final Upload_my_events uploadCurrent = mUploads.get(position);
        Glide.with(mcontext)
                .load(uploadCurrent.getmImageurl())
                .into(holder.mevent_image);

        holder.mheading.setText(uploadCurrent.getHeading());
        holder.mdesc.setText(uploadCurrent.getMdesc());
        holder.mdate.setText(uploadCurrent.getMdate());
        holder.mtime.setText(uploadCurrent.getMtime());
        holder.mvenue.setText(uploadCurrent.getMvenue());

        holder.materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext , ShowInvitationActivity.class);
                intent.putExtra("postKey",uploadCurrent.getKey());
                intent.putExtra("mImageUrl",uploadCurrent.getmImageurl());
                intent.putExtra("mheading",uploadCurrent.getHeading());
                intent.putExtra("mdesc",uploadCurrent.getMdesc());
                intent.putExtra("mvenue",uploadCurrent.getMvenue());
                intent.putExtra("mtimeinmillis",uploadCurrent.getTime_in_millis());
                intent.putExtra("mAuth" , uploadCurrent.getmAuth());

                mcontext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class HoldView extends RecyclerView.ViewHolder{

        ImageView mevent_image;
        TextView mheading , mdesc , mvenue , mtime , mdate;
        MaterialCardView materialCardView;

        public HoldView(@NonNull View itemView) {
            super(itemView);

            mevent_image = itemView.findViewById(R.id.image_of_event);
            mheading = itemView.findViewById(R.id.heading);
            mdesc = itemView.findViewById(R.id.description);
            mvenue = itemView.findViewById(R.id.venue);
            mdate = itemView.findViewById(R.id.date);
            mtime = itemView.findViewById(R.id.time);
            materialCardView = itemView.findViewById(R.id.card_view);

        }


    }

}
