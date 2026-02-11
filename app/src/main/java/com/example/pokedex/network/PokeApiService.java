package com.example.pokedex.network;



import com.example.pokedex.models.PokemonDetail;
import com.example.pokedex.models.PokemonResponse;
import com.example.pokedex.models.PokemonSpecies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface PokeApiService {


    @GET("pokemon")
    Call<PokemonResponse> getPokemonList(@Query("limit") int limit);
    // convierte al Json en esta estructura Models que creamos

    @GET("pokemon/{id}")
    Call<PokemonDetail> getPokemonDetail(@Path("id") int id);

    @GET("pokemon-species/{id}")
    Call<PokemonSpecies> getPokemonSpecies(@Path("id") int id);

}
// las peticiónes generan Call que son peticiones a la espera de ser llamadas pero que aun no se ejecutan
