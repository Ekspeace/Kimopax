package com.ekspeace.kimopax.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ekspeace.kimopax.Adapter.RecyclerViewSelectedServiceAdapter;
import com.ekspeace.kimopax.Constants.Common;
import com.ekspeace.kimopax.Model.EventBus.DeleteServiceEvent;
import com.ekspeace.kimopax.Model.Service;
import com.ekspeace.kimopax.R;
import com.ekspeace.kimopax.ViewModel.ServiceViewModel;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.util.List;

public class GenerateQuotation extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Service> serviceList;
    private ServiceViewModel serviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_quotation);

        InitView();
    }
    private void InitView(){
        recyclerView = findViewById(R.id.generate_recycler_view);
        serviceViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ServiceViewModel.class);

        findViewById(R.id.generate_back).setOnClickListener(view -> onBackPressed());
        findViewById(R.id.generate_delete_all).setOnClickListener(view -> serviceViewModel.deleteAll());
        findViewById(R.id.generate_button).setOnClickListener(view -> {
            Common.services = serviceList;
            try {
                String path = getFilesDir()+"/Kimopax_Quotation.pdf";
                Common.GeneratePdf(this, path, serviceList);
                Toast.makeText(this, "Pdf created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, PdfViewer.class));
            } catch (FileNotFoundException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        LoadServices();
    }
    private void LoadServices(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceViewModel.getAllServices().observe(this, (Observer<List<Service>>) services -> {
            RecyclerViewSelectedServiceAdapter adapter = new RecyclerViewSelectedServiceAdapter(this, services);
            recyclerView.setAdapter(adapter);
            serviceList = services;
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
        });
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
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void DeleteService(DeleteServiceEvent event) {
        if (event.isSelected()) {
            if(event.getService() != null) {
                serviceViewModel.delete(event.getService());
                Toast.makeText(this, "Service has been deleted", Toast.LENGTH_LONG).show();
            }
        }
    }
}