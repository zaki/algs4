// vim: noai:ts=4:sw=4
// 8-Puzzle assignment, week 3, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Board
{
    private short[] blocks;
    private int dimension;
    private int hamming = 0;
    private int manhattan = 0;
    private boolean goal = true;

    private int gapx;
    private int gapy;

    public Board(int[][] blocks)
    {
        if (blocks == null) throw new java.lang.NullPointerException("Null supplied to constructor");

        dimension = blocks[0].length;
        this.blocks = new short[dimension * dimension];
        for (int x = 0; x < dimension; x++)
        {
            for (int y = 0; y < dimension; y++)
            {
                int idx = index(x, y);
                this.blocks[idx] = (short) blocks[x][y];
                if (this.blocks[idx] == 0)
                {
                    gapx = x;
                    gapy = y;
                    continue;
                }

                if (this.blocks[idx] != (idx + 1))
                {
                    hamming++;
                    manhattan += dist(x, y, this.blocks[idx]);
                    goal = false;
                }
            }
        }
    }

    public int dimension()
    {
        return dimension;
    }

    public int hamming()
    {
        return hamming;
    }

    public int manhattan()
    {
        return manhattan;
    }

    public boolean isGoal()
    {
        return goal;
    }

    public Board twin()
    {
        int swpx1 = 0;
        int swpy1 = 0;
        int swpx2 = 1;
        int swpy2 = 1;

        if (gapx == 0 && gapy == 0) swpx1 = 1;
        if (gapx == 1 && gapy == 1) swpx2 = 0;

        return exchange(swpx1, swpy1, swpx2, swpy2);
    }

    public boolean equals(Object y)
    {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        return this.toString().equals(((Board) y).toString());
    }

    private Board exchange(int x1, int y1, int x2, int y2)
    {
        int[][] neighborBlocks = new int[dimension][dimension];
        int tmp;
        for (int x = 0; x < dimension; x++)
        {
            for (int y = 0; y < dimension; y++)
            {
                neighborBlocks[x][y] = blocks[index(x, y)];
            }
        }

        tmp = neighborBlocks[x1][y1];
        neighborBlocks[x1][y1] = neighborBlocks[x2][y2];
        neighborBlocks[x2][y2] = tmp;

        return new Board(neighborBlocks);
    }

    public Iterable<Board> neighbors()
    {
        java.util.ArrayList<Board> neighbors = new java.util.ArrayList<Board>();

        if (gapx > 0)             neighbors.add(exchange(gapx - 1, gapy,     gapx, gapy));
        if (gapy > 0)             neighbors.add(exchange(gapx,     gapy - 1, gapx, gapy));
        if (gapx < dimension - 1) neighbors.add(exchange(gapx + 1, gapy,     gapx, gapy));
        if (gapy < dimension - 1) neighbors.add(exchange(gapx,     gapy + 1, gapx, gapy));

        return neighbors;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("" + dimension + "\n");

        for (int x = 0; x < dimension; x++)
        {
            for (int y = 0; y < dimension; y++)
            {
                sb.append(String.format("%2d ", blocks[index(x, y)]));
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private int index(int x, int y)
    {
        return x * dimension + y;
    }

    private int dist(int x, int y, int val)
    {
        int rx = (val - 1) / dimension;
        int ry = (val - 1) - rx * dimension;

        return Math.abs(rx - x) + Math.abs(ry - y);
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println(initial.toString());
        StdOut.println("Twin:");
        StdOut.println(initial.twin().toString());
        StdOut.println("manhattan: " + initial.manhattan());
        StdOut.println("hamming:   " + initial.hamming());
        StdOut.println("is goal:   " + initial.isGoal());

        StdOut.println("Neighbors:");
        for (Board b : initial.neighbors())
        {
            StdOut.println(b.toString());
        }
    }
}
