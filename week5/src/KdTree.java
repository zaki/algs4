// vim: noai:ts=4:sw=4
// Kd-tree assignment, week 5, Coursera Algorithms 1
// Student: Zoltan Dezso

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree
{
    private static class Node
    {
        private int level;
        private Point2D point;
        private Node left;
        private Node right;
        private RectHV leftRect;
        private RectHV rightRect;

        public Node(Point2D point, int level, RectHV parentRect)
        {
            this.point = point;
            this.level = level;

            if (this.isVertical())
            {
                double px = point.x();
                double ymin = parentRect.ymin();
                double ymax = parentRect.ymax();
                this.leftRect  = new RectHV(parentRect.xmin(), ymin, px,                ymax);
                this.rightRect = new RectHV(px,                ymin, parentRect.xmax(), ymax);
            }
            else
            {
                double py = point.y();
                double xmin = parentRect.xmin();
                double xmax = parentRect.xmax();
                this.leftRect  = new RectHV(xmin, parentRect.ymin(), xmax, py);
                this.rightRect = new RectHV(xmin, py,                xmax, parentRect.ymax());
            }
        }

        public int compare(Point2D other)
        {
            if (this.point.equals(other)) return 0;

            if (this.isVertical() && this.point.x() < other.x())
                return -1;
            else if (this.point.y() < other.y())
                return -1;

            return 1;
        }

        public boolean isVertical()
        {
            return this.level % 2 == 0;
        }

        public String toString()
        {
            return "Node: " + level + " @ " + point.x() + "," + point.y();
        }
    }

    private int size = 0;
    private Node root = null;

    public KdTree()
    {
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return size;
    }

    public void insert(Point2D p)
    {
        if (root == null)
        {
            root = new Node(p, 0, new RectHV(0.0, 0.0, 1.0, 1.0));
            size++;
            return;
        }

        boolean found = false;
        Node current = root;
        Node next = null;

        boolean cmp = false;
        int comp;
        while (!found)
        {
            comp = current.compare(p);
            if (comp == 0)
            {
                found = true;
                continue;
            }

            if (current.leftRect.contains(p))
            {
                if (current.left == null)
                {
                    Node node = new Node(p, current.level + 1, current.leftRect);
                    size++;
                    current.left = node;
                }
                else
                {
                    current = current.left;
                }
            }
            else
            {
                if (current.right == null)
                {
                    Node node = new Node(p, current.level + 1, current.rightRect);
                    size++;
                    current.right = node;
                }
                else
                {
                    current = current.right;
                }
            }
        }
    }

    public boolean contains(Point2D p)
    {
        return containsSearch(p, root);
    }

    private boolean containsSearch(Point2D p, Node node)
    {
        if (node == null) return false;

        if (node.point.equals(p)) return true;

        if (node.leftRect.contains(p)) return containsSearch(p, node.left);
        else                           return containsSearch(p, node.right);
    }

    private void drawNode(Node node, RectHV rect)
    {
        if (node == null) return;

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(node.point.x(), node.point.y());

        StdDraw.setPenColor(node.isVertical() ? StdDraw.RED : StdDraw.BLUE);

        StdDraw.setPenRadius(0.002);

        if (node.isVertical())
        {
            // vertical
            StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
        }
        else
        {
            // horizontal
            StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
        }
        drawNode(node.left, node.leftRect);
        drawNode(node.right, node.rightRect);
    }

    public void draw()
    {
        if (root != null)
        {
            drawNode(root, new RectHV(0.0, 0.0, 1.0, 1.0));
        }
    }

    private void searchRange(RectHV rect, Node node, java.util.ArrayList<Point2D> acc)
    {
        if (node == null) return;

        if (rect.contains(node.point))       acc.add(node.point);
        if (node.leftRect.intersects(rect))  searchRange(rect, node.left,  acc);
        if (node.rightRect.intersects(rect)) searchRange(rect, node.right, acc);
    }

    public Iterable<Point2D> range(RectHV rect)
    {
        java.util.ArrayList<Point2D> accumulator = new java.util.ArrayList<Point2D>();
        searchRange(rect, root, accumulator);

        return accumulator;
    }

    public Point2D nearest(Point2D p)
    {
        return nearestSearch(root, p, null);
    }

    private Point2D nearestSearch(Node node, Point2D p, Point2D nearest)
    {
        if (node == null) return nearest;

        if (nearest == null || node.point.distanceSquaredTo(p) < nearest.distanceSquaredTo(p))
        {
            nearest = node.point;
        }

        // nearest points in respective subtrees
        Point2D l = null;
        Point2D r = null;
        if (node.leftRect.contains(p))
        {
            // go left first
            if (node.leftRect.distanceSquaredTo(p)  < nearest.distanceSquaredTo(p)) l = nearestSearch(node.left,  p, nearest);
            if (l != null && l.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) nearest = l;
            if (node.rightRect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) r = nearestSearch(node.right, p, nearest);
            if (r != null && r.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) nearest = r;
        }
        else
        {
            // go right first
            if (node.rightRect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) r = nearestSearch(node.right, p, nearest);
            if (r != null && r.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) nearest = r;
            if (node.leftRect.distanceSquaredTo(p)  < nearest.distanceSquaredTo(p)) l = nearestSearch(node.left,  p, nearest);
            if (l != null && l.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) nearest = l;
        }

        return nearest;
    }

    public static void main(String[] args)
    {
    }
}
