package com.example.apl_live_scores;

public class BatsmanModel {
    private String name;
    private String runs;
    private String status;

    public BatsmanModel(String name, String runs, String status) {
        this.name = name;
        this.runs = runs;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuns() {
        return runs;
    }

    public void setRuns(String runs) {
        this.runs = runs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
