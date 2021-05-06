package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnPlay = findViewById(R.id.play);
        ImageView volume2 = findViewById(R.id.volume2);

        //Creating media player to play some music in app

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();



        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                finish();
                startActivity(new Intent(MainActivity.this, game.class));

            }
        });
        //Stopping and starting song on click
        volume2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volume2.getTag().toString().equals("volume")) {
                    volume2.setImageResource(R.drawable.nosound);
                    volume2.setTag("nosound");
                    mediaPlayer.stop();
                }
                else{
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.theme);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    volume2.setImageResource(R.drawable.volume);
                    volume2.setTag("volume");

                }
            }
        });
    }

}