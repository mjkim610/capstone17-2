package com.capstone.controllerandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Properties;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.os.AsyncTask;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelExec;

import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private String output = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.textView2);
        result.setText("RESULTS ARE SHOWN HERE");
    }

    public void onBeginClick(View view) {
        new PostTask().execute();
    }

    private class PostTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            result = (TextView) findViewById(R.id.textView2);
            result.setText("WORKING NOW...");
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("OUTPUT", "inside doInBackground...");
            try {
                output = executeRemoteCommand("username", "password", "hostname", 22); // FILL IN WITH SERVER INFO
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output;
        }

        protected void onPostExecute(String output) {
            result = (TextView) findViewById(R.id.textView2);
            result.setText(output);
        }

        public String executeRemoteCommand(String username, String password, String hostname, int port) throws Exception {
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
            channelssh.setCommand("cd capstone17-2/CalculateSHA1/src/; javac com/capstone/Main.java; java -classpath . com.capstone.Main \"The quick brown fox jumps over the lazy dog.\"");
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
