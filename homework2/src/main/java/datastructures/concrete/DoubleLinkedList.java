package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == 0) {
            front = new Node<>(item);
            back = front;
            size++;
        } else if (size == 1) {
            front.next = new Node<>(item);
            back = front.next;
            back.prev = front;
            back.next = null;
            size++;
        } else {
            back.next = new Node<>(item);
            Node temp = back;
            back = back.next;
            back.prev = temp;
            size++;
        }
    }

    @Override
    public T remove() {
        if (size == 0) {
            throw new EmptyContainerException();
        } else if (size == 1) {
            T retData = back.data;
            front = null;
            back = null;
            size--;
            return retData;
        } else {
            T retData = back.data;
            back = back.prev;
            back.next = null;
            size--;
            return retData;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else {
            if (index < (size/2)) {
                int pos = 0;
                Node cur = front;
                while (pos < index) {
                    cur = cur.next;
                    pos++;
                }
                return (T) cur.data;
            } else {
                int pos = size - 1;
                Node cur = back;
                while (pos > index) {
                    cur = cur.prev;
                    pos--;
                }
                return (T) cur.data;
            }
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else if (size == 1) {
            front = new Node(item);
            back = front;
        } else {
            if (index == 0) {
                Node temp = front.next;
                temp.prev = new Node(null, item, temp);
                front = temp.prev;
            } else if (index == (size-1)) {
                Node temp = back.prev;
                temp.next = new Node(temp, item, null);
                back = temp.next;
            } else {
                if (index < (size/2)) {
                    int pos = 0;
                    Node cur = front;
                    while (pos < (index - 1)) {
                        cur = cur.next;
                        pos++;
                    }
                    Node temp = cur.next.next;
                    cur.next = new Node(cur, item, temp);
                    temp.prev = cur.next;
                } else {
                    int pos = size - 1;
                    Node cur = back;
                    while (pos > index + 1) {
                        cur = cur.prev;
                        pos--;
                    }
                    Node temp = cur.prev.prev;
                    cur.prev = new Node(temp, item, cur);
                    temp.next = cur.prev;
                }
            }
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= (size + 1)) {
            throw new IndexOutOfBoundsException();
        } else if (size == 0) {
            front = new Node(item);
            back = front;
            size++;
        } else {
            if (index == 0) {
                front.prev = new Node(null, item, front);
                front = front.prev;
                size++;
            } else if (index == (size-1)) {
                Node temp = back.prev;
                temp.next = new Node(temp, item, back);
                back.prev = temp.next;
                size++;
            } else if (index == size) {
                back.next = new Node(back, item, null);
                back = back.next;
                size++;
            } else {
                if (index < size/2){
                    int pos = 0;
                    Node cur = front;
                    while (pos < (index - 1)) {
                        cur = cur.next;
                        pos++;
                    }
                    Node temp = cur.next;
                    cur.next = new Node(cur, item, temp);
                    temp.prev = cur.next;
                    size++;
                } else {
                    int pos = size - 1;
                    Node cur = back;
                    while (pos > index) {
                        cur = cur.prev;
                        pos--;
                    }
                    Node temp = cur.prev;
                    cur.prev = new Node(temp, item, cur);
                    temp.next = cur.prev;
                    size++;
                }
            }
        }
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else if (size == 1) {
            T retData = front.data;
            front = null;
            back = null;
            size--;
            return retData;
        } else {
            if (index == 0) {
                T retData = front.data;
                front = front.next;
                front.prev = null;
                size--;
                return retData;
            } else if (index == (size-1)) {
                T retData = back.data;
                back = back.prev;
                back.next = null;
                size--;
                return retData;
            } else {
                if (index < size/2) {
                    int pos = 0;
                    Node cur = front;
                    while (pos < (index - 1)) {
                        cur = cur.next;
                        pos++;
                    }
                    Object retData = cur.next.data;
                    Node temp = cur.next.next;
                    cur.next = temp;
                    temp.prev = cur;
                    size--;
                    return (T) retData;
                } else {
                    int pos = size - 1;
                    Node cur = back;
                    while (pos > index + 1) {
                        cur = cur.prev;
                        pos--;
                    }
                    Object retData = cur.prev.data;
                    Node temp = cur.prev.prev;
                    cur.prev = temp;
                    temp.next = cur;
                    size--;
                    return (T) retData;
                }
            }
        }
    }

    @Override
    public int indexOf(T item) {
        Node cur = front;
        int ind = 0;
        while (cur != null) {
           if (item == null && null == cur.data) {
               return ind;
           } else if (cur.data.equals(item)) {
               return ind;
           }
           ind++;
           cur = cur.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        Node cur = front;
        while (cur != null) {
            if (other == null) {
                if (other == cur.data) {
                    return true;
                }
            } else {
                if (other.equals(cur.data)) {
                    return true;
                }
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }
        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
           return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T retData = current.data;
            current = current.next;
            return retData;
        }
    }
}
