// vim: noai:ts=4:sw=4
// 8-Puzzle assignment, week 3, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver
{
    private static final boolean DEBUG = false;

    private SearchNode origRoot;
    private SearchNode twinRoot;

    private boolean solvable = false;
    private boolean nosolutn = false;

    private SearchNode solution = null;

    private int inserts = 0;
    private int delmins = 0;

    private class SearchNode implements Comparable<SearchNode>
    {
        private Board board;
        private SearchNode parent;
        private int moves;
        private boolean isOriginal;

        public SearchNode(Board board, SearchNode parent, int moves, boolean isOriginal)
        {
            this.board = board;
            this.parent = parent;
            this.moves = moves;
            this.isOriginal = isOriginal;
        }

        public int priority()
        {
            return this.board.manhattan() + this.moves;
        }

        public int compareTo(SearchNode that)
        {
            if (that == null) throw new IllegalArgumentException("");

            int p1 = this.priority();
            int p2 = that.priority();

            if (p1 < p2) return -1;
            if (p2 < p1) return 1;
            if (this.isOriginal()) return -1;
            if (that.isOriginal()) return 1;

            if (this.board.hamming() + this.moves() < that.board.hamming() + that.moves()) return -1;
            if (this.board.hamming() + this.moves() > that.board.hamming() + that.moves()) return 1;
            return 0;
        }

        public int moves()
        {
            return moves;
        }

        public boolean isOriginal()
        {
            return isOriginal;
        }

        public String toString()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("SearchNode(original=" + isOriginal + ", priority=" + priority() + ", moves=" + moves() + ")\n");
            sb.append(board.toString());
            return sb.toString();
        }
    }

    public Solver(Board initial)
    {
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();

        Board twinBoard = initial.twin();
        origRoot = new SearchNode(initial,  null, 0, true);
        twinRoot = new SearchNode(twinBoard, null, 0, false);

        if (initial.isGoal())
        {
            solvable = true;
            solution = origRoot;
            return;
        }

        if (twinBoard.isGoal())
        {
            nosolutn = true;
            return;
        }

        pq.insert(origRoot);
        pq.insert(twinRoot);

        SearchNode node;

        while (!solvable && !nosolutn)
        {
            // if (DEBUG) StdOut.println("PQ Size: " + pq.size());
            node = pq.delMin();
            // if (DEBUG) StdOut.print(node.toString());
            delmins++;
            if (oneStep(pq, node))
            {
                if (node.isOriginal())
                {
                    solvable = true;
                    solution = node;
                }
                else
                {
                    nosolutn = true;
                }
            }
        }

        if (DEBUG) StdOut.println("inserts: " + inserts + " delmins: " + delmins);
    }

    private boolean oneStep(MinPQ<SearchNode> pq, SearchNode node)
    {
        if (node.board.isGoal()) return true;

        for (Board b : node.board.neighbors())
        {
            if (node.parent == null || !b.equals(node.parent.board))
            {
                pq.insert(new SearchNode(b, node, node.moves() + 1, node.isOriginal()));
                inserts++;
            }
        }

        return false;
    }

    public boolean isSolvable()
    {
        return solvable;
    }

    public int moves()
    {
        if (solvable) return solution.moves();
        return -1;
    }

    public Iterable<Board> solution()
    {
        if (solvable)
        {
            SearchNode sn = solution;
            Stack<Board> sltn = new Stack<Board>();
            while (sn != null)
            {
                sltn.push(sn.board);
                sn = sn.parent;
            }

            return sltn;
        }

        return null;
    }

    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
