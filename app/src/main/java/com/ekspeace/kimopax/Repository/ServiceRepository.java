package com.ekspeace.kimopax.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ekspeace.kimopax.Constants.ServiceDatabase;
import com.ekspeace.kimopax.Interface.ServiceDao;
import com.ekspeace.kimopax.Model.Service;

import java.util.List;

public class ServiceRepository {
    private ServiceDao serviceDao;
    private LiveData<List<Service>> allServices;

    public ServiceRepository(Application application){
        ServiceDatabase serviceDatabase = ServiceDatabase.getInstance(application);
        serviceDao = serviceDatabase.serviceDao();
        allServices = serviceDao.getAllServices();
    }

    public void insert(Service service) {
        new InsertServiceAsyncTask(serviceDao).execute(service);
    }

    public void delete(Service service) {
        new DeleteServiceAsyncTask(serviceDao).execute(service);
    }

    public void deleteAll() {
        new DeleteAllServiceAsyncTask(serviceDao).execute();
    }

    public LiveData<List<Service>> getAllServices(){
        return allServices;
    }

    private static class InsertServiceAsyncTask extends AsyncTask<Service, Void, Void> {
        private ServiceDao serviceDao;

        private InsertServiceAsyncTask(ServiceDao serviceDao) {
            this.serviceDao = serviceDao;
        }

        @Override
        protected Void doInBackground(Service... services) {
            serviceDao.Insert(services[0]);
            return null;
        }
    }

    private static class DeleteServiceAsyncTask extends AsyncTask<Service, Void, Void> {
        private ServiceDao serviceDao;

        private DeleteServiceAsyncTask(ServiceDao serviceDao) {
            this.serviceDao = serviceDao;
        }

        @Override
        protected Void doInBackground(Service... services) {
            serviceDao.Delete(services[0]);
            return null;
        }
    }
    private static class DeleteAllServiceAsyncTask extends AsyncTask<Service, Void, Void> {
        private ServiceDao serviceDao;

        private DeleteAllServiceAsyncTask(ServiceDao serviceDao) {
            this.serviceDao = serviceDao;
        }

        @Override
        protected Void doInBackground(Service... services) {
            serviceDao.DeleteAll();
            return null;
        }
    }
}
