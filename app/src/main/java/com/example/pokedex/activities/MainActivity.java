package com.example.pokedex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import com.example.pokedex.utils.PokemonSoundPlayer;
import com.example.pokedex.views.PokemonListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PokemonListView {

    // Decalracion de los componentes UI
    private RecyclerView recyclerViewPrin;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private ImageView imageViewFavorites;
    private LinearLayout layoutError;
    private TextView txtErrorTitle;
    private TextView txtErrorMessage;
    private Button btnRetry;
    private PokemonAdapter adapterPrin;
    private PokemonListPresenter presenterPrin;

    private PokemonSoundPlayer soundPlayer;

    private TextToSpeech textToSpeech;

    private int limit = 151;

    // array de los pokemos obtenidos para la opcion de busqueda
    private List<Pokemon> allPokemonList = new ArrayList<>();



    // preparar la pantalla
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlayer = new PokemonSoundPlayer(this);

        // Inicializar vistas
        recyclerViewPrin = findViewById(R.id.recyclerViewPokemons);
        progressBar = findViewById(R.id.progressBar);
        searchEditText = findViewById(R.id.searchEditText);
        imageViewFavorites = findViewById(R.id.imageViewFavorites);
        layoutError = findViewById(R.id.layoutError);
        txtErrorTitle = findViewById(R.id.txtErrorTitle);
        txtErrorMessage = findViewById(R.id.txtErrorMessage);
        btnRetry = findViewById(R.id.btnRetry);

        // Ocultar ActionBar nativa
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Configurar RecyclerView
        //gridLayoutManager diseño de cuadricula, this es la pantalla y se divide en 2
        recyclerViewPrin.setLayoutManager(new GridLayoutManager(this, 2));


        //Inicializamos la voz
        startroboticvoice();


        // variable donde guardamos el adaptador,  creamos una nueva instancia, y de la clase pokemonadapter, creamos una nueva implementacion de onPokemons
        adapterPrin = new PokemonAdapter(new PokemonAdapter.OnPokemonClickListener() {
            @Override
            public void onPokemonClick(Pokemon pokemon) {
                openDetailActivity(pokemon);
            }

            @Override
            public void onPokemonLongClick(Pokemon pokemon) {
                playName(pokemon.getName());
            }
        });
// le dice al reciclerView que los datos a mostrar son los del adapter
        recyclerViewPrin.setAdapter(adapterPrin);

        // Inicializar presenter
        presenterPrin = new PokemonListPresenter(this);

        // Configurar botón de reintento
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reintentar cargar los pokemones
                presenterPrin.loadPokemonList(limit);
            }
        });

        // Cargar pokemones
        presenterPrin.loadPokemonList(limit);

        // Configurar búsqueda
        setupSearch();

        // Configurar botón de favoritos
        imageViewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            //ir de esta pantalla a favoritos
            public void onClick(View v) {
                Intent fav = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(fav);
            }
        });
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            //cambiar s
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
            adapterPrin.setPokemonList(allPokemonList);
            return;
        }

        List<Pokemon> filteredList = new ArrayList<>();
        for (Pokemon pokemon : allPokemonList) {
            //contains busca coincidencias entre pokemones y las letras
            if (pokemon.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(pokemon);
            }
        }
        adapterPrin.setPokemonList(filteredList);
    }

    private void openDetailActivity(Pokemon pokemon) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("pokemon_id", pokemon.getId());
        intent.putExtra("pokemon_name", pokemon.getName());
        intent.putExtra("pokemon_url", pokemon.getUrl());
        startActivity(intent);
    }


    private void startroboticvoice() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = textToSpeech.setLanguage(new Locale("es","Es"));

                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this,
                                "Idioma no soportado", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            }
        });
    }

    private void playName(String PokemonName) {
        if (textToSpeech != null) {

            textToSpeech.stop();

            textToSpeech.setSpeechRate(0.85f);
            textToSpeech.setPitch(1.0f);

            textToSpeech.speak(PokemonName, TextToSpeech.QUEUE_FLUSH, null, null);


        } else {
            Toast.makeText(this, "Voz no inicializada", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewPrin.setVisibility(View.GONE);
        layoutError.setVisibility(View.GONE); // Ocultar error al cargar
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerViewPrin.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE); // Asegurar que error esté oculto
    }

    @Override
    public void showPokemonList(List<Pokemon> pokemonList) {
        this.allPokemonList = new ArrayList<>(pokemonList);
        adapterPrin.setPokemonList(pokemonList);
        recyclerViewPrin.setVisibility(View.VISIBLE);
        layoutError.setVisibility(View.GONE); // Ocultar error al tener datos
    }

    @Override
    public void showError(String message) {
        // Ocultar loading y RecyclerView
        progressBar.setVisibility(View.GONE);
        recyclerViewPrin.setVisibility(View.GONE);

        // Configurar y mostrar layout de error
        txtErrorTitle.setText("Error de conexión");
        txtErrorMessage.setText("No se pudo conectar al servidor. Verifica tu conexión a internet.");
        layoutError.setVisibility(View.VISIBLE);

        // Mantener el Toast para compatibilidad
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();


        presenterPrin.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



}