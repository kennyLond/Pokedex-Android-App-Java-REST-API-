package com.example.pokedex.activities;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioAttributes;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;
import com.example.pokedex.adapters.PokemonAdapter;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.presenters.FavoritesPresenter;
import com.example.pokedex.utils.PokemonSoundPlayer;
import com.example.pokedex.views.FavoritesView;

import java.io.IOException;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoritesView {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private ImageView imageViewBack;
    private PokemonAdapter adapterFav;
    private FavoritesPresenter presenterFav;
    private PokemonSoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        // Ocultar ActionBar nativa
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        soundPlayer = new PokemonSoundPlayer(this);

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        imageViewBack = findViewById(R.id.imageViewBack);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapterFav = new PokemonAdapter(new PokemonAdapter.OnPokemonClickListener() {
            @Override
            public void onPokemonClick(Pokemon pokemon) {
                openDetailActivity(pokemon);
            }
            @Override
            public void onPokemonLongClick(Pokemon pokemon) {
                soundPlayer.play(pokemon.getName());
            }
        });
        // Recycler muestre lo que esta en e Adapter
        recyclerView.setAdapter(adapterFav);

        // Configurar botón de regresar
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Inicializar presenter
        presenterFav = new FavoritesPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar favoritos cada vez que se vuelve a esta pantalla
        presenterFav.loadFavorites();
    }

    private void openDetailActivity(Pokemon pokemon) {
        Intent intentDetail = new Intent(this, DetailActivity.class);
        // envia estos detalles necesario para que abra la ventana de details
        intentDetail.putExtra("pokemon_id", pokemon.getId());
        intentDetail.putExtra("pokemon_name", pokemon.getName());
        intentDetail.putExtra("pokemon_url", pokemon.getUrl());
        startActivity(intentDetail);
    }



    @Override
    public void showFavorites(List<Pokemon> favorites) {
        recyclerView.setVisibility(View.VISIBLE);
        textViewEmpty.setVisibility(View.GONE);
        adapterFav.setPokemonList(favorites);
    }

    @Override
    public void showEmptyMessage() {
        recyclerView.setVisibility(View.GONE);
        textViewEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenterFav.onDestroy();
    }
}