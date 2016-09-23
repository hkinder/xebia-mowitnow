package mowitnow.common;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import mowitnow.common.Location;

public class TestLocation {

	@Test
	public void testGetter() {
		// Instantiate the object to test : a location of x=2 and y=3.
		Location location = Location.valueOf(BigInteger.valueOf(2l), BigInteger.valueOf(3l)); 
		// check that x=2
		assertEquals(BigInteger.valueOf(2l), location.getCoordinateX());
		// check that y=3
		assertEquals(BigInteger.valueOf(3l), location.getCoordinateY());
	}

	@Test
	public void testEquals() {
		// Instantiate the object to test : a location of x=2 and y=3.
		Location location = Location.valueOf(BigInteger.valueOf(2l), BigInteger.valueOf(3l)); 
		// check that the object is equal to itself
		assertTrue(location.equals(location));
		// check that the object is equal to another object but same content
		assertTrue(location.equals(Location.valueOf(BigInteger.valueOf(2l), BigInteger.valueOf(3l))));
		// check that the object is equal to another object but same content
		assertTrue(location.equals(new Location(BigInteger.valueOf(2l), BigInteger.valueOf(3l))));
		// check that the object is not equal to null without exception
		assertFalse(location.equals(null));
		// check that the object is not equal a completely different location
		assertFalse(location.equals(new Location(BigInteger.valueOf(5l), BigInteger.valueOf(18l))));
		// check that the object is not equal to a different x location
		assertFalse(location.equals(new Location(BigInteger.valueOf(2l), BigInteger.valueOf(18l))));
		// check that the object is not equal to a different x location
		assertFalse(location.equals(new Location(BigInteger.valueOf(5l), BigInteger.valueOf(3l))));
	}

}
