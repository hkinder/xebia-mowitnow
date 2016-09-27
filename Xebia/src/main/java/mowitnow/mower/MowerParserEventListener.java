package mowitnow.mower;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import mowitnow.area.IArea;
import mowitnow.area.RectangularAreaImpl;
import mowitnow.common.ECardinalPoint;
import mowitnow.common.EOrder;
import mowitnow.common.Location;
import mowitnow.parser.ParserEventListener;
import mowitnow.parser.ParserStep;

public class MowerParserEventListener implements ParserEventListener {

	private Map<ParserStep, StringBuilder> savedParsedParameters = new HashMap<ParserStep, StringBuilder>();

	private IArea playground = null;

	private Location mowerStartLocation = null;

	private ECardinalPoint mowerStartDirection = null;

	private Mower currentMower = null;

	@Override
	public void stepChanged(ParserStep previousStep, ParserStep nextStep, OutputStream outputToWriteTo) throws IOException {
		// in case of step change from ParserStep.AREA_RIGHT to ParserStep.AREA_RIGHT_VALIDATOR : the area data are finished of reading so initialize the related playground
		if (previousStep == ParserStep.AREA_RIGHT && nextStep == ParserStep.AREA_RIGHT_VALIDATOR && playground == null) {
			playground = new RectangularAreaImpl(Location.valueOf(new BigInteger(savedParsedParameters.get(ParserStep.AREA_RIGHT).toString()), new BigInteger(savedParsedParameters.get(ParserStep.AREA_TOP).toString())));
			// savedParsedParameters cleanup
			savedParsedParameters.remove(ParserStep.AREA_RIGHT);
			savedParsedParameters.remove(ParserStep.AREA_TOP);
		} else
		// in case of step change from ParserStep.MOWER_Y_LOCATION to ParserStep.MOWER_Y_LOCATION_VALIDATOR : the mower start location data are finished of reading so initialize the related location
		if (previousStep == ParserStep.MOWER_Y_LOCATION && nextStep == ParserStep.MOWER_Y_LOCATION_VALIDATOR && mowerStartLocation == null) {
			mowerStartLocation = Location.valueOf(new BigInteger(savedParsedParameters.get(ParserStep.MOWER_X_LOCATION).toString()), new BigInteger(savedParsedParameters.get(ParserStep.MOWER_Y_LOCATION).toString()));
			// savedParsedParameters cleanup
			savedParsedParameters.remove(ParserStep.MOWER_X_LOCATION);
			savedParsedParameters.remove(ParserStep.MOWER_Y_LOCATION);
		} else
		// in case of step change from ParserStep.MOWER_DIRECTION to ParserStep.MOWER_DIRECTION_VALIDATOR :
		if (previousStep == ParserStep.MOWER_DIRECTION && nextStep == ParserStep.MOWER_DIRECTION_VALIDATOR) {
			// the start direction data are finished of reading so initialize the related start direction
			if (mowerStartDirection == null) {
				mowerStartDirection = ECardinalPoint.valueOf(savedParsedParameters.get(ParserStep.MOWER_DIRECTION).toString().charAt(0));
				// savedParsedParameters cleanup
				savedParsedParameters.remove(ParserStep.MOWER_DIRECTION);
			}
			// the needed data for the mower are all available so create it
			if (currentMower == null) {
				currentMower = new Mower(playground, mowerStartLocation, mowerStartDirection);
				// savedParsedParameters cleanup
				// keep playground as it is the same for whole data flow
				mowerStartLocation = null;
				mowerStartDirection = null;
			}
		} else
		// in case of step change from ParserStep.MOWER_DIRECTION_VALIDATOR to ParserStep.MOWER_X_LOCATION : The order data is empty
		if (previousStep == ParserStep.MOWER_DIRECTION_VALIDATOR && nextStep == ParserStep.MOWER_X_LOCATION && currentMower != null) {
			outputToWriteTo.write(currentMower.getStatus().getBytes());
			outputToWriteTo.write("\n".getBytes());
			// clean up
			currentMower = null;
		} else
		// in case of step change from ParserStep.ORDERS to ParserStep.ORDERS_VALIDATOR
		if (previousStep == ParserStep.ORDERS && nextStep == ParserStep.ORDERS_VALIDATOR && currentMower != null) {
			outputToWriteTo.write(currentMower.getStatus().getBytes());
			outputToWriteTo.write("\n".getBytes());
			// clean up
			currentMower = null;
		}

	}

	@Override
	public void handleChar(Character nextChar, ParserStep currentStep, OutputStream outputToWriteTo) throws IOException {
		if (currentStep != ParserStep.ORDERS) {
			if (currentStep.hasStepValuableData()) {
				StringBuilder stepStringBuilder = savedParsedParameters.get(currentStep);
				if (stepStringBuilder == null) {
					stepStringBuilder = new StringBuilder();
					this.savedParsedParameters.put(currentStep, stepStringBuilder);
				}
				stepStringBuilder.append(nextChar);
			}
		} else if (currentStep == ParserStep.ORDERS && currentMower != null) {
			currentMower.handleOrder(EOrder.valueOf(nextChar));
		}
	}

}
