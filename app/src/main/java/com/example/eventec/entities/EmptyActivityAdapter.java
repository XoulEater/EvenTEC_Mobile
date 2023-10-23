package com.example.eventec.entities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventec.R;
import com.example.eventec.activities.Event;

import java.util.ArrayList;

public class EmptyActivityAdapter extends RecyclerView.Adapter<EmptyActivityAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<ActivityModel> activities;

    // Constructor
    public EmptyActivityAdapter(Context context, ArrayList<ActivityModel> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public EmptyActivityAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_activity_layout, parent, false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull EmptyActivityAdapter.Viewholder holder, int position) {
        ActivityModel model = activities.get(position);
        // to set data to editText of each card layout
        holder.dateET.setText(model.getDate());
        holder.timeET.setText(model.getTime());
        holder.titleET.setText(model.getTitle());
        holder.modET.setText(model.getModer());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return activities.size();
    }

    public ArrayList<ActivityModel> getActivities() {
        return activities;
    }

    public void addActivity(ActivityModel activity) {
        activities.add(activity);
        notifyItemInserted(activities.size() - 1);
    }

    public void removeActivity() {
        int position = activities.size() - 1;
        if (position >= 0 && position < activities.size()) {
            activities.remove(position);
            notifyItemRemoved(position);
        }
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final EditText dateET;
        private final EditText timeET;
        private final EditText titleET;
        private final EditText modET;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            dateET = itemView.findViewById(R.id.date);
            timeET = itemView.findViewById(R.id.time);
            titleET = itemView.findViewById(R.id.title);
            modET = itemView.findViewById(R.id.mod);

        }

    }
}