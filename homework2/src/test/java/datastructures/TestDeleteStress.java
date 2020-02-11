package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {

    @Test(timeout=6 * SECOND)
    public void testDeleteNearEndSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i);
        }
        for (int i = cap - 1; i >= 2; i--) {
            int ret = list.delete(i-1);
            assertEquals(i-1, ret);
        }
        assertEquals(2, list.size());
    }

    @Test(timeout=15 * SECOND)
    public void testDeleteNearFrontSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        for (int i = 0; i < cap; i++) {
            list.add(i);
        }
        for (int i = 0; i < cap-2; i++) {
            int ret = list.delete(1);
            assertEquals(i+1, ret);
        }

        assertEquals(2, list.size());
    }



}
