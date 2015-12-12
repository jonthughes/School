/* 
 * TCSS 305 – Autumn 2014
 * Assignment 2 - ShoppingCart
 */

package model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a shopping cart with a collection of item orders.
 * 
 * @author Jonathan Hughes
 * @version 10 October 2014
 */
public class ShoppingCart {

    /** If customer has a membership for a discount or not. True for yes, false if no. */
    private boolean myMembership;
    
    /** A set of all the item orders that were added to the shopping cart. */
    private final Map<Item, ItemOrder> myCart;

    /** Constructs an empty shopping cart as a HashMap, without a store membership. */
    public ShoppingCart() {
        myCart = new HashMap<>();
        myMembership = false;
    }

    /** 
     * Adds the specified ItemOrder to the shopping cart.
     * Note: If the Item objects are the same as an existing order, 
     * the old order will be over written.
     * 
     * @param theOrder the item order to add to the shopping cart
     * @throws IllegalArgumentException if the item order is null
     */
    public void add(final ItemOrder theOrder) {
        if (theOrder == null) {
            throw new IllegalArgumentException("The item order must not be null!");
        }
        myCart.put(theOrder.getItem(), theOrder);
    }

    /**
     * Changes the membership status of the customer's shopping cart for discount purposes.
     * 
     * @param theMembership requested membership status, member = true, non-member = false
     */
    public void setMembership(final boolean theMembership) {
        myMembership = theMembership;
    }

    /**
     * Calculates the total price of all item orders in the shopping cart.  
     * Applies a 10% discount if customer has a membership.
     * 
     * @return the total price of all items in the shopping cart
     */
    public BigDecimal calculateTotal() {
        BigDecimal result = BigDecimal.ZERO;
        //iterate through all item orders, add their individual total to overall total
        for (final Entry<Item, ItemOrder> entry : myCart.entrySet()) {
            final ItemOrder value = entry.getValue();
            result = result.add(value.calculateOrderTotal());
        }
        //if customer has a membership, apply 10% discount
        if (myMembership) {
            final BigDecimal discount = new BigDecimal("0.9");
            result = result.multiply(discount);
        }
        return result;
    }

    /**
     * Returns a String representation of all item orders in the shopping cart separated by 
     * semicolons in the following format: 
     * "ItemOrder; Item Order; Item Order;", etc.
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(128);
        for (final Entry<Item, ItemOrder> entry : myCart.entrySet()) {
            final ItemOrder value = entry.getValue();
            result.append(value.toString());
            result.append("; ");
        }
        return result.toString();
    }
}
