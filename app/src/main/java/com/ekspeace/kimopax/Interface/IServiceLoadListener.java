package com.ekspeace.kimopax.Interface;

import com.ekspeace.kimopax.Model.Service;

import java.util.List;

public interface IServiceLoadListener {
    void OnServiceLoadSuccess(List<Service> services);
    void OnServiceLoadFailed(String message);
}
