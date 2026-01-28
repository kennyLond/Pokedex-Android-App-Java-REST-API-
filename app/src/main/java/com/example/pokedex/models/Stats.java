package com.example.pokedex.models;

import com.google.gson.annotations.SerializedName;
public class Stats {
    @SerializedName("base_stat")
    private int baseStat;

    private Stat stat;

    public int getBaseStat() {
        return baseStat;
    }

    public Stat getStat() {
        return stat;
    }

    public static class Stat {
        private String name;

        public String getName() {
            return name;
        }
    }
}
