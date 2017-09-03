package com.capstone.calculatesha1android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private Button compute;
    private String teststring;
    private String HashValue;
    private String tt;
    private String output;
    private long tsLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compute=(Button)findViewById(R.id.btn1);
        result= (TextView)findViewById(R.id.textView2);
        teststring= getResources().getString(R.string.teststring);
    }

    public void computeSHAHash(String password)
    {

        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            Log.e("Benchmark", "Error initializing SHA1");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] data = mdSha1.digest();
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex = Base64.encodeToString(data, 0, data.length, 0);

        sb.append(hex);
        HashValue=sb.toString();

    }

    public void onBeginClick (View view) {
        tsLong = System.nanoTime();
        for (Integer i = 0; i<5000000; i++) {
            computeSHAHash(teststring);
        }
        Long ttLong = System.nanoTime() - tsLong;
        tt = ttLong.toString();
        Integer roundnumber = Math.round(ttLong / 100000000);
        String score =  roundnumber.toString();
        output = "SHA-1 hash: " + " " + HashValue + "\n Time Taken: " + tt + "\n Score: " + score;
        result.setText(output);
    }
}
