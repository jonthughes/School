/* 
 * TCSS 305 – Autumn 2014
 * Assignment 2 - ShoppingCart
 */

package model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * Represents an item.
 * 
 * @author Jonathan Hughes
 * @version 10 October 2014
 */
public final class Item {
    
    /** Formatter to display as a price in US Dollars. */
    public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    
    /** The name of the item. */
    private final String myName;
    
    /** The price of the item. */
    private final BigDecimal myPrice;
    
    /** The quantity of items in a bulk order. */
    private int myBulkQuantity;
    
    /** The discounted price for an entire bulk order (not per item). */
    private BigDecimal myBulkPrice;
    
    /**
     * Constructs an Item with the specified name and price.
     * Also sets myHasBulkPrice to false, since no bulk price was specified.
     * 
     * @param theName the name to assign to this item
     * @param thePrice the price to assign to this item
     * @throws IllegalArgumentException if the name is null
     *                                  OR the name has no length
     *                                  OR the price is null
     *                                  OR the price is less than or equal to zero
     */
    public Item(final String theName, final BigDecimal thePrice) {
        if (theName == null) {
            throw new IllegalArgumentException("The item's name must not be null!");
        }
        if (theName.length() <= 0) {
            throw new IllegalArgumentException("The item must have a name!");
        }  
        if (thePrice == null) {
            throw new IllegalArgumentException("The price must not be null!");
        }        
        if (thePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The price must be a positive value!");
        }
        myName = theName;
        myPrice = thePrice;
    }

    /**
     * Constructs an Item with the specified name, price, bulk quantity, and bulk order price.
     * Also sets myHasBulkPrice to true, since a bulk price and quantity were specified.
     * 
     * @param theName the name to assign to this item
     * @param thePrice the price to assign to this item
     * @param theBulkQuantity the minimum number of this item required for the bulk order price
     * @param theBulkPrice the discounted price for bulk orders
     * @throws IllegalArgumentException if the name is null
     *                                  OR the name has no length
     *                                  OR the price is null
     *                                  OR the price is less than or equal to zero
     *                                  OR the bulk quantity is less than or equal to one
     *                                  OR the bulk price is null
     *                                  OR the bulk price is less than or equal to zero
     */
    public Item(final String theName, final BigDecimal thePrice, final int theBulkQuantity,
                final BigDecimal theBulkPrice) {
        this(theName, thePrice);
        if (theBulkQuantity <= 1) {
            throw new IllegalArgumentException("The bulk quantity must be more than one!");
        }
        if (theBulkPrice == null) {
            throw new IllegalArgumentException("The bulk price must not be null!");
        }        
        if (theBulkPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The bulk price must be a positive value!");
        }
        myBulkQuantity = theBulkQuantity;
        myBulkPrice = theBulkPrice;
    }

    /**
     * Calculates the item's total price from the quantity of the item that was ordered.
     * 
     * @param theQuantity the quantity of the item that was ordered
     * @return the total price
     */
    public BigDecimal calculateItemTotal(final int theQuantity) {
        final int quantity = theQuantity;
        BigDecimal numOfBulkOrders;
        BigDecimal numOfStandardOrders;
        final BigDecimal quantityBigDecimal = new BigDecimal(quantity);
        final BigDecimal bulkQuantityBigDecimal = new BigDecimal(myBulkQuantity);
        BigDecimal itemTotal;
        if (myBulkPrice != null && quantity >= myBulkQuantity) { //valid bulk price
            numOfBulkOrders = quantityBigDecimal.divideToIntegralValue(bulkQuantityBigDecimal);
            numOfStandardOrders = quantityBigDecimal.remainder(bulkQuantityBigDecimal);
            itemTotal = numOfBulkOrders.multiply(myBulkPrice);
            itemTotal = itemTotal.add(numOfStandardOrders.multiply(myPrice));
        } else { //original price
            itemTotal = myPrice.multiply(quantityBigDecimal);
        }
        return itemTotal;
    }

    /**
     * Returns a String representation of the Item in the following format: 
     * "Item name, item price (bulk quantity for bulk price)".
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(128);
        result.append(myName);
        result.append(", ");
        result.append(CURRENCY_FORMAT.format(myPrice));
        if (myBulkPrice != null) {
            result.append(" (");
            result.append(myBulkQuantity);
            result.append(" for ");
            result.append(CURRENCY_FORMAT.format(myBulkPrice));
            result.append(')');
        }
        return result.toString();
    }

    /**
     * Creates a hash code for an Item object with its instance fields.
     * 
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(myName, myPrice, myBulkQuantity, myBulkPrice);
    }
    
    /**
     * Compares the instance fields of two Item objects for equality.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object theOther) {
        boolean result = false;
        //check if they are the exact same object
        if (this == theOther) {
            result = true;
        //check if it is not null and they are of the same class
        } else if ((theOther != null) && (theOther.getClass() == this.getClass())) {
            //cast theOther to the Item class
            final Item otherItem = (Item) theOther;
            //check if the instance variables are the same
            result = this.checkItemName(otherItem)
                  && this.checkItemPrice(otherItem)
                  && this.checkBulkPrice(otherItem)
                  && this.checkBulkQuantity(otherItem);           
        }
        return result;
    }
    
    /**
     * Checks if two Items' names are the same.
     * 
     * @param theOtherItem the other item to compare
     * @return boolean true if they are the same, false if not
     */
    private boolean checkItemName(final Item theOtherItem) {
        return this.myName.equals(theOtherItem.myName);
    }
    
    /**
     * Checks if two Items' prices are the same.
     * 
     * @param theOtherItem the other item to compare
     * @return boolean true if they are the same, false if not
     */
    private boolean checkItemPrice(final Item theOtherItem) {
        return this.myPrice.equals(theOtherItem.myPrice);
    }
    
    /**
     * Checks if two Items' bulk prices are the same.
     * 
     * @param theOtherItem the other item to compare
     * @return boolean true if they are the same, false if not
     */
    private boolean checkBulkPrice(final Item theOtherItem) {
        boolean result = true;
        //both items do not have a bulk price, considered the same
        if (myBulkPrice == null && theOtherItem.myBulkPrice == null) {
            result = true;
        }
        //only the other item has a bulk price, so they are not the same
        if (myBulkPrice == null && theOtherItem.myBulkPrice != null) {
            result = false;
        }
        //only this item has a bulk price, so they are not the same
        if (myBulkPrice != null && theOtherItem.myBulkPrice == null) {
            result = false; 
        } 
        //both items have a bulk price, so compare them
        if (myBulkPrice != null && theOtherItem.myBulkPrice != null) {
            result = myBulkPrice.equals(theOtherItem.myBulkPrice);
        }
        return result;
    }
    
    /**
     * Checks if two Items' bulk quantities are the same.
     * 
     * @param theOtherItem the other item to compare
     * @return boolean true if they are the same, false if not
     */
    private boolean checkBulkQuantity(final Item theOtherItem) {
        return myBulkQuantity == theOtherItem.myBulkQuantity;
    }
}
