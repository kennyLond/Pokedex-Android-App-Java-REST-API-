package com.example.pokedex.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";
    private static Retrofit retrofit = null;

    // prepara el sistema, para convertir el Json en objetos, esperando a que lo soliciten
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder() //
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit; //espera a ser solicitado para entregar los objetos
    }

// se encarga de entregar las peticiones


    //se utiliza el objeto de getCliente, y se le implementas las peticiones que contiene la interfaz
    public static PokeApiService getPokeApiService() {
        return getClient().create(PokeApiService.class);
    }
}
