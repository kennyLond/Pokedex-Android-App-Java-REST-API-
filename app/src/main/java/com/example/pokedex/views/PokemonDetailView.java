package com.example.pokedex.views;
import com.example.pokedex.models.PokemonDetail;
public interface PokemonDetailView {
    void showLoading();
    void hideLoading();
    void showPokemonDetail(PokemonDetail pokemonDetail);
    void showError(String message);
    void updateFavoriteButton(boolean isFavorite);
}
