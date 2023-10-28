package com.example.eventec.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventec.R;

import java.util.List;

public class CollabAdapter extends RecyclerView.Adapter<CollabAdapter.Viewholder> {

    private final Context context;
    private final List<CollabModel> collabModelArrayList;

    // Constructor
    public CollabAdapter(Context context, List<CollabModel> collabModelArrayList) {
        this.context = context;
        this.collabModelArrayList = collabModelArrayList;
    }

    @NonNull
    @Override
    public CollabAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_collab_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollabAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        CollabModel model = collabModelArrayList.get(position);

        holder.modImageIV.setImageResource(model.getProfileImage());
        holder.modNameTV.setText(model.getName());
        holder.jobTV.setText(model.getJob());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, model.getName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return collabModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView modImageIV;
        private final TextView modNameTV;
        private final TextView jobTV;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            modImageIV = itemView.findViewById(R.id.modImage);
            modNameTV = itemView.findViewById(R.id.modName);
            jobTV = itemView.findViewById(R.id.job);
        }
    }
}