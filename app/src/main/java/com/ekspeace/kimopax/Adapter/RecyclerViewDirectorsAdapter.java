package com.ekspeace.kimopax.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.kimopax.Model.Director;
import com.ekspeace.kimopax.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class RecyclerViewDirectorsAdapter extends RecyclerView.Adapter<RecyclerViewDirectorsAdapter.MyViewHolder> {
    private Context context;
    private List<Director> directors;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewDirectorsAdapter(Context context, List<Director> directors) {
        this.context = context;
        this.directors = directors;
    }

    @NonNull
    @Override
    public RecyclerViewDirectorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_director_recycler_view, parent, false);
        return new RecyclerViewDirectorsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDirectorsAdapter.MyViewHolder holder, int position) {
        Director director = directors.get(position);
        holder.tvDirectorTitle.setText(director.getTitle());
        holder.tvDirectorName.setText(director.getName());
        Picasso.get().load(director.getImage()).into(holder.ivDirectorImage);
    }

    @Override
    public int getItemCount() {
        return directors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView ivDirectorImage;
        private TextView tvDirectorName, tvDirectorTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDirectorImage = itemView.findViewById(R.id.director_image);
            tvDirectorName = itemView.findViewById(R.id.director_name);
            tvDirectorTitle = itemView.findViewById(R.id.director_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                int position = RecyclerViewDirectorsAdapter.MyViewHolder.this.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(RecyclerViewDirectorsAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}