package mowitnow.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import mowitnow.area.IArea;
import mowitnow.area.RectangularAreaImpl;
import mowitnow.common.ECardinalPoint;
import mowitnow.common.EOrder;
import mowitnow.common.Location;
import mowitnow.mower.Mower;

public class InputStreamParser {
	
	private static final InputStreamParser instance = new InputStreamParser();
	
	private InputStreamParser() {
	}
	
	public static final InputStreamParser getInstance() {
		return instance;
	}
	
	public void parse(InputStream inputToParse, OutputStream outputToWriteTo) throws IOException {
		ParseStep currentStep = ParseStep.AREA_TOP;
		Map<ParseStep, StringBuilder> parameters = new HashMap<ParseStep, StringBuilder>();
		IArea playground = null;
		Location mowerStartLocation = null;
		ECardinalPoint mowerStartDirection = null;
		Mower currentMower = null;
		Character nextChar = null;
		while((nextChar = readNextChar(inputToParse)) != null) {
			switch (currentStep) {
			case AREA_TOP:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.AREA_TOP, ParseStep.AREA_TOP_VALIDATOR);
				// nothing else to do either for ParseStep.AREA_TOP or ParseStep.AREA_TOP_VALIDATOR
				break;
			case AREA_TOP_VALIDATOR:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.AREA_TOP_VALIDATOR, ParseStep.AREA_RIGHT);
				// nothing else to do either for ParseStep.AREA_TOP_VALIDATOR or ParseStep.AREA_RIGHT
				break;
			case AREA_RIGHT:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.AREA_RIGHT, ParseStep.AREA_RIGHT_VALIDATOR);
				// in case of step change from ParseStep.AREA_RIGHT to ParseStep.AREA_RIGHT_VALIDATOR : the area data are finished of reading so initialize the related playground
				if (currentStep == ParseStep.AREA_RIGHT_VALIDATOR && playground == null) {
					playground = new RectangularAreaImpl(Location.valueOf(new BigInteger(parameters.get(ParseStep.AREA_RIGHT).toString()), new BigInteger(parameters.get(ParseStep.AREA_TOP).toString())));
					// parameters cleanup
					parameters.remove(ParseStep.AREA_RIGHT);
					parameters.remove(ParseStep.AREA_TOP);
				}
				break;
			case AREA_RIGHT_VALIDATOR:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.AREA_RIGHT_VALIDATOR, ParseStep.MOWER_X_LOCATION);
				// nothing else to do either for ParseStep.AREA_RIGHT or ParseStep.AREA_RIGHT_VALIDATOR
				break;
			case MOWER_X_LOCATION:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.MOWER_X_LOCATION, ParseStep.MOWER_X_LOCATION_VALIDATOR);
				// nothing else to do either for ParseStep.MOWER_X_LOCATION or ParseStep.MOWER_X_LOCATION_VALIDATOR
				break;
			case MOWER_X_LOCATION_VALIDATOR:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.MOWER_X_LOCATION_VALIDATOR, ParseStep.MOWER_Y_LOCATION);
				// nothing else to do either for ParseStep.MOWER_X_LOCATION_VALIDATOR or ParseStep.MOWER_Y_LOCATION
				break;
			case MOWER_Y_LOCATION:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.MOWER_Y_LOCATION, ParseStep.MOWER_Y_LOCATION_VALIDATOR);
				// in case of step change from ParseStep.MOWER_Y_LOCATION to ParseStep.MOWER_Y_LOCATION_VALIDATOR : the start location data are finished of reading so initialize the related start location
				if (currentStep == ParseStep.MOWER_Y_LOCATION_VALIDATOR && mowerStartLocation == null) {
					mowerStartLocation = Location.valueOf(new BigInteger(parameters.get(ParseStep.MOWER_X_LOCATION).toString()), new BigInteger(parameters.get(ParseStep.MOWER_Y_LOCATION).toString()));
					// parameters cleanup
					parameters.remove(ParseStep.MOWER_X_LOCATION);
					parameters.remove(ParseStep.MOWER_Y_LOCATION);
				}
				break;
			case MOWER_Y_LOCATION_VALIDATOR:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.MOWER_Y_LOCATION_VALIDATOR, ParseStep.MOWER_DIRECTION);
				// nothing else to do either for ParseStep.MOWER_Y_LOCATION_VALIDATOR or ParseStep.MOWER_DIRECTION
				break;
			case MOWER_DIRECTION:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.MOWER_DIRECTION, ParseStep.MOWER_DIRECTION_VALIDATOR);
				// in case of step change from ParseStep.MOWER_DIRECTION to ParseStep.MOWER_DIRECTION_VALIDATOR : the start direction data are finished of reading so initialize the related start direction
				if (currentStep == ParseStep.MOWER_DIRECTION_VALIDATOR && mowerStartDirection == null) {
					mowerStartDirection = ECardinalPoint.valueOf(parameters.get(ParseStep.MOWER_DIRECTION).toString().charAt(0));
					// parameters cleanup
					parameters.remove(ParseStep.MOWER_DIRECTION);
				}
				// in case of step change from ParseStep.MOWER_DIRECTION to ParseStep.MOWER_DIRECTION_VALIDATOR : the needed data for the mower are all available so create it
				if (currentStep == ParseStep.MOWER_DIRECTION_VALIDATOR && currentMower == null) {
					currentMower = new Mower(playground, mowerStartLocation, mowerStartDirection);
					// parameters cleanup
					// keep playground as it is the same for whole data flow
					mowerStartLocation = null;
					mowerStartDirection = null;
				}
				break;
			case MOWER_DIRECTION_VALIDATOR:
				// determine the real current step and save needed data
				if (currentStep.checkStepChar(nextChar)) {
					// the current step is unchanged
					updateStepParameter(nextChar, parameters, currentStep);
				} else if (ParseStep.ORDERS.checkStepChar(nextChar)) {
					updateStepParameter(nextChar, parameters, ParseStep.ORDERS);
					currentStep = ParseStep.ORDERS;
				} else if (ParseStep.MOWER_X_LOCATION.checkStepChar(nextChar)) {
					updateStepParameter(nextChar, parameters, ParseStep.MOWER_X_LOCATION);
					currentStep =  ParseStep.MOWER_X_LOCATION;
				} else {
					// ignore invalid data, but keep current step
				}
				// in case of step change from ParseStep.MOWER_DIRECTION_VALIDATOR to ParseStep.ORDERS : the current char is already an order for the mower
				if (currentStep == ParseStep.ORDERS && currentMower != null) {
					currentMower.handleOrder(EOrder.valueOf(nextChar));
				}
				// in case of step change from ParseStep.MOWER_DIRECTION_VALIDATOR to ParseStep.MOWER_X_LOCATION : The order data is empty
				if (currentStep == ParseStep.MOWER_X_LOCATION && currentMower != null) {
					outputToWriteTo.write(currentMower.getStatus().getBytes());
					outputToWriteTo.write("\n".getBytes());
					// clean up
					currentMower = null;
				}
				break;
			case ORDERS:
				// determine the real current step
				if (currentStep.checkStepChar(nextChar)) {
					updateStepParameter(nextChar, parameters, currentStep);
					currentMower.handleOrder(EOrder.valueOf(nextChar));
				} else if (ParseStep.ORDERS_VALIDATOR.checkStepChar(nextChar)) {
					updateStepParameter(nextChar, parameters, ParseStep.ORDERS_VALIDATOR);
					currentStep =  ParseStep.ORDERS_VALIDATOR;
				} else {
					// ignore invalid data, but keep current step
				}
				// in case of step change from ParseStep.ORDERS to ParseStep.ORDERS_VALIDATOR
				if (currentStep == ParseStep.ORDERS_VALIDATOR && currentMower != null) {
					outputToWriteTo.write(currentMower.getStatus().getBytes());
					outputToWriteTo.write("\n".getBytes());
					// clean up
					currentMower = null;
				}
				break;
			case ORDERS_VALIDATOR:
				// determine the real current step and save needed data
				currentStep = defaultStepHandler(nextChar, parameters, ParseStep.ORDERS_VALIDATOR, ParseStep.MOWER_X_LOCATION);
				break;
			default:
				throw new IllegalArgumentException();
			}
		}
		if ((currentStep == ParseStep.ORDERS || currentStep == ParseStep.MOWER_DIRECTION_VALIDATOR) && currentMower != null) {
			outputToWriteTo.write(currentMower.getStatus().getBytes());
			outputToWriteTo.write("\n".getBytes());
			// clean up
			currentMower = null;
		}
	}
	
	private Character readNextChar(InputStream streamToParse) throws IOException {
		int readByte = streamToParse.read();
		if (readByte == -1) {
			return null;
		}
		return Character.valueOf((char) readByte);
	}

	private ParseStep defaultStepHandler(Character nextChar, Map<ParseStep, StringBuilder> parameters, ParseStep currentStep, ParseStep nextStep) {
		ParseStep returnedStep = null;
		if (currentStep.checkStepChar(nextChar)) {
			updateStepParameter(nextChar, parameters, currentStep);
			returnedStep = currentStep;
		} else if (nextStep.checkStepChar(nextChar)) {
			updateStepParameter(nextChar, parameters, nextStep);
			returnedStep = nextStep;
		} else {
			// ignore invalid data, but stay at current step
			returnedStep = currentStep;
		}
		return returnedStep;
	}

	private void updateStepParameter(Character nextChar, Map<ParseStep, StringBuilder> parameters, ParseStep determinedStep) {
		if (determinedStep.isMeaningFull()) {
			StringBuilder stepStringBuilder = parameters.get(determinedStep);
			if (stepStringBuilder == null) {
				stepStringBuilder = new StringBuilder();
				parameters.put(determinedStep, stepStringBuilder);
			}
			stepStringBuilder.append(nextChar);
		}
	}
	
}
