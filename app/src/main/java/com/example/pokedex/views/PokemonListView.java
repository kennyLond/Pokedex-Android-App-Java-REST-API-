package com.example.pokedex.views;

import java.util.List;
import com.example.pokedex.models.Pokemon;

public interface PokemonListView {
    void showLoading();
    void hideLoading();
    void showPokemonList(List<Pokemon> pokemonList);
    void showError(String message);
}
