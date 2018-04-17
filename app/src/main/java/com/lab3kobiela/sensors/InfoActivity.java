package com.lab3kobiela.sensors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.lab3kobiela.sensors.Settings.listOfLists;

public class InfoActivity extends AppCompatActivity {

    TextView tvHowToPlay, tvAvailableRounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tvHowToPlay = (TextView) findViewById(R.id.tvHowToPlay);
        tvAvailableRounds = (TextView) findViewById(R.id.tvAvailableRounds);
        showRounds();
    }

    public void showRounds() {
        String result = "";
        for (int i = 0; i < listOfLists.size(); i++) {
            for (int j = 0; j < listOfLists.get(i).size(); j++) {
                result += listOfLists.get(i).get(j) + "\n\n";
            }
            result += "\n\n\n";
        }
        tvAvailableRounds.setText(tvAvailableRounds.getText() + result);
    }
}
