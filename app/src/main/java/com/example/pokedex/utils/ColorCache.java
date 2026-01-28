package com.example.pokedex.utils;

import java.util.HashMap;
import java.util.Map;

public class ColorCache {
    private static ColorCache instance;
//    guarda id y color
    private Map<Integer, Integer> lightColorCache;
    private Map<Integer, Integer> darkColorCache;

    private ColorCache() {
        lightColorCache = new HashMap<>();
        darkColorCache = new HashMap<>();
    }

//    si no esxixte cache, crea la instacia de color
    public static ColorCache getInstance() {
        if (instance == null) {
            instance = new ColorCache();
        }
        return instance;
    }
//guarda colores
    public void putColors(int pokemonId, int lightColor, int darkColor) {
        lightColorCache.put(pokemonId, lightColor);
        darkColorCache.put(pokemonId, darkColor);
    }

    public Integer getLightColor(int pokemonId) {
        return lightColorCache.get(pokemonId);
    }

    public Integer getDarkColor(int pokemonId) {
        return darkColorCache.get(pokemonId);
    }

    public boolean hasColors(int pokemonId) {
        return lightColorCache.containsKey(pokemonId) && darkColorCache.containsKey(pokemonId);
    }
}