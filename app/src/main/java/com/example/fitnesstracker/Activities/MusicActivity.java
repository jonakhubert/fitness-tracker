package com.example.fitnesstracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.fitnesstracker.R;

/**
 * Represents the MusicActivity class for playing music.
 */
public class MusicActivity extends AppCompatActivity
{
    /**
     * Button used to play the music.
     */
    private ImageButton playButton;
    /**
     * Button used to pause the music.
     */
    private ImageButton pauseButton;
    /**
     * Button used to stop the music.
     */
    private ImageButton stopButton;
    /**
     * MediaPlayer instance for playing the music.
     */
    private MediaPlayer mediaPlayer;
    /**
     * Instance of the MusicActivity class.
     */
    public static MusicActivity instance;

    /**
     * Gets the instance of the MusicActivity.
     *
     * @return The instance of MusicActivity.
     */
    public static MusicActivity getInstance()
    {
        return instance;
    }

    /**
     * Called when the activity is created.
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        instance = this;

        playButton = findViewById(R.id.play_btn);
        pauseButton = findViewById(R.id.pause_btn);
        stopButton = findViewById(R.id.stop_btn);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
    }

    /**
     * Plays the music.
     *
     * @param view The view that triggered the event.
     */
    public void play(View view)
    {
        if(mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            mediaPlayer.setOnCompletionListener(mp -> stopPlayer());
        }

        mediaPlayer.start();
    }

    /**
     * Pauses the music.
     *
     * @param view The view that triggered the event.
     */
    public void pause(View view)
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
        }
    }

    /**
     * Stops the music.
     *
     * @param view The view that triggered the event.
     */
    public void stop(View view)
    {
        stopPlayer();
    }

    /**
     * Stops the MediaPlayer and releases resources.
     */
    private void stopPlayer()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Stop the MediaPlayer when the activity is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}