import java.lang.Math;

public class Main {

    public static double throwDart(int r, int n) {
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
        return (double)bullseyeCount/(double)n;
    }

    public static void main(String[] args) {
        System.out.print("Program argument (r): ");
        for (int i=0; i<args.length; i++) {
            System.out.println(args[i]);
        }

        int r = Integer.parseInt(args[0]);
        int n = 500000;

        long startTime = System.nanoTime();

        double ratio = throwDart(r, n);
        double piEstimate = ratio*4.0;
        System.out.println("Pi Estimate: " + piEstimate);

        long duration = System.nanoTime() - startTime;
        System.out.println("Duration: " + duration);

    }
}
