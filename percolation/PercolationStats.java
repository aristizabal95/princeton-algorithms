import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
  private final double[] results;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    results = new double[trials];
    for (int i = 0; i < trials; i++) {
      results[i] = runTrial(n);
    }
  }

  // Performs a single trial, returns the percolation ratio
  private double runTrial(int n) {
    Percolation p = new Percolation(n);
    while (!p.percolates()) {
      int row = StdRandom.uniform(n)+1;
      int col = StdRandom.uniform(n)+1;
      p.open(row, col);
    }

    double threshold = (double) p.numberOfOpenSites() / (n*n);
    return threshold;
  }

  // sample mean of percolation threshold
  public double mean() {
    return StdStats.mean(results);
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return StdStats.stddev(results);
  }

  private double confidenceRange() {
    double z = 1.960; // Z-score for a 95% confidence.
    return z * stddev()/Math.sqrt(results.length);
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean() - confidenceRange();
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean() + confidenceRange();
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int t = Integer.parseInt(args[1]);
    PercolationStats stats = new PercolationStats(n, t);
    double[] confInterval = {stats.confidenceLo(), stats.confidenceHi()};
    System.out.println("mean                    = " + stats.mean());
    System.out.println("stddev                  = " + stats.stddev());
    System.out.println("95% confidence interval = [" + confInterval[0] + ", " + confInterval[1] + "]");
  }

}
