package mowitnow.common;

import static org.junit.Assert.*;

import org.junit.Test;

import mowitnow.common.ECardinalPoint;

public class TestECardinalPoint {

	@Test
	public void testValueOf() {
		// N = NORTH
		assertEquals(ECardinalPoint.NORTH, ECardinalPoint.valueOf('N'));
		// E = EAST
		assertEquals(ECardinalPoint.EAST, ECardinalPoint.valueOf('E'));
		// S = SOUTH
		assertEquals(ECardinalPoint.SOUTH, ECardinalPoint.valueOf('S'));
		// W = WEST
		assertEquals(ECardinalPoint.WEST, ECardinalPoint.valueOf('W'));
		// * = NULL
		assertNull(ECardinalPoint.valueOf('*'));
	}

	@Test
	public void testTurnLeft() {
		// NORTH + turn left = WEST
		assertEquals(ECardinalPoint.WEST, ECardinalPoint.NORTH.previous());
		// WEST + turn left = SOUTH
		assertEquals(ECardinalPoint.SOUTH, ECardinalPoint.WEST.previous());
		// SOUTH + turn left = EAST
		assertEquals(ECardinalPoint.EAST, ECardinalPoint.SOUTH.previous());
		// EAST + turn left = NORTH
		assertEquals(ECardinalPoint.NORTH, ECardinalPoint.EAST.previous());
	}

	@Test
	public void testNextCardinal() {
		// NORTH + turn right = EAST
		assertEquals(ECardinalPoint.EAST, ECardinalPoint.NORTH.next());
		// EAST + turn right = SOUTH
		assertEquals(ECardinalPoint.SOUTH, ECardinalPoint.EAST.next());
		// SOUTH + turn right = WEST
		assertEquals(ECardinalPoint.WEST, ECardinalPoint.SOUTH.next());
		// WEST + turn right = NORTH
		assertEquals(ECardinalPoint.NORTH, ECardinalPoint.WEST.next());
	}

}
