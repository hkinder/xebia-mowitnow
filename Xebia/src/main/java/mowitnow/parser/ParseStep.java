package mowitnow.parser;

import java.util.HashSet;
import java.util.Set;

import mowitnow.common.ECardinalPoint;
import mowitnow.common.EOrder;

public enum ParseStep {
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
	ORDERS(EOrder.getPossibleIdentifier(), false),
	ORDERS_VALIDATOR(" \t\r\n", false)
	;
	
	private Set<Character> stepValidCharSet;
	
	private boolean meaningFull;

	private ParseStep(Set<Character> stepValidCharSet, boolean meaningFull) {
		this.meaningFull = meaningFull;
		this.stepValidCharSet = stepValidCharSet;
	}

	private ParseStep(String validChars, boolean meaningFull) {
		Set<Character> stepValidCharSet = new HashSet<Character>();
		for (char currentChar : validChars.toCharArray()) {
			stepValidCharSet.add(Character.valueOf(currentChar));
		}
		this.meaningFull = meaningFull;
		this.stepValidCharSet = stepValidCharSet;
	}

	public boolean checkStepChar(Character inputToCheck) {
		return this.stepValidCharSet.contains(inputToCheck);
	}
	
	public boolean isMeaningFull() {
		return this.meaningFull;
	}
}
