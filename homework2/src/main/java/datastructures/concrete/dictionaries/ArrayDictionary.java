package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private int arrSize;
    private int curSize;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        arrSize = 10;
        curSize = 0;
        pairs = makeArrayOfPairs(arrSize);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        for (int i = 0; i < curSize; i++) {
            if (key == null) {
                if (pairs[i].key == null) {
                    return pairs[i].value;
                }
            } else {
                if (key.equals(pairs[i].key)) {
                    return pairs[i].value;
                }
            }
        }
        throw new NoSuchKeyException();
    }

    private void set(K key, V value) {
        for (int i = 0; i < curSize; i++) {
            if (key == null) {
                if (pairs[i].key == null) {
                    pairs[i].value = value;
                }
            } else {
                if (key.equals(pairs[i].key)) {
                    pairs[i].value = value;
                }
            }
        }
    }

    @Override
    public void put(K key, V value) {
        if (curSize < arrSize - 1) {
            if (containsKey(key)) {
                set(key, value);
            } else {
                pairs[curSize] = new Pair(key, value);
                curSize++;
            }
        } else {
            Pair<K, V>[] pairsCopy = pairs;
            pairs = makeArrayOfPairs(arrSize * 2);
            arrSize *= 2;
            for (int i = 0; i < curSize; i++) {
                pairs[i] = pairsCopy[i];
            }
            if (containsKey(key)) {
                set(key, get(key));
            } else {
                pairs[curSize] = new Pair(key, value);
                curSize++;
            }
        }
    }

    @Override
    public V remove(K key) {
        V retVal = null;
        for (int i = 0; i < curSize; i++) {
            if (key == null) {
                if (key == pairs[i].key) {
                    retVal = pairs[i].value;
                    pairs[i] = pairs[curSize - 1];
                    break;
                }
            } else {
                if (key.equals(pairs[i].key)) {
                    retVal = pairs[i].value;
                    pairs[i] = pairs[curSize - 1];
                    break;
                }
            }
        }
        if (retVal == null) {
            throw new NoSuchKeyException();
        }
        curSize--;
        return retVal;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < curSize; i++) {
            if (key == null) {
                if (key == pairs[i].key) {
                    return true;
                }
            } else {
                if (key.equals(pairs[i].key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return curSize;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
