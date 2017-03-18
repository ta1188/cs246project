package com.example.cs246project.kindergartenprepapp;

import java.util.List;

/**
 * A class for holding and managing image/audio file references.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class MediaModel<T> {

    // The "drawable" resource index of an image file
    private int _imageFileResourceIndex;

    // The "raw" resources indexes of audio files
    private List<Integer> _audioFileResourceIndexes;

    // The current index of an audio resource
    private int _currentAudioResourceIndex;

    // The "value" of the object.
    private T _value;

    /**
     * Non-Default Constructor
     * Constructs a MediaModel with image/audio resources and an associated value
     * @param imageFileResourceIndex used to set _imageFileResourceIndex
     * @param audioFileResourceIndexes used to set _audioFileResourceIndexes
     * @param value used to set _value
     */
    public MediaModel(int imageFileResourceIndex, List<Integer> audioFileResourceIndexes, T value) {
        _imageFileResourceIndex = imageFileResourceIndex;
        _audioFileResourceIndexes = audioFileResourceIndexes;
        _currentAudioResourceIndex = 0;
        _value = value;
    }

    /**
     * Get Image File Resource Index
     * Getter for the _imageFileResourceIndex.
     * @return _imageFileResourceIndex
     */
    public int getImageFileResourceIndex() {
        return _imageFileResourceIndex;
    }

    /**
     * Get Value
     * Getter for the _value.
     * @return _value
     */
    public T getValue() {
        return _value;
    }

    /**
     * Get Audio Source Index
     * Gets the current audio resource index from _audioFileResourceIndexes and then increments the
     * _currentAudioResourceIndex so clients use audio resources in succession.
     * @return the current audio resource index
     */
    public int getAudioSourceIndex() {
        if (!isAtEndOfAudio()) {
            int resourceIndex = _audioFileResourceIndexes.get(_currentAudioResourceIndex);
            _currentAudioResourceIndex++;
            return resourceIndex;
        } else {
            _currentAudioResourceIndex = 0;
            return _audioFileResourceIndexes.get(_currentAudioResourceIndex);
        }
    }

    /**
     * Reset Audio Index
     * Set the _currentAudioResourceIndex back to 0.
     */
    public void resetAudioIndex() {
        _currentAudioResourceIndex = 0;
    }

    /**
     * Is At End Of Audio
     * Has the client successively gone through all the audio resources?
     * @return if the _currentAudioResourceIndex has reached the end
     */
    public boolean isAtEndOfAudio() {
        return _currentAudioResourceIndex >= _audioFileResourceIndexes.size();
    }

    /**
     * Has Audio
     * Flag to know if there are any audio resources at all.
     * @return if there is audio resources or not.
     */
    public boolean hasAudio() {
        return _audioFileResourceIndexes.size() > 0;
    }
}
