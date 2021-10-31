package com.ekspeace.kimopax.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ekspeace.kimopax.Model.Service;
import com.ekspeace.kimopax.Repository.ServiceRepository;

import java.util.List;

public class ServiceViewModel extends AndroidViewModel {
    private ServiceRepository repository;
    private LiveData<List<Service>> allServices;
    public ServiceViewModel(Application application) {
        super(application);
        repository = new ServiceRepository(application);
        allServices = repository.getAllServices();
    }
    public void insert(Service service) {
        repository.insert(service);
    }
    public void delete(Service service) {
        repository.delete(service);
    }
    public void deleteAll() {
        repository.deleteAll();
    }
    public LiveData<List<Service>> getAllServices() {
        return allServices;
    }
}
