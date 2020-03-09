package com.dtu.house.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dtu.house.Model.Upload_promo;
import com.dtu.house.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.senab.photoview.PhotoViewAttacher;
import xyz.hanks.library.bang.SmallBangView;

public class Public_promote_Adapter  extends RecyclerView.Adapter<Public_promote_Adapter.HoldView> {

    private Context context;
    private List<Upload_promo> mUploads;

    public Public_promote_Adapter(Context context, List<Upload_promo> mUploads) {
        this.context = context;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public HoldView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.public_promote_layout , parent , false);
        return new Public_promote_Adapter.HoldView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HoldView holder, int position) {

        final Upload_promo uploadCurrent = mUploads.get(position);



        Glide.with(context)
                .load(uploadCurrent.getmImage())
                .into(holder.mimage);

        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(holder.mimage , true);
        photoViewAttacher.update();

        Glide.with(context)
                .load(uploadCurrent.getImageUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.mcircle);

        holder.musername.setText(uploadCurrent.getMname());
        holder.mtitle.setText(uploadCurrent.getMtitle());

        isLikes(uploadCurrent.getPublisherId() , uploadCurrent.getMkey() , holder.msmall);

        holder.mshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent send_intent = new Intent(Intent.ACTION_SEND);
                send_intent.setType("text/plain");
                String title = uploadCurrent.getMtitle() + "\n\n";
                String mimage_link = uploadCurrent.getmImage() + "\n\n";
                String mdesc = uploadCurrent.getMdesc() +  "\n\n";
                send_intent.putExtra(Intent.EXTRA_TEXT, title + mimage_link + mdesc + "\n\n");
                context.startActivity(Intent.createChooser(send_intent , "Share Using"));

            }
        });

        holder.mdesc.setText(uploadCurrent.getMdesc());



        holder.msmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.msmall.isSelected())
                {
                    holder.msmall.setSelected(false);
                    FirebaseDatabase.getInstance().getReference().child("Likes/promotion").child(uploadCurrent.getPublisherId()).child(uploadCurrent.getMkey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();


                }else
                {

                    holder.msmall.likeAnimation();
                    holder.msmall.setSelected(true);
                    FirebaseDatabase.getInstance().getReference().child("Likes/promotion").child(uploadCurrent.getPublisherId()).child(uploadCurrent.getMkey()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                }

            }
        });



    }


    private void isLikes(final String key,final String mkey, final SmallBangView mimageView) {

        FirebaseDatabase.getInstance().getReference().child("Likes/promotion").child(key).child(mkey)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists())
                {

                    mimageView.setSelected(true);
                }else

                {
                    mimageView.setSelected(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

        @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class HoldView extends RecyclerView.ViewHolder{

        CircleImageView mcircle;
        TextView musername;
        TextView mtitle;
        ImageView mimage;
        ImageView mshare;
        SmallBangView msmall;
        ReadMoreTextView mdesc;


        public HoldView(@NonNull View itemView) {
            super(itemView);

            mcircle = (CircleImageView) itemView.findViewById(R.id.circle_image);
            musername = (TextView) itemView.findViewById(R.id.username);
            mtitle = (TextView) itemView.findViewById(R.id.title);
            mimage = (ImageView) itemView.findViewById(R.id.image_view);
            mshare = (ImageView) itemView.findViewById(R.id.share);
            msmall = (SmallBangView) itemView.findViewById(R.id.heart_small);
            mdesc = (ReadMoreTextView) itemView.findViewById(R.id.description);


        }
    }

}
