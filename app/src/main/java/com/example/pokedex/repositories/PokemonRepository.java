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

    // canal de comunicacion con la Api,sabe como tenerlos
    public PokemonRepository() {
        this.apiService = RetrofitClient.getPokeApiService();
    }

    //callback, funciona que cuando se haga la peticion y se tenga la respuesta, se llama estos callback y entrega el objeto,
    // que la variable sea del tipo de modelo que aparece en <>
    public void getPokemonList(int limit, Callback<PokemonResponse> callback) {
        Call<PokemonResponse> pokemonListCall= apiService.getPokemonList(limit);
        //prepara la peticion y cuando tiene la respuesta llama al callback
        pokemonListCall.enqueue(callback);
    }

    public void getPokemonDetail(int id, Callback<PokemonDetail> callback) {
        Call<PokemonDetail> pokemonDetailCall = apiService.getPokemonDetail(id);
        pokemonDetailCall.enqueue(callback);
    }

    public void getPokemonSpecies(int id, Callback<PokemonSpecies> callback) {
        Call<PokemonSpecies> pokemonSpeciesCall = apiService.getPokemonSpecies(id);
        pokemonSpeciesCall.enqueue(callback);
    }


    //estos callback piden el objeto, lo preparan y estan a la espera de que los llamen para entregarlos
}