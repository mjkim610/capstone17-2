package com.capstone;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class Main {

    private static String computeSHAHash(String password) {
        MessageDigest mdSha1 = null;
        try {
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
        String hex;

        hex = Base64.getEncoder().encodeToString(data);

        sb.append(hex);
        return sb.toString();
    }

    private static void begin(String[] args) {
        for (int i=0; i<args.length; i++) {
            System.out.println(args[i]);
        }

        long startTime = System.nanoTime();
        String HashValue = "error";

        for (Integer i = 0; i<50000; i++) {
            HashValue = computeSHAHash(args[0]);
        }
        long duration = System.nanoTime() - startTime;
        String output = "SHA-1 hash: " + HashValue + "\nTime Taken: " + duration;
        System.out.println(output);
    }

    public static void main(String[] args) {
        begin(args);
    }
}
