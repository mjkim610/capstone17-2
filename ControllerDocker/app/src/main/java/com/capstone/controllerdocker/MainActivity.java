package com.capstone.controllerdocker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.os.AsyncTask;

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

    private Date current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            if (repetition > Integer.MAX_VALUE) {
                result.setText("Please choose a value less than " + Integer.MAX_VALUE);
            } else {
                startTimeTV.setText("WORKING NOW...");
                endTimeTV.setText("WORKING NOW...");
                result.setText("WORKING NOW...");

                if (simulation.equals("CalculateSha1")) {
                    command = "cd docker/sandbox/; ./run-calculate-sha1.sh " + repetition;
                } else if (simulation.equals("CalculateFibonacci")) {
                    command = "cd docker/sandbox/; ./run-calculate-fibonacci.sh " + repetition;
                } else if (simulation.equals("EstimatePi")) {
                    command = "cd docker/sandbox/; ./run-estimate-pi.sh " + repetition;
                }

                // FILL IN SERVER INFO
                if (server.equals("AWS - N.Virginia")) {
                    username = "";
                    password = "";
                    hostname = "";
                    port = 22;
                } else if (server.equals("AWS - Oregon")) {
                    username = "";
                    password = "";
                    hostname = "";
                    port = 22;
                } else if (server.equals("AWS - London")) {
                    username = "";
                    password = "";
                    hostname = "";
                    port = 22;
                } else if (server.equals("AWS - Mumbai")) {
                    username = "";
                    password = "";
                    hostname = "";
                    port = 22;
                } else if (server.equals("AWS - Seoul")) {
                    username = "";
                    password = "";
                    hostname = "";
                    port = 22;
                } else if (server.equals("Cloudlet - High-end")) {
                    username = "";
                    password = "";
                    hostname = "";
                    port = 22;
                } else if (server.equals("Cloudlet - Mid-end")) {
                    username = "";
                    password = "";
                    hostname = "";
                    port = 22;
                }

                startTime = System.currentTimeMillis();
                new PostTask().execute();
            }
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

            result = (TextView) findViewById(R.id.textView2);
            result.setText(output);

            endTime = System.currentTimeMillis();
            startTimeTV.setText(""+(startTime+ntpDiff));
            endTimeTV.setText(""+(endTime+ntpDiff));

        }

        private String executeRemoteCommand(String username, String password, String hostname, int port) throws Exception {

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
