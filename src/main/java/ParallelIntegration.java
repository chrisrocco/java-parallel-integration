package main.java;

public class ParallelIntegration {

    private double area;
    private double tolerance;
    private int workers;
    Function<Double> function;

    public ParallelIntegration (int workers, double tolerance, Function<Double> function) {
        this.area = 0;
        this.tolerance = tolerance;
        this.function = function;
        this.workers = workers;
    }

    public double doWork() {
        double low = 0;
        double high = Math.PI / 4;
        double xPoints[] = new double[workers + 1];
        for (int i = 0; i < workers; i++){
            xPoints[i] = i*(high-low)/workers + low;
        }
        xPoints[workers] = high;

        Worker[] threads = new Worker[workers];
        for(int i = 0; i < workers; i++) {
            Worker w = new Worker(xPoints[i], xPoints[i+1]);
            w.run();
            threads[i] = w;
        }

        for (Worker r: threads) {
            try {
                r.join();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        return this.area;
    }

    synchronized void incArea(double amount) {
        this.area += amount;
    }

    private class Worker extends Thread {
        double lo, hi;

        public Worker(double lo, double hi){
            this.lo = lo;
            this.hi = hi;
        }

        @Override
        public void run() {
            this.addarea(this.lo, this.hi);
        }

        public void addarea(double lo, double hi){
            double I1 = ((hi - lo) / 2) * (function.compute(lo) + function.compute(hi));
            double m = (lo + hi) / 2;
            double I2 = ((hi-lo) / 4) * (function.compute(lo) + function.compute(hi) + 2 * function.compute(m));

            double tmp_1 = Math.abs(I1 - I2);
            double tmp_2 = 3 * (this.hi - this.lo) * tolerance;

            if (tmp_1 <= tmp_2){
                incArea(I2);
                return;
            }

            this.addarea(lo, m);
            this.addarea(m, hi);
        }
    }

}

interface Function<T> {
    T compute(T x);
}