package mowitnow.mower;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import mowitnow.area.RectangularAreaImpl;
import mowitnow.common.ECardinalPoint;
import mowitnow.common.EOrder;
import mowitnow.common.Location;
import mowitnow.mower.Mower;

public class TestMower {

	@Test
	public void testGetStatus() {
		// Instantiate the dependencies
		RectangularAreaImpl playground = new RectangularAreaImpl(Location.valueOf(BigInteger.valueOf(3l), BigInteger.valueOf(2l)));
		Location startLocation = Location.valueOf(BigInteger.ONE, BigInteger.ZERO);
		ECardinalPoint startDirection = ECardinalPoint.NORTH;
		// instantiate the test object
		Mower mower = new Mower(playground, startLocation, startDirection);
		// test the get
		assertEquals("1 0 N", mower.getStatus());
	}

	@Test
	public void testGoingNorthBorder() {
		// Instantiate the dependencies
		RectangularAreaImpl playground = new RectangularAreaImpl(Location.valueOf(BigInteger.valueOf(2l), BigInteger.valueOf(2l)));
		Location startLocation = Location.valueOf(BigInteger.ONE, BigInteger.ONE);
		ECardinalPoint startDirection = ECardinalPoint.NORTH;
		// instantiate the test object
		Mower mower = new Mower(playground, startLocation, startDirection);
		// proceed with the test cases
		assertEquals("1 1 N", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("1 2 N", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("1 2 N", mower.getStatus());
	}

	@Test
	public void testGoingEastBorder() {
		// Instantiate the dependencies
		RectangularAreaImpl playground = new RectangularAreaImpl(Location.valueOf(BigInteger.valueOf(2l), BigInteger.valueOf(2l)));
		Location startLocation = Location.valueOf(BigInteger.ONE, BigInteger.ONE);
		ECardinalPoint startDirection = ECardinalPoint.EAST;
		// instantiate the test object
		Mower mower = new Mower(playground, startLocation, startDirection);
		// proceed with the test cases
		assertEquals("1 1 E", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("2 1 E", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("2 1 E", mower.getStatus());
	}

	@Test
	public void testGoingSouthBorder() {
		// Instantiate the dependencies
		RectangularAreaImpl playground = new RectangularAreaImpl(Location.valueOf(BigInteger.valueOf(2l), BigInteger.valueOf(2l)));
		Location startLocation = Location.valueOf(BigInteger.ONE, BigInteger.ONE);
		ECardinalPoint startDirection = ECardinalPoint.SOUTH;
		// instantiate the test object
		Mower mower = new Mower(playground, startLocation, startDirection);
		// proceed with the test cases
		assertEquals("1 1 S", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("1 0 S", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("1 0 S", mower.getStatus());
	}

	@Test
	public void testGoingWestBorder() {
		// Instantiate the dependencies
		RectangularAreaImpl playground = new RectangularAreaImpl(Location.valueOf(BigInteger.valueOf(2l), BigInteger.valueOf(2l)));
		Location startLocation = Location.valueOf(BigInteger.ONE, BigInteger.ONE);
		ECardinalPoint startDirection = ECardinalPoint.WEST;
		// instantiate the test object
		Mower mower = new Mower(playground, startLocation, startDirection);
		assertEquals("1 1 W", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("0 1 W", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		assertEquals("0 1 W", mower.getStatus());
	}

	@Test
	public void testGoingClockwise() {
		// Instantiate the dependencies
		RectangularAreaImpl playground = new RectangularAreaImpl(Location.valueOf(BigInteger.ONE, BigInteger.ONE));
		Location startLocation = Location.valueOf(BigInteger.ZERO, BigInteger.ZERO);
		ECardinalPoint startDirection = ECardinalPoint.NORTH;
		// instantiate the test object
		Mower mower = new Mower(playground, startLocation, startDirection);
		assertEquals("0 0 N", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.RIGHT);
		assertEquals("0 1 E", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.RIGHT);
		assertEquals("1 1 S", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.RIGHT);
		assertEquals("1 0 W", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.RIGHT);
		assertEquals("0 0 N", mower.getStatus());
	}

	@Test
	public void testGoingAnticlockwise() {
		// Instantiate the dependencies
		RectangularAreaImpl playground = new RectangularAreaImpl(Location.valueOf(BigInteger.ONE, BigInteger.ONE));
		Location startLocation = Location.valueOf(BigInteger.ONE, BigInteger.ZERO);
		ECardinalPoint startDirection = ECardinalPoint.NORTH;
		// instantiate the test object
		Mower mower = new Mower(playground, startLocation, startDirection);
		assertEquals("1 0 N", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.LEFT);
		assertEquals("1 1 W", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.LEFT);
		assertEquals("0 1 S", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.LEFT);
		assertEquals("0 0 E", mower.getStatus());
		mower.handleOrder(EOrder.MOVE);
		mower.handleOrder(EOrder.LEFT);
		assertEquals("1 0 N", mower.getStatus());
	}


}
