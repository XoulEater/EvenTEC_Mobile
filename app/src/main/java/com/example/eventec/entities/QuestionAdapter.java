package com.example.eventec.entities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventec.R;
import com.example.eventec.activities.Event;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.Viewholder> {
    private final Context context;

    private final ArrayList<QuestionModel> questionModelArrayList;

    public ArrayList<QuestionModel> getQuestionModelArrayList() {
        return questionModelArrayList;
    }

    public QuestionAdapter(Context context, ArrayList<QuestionModel> questionModelArrayList) {
        this.context = context;
        this.questionModelArrayList = questionModelArrayList;
    }

    @NonNull
    @Override
    public QuestionAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
        return new QuestionAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        QuestionModel model = questionModelArrayList.get(position);

        holder.activityTitle.setText(model.getTitle());
        holder.activityQuestion.setText(model.getQuestion());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, Event.class);
//
//                // Pasa los datos del evento a la actividad EventActivity
//                intent.putExtra("index", position);
//                // Añade más extras según sea necesario
//
//                // Inicia la actividad
//                context.startActivity(intent);
//            }
//        });
    }

    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return questionModelArrayList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView activityTitle;
        private final TextView activityQuestion;
        private final Spinner ratings_spinner;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            activityTitle = itemView.findViewById(R.id.activityTitle);
            activityQuestion = itemView.findViewById(R.id.activityQuestion);
            ratings_spinner = itemView.findViewById(R.id.ratings_spinner);

            // Create an ArrayAdapter using the string array and a default spinner layout.
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    activityQuestion.getContext(),
                    R.array.ratings_array,
                    android.R.layout.simple_spinner_item
            );
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            ratings_spinner.setAdapter(adapter);
            ratings_spinner.setSelection(2);
        }
    }


}
