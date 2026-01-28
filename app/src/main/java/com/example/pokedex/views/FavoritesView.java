package com.example.pokedex.views;

import java.util.List;
import com.example.pokedex.models.Pokemon;

public interface FavoritesView {
    void showFavorites(List<Pokemon> favorites);
    void showEmptyMessage();
    void showError(String message);
}
