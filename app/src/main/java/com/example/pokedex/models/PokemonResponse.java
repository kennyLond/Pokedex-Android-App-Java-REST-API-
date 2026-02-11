package com.example.pokedex.models;

import java.util.List;

public class PokemonResponse {
    private int count;
    private String next;
    private String previous;
    private List<Pokemon> results;

    public int getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public List<Pokemon> getResults() {
        return results;
    }

    //actualizar la lista de pokemones
    public void setResults(List<Pokemon> results) {
        this.results = results;
    }
}
