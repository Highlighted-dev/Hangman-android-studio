package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class game extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    StringBuilder wordToGuess_ = new StringBuilder(), letters = new StringBuilder();
    String wordToGuess, playerInput, allChars = "abcdefghijklmnopqrstuvwxyz",  category;
    int badGuesses = 1;
    Handler myHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView wordToGuessTextView = findViewById(R.id.wordToGuessTextView);
        TextView lettersTextView = findViewById(R.id.letters);
        TextView categoryTextView = findViewById(R.id.category);
        TextView won = findViewById(R.id.won);
        TextView lost = findViewById(R.id.lost);
        Button btnEnter = findViewById(R.id.btnEnter);
        ImageView ivExit = findViewById(R.id.exit2);
        EditText input_enterALetter = findViewById(R.id.input_enterALetter);
        ImageView ivHangman = findViewById(R.id.hangman);
        ImageView volume = findViewById(R.id.volume);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        //Here the program will randomly select category, then word to guess from this category.
        Random rand = new Random();
        String[] vegetables = { "Cabbage", "Eggplant", "Carrot","Courgette","Artichoke","Beet","Broccoli","Cauliflower","Cucumber","Leek","Lettuce","Mushroom","Onion","Pea","Pepper","Potato","Pumpkin","Radish",
                "Zucchini", "Brussels","Corn","Tomato","Celery" };
        String[] fruits = {"apple", "apricot","avocado","banana","berry","cantaloupe","cherry","citron","citrus","coconut", "date", "fig", "grape", "guava","kiwi","lemon","lime", "mango", "melon","mulberry",
                "nectarine","orange", "papaya", "peach", "pear", "pineapple", "plum", "prune", "raisin", "raspberry", "tangerine"};
        String path = "/vegetables.txt";
        switch(rand.nextInt(2))
        {
            case 0:
                wordToGuess = vegetables[(int)(Math.random() * vegetables.length)].toLowerCase();
                categoryTextView.setHint("Category: Vegetables");
                break;
            case 1:
                wordToGuess = fruits[(int)(Math.random() * vegetables.length)].toLowerCase();
                categoryTextView.setHint("Category: Fruits");
                break;
        }



        //Changing letters to "_" so player can't see the word
        for (int i = 0; i<wordToGuess.length();i++)
        {
            wordToGuess_.append("_ ");
            wordToGuessTextView.setText(wordToGuess_);
        }
        //The wordToGuess string must match the wordToGuess_ string  so the program will easily check if the letter entered by user is in this word, that's why program  here will add space after every char
        char[] wordToGuessArray = wordToGuess.toCharArray();
        StringBuilder temp = new StringBuilder();
        for(char c : wordToGuessArray)
        {
            temp.append(c).append(" ");
        }
        wordToGuess = temp.toString();

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    boolean wrongGuess = true;
                    //Getting an user input
                    playerInput = input_enterALetter.getText().toString().toLowerCase();

                    //Checking if the letter, that player entered, is in string wordToGuess
                    for(int i = 0; i < wordToGuess.length(); i++)
                    {
                        //If character that player has entered is in word to guess && If character that player has entered is a letter && If the character that player has entered wasn't entered before
                        if(wordToGuess.charAt(i) == playerInput.charAt(0)  &&  allChars.contains(playerInput) && letters.indexOf(playerInput.toUpperCase())==-1)
                        {
                            wordToGuess_.setCharAt(i, playerInput.charAt(0));
                            wordToGuessTextView.setText(wordToGuess_);
                            wrongGuess = false;
                        }
                    }
                    //Program will add a letter that player has entered to the textview.
                    if(allChars.contains(playerInput) && letters.indexOf(playerInput.toUpperCase())==-1)
                    {
                        letters.append(playerInput.toUpperCase()).append(", ");
                        lettersTextView.setText(letters);
                    }

                    if(wrongGuess)
                    {
                        badGuesses += 1;
                        ivHangman.setImageResource(getResources().getIdentifier("hangman"  + badGuesses, "drawable", getPackageName()));
                        //If the player get wrong guess 6 times, the program will display "You lost" and go back to the menu.
                        if(badGuesses == 7) {
                            lost.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.FadeInDown).duration(1000).playOn(lost);
                            new Timer().schedule(new TimerTask(){
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mediaPlayer.stop();
                                            finish();
                                            startActivity(new Intent(game.this, MainActivity.class));

                                        }
                                    });
                                }
                            }, 3000, 100000000);
                        }
                    }

                    //If there's no "_" left in wordToGuess_, display "You won"
                    if(wordToGuess_.indexOf("_") == -1)
                    {
                        won.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.FadeInDown).duration(1000).playOn(won);
                        new Timer().schedule(new TimerTask(){
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mediaPlayer.stop();
                                        startActivity(new Intent(game.this, MainActivity.class));
                                    }
                                });
                            }
                        }, 3000, 100000000);
                    }

            }

        });
        //Stopping and starting song on click
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volume.getTag().toString().equals("volume")) {
                    volume.setImageResource(R.drawable.nosound);
                    volume.setTag("nosound");
                    mediaPlayer.stop();
                }
                else{
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.theme);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                    volume.setImageResource(R.drawable.volume);
                    volume.setTag("volume");

                }
            }
        });
        //Exit to menu
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                startActivity(new Intent(game.this, MainActivity.class));
            }
        });
    }


}