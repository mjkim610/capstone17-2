package com.capstone.controllerdocker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import android.os.SystemClock;
import java.util.Date;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelExec;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private String fileOutput = "";
    private int counter = 0;

    private TextView startTimeTV;
    private TextView endTimeTV;
    private TextView result;
    private EditText repetitionCount;
    private Spinner serverSpinner;
    private Spinner simulSpinner;

    private long ntpDiff;
    private long startTime;
    private long endTime;

    private String username, password, hostname;
    private int port = 22;

    private String server = "AWS - N.Virginia";
    private String simulation = "CalculateSha1";
    private String command;
    private String output = "";

    private String[] usernames;
    private String[] passwords;
    private String[] hostnames;
    private int[] ports;

    private Date current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernames = getResources().getStringArray(R.array.usernames);
        passwords = getResources().getStringArray(R.array.passwords);
        hostnames = getResources().getStringArray(R.array.hostnames);
        ports = getResources().getIntArray(R.array.ports);

        repetitionCount = (EditText)findViewById(R.id.number1);

        startTimeTV = (TextView) findViewById(R.id.timeTextView1);
        endTimeTV = (TextView) findViewById(R.id.timeTextView2);

        result = (TextView) findViewById(R.id.textView2);
        result.setText("RESULTS ARE SHOWN HERE");

        serverSpinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> serverAdapter = ArrayAdapter.createFromResource(this, R.array.serverArray, android.R.layout.simple_spinner_item);
        serverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serverSpinner.setAdapter(serverAdapter);
        serverSpinner.setOnItemSelectedListener(this);
        simulSpinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> simulAdapter = ArrayAdapter.createFromResource(this, R.array.simulationArray, android.R.layout.simple_spinner_item);
        simulAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        simulSpinner.setAdapter(simulAdapter);
        simulSpinner.setOnItemSelectedListener(this);

        new GetNtpTimeTask().execute();

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch(parent.getId()) {
            case R.id.spinner1:
                server = parent.getSelectedItem().toString();
                break;
            case R.id.spinner2:
                simulation = parent.getSelectedItem().toString();
                break;
            default:
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onBeginClick(View view) {
        if (repetitionCount.getText().toString().trim().isEmpty()) {
            result.setText("Please specify the number of repetition");
        } else {
            // MUST CHECK THAT INPUT VALUE IS LESS THAN INTEGER.MAX_VALUE BEFORE ACCEPTING AS INT
            int repetition = Integer.parseInt(repetitionCount.getText().toString().trim());

            if (repetition < Integer.MAX_VALUE) {
                startTimeTV.setText("WORKING NOW...");
                endTimeTV.setText("WORKING NOW...");
                result.setText("WORKING NOW...");

                if (simulation.equals("CalculateSha1")) {
                    command = "docker run mjkim610/capstone-calculate-sha1 " + repetition;
                } else if (simulation.equals("CalculateFibonacci")) {
                    command = "docker run mjkim610/capstone-calculate-fibonacci " + repetition;
                } else if (simulation.equals("EstimatePi")) {
                    command = "docker run mjkim610/capstone-estimate-pi " + repetition;
                }

                int arrayIndex = 0;
                if (server.equals("AWS - N.Virginia")) {
                    arrayIndex = 0;
                } else if (server.equals("AWS - Oregon")) {
                    arrayIndex = 1;
                } else if (server.equals("AWS - London")) {
                    arrayIndex = 2;
                } else if (server.equals("AWS - Mumbai")) {
                    arrayIndex = 3;
                } else if (server.equals("AWS - Seoul")) {
                    arrayIndex = 4;
                } else if (server.equals("Cloudlet - High-end")) {
                    arrayIndex = 5;
                } else if (server.equals("Cloudlet - Mid-end")) {
                    arrayIndex = 6;
                }

                username = usernames[arrayIndex];
                password = passwords[arrayIndex];
                hostname = hostnames[arrayIndex];
                port = ports[arrayIndex];

                for (int i=0; i<10; i++) {
                    new PostTask().execute();
                }

            } else {
                result.setText("Please choose a value less than " + Integer.MAX_VALUE);
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void getPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Perform write-external-storage related task
//                } else {
//                    // Perform task for when permission is denied.
//                }
//                return;
//            }
//        }
//    }

    public void saveToFile() {
        getPermission();

        File file;
        FileOutputStream outputStream;
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ""+Calendar.getInstance().getTime()+".txt");

            outputStream = new FileOutputStream(file);
            outputStream.write(fileOutput.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class PostTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            try {
                output = executeRemoteCommand(username, password, hostname, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

        protected void onPostExecute(String output) {
            counter++;

            result = (TextView) findViewById(R.id.textView2);
            result.setText("Counter: " + counter + "\n" + output);
            endTime = System.currentTimeMillis();
            startTimeTV.setText(""+(startTime+ntpDiff));
            endTimeTV.setText(""+(endTime+ntpDiff));

            fileOutput += "Request sent: " + (startTime+ntpDiff) + "\n";
            fileOutput += "Response received: " + (endTime+ntpDiff) + "\n";
            fileOutput += "Counter: " + counter + "\n" + output;
            fileOutput += "===\n";

            if (counter >= 10) {
                if (isExternalStorageWritable()) {
                    saveToFile();
                    result.setText(result.getText() + "\nDONE!");
                } else {
                    Log.e("W", "Storage not writable");
                }
            }

        }

        private String executeRemoteCommand(String username, String password, String hostname, int port) throws Exception {
            startTime = System.currentTimeMillis();

            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);

            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");

            session.setConfig(prop);
            session.setTimeout(10000);
            session.connect();

            // SSH Channel
            ChannelExec channelssh = (ChannelExec) session.openChannel("exec");

            InputStream inputStream = channelssh.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            // Execute command
            channelssh.setCommand(command);
            channelssh.connect();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
            channelssh.disconnect();

            return stringBuilder.toString();
        }
    }

    private class GetNtpTimeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            SntpClient client = new SntpClient();
            if (client.requestTime("time.nist.gov", 1000000000)) {
                long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
                current = new Date(now);
            }
            return ""+current;
        }

        protected void onPostExecute(String output) {

            Log.i("NTP tag", ""+current.getTime());
            Log.d("NTP diff", "" + (current.getTime() - System.currentTimeMillis()));
            ntpDiff = current.getTime() - System.currentTimeMillis();

        }
    }
}
