package com.ekspeace.kimopax.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.kimopax.Adapter.RecyclerViewServicesAdapter;
import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Constants.PopUp;
import com.ekspeace.kimopax.Interface.IServiceLoadListener;
import com.ekspeace.kimopax.Model.EventBus.AddServiceEvent;
import com.ekspeace.kimopax.Model.EventBus.DeleteServiceEvent;
import com.ekspeace.kimopax.Model.EventBus.NetworkConnectionEvent;
import com.ekspeace.kimopax.Model.Service;
import com.ekspeace.kimopax.R;
import com.ekspeace.kimopax.ViewModel.ServiceViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

public class Services extends AppCompatActivity implements IServiceLoadListener, NavigationView.OnNavigationItemSelectedListener{
    private IServiceLoadListener iServiceLoadListener;
    private RecyclerView recyclerView;
    private TextView tvNumberOfService;
    private View vSelectedServices;
    private LinearLayout loading;
    private Service service;
    private ServiceViewModel serviceViewModel;
    private int serviceNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Intent intent = getIntent();
        String message = intent.getStringExtra("LOGIN");
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        InitView();
    }
    private void InitView(){
        recyclerView = findViewById(R.id.service_recycler_view);
        tvNumberOfService = findViewById(R.id.service_number_selected);
        loading = findViewById(R.id.service_progressbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.service_toolbar);
        NavigationView navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.user_profile_name);
        TextView tvEmail = headerView.findViewById(R.id.user_profile_email);
        TextView tvUserName = findViewById(R.id.service_user_name);
        vSelectedServices = findViewById(R.id.service_view_selected_button);
        vSelectedServices.setEnabled(false);
        vSelectedServices.setOnClickListener(view -> startActivity(new Intent(Services.this, GenerateQuotation.class)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toolbar.setNavigationIcon(R.drawable.menu);
        navigationView.setNavigationItemSelectedListener(this);

        iServiceLoadListener = this;
        serviceViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ServiceViewModel.class);

        tvName.setText(Common.currentUser.getName());
        tvEmail.setText(Common.currentUser.getEmail());
        tvUserName.setText(Common.currentUser.getName());

        LoadServices();
        LoadSelectedServices();
    }
    private void LoadServices() {
        loading.setVisibility(View.VISIBLE);
        if (Common.isOnline(this)) {
            CollectionReference reference = FirebaseFirestore.getInstance().collection("Services");
            List<Service> services = new ArrayList<>();
            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        service = queryDocumentSnapshot.toObject(Service.class);
                                        services.add(service);
                                    }
                                    iServiceLoadListener.OnServiceLoadSuccess(services);
                                }
                            }
                        }
                    }).addOnFailureListener(e -> iServiceLoadListener.OnServiceLoadFailed(e.getMessage()));

        } else {
            loading.setVisibility(View.GONE);
            PopUp.Network(this);
        }
    }
    private void LoadSelectedServices(){
        serviceViewModel.getAllServices().observe(this, new Observer<List<Service>>() {
            @Override
            public void onChanged(List<Service> services) {
                if (services != null) {
                    serviceNumber = services.size();
                    if(services.size() != 0)
                        vSelectedServices.setEnabled(true);
                }
                tvNumberOfService.setText("[" + serviceNumber + "]");
            }
        });
    }
    @Override
    public void OnServiceLoadSuccess(List<Service> services) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewServicesAdapter adapter = new RecyclerViewServicesAdapter(this, services);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, position) -> {
            String info = services.get(position).getInfo();
            String name = services.get(position).getName();
            String header = services.get(position).getHeader();
            Intent intent = new Intent(this, ServiceDetails.class);
            intent.putExtra("INFO", info);
            intent.putExtra("NAME", name);
            intent.putExtra("HEADER", header);
            startActivity(intent);
        });
        loading.setVisibility(View.GONE);
    }

    @Override
    public void OnServiceLoadFailed(String message) {
        loading.setVisibility(View.GONE);
        Toast.makeText(this, "Error!: "+ message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==  R.id.about_us)
            startActivity(new Intent(this, About.class));
        if (item.getItemId() ==  R.id.projects)
            startActivity(new Intent(this, Projects.class));
        if (item.getItemId() ==  R.id.directors)
            startActivity(new Intent(this, Directors.class));
        if (item.getItemId() ==  R.id.team)
            startActivity(new Intent(this, Teams.class));
        if (item.getItemId() ==  R.id.contact_us)
            startActivity(new Intent(this, Contact.class));
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
       // LoadSelectedServices();
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
            LoadServices();
        }
    }
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void AddService(AddServiceEvent event) {
        if (event.isSelected()) {
            if (event.getService() != null) {
                serviceViewModel.insert(event.getService());
                Toast.makeText(this, "Service has been added", Toast.LENGTH_LONG).show();
            }
        }
    }
}