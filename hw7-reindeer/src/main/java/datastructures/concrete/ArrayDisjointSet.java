package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private IDictionary<T, Integer> locations;
    private int curSize;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        pointers = new int[2];
        locations = new ChainedHashDictionary<>();
        curSize = 0;
    }

    @Override
    public void makeSet(T item) {
        if (locations.containsKey(item)) {
            throw new IllegalArgumentException();
        }

        if (curSize == pointers.length) {
            int[] copy = pointers;
            pointers = new int[curSize*2];
            for (int i = 0; i < copy.length; i++) {
                pointers[i] = copy[i];
            }
        }
        curSize++;
        pointers[curSize-1] = -1;
        locations.put(item, curSize - 1);
    }

    @Override
    public int findSet(T item) {
        if (!locations.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int location = locations.get(item);
        if (pointers[location] <= -1) {
            return location;
        } else {
            int parent = findSetHelper(pointers[location], 1);
            return parent;
        }
    }

    private int findSetHelper(int location, int rank) {
        if (pointers[location] <= -1) {
            pointers[location] = (-1 * rank) - 1;
            return location;
        } else {
            int parent = findSetHelper(pointers[location], rank + 1);
            pointers[location] = parent;
            return parent;
        }
    }

    @Override
    public void union(T item1, T item2) {
        if (!locations.containsKey(item1) || !locations.containsKey(item2)) {
            throw new IllegalArgumentException();
        }

        if (item1.equals(item2)) {
            return;
        }

        int rep1 = findSet(item1);
        int rep2 = findSet(item2);
        int rank1 = -1*pointers[rep1];
        int rank2 = -1*pointers[rep2];

        if (rep1 == rep2) {
            return;
        }

        if (rank1 > rank2) {
            pointers[rep2] = rep1;
        } else {
            pointers[rep1] = rep2;
        }
    }
}
