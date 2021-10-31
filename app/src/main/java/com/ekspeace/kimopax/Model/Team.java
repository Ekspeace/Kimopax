package com.ekspeace.kimopax.Model;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private ArrayList<String> members;

    public Team() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
