package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
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

    /**
     * Non-Default Constructor
     * Constructs an instance of MediaButton with a model object for managing all of the image and
     * audio file resources.
     * @param context of the application
     * @param model used for managing resources
     */
    public MediaButton(Context context, MediaModel<T> model) {
        super(context);
        _model = model;

        // Set the image of the button
        setImageResource(_model.getImageFileResourceIndex());

        // Setup to play audio files when clicked/touched
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });
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
            _mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(!_model.isAtEndOfAudio())
                    {
                        // Reset the media player
                        _mediaPlayer.reset();

                        // Load the media player with a new audio resource
                        try {
                            AssetFileDescriptor afd = getContext().getResources().openRawResourceFd(_model.getAudioSourceIndex());
                            if (afd == null) return;
                            _mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            afd.close();
                            _mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Play the audio
                        _mediaPlayer.start();
                    }
                    else
                    {
                        // Release the media player
                        _mediaPlayer.release();

                        // Reset the index to point back to the first audio file
                        _model.resetAudioIndex();
                    }
                }
            });

            // Play the first audio track
            _mediaPlayer.start();
        }
    }

}
