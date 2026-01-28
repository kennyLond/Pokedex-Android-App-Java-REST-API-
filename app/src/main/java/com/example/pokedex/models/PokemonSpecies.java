package com.example.pokedex.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PokemonSpecies {

    @SerializedName("flavor_text_entries")
    private List<FlavorTextEntry> flavorTextEntries;

    public List<FlavorTextEntry> getFlavorTextEntries() {
        return flavorTextEntries;
    }

    // Método para obtener la primera descripción en español
    public String getSpanishDescription() {
        if (flavorTextEntries == null) return "";

        for (FlavorTextEntry entry : flavorTextEntries) {
            if (entry.getLanguage().getName().equals("es")) {

                return entry.getFlavorText()
                        .replace("\n", " ")
                        .replace("\f", " ");
            }
        }

        // Si no hay español, buscar inglés
        for (FlavorTextEntry entry : flavorTextEntries) {
            if (entry.getLanguage().getName().equals("en")) {
                return entry.getFlavorText()
                        .replace("\n", " ")
                        .replace("\f", " ");
            }
        }

        return "";
    }

    public static class FlavorTextEntry {
        @SerializedName("flavor_text")
        private String flavorText;
        private Language language;

        public String getFlavorText() {
            return flavorText;
        }

        public Language getLanguage() {
            return language;
        }
    }

    public static class Language {
        private String name;

        public String getName() {
            return name;
        }
    }
}