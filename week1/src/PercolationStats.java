// vim: noai:ts=4:sw=4
// Percolation assignment, week 1, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/// PercolationStats
/// This class runs `trials` consecutive monte carlo simulations on a percolation
/// grid of size `n`, aggregates the results and shows the threshold for the vacancy
/// probability that causes the grid to percolate.
public class PercolationStats
{
    private int trials;
    private double[] results;

    /// Constructs and immediately executes a simulation on a new percolation grid
    ///
    /// @param n The size of the grid
    /// @param trials The number of simulations to run
    public PercolationStats(int n, int trials)
    {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be larger than zero");
        if (trials <= 0) throw new IllegalArgumentException("Number of trials must be larger than zero");

        this.trials = trials;
        results = new double[trials];

        for (int t = 0; t < trials; t++)
        {
            Percolation p = new Percolation(n);
            results[t] = 0.0;

            while (!p.percolates())
            {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;

                if (p.isOpen(row, col)) continue;

                p.open(row, col);

                results[t] += 1.0;
            }

            results[t] /= (double) (n * n);
        }
    }

    /// Return the mean ratio of open sites that caused the grids to percolate
    /// during the consecutive simulations
    public double mean()
    {
        return StdStats.mean(results);
    }

    /// Return the standard deviation for the ratio of open sites that cause the
    /// grids to percolate during the consecutive simulations
    public double stddev()
    {
        return StdStats.stddev(results);
    }

    /// Return the lower bound for the 95% confidence interval
    public double confidenceLo()
    {
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    /// Return the higher bound for the 95% confidence interval
    public double confidenceHi()
    {
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    ///{{{ - Test client
    public static void main(String[] args)
    {
        if (args.length != 2) return;

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo() + "," + stats.confidenceHi());
    }
    ///}}}
}
