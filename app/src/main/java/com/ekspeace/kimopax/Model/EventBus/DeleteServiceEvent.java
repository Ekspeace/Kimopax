package com.ekspeace.kimopax.Model.EventBus;

import com.ekspeace.kimopax.Model.Service;

import java.util.List;

public class DeleteServiceEvent {
    private Service service;
    private boolean isSelected;
    private List<Service> services;

    public DeleteServiceEvent(Service service, List<Service> services, Boolean isSelected) {
        this.service = service;
        this.services = services;
        this.isSelected = isSelected;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
