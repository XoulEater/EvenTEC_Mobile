package com.example.eventec.entities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventec.R;

import java.util.ArrayList;
import java.util.List;

// Clase para los elementos del RecyclerView de comentarios
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Viewholder> {

    private final Context context;

    private final List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new CommentAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.Viewholder holder, int position) {
        // to set data to textview of each card layout
        Comment model = commentList.get(position);

        holder.commentText.setText(model.getComment());
        holder.userInfoText.setText(model.getUserInfo());
        holder.fechaText.setText(model.getTimestamp());

    }

    public int getItemCount() {
        // this method is used for showing number of card items in recycler view
        return commentList.size();
    }

    // View holder class for initializing of your views such as TextView and Imageview
    public static class Viewholder extends RecyclerView.ViewHolder {
        private final TextView commentText;
        private final TextView userInfoText;
        private final TextView fechaText;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment);
            userInfoText = itemView.findViewById(R.id.userInfo);
            fechaText = itemView.findViewById(R.id.fecha);
        }
    }
}
