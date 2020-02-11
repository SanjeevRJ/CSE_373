package datastructures.sorting;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.Sorter;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapAndSorterStress extends BaseTest {

    @Test(timeout=15*SECOND)
    public void testManyInsertsAndRemoves() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        int size = 10000000;
        for (int i = 0; i < size; i++) {
            heap.insert(i);
        }
        assertEquals(size, heap.size());

        int i = 0;
        while (!heap.isEmpty()) {
            assertEquals(i, heap.removeMin());
            i++;
        }
    }

    @Test(timeout=15*SECOND)
    public void testBigSort() {
        IList<Integer> list = new DoubleLinkedList<>();
        int size = 1000000;
        for (int i = 0; i < size; i++) {
            list.add(i);
        }

        int size2 = 100000;
        IList<Integer> top = Sorter.topKSort(size2, list);
        assertEquals(size2, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals((size-size2) + i, top.get(i));
        }
    }
}
