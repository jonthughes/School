/* 
 * TCSS 305 – Autumn 2014
 * Assignment 3 - easystreet
 */

package model;

import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Represents an abstract vehicle.
 * 
 * @author Jonathan Hughes
 * @version 15 October 2014
 */
public abstract class AbstractVehicle implements Vehicle {
    
    /** A Random that we use for generating random directions. */
    protected static final Random RANDOM = new Random();
    
    /** An English locale for converting Strings. */
    private static final Locale ENGLISH_LOCALE = Locale.ENGLISH;
    
    /** The type of vehicle. */
    private final String myType;
    
    /** The current x-coordinate of the vehicle. */
    private int myX;
    
    /** The original x-coordinate of the vehicle. */
    private final int myOriginalX;    
    
    /** The current y-coordinate of the vehicle. */
    private int myY;
    
    /** The original y-coordinate of the vehicle. */
    private final int myOriginalY;
    
    /** The current direction of the vehicle. */
    private Direction myDirection;
    
    /** The original direction of the vehicle. */
    private final Direction myOriginalDirection;
    
    /** 
     * The amount of time that this vehicle stays dead after it is killed.
     *  
     * NOTE: The larger the death time, the easier it is for this vehicle to 
     * be killed.  Any vehicle with a lesser death time will kill it upon 
     * collision.
     */
    private final int myDeathTime;
    
    /** Whether this vehicle is still alive or not. */
    private boolean myIsAlive;
    
    /** When dead, the number of turns until this vehicle is alive again. */
    private int myTurnsUntilAlive;
    
    /** The image file name for when this vehicle is alive. */
    private final String myImageFileNameAlive;
    
    /** The image file name for when this vehicle is dead. */   
    private final String myImageFileNameDead;
    
    /** Whether this vehicle is on a rampage. */
    private boolean myIsOnRampage;
    
    /**
     * Constructs an abstract vehicle.
     * 
     * @param theX the starting x-coordinate for this vehicle
     * @param theY the starting y-coordinate for this vehicle
     * @param theDir the starting direction for this vehicle
     * @param theDeathTime the amount of time that this vehicle stays dead after it is killed
     */
    protected AbstractVehicle(final int theX, final int theY, final Direction theDir, 
                           final int theDeathTime) {
        myOriginalX = theX;
        myOriginalY = theY;
        myOriginalDirection = theDir;
        myDeathTime = theDeathTime;
        //uses the simple class name as the type of vehicle, e.g. "Truck"
        myType = getClass().getSimpleName();
        //converts vehicle type to lower case and adds ".gif", e.g. "truck.gif"
        myImageFileNameAlive = myType.toLowerCase(ENGLISH_LOCALE) + ".gif";
        //converts vehicle type to lower case and adds "_dead.gif", e.g. "truck_dead.gif"
        myImageFileNameDead = myType.toLowerCase(ENGLISH_LOCALE) + "_dead.gif";
        //sets all instances fields to the stored original settings
        setOriginalSettings();
    }
    
    /**
     * Sets instance fields to the original settings.
     */
    private void setOriginalSettings() {
        myX = myOriginalX;
        myY = myOriginalY;
        myDirection = myOriginalDirection;
        myIsAlive = true;
        myTurnsUntilAlive = 0;
        myIsOnRampage = false;
    }

    /**
     * Models a collision of two live vehicles.  When a collision occurs, if there is a 
     * vehicle with a longer death time, it dies and its turns until alive is set to the 
     * death time.  If a vehicle killed another vehicle, it goes on a rampage.
     * 
     * @param theOther the other vehicle that is colliding with this vehicle
     * 
     * {@inheritDoc}
     */
    @Override
    public void collide(final Vehicle theOther) {
        //if a live vehicle is more easily killed than the other, kill it
        if ((theOther.getDeathTime() < myDeathTime) && myIsAlive) {
            myIsAlive = false;
            myTurnsUntilAlive = myDeathTime;
            //if vehicle is on a rampage, end the rampage
            if (myIsOnRampage) {
                myIsOnRampage = false;
            }
        //if a vehicle is less easily killed than the other, and it is killed, go on a rampage
        } else if ((theOther.getDeathTime() > myDeathTime) && myIsAlive 
                        //the other vehicle is about to die or just died
                        && (theOther.isAlive() || ((AbstractVehicle) theOther).justDied())) {
            myIsOnRampage = true;
        }
    }

    /**
     * Returns the amount of time that this vehicle stays dead after it is killed.
     * 
     * @return the amount of time that this vehicle stays dead after it is killed
     * 
     * {@inheritDoc}
     */
    @Override
    public int getDeathTime() {
        return myDeathTime;
    }

