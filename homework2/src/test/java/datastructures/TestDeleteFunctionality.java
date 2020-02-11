package datastructures;

import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.fail;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {

    @Test(timeout=SECOND)
    public void testDeleteDecrementsSize() {
        IList<String> list = makeBasicList();
        int initSize = list.size();
        list.delete(2);

        assertEquals(initSize - 1, list.size());
    }

    @Test(timeout=SECOND)
    public void testDelete() {
        IList<String> list = makeBasicList();
        String ret = list.delete(1);

        assertEquals(ret, "b");
        assertListMatches(new String[] {"a", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteEnd() {
        IList<String> list = makeBasicList();
        String ret = list.delete(2);

        assertEquals(ret, "c");
        assertListMatches(new String[] {"a", "b"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteBackPointer() {
        IList<String> list = makeBasicList();
        list.delete(2);
        String back = list.get(1);

        assertEquals(back, "b");
    }

    @Test(timeout=SECOND)
    public void testDeleteNextPointer() {
        IList<String> list = makeBasicList();
        list.add("d");
        list.add("e");
        list.delete(2);
        String back = list.get(2);

        assertEquals(back, "d");
    }

    @Test(timeout=SECOND)
    public void testDeleteFront() {
        IList<String> list = makeBasicList();
        String ret = list.delete(0);

        assertEquals(ret, "a");
        assertListMatches(new String[] {"b", "c"}, list);
    }

    @Test(timeout=SECOND)
    public void testDeleteOutOfBoundsThrowsException() {
        IList<String> list = this.makeBasicList();

        try {
            list.delete(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }

        try {
            list.delete(4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

}
