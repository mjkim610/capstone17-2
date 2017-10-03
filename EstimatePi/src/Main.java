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
        String output = "";

        Random random = new Random();
        int r = random.nextInt((int)Math.sqrt(Integer.MAX_VALUE));
        int n = Integer.parseInt(args[0]);

        long startTime = System.nanoTime();

        double piEstimate = estimatePi(r, n);
        output = output + "Pi Estimate: " + piEstimate + "\n";

        long duration = System.nanoTime() - startTime;
        output = output + "Duration: " + duration;

        System.out.println(output);
    }
}