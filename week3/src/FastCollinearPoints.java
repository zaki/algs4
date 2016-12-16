// vim: noai:ts=4:sw=4
// Pattern Recognition assignment, week 3, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints
{
    private LineSegment[] segments;
    private java.util.ArrayList<LineSegment> seg;

    public FastCollinearPoints(Point[] points)
    {
        if (points == null) throw new NullPointerException("Null argument supplied to constructor");

        seg = new java.util.ArrayList<LineSegment>();
        Point[] work = java.util.Arrays.copyOf(points, points.length);
        java.util.Arrays.sort(work);
        Point[] tmp = java.util.Arrays.copyOf(work, work.length);
        Point begin;
        Point end;

        for (int i = 0; i < work.length - 1; i++)
        {
            // This should be optimizable
            java.util.Arrays.sort(tmp, work[i].slopeOrder());
            double slope = work[i].slopeTo(tmp[0]);

            begin = work[i];
            end   = work[i];

            // 1 1 1 2 1 2 3 4 5 1 2 3 1 2 3 4
            // | x | | + + + + + . . . - - - -
            int count = 1;
            int collision = 0;
            for (int j = 0; j < tmp.length; j++)
            {
                double s1 = work[i].slopeTo(tmp[j]);
                if (tmp[j].compareTo(work[i]) == 0)
                {
                    collision++;
                    if (collision > 1) throw new IllegalArgumentException("Duplicate point");
                    continue;
                }
                else if (Double.compare(slope, s1) == 0)
                {
                    if (tmp[j].compareTo(begin) < 0) begin = tmp[j];
                    if (tmp[j].compareTo(end) > 0) end = tmp[j];
                    count++;
                }
                else
                {
                    if (count > 3)
                    {
                        if (work[i].compareTo(begin) == 0) seg.add(new LineSegment(begin, end));
                    }

                    // We already have points[i] and the currently checked point
                    count = 2;

                    slope = work[i].slopeTo(tmp[j]);

                    begin = work[i];
                    end   = work[i];
                    if (tmp[j].compareTo(begin) < 0) begin = tmp[j];
                    if (tmp[j].compareTo(end) > 0) end = tmp[j];
                }
            }

            if (count > 3)
            {
                if (work[i].compareTo(begin) == 0) seg.add(new LineSegment(begin, end));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
