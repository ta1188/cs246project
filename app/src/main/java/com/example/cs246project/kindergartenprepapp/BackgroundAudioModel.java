package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import static android.content.Context.MODE_PRIVATE;

/**
 * The Background Audio Model supports the playing on theme music and can be muted by
 * the menu screen button.
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-21
 */

public class BackgroundAudioModel {

    private Boolean _isBackgroundAudioPlayable = true;
    private MediaPlayer _themeMediaPlayer;

    public BackgroundAudioModel(Context context) {

        // get settings if audio has been disabled
        // retrieve from shared preferences
        final SharedPreferences settings = context.getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        _isBackgroundAudioPlayable = settings.getBoolean(AppConstants.sharePreferenceMusicPlayable, true);

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