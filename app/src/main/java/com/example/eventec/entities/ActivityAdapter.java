package com.example.eventec.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventec.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.Viewholder> {

    private final Context context;
    private final List<ActivityModel> activityModelArrayList;

    // Constructor
    public ActivityAdapter(Context context, List<ActivityModel> activityModelArrayList) {
        this.context = context;
        this.activityModelArrayList = activityModelArrayList;
    }

    @NonNull
    @Override
    public ActivityAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_activity_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        ActivityModel model = activityModelArrayList.get(position);

        holder.dateTV.setText(model.getDate());
        holder.timeTV.setText(model.getTime());
        holder.actTitleTV.setText(model.getTitle());
        holder.modTV.setText(model.getModer());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, model.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return activityModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {

        private final TextView dateTV;
        private final TextView timeTV;
        private final TextView actTitleTV;
        private final TextView modTV;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            dateTV = itemView.findViewById(R.id.date);
            timeTV = itemView.findViewById(R.id.time);
            actTitleTV = itemView.findViewById(R.id.actTitle);
            modTV = itemView.findViewById(R.id.mod);
        }
    }
}