package mowitnow.parser;

import java.util.HashSet;
import java.util.Set;

import mowitnow.common.ECardinalPoint;
import mowitnow.common.EOrder;

public enum ParserStep {
	AREA_TOP("0123456789", true),
	AREA_TOP_VALIDATOR(" \t", false),
	AREA_RIGHT("0123456789", true),
	AREA_RIGHT_VALIDATOR(" \t\r\n", false),
	MOWER_X_LOCATION("0123456789", true),
	MOWER_X_LOCATION_VALIDATOR(" \t", false),
	MOWER_Y_LOCATION("0123456789", true),
	MOWER_Y_LOCATION_VALIDATOR(" \t", false),
	MOWER_DIRECTION(ECardinalPoint.getPossibleIdentifier(), true),
	MOWER_DIRECTION_VALIDATOR(" \t\r\n", false),
	ORDERS(EOrder.getPossibleIdentifier(), true),
	ORDERS_VALIDATOR(" \t\r\n", false)
	;
	
	private Set<Character> stepValidCharSet;
	
	private boolean stepHasValuableData;

	private ParserStep(Set<Character> stepValidCharSet, boolean stepHasValuableData) {
		this.stepHasValuableData = stepHasValuableData;
		this.stepValidCharSet = stepValidCharSet;
	}

	private ParserStep(String validChars, boolean stepHasValuableData) {
		Set<Character> stepValidCharSet = new HashSet<Character>();
		for (char currentChar : validChars.toCharArray()) {
			stepValidCharSet.add(Character.valueOf(currentChar));
		}
		this.stepHasValuableData = stepHasValuableData;
		this.stepValidCharSet = stepValidCharSet;
	}

	public boolean checkStepChar(Character inputToCheck) {
		return this.stepValidCharSet.contains(inputToCheck);
	}
	
	public boolean hasStepValuableData() {
		return this.stepHasValuableData;
	}
}