    /** 
     * Returns the current image file name.
     * 
     * @return alive vehicle image file name if alive, or dead vehicle image file name if dead
     * 
     * {@inheritDoc}
     */
    @Override
    public String getImageFileName() {
        String result = myImageFileNameAlive;
        if (!myIsAlive) { //if the vehicle is dead
            result = myImageFileNameDead;
        }
        return result;
    }

    /**
     * Returns the current direction of this vehicle.
     * 
     * @return the current direction of this vehicle
     * 
     * {@inheritDoc}
     */
    @Override
    public Direction getDirection() {
        return myDirection;
    }

    /**
     * Returns the current x-coordinate of the vehicle.
     * 
     * @return the current x-coordinate of the vehicle
     * 
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return myX;
    }

    /**
     * Returns the current y-coordinate of the vehicle.
     * 
     * @return the current y-coordinate of the vehicle
     * 
     * {@inheritDoc}
     */   
    @Override
    public int getY() {
        return myY;
    }

    /**
     * Returns whether this vehicle is alive.
     * 
     * @return whether this vehicle is alive
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return myIsAlive;
    }

    /**
     * The vehicle's counter for turns until revived is reduced by one, if this 
     * vehicle is not alive.  If the counter reaches zero, the vehicle's state is
     * change from dead to alive and it starts in a random direction.
     * 
     * {@inheritDoc}
     */
    @Override
    public void poke() {
        //if not ready to revive
        if (myTurnsUntilAlive > 0) {
            //reduce count of turns until alive by 1
            myTurnsUntilAlive--;
        }
        //if now ready to revive
        if (myTurnsUntilAlive == 0) {
             //revive vehicle
            myIsAlive = true;
            //choose a random direction
            myDirection = Direction.random();
        }
    }
    
    /**
     * Changes this vehicle to its starting conditions.
     * 
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        setOriginalSettings();
    }

    /**
     * Sets this object's direction to the given value.
     * 
     * {@inheritDoc}
     */
    @Override
    public void setDirection(final Direction theDir) {
        myDirection = theDir;
    }

    /**
     * Sets this object's x-coordinate to the given value.
     * 
     * {@inheritDoc}
     */
    @Override
    public void setX(final int theX) {
        myX = theX;
    }

    /**
     * Sets this object's y-coordinate to the given value.
     * 
     * {@inheritDoc}
     */
    @Override
    public void setY(final int theY) {
        myY = theY;
    }
    
    /**
     * Returns a String of the vehicle type when alive, if dead returns "Pokes:" and 
     * turns until alive. If on a rampage, displays "Rampaging" in front of the type.
     * 
     * @return a String of the vehicle type when alive, if dead returns "Pokes:" and 
     * turns until alive.
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String result = myType;
        if (myIsOnRampage) {
            result = "Rampaging " + myType;
        }
        if (!myIsAlive) {
            result = "Pokes:" + myTurnsUntilAlive;
        }
        return result;
    }
    
    /**
     * Returns whether or not this vehicle may move onto the given type of
     * terrain, when the street lights are the given color.
     * 
     * @param theTerrain the terrain
     * @param theLight the light color
     * @return whether or not this object may move onto the given type of
     *         terrain, when the street lights are the given color
     * 
     * {@inheritDoc}
     */
    @Override
    public abstract boolean canPass(final Terrain theTerrain, final Light theLight);

    /**
     * Returns the direction this vehicle would like to move, based upon the given
     * map of the neighboring terrain.
     * 
     * @param theNeighbors a map of neighboring terrain.
     * @return the direction this vehicle would like to move.
     * 
     * {@inheritDoc}
     */
    @Override
    public abstract Direction chooseDirection(final Map<Direction, Terrain> theNeighbors);
    
    /** 
     * Returns whether or not this vehicle may move onto the given type of
     * terrain, ignoring street light color (pass.
     * 
     * @param theTerrain the terrain to check if the vehicle can travel on it
     * @return true if the vehicle can travel on the terrain, otherwise false
     */
    protected boolean canTravelOnTerrain(final Terrain theTerrain) {
        return canPass(theTerrain, Light.GREEN);
    }
    
    /**
     * Returns whether this vehicle has just died.
     * 
     * @return whether this vehicle has just died
     */
    private boolean justDied() {
        boolean result = false;
        if (myTurnsUntilAlive == myDeathTime) {
            result = true;
        }
        return result;       
    }
}
