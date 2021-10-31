package com.ekspeace.kimopax.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.kimopax.Model.Team;
import com.ekspeace.kimopax.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewTeamAdapter extends RecyclerView.Adapter<RecyclerViewTeamAdapter.MyViewHolder> {
    private Context context;
    private List<Team> teams;

    public RecyclerViewTeamAdapter(Context context, List<Team> teams) {
        this.context = context;
        this.teams = teams;
    }

    @NonNull
    @Override
    public RecyclerViewTeamAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_team_recycler_view, parent, false);
        return new RecyclerViewTeamAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTeamAdapter.MyViewHolder holder, int position) {
        Team team = teams.get(position);
        holder.tvTeamName.setText(team.getName());
        ArrayList<String> members = team.getMembers();
        String sMembers = "<ul>";
        for(int i = 0; i < members.size(); i++){
            sMembers += "<li>" + members.get(i) + "</li>";
        }
        sMembers += "</ul>";
        holder.tvTeamMembers.setText(Html.fromHtml(sMembers));
    }
    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTeamName, tvTeamMembers;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.team_name);
            tvTeamMembers = itemView.findViewById(R.id.team_members);
        }
    }
}