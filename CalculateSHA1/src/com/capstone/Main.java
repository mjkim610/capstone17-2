package com.capstone;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static String computeSHAHash(String password)
    {

        MessageDigest mdSha1 = null;
        try
        {
            mdSha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e1) {
            System.out.println("Error initializing SHA1");
        }
        try {
            mdSha1.update(password.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Unsupported Encoding Exception");
        }
        byte[] data = mdSha1.digest();
        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex = Base64.getEncoder().encodeToString(data);

        sb.append(hex);
        return sb.toString();

    }

    public static void begin(String[] args) {
        for (int i=0; i<args.length; i++) {
            System.out.println(args[i]);
        }

        long tsLong = System.nanoTime();
        String HashValue = "error";

        for (Integer i = 0; i<5000000; i++) {
            HashValue = computeSHAHash(args[0]);
        }
        Long ttLong = System.nanoTime() - tsLong;
        String tt = ttLong.toString();
        Integer roundnumber = Math.round(ttLong / 100000000);
        String score =  roundnumber.toString();
        String output = "SHA-1 hash: " + " " + HashValue + "\n Time Taken: " + tt + "\n Score: " + score;
        System.out.println(output);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("CalculateSHA1.txt"))) {
            bw.write(tt);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        begin(args);
    }
}
