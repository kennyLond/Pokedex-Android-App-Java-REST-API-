package com.example.pokedex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;
import com.example.pokedex.adapters.PokemonAdapter;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.presenters.FavoritesPresenter;
import com.example.pokedex.views.FavoritesView;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoritesView {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private ImageView imageViewBack;
    private PokemonAdapter adapter;
    private FavoritesPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Ocultar ActionBar nativa
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        imageViewBack = findViewById(R.id.imageViewBack);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new PokemonAdapter(new PokemonAdapter.OnPokemonClickListener() {
            @Override
            public void onPokemonClick(Pokemon pokemon) {
                openDetailActivity(pokemon);
            }
        });
        recyclerView.setAdapter(adapter);

        // Configurar botón de regresar
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Inicializar presenter
        presenter = new FavoritesPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar favoritos cada vez que se vuelve a esta pantalla
        presenter.loadFavorites();
    }

    private void openDetailActivity(Pokemon pokemon) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("pokemon_id", pokemon.getId());
        intent.putExtra("pokemon_name", pokemon.getName());
        intent.putExtra("pokemon_url", pokemon.getUrl());
        startActivity(intent);
    }

    @Override
    public void showFavorites(List<Pokemon> favorites) {
        recyclerView.setVisibility(View.VISIBLE);
        textViewEmpty.setVisibility(View.GONE);
        adapter.setPokemonList(favorites);
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
        presenter.onDestroy();
    }
}