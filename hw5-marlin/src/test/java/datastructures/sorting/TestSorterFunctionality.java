package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Sorter;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSorterFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testKSizeList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(10, list);
        assertEquals(10, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testKGreaterThanList() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(15, list);
        assertEquals(10, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testKZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(0, list);
        if (top == null) {
            fail("top should not be null");
        }
        assertEquals(0, top.size());
    }

    @Test(timeout=SECOND)
    public void testUnsortedList() {
        IList<Integer> list = new DoubleLinkedList<>();

        list.add(100);
        list.add(72);
        list.add(26);
        list.add(101);
        list.add(23);
        list.add(13);

        IList<Integer> top = Sorter.topKSort(3, list);
        assertEquals(3, top.size());

        assertEquals(72, top.get(0));
        assertEquals(100, top.get(1));
        assertEquals(101, top.get(2));
    }

    @Test(timeout=SECOND)
    public void testIllegalArgumentException() {
        IList<Integer> list = null;
        try {
            IList<Integer> top = Sorter.topKSort(3, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex){
            //this is ok
        }

        list = new DoubleLinkedList<>();
        list.add(3);
        try {
            IList<Integer> top = Sorter.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            //this is ok
        }
    }
}
