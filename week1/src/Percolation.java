// vim: noai:ts=4:sw=4
// Percolation assignment, week 1, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/// Percolation
/// This class represents a square grid of n-by-n sites
public class Percolation
{
    private int n;
    private boolean[] open;
    private boolean[] connectedTop;
    private boolean[] connectedBot;
    private boolean percolates = false;
    private WeightedQuickUnionUF unionFind;

    private int idx = -1;
    private int idx2 = -1;

    /// Construct an n-by-n square grid of empty sites
    ///
    /// @param n The size of the grid
    /// @throws IllegalArgumentException if the grid size is less than 1
    public Percolation(int n)
    {
        if (n <= 0)
            throw new IllegalArgumentException("Size of grid cannot be negative");

        this.n = n;

        unionFind = new WeightedQuickUnionUF(n * n);
        open = new boolean[n * n];
        connectedTop = new boolean[n * n];
        connectedBot = new boolean[n * n];

        for (int row = 1; row <= n; row++)
        {
            for (int col = 1; col <= n; col++)
            {
                idx = index(row, col);
                open[idx] = false;
                connectedTop[idx] = false;
                connectedBot[idx] = false;
            }
        }

        // connect source and first row
        for (int i = 0; i < n; i++)
        {
            connectedTop[i] = true;
            connectedBot[n * n - i - 1] = true;
        }
    }

    /// Open a site
    ///
    /// @param row The row of the site to open, starting at 1
    /// @param col The column of the site to open, starting at 1
    public void open(int row, int col)
    {
        validateIndex(row, col);
        idx = index(row, col);
        open[idx] = true;

        // Connect neighbors
        if (row > 1 && isOpen(row - 1, col))     connectNeighbors(row, col, row - 1, col);
        if (row < n && isOpen(row + 1, col))     connectNeighbors(row, col, row + 1, col);
        if (col > 1 && isOpen(row,     col - 1)) connectNeighbors(row, col, row,     col - 1);
        if (col < n && isOpen(row,     col + 1)) connectNeighbors(row, col, row,     col + 1);

        if (connectedTop[idx] && connectedBot[idx]) percolates = true;
    }

    /// Check if the site at the given coordinates is open
    /// returns true if the site is open, false otherwise
    ///
    /// @param row The row of the site, starting at 1
    /// @param col The column of the site, starting at 1
    public boolean isOpen(int row, int col)
    {
        validateIndex(row, col);
        return open[index(row, col)];
    }

    /// Check if the site at the given coordinates is full
    /// in other words if it is accessible via an open site from the top
    /// returns true if the site is full, false otherwise
    ///
    /// @param row The row of the site, starting at 1
    /// @param col The column of the site, starting at 1
    public boolean isFull(int row, int col)
    {
        validateIndex(row, col);
        idx = index(row, col);
        return open[idx] && connectedTop[unionFind.find(idx)];
    }

    /// Check whether the grid percolates
    /// The grid is considered to percolate when any sites at the bottom are full
    /// returns true if the grid percolates, false otherwise
    public boolean percolates()
    {
        return this.percolates;
    }

    //{{{ - Private methods
    private int index(int row, int col)
    {
        return (row - 1) * n + (col - 1);
    }

    private void validateIndex(int row, int col)
    {
        if (row > n || col > n || row < 1 || col < 1)
            throw new IndexOutOfBoundsException("Invalid index " + row + "," + col);
    }

    private void connectNeighbors(int row, int col, int nRow, int nCol)
    {
        idx = index(row, col);
        idx2 = index(nRow, nCol);
        int rootBefore = unionFind.find(idx);
        int rootNeighbor = unionFind.find(idx2);

        boolean top = connectedTop[rootBefore];
        boolean bot = connectedBot[rootBefore];
        top = top || connectedTop[rootNeighbor];
        bot = bot || connectedBot[rootNeighbor];

        unionFind.union(idx, idx2);

        int rootAfter     = unionFind.find(idx);

        top = top || connectedTop[unionFind.find(idx)];
        bot = bot || connectedBot[rootAfter];

        connectedTop[rootAfter] = top;
        connectedBot[rootAfter] = bot;

        if (connectedTop[rootAfter] && connectedBot[rootAfter]) this.percolates = true;
    }
    //}}}
}
