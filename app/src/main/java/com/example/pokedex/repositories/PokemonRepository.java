package com.example.pokedex.repositories;

import com.example.pokedex.models.PokemonDetail;
import com.example.pokedex.models.PokemonResponse;
import com.example.pokedex.models.PokemonSpecies;
import com.example.pokedex.network.PokeApiService;
import com.example.pokedex.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;

public class PokemonRepository {
    private PokeApiService apiService;

    public PokemonRepository() {
        this.apiService = RetrofitClient.getPokeApiService();
    }

    public void getPokemonList(int limit, int offset, Callback<PokemonResponse> callback) {
        Call<PokemonResponse> call = apiService.getPokemonList(limit, offset);
        call.enqueue(callback);
    }

    public void getPokemonDetail(int id, Callback<PokemonDetail> callback) {
        Call<PokemonDetail> call = apiService.getPokemonDetail(id);
        call.enqueue(callback);
    }

    public void getPokemonSpecies(int id, Callback<PokemonSpecies> callback) {
        Call<PokemonSpecies> call = apiService.getPokemonSpecies(id);
        call.enqueue(callback);
    }
}