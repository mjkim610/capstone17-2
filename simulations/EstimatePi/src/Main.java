import java.lang.Math;
import java.util.Random;

public class Main {
    public static double estimatePi(int r, int n) {
        double x;
        double y;

        int bullseyeCount = 0;

        for (int i=0; i<n; i++) {
            x = Math.random() * r;
            y = Math.random() * r;

            if (x*x + y*y < r*r) {
                bullseyeCount++;
            }
        }
        return 4.0*(double)bullseyeCount/(double)n;
    }

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Random random = new Random();
        int r = random.nextInt((int)Math.sqrt(Integer.MAX_VALUE));
        int n = Integer.parseInt(args[0]);

        double piEstimate = estimatePi(r, n);
        String estimate = "" + piEstimate;

        long endTime = System.currentTimeMillis();
        String output = "Pi estimate:\t" + estimate + "\nStart time:\t" + startTime + "\nEnd time:\t" + endTime;

        System.out.println(output);
    }
}