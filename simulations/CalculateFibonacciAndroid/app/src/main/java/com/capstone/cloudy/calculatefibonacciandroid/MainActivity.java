package com.capstone.cloudy.calculatefibonacciandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private TextView time;
    private TextView result;
    private EditText nValue;
    private String solution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView)findViewById(R.id.textView1);
        result = (TextView)findViewById(R.id.textView2);
        nValue = (EditText)findViewById(R.id.number1);
    }

    public long recursiveFib(long n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return recursiveFib(n-1) + recursiveFib(n-2);
    }

    public void onBeginClick (View view) {
        if (nValue.getText().toString().trim().isEmpty()) {
            result.setText("Please specify the number of repetition");
        } else {
            long n = Integer.parseInt(nValue.getText().toString().trim());

            if (n > Integer.MAX_VALUE) {
                result.setText("Please choose a value less than " + Integer.MAX_VALUE);
            } else {
                time.setText("WORKING...");
                result.setText("WORKING...");

                long startTime = System.nanoTime();
                solution = NumberFormat.getNumberInstance().format(recursiveFib(n));
                long duration = System.nanoTime() - startTime;
                time.setText(NumberFormat.getNumberInstance().format(duration));
                String output = "N: " + n + "\n" + "Solution: " + " " + solution;
                result.setText(output);
            }
        }
    }
}
