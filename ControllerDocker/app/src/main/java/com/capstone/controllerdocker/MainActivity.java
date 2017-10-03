package com.capstone.controllerdocker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.os.AsyncTask;

import java.util.Properties;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelExec;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView time;
    private TextView result;
    private EditText repetitionCount;
    private long startTime;

    private String command;
    private String output = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repetitionCount = (EditText)findViewById(R.id.number1);

        time = (TextView) findViewById(R.id.textView1);
        result = (TextView) findViewById(R.id.textView2);
        result.setText("RESULTS ARE SHOWN HERE");
    }

    public void onBeginClick(View view) {
        if (repetitionCount.getText().toString().trim().isEmpty()) {
            result.setText("Please specify the number of repetition");
        } else {
            int repetition = Integer.parseInt(repetitionCount.getText().toString().trim());

            if (repetition > Integer.MAX_VALUE) {
                result.setText("Please choose a value less than " + Integer.MAX_VALUE);
            } else {
                time.setText("WORKING NOW...");
                result.setText("WORKING NOW...");

                command = "cd docker/sandbox/; ./run-calculate-sha1.sh " + repetition;

                startTime = System.nanoTime();
                new PostTask().execute();
            }
        }
    }

    private class PostTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {

            // FILL IN SERVER INFO
            String username = "username";
            String password = "password";
            String hostname = "hostname";
            int port = 22;

            Log.d("OUTPUT", "inside doInBackground...");
            try {
                if (username.equals("username")) {
                    Log.d("OUTPUT", "FILL IN SERVER INFO...");
                }
                output = executeRemoteCommand(username, password, hostname, port);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

        protected void onPostExecute(String output) {
            Log.d("OUTPUT", "inside onPostExecute...");
            result = (TextView) findViewById(R.id.textView2);
            result.setText(output);

            long duration = System.nanoTime() - startTime;
            time = (TextView) findViewById(R.id.textView1);
            time.setText(Long.toString(duration));
        }

        private String executeRemoteCommand(String username, String password, String hostname, int port) throws Exception {
            Log.d("OUTPUT", "inside executeRemoteCommand...");

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
}
