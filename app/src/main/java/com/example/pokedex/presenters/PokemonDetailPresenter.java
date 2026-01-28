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
        Log.d(TAG, "🔍 loadPokemonDetail() llamado para ID: " + pokemonId);
        view.showLoading();

        //  PASO 1: Obtener detalles básicos
        repository.getPokemonDetail(pokemonId, new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                Log.d(TAG, " Detalles básicos recibidos. Código: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetail detail = response.body();
                    Log.d(TAG, "📋 Pokémon: " + detail.getName());

                    // PASO 2: Ahora obtener la descripción (species)
                    repository.getPokemonSpecies(pokemonId, new Callback<PokemonSpecies>() {
                        @Override
                        public void onResponse(Call<PokemonSpecies> call, Response<PokemonSpecies> speciesResponse) {
                            Log.d(TAG, " Species recibido. Código: " + speciesResponse.code());

                            if (speciesResponse.isSuccessful() && speciesResponse.body() != null) {
                                // Extraer descripción en español
                                String description = speciesResponse.body().getSpanishDescription();
                                Log.d(TAG, "📝 Descripción obtenida: " + (description.isEmpty() ? "VACÍA" : description));
                                detail.setDescription(description); //  ASIGNAR DESCRIPCIÓN
                            } else {
                                Log.d(TAG, "⚠️ Species vacío o error");
                                detail.setDescription("");
                            }

                            view.hideLoading();
                            view.showPokemonDetail(detail);
                        }

                        @Override
                        public void onFailure(Call<PokemonSpecies> call, Throwable t) {
                            Log.e(TAG, "❌ Error en species: " + t.getMessage());
                            detail.setDescription("");
                            view.hideLoading();
                            view.showPokemonDetail(detail);
                        }
                    });

                } else {
                    Log.e(TAG, "❌ Error en detalles básicos");
                    view.hideLoading();
                    view.showError("Error al cargar detalles del pokemon");
                }
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                Log.e(TAG, "❌ Falló detalles básicos: " + t.getMessage());
                view.hideLoading();
                view.showError("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void setCurrentPokemon(Pokemon pokemon) {
        this.currentPokemon = pokemon;
        updateFavoriteStatus();
    }

    public void toggleFavorite() {
        if (currentPokemon != null) {
            favoritesManager.toggleFavorite(currentPokemon);
            updateFavoriteStatus();
        }
    }

    private void updateFavoriteStatus() {
        if (currentPokemon != null) {
            boolean isFavorite = favoritesManager.isFavorite(currentPokemon);
            view.updateFavoriteButton(isFavorite);
        }
    }

    public void onDestroy() {
        view = null;
    }
}