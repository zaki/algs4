// vim: noai:ts=4:sw=4
// Dequeue assignment, week 2, Coursera Algorithms 1
// Student: Zoltan Dezso

public class DequeTest
{
    private static void test(boolean predicate, String message)
    {
        if (!predicate)
        {
            System.out.print("F");
            throw new RuntimeException(message);
        }
        else
        {
            System.out.print(".");
        }
    }

    static void testConstruction()
    {
        Deque<String> dq = new Deque<String>();
        test(dq.size() == 0, "Empty queue size is not zero");
    }

    static void testRemoveFromEmptyQueue()
    {
        boolean thrown = false;
        Deque<String> dq = new Deque<String>();
        try
        {
            dq.removeFirst();
        }
        catch (java.util.NoSuchElementException ex)
        {
            thrown = true;
        }

        test(thrown, "Should throw on removing element");

        thrown = false;
        dq = new Deque<String>();
        try
        {
            dq.removeLast();
        }
        catch (java.util.NoSuchElementException ex)
        {
            thrown = true;
        }

        test(thrown, "Should throw on removing element");
    }

    static void testAddFirst()
    {
        Deque<Integer> dq = new Deque<Integer>();
        for (int i = 0; i < 10; i++)
        {
            dq.addFirst(i);
        }
        test(dq.size() == 10, "");
        for (int i = 0; i < 10; i++)
        {
            int j = dq.removeLast();
            test(j == i, "");
        }
    }

    static void testAddLast()
    {
        Deque<Integer> dq = new Deque<Integer>();
        for (int i = 0; i < 10; i++)
        {
            dq.addLast(i);
        }
        test(dq.size() == 10, "");
        for (int i = 0; i < 10; i++)
        {
            int j = dq.removeFirst();
            test(j == i, "");
        }
    }

    static void testAddNull()
    {
        boolean thrown = false;
        Deque<String> dq = new Deque<String>();
        try
        {
            dq.addFirst(null);
        }
        catch (java.lang.NullPointerException ex)
        {
            thrown = true;
        }

        test(thrown, "Should throw on adding null element");

        thrown = false;
        dq = new Deque<String>();
        try
        {
            dq.addLast(null);
        }
        catch (java.lang.NullPointerException ex)
        {
            thrown = true;
        }

        test(thrown, "Should throw on adding null element");
    }

    public static void main(String[] args)
    {
        testConstruction();
        testRemoveFromEmptyQueue();
        testAddFirst();
        testAddLast();
        testAddNull();
        System.out.println("\nAll tests passed");
    }
}
