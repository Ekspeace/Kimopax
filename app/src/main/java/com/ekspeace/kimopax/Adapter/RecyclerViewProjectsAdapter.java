package com.ekspeace.kimopax.Adapter;

import android.content.Context;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.kimopax.Model.Project;
import com.ekspeace.kimopax.R;


import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewProjectsAdapter extends RecyclerView.Adapter<RecyclerViewProjectsAdapter.MyViewHolder> {
    private Context context;
    private List<Project> projects;

    public RecyclerViewProjectsAdapter(Context context, List<Project> projects) {
        this.context = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public RecyclerViewProjectsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_project_recycler_view, parent, false);
        return new RecyclerViewProjectsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewProjectsAdapter.MyViewHolder holder, int position) {
        Project project = projects.get(position);
        String msg = new StringBuffer().append("<strong>").append(project.getName()).append("</strong>").toString() + " " + project.getYear() + " - " + project.getInfo();
        holder.tvProjectDetails.setText(Html.fromHtml(msg));
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProjectDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProjectDetails = itemView.findViewById(R.id.project_details);

        }
    }
}