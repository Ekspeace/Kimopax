package com.ekspeace.kimopax.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekspeace.kimopax.Adapter.RecyclerViewDirectorsAdapter;
import com.ekspeace.kimopax.Adapter.RecyclerViewServicesAdapter;
import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Constants.PopUp;
import com.ekspeace.kimopax.Interface.IDirectorLoadListener;
import com.ekspeace.kimopax.Interface.IProjectLoadListener;
import com.ekspeace.kimopax.Model.Director;
import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.Model.Project;
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

public class Directors extends AppCompatActivity implements IDirectorLoadListener {
    private RecyclerView recyclerView;
    private LinearLayout loading;
    private IDirectorLoadListener iDirectorLoadListener;
    private Director director;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directors);

        InitView();
    }
    private void InitView() {
        recyclerView = findViewById(R.id.directors_recycler_view);
        loading = findViewById(R.id.directors_progressbar);
        iDirectorLoadListener = this;
        findViewById(R.id.directors_back).setOnClickListener(view -> onBackPressed());
        LoadDirectors();
    }
    private void LoadDirectors() {
        loading.setVisibility(View.VISIBLE);
        if (Common.isOnline(this)) {
            CollectionReference reference = FirebaseFirestore.getInstance().collection("Directors");
            List<Director> directors = new ArrayList<>();
            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        director = queryDocumentSnapshot.toObject(Director.class);
                                        directors.add(director);
                                    }
                                    iDirectorLoadListener.OnDirectorLoadSuccess(directors);
                                }
                            }
                        }
                    }).addOnFailureListener(e -> iDirectorLoadListener.OnDirectorLoadFailed(e.getMessage()));

        } else {
            loading.setVisibility(View.GONE);
            PopUp.Network(this);
        }
    }

    @Override
    public void OnDirectorLoadSuccess(List<Director> directors) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        RecyclerViewDirectorsAdapter adapter = new RecyclerViewDirectorsAdapter(this, directors);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            int count = 0;
            String sHoldInfo =  directors.get(position).getInfo(), formattedStringInfo = "";
            for(int i = 0; i < sHoldInfo.length(); i++){
                formattedStringInfo += sHoldInfo.charAt(i);
                if(sHoldInfo.charAt(i) == '.'){
                    count++;
                    if(count == 3)
                         formattedStringInfo += "<br><br>";
                }
            }
            String info = formattedStringInfo;
            String name = directors.get(position).getName();
            String image = directors.get(position).getImage();
            String title = directors.get(position).getTitle();
            Intent intent = new Intent(this, DirectorDetails.class);
            intent.putExtra("INFO", info);
            intent.putExtra("NAME", name);
            intent.putExtra("IMAGE", image);
            intent.putExtra("TITLE", title);
            startActivity(intent);
        });
        loading.setVisibility(View.GONE);
    }

    @Override
    public void OnDirectorLoadFailed(String message) {
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
            LoadDirectors();
        }
    }
}