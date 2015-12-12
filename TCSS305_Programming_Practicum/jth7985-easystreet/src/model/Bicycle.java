/* 
 * TCSS 305 – Autumn 2014
 * Assignment 3 - easystreet
 */

package model;

import java.util.Map;

/**
 * Represents a bicycle.
 * 
 * @author Jonathan Hughes
 * @version 15 October 2014
 */
public class Bicycle extends AbstractVehicle {
    
    /** The number of turns until this vehicle comes back alive after it is killed. */
    private static final int DEATH_TIME = 20;
    
    /**
     * Constructs a bicycle object.
     * 
     * @param theX the starting x-coordinate for this vehicle
     * @param theY the starting y-coordinate for this vehicle
     * @param theDir the starting direction for this vehicle
     */
    public Bicycle(final int theX, final int theY, final Direction theDir) {
        super(theX, theY, theDir, DEATH_TIME);
    }
    
    /**
     * Returns whether or not this bicycle may move onto the given type of
     * terrain, when the street lights are the given color. 
     * 
     * Behavior: Returns true for a street, green light, or trail.
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean canPass(final Terrain theTerrain, final Light theLight) {
        boolean result = false;
        if (theTerrain.equals(Terrain.STREET) || theTerrain.equals(Terrain.TRAIL)
            || (theTerrain.equals(Terrain.LIGHT) && theLight.equals(Light.GREEN))) {
            result = true;
        }
        return result;
    }

    /**
     * Returns the direction this vehicle would like to move, based upon the given
     * map of the neighboring terrain.
     * 
     * Behavior: Bicycles can travel on streets and through lights, but they prefer to 
     * travel on trails. If a bicycle is on a trail, it always goes straight ahead in 
     * the direction it is facing. Trails are guaranteed to be straight (horizontal or 
     * vertical) lines that end at streets, and you are guaranteed that a bicycle will 
     * never start on a trail facing terrain it cannot traverse. If a bicycle is not 
     * on a trail, but there is a trail either straight ahead, to the left, or to the 
     * right of the bicycle’s current direction (any neighboring direction except reverse), 
     * then the bicycle turns to face the trail and moves in that direction. You may assume 
     * that the map is laid out so that only one trail will neighbor a bicycle at any given
     * time. If a bicycle is on a street and there is no trail to the left or right, it 
     * prefers to move straight ahead on the street if it can. If it cannot move straight 
     * ahead, it turns left if possible; if it cannot turn left, it turns right if possible. 
     * As a last resort, if none of these three directions is legal (all not streets or 
     * lights), the bicycle turns around. Bicycles ignore green lights. Bicycles stop for 
     * yellow and red lights; if a traffic light is immediately ahead of the bicycle and 
     * the light is not green, the bicycle stays still and does not move unless a trail is 
     * to the left or right. If a bicycle is facing a red or yellow light and there is a 
     * trail to the left or right, the bicycle will turn to face the trail.
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
        final Terrain currentTerrain = theNeighbors.get(Direction.CENTER);
        //if the bike is on a trail, go forward
        if (currentTerrain.equals(Terrain.TRAIL)) {
            result = initialDirection;
        //if not on a trail and there is a trail on the left, go left
        } else if (leftTerrain.equals(Terrain.TRAIL)) {
            result = initialDirection.left();
        //if not on a trail and there is a trail on the right, go right
        } else if (rightTerrain.equals(Terrain.TRAIL)) {
            result = initialDirection.right();
        //if it can go forward still (street or light), go forward
        } else if (canTravelOnTerrain(forwardTerrain)) {
            result = initialDirection;
        //else if the vehicle can go left, go left
        } else if (canTravelOnTerrain(leftTerrain)) {
            result = getDirection().left();
        //else if the vehicle can go right, go right
        } else if (canTravelOnTerrain(rightTerrain)) {
            result = getDirection().right();
        //else if the vehicle can go reverse, go reverse
        } else if (canTravelOnTerrain(reverseTerrain)) {
            result = getDirection().reverse();
        } 
        return result;
    }
}