package com.capstone.cloudy.estimatepiandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.Math;
import java.text.NumberFormat;
import java.util.Random;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView time;
    private TextView result;
    private EditText repetitionCount;

    public static double estimatePi(int r, int n) {
        double x;
        double y;

        int bullseyeCount = 0;

        for (int i = 0; i < n; i++) {
            x = Math.random() * r;
            y = Math.random() * r;

            if (x * x + y * y < r * r) {
                bullseyeCount++;
            }
        }
        return 4.0 * (double) bullseyeCount / (double) n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView) findViewById(R.id.textView1);
        result = (TextView) findViewById(R.id.textView2);
        repetitionCount = (EditText)findViewById(R.id.number1);
    }

    public void onBeginClick(View view) {
        if (repetitionCount.getText().toString().trim().isEmpty()) {
            result.setText("Please specify the number of repetition");
        } else {
            int repetition = Integer.parseInt(repetitionCount.getText().toString().trim());

            if (repetition > Integer.MAX_VALUE) {
                result.setText("Please choose a value less than " + Integer.MAX_VALUE);
            } else {
                time.setText("WORKING...");
                result.setText("WORKING...");

                Random random = new Random();
                int radius = random.nextInt((int)Math.sqrt(Integer.MAX_VALUE));
                String output = "";

                output = output + "Program argument (r): ";
                output = output + radius + "\n";

                long startTime = System.nanoTime();

                double piEstimate = estimatePi(radius, repetition);
                output = output + "Pi Estimate: " + piEstimate + "\n";

                long duration = System.nanoTime() - startTime;
                time.setText(NumberFormat.getNumberInstance().format(duration));
                result.setText(output);
            }
        }
    }
}
