// vim: noai:ts=4:sw=4
// Dequeue assignment, week 2, Coursera Algorithms 1
// Student: Zoltan Dezso

import java.util.Iterator;

/// Deque
/// Implements a standard double ended queue
public class Deque<Item> implements Iterable<Item>
{
    ///{{{ - Private classes
    private class Node<Item>
    {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node<Item> current = first;

        public Item next()
        {
            if (current == null) throw new java.util.NoSuchElementException("No more elements to return");

            Item item = current.item;
            current = current.next;
            return item;
        }

        public boolean hasNext()
        {
            return current != null;
        }

        public void remove()
        {
            throw new java.lang.UnsupportedOperationException("remove() is not supported");
        }
    }
    ///}}}

    private Node<Item> first = null;
    private Node<Item> last = null;
    private int size = 0;

    public Deque()
    {
    }

    /// Returns true if the deque has no elements
    public boolean isEmpty()
    {
        return size == 0;
    }

    /// Returns the number of elements currently in the deque
    public int size()
    {
        return size;
    }

    /// Adds an element to the front of the queue
    /// @param item The element to be added
    public void addFirst(Item item)
    {
        if (item == null) throw new NullPointerException("Cannot add null");

        Node<Item> node = new Node<Item>();
        node.item = item;

        if (first == null)
        {
            node.next = null;
            node.prev = null;
            first = node;
            last = node;
        }
        else
        {
            Node<Item> currentFirst = first;
            first = node;
            node.next = currentFirst;
            currentFirst.prev = node;
        }

        size++;
    }

    /// Adds an element to the end of the queue
    /// @param item The element to be added
    public void addLast(Item item)
    {
        if (item == null) throw new NullPointerException("Cannot add null");

        Node<Item> node = new Node<Item>();
        node.item = item;

        if (last == null)
        {
            node.next = null;
            node.prev = null;
            first = node;
            last = node;
        }
        else
        {
            Node<Item> currentLast = last;
            last = node;
            node.prev = currentLast;
            currentLast.next = node;
        }

        size++;
    }

    /// Removes and returns an element from the front of the queue
    /// @return Item The element removed
    public Item removeFirst()
    {
        if (first == null) throw new java.util.NoSuchElementException("Cannot remove item from empty deque");

        Item item = first.item;

        if (first.next == null)
        {
            first = null;
            last = null;
            size--;
        }
        else
        {
            first = first.next;
            first.prev = null;
            size--;
        }

        return item;
    }

    /// Removes and returns an element from the end of the queue
    /// @return Item The element removed
    public Item removeLast()
    {
        if (last == null) throw new java.util.NoSuchElementException("Cannot remove item from empty deque");

        Item item = last.item;

        if (last.prev == null)
        {
            first = null;
            last = null;
            size--;
        }
        else
        {
            last = last.prev;
            last.next = null;
            size--;
        }

        return item;
    }

    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }
}
