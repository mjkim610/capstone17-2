package com.capstone.calculatesha1android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView time;
    private TextView result;
    private String teststring;
    private String HashValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = (TextView)findViewById(R.id.textView1);
        result = (TextView)findViewById(R.id.textView2);
        teststring = getResources().getString(R.string.teststring);
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
        HashValue=sb.toString();
    }

    public void onBeginClick (View view) {
        time.setText("WORKING...");
        result.setText("WORKING...");
        long startTime = System.nanoTime();
        for (Integer i = 0; i<50000; i++) {
            computeSHAHash(teststring);
        }
        long duration = System.nanoTime() - startTime;
        time.setText(Long.toString(duration));
        String output = "SHA-1 hash: " + " " + HashValue;
        result.setText(output);
    }
}
