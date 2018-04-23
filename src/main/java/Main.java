package main.java;

public class Main {

    public static void main(String[] args) {
        double result = (new ParallelIntegration(4, 1, (x) -> Math.pow(x, 2))).doWork();

        System.out.println("\n\n Starting parallel integration");
        System.out.println(result);
    }

}
