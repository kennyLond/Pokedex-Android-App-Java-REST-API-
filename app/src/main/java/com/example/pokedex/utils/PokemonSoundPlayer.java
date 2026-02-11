package com.example.pokedex.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;

public class PokemonSoundPlayer {

    private MediaPlayer mediaPlayer;
    private Context context;
    private OnSoundCompletionListener listener;

    // Interfaz para notificar cuando termina el sonido
    public interface OnSoundCompletionListener {
        void onSoundCompleted();
    }

    public PokemonSoundPlayer(Context context) {
        this.context = context;
    }

    // Método para configurar el listener
    public void setOnSoundCompletionListener(OnSoundCompletionListener listener) {
        this.listener = listener;
    }

    public void play(String pokemonName) {
        String soundUrl = "https://play.pokemonshowdown.com/audio/cries/" +
                pokemonName.toLowerCase() + ".mp3";

        stop();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );

            mediaPlayer.setDataSource(soundUrl);

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mediaPlayer = null;

                    // Notificar que el sonido terminó
                    if (listener != null) {
                        listener.onSoundCompleted();
                    }
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(context, "Error al reproducir sonido",
                            Toast.LENGTH_SHORT).show();

                    // Notificar también en caso de error
                    if (listener != null) {
                        listener.onSoundCompleted();
                    }
                    return true;
                }
            });

            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al cargar el sonido",
                    Toast.LENGTH_SHORT).show();


            if (listener != null) {
                listener.onSoundCompleted();
            }
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
}