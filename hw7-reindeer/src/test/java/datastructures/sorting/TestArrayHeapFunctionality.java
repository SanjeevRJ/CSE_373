package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    protected IPriorityQueue<Integer> makeBasicHeap() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();

        heap.insert(3);
        heap.insert(2);
        heap.insert(1);
        return heap;
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testPeek() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        assertEquals(1, heap.peekMin());
    }

    @Test(timeout=SECOND)
    public void testRemove() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        assertEquals(1, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testRemoveAndPeek() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        assertEquals(1, heap.peekMin());
        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.peekMin());
        assertEquals(2, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testRemoveSize() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        assertEquals(1, heap.removeMin());
        assertEquals(2, heap.size());
    }

    @Test(timeout=SECOND)
    public void testMinInsert() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        heap.insert(0);
        assertEquals(0, heap.peekMin());
    }

    @Test(timeout=SECOND)
    public void testInsertSize() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        heap.insert(0);
        assertEquals(4, heap.size());
    }

    @Test(timeout=SECOND)
    public void testManyInserts() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        for (int i = -5; i > -20; i--) {
            heap.insert(i);
        }
        assertEquals(-19, heap.peekMin());
    }


    @Test(timeout=SECOND)
    public void testManyRemoves() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        for (int i = 5; i < 20; i++) {
            heap.insert(i);
        }
        while (!heap.isEmpty()) {
            heap.removeMin();
        }
        assertTrue(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testManyInsertsAndRemoves() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 20; i++) {
            heap.insert(i);
        }
        assertEquals(20, heap.size());
        int i = 0;
        while (!heap.isEmpty()) {
            assertEquals(i, heap.removeMin());
            i++;
        }
        assertEquals(0, heap.size());
    }


    @Test(timeout=SECOND)
    public void testChildrenGreater() {
        IPriorityQueue<Integer> heap = this.makeBasicHeap();
        for (int i = 5; i <10; i++) {
            heap.insert(i);
        }
        Integer parent = heap.removeMin();
        while (!heap.isEmpty()) {
            assertTrue(parent.compareTo(heap.removeMin()) <= 0);
        }
    }

    @Test(timeout=SECOND)
    public void testUnsortedAdd() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        heap.insert(72);
        heap.insert(16);
        heap.insert(15);
        heap.insert(66);
        heap.insert(42);

        assertEquals(15, heap.removeMin());
        assertEquals(16, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testExceptions() {
        IPriorityQueue<Integer> heap = makeInstance();
        try {
            heap.removeMin();
            fail();
        } catch (EmptyContainerException ex){
            //this is ok
        }

        try {
            heap.peekMin();
            fail();
        } catch (EmptyContainerException ex){
            //this is ok
        }

        heap = this.makeBasicHeap();
        try {
            heap.insert(null);
            fail();
        } catch (IllegalArgumentException ex){
            //this is ok
        }

    }
}
