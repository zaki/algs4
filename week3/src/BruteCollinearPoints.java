// vim: noai:ts=4:sw=4
// Pattern Recognition assignment, week 3, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints
{
    private LineSegment[] segments;
    public BruteCollinearPoints(Point[] points)
    {
        if (points == null) throw new NullPointerException("Null supplied to constructor");
        java.util.ArrayList<LineSegment> seg = new java.util.ArrayList<LineSegment>();

        double s1, s2, s3;
        LineSegment s;

        Point[] work = java.util.Arrays.copyOf(points, points.length);
        java.util.Arrays.sort(work);

        for (int i = 0; i < work.length; i++)
        {
            if (work[i] == null) throw new NullPointerException("Null point passed");
            for (int j = i + 1; j < work.length; j++)
            {
                if (work[i].compareTo(work[j]) == 0) throw new IllegalArgumentException("Duplicate point");

                s1 = work[i].slopeTo(work[j]);
                for (int k = j + 1; k < work.length; k++)
                {
                    s2 = work[i].slopeTo(work[k]);

                    if (Double.compare(s1, s2) != 0) continue;

                    for (int l = k + 1; l < work.length; l++)
                    {
                        s3 = work[i].slopeTo(work[l]);

                        if (Double.compare(s1, s3) == 0)
                        {
                            seg.add(new LineSegment(work[i], work[l]));
                        }
                    }
                }
            }
        }

        segments = new LineSegment[seg.size()];
        segments = seg.toArray(segments);
    }

    public int numberOfSegments()
    {
        return segments.length;
    }

    public LineSegment[] segments()
    {
        return java.util.Arrays.copyOf(segments, segments.length);
    }

    public static void main(String[] args)
    {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
