package com.capstone.montecarlopiandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.Math;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private Button compute;

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

        compute = (Button) findViewById(R.id.btn1);
        result = (TextView) findViewById(R.id.textView2);
    }

    public void onBeginClick(View view) {
        int radius = 10;
        String output = "";

        output = output + "Program argument (r): ";
        output = output + radius + "\n";

        int n = 500000;

        long startTime = System.nanoTime();

        double piEstimate = estimatePi(radius, n);
        output = output + "Pi Estimate: " + piEstimate + "\n";

        long duration = System.nanoTime() - startTime;
        output = output + "Duration: " + duration + "\n";

        result.setText(output);

    }
}
