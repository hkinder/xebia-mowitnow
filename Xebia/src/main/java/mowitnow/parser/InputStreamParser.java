package mowitnow.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mowitnow.mower.MowerParserEventListener;

public class InputStreamParser extends MowerParserEventListener {
	
	private static final InputStreamParser instance = new InputStreamParser();
	
	private InputStreamParser() {
	}
	
	public static final InputStreamParser getInstance() {
		return instance;
	}
	
	public void parse(InputStream inputToParse, ParserEventListener eventListener, OutputStream outputToWriteTo) throws IOException {
		ParserStep currentStep = ParserStep.AREA_TOP;
		fireStepChangedEventIfNeeded(eventListener, null, currentStep, outputToWriteTo);
		ParserStep nextStep = null;
		Character readChar = null;
		while((readChar = readNextChar(inputToParse)) != null) {
			// determine the next parser step
			switch (currentStep) {
			case AREA_TOP:
				// Next possible steps are : ParserStep.AREA_TOP or ParserStep.AREA_TOP_VALIDATOR
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.AREA_TOP, outputToWriteTo, ParserStep.AREA_TOP_VALIDATOR);
				break;
			case AREA_TOP_VALIDATOR:
				// Next possible steps are : ParserStep.AREA_TOP_VALIDATOR or ParserStep.AREA_RIGHT
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.AREA_TOP_VALIDATOR, outputToWriteTo, ParserStep.AREA_RIGHT);
				break;
			case AREA_RIGHT:
				// Next possible steps are : ParserStep.AREA_RIGHT or ParserStep.AREA_RIGHT_VALIDATOR
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.AREA_RIGHT, outputToWriteTo, ParserStep.AREA_RIGHT_VALIDATOR);
				break;
			case AREA_RIGHT_VALIDATOR:
				// Next possible steps are : ParserStep.AREA_RIGHT_VALIDATOR or ParserStep.MOWER_X_LOCATION
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.AREA_RIGHT_VALIDATOR, outputToWriteTo, ParserStep.MOWER_X_LOCATION);
				break;
			case MOWER_X_LOCATION:
				// Next possible steps are : ParserStep.MOWER_X_LOCATION or ParserStep.MOWER_X_LOCATION_VALIDATOR
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.MOWER_X_LOCATION, outputToWriteTo, ParserStep.MOWER_X_LOCATION_VALIDATOR);
				break;
			case MOWER_X_LOCATION_VALIDATOR:
				// Next possible steps are : ParserStep.MOWER_X_LOCATION_VALIDATOR or ParserStep.MOWER_Y_LOCATION
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.MOWER_X_LOCATION_VALIDATOR, outputToWriteTo, ParserStep.MOWER_Y_LOCATION);
				break;
			case MOWER_Y_LOCATION:
				// Next possible steps are : ParserStep.MOWER_Y_LOCATION or ParserStep.MOWER_Y_LOCATION_VALIDATOR
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.MOWER_Y_LOCATION, outputToWriteTo, ParserStep.MOWER_Y_LOCATION_VALIDATOR);
				break;
			case MOWER_Y_LOCATION_VALIDATOR:
				// Next possible steps are : ParserStep.MOWER_Y_LOCATION_VALIDATOR or ParserStep.MOWER_DIRECTION
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.MOWER_Y_LOCATION_VALIDATOR, outputToWriteTo, ParserStep.MOWER_DIRECTION);
				break;
			case MOWER_DIRECTION:
				// Next possible steps are : ParserStep.MOWER_DIRECTION or ParserStep.MOWER_DIRECTION_VALIDATOR
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.MOWER_DIRECTION, outputToWriteTo, ParserStep.MOWER_DIRECTION_VALIDATOR);
				break;
			case MOWER_DIRECTION_VALIDATOR:
				// Next possible steps are : ParserStep.MOWER_DIRECTION_VALIDATOR, ParserStep.ORDERS or ParserStep.MOWER_X_LOCATION
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.MOWER_DIRECTION_VALIDATOR, outputToWriteTo, ParserStep.ORDERS, ParserStep.MOWER_X_LOCATION);
				// in case of switch to ParserStep.MOWER_X_LOCATION, then simulate the ParserStep.ORDERS and the ParserStep.ORDERS_VALIDATOR step changes
				if (nextStep == ParserStep.MOWER_X_LOCATION) {
					fireStepChangedEventIfNeeded(eventListener, currentStep, ParserStep.ORDERS, outputToWriteTo);
					currentStep = ParserStep.ORDERS;
					fireStepChangedEventIfNeeded(eventListener, currentStep, ParserStep.ORDERS_VALIDATOR, outputToWriteTo);
					currentStep = ParserStep.ORDERS_VALIDATOR;
				}
				break;
			case ORDERS:
				// Next possible steps are : ParserStep.ORDERS or ParserStep.ORDERS_VALIDATOR
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.ORDERS, outputToWriteTo, ParserStep.ORDERS_VALIDATOR);
				break;
			case ORDERS_VALIDATOR:
				// Next possible steps are : ParserStep.ORDERS_VALIDATOR or ParserStep.MOWER_X_LOCATION
				nextStep = determineNextStepParser(eventListener, readChar, ParserStep.ORDERS_VALIDATOR, outputToWriteTo, ParserStep.MOWER_X_LOCATION);
				break;
			default:
				throw new IllegalArgumentException();
			}
			// based on the current step and the next step a step change event may be fired
			fireStepChangedEventIfNeeded(eventListener, currentStep, nextStep, outputToWriteTo);
			// based on the new step, the current char may be parsed
			fireHandleCharEventIfNeeded(eventListener, readChar, nextStep, outputToWriteTo);
			// clean up to prepare next loop
			currentStep = nextStep;
			nextStep = null;
		}
		if (currentStep == ParserStep.MOWER_DIRECTION_VALIDATOR) {
			fireStepChangedEventIfNeeded(eventListener, currentStep, ParserStep.ORDERS, outputToWriteTo);
			currentStep = ParserStep.ORDERS;
		}
		if (currentStep == ParserStep.ORDERS) {
			fireStepChangedEventIfNeeded(eventListener, currentStep, ParserStep.ORDERS_VALIDATOR, outputToWriteTo);
			currentStep = ParserStep.ORDERS_VALIDATOR;
		}
	}
	
	private Character readNextChar(InputStream streamToParse) throws IOException {
		int readByte = streamToParse.read();
		if (readByte == -1) {
			return null;
		}
		return Character.valueOf((char) readByte);
	}

	private ParserStep determineNextStepParser(ParserEventListener eventListener, Character nextChar, ParserStep currentStep, OutputStream outputToWriteTo, ParserStep ... nextSteps) throws IOException {
		ParserStep returnedStep = null;
		if (currentStep != null && currentStep.checkStepChar(nextChar)) {
			returnedStep = currentStep;
		} else if (nextSteps != null && nextSteps.length > 0) {
			for (ParserStep nextStep : nextSteps) {
				if (nextStep != null && nextStep.checkStepChar(nextChar)) {
					returnedStep = nextStep;
					break;
				}
			}
		}
		if (returnedStep == null) {
			// ignore invalid data, but stay at current step
			returnedStep = currentStep;
		}
		return returnedStep;
	}
	
	private void fireStepChangedEventIfNeeded(ParserEventListener eventListener, ParserStep previousStep, ParserStep nextStep, OutputStream outputToWriteTo) throws IOException {
		if (previousStep != nextStep) {
			eventListener.stepChanged(previousStep, nextStep, outputToWriteTo);
		}
	}

	private void fireHandleCharEventIfNeeded(ParserEventListener eventListener, Character currentChar, ParserStep currentStep, OutputStream outputToWriteTo) throws IOException  {
		if (currentStep != null  && currentStep.hasStepValuableData() && currentStep.checkStepChar(currentChar)) {
			eventListener.handleChar(currentChar, currentStep, outputToWriteTo);
		} else {
			// - ignore invalid data, but stay at current step
			// - skip invaluable data
		}
	}

}
