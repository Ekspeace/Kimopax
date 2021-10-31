package com.ekspeace.kimopax.Interface;

import com.ekspeace.kimopax.Model.Director;
import com.ekspeace.kimopax.Model.Project;

import java.util.List;

public interface IDirectorLoadListener {
    void OnDirectorLoadSuccess(List<Director> directors);
    void OnDirectorLoadFailed(String message);
}
