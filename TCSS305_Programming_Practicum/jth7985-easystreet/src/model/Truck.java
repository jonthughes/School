/* 
 * TCSS 305 – Autumn 2014
 * Assignment 3 - easystreet
 */

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a truck.
 * 
 * @author Jonathan Hughes
 * @version 15 October 2014
 */
public class Truck extends AbstractVehicle {
    
    /** The number of turns until this vehicle comes back alive after it is killed. */
    private static final int DEATH_TIME = 0;

    /**
     * Constructs a truck object.
     * 
     * @param theX the starting x-coordinate for this vehicle
     * @param theY the starting y-coordinate for this vehicle
     * @param theDir the starting direction for this vehicle
     */
    public Truck(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, DEATH_TIME);
    }
    
    /**
     * Returns whether or not this truck may move onto the given type of
     * terrain, when the street lights are the given color.  
     * 
     * Behavior: Returns true when it is on a street or at any color of light.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean result = false;
        if (theTerrain.equals(Terrain.STREET) || theTerrain.equals(Terrain.LIGHT)) {
            result = true;
        }
        return result;
    }

    /**
     * Returns the direction this vehicle would like to move, based upon the given
     * map of the neighboring terrain.
     * 
     * Behavior: Trucks travel only on streets and through lights. Their preferred movement is
     * to randomly select to go straight, turn left, or turn right. As a last resort, if none 
     * of these three directions is legal (all not streets or lights), the truck turns around. 
     * Trucks drive through all traffic lights without stopping! 
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
        //create a list to hold directions that the vehicle can go in
        List<Direction> preferredDirections;
        preferredDirections = new ArrayList<>();
        //check if the vehicle can go left
        if (canTravelOnTerrain(leftTerrain)) {
            preferredDirections.add(initialDirection.left());
        }
        //check if the vehicle can go right
        if (canTravelOnTerrain(rightTerrain)) {
            preferredDirections.add(initialDirection.right());
        }
        //check if the vehicle can go forward
        if (canTravelOnTerrain(forwardTerrain)) {
            preferredDirections.add(initialDirection);
        }
        //if the vehicle cannot go the other directions, check if it can go reverse
        if (preferredDirections.isEmpty()) {
            if (canTravelOnTerrain(reverseTerrain)) {
                //change direction to reverse
                result = initialDirection.reverse();
            } else { //if the vehicle cannot go in reverse, e.g. on an island, choose randomly
                result = Direction.random();
            }
        } else { //if the vehicle can go in one of the other directions, pick one randomly
            final int index = RANDOM.nextInt(preferredDirections.size());
            result = preferredDirections.get(index);           
        }
        return result;
    }
}

