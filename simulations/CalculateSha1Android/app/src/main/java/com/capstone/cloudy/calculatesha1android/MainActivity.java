package com.capstone.cloudy.calculatesha1android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.NumberFormat;
import java.util.Random;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String CHARSLIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private TextView time;
    private TextView result;
    private EditText repetitionCount;
    private String hashValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView)findViewById(R.id.textView1);
        result = (TextView)findViewById(R.id.textView2);
        repetitionCount = (EditText)findViewById(R.id.number1);
    }

    private static String generateRandomString(String charList) {
        Random random = new Random();
        StringBuilder res = new StringBuilder();
        for (int i=0; i<100; i++) {
            int randomIndex = random.nextInt(charList.length());
            res.append(charList.charAt(randomIndex));
        }
        return res.toString();
    }

    public void computeSHAHash(String password)
    {

        MessageDigest mdSha1 = null;
        try {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("Benchmark", "Error initializing SHA1");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        StringBuilder sb = new StringBuilder();
        String hex;

        hex = Base64.encodeToString(data, 0, data.length, 0);

        sb.append(hex);
        hashValue=sb.toString();
    }

    public void onBeginClick (View view) {
        if (repetitionCount.getText().toString().trim().isEmpty()) {
            result.setText("Please specify the number of repetition");
        } else {
            int repetition = Integer.parseInt(repetitionCount.getText().toString().trim());

            if (repetition > Integer.MAX_VALUE) {
                result.setText("Please choose a value less than " + Integer.MAX_VALUE);
            } else {
                time.setText("WORKING...");
                result.setText("WORKING...");

                long startTime = System.nanoTime();
                for (Integer i = 0; i<repetition; i++) {
                    computeSHAHash(generateRandomString(CHARSLIST));
                }

                long duration = System.nanoTime() - startTime;
                time.setText(NumberFormat.getNumberInstance().format(duration));
                String output = "Repetition: " + repetition + "\n" + "SHA-1 hash: " + " " + hashValue;
                result.setText(output);
            }
        }
    }
}