package com.example.pokedex.utils;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.pokedex.models.Pokemon;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class FavoritesManager {
    private static final String PREFS_NAME = "PokedexPrefs";
    private static final String FAVORITES_KEY = "favorites";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public FavoritesManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // Guardar favoritos
    public void saveFavorites(List<Pokemon> favorites) {
        String json = gson.toJson(favorites);
        sharedPreferences.edit().putString(FAVORITES_KEY, json).apply();
    }

    // Obtener favoritos
    public List<Pokemon> getFavorites() {
        String json = sharedPreferences.getString(FAVORITES_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Pokemon>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Agregar a favoritos
    public void addFavorite(Pokemon pokemon) {
        List<Pokemon> favorites = getFavorites();
        if (!isFavorite(pokemon)) {
            favorites.add(pokemon);
            saveFavorites(favorites);
        }
    }

    // Remover de favoritos
    public void removeFavorite(Pokemon pokemon) {
        List<Pokemon> favorites = getFavorites();
        favorites.removeIf(p -> p.getName().equals(pokemon.getName()));
        saveFavorites(favorites);
    }

    // Verificar si es favorito
    public boolean isFavorite(Pokemon pokemon) {
        List<Pokemon> favorites = getFavorites();
        for (Pokemon fav : favorites) {
            if (fav.getName().equals(pokemon.getName())) {
                return true;
            }
        }
        return false;
    }

    // Toggle favorito
    public void toggleFavorite(Pokemon pokemon) {
        if (isFavorite(pokemon)) {
            removeFavorite(pokemon);
        } else {
            addFavorite(pokemon);
        }
    }
}
