package com.lab3kobiela.sensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.lab3kobiela.sensors.Settings.NUMBER_OF_ROUNDS_PER_LEVEL;
import static com.lab3kobiela.sensors.Settings.SOUND_ON;
import static com.lab3kobiela.sensors.Settings.VALUE_BELOW_GRAVITY;
import static com.lab3kobiela.sensors.Settings.listOfLists;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private SoundPool soundPool;
    private int correctId, wrongId, roundWonId;

    private TextView tvLevel, tvRound, tvSteps, tvResult, tvWinLose;
    private Button bStart;

    private List<ArrayList<String>> list = new ArrayList<>();
    private boolean gameOn = false;
    private long lastUpdate = 0;
    private int counter = 0, allCounter = 0, levelNumber = 1;
    private List<String> listOfSteps;
    private String currentPosition, lastPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvRound = (TextView) findViewById(R.id.tvRound);
        tvSteps = (TextView) findViewById(R.id.tvSteps);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvWinLose = (TextView) findViewById(R.id.tvWinLose);
        bStart = (Button) findViewById(R.id.bStart);

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        correctId = soundPool.load(this, R.raw.correct_beep, 1);
        wrongId = soundPool.load(this, R.raw.wrong_beep_1, 1);
        roundWonId = soundPool.load(this, R.raw.round_won_beep, 1);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bStart.getText().equals("PLAY AGAIN")) {
                    startNewGame();
                } else {
                    if (bStart.getText().equals("Next round")) {
                        setList(true);
                        tvResult.setVisibility(View.INVISIBLE);
                        bStart.setText("START");
                    } else {
                        if (bStart.getText().equals("Play again")) setList(false);
                        tvResult.setVisibility(View.VISIBLE);
                        restartGame();
                    }
                }

            }
        });

        setList(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.mi_how_to_play:
                Intent intentInfo = new Intent(GameActivity.this, InfoActivity.class);
                startActivity(intentInfo);
                break;
            case R.id.sound:
                Toast.makeText(getApplicationContext(), "Sound " + (SOUND_ON ? "OFF" : "ON"), Toast.LENGTH_LONG).show();
                if (SOUND_ON) item.setIcon(R.mipmap.sound_off);
                else item.setIcon(R.mipmap.sound_on);
                SOUND_ON = !SOUND_ON;
                break;
            case R.id.mi_add_new_round:
                Intent intentAdd = new Intent(GameActivity.this, AddRoundActivity.class);
                startActivity(intentAdd);
                break;
        }
        return true;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (gameOn) {
                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {
                    lastUpdate = curTime;
                    currentPosition = getPositionName(x, y, z);

                    if (counter == listOfSteps.size()) endingProcedure(true);
                    else if (!currentPosition.equals("") && !currentPosition.equals(lastPosition)) {
                        if (currentPosition.equals(listOfSteps.get(counter))) {
                            tvResult.setText(Html.fromHtml("<font color=green>" + findNewLineAndReplaceHTMLBr(tvResult.getText().toString()) + "<br>" + currentPosition + "</font>"));
                            counter++;
                            if (counter != listOfSteps.size())
                                playBeep(correctId);
                            else playBeep(roundWonId);
                        } else endingProcedure(false);
                    }

                    if (!currentPosition.equals("")) lastPosition = currentPosition;

                }
            } else lastPosition = getPositionName(x, y, z);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void startNewGame() {
        lastUpdate = 0;
        counter = 0;
        allCounter = 0;
        levelNumber = 1;
        gameOn = false;
        tvLevel.setVisibility(View.VISIBLE);
        tvRound.setVisibility(View.VISIBLE);
        tvSteps.setVisibility(View.VISIBLE);
        tvResult.setVisibility(View.VISIBLE);
        tvResult.setText("");
        bStart.setText("START");
        setList(true);
        tvWinLose.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void setList(boolean result) {
        Random r = new Random();
        if (allCounter % NUMBER_OF_ROUNDS_PER_LEVEL == 0 && result) {
            list = new ArrayList<>(listOfLists.get(allCounter / NUMBER_OF_ROUNDS_PER_LEVEL));
            tvLevel.setText("LEVEL " + levelNumber);
            levelNumber++;

        }

        if (!result) {
            allCounter -= 1;
        } else listOfSteps = list.remove(r.nextInt(list.size()));

        tvRound.setText("\nROUND " + ((allCounter) % (NUMBER_OF_ROUNDS_PER_LEVEL) + 1) + "\n");
        tvSteps.setText("");
        for (int i = 0; i < listOfSteps.size(); i++) {
            tvSteps.setText(tvSteps.getText() + "\n" + listOfSteps.get(i));
        }
        Toast.makeText(getApplicationContext(), "Set your phone different than " + listOfSteps.get(0), Toast.LENGTH_LONG).show();
        allCounter++;

    }


    public String getPositionName(float x, float y, float z) {
        String result = "";
        if (y > VALUE_BELOW_GRAVITY) result = "NORMAL";
        else if (y < -VALUE_BELOW_GRAVITY) result = "UPSIDE DOWN";
        else if (x > VALUE_BELOW_GRAVITY) result = "ON LEFT SIDE";
        else if (x < -VALUE_BELOW_GRAVITY) result = "ON RIGHT SIDE";
        else if (z > VALUE_BELOW_GRAVITY) result = "SCREEN UP";
        else if (z < -VALUE_BELOW_GRAVITY) result = "SCREEN DOWN";

        /*
        if (y > VALUE_BELOW_GRAVITY) result = normal;
        else if (y < -VALUE_BELOW_GRAVITY) result = upsideDown;
        else if (x > VALUE_BELOW_GRAVITY) result = onLeftSide;
        else if (x < -VALUE_BELOW_GRAVITY) result = onRightSide;
        else if (z > VALUE_BELOW_GRAVITY) result = screenUp;
        else if (z < -VALUE_BELOW_GRAVITY) result = screenDown;*/

        return result;
    }

    public void restartGame() {
        tvSteps.setVisibility(View.INVISIBLE);
        bStart.setVisibility(View.INVISIBLE);
        tvResult.setText("");
        tvWinLose.setVisibility(View.INVISIBLE);
        gameOn = true;
        counter = 0;
    }

    public void endingProcedure(boolean result) {
        gameOn = false;
        tvWinLose.setVisibility(View.VISIBLE);
        if (result) {
            tvWinLose.setText("YOU WON");
        } else {
            tvResult.setText(Html.fromHtml("<font color=green>" + findNewLineAndReplaceHTMLBr(tvResult.getText().toString())
                    + "<br></font><font color=red>XXX " + listOfSteps.get(counter) + " XXX" + "</font><br>"));
            tvWinLose.setText("YOU LOST");
            playBeep(wrongId);
        }

        if (allCounter == listOfLists.size() * NUMBER_OF_ROUNDS_PER_LEVEL && result) {
            winnerProcedure();
        } else {
            tvSteps.setVisibility(View.VISIBLE);
            bStart.setVisibility(View.VISIBLE);
            if (result) bStart.setText("Next round");
            else bStart.setText("Play again");
            //setList(result);
        }

    }

    public static ArrayList<String> l = new ArrayList<String>();

    public void winnerProcedure() {
        tvWinLose.setText("YOU WON THE GAME");
        tvLevel.setVisibility(View.INVISIBLE);
        tvRound.setVisibility(View.INVISIBLE);
        tvSteps.setVisibility(View.INVISIBLE);
        tvResult.setVisibility(View.INVISIBLE);
        bStart.setVisibility(View.VISIBLE);
        bStart.setText("PLAY AGAIN");
    }

    public void playBeep(int beepId) {
        if (SOUND_ON) soundPool.play(beepId, 1, 1, 1, 0, 1);
    }

    public String findNewLineAndReplaceHTMLBr(String str) {
        return str.replaceAll("\n", "<br>");
    }

}