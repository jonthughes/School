/* 
 * TCSS 305 – Autumn 2014
 * Assignment 3 - easystreet
 */

package model;

import java.util.Map;

/**
 * Represents a car.
 * 
 * @author Jonathan Hughes
 * @version 15 October 2014
 */
public class Car extends AbstractVehicle {
    
    /** The number of turns until this vehicle comes back alive after it is killed. */
    private static final int DEATH_TIME = 10;

    /**
     * Constructs a car object.
     * 
     * @param theX the starting x-coordinate for this vehicle
     * @param theY the starting y-coordinate for this vehicle
     * @param theDir the starting direction for this vehicle
     */
    public Car(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, DEATH_TIME);
    }
    
    /**
     * Returns whether or not this car may move onto the given type of
     * terrain, when the street lights are the given color.  
     * 
     * Behavior: Returns true for a street or at green or yellow lights.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean result = false;
        if (theTerrain.equals(Terrain.STREET)
                        || (theTerrain.equals(Terrain.LIGHT) && !theLight.equals(Light.RED))) {
            result = true;
        }
        return result;
    }

    /**
     * Returns the direction this vehicle would like to move, based upon the given
     * map of the neighboring terrain.
     * 
     * Behavior: Cars can only travel on streets and through lights. A car prefers to 
     * drive straight ahead on the street if it can. If it cannot move straight ahead, 
     * it turns left if possible; if it cannot turn left, it turns right if possible; 
     * as a last resort, it turns around. Cars stop for red lights; if a traffic light is
     * immediately ahead of the car and the light is red, the car stays still and does 
     * not move. It does not turn to avoid the light. When the light turns green, the car 
     * resumes its original direction. Cars ignore yellow and green lights.
     *
     * @param theNeighbors a map of neighboring terrain.
     * @return the direction this vehicle would like to move.
     * 
     * {@inheritDoc}
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction result = Direction.random();
        //find current direction
        Direction initialDirection = getDirection();
        //if current direction is center, choose a random direction
        if (initialDirection.equals(Direction.CENTER)) {
            initialDirection = Direction.random();
        }
        //find terrain in each direction
        final Terrain leftTerrain = theNeighbors.get(initialDirection.left());
        final Terrain rightTerrain = theNeighbors.get(initialDirection.right());
        final Terrain forwardTerrain = theNeighbors.get(initialDirection);
        final Terrain reverseTerrain = theNeighbors.get(initialDirection.reverse());
        //if the vehicle can go forward, go forward
        if (canTravelOnTerrain(forwardTerrain)) {
            result = initialDirection;
        //else if the vehicle can go left, go left
        } else if (canTravelOnTerrain(leftTerrain)) {
            result = initialDirection.left();
        //else if the vehicle can go right, go right
        } else if (canTravelOnTerrain(rightTerrain)) {
            result = initialDirection.right();
        //else if the vehicle can go reverse, go reverse
        } else if (canTravelOnTerrain(reverseTerrain)) {
            result = initialDirection.reverse();
        } 
        return result;
    }
}