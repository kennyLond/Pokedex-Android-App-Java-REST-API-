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

    // Constructor para DetailActivity
    public FavoritesPresenter(FavoriteButtonView view, Context context) {
        this.view = view;
        this.favoritesManager = new FavoritesManager(context);
    }

    // ========== MÉTODOS PARA FAVORITESACTIVITY ==========
    public void loadFavorites() {
        if (view instanceof FavoritesView) {
            List<Pokemon> favorites = favoritesManager.getFavorites();
            if (favorites.isEmpty()) {
                ((FavoritesView) view).showEmptyMessage();
            } else {
                ((FavoritesView) view).showFavorites(favorites);
            }
        }
    }

    // ========== MÉTODOS PARA DETAILACTIVITY ==========
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

    public boolean isFavorite(Pokemon pokemon) {
        return favoritesManager.isFavorite(pokemon);
    }

    private void updateFavoriteStatus() {
        if (currentPokemon != null && view instanceof FavoriteButtonView) {
            boolean isFavorite = favoritesManager.isFavorite(currentPokemon);
            ((FavoriteButtonView) view).updateFavoriteButton(isFavorite);
        }
    }

    public void onDestroy() {
        view = null;
    }
}