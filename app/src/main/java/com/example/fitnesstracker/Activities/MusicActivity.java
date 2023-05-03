package com.example.fitnesstracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.fitnesstracker.R;

public class MusicActivity extends AppCompatActivity {
    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton stopButton;
    private MediaPlayer mediaPlayer;
    public static MusicActivity instance;

    public static MusicActivity getInstance() {
        return instance;
    }

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

    public void play(View view)
    {
        if(mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            mediaPlayer.setOnCompletionListener(mp -> stopPlayer());
        }

        mediaPlayer.start();
    }

    public void pause(View view)
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
        }
    }

    public void stop(View view)
    {
        stopPlayer();
    }

    private void stopPlayer()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}