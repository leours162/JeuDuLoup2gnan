package com.example.jeuduloup2.View;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    private static MediaPlayer mediaPlayer;
    private static double currentVolume = 0.5;

    public static void initMusic() {
        if (mediaPlayer == null) {
            String path = SoundManager.class.getResource("/com/example/jeuduloup2/musique.mp3").toExternalForm();
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(currentVolume);
            mediaPlayer.play();
        } else if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mediaPlayer.play();
        }
    }

    public static void setVolume(double volume) {
        currentVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    public static double getVolume() {
        return currentVolume;
    }
}
