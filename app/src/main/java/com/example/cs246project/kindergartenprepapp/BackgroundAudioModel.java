package com.example.cs246project.kindergartenprepapp;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.media.MediaPlayer;
        import android.util.Log;

        import java.io.IOException;

        import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Michael Lucero on 3/21/17.
 */

public class BackgroundAudioModel {

    private Boolean _isBackgroundAudioPlayable = true;
    private MediaPlayer _themeMediaPlayer;

    public BackgroundAudioModel(Context context) {

        // get settings if audio has been disabled
        // retrieve from shared preferences
        final SharedPreferences settings = context.getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        _isBackgroundAudioPlayable = settings.getBoolean("IS_MUSIC_PLAYABLE", true);

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
}