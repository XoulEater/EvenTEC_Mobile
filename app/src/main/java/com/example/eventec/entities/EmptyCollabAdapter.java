package com.example.eventec.entities;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventec.R;

import java.util.ArrayList;

public class EmptyCollabAdapter extends RecyclerView.Adapter<EmptyCollabAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<CollabModel> collabs;

    // Constructor
    public EmptyCollabAdapter(Context context, ArrayList<CollabModel> collabs) {
        this.context = context;
        this.collabs = collabs;
    }

    @NonNull
    @Override
    public EmptyCollabAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_collab_layout, parent, false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull EmptyCollabAdapter.Viewholder holder, int position) {
        CollabModel model = collabs.get(position);
        // to set data to editText of each card layout
        holder.nameET.setText(model.getName());
        holder.jobET.setText(model.getJob());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return collabs.size();
    }

    public ArrayList<CollabModel> getCollabs() {
        return collabs;
    }

    public void addCollab(CollabModel collab) {
        collabs.add(collab);
        notifyItemInserted(collabs.size() - 1);
    }

    public void removeCollab() {
        int position = collabs.size() - 1;
        if (position >= 0 && position < collabs.size()) {
            collabs.remove(position);
            notifyItemRemoved(position);
        }
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final EditText nameET;
        private final EditText jobET;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nameET = itemView.findViewById(R.id.name);
            jobET = itemView.findViewById(R.id.job);
        }

    }
}