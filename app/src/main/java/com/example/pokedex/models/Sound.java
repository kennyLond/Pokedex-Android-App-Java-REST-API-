package com.example.pokedex.models;

import com.google.gson.annotations.SerializedName;

public class Sound {

    @SerializedName("latest")
    private String latest;

    @SerializedName("legacy")
    private String legacy;

    // constructor
    public Sound(String latest, String legacy) {
        this.latest = latest;
        this.legacy = legacy;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getLegacy() {
        return legacy;
    }

    public void setLegacy(String legacy) {
        this.legacy = legacy;
    }
}