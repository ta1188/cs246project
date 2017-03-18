package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;

/**
 * An Image Button that plays audio when clicked/touched.
 * <p>
 * @author  Michael Lucero & Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class MediaButton<T> extends android.support.v7.widget.AppCompatImageButton {

    // The media player used to play the audio when clicked
    private MediaPlayer _mediaPlayer;

    // Thee model object that handles image/audio resources
    private MediaModel<T> _model;

    // The audio handler that knows what to do when all audio is complete.
    private AudioHandler _audioHandler;

    // The On Completion Listener for when the button's audio is played.
    private MediaPlayer.OnCompletionListener _onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if(!_model.isAtEndOfAudio()) {
                if (_mediaPlayer != null) {
                    // Release the media player
                    _mediaPlayer.release();
                }
                // Reset the _mediaPlayer to have the next audio file
                _mediaPlayer = MediaPlayer.create(getContext(), _model.getAudioSourceIndex());

                // Set the onCompletionListener to this member variable (recursion?)
                _mediaPlayer.setOnCompletionListener(_onCompletionListener);

                // Play the audio
                _mediaPlayer.start();
            }
            else {
                if (_mediaPlayer != null) {
                    // Release the media player
                    _mediaPlayer.release();
                }

                // Reset the index to point back to the first audio file
                _model.resetAudioIndex();

                // Call the audio handler's onAudioComplete
                if (_audioHandler != null) {
                    _audioHandler.onAudioComplete();
                }
            }
        }
    };

    /**
     * Non-Default Constructor
     * Constructs an instance of MediaButton with a model object for managing all of the image and
     * audio file resources.
     * @param context of the application
     * @param model used for managing resources
     */
    public MediaButton(Context context, MediaModel<T> model, AudioHandler audioHandler) {
        super(context);
        _model = model;
        _audioHandler = audioHandler;

        // Set the image of the button
        setImageResource(_model.getImageFileResourceIndex());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(getClass().getName(), "onTouch entered");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            playAudio();
        }

        // Consume the touch event so nothing else will respond to the touch
        return true;
    }

    /**
     * Get Value
     * Gets the "value" associated with the model.
     * @return the model's value.
     */
    public T getValue() {
        return _model.getValue();
    }

    /**
     * Play Audio
     * Plays any/all audio resources stored in the model.
     */
    private void playAudio() {
        // Only play audio if you have it.
        if (_model.hasAudio()) {
            // Initialize the media player with the first audio resource
            _mediaPlayer = MediaPlayer.create(getContext(), _model.getAudioSourceIndex());

            // Set the on completion listener to play all other audio resources when each is complete
            _mediaPlayer.setOnCompletionListener(_onCompletionListener);

            // Play the first audio track
            _mediaPlayer.start();
        }
    }

}
