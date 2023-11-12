package com.example.eventec.entities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventec.R;
import com.example.eventec.activities.Event;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Viewholder> {

    private final Context context;
    private final ArrayList<EventModel> eventModelArrayList;

    // Constructor
    public EventAdapter(Context context, ArrayList<EventModel> eventModelArrayList) {
        this.context = context;
        this.eventModelArrayList = eventModelArrayList;
    }

    @NonNull
    @Override
    public EventAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        EventModel model = eventModelArrayList.get(position);

        holder.eventImageIV.setImageResource(R.drawable.no_image);
        holder.eventTitleTV.setText(model.getTitulo());
        holder.dateTV.setText(model.getDate());
        holder.capacityTV.setText("" + model.getCapacidad());
        holder.asoNameTV.setText(model.getNombreAsociacion());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Event.class);

                // Pasa los datos del evento a la actividad EventActivity
                intent.putExtra("index", position);
                // Añade más extras según sea necesario

                // Inicia la actividad
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return eventModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView eventImageIV;
        private final TextView eventTitleTV;
        private final TextView dateTV;
        private final TextView capacityTV;
        private final TextView asoNameTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            eventImageIV = itemView.findViewById(R.id.eventImage);
            eventTitleTV = itemView.findViewById(R.id.eventTitle);
            dateTV = itemView.findViewById(R.id.date);
            capacityTV = itemView.findViewById(R.id.capacity);
            asoNameTV = itemView.findViewById(R.id.asoName);
        }
    }
}