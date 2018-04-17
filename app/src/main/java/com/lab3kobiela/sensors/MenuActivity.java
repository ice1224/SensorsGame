package com.lab3kobiela.sensors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import static com.lab3kobiela.sensors.Settings.MAX_SIZE;
import static com.lab3kobiela.sensors.Settings.MIN_SIZE;
import static com.lab3kobiela.sensors.Settings.NUMBER_OF_ROUNDS_PER_LEVEL;
import static com.lab3kobiela.sensors.Settings.listOfLists;

public class MenuActivity extends AppCompatActivity {

    Button bStartGame, bAddRound, bExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bStartGame = (Button) findViewById(R.id.bStartGame);
        bAddRound = (Button) findViewById(R.id.bAddRound);
        bExit = (Button) findViewById(R.id.bExit);

        addListeners();
        if (listOfLists.size() < MAX_SIZE - MIN_SIZE + 1) createRounds2();
    }

    public void addListeners() {

        bStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(MenuActivity.this, GameActivity.class);
                startActivity(intentMain);
            }
        });

        bAddRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(MenuActivity.this, AddRoundActivity.class);
                startActivity(intentAdd);
            }
        });

        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

    }

    public void createRounds2() {
        ArrayList<ArrayList<String>> newLevel;
        ArrayList<String> newRound;
        String previous = "";
        for (int i = 0; i < MAX_SIZE - MIN_SIZE + 1; i++) {
            newLevel = new ArrayList<>();
            for (int k = 0; k < NUMBER_OF_ROUNDS_PER_LEVEL * 2; k++) {
                newRound = new ArrayList<>();
                for (int j = 0; j < (i + MIN_SIZE); j++) {
                    previous = oneRandStep(previous);
                    newRound.add(previous);
                }
                newLevel.add(newRound);
            }
            listOfLists.add(newLevel);
        }
    }


    public String oneRandStep(String prev) {
        Random r = new Random();
        String[] arOfAvSteps = {"NORMAL", "UPSIDE DOWN", "ON RIGHT SIDE", "ON LEFT SIDE", "SCREEN UP", "SCREEN DOWN"};
        String current;
        do {
            current = arOfAvSteps[r.nextInt(arOfAvSteps.length)];
        } while (!checkPrevious(prev, current));

        return current;
    }

    public boolean checkPrevious(String previous, String current) {
        boolean result = true;
        if ((previous.equals("NORMAL") || previous.equals("UPSIDE DOWN")) && (current.equals("NORMAL") || current.equals("UPSIDE DOWN")))
            result = false;
        if ((previous.equals("ON LEFT SIDE") || previous.equals("ON RIGHT SIDE")) && (current.equals("ON LEFT SIDE") || current.equals("ON RIGHT SIDE")))
            result = false;
        if ((previous.equals("SCREEN UP") || previous.equals("SCREEN DOWN")) && (current.equals("SCREEN UP") || current.equals("SCREEN DOWN")))
            result = false;
        return result;
    }


}
