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
		fireStepChangedEvent(eventListener, null, currentStep, outputToWriteTo);
		Character nextChar = null;
		while((nextChar = readNextChar(inputToParse)) != null) {
			switch (currentStep) {
			case AREA_TOP:
				// Next possible steps are : ParserStep.AREA_TOP or ParserStep.AREA_TOP_VALIDATOR
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.AREA_TOP, outputToWriteTo, ParserStep.AREA_TOP_VALIDATOR);
				break;
			case AREA_TOP_VALIDATOR:
				// Next possible steps are : ParserStep.AREA_TOP_VALIDATOR or ParserStep.AREA_RIGHT
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.AREA_TOP_VALIDATOR, outputToWriteTo, ParserStep.AREA_RIGHT);
				break;
			case AREA_RIGHT:
				// Next possible steps are : ParserStep.AREA_RIGHT or ParserStep.AREA_RIGHT_VALIDATOR
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.AREA_RIGHT, outputToWriteTo, ParserStep.AREA_RIGHT_VALIDATOR);
				break;
			case AREA_RIGHT_VALIDATOR:
				// Next possible steps are : ParserStep.AREA_RIGHT_VALIDATOR or ParserStep.MOWER_X_LOCATION
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.AREA_RIGHT_VALIDATOR, outputToWriteTo, ParserStep.MOWER_X_LOCATION);
				break;
			case MOWER_X_LOCATION:
				// Next possible steps are : ParserStep.MOWER_X_LOCATION or ParserStep.MOWER_X_LOCATION_VALIDATOR
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.MOWER_X_LOCATION, outputToWriteTo, ParserStep.MOWER_X_LOCATION_VALIDATOR);
				break;
			case MOWER_X_LOCATION_VALIDATOR:
				// Next possible steps are : ParserStep.MOWER_X_LOCATION_VALIDATOR or ParserStep.MOWER_Y_LOCATION
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.MOWER_X_LOCATION_VALIDATOR, outputToWriteTo, ParserStep.MOWER_Y_LOCATION);
				break;
			case MOWER_Y_LOCATION:
				// Next possible steps are : ParserStep.MOWER_Y_LOCATION or ParserStep.MOWER_Y_LOCATION_VALIDATOR
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.MOWER_Y_LOCATION, outputToWriteTo, ParserStep.MOWER_Y_LOCATION_VALIDATOR);
				break;
			case MOWER_Y_LOCATION_VALIDATOR:
				// Next possible steps are : ParserStep.MOWER_Y_LOCATION_VALIDATOR or ParserStep.MOWER_DIRECTION
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.MOWER_Y_LOCATION_VALIDATOR, outputToWriteTo, ParserStep.MOWER_DIRECTION);
				break;
			case MOWER_DIRECTION:
				// Next possible steps are : ParserStep.MOWER_DIRECTION or ParserStep.MOWER_DIRECTION_VALIDATOR
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.MOWER_DIRECTION, outputToWriteTo, ParserStep.MOWER_DIRECTION_VALIDATOR);
				break;
			case MOWER_DIRECTION_VALIDATOR:
				// Next possible steps are : ParserStep.MOWER_DIRECTION_VALIDATOR, ParserStep.ORDERS or ParserStep.MOWER_X_LOCATION
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.MOWER_DIRECTION_VALIDATOR, outputToWriteTo, ParserStep.ORDERS, ParserStep.MOWER_X_LOCATION);
				break;
			case ORDERS:
				// Next possible steps are : ParserStep.ORDERS or ParserStep.ORDERS_VALIDATOR
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.ORDERS, outputToWriteTo, ParserStep.ORDERS_VALIDATOR);
				break;
			case ORDERS_VALIDATOR:
				// Next possible steps are : ParserStep.ORDERS_VALIDATOR or ParserStep.MOWER_X_LOCATION
				currentStep = determineAndHandleNextStepParser(eventListener, nextChar, ParserStep.ORDERS_VALIDATOR, outputToWriteTo, ParserStep.MOWER_X_LOCATION);
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
		if (currentStep == ParserStep.MOWER_DIRECTION_VALIDATOR) {
			fireStepChangedEvent(eventListener, currentStep, ParserStep.ORDERS, outputToWriteTo);
			currentStep = ParserStep.ORDERS;
		}
		if (currentStep == ParserStep.ORDERS) {
			fireStepChangedEvent(eventListener, currentStep, ParserStep.ORDERS_VALIDATOR, outputToWriteTo);
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

	private ParserStep determineAndHandleNextStepParser(ParserEventListener eventListener, Character nextChar, ParserStep currentStep, OutputStream outputToWriteTo, ParserStep ... nextSteps) throws IOException {
		ParserStep returnedStep = null;
		if (currentStep.checkStepChar(nextChar)) {
			returnedStep = currentStep;
			fireHandleCharEvent(eventListener, nextChar, returnedStep, outputToWriteTo);
		} else if (nextSteps != null && nextSteps.length > 0) {
			for (ParserStep nextStep : nextSteps) {
				if (nextStep != null && nextStep.checkStepChar(nextChar)) {
					fireStepChangedEvent(eventListener, currentStep, nextStep, outputToWriteTo);
					returnedStep = nextStep;
					fireHandleCharEvent(eventListener, nextChar, returnedStep, outputToWriteTo);
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

	private void fireStepChangedEvent(ParserEventListener eventListener, ParserStep previousStep, ParserStep nextStep, OutputStream outputToWriteTo) throws IOException {
		eventListener.stepChanged(previousStep, nextStep, outputToWriteTo);
	}

	private void fireHandleCharEvent(ParserEventListener eventListener, Character nextChar, ParserStep currentStep, OutputStream outputToWriteTo) throws IOException  {
		eventListener.handleChar(nextChar, currentStep, outputToWriteTo);
	}

}
