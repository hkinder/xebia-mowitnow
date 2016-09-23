package mowitnow.area;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import mowitnow.area.RectangularAreaImpl;
import mowitnow.common.Location;

public class TestRectangularAreaImpl {

	@Test
	public void testContains() {
		// Instantiate the object to test : an area between location (x=0, y=0) and (x=3 and y=2) (means a rectangle of 4x and 3y).
		RectangularAreaImpl rectangularArea = new RectangularAreaImpl(Location.valueOf(BigInteger.valueOf(3l), BigInteger.valueOf(2l))); 
		// x=0 y=0 is inside
		assertTrue(rectangularArea.contains(Location.valueOf(BigInteger.ZERO,  BigInteger.ZERO)));
		// Test border x case
		// x=2 y=1 is inside
		assertTrue(rectangularArea.contains(Location.valueOf(BigInteger.valueOf(2l), BigInteger.ONE)));
		// x=3 y=1 is inside
		assertTrue(rectangularArea.contains(Location.valueOf(BigInteger.valueOf(3l), BigInteger.ONE)));
		// x=4 y=1 is outside
		assertFalse(rectangularArea.contains(Location.valueOf(BigInteger.valueOf(4l), BigInteger.ONE)));
		// Test border y case
		// x=1 y=1 is inside
		assertTrue(rectangularArea.contains(Location.valueOf(BigInteger.ONE, BigInteger.ONE)));
		// x=1 y=2 is inside
		assertTrue(rectangularArea.contains(Location.valueOf(BigInteger.ONE, BigInteger.valueOf(2l))));
		// x=1 y=3 is outside
		assertFalse(rectangularArea.contains(Location.valueOf(BigInteger.ONE, BigInteger.valueOf(3l))));
	}

}
