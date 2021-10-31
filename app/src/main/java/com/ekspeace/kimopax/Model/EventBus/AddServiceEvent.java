package com.ekspeace.kimopax.Model.EventBus;

import com.ekspeace.kimopax.Model.Service;

import java.util.List;

public class AddServiceEvent {
    private Service service;
    private boolean isSelected;

    public AddServiceEvent(Service service, Boolean isSelected) {
        this.service = service;
        this.isSelected = isSelected;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
