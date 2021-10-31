package com.ekspeace.kimopax.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekspeace.kimopax.Adapter.RecyclerViewProjectsAdapter;
import com.ekspeace.kimopax.Adapter.RecyclerViewTeamAdapter;
import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Constants.PopUp;
import com.ekspeace.kimopax.Interface.IProjectLoadListener;
import com.ekspeace.kimopax.Interface.ITeamLoadListener;
import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.Model.Project;
import com.ekspeace.kimopax.Model.Team;
import com.ekspeace.kimopax.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teams extends AppCompatActivity implements ITeamLoadListener{
    private RecyclerView recyclerView;
    private LinearLayout loading;
    private ITeamLoadListener iTeamLoadListener;
    private Team team;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        InitView();
    }
    private void InitView() {
        recyclerView = findViewById(R.id.teams_recycler_view);
        loading = findViewById(R.id.teams_progressbar);
        iTeamLoadListener = this;
        findViewById(R.id.teams_back).setOnClickListener(view -> onBackPressed());
        LoadTeams();
    }
    private void LoadTeams() {
        loading.setVisibility(View.VISIBLE);
        if (Common.isOnline(this)) {
            CollectionReference reference = FirebaseFirestore.getInstance().collection("Teams");
            List<Team> teams = new ArrayList<>();
            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        team = queryDocumentSnapshot.toObject(Team.class);
                                        teams.add(team);
                                    }
                                    iTeamLoadListener.OnTeamLoadSuccess(teams);
                                }
                            }
                        }
                    }).addOnFailureListener(e -> iTeamLoadListener.OnTeamLoadFailed(e.getMessage()));

        } else {
            loading.setVisibility(View.GONE);
            PopUp.Network(this);
        }
    }
    @Override
    public void OnTeamLoadSuccess(List<Team> teams) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewTeamAdapter adapter = new RecyclerViewTeamAdapter(this, teams);
        recyclerView.setAdapter(adapter);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void OnTeamLoadFailed(String message) {
        loading.setVisibility(View.GONE);
        Toast.makeText(this, "Error!: "+ message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Connection(NetworkConnectionEvent event) {
        if (event.isNetworkConnected()) {
            LoadTeams();
        }
    }
}