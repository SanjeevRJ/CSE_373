package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(10);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        T retObj = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        heap = removeHelper(0);
        return retObj;
    }

    private T[] removeHelper(int ind) {
        int maxChild;
        if ((ind*NUM_CHILDREN + NUM_CHILDREN) >= size) {
            maxChild = size - (ind*NUM_CHILDREN + 1);
        } else {
            maxChild = NUM_CHILDREN;
        }
        int smallInd = -1;
        for (int i = 1; i <= maxChild; i++) {
            int curInd = NUM_CHILDREN*ind + i;
            if (smallInd == -1 || heap[curInd].compareTo(heap[smallInd]) < 0) {
                smallInd = curInd;
            }
        }
        if (smallInd == -1) {
            return heap;
        } else {
            if (heap[ind].compareTo(heap[smallInd]) > 0) {
                T curObj = heap[ind];
                heap[ind] = heap[smallInd];
                heap[smallInd] = curObj;
                return removeHelper(smallInd);
            }
            return heap;
        }
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == heap.length) {
            T[] oldHeap = heap;
            heap = makeArrayOfT(heap.length * 2);
            for (int i = 0; i < size; i++) {
                heap[i] = oldHeap[i];
            }
        }
        heap[size] = item;
        size++;
        heap = insertHelper(size - 1);
    }

    private T[] insertHelper(int ind) {
        if (size == 1 || ind == 0) {
            return heap;
        }
        int parent = (ind-1)/NUM_CHILDREN;
        if (heap[ind].compareTo(heap[parent]) < 0) {
            T curObj= heap[ind];
            heap[ind] = heap[parent];
            heap[parent] = curObj;
            return insertHelper(parent);
        }
        return heap;
    }

    @Override
    public int size() {
        return size;
    }
}
