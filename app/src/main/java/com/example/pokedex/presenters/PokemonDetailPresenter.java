package com.example.pokedex.presenters;

import android.content.Context;
import android.util.Log;

import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonDetail;
import com.example.pokedex.models.PokemonSpecies;
import com.example.pokedex.repositories.PokemonRepository;
import com.example.pokedex.utils.FavoritesManager;
import com.example.pokedex.views.PokemonDetailView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonDetailPresenter {
    private static final String TAG = "DetailPresenter";

    private PokemonDetailView view;
    private PokemonRepository repository;
    private FavoritesManager favoritesManager;
    private Pokemon currentPokemon;

    public PokemonDetailPresenter(PokemonDetailView view, Context context) {
        this.view = view;
        this.repository = new PokemonRepository();
        this.favoritesManager = new FavoritesManager(context);
    }

    public void loadPokemonDetail(int pokemonId) {
        view.showLoading();

        //  PASO 1: Obtener detalles básicos
        repository.getPokemonDetail(pokemonId, new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {

                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail detail = response.body();

                    // PASO 2: Ahora obtener la descripción (species)
                    repository.getPokemonSpecies(pokemonId, new Callback<PokemonSpecies>() {
                        @Override
                        public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> speciesResponse) {

                            if (speciesResponse.isSuccessful() && speciesResponse.body() != null) {
                                // Extraer descripción en español
                                String description = speciesResponse.body().getSpanishDescription();
                                detail.setDescription(description);
                            } else {
                                detail.setDescription("");
                            }

                            view.hideLoading();
                            view.showPokemonDetail(detail);
                        }

                        @Override
                        public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                            //mostrar error
                            detail.setDescription("");
                            view.hideLoading();
                            view.showPokemonDetail(detail);
                        }
                    });

                } else {
                    view.hideLoading();
                    view.showError("Error al cargar detalles del pokemon");
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                view.hideLoading();
                view.showError("Error de conexión: " + t.getMessage());
            }
        });
    }
// verifica si el pokemon esta en favoritos
    public void setCurrentPokemon(Pokemon pokemon) {
        this.currentPokemon = pokemon;
        updateFavoriteStatus();
    }
// agrega o quita e favoritos
    public void toggleFavorite() {
        if (currentPokemon != null) {
            favoritesManager.toggleFavorite(currentPokemon); //agrega y quita de favoritos
            updateFavoriteStatus();
        }
    }
// actualiza el boton de favoritos
    private void updateFavoriteStatus() {
        if (currentPokemon != null) {
            boolean isFavorite = favoritesManager.isFavorite(currentPokemon); //si esta en favoritos o no actualiza el boton
            view.updateFavoriteButton(isFavorite);
        }
    }

    public void onDestroy() {
        view = null;
    }
}