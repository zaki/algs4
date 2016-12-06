// vim: noai:ts=4:sw=4
// Queues assignment, week 2, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

/// Subset
/// 
public class Subset
{
    public static void main(String[] args)
    {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        int k = Integer.parseInt(args[0]);
        int filled = 0;
        int seen = 0;

        String line;
        while (!StdIn.isEmpty())
        {
            line = StdIn.readString();

            if (k == 0) continue;

            if (filled < k)
            {
                rq.enqueue(line);
                filled++;
            }
            else
            {
                if (StdRandom.uniform(seen + 1) < k)
                {
                    rq.dequeue();
                    rq.enqueue(line);
                }
            }
            seen++;
        }

        for (String s : rq) StdOut.println(s);
    }
}
