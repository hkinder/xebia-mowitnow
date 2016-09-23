package mowitnow.common;

import static org.junit.Assert.*;

import org.junit.Test;

import mowitnow.common.EOrder;

public class TestEOrder {

	@Test
	public void testValueOf() {
		// A = MOVE
		assertEquals(EOrder.MOVE, EOrder.valueOf('A'));
		// G = LEFT
		assertEquals(EOrder.LEFT, EOrder.valueOf('G'));
		// D = RIGHT
		assertEquals(EOrder.RIGHT, EOrder.valueOf('D'));
		// * = NULL
		assertNull(EOrder.valueOf('*'));
	}

}
