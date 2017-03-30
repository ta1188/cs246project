package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * A view for free drawing on a canvas.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class DrawView extends View {

    // I don't know just yet what this is used for...
    private Bitmap _bitmap;

    // The canvas for drawing on (like a paint canvas)
    private Canvas _canvas;

    // A of line ("path") the user draws on the canvas
    private Path _path;

    // The series of lines ("path") the user has drawn on the canvas
    private List<Path> _paths;

    // The index for the latest path drawn
    private int _currentPathIndex;

    // Variables for tracking the previous coordinates
    private float _previousX;
    private float _previousY;

    // The paint handles the color, style, and width of the stroke
    private Paint _paint;

    // The current color of the paint
    private int _currentColor;

    // Constructs a DrawView
    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        _path = new Path();
        _paths = new ArrayList<>();
        _currentPathIndex = -1;

        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(10f);
        randomizeCurrentColor();

        setBackgroundColor(Color.TRANSPARENT);
    }

    // Constructs a DrawView
    public DrawView(Context context) {
        super(context);

        _path = new Path();
        _paths = new ArrayList<>();
        _currentPathIndex = -1;

        _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(10f);
        randomizeCurrentColor();

        setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    /**
     * {@inheritDoc}
     * When the view is drawn on the screen, draw the path (or lines) on to the canvas that the user
     * has drawn already.
     */
    protected void onDraw(Canvas canvas) {
        // Draw the first path
        canvas.drawPath(_path, _paint);

        // Draw previous paths
        if (_paths != null) {
            for (int i = 0; i < _paths.size(); i++) {
                canvas.drawPath(_paths.get(i), _paint);
            }
        }

        // Invalidate so that the view will refresh with the changes
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        _bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        _canvas = new Canvas(_bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Which touch event action are we currently doing?
        switch(MotionEventCompat.getActionMasked(event)) {
            case (MotionEvent.ACTION_DOWN) :
                // We have just started our touch
                onTouchStart(event.getX(), event.getY());
                break;
            case (MotionEvent.ACTION_MOVE) :
                // We are moving our touch
                onTouchMove(event.getX(), event.getY());
                break;
            case (MotionEvent.ACTION_UP) :
                // Record the last path drawn
                onTouchEnd();
        }

        // Invalidate means to set the view's state as invalid,
        // forcing a redraw/refresh of the view (we don't want
        // it lagging so we invalidate/refresh with every touch)
        invalidate();

        // true = we've processed the event and are done with it
        // (otherwise it would propagate to other views that
        // might be on top or underneath this view - i.e. false).
        return true;
    }

    /**
     * OnTouchStart
     * Sets up for drawing (once the user moves their touch position) by resetting the
     * path's starting point and saving the (x, y) coordinates.
     *
     * @param x is the horizontal position of where the user is touching on the view
     * @param y is the vertical position of where the user is touching on the view
     */
    private void onTouchStart(float x, float y) {
        _path = new Path();

        // Move the path to where you are currently touching (otherwise a
        // line will be drawn from your last touch to this touch).
        _path.moveTo(x, y);

        // Set the previous coordinates to the current ones (track where the touch is)
        _previousX = x;
        _previousY = y;
    }

    /**
     * OnTouchMove
     * Draws a line from (_previousX, _previousY) to (x, y) and saves the (x, y) coordinates.
     *
     * @param x is the horizontal position of where the user is touching on the view
     * @param y is the vertical position of where the user is touching on the view
     */
    private void onTouchMove(float x, float y) {
        // Draw the line
        _path.quadTo(_previousX, _previousY, (x + _previousX) /2, (y + _previousY) /2);

        // Set the previous coordinates to the current ones (track where the touch is)
        _previousX = x;
        _previousY = y;
    }

    /**
     * OnTouchEnd
     * Saves the path that was just drawn to the list of paths
     */
    private void onTouchEnd() {
        // Draw the line
        if (_paths != null) {
            _paths.add(_path);
            ++_currentPathIndex;
        }
    }

    /**
     * Get the _paths drawn on the canvas.
     * @return the drawn paths
     */
    public List<Path> getPaths() {
        return _paths;
    }

    /**
     * Get the _paths drawn on the canvas.
     * @return the drawn paths
     */
    public void setPaths(List<Path> paths) {
        _paths = paths;
        draw(_canvas);
    }

    /**
     * ClearPreviousPath
     * Clears the view of the last path drawn
     */
    public void clearPreviousPath() {
        // Clear the path
        _path.reset();

        // Remove that path from the _paths
        if (!_paths.isEmpty() && _currentPathIndex < _paths.size() && _currentPathIndex >= 0) {
            _paths.remove(_currentPathIndex);
            --_currentPathIndex;
        }

        // Set the current path to the last one in the list of _paths
        if (!_paths.isEmpty() && _currentPathIndex >= 0) {
            _path = _paths.get(_currentPathIndex);
        }

        // Invalidate so that the view will refresh with the changes
        invalidate();
    }

    /**
     * ClearAllPaths
     * Clears the view of any drawings
     */
    public void clearAllPaths() {
        _path.reset();

        for (int i = 0; i < _paths.size(); i++) {
            _paths.get(i).reset();
        }

        // Invalidate so that the view will refresh with the changes
        invalidate();
    }

    /**
     * SetPaintColor
     * Sets the color of the paint to another color
     */
    public void randomizeCurrentColor() {
        int randomColor = _currentColor;
        Random rand = new Random();

        while (randomColor == _currentColor) {
            switch (rand.nextInt(5) + 1) {
                case 1: {
                    randomColor = Color.parseColor("#8CCC28");// Lime Green
                    break;
                }
                case 2: {
                    randomColor = Color.parseColor("#FF8333"); // Orange
                    break;
                }
                case 3: {
                    randomColor = Color.parseColor("#33AFFF"); // Sky Blue
                    break;
                }
                case 4: {
                    randomColor = Color.parseColor("#FF33AF"); // Pink
                    break;
                }
                case 5: {
                    randomColor = Color.parseColor("#BA28CC"); // Purple
                    break;
                }
                default: {
                    randomColor = Color.RED;
                    break;
                }
            }
        }

        _paint.setColor(randomColor);
    }
}