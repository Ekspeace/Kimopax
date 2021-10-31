package com.ekspeace.kimopax.Interface;


import com.ekspeace.kimopax.Model.Project;

import java.util.List;

public interface IProjectLoadListener {
    void OnProjectLoadSuccess(List<Project> projects);
    void OnProjectLoadFailed(String message);
}
