import java.text.NumberFormat;

public class Main {

    private static long recursiveFib(long n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return recursiveFib(n-1) + recursiveFib(n-2);
    }

    private static void begin(String[] args) {

        long startTime = System.currentTimeMillis();

        int n = Integer.parseInt(args[0]);

        String solution = NumberFormat.getNumberInstance().format(recursiveFib(n));
        long endTime = System.currentTimeMillis();
        String output = "Solution:\t" + solution + "\nStart time:\t" + startTime + "\nEnd time:\t" + endTime;
        System.out.println(output);
    }

    public static void main(String[] args) {
        begin(args);
    }
}
