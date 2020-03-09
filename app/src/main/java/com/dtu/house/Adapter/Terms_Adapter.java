package com.dtu.house.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.house.Model.Upload_terms;
import com.dtu.house.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Terms_Adapter extends  RecyclerView.Adapter<Terms_Adapter.ViewHold> {


    Context mContext;
    List<Upload_terms> mUploads;

    public Terms_Adapter(Context mContext, List<Upload_terms> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
    }

    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.terms_layout, parent, false);
        return new Terms_Adapter.ViewHold(v);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {

        Upload_terms Terms = mUploads.get(position);
        holder.mhead.setText(Terms.getHeading());
        holder.mdesc.setText(Terms.getDesc());

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHold extends RecyclerView.ViewHolder
    {
        TextView mhead , mdesc;


        public ViewHold(@NonNull View itemView) {
            super(itemView);

            mhead = itemView.findViewById(R.id.heading);
            mdesc  =itemView.findViewById(R.id.Desc);

        }
    }

}
