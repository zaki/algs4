// vim: noai:ts=4:sw=4
// Queues assignment, week 2, Coursera Algorithms 1
// Student: Zoltan Dezso

import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

/// RandomizedQueue
/// 
public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] items;
    private int size = 0;

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int current = 0;
        private int size = 0;
        private Item[] array;
        private int[] shuffle; // Indices into the array holding the items

        ///{{{ - Private classes
        RandomizedQueueIterator(Item[] array, int size)
        {
            if (array == null || size == 0) return;

            this.size = size;
            this.array = array;
            current = 0;
            shuffle = new int[size];
            for (int i = 0; i < size; i++)
            {
                shuffle[i] = i;
            }
            StdRandom.shuffle(shuffle, 0, size - 1);
        }

        public Item next()
        {
            if (current < size && size > 0)
            {
                return array[shuffle[current++]];
            }

            throw new java.util.NoSuchElementException("No more elements to return");
        }

        public boolean hasNext()
        {
            return current < size;
        }

        public void remove()
        {
            throw new java.lang.UnsupportedOperationException("remove() is not supported");
        }
    }
    ///}}}

    public RandomizedQueue()
    {
        size = 0;
        items = (Item[]) new Object[4];
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    public int size()
    {
        return size;
    }

    public void enqueue(Item item)
    {
        if (item == null) throw new java.lang.NullPointerException("Cannot add null item");

        resizeItems(size + 1);
        items[size++] = item;
    }

    public Item dequeue()
    {
        if (size == 0) throw new java.util.NoSuchElementException("Cannot dequeue from empty queue");

        int idx = StdRandom.uniform(size);
        Item item = items[idx];

        items[idx] = null;

        // We can swap the removed element with the last, because access is random anyway
        if (size > 1 && idx < size - 1)
        {
            items[idx] = items[size - 1];
            items[size - 1] = null;
        }

        size--;
        resizeItems(size);

        return item;
    }

    public Item sample()
    {
        if (size == 0) throw new java.util.NoSuchElementException("Cannot dequeue from empty queue");

        Item item = null;
        if (size > 0)
        {
            int idx = StdRandom.uniform(size);
            item = items[idx];
        }
        return item;
    }

    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator(this.items, this.size);
    }

    private void resizeItems(int newSize)
    {
        if (newSize == 0) return;

        if (newSize > items.length)
        {
            int len = items.length * 2;
            Item[] tmp = (Item[]) new Object[len];
            for (int i = 0; i < size; i++)
            {
                tmp[i] = items[i];
                items[i] = null;
            }

            items = tmp;
        }
        else if (newSize < items.length / 4)
        {
            int len = items.length / 2;
            Item[] tmp = (Item[]) new Object[len];
            for (int i = 0; i < size; i++)
            {
                tmp[i] = items[i];
                items[i] = null;
            }

            items = tmp;
        }
    }
}
