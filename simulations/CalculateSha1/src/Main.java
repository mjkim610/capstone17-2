import java.util.Random;
import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class Main {
    private static final String CHARSLIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static String generateRandomString(String charList) {
        Random random = new Random();
        StringBuilder res = new StringBuilder();
        for (int i=0; i<100; i++) {
            int randomIndex = random.nextInt(charList.length());
            res.append(charList.charAt(randomIndex));
        }
        return res.toString();
    }

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

        long startTime = System.currentTimeMillis();
        String hashValue = "error";

        int repetition = Integer.parseInt(args[0]);

        for (Integer i = 0; i<repetition; i++) {
            hashValue = computeSHAHash(generateRandomString(CHARSLIST));
        }
        long endTime = System.currentTimeMillis();
        String output = "SHA-1 hash:\t" + hashValue + "\nStart time:\t" + startTime + "\nEnd time:\t" + endTime;
        System.out.println(output);
    }

    public static void main(String[] args) {
        begin(args);
    }
}