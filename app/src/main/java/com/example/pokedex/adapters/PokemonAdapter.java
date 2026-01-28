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

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList;
    private OnPokemonClickListener listener;

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    public PokemonAdapter(OnPokemonClickListener listener) {
        this.pokemonList = new ArrayList<>();
        this.listener = listener;
    }

    public void setPokemonList(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.bind(pokemon, listener);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imageViewPokemon;
        private ProgressBar progressBarImage;
        private TextView textViewName;
        private int currentPokemonId = -1;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            imageViewPokemon = itemView.findViewById(R.id.imageViewPokemon);
            progressBarImage = itemView.findViewById(R.id.progressBarImage);
            textViewName = itemView.findViewById(R.id.textViewPokemonName);
        }

        public void bind(final Pokemon pokemon, final OnPokemonClickListener listener) {
            currentPokemonId = pokemon.getId();

            // Capitalizar el nombre
            String capitalizedName = pokemon.getName().substring(0, 1).toUpperCase()
                    + pokemon.getName().substring(1);
            textViewName.setText(capitalizedName);

            //  el spinner ANTES de cargar la imagen
            progressBarImage.setVisibility(View.VISIBLE);

            // Cargar imagen con Glide CON LISTENER para ocultar el spinner
            Glide.with(itemView.getContext())
                    .load(pokemon.getImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            // OCULTAR spinner si la carga FALLA
                            progressBarImage.setVisibility(View.GONE);
                            return false; // Permite que Glide maneje el placeholder
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource,
                                                       boolean isFirstResource) {
                            // OCULTAR spinner cuando la imagen CARGA EXITOSAMENTE
                            progressBarImage.setVisibility(View.GONE);
                            return false; // Permite que Glide muestre la imagen
                        }
                    })
                    .into(imageViewPokemon);

            // Verificar si ya tenemos el color en caché
            ColorCache cache = ColorCache.getInstance();
            if (cache.hasColors(pokemon.getId())) {
                // Ya tenemos el color, aplicarlo directamente
                int lightColor = cache.getLightColor(pokemon.getId());
                int darkColor = cache.getDarkColor(pokemon.getId());
                cardView.setCardBackgroundColor(lightColor);
                textViewName.setTextColor(darkColor);
            } else {
                // No tenemos el color, establecer color por defecto y pedirlo
                cardView.setCardBackgroundColor(Color.parseColor("#E0E0E0"));
                textViewName.setTextColor(Color.parseColor("#666666"));
                loadPokemonType(pokemon.getId());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onPokemonClick(pokemon);
                    }
                }
            });
        }

        private void loadPokemonType(final int pokemonId) {
            RetrofitClient.getPokeApiService()
                    .getPokemonDetail(pokemonId)
                    .enqueue(new Callback<PokemonDetail>() {
                        @Override
                        public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                            // Verificar que esta vista sigue mostrando el mismo pokemon
                            if (response.isSuccessful() && response.body() != null && currentPokemonId == pokemonId) {
                                String type = response.body().getPrimaryType();
                                int lightColor = TypeColors.getTypeLightColor(type);
                                int darkColor = TypeColors.getTypeColor(type);

                                // Guardar en caché
                                ColorCache.getInstance().putColors(pokemonId, lightColor, darkColor);

                                // Aplicar colores
                                cardView.setCardBackgroundColor(lightColor);
                                textViewName.setTextColor(darkColor);
                            }
                        }

                        @Override
                        public void onFailure(Call<PokemonDetail> call, Throwable t) {
                            // Si falla, dejar colores por defecto
                        }
                    });
        }
    }
}