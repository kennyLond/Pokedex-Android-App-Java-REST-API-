package com.example.pokedex.presenters;

import android.content.Context;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.utils.FavoritesManager;
import com.example.pokedex.views.FavoritesView;
import java.util.List;

public class FavoritesPresenter {
    // Interfaces que maneja este presenter
    public interface FavoriteButtonView {
        void updateFavoriteButton(boolean isFavorite);
    }

    // Variables
    private Object view; // Puede ser FavoritesView O FavoriteButtonView
    private FavoritesManager favoritesManager;
    private Pokemon currentPokemon;

    // Constructor para FavoritesActivity
    public FavoritesPresenter(FavoritesView view, Context context) {
        this.view = view;
        this.favoritesManager = new FavoritesManager(context);
    }


    public FavoritesPresenter(FavoriteButtonView view, Context context) {
        this.view = view;
        this.favoritesManager = new FavoritesManager(context);
    }

    // cargar favoritos
    public void loadFavorites() {
        if (view instanceof FavoritesView) {
            List<Pokemon> favorites = favoritesManager.getFavorites();
            //pide lista de pokemones
            if (favorites.isEmpty()) {
                ((FavoritesView) view).showEmptyMessage();
            } else {
                // pasa para que la muestre
                ((FavoritesView) view).showFavorites(favorites);
            }
        }
    }


    public void setCurrentPokemon(Pokemon pokemon) {
        //guarda el pokemon actual y actualiza el estado del boton
        this.currentPokemon = pokemon;
        updateFavoriteStatus();
    }

    // carga datos y da al pokemonon como favorito
    public void toggleFavorite() {
        if (currentPokemon != null) {
            favoritesManager.toggleFavorite(currentPokemon);
            updateFavoriteStatus();
        }
    }

    public boolean isFavorite(Pokemon pokemon) {
        return favoritesManager.isFavorite(pokemon);
    }

    private void updateFavoriteStatus() {
        //actualiza la vista del boton
        if (currentPokemon != null && view instanceof FavoriteButtonView) {
            boolean isFavorite = favoritesManager.isFavorite(currentPokemon);
            ((FavoriteButtonView) view).updateFavoriteButton(isFavorite);
        }
    }
// Evita fuga de información
    public void onDestroy() {
        view = null;
    }
}