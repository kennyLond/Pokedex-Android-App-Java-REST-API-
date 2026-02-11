package com.example.pokedex.presenters;

import android.util.Log;

import com.example.pokedex.models.PokemonResponse;
import com.example.pokedex.repositories.PokemonRepository;
import com.example.pokedex.views.PokemonListView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonListPresenter {

    private static final String TAG = "PokemonListPresenter";

    private PokemonListView view;
    private PokemonRepository repository;

    public PokemonListPresenter(PokemonListView view) {
        this.view = view;
        this.repository = new PokemonRepository();
    }

    public void loadPokemonList(int limit) {
        view.showLoading();

        repository.getPokemonList(limit, new Callback<PokemonResponse>() {
            @Override
            // se hizo la llamda, respondio el servidor
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                view.hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    view.showPokemonList(response.body().getResults());
                } else {
                    view.showError("Error al cargar los pokemones");
                }
            }
// si ocurrio una llamada y se identifico error
            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Log.e(TAG, "onFailure() - Error: " + t.getMessage(), t);
                view.hideLoading();
                view.showError("Error de conexión");
            }
        });
    }

    //evitar fugas de memoria, se autodetruye
    public void onDestroy() {
        view = null;
    }
}
