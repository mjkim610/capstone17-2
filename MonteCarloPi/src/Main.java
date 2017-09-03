import java.lang.Math;

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

        output = output + "Program argument (r): ";
        for (int i=0; i<args.length; i++) {
            output = output + args[i] + "\n";
        }

        int r = Integer.parseInt(args[0]);
        int n = 500000;

        long startTime = System.nanoTime();

        double piEstimate = estimatePi(r, n);
        output = output + "Pi Estimate: " + piEstimate + "\n";

        long duration = System.nanoTime() - startTime;
        output = output + "Duration: " + duration + "\n";

        System.out.println(output);
    }
}
