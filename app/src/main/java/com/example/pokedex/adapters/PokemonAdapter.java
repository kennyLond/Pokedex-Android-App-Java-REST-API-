package com.example.pokedex.adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.pokedex.R;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonDetail;
import com.example.pokedex.network.RetrofitClient;
import com.example.pokedex.utils.ColorCache;
import com.example.pokedex.utils.TypeColors;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Clase principal del Adapter, conecta los datos con las tarjetas del RecyclerView
public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList;
    private OnPokemonClickListener listener;


    //  Interfaz para detectar clicks en las tarjetas

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
        void onPokemonLongClick(Pokemon pokemon);
    }

    public PokemonAdapter(OnPokemonClickListener listener) {
        this.pokemonList = new ArrayList<>();
        this.listener = listener;
    }


    // Recibe la lista de Pokémon desde MainActivity y le avisa al RecyclerView que hay datos nuevos

    public void setPokemonList(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
        notifyDataSetChanged();
    }


    // El RecyclerView pregunta ¿cuántas tarjetas necesito?

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }


    //  El RecyclerView crea las tarjetas vacías
    // Solo se ejecuta cuando necesita una tarjeta nueva

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false); // Infla el layout XML
        return new PokemonViewHolder(view); // La envuelve en el ViewHolder
    }


    // PASO 5: El RecyclerView llena cada tarjeta con datos
    // Se ejecuta cada vez que una tarjeta aparece en pantalla
    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);


        // Toma el Pokémon según su posición
        holder.bind(pokemon, listener);              // Le pasa los datos al método bind
    }

    // Maneja cada tarjeta individual

    static class PokemonViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView imageViewPokemon;
        private ProgressBar progressBarImage;
        private TextView textViewName;
        private int currentPokemonId = -1; // Sin Pokémon asignado

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageViewPokemon = itemView.findViewById(R.id.imageViewPokemon);
            progressBarImage = itemView.findViewById(R.id.progressBarImage);
            textViewName = itemView.findViewById(R.id.textViewPokemonName);
        }

        //bind() llena la tarjeta con los datos del Pokémon
        // Este es el método principal que pone nombre, imagen y colores

        public void bind(final Pokemon pokemon, final OnPokemonClickListener listener) {
            currentPokemonId = pokemon.getId();

            // Capitaliza y muestra el nombre
            String capitalizedName = pokemon.getName().substring(0, 1).toUpperCase()
                    + pokemon.getName().substring(1);
            textViewName.setText(capitalizedName);

            //  Muestra el spinner antes de cargar la imagen
            progressBarImage.setVisibility(View.VISIBLE);

            //  Carga la imagen con Glide
            Glide.with(itemView.getContext())
                    .load(pokemon.getImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        // Si la imagen falla, oculta el spinner
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            progressBarImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        // Si la imagen se carga bien, oculta el spinner
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            progressBarImage.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageViewPokemon);

            //  Aplica colores: desde caché o llamando a la API
            ColorCache cache = ColorCache.getInstance();
            if (cache.hasColors(pokemon.getId())) {
                // Ya tiene colores guardados, los aplica directamente
                int lightColor = cache.getLightColor(pokemon.getId());
                int darkColor = cache.getDarkColor(pokemon.getId());
                cardView.setCardBackgroundColor(lightColor);
                textViewName.setTextColor(darkColor);
            } else {
                // No tiene colores, pone gris temporal y los pide a la API
                cardView.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                textViewName.setTextColor(Color.parseColor("#666666"));
                loadPokemonType(pokemon.getId());
            }

            //  Configura el click en la tarjeta
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onPokemonClick(pokemon);
                    }
                }
            });

            //  Configura el click largo en la tarjeta (mantener presionado)
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onPokemonLongClick(pokemon);
                    }
                    return true;
                }
            });
        }


        // Pide el tipo del Pokémon a la API para obtener su color
        // Solo se ejecuta si no tiene colores en caché

        private void loadPokemonType(final int pokemonId) {
            RetrofitClient.getPokeApiService()
                    .getPokemonDetail(pokemonId)
                    .enqueue(new Callback<PokemonDetail>() {

                        @Override
                        // La API respondió, aplica los colores a la tarjeta
                        public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                            if (response.isSuccessful() && response.body() != null && currentPokemonId == pokemonId) {
                                String type = response.body().getPrimaryType();
                                int lightColor = TypeColors.getTypeLightColor(type);
                                int darkColor = TypeColors.getTypeColor(type);

                                // Guarda los colores en caché para no pedirlos otra vez
                                ColorCache.getInstance().putColors(pokemonId, lightColor, darkColor);

                                // Aplica los colores a la tarjeta
                                cardView.setCardBackgroundColor(lightColor);
                                textViewName.setTextColor(darkColor);
                            }
                        }

                        @Override
                        // La API falló, deja los colores gris por defecto
                        public void onFailure(Call<PokemonDetail> call, Throwable t) {
                        }


                    });
        }
    }
}