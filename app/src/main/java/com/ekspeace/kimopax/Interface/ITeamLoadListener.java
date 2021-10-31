package com.ekspeace.kimopax.Interface;

import com.ekspeace.kimopax.Model.Project;
import com.ekspeace.kimopax.Model.Team;

import java.util.List;

public interface ITeamLoadListener {
    void OnTeamLoadSuccess(List<Team> teams);
    void OnTeamLoadFailed(String message);
}
