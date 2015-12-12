/* 
 * TCSS 305 – Autumn 2014
 * Assignment 1 - Testing
 */

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

/**
 * A JUnit test class for testing the Circle class.
 * 
 * @author Jonathan Hughes
 * @version 05 October 2014
 */
public class CircleTest {

    /** A double that is used as the tolerance for testing the equality of doubles. */
    private static final double TOLERANCE = .00001;
    
    /** A double that is a valid radius input for testing. */
    private static final double VALID_RADIUS = 2;
    
    /** This is a Point that has valid inputs for testing. */
    private static final Point VALID_POINT = new Point(2, 2);
    
    /** This is a Color that is a valid input for testing. */
    private static final Color VALID_COLOR = Color.BLUE;
    
    /** A double that is an invalid radius input for testing. */
    private static final double INVALID_RADIUS = -1;
    
    /** This is a Point that has invalid inputs for testing. */
    private static final Point INVALID_POINT = null;
    
    /** This is a Color that is an invalid input for testing. */
    private static final Color INVALID_COLOR = null;
    
    /** A circle object for testing. */
    private Circle myTestCircle;
    
    /**
     * Sets up a new Circle named myTestCircle for testing before each test method.
     * 
     * @throws java.lang.Exception Throws an Exception with invalid input.
     */
    @Before
    public void setUp() throws Exception {
        myTestCircle = new Circle();
    }

    /**
     * Test Circle constructor with an invalid radius input and valid other inputs.
     * This should cause an IllegalArgumentException
     */
    @Test (expected = IllegalArgumentException.class)
    public void testCircleInvalidRadius() {
        myTestCircle = new Circle(INVALID_RADIUS, VALID_POINT, VALID_COLOR);
    }
    
    /**
     * Test Circle constructor with an invalid Point input and valid other inputs.
     * This should cause an IllegalArgumentException
     */
    @Test (expected = IllegalArgumentException.class)
    public void testCircleInvalidPoint() {
        myTestCircle = new Circle(VALID_RADIUS, INVALID_POINT, VALID_COLOR);
    }
    
    /**
     * Test Circle constructor with an invalid Color input and valid other inputs.
     * This should cause an IllegalArgumentException
     */
    @Test (expected = IllegalArgumentException.class)
    public void testCircleInvalidColor() {
        myTestCircle = new Circle(VALID_RADIUS, VALID_POINT, INVALID_COLOR);
    }
    
    /**
     * Tests the setRadius method of the Circle class with a valid non-default double.
     */
    @Test
    public void testSetRadius() {
        myTestCircle.setRadius(VALID_RADIUS);
        assertEquals(myTestCircle.getRadius(), VALID_RADIUS, TOLERANCE);
    }
    
    /**
     * Tests the setRadius method with an invalid radius.
     * This should cause an IllegalArgumentException
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetRadiusException() {
        myTestCircle.setRadius(INVALID_RADIUS);
    }

    /**
     * Tests the setCenter method of the Circle class with a valid non-default Point.
     */
    @Test
    public void testSetCenter() {
        myTestCircle.setCenter(VALID_POINT);
        assertEquals(myTestCircle.getCenter(), VALID_POINT);
    }
    
    /**
     * Tests the setCenter method with an invalid Point.
     * This should cause an IllegalArgumentException
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetCenterException() {
        myTestCircle.setCenter(INVALID_POINT);
    }

    /**
     * Tests the setColor method of the Circle class with a valid non-default Color.
     */
    @Test
    public void testSetColor() {
        myTestCircle.setColor(VALID_COLOR);
        assertEquals(myTestCircle.getColor(), VALID_COLOR);
    }
    
    /**
     * Tests the setColor method with an invalid Color.
     * This should cause an IllegalArgumentException
     */
    @Test (expected = IllegalArgumentException.class)
    public void testSetColorException() {
        myTestCircle.setColor(INVALID_COLOR);
    }

    /**
     * Tests the calculateDiameter method of the Circle class from default values.
     * The formula is circumference = 2 * radius
     */
    @Test
    public void testCalculateDiameter() {
        final double testDiameter = 2 * myTestCircle.getRadius();
        assertEquals(myTestCircle.calculateDiameter(), testDiameter, TOLERANCE);
    }

    /**
     * Tests the calculateCircumference method of the Circle class from default values.
     * The formula is circumference = pi * diameter = 2 * pi * radius
     */
    @Test
    public void testCalculateCircumference() {
        final double testCircumference = Math.PI * myTestCircle.calculateDiameter();
        assertEquals(myTestCircle.calculateCircumference(), testCircumference, TOLERANCE);
    }

    /**
     * Tests the calculateArea method of the Circle class from default values.
     * The formula is area = pi * radius^2
     */
    @Test
    public void testCalculateArea() {
        final double testArea = Math.PI * Math.pow(myTestCircle.getRadius(), 2);
        assertEquals(myTestCircle.calculateArea(), testArea, TOLERANCE);
    }

    /**
     * Tests the toString method for Circle class for an expected output from default values.
     * Expected output is the following line:
     * Circle [radius=1.00, center=java.awt.Point[x=0,y=0], color=java.awt.Color[r=0,g=0,b=0]]
     */
    @Test
    public void testToString() {
        final String testString = myTestCircle.toString();
        assertEquals(testString, "Circle [radius=1.00, center=java.awt.Point[x=0,y=0], "
                        + "color=java.awt.Color[r=0,g=0,b=0]]");
    }
}
