package com.example.pokedex.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PokemonDetail {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    @SerializedName("sprites")
    private Images images;

    //Son lists por que no contienen valores unicos
    @SerializedName("types")
    private List<TypeSlot> types;

    @SerializedName("stats")
    private List<Stats> stats;

    @SerializedName("cries")
    private Sound sound;

    private String description;

    public PokemonDetail() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public List<TypeSlot> getTypes() {
        return types;
    }

    public void setTypes(List<TypeSlot> types) {
        this.types = types;
    }

    public List<Stats> getStats() {
        return stats;
    }

    public void setStats(List<Stats> stats) {
        this.stats = stats;
    }

    public Sound getSound() {
        return sound;
    }

    public void setCries(Sound sound) {
        this.sound = sound;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Imagen oficial del Pokémon
    // mira todas las capas de imagen hasta que encuentre algo
    public String getImageUrl() {
        if (images != null && images.getOther() != null
                && images.getOther().getOfficialArtwork() != null) {
            return images.getOther().getOfficialArtwork().getFrontDefault();
        }
        return null;
    }

    // Tipo principal del Pokémon
    public String getPrimaryType() {
        if (types != null && !types.isEmpty()) {
            return types.get(0).getType().getName();
        }
        return "normal";
    }

    // ===== Clases internas =====
    // clases que usamos para la logica de las imagenes

    public static class Images {

        @SerializedName("other")
        private Other other;

        public Other getOther() {
            return other;
        }

        public void setOther(Other other) {
            this.other = other;
        }
    }

    public static class Other {

        @SerializedName("official-artwork")
        private OfficialArtwork officialArtwork;

        public OfficialArtwork getOfficialArtwork() {
            return officialArtwork;
        }

        public void setOfficialArtwork(OfficialArtwork officialArtwork) {
            this.officialArtwork = officialArtwork;
        }
    }

    public static class OfficialArtwork {

        @SerializedName("front_default")
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }

        public void setFrontDefault(String frontDefault) {
            this.frontDefault = frontDefault;
        }
    }

    public static class TypeSlot {

        @SerializedName("slot")
        private int slot;

        @SerializedName("type")
        private Type type;

        public int getSlot() {
            return slot;
        }

        public void setSlot(int slot) {
            this.slot = slot;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }
    }

    public static class Type {

        @SerializedName("name")
        private String name;

        @SerializedName("url")
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
