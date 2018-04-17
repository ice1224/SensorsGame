package com.lab3kobiela.sensors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lab3kobiela.sensors.Settings.MAX_SIZE;
import static com.lab3kobiela.sensors.Settings.MIN_SIZE;
import static com.lab3kobiela.sensors.Settings.listOfLists;

public class AddRoundActivity extends AppCompatActivity {


    private Button bNormal, bUpsideDown, bOnLeftSide, bOnRightSide, bScreenUp, bScreenDown, bRemove, bAccept;
    private TextView tvResult, tvChecker;

    private ArrayList<String> stepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_round);

        bNormal = (Button) findViewById(R.id.bNormal);
        bUpsideDown = (Button) findViewById(R.id.bUpsideDown);
        bOnLeftSide = (Button) findViewById(R.id.bOnLeftSide);
        bOnRightSide = (Button) findViewById(R.id.bOnRightSide);
        bScreenUp = (Button) findViewById(R.id.bScreenUp);
        bScreenDown = (Button) findViewById(R.id.bScreenDown);

        tvChecker = (TextView) findViewById(R.id.tvChecker);

        bRemove = (Button) findViewById(R.id.bRemove);
        bAccept = (Button) findViewById(R.id.bAccept);

        tvResult = (TextView) findViewById(R.id.tvResult);
        setButtons();
        bRemove.setEnabled(false);
        //checkcheckcheck();

    }

    public void setButtonInside(String name) {
        stepList.add(name);
        disableAfterClick(name);
        tvResult.setText(listToString(stepList));
    }

    public void setButtons() {
        bNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonInside("NORMAL");
            }
        });

        bUpsideDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonInside("UPSIDE DOWN");
            }
        });

        bOnLeftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonInside("ON LEFT SIDE");
            }
        });

        bOnRightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonInside("ON RIGHT SIDE");
            }
        });

        bScreenUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonInside("SCREEN UP");
            }
        });

        bScreenDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonInside("SCREEN DOWN");
            }
        });

        bRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepList.remove(stepList.size() - 1);
                if (stepList.size() > 0) disableAfterClick(stepList.get(stepList.size() - 1));
                else enableAllButtons();
                tvResult.setText(listToString(stepList));
                if (stepList.size() == 0) bRemove.setEnabled(false);
            }
        });

        bAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lengthChecker()) {
                    if (!listExistsInBase()) {
                        listOfLists.get(stepList.size() - MIN_SIZE).add(new ArrayList<String>(stepList));
                        Toast.makeText(getApplicationContext(), "List added", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getApplicationContext(), "List already exists in base", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplicationContext(), "Wrong length of list", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void enableAllButtons() {
        bNormal.setEnabled(true);
        bUpsideDown.setEnabled(true);
        bOnLeftSide.setEnabled(true);
        bOnRightSide.setEnabled(true);
        bScreenUp.setEnabled(true);
        bScreenDown.setEnabled(true);
        bRemove.setEnabled(true);
    }

    public boolean lengthChecker() {
        return (stepList.size() >= MIN_SIZE && stepList.size() <= MAX_SIZE);
    }

    public void disableAfterClick(String previous) {
        enableAllButtons();
        if (previous.equals("NORMAL") || previous.equals("UPSIDE DOWN")) {
            bNormal.setEnabled(false);
            bUpsideDown.setEnabled(false);
        } else if (previous.equals("ON LEFT SIDE") || previous.equals("ON RIGHT SIDE")) {
            bOnLeftSide.setEnabled(false);
            bOnRightSide.setEnabled(false);
        } else if (previous.equals("SCREEN UP") || previous.equals("SCREEN DOWN")) {
            bScreenUp.setEnabled(false);
            bScreenDown.setEnabled(false);
        }
    }

    public String listToString(List<String> list) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result = result + "\n" + list.get(i);
        }
        return list.size() > 0 ? result.substring(1, result.length()) : result;
    }

    private void checkcheckcheck() {
        ArrayList<String> aList = new ArrayList<>(Arrays.asList("SCREEN UP", "NORMAL", "ON RIGHT SIDE"));
        ArrayList<String> bList = new ArrayList<>();
        bList.add("SCREEN UP");
        bList.add("NORMAL");
        bList.add("ON RIGHT SIDE");
        if (aList.equals(bList)) bRemove.setText("RÓWNE");
        else bRemove.setText("NIE RÓWNE");

    }


    private boolean listExistsInBase() {
        boolean result = false;
        int numberOfStepListInListOfLists = stepList.size() - MIN_SIZE;
        List<String> stepListCopy = new ArrayList<>(stepList);
        List<ArrayList<String>> checkList = listOfLists.get(numberOfStepListInListOfLists);
        //tvChecker.setText(stepList.toString()+"\n\n");
        for (int i = 0; i < checkList.size(); i++) {
            //tvChecker.setText(tvChecker.getText() + "\n" + checkList.get(i).toString());
            if (checkList.get(i).equals(stepListCopy)) {
                result = true;
                // tvChecker.setText(tvChecker.getText() + "<--- TEN OTO");
            }
        }

        return result;
    }

}
