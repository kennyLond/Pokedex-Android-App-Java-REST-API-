package com.example.pokedex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;
import com.example.pokedex.adapters.PokemonAdapter;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.presenters.PokemonListPresenter;
import com.example.pokedex.views.PokemonListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PokemonListView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private ImageView imageViewFavorites;
    private LinearLayout layoutError;
    private TextView txtErrorTitle;
    private TextView txtErrorMessage;
    private Button btnRetry;

    private PokemonAdapter adapter;
    private PokemonListPresenter presenter;
    private List<Pokemon> allPokemonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ocultar ActionBar nativa
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewPokemons);
        progressBar = findViewById(R.id.progressBar);
        searchEditText = findViewById(R.id.searchEditText);
        imageViewFavorites = findViewById(R.id.imageViewFavorites);
        layoutError = findViewById(R.id.layoutError);
        txtErrorTitle = findViewById(R.id.txtErrorTitle);
        txtErrorMessage = findViewById(R.id.txtErrorMessage);
        btnRetry = findViewById(R.id.btnRetry);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new PokemonAdapter(new PokemonAdapter.OnPokemonClickListener() {
            @Override
            public void onPokemonClick(Pokemon pokemon) {
                openDetailActivity(pokemon);
            }
        });
        recyclerView.setAdapter(adapter);

        // Inicializar presenter
        presenter = new PokemonListPresenter(this);

        // Configurar botón de reintento
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reintentar cargar los pokemones
                presenter.loadPokemonList(151, 0);
            }
        });

        // Cargar pokemones
        presenter.loadPokemonList(151, 0);

        // Configurar búsqueda
        setupSearch();

        // Configurar botón de favoritos
        imageViewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPokemon(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterPokemon(String query) {
        if (query.isEmpty()) {
            adapter.setPokemonList(allPokemonList);
            return;
        }

        List<Pokemon> filteredList = new ArrayList<>();
        for (Pokemon pokemon : allPokemonList) {
            if (pokemon.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(pokemon);
            }
        }
        adapter.setPokemonList(filteredList);
    }

    private void openDetailActivity(Pokemon pokemon) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("pokemon_id", pokemon.getId());
        intent.putExtra("pokemon_name", pokemon.getName());
        intent.putExtra("pokemon_url", pokemon.getUrl());
        startActivity(intent);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE); // Ocultar error al cargar
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE); // Asegurar que error esté oculto
    }

    @Override
    public void showPokemonList(List<Pokemon> pokemonList) {
        this.allPokemonList = new ArrayList<>(pokemonList);
        adapter.setPokemonList(pokemonList);
        recyclerView.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE); // Ocultar error al tener datos
    }

    @Override
    public void showError(String message) {
        // Ocultar loading y RecyclerView
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Configurar y mostrar layout de error
        txtErrorTitle.setText("Error de conexión");
        txtErrorMessage.setText("No se pudo conectar al servidor. Verifica tu conexión a internet.");
        layoutError.setVisibility(View.VISIBLE);

        // Mantener el Toast para compatibilidad
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}