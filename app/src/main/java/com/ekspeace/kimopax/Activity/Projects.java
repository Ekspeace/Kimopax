package com.ekspeace.kimopax.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekspeace.kimopax.Adapter.RecyclerViewProjectsAdapter;
import com.ekspeace.kimopax.Adapter.RecyclerViewServicesAdapter;
import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Constants.PopUp;
import com.ekspeace.kimopax.Interface.IProjectLoadListener;
import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.Model.Project;
import com.ekspeace.kimopax.Model.Service;
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

public class Projects extends AppCompatActivity implements IProjectLoadListener {
    private RecyclerView recyclerView;
    private LinearLayout loading;
    private IProjectLoadListener iProjectLoadListener;
    private Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        InitView();
    }
    private void InitView() {
        recyclerView = findViewById(R.id.projects_recycler_view);
        loading = findViewById(R.id.project_progressbar);
        iProjectLoadListener = this;
        findViewById(R.id.project_back).setOnClickListener(view -> onBackPressed());
        LoadProjects();
    }
    private void LoadProjects() {
        loading.setVisibility(View.VISIBLE);
        if (Common.isOnline(this)) {
            CollectionReference reference = FirebaseFirestore.getInstance().collection("Projects");
            List<Project> projects = new ArrayList<>();
            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        project = queryDocumentSnapshot.toObject(Project.class);
                                        projects.add(project);
                                    }
                                    iProjectLoadListener.OnProjectLoadSuccess(projects);
                                }
                            }
                        }
                    }).addOnFailureListener(e -> iProjectLoadListener.OnProjectLoadFailed(e.getMessage()));

        } else {
            loading.setVisibility(View.GONE);
            PopUp.Network(this);
        }
    }
    @Override
    public void OnProjectLoadSuccess(List<Project> projects) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewProjectsAdapter adapter = new RecyclerViewProjectsAdapter(this, projects);
        recyclerView.setAdapter(adapter);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void OnProjectLoadFailed(String message) {
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
           LoadProjects();
        }
    }
}