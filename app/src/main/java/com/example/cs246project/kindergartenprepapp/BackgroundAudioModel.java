package com.example.cs246project.kindergartenprepapp;

        import android.content.Context;
        import android.media.MediaPlayer;
        import android.util.Log;

        import java.io.IOException;

/**
 * Created by Michael Lucero on 3/21/17.
 */

public class BackgroundAudioModel {

    private Boolean _isBackgroundAudioPlayable = true;
    private MediaPlayer _themeMediaPlayer;

    public BackgroundAudioModel(Context context) {
        startBackgroundAudio(context);
    }

    public boolean isPlaying() {
        if (_themeMediaPlayer != null) {
            return _themeMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void startBackgroundAudio(Context context) {
        if (_isBackgroundAudioPlayable) {
            _themeMediaPlayer = MediaPlayer.create(context, R.raw.skiptap_theme);
            _themeMediaPlayer.setLooping(true);
            _themeMediaPlayer.setVolume(0.2f, 0.2f);
            _themeMediaPlayer.start();
        }
    }

    public void stopBackgroundAudio() {
        if (_themeMediaPlayer != null) {
            if (_themeMediaPlayer.isPlaying()) {
                _themeMediaPlayer.setLooping(false);
                _themeMediaPlayer.stop();
                _themeMediaPlayer.release();
                _themeMediaPlayer = null;
            }
        }
    }

    public void disableAudio(Boolean isBackgroundAudioPlayable) {
        _isBackgroundAudioPlayable = isBackgroundAudioPlayable;
    }


    public void pauseBackgroundAudio() {
        if (_themeMediaPlayer.isPlaying()) {
            _themeMediaPlayer.pause();
        }

    }

    public void resumeBackgroundAudio() {
        if (_themeMediaPlayer.isPlaying()) {
            _themeMediaPlayer.start();
        }
    }
}