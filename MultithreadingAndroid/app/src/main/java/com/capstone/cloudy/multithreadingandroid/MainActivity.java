package com.capstone.cloudy.multithreadingandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.os.AsyncTask;

import android.os.SystemClock;
import java.util.Date;
import java.util.Random;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView startTimeTV;
    private TextView endTimeTV;
    public static TextView[] connTVs = new TextView[1];

    private Spinner algoSpinner;
    private String algorithm = "Round Robin";

    public static long ntpDiff;


    public static String[] simulations = {"docker run mjkim610/capstone-calculate-sha1 ",
                                            "docker run mjkim610/capstone-calculate-fibonacci ",
                                            "docker run mjkim610/capstone-estimate-pi "};
    public static String command;

    public static String username, password, hostname;
    public static int port;

    private String[] usernames;
    private String[] passwords;
    private String[] hostnames;
    private int[] ports;

    private ExecutorService pool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connTVs[0] = (TextView) findViewById(R.id.connTV1);

        usernames = getResources().getStringArray(R.array.usernames);
        passwords = getResources().getStringArray(R.array.passwords);
        hostnames = getResources().getStringArray(R.array.hostnames);
        ports = getResources().getIntArray(R.array.ports);

        startTimeTV = (TextView) findViewById(R.id.timeTV1);
        endTimeTV = (TextView) findViewById(R.id.timeTV2);

        for (int i=0; i<connTVs.length; i++) {
            connTVs[i].setText("RESULTS ARE SHOWN HERE");
        }

        algoSpinner = (Spinner) findViewById(R.id.algoSpinner);
        ArrayAdapter<CharSequence> algoAdapter = ArrayAdapter.createFromResource(this, R.array.algoArray, android.R.layout.simple_spinner_item);
        algoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algoSpinner.setAdapter(algoAdapter);
        algoSpinner.setOnItemSelectedListener(this);


        new GetNtpTimeTask().execute();

        pool = Executors.newFixedThreadPool(10);

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch(parent.getId()) {
            case R.id.algoSpinner:
                algorithm = parent.getSelectedItem().toString();
                break;
            default:
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }
//
//    public void onBeginClick(View view) {
//        if (repCount.getText().toString().trim().isEmpty()) {
//            result.setText("Please specify the number of repetition");
//        } else {
//            // MUST CHECK THAT INPUT VALUE IS LESS THAN INTEGER.MAX_VALUE BEFORE ACCEPTING AS INT
//            int rep = Integer.parseInt(repCount.getText().toString().trim());
//
//            if (rep < Integer.MAX_VALUE) {
//                startTimeTV.setText("WORKING NOW...");
//                endTimeTV.setText("WORKING NOW...");
//                result.setText("WORKING NOW...");
//
//
//                createRandomJobs(10);
//
//            } else {
//                result.setText("Please choose a value less than " + Integer.MAX_VALUE);
//            }
//        }
//    }

    public void onBeginClick(View view) {

        createRandomJobs(10);
    }

    private void createRandomJobs(int jobCount) {
        Random random = new Random();

        int[] workerIds = new int[jobCount];
        String[] jobUsernames = new String[jobCount];
        String[] jobPasswords = new String[jobCount];
        String[] jobHostnames = new String[jobCount];
        int[] jobPorts = new int[jobCount];
        String[] jobCommands = new String[jobCount];


        for (int i=0; i<jobCount; i++) {
            int workerId = random.nextInt(5);
            int simId = random.nextInt(3);
            int rep = random.nextInt(50);

            workerIds[i] = workerId;
            jobUsernames[i] = usernames[workerId];
            jobPasswords[i] = passwords[workerId];
            jobHostnames[i] = hostnames[workerId];
            jobPorts[i] = ports[workerId];
            jobCommands[i] = simulations[simId] + rep;

        }

        for (int i=0; i<jobCount; i++) {
            connTVs[workerIds[i]].setText("Working...");
            pool.execute(new SshClient(i, workerIds[i], jobUsernames, jobPasswords, jobHostnames, jobPorts, jobCommands));
        }
    }

    private class GetNtpTimeTask extends AsyncTask<Void, Void, String> {

        private Date current;

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
