/* 
 * TCSS 305 – Autumn 2014
 * Assignment 2 - ShoppingCart
 */

package tests;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import model.Item;

import org.junit.Before;
import org.junit.Test;

/**
 * Represents an item.
 * 
 * @author Jonathan Hughes
 * @version 10 October 2014
 */
public class ItemTest {

    /** An arbitrary name String for testing. */
    private static final String TEST_NAME1 = "Test Name";
    
    /** An arbitrary BigDecimal price for testing. */
    private static final BigDecimal TEST_PRICE1 = new BigDecimal("1.00");
    
    /** An arbitrary number required for bulk pricing for testing. */
    private static final int TEST_BULK_QUANTITY1 = 20;
    
    /** An arbitrary BigDecimal price for testing. */
    private static final BigDecimal TEST_BULK_PRICE1 = new BigDecimal("19.00");   
    
    /** An arbitrary name String for testing. */
    private static final String TEST_NAME2 = "Test Name Two";
    
    /** An arbitrary BigDecimal price for testing. */
    private static final BigDecimal TEST_PRICE2 = new BigDecimal("9.99");
    
    /** An arbitrary number required for bulk pricing for testing. */
    private static final int TEST_BULK_QUANTITY2 = 42;
    
    /** An arbitrary BigDecimal price for testing. */
    private static final BigDecimal TEST_BULK_PRICE2 = new BigDecimal("359.94");   
    
    /** An arbitrary BigDecimal invalid price. */
    private static final BigDecimal BAD_PRICE = new BigDecimal("-1.00");
    
    /** A null Item object for testing. */
    private static final Item NULL_ITEM = null;
    
    /** Item to be used for testing, without bulk price. */
    private Item myTestItem1;
    
    /** Item to be used for testing, without bulk price. */
    private Item myTestItem2;
    
    /** Item to be used for testing, with bulk price. */
    private Item myTestItemBulk1;
    
    /** Item to be used for testing, with bulk price. */
    private Item myTestItemBulk2;
     
    /**
     * Sets up four Items for testing methods.
     * 
     * @throws java.lang.Exception throws Exception with invalid input
     */
    @Before
    public void setUp() throws Exception {
        myTestItem1 = new Item(TEST_NAME1, TEST_PRICE1);
        myTestItemBulk1 = new Item(TEST_NAME1, TEST_PRICE1, 
                                   TEST_BULK_QUANTITY1, TEST_BULK_PRICE1);
        myTestItem2 = new Item(TEST_NAME2, TEST_PRICE2);
        myTestItemBulk2 = new Item(TEST_NAME2, TEST_PRICE2, 
                                   TEST_BULK_QUANTITY2, TEST_BULK_PRICE2);
    }
    
    /**
     * Tests the calculateItemTotal for proper calculations.
     */
    @Test
    public void testCalculateItemTotal() {
        assertEquals(myTestItem1.calculateItemTotal(10), new BigDecimal("10.00"));
        assertEquals(myTestItem2.calculateItemTotal(100), new BigDecimal("999.00"));
        assertEquals(myTestItemBulk1.calculateItemTotal(10), new BigDecimal("10.00"));
        assertEquals(myTestItemBulk1.calculateItemTotal(20), new BigDecimal("19.00"));
        assertEquals(myTestItemBulk1.calculateItemTotal(21), new BigDecimal("20.00"));
        assertEquals(myTestItemBulk2.calculateItemTotal(41), new BigDecimal("409.59"));
        assertEquals(myTestItemBulk2.calculateItemTotal(42), new BigDecimal("359.94"));
        assertEquals(myTestItemBulk2.calculateItemTotal(44), new BigDecimal("379.92"));
    }

    /**
     * Tests the toString for proper String output.
     */
    @Test
    public void testToString() {
        assertEquals(myTestItem1.toString(), "Test Name, $1.00");
        assertEquals(myTestItem2.toString(), "Test Name Two, $9.99");
        assertEquals(myTestItemBulk1.toString(), "Test Name, $1.00 (20 for $19.00)");
        assertEquals(myTestItemBulk2.toString(), "Test Name Two, $9.99 (42 for $359.94)");
    }

