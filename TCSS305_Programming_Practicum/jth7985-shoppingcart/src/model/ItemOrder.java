/* 
 * TCSS 305 – Autumn 2014
 * Assignment 2 - ShoppingCart
 */

package model;

import java.math.BigDecimal;

/**
 * Represents an item order with an Item object and the number of Items ordered.
 * 
 * @author Jonathan Hughes
 * @version 10 October 2014
 */
public final class ItemOrder {
    
    /** The Item object that is associated with this ItemOrder. */
    private final Item myItem;
    
    /** The quantity of the items that were ordered. */
    private final int myQuantity;

    /**
     * Constructs an ItemOrder with the specified item and quantity.
     * 
     * @param theItem the Item that is in the order
     * @param theQuantity the number of Items in the order
     * @throws IllegalArgumentException if the item is null
     *                                  OR the quantity is less than zero
     */
    public ItemOrder(final Item theItem, final int theQuantity) {
        if (theItem == null) {
            throw new IllegalArgumentException("The item cannot be null!");
        }
        if (theQuantity < 0) {
            throw new IllegalArgumentException("The quantity cannot be less than zero!");
        }
        myItem = theItem;
        myQuantity = theQuantity;
    }

    /**
     * Calculates the total price of the ItemOrder.
     * 
     * @return the total price of the ItemOrder
     */
    public BigDecimal calculateOrderTotal() {
        return myItem.calculateItemTotal(myQuantity);
    }

    /**
     * Gets the Item object associated with this order.
     * 
     * @return the Item associated with this order
     */
    public Item getItem() {
        return myItem;
    }

    /**
     * Returns a String representation of the ItemOrder in the following format: 
     * "Quantity: quantity Item: item".
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(128);
        result.append("Quantity: ");
        result.append(myQuantity);
        result.append(" Item: ");
        result.append(myItem.toString());
        return result.toString();
    } 
}
