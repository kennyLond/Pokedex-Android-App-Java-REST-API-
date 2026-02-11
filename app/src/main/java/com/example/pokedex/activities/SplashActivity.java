package com.example.pokedex.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pokedex.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Esperar 2 segundos y luego ir a MainActivity
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, SPLASH_DURATION);

        ImageView pokeball = findViewById(R.id.imageView2);
        pokeball.animate()
                .rotationBy(360f)
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(1500)
                .withEndAction(() ->
                        pokeball.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(300)
                )
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}