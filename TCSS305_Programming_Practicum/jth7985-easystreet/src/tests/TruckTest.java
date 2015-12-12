/* 
 * TCSS 305 – Autumn 2014
 * Assignment 3 - easystreet
 */

package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import model.Direction;
import model.Light;
import model.Terrain;
import model.Truck;

import org.junit.Test;

/**
 * Tests the Truck class.
 * 
 * @author Alan Fowler (acfowler@uw.edu)
 * @author Jonathan Hughes - started with HumanTest class, changed to test Truck class
 * @version 23 October 2014
 */
public class TruckTest {

    /**
     * The number of times to repeat a test to have a high probability that all
     * random possibilities have been explored.
     */
    private static final int TRIES_FOR_RANDOMNESS = 100;
    
    /** Test method for truck constructor. */
    @Test
    public void testTruck() {
        final Truck truck = new Truck(10, 11, Direction.WEST);
        final Truck truckCenter = new Truck(10, 11, Direction.CENTER);
        assertEquals("Truck x-coordinate not initialized correctly!", 10, truck.getX());
        assertEquals("Truck y-coordinate not initialized correctly!", 11, truck.getY());
        assertEquals(" direction not initialized correctly!",
                     Direction.WEST, truck.getDirection());
        assertEquals(" direction not initialized correctly!",
                     Direction.CENTER, truckCenter.getDirection());
        assertEquals("Truck death time not initialized correctly!", 0, truck.getDeathTime());
        assertTrue("Truck isAlive() fails initially!", truck.isAlive());
    }
    
    /** Test method for truck setters. */
    @Test
    public void testTruckSetters() {
        final Truck truck = new Truck(10, 11, Direction.WEST);
        
        truck.setX(12);
        assertEquals("Truck setX failed!", 12, truck.getX());
        truck.setY(13);
        assertEquals("Truck setY failed!", 13, truck.getY());
        truck.setDirection(Direction.NORTH);
        assertEquals("Truck setDirection failed!", Direction.NORTH, truck.getDirection());
    }

    /**
     * Test method for {@link Truck#canPass(Terrain, Light)}.
     */
    @Test
    public void testCanPass() {
        //set valid starting terrain (street or light)
        final Terrain[] validStartingTerrain = {Terrain.STREET, Terrain.LIGHT};
        // start from street or light
        for (final Terrain testTerrain : validStartingTerrain) {
            final Truck truck = new Truck(0, 0, Direction.WEST);
            // go to each terrain type
            for (final Terrain t : Terrain.values()) {
                // try the test under each light condition
                for (final Light l : Light.values()) {
                    if ((t.equals(testTerrain))
                        || (t == Terrain.LIGHT && testTerrain == Terrain.STREET)
                        || (t == Terrain.STREET && testTerrain == Terrain.LIGHT)) {
                        // trucks can pass the terrain they start on under any light
                        // conditions, and can also pass lights if they start on
                        // streets and vice-versa
                        assertTrue("truck started on " + testTerrain
                                                   + " should be able to pass " + t
                                                   + ", with light " + l,
                                   truck.canPass(t, l));
                    } else {
                        // trucks can't leave streets or lights
                        assertFalse("truck started on " + testTerrain
                                     + " should NOT be able to pass " + t
                                     + ", with light " + l, truck.canPass(t, l));
                    }
                }
            }
        }
    }

    /**
     * Test method for {@link Truck#chooseDirection(java.util.Map)}.
     */
    @Test
    public void testChooseDirection() {
        final Truck truckNorth = new Truck(0, 0, Direction.NORTH);
        final Truck truckCenter = new Truck(0, 0, Direction.CENTER);
        final Truck truckEast = new Truck(0, 0, Direction.EAST);
        final Truck truckSouth = new Truck(0, 0, Direction.SOUTH);
        
        //trucks need to stay on their own terrain, but otherwise can do anything
        //trucks can not choose center. Choosing center would result in no movement.
        final Map<Direction, Terrain> neighbors = new HashMap<Direction, Terrain>();
        neighbors.put(Direction.WEST, Terrain.WALL);
        neighbors.put(Direction.NORTH, Terrain.WALL);
        
        for (final Terrain t : Terrain.values()) {
            //trucks start on streets or lights
            if (t == Terrain.STREET || t == Terrain.LIGHT) { 
                //test valid terrain
                neighbors.put(Direction.CENTER, t);
                neighbors.put(Direction.EAST, t);
                neighbors.put(Direction.SOUTH, t);
                //test truck facing center
                assertFalse("truck facing center should not stay facing center", 
                            truckCenter.chooseDirection(neighbors).equals(Direction.CENTER));
                
                //test truck facing east
                int tries = 0;
                boolean seenSouth = false;
                boolean seenEast = false;
                while (tries < TRIES_FOR_RANDOMNESS) {
                    tries = tries + 1;
                    final Direction dir = truckEast.chooseDirection(neighbors);
                    assertTrue("invalid dir chosen, should be east or south, was " + dir,
                               dir == Direction.EAST || dir == Direction.SOUTH);
                    seenSouth = seenSouth || dir == Direction.SOUTH;
                    seenEast = seenEast || dir == Direction.EAST;
                }
                neighbors.put(Direction.EAST, Terrain.WALL);
                tries = 0;
                while (tries < TRIES_FOR_RANDOMNESS) {
                    tries = tries + 1;
                    final Direction dir = truckEast.chooseDirection(neighbors);
                    assertSame("invalid dir chosen, should be south, was " + dir,
                               dir, Direction.SOUTH);
                }
                //test truck facing south
                tries = 0;
                seenSouth = false;
                seenEast = false;
                while (tries < TRIES_FOR_RANDOMNESS) {
                    tries = tries + 1;
                    final Direction dir = truckSouth.chooseDirection(neighbors);
                    assertTrue("invalid dir chosen, should be east or south, was " + dir,
                               dir == Direction.EAST || dir == Direction.SOUTH);
                    seenSouth = seenSouth || dir == Direction.SOUTH;
                    seenEast = seenEast || dir == Direction.EAST;
                }
                neighbors.put(Direction.EAST, Terrain.WALL);
                tries = 0;
                while (tries < TRIES_FOR_RANDOMNESS) {
                    tries = tries + 1;
                    final Direction dir = truckSouth.chooseDirection(neighbors);
                    assertSame("invalid dir chosen, should be south, was " + dir,
                               dir, Direction.SOUTH);
                }
            }
        }
        //test specific weird cases
        
        //test island, do anything but turn center
        neighbors.put(Direction.EAST, Terrain.GRASS);
        neighbors.put(Direction.SOUTH, Terrain.TRAIL);
        neighbors.put(Direction.CENTER, Terrain.STREET);
        assertFalse("truck should not face center", 
                    truckNorth.chooseDirection(neighbors).equals(Direction.CENTER));
        //test dead-end, required to turn around
        neighbors.put(Direction.SOUTH, Terrain.STREET);
        assertTrue("truck should face south", 
                   truckNorth.chooseDirection(neighbors).equals(Direction.SOUTH));
        //test required to turn left
        neighbors.put(Direction.WEST, Terrain.STREET);
        assertTrue("truck should face west", 
                   truckNorth.chooseDirection(neighbors).equals(Direction.WEST));
        
    }
}