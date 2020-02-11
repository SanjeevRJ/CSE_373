package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int numItems;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        numItems = 0;
        chains = makeArrayOfChains(10);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    private int hash(K key) {
        int ind;
        if (key == null) {
            ind = 0;
        } else {
            ind = key.hashCode();
        }
        if (ind < 0 || ind >= chains.length) {
            ind = Math.abs(ind) % chains.length;
        }
        return ind;
    }

    @Override
    public V get(K key) {
        if (chains == null) {
            throw new NoSuchKeyException();
        }
        int ind = hash(key);
        if (chains[ind] == null || !chains[ind].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return chains[ind].get(key);
    }

    @Override
    public void put(K key, V value) {
        if (numItems == chains.length) {
            IDictionary<K, V>[] oldChains = chains;
            chains = makeArrayOfChains(oldChains.length*2);
            for (int i = 0; i < oldChains.length; i++) {
                if (oldChains[i] != null) {
                    for (KVPair<K, V> item : oldChains[i]) {
                        putHelper(item.getKey(), item.getValue(), false);
                    }
                }
            }
        }
        putHelper(key, value, true);
    }

    private void putHelper(K key, V value, boolean notSameArr) {
        int ind = hash(key);
        if (chains[ind] == null) {
            chains[ind] = new ArrayDictionary<>();
        }
        if (notSameArr && !chains[ind].containsKey(key)) {
            numItems++;
        }
        chains[ind].put(key, value);
    }

    @Override
    public V remove(K key) {
        if (chains == null) {
            throw new NoSuchKeyException();
        }
        int ind = hash(key);
        if (chains[ind] == null || !chains[ind].containsKey(key)) {
            throw new NoSuchKeyException();
        }
        numItems--;
        return chains[ind].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        if (chains == null) {
            return false;
        }
        int ind = hash(key);
        if (chains[ind] == null || !chains[ind].containsKey(key)) {
            return false;
        }
        return chains[ind].containsKey(key);
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        int ind = hash(key);
        if (chains[ind].containsKey(key)) {
            return chains[ind].get(key);
        } else {
            return defaultValue;
        }

    }

    @Override
    public int size() {
        return numItems;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int curInd;
        private Iterator<KVPair<K, V>> curIter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            curInd = 0;
            this.chains = chains;
            curIter = null;
        }

        @Override
        public boolean hasNext() {
            if (chains == null) {
                return false;
            }
            while (curInd < chains.length && chains[curInd] == null) {
                curInd++;
            }
            if (curInd >= chains.length) {
                return false;
            } else {
                if (curIter == null) {
                    curIter = chains[curInd].iterator();
                }
                if (curIter.hasNext()) {
                    return true;
                } else  {
                    curInd++;
                    while (curInd < chains.length && chains[curInd] == null) {
                        curInd++;
                    }
                    if (curInd >= chains.length) {
                        return false;
                    } else {
                        curIter = chains[curInd].iterator();
                        if (curIter.hasNext()) {
                            return true;
                        }
                        return false;
                    }
                }
            }
        }

        @Override
        public KVPair<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (!curIter.hasNext()) {
                curInd++;
            }
            return curIter.next();
        }
    }
}