    /**
     * Tests equals method with all combinations of equal and unequal Items.
     */
    @Test
    public void testEquals() {
        //test equals() on itself
        assertEquals(myTestItem1.equals(myTestItem1), true);
        //test equals() for null object
        assertEquals(myTestItem1.equals(NULL_ITEM), false);
        //test equals() on a different type of object
        final Object testObject = (Object) TEST_PRICE1;
        assertEquals(myTestItem1.equals(testObject), false);
        //test equals() for same name, but wrong price
        final Item rightNameWrongPrice = new Item(TEST_NAME1, TEST_PRICE2);
        assertEquals(myTestItem1.equals(rightNameWrongPrice), false);
        //test equals() for wrong name, but same price
        final Item wrongNameRightPrice = new Item(TEST_NAME2, TEST_PRICE1);
        assertEquals(myTestItem1.equals(wrongNameRightPrice), false);
        //test equals() when one has a bulk price and one doesn't
        assertEquals(myTestItem1.equals(myTestItemBulk1), false);
        assertEquals(myTestItemBulk1.equals(myTestItem1), false);
        assertEquals(myTestItem1.equals(myTestItemBulk2), false);
        assertEquals(myTestItem2.equals(myTestItemBulk1), false);
        //test equals() when bulk price is the same, but not bulk quantity
        final Item wrongBulkQuantity = new Item(TEST_NAME1, TEST_PRICE1,
                                                TEST_BULK_QUANTITY2, TEST_BULK_PRICE1);
        assertEquals(myTestItemBulk1.equals(wrongBulkQuantity), false);
        //test equals() when bulk quantity is the same, but not bulk price
        final Item wrongBulkPrice = new Item(TEST_NAME1, TEST_PRICE1,
                                                TEST_BULK_QUANTITY1, TEST_BULK_PRICE2);
        assertEquals(myTestItemBulk1.equals(wrongBulkPrice), false);
        //test equals() for items that are not the same
        assertEquals(myTestItem1.equals(myTestItem2), false);
        assertEquals(myTestItemBulk1.equals(myTestItemBulk2), false);
        //mirror items
        myTestItem2 = new Item(TEST_NAME1, TEST_PRICE1);
        myTestItemBulk2 = new Item(TEST_NAME1, TEST_PRICE1, 
                                   TEST_BULK_QUANTITY1, TEST_BULK_PRICE1);
        //test equals() for mirrored items
        assertEquals(myTestItem1.equals(myTestItem2), true);
        assertEquals(myTestItemBulk1.equals(myTestItemBulk2), true);
    }
    
    /**
     * Tests if the hash code is the same for mirrored items with and without a bulk price.
     */
    @Test
    public void testHashCode() {
        //mirror items
        myTestItem2 = new Item(TEST_NAME1, TEST_PRICE1);
        myTestItemBulk2 = new Item(TEST_NAME1, TEST_PRICE1, 
                                   TEST_BULK_QUANTITY1, TEST_BULK_PRICE1);
        //test hash code from mirrored items
        assertEquals(myTestItem1.hashCode(), myTestItem2.hashCode());
        assertEquals(myTestItemBulk1.hashCode(), myTestItemBulk2.hashCode());
    }

    /**
     * Tests to ensure constructor throws Exception with invalid name. 
     */
    @Test (expected = IllegalArgumentException.class)
    public void testNullName() {
        myTestItem1 = new Item(null, TEST_PRICE1);
    }
    
    /**
     * Tests to ensure constructor throws Exception with invalid name. 
     */
    @Test (expected = IllegalArgumentException.class)
    public void testBadName() {
        myTestItem1 = new Item("", TEST_PRICE1);
    }
    
    /**
     * Tests to ensure constructor throws Exception with null price. 
     */
    @Test (expected = IllegalArgumentException.class)
    public void testNullPrice() {
        myTestItem1 = new Item(TEST_NAME1, null);
    }
    
    /**
     * Tests to ensure constructor throws Exception with invalid price. 
     */
    @Test (expected = IllegalArgumentException.class)
    public void testBadPrice() {
        myTestItem1 = new Item(TEST_NAME1, BAD_PRICE);
    }
    
    /**
     * Tests to ensure constructor throws Exception with invalid bulk quantity. 
     */
    @Test (expected = IllegalArgumentException.class)
    public void testBadBulkQuantity() {
        myTestItemBulk1 = new Item(TEST_NAME1, TEST_PRICE1, 
                                   0, TEST_BULK_PRICE1);
    }
    
    /**
     * Tests to ensure constructor throws Exception with null bulk price. 
     */
    @Test (expected = IllegalArgumentException.class)
    public void testNullBulkPrice() {
        myTestItemBulk1 = new Item(TEST_NAME1, TEST_PRICE1, 
                                   TEST_BULK_QUANTITY1, null);
    }
    /**
     * Tests to ensure constructor throws Exception with invalid bulk price. 
     */
    @Test (expected = IllegalArgumentException.class)
    public void testBadBulkPrice() {
        myTestItemBulk1 = new Item(TEST_NAME1, TEST_PRICE1, 
                                   TEST_BULK_QUANTITY1, BAD_PRICE);
    }   
}