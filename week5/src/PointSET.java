// vim: noai:ts=4:sw=4
// Kd-tree assignment, week 5, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET
{
    private int size = 0;
    private java.util.TreeSet<Point2D> tree;

    public PointSET()
    {
        tree = new java.util.TreeSet<Point2D>();
    }

    public boolean isEmpty()
    {
        return (size == 0);
    }

    public int size()
    {
        return size;
    }

    public void insert(Point2D p)
    {
        if (tree.add(p)) size++;
    }

    public boolean contains(Point2D p)
    {
        return tree.contains(p);
    }

    public void draw()
    {
        for (Point2D p : tree)
        {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        java.util.ArrayList<Point2D> list = new java.util.ArrayList<Point2D>();
        for (Point2D p : tree)
        {
            if (rect.contains(p)) list.add(p);
        }
        return list;
    }

    public Point2D nearest(Point2D p)
    {
        double dist = Double.MAX_VALUE;
        Point2D nearest = null;

        for (Point2D p2 : tree)
        {
            if (p.distanceSquaredTo(p2) < dist)
            {
                nearest = p2;
                dist = p.distanceSquaredTo(p2);
            }
        }
        return nearest;
    }

    public static void main(String[] args)
    {
    }
}
