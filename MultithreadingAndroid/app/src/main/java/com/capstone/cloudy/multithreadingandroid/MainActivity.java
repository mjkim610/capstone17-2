package com.capstone.cloudy.multithreadingandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelExec;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String username;
    private String password;
    private String hostname;
    private int port = 22;

    private String command;
    private int repetition;
    private String output;

    private TextView conn1;
    private TextView conn2;
    private TextView conn3;
    private TextView conn4;
    private TextView conn5;

    private ExecutorService pool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conn1 = (TextView) findViewById(R.id.textView1);
        conn2 = (TextView) findViewById(R.id.textView2);
        conn3 = (TextView) findViewById(R.id.textView3);
        conn4 = (TextView) findViewById(R.id.textView4);
        conn5 = (TextView) findViewById(R.id.textView5);

        // give pool 10 threads
        pool = Executors.newFixedThreadPool(10);
    }

    public void onBeginClick(View view) {
        repetition = 10;
        command = "cd docker/sandbox/; ./run-calculate-sha1.sh " + repetition;

        // CHECK THAT EACH THREAD IS ACTUALLY SENDING TO THE SPECIFIED SERVERS
        username = "FILL IN HERE";
        password = "FILL IN HERE";
        hostname = "FILL IN HERE";
        pool.execute(new NetworkService(1));

        username = "FILL IN HERE";
        password = "FILL IN HERE";
        hostname = "FILL IN HERE";
        pool.execute(new NetworkService(2));

        username = "FILL IN HERE";
        password = "FILL IN HERE";
        hostname = "FILL IN HERE";
        pool.execute(new NetworkService(3));

        username = "FILL IN HERE";
        password = "FILL IN HERE";
        hostname = "FILL IN HERE";
        pool.execute(new NetworkService(4));

        username = "FILL IN HERE";
        password = "FILL IN HERE";
        hostname = "FILL IN HERE";
        pool.execute(new NetworkService(5));
    }

    private class NetworkService implements Runnable {
        private Handler mHandler;
        public NetworkService(final int i) {
            mHandler = new Handler(Looper.getMainLooper()){

                @Override
                public void handleMessage(Message inputMessage){
                    String msg = (String) inputMessage.obj;

                    switch(i) {
                        case 1:
                            conn1.setText(msg);
                            break;
                        case 2:
                            conn2.setText(msg);
                            break;
                        case 3:
                            conn3.setText(msg);
                            break;
                        case 4:
                            conn4.setText(msg);
                            break;
                        case 5:
                            conn5.setText(msg);
                            break;
                    }
                }
            };
        }

        @Override
        public void run() {
            String recv = HttpRequest();
            sendMessage(1, recv);
        }

        public void sendMessage(int what, String msg){
            Message message = mHandler.obtainMessage(what, msg);
            message.sendToTarget();
        }

    }

    public String HttpRequest() {
        try {
            output = executeRemoteCommand(username, password, hostname, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
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
