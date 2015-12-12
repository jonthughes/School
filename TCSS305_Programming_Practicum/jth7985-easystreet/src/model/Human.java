/* 
 * TCSS 305 – Autumn 2014
 * Assignment 3 - easystreet
 */

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a human.
 * 
 * @author Jonathan Hughes
 * @version 15 October 2014
 */
public class Human extends AbstractVehicle {
    
    /** The number of turns until this vehicle comes back alive after it is killed. */
    private static final int DEATH_TIME = 50;
    
    /** The vehicle's original terrain. */
    private final Terrain myOriginalTerrain;
    
    /**
     * Constructs a human object.
     * 
     * @param theX the starting x-coordinate for this vehicle
     * @param theY the starting y-coordinate for this vehicle
     * @param theDir the starting direction for this vehicle
     * @param theTerrain the starting terrain for this vehicle
     */
    public Human(final int theX, final int theY, final Direction theDir, 
                 final Terrain theTerrain) {
        super(theX, theY, theDir, DEATH_TIME);
        myOriginalTerrain = theTerrain;
    }
    
    /**
     * Returns whether or not this human may move onto the given type of
     * terrain, when the street lights are the given color.  
     * 
     * Behavior: Returns true for any terrain when it matches its original terrain 
     * (streets and lights count as the same starting terrain).
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean result = false;
        if (myOriginalTerrain.equals(theTerrain)) {
            result = true;
        } else if ((myOriginalTerrain.equals(Terrain.STREET) 
                  || myOriginalTerrain.equals(Terrain.LIGHT)) 
                  && (theTerrain.equals(Terrain.STREET) || theTerrain.equals(Terrain.LIGHT))) {
            result = true;
        }
        return result;
    }

    /**
     * Returns the direction this vehicle would like to move, based upon the given
     * map of the neighboring terrain.
     * 
     * Behavior: A Human moves in a random direction (straight, left, right, or reverse), 
     * always on terrain that matches his initial terrain. Streets and lights are 
     * considered the same terrain for this purpose. If a human starts on a street or a 
     * light, he roams on streets and through lights; if he starts on grass, he roams that 
     * body of grass; etc. Humans ignore the color of traffic lights.
     * 
     * @param theNeighbors a map of neighboring terrain.
     * @return the direction this vehicle would like to move.
     * 
     * {@inheritDoc}
     */
    @Override
    public Direction chooseDirection(final Map<Direction, Terrain> theNeighbors) {
        Direction result = Direction.random();
        //find terrain in each direction
        final Terrain westTerrain = theNeighbors.get(Direction.WEST);
        final Terrain eastTerrain = theNeighbors.get(Direction.EAST);
        final Terrain northTerrain = theNeighbors.get(Direction.NORTH);
        final Terrain southTerrain = theNeighbors.get(Direction.SOUTH);
        //create a list to hold directions that the vehicle can go in
        List<Direction> preferredDirections;
        preferredDirections = new ArrayList<>();
        //check if the vehicle can go west
        if (canTravelOnTerrain(westTerrain)) {
            preferredDirections.add(Direction.WEST);
        }
        //check if the vehicle can go east
        if (canTravelOnTerrain(eastTerrain)) {
            preferredDirections.add(Direction.EAST);
        }
        //check if the vehicle can go north
        if (canTravelOnTerrain(northTerrain)) {
            preferredDirections.add(Direction.NORTH);
        }
        //check if the vehicle can go south
        if (canTravelOnTerrain(southTerrain)) {
            preferredDirections.add(Direction.SOUTH);
        }
        //if the vehicle can go the other directions, pick a random one
        if (!preferredDirections.isEmpty()) {
            final int index = RANDOM.nextInt(preferredDirections.size());
            result = preferredDirections.get(index);
        }
        return result;
    }
}