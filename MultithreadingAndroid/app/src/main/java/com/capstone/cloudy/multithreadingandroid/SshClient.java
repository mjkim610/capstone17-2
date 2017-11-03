package com.capstone.cloudy.multithreadingandroid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class SshClient implements Runnable {

    private long startTime;
    private long endTime;

    private String username, password, hostname;
    private int port;
    private String command;

    private Handler mHandler;

    public SshClient(int jobId, final int workerId, String[] usernames, String[] passwords, String[] hostnames, int[] ports, String[] commands) {

        this.username = usernames[jobId];
        this.password = passwords[jobId];
        this.hostname = hostnames[jobId];
        this.port = ports[jobId];
        this.command = commands[jobId];

        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message inputMessage){

//                Log.d("SshClient", "response received");

                // If I want to log the responses, do it using response
                String response = (String) inputMessage.obj;
                endTime = System.currentTimeMillis() + MainActivity.ntpDiff;

                switch(workerId) {
                    case 0:
                        MainActivity.connTVs[0].setText(response + "\nSend time: " + startTime + "\nReceive time: " + endTime);
                        break;
                    case 1:
                        MainActivity.connTVs[1].setText(response + "\nSend time: " + startTime + "\nReceive time: " + endTime);
                        break;
                    case 2:
                        MainActivity.connTVs[2].setText(response + "\nSend time: " + startTime + "\nReceive time: " + endTime);
                        break;
                    case 3:
                        MainActivity.connTVs[3].setText(response + "\nSend time: " + startTime + "\nReceive time: " + endTime);
                        break;
                    case 4:
                        MainActivity.connTVs[4].setText(response + "\nSend time: " + startTime + "\nReceive time: " + endTime);
                        break;
                }
            }
        };
    }

    @Override
    public void run() {
        String response = sshRequest();
        sendMessage(1, response);
    }

    public void sendMessage(int what, String msg){
        Message message = mHandler.obtainMessage(what, msg);
        message.sendToTarget();
    }

    public String sshRequest() {
        startTime = System.currentTimeMillis() + MainActivity.ntpDiff;
        String request = "";

        try {

                Log.d("sshRequest", "hostname: " + hostname);

            request = executeRemoteCommand(username,
                    password,
                    hostname,
                    port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    private String executeRemoteCommand(String username, String password, String hostname, int port) throws Exception {
//        Log.d("SshClient", "inside executeRemoteCommand...");

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
