package com.example.apl_live_scores;

public class BowlerModel {
    private String name;
    private String runs_wicket;

    public BowlerModel(String name, String runs_wicket) {
        this.name = name;
        this.runs_wicket = runs_wicket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuns_wicket() {
        return runs_wicket;
    }

    public void setRuns_wicket(String runs_wicket) {
        this.runs_wicket = runs_wicket;
    }
}
