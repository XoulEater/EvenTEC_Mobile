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

import java.util.ArrayList;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<AlertModel> alertModelArrayList;

    // Constructor
    public AlertAdapter(Context context, ArrayList<AlertModel> alertModelArrayList) {
        this.context = context;
        this.alertModelArrayList = alertModelArrayList;
    }

    @NonNull
    @Override
    public AlertAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        AlertModel model = alertModelArrayList.get(position);
        switch (model.getAlertImage()){
            case 1:
                holder.alertImageIV.setImageResource(R.drawable.full);
                break;
            default:
                holder.alertImageIV.setImageResource(R.drawable.cancel);
        }
        holder.subjectTV.setText(model.getSubject());
        holder.bodyTV.setText(model.getBody());
        holder.dateTV.setText(model.getPostdate());

        holder.itemView.setOnClickListener(v -> Toast.makeText(context, "You clicked on " + model.getSubject(), Toast.LENGTH_SHORT).show());
    }
    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return alertModelArrayList.size();
    }

    // update list
    public void updateList(ArrayList<AlertModel> newList){
        alertModelArrayList.clear();
        alertModelArrayList.addAll(newList);
        notifyDataSetChanged();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView alertImageIV;
        private final TextView subjectTV;
        private final TextView bodyTV;
        private final TextView dateTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            alertImageIV = itemView.findViewById(R.id.alertImage);
            subjectTV = itemView.findViewById(R.id.subject);
            bodyTV = itemView.findViewById(R.id.body);
            dateTV = itemView.findViewById(R.id.date);
        }
    }
}