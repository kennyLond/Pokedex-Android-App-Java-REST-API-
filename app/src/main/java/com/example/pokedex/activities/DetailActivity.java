package com.example.pokedex.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.models.Pokemon;
import com.example.pokedex.models.PokemonDetail;
import com.example.pokedex.models.Stats;
import com.example.pokedex.presenters.PokemonDetailPresenter;
import com.example.pokedex.views.PokemonDetailView;
import com.example.pokedex.utils.TypeColors;
import com.example.pokedex.utils.PokemonSoundPlayer;

public class DetailActivity extends AppCompatActivity implements PokemonDetailView {

    private ImageView imageViewPokemon;
    private ImageView imageViewHeart;
    private ImageView imageViewBack;
    private ImageView imageViewSound;
    private TextView textViewName;
    private TextView textViewType;
    private TextView textViewDescription;
    private TextView textViewHeight;
    private TextView textViewWeight;
    private TextView textViewFavorite;
    private LinearLayout layoutStats;
    private LinearLayout layoutFavorite;
    private LinearLayout layoutSound;
    private ProgressBar progressBar;
    private LinearLayout contentLayout;
    private PokemonDetailPresenter presenter;
    private Pokemon currentPokemon;
    private PokemonSoundPlayer soundPlayer;

    // Variables para detectar doble click
    private static final long DOUBLE_CLICK_TIME = 300;
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Ocultar ActionBar nativa
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar reproductor de sonido
        soundPlayer = new PokemonSoundPlayer(this);

        // Configurar listener para cuando termine el sonido
        soundPlayer.setOnSoundCompletionListener(new PokemonSoundPlayer.OnSoundCompletionListener() {
            @Override
            public void onSoundCompleted() {
                imageViewSound.setImageResource(R.drawable.ic_volume_off);
            }
        });

        // Inicializar vistas
        imageViewPokemon = findViewById(R.id.imageViewPokemon);
        imageViewHeart = findViewById(R.id.imageViewHeart);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewSound = findViewById(R.id.imageViewSound);
        textViewName = findViewById(R.id.textViewName);
        textViewType = findViewById(R.id.textViewType);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewHeight = findViewById(R.id.textViewHeight);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewFavorite = findViewById(R.id.textViewFavorite);
        layoutStats = findViewById(R.id.layoutStats);
        layoutFavorite = findViewById(R.id.layoutFavorite);
        layoutSound = findViewById(R.id.layoutSound);
        progressBar = findViewById(R.id.progressBar);
        contentLayout = findViewById(R.id.contentLayout);

        presenter = new PokemonDetailPresenter(this, this);

        // Obtener datos del Intent desde activity main
        int pokemonId = getIntent().getIntExtra("pokemon_id", 1);
        String pokemonName = getIntent().getStringExtra("pokemon_name");
        String pokemonUrl = getIntent().getStringExtra("pokemon_url");

        currentPokemon = new Pokemon(pokemonName, pokemonUrl);
        presenter.setCurrentPokemon(currentPokemon);

        // Cargar detalles
        presenter.loadPokemonDetail(pokemonId);

        // Configurar botón de favorito
        layoutFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.toggleFavorite();
            }
        });

        // Configurar botón de sonido con doble click
        layoutSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long clickTime = System.currentTimeMillis();

                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME) {

                    playPokemonSound();
                    lastClickTime = 0;
                } else {

                    lastClickTime = clickTime;
                }
            }
        });

        // Configurar botón de regresar
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void playPokemonSound() {
        if (currentPokemon != null) {
            soundPlayer.play(currentPokemon.getName());
            imageViewSound.setImageResource(R.drawable.ic_volume_up);
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPokemonDetail(PokemonDetail pokemonDetail) {
        // Nombre
        String capitalizedName = pokemonDetail.getName().substring(0, 1).toUpperCase()
                + pokemonDetail.getName().substring(1);
        textViewName.setText(capitalizedName);

        // Mostrar el tipo
        String primaryType = pokemonDetail.getPrimaryType();
        String translatedType = translateTypeName(primaryType);
        textViewType.setText("Tipo: " + translatedType);

        // MOSTRAR DESCRIPCIÓN
        String description = pokemonDetail.getDescription();
        if (description != null && !description.isEmpty()) {
            textViewDescription.setText(description);
        } else {
            textViewDescription.setText("Descripción no disponible");
        }

        // Altura (viene en decímetros, convertir a metros)
        double heightInMeters = pokemonDetail.getHeight() / 10.0;
        textViewHeight.setText(String.format("%.1f m", heightInMeters));

        // Peso (viene en hectogramos, convertir a kg)
        double weightInKg = pokemonDetail.getWeight() / 10.0;
        textViewWeight.setText(String.format("%.1f kg", weightInKg));

        // Imagen
        Glide.with(this)
                .load(pokemonDetail.getImageUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(imageViewPokemon);

        // Cambiar color de fondo según el tipo
        int typeColor = TypeColors.getTypeColor(primaryType);

        // Cambiar el color de fondo del layout principal
        contentLayout.setBackgroundColor(TypeColors.getTypeLightColor(primaryType));

        // Cambiar color de la etiqueta del tipo
        textViewType.setBackgroundColor(typeColor);

        // Cambiar color del botón de favorito
        layoutFavorite.setBackgroundColor(typeColor);

        // Cambiar color del botón de sonido
        layoutSound.setBackgroundColor(typeColor);

        // Estadísticas
        displayStats(pokemonDetail);
    }

    //  Método para traducir nombres de tipos
    private String translateTypeName(String typeName) {
        switch (typeName.toLowerCase()) {
            case "normal":
                return "Normal";
            case "fire":
                return "Fuego";
            case "water":
                return "Agua";
            case "electric":
                return "Eléctrico";
            case "grass":
                return "Planta";
            case "ice":
                return "Hielo";
            case "fighting":
                return "Lucha";
            case "poison":
                return "Veneno";
            case "ground":
                return "Tierra";
            case "flying":
                return "Volador";
            case "psychic":
                return "Psíquico";
            case "bug":
                return "Bicho";
            case "rock":
                return "Roca";
            case "ghost":
                return "Fantasma";
            case "dragon":
                return "Dragón";
            case "dark":
                return "Siniestro";
            case "steel":
                return "Acero";
            case "fairy":
                return "Hada";
            default:
                return typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
        }
    }

    private void displayStats(PokemonDetail pokemonDetail) {
        layoutStats.removeAllViews();
//hace un recorrido en cada una de las estadisticas, las crea, agrega color, barra de progreso y valor
        // Obtener el color del tipo del pokemon
        String primaryType = pokemonDetail.getPrimaryType();
        int typeColor = TypeColors.getTypeColor(primaryType);

        if (pokemonDetail.getStats() != null) {
            for (Stats stat : pokemonDetail.getStats()) {
                // inflamos las vistas desde item_stats
                View statView = getLayoutInflater().inflate(R.layout.item_stat, layoutStats, false);

                TextView statName = statView.findViewById(R.id.textViewStatName);
                TextView statValue = statView.findViewById(R.id.textViewStatValue);
                ProgressBar progressBarStat = statView.findViewById(R.id.progressBarStat);

                // Traducir nombres de stats
                String translatedName = translateStatName(stat.getStat().getName());
                statName.setText(translatedName);
                statValue.setText(String.valueOf(stat.getBaseStat()));

                // Configurar la barra de progreso
                progressBarStat.setMax(255);
                progressBarStat.setProgress(stat.getBaseStat());

                // Aplicar el color del tipo a la barra
                progressBarStat.setProgressTintList(android.content.res.ColorStateList.valueOf(typeColor));

                // Cambiar también el color del valor numérico
                statValue.setTextColor(typeColor);

                layoutStats.addView(statView);
            }
        }
    }

    private String translateStatName(String statName) {
        switch (statName) {
            case "hp":
                return "HP";
            case "attack":
                return "Ataque";
            case "defense":
                return "Defensa";
            case "special-attack":
                return "Ataque Especial";
            case "special-defense":
                return "Defensa Especial";
            case "speed":
                return "Velocidad";
            default:
                return statName;
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFavoriteButton(boolean isFavorite) {
        if (isFavorite) {
            imageViewHeart.setImageResource(R.drawable.ic_heart_filled);
            textViewFavorite.setText("Quitar de favoritos");

        } else {
            imageViewHeart.setImageResource(R.drawable.ic_heart_outline);
            textViewFavorite.setText("Añadir a favoritos");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPlayer.stop();
        presenter.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPlayer.pause();
        imageViewSound.setImageResource(R.drawable.ic_volume_off);
    }
}