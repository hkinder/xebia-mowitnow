package mowitnow.mower;

import java.math.BigInteger;

import mowitnow.area.IArea;
import mowitnow.common.ECardinalPoint;
import mowitnow.common.EOrder;
import mowitnow.common.Location;

/**
 * The mower object.<br />
 * This object has a playground to mow, a current location where the mower is and a current direction of the mower.<br />
 * Once the object is created, two main methods are available : one for receiving new order and one for obtaining a standardized status. <br /> 
 */
public class Mower {

	/**
	 * The playground used to limit the area the mow. 
	 */
	private IArea playground;

	/**
	 * The location giving the current position of the mower inside the playground.
	 */
	private Location location;

	/**
	 * The direction giving the orientation of the mower inside the playground.
	 */
	private ECardinalPoint direction;

	/**
	 * The mower object constructor that also checks the used parameters.
	 * 
	 * @param playground The playground as an area to mow.
	 * @param location The stating location of the mower. It should be inside the playground to be accepted.
	 * @param direction The starting direction of the mower.
	 * 
	 * @throws IllegalArgumentException If any parameter is null, or if the playground does not contain the starting location.
	 */
	public Mower(IArea playground, Location location, ECardinalPoint direction) {
		if (playground == null || location == null || direction == null || !playground.contains(location)) {
			throw new IllegalArgumentException();
		}
		this.playground = playground;
		this.location = location;
		this.direction = direction;
	}

	/**
	 * Getter for a string in a standardized status.<br />
	 * 
	 * The standardized format is the following one : "([0123456789]+|?) ([0123456789]+|?) ([NEWS]|?)". It is expressed here as a regular expression.<br />
	 * Where: <br />
	 * <ul>
	 * <li>The first ([0123456789]+|?) group is the abscissa of the current mower location inside the area. In case of any trouble with the abscissa, a question mark is used as replacement.</li>
	 * <li>The second ([0123456789]+|?) group is the ordinate of the current mower location inside the area. In case of any trouble with the ordinate, a question mark is used as replacement.</li>
	 * <li>The last ([NEWS]|?) group is the direction of the current mower location. In case of any trouble with the direction, a question mark is used as replacement.</li>
	 * </ul>
	 * 
	 * @return The standardized status.
	 */
	public String getStatus() {
		StringBuilder outputBuffer = new StringBuilder();
		if (location != null && location.getCoordinateX() != null) {
			outputBuffer.append(location.getCoordinateX());
		} else {
			outputBuffer.append('?');
		}
		outputBuffer.append(' ');
		if (location != null && location.getCoordinateY() != null) {
			outputBuffer.append(location.getCoordinateY());
		} else {
			outputBuffer.append('?');
		}
		outputBuffer.append(' ');
		if (direction != null) {
			outputBuffer.append(direction.getId());
		} else {
			outputBuffer.append('?');
		}
		return outputBuffer.toString();
	}

	/**
	 * The mower order handler.<br />
	 * 
	 * @param nextOrder the next order to take in account by the mower. Any null value is ignored.
	 * 
	 * @see EOrder EOrder - The mower order enumeration.
	 */
	public void handleOrder(EOrder nextOrder) {
		if (nextOrder == null) {
			return;
		}
		switch (nextOrder) {
		case LEFT:
			turnLeft();
			break;
		case RIGHT:
			turnRight();
			break;
		case MOVE:
			moveForward();
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Internal method for handling the {@link EOrder.LEFT} order.<br />
	 * The direction of the mower should become the previous cardinal point of the current direction.<br />
	 * 
	 * @see ECardinalPoint ECardinalPoint - The cardinal point used for the direction.
	 */
	protected void turnLeft() {
		this.direction = this.direction.previous();
	}

	/**
	 * Internal method for handling the {@link EOrder.RIGHT} order.<br />
	 * The direction of the mower should become the next cardinal point of the current direction.<br />
	 * 
	 * @see ECardinalPoint ECardinalPoint - The cardinal point used for the direction.
	 */
	protected void turnRight() {
		this.direction = this.direction.next();
	}

	/**
	 * Internal method for handling the {@link EOrder.MOVE} order.<br />
	 * The location of the mower should be modified to the next location according to the current direction.<br />
	 * But before moving to the next location, the mower should check that the next location is inside the playground or else the move order must be ignored.<br /> 
	 */
	protected void moveForward() {
		BigInteger xNextLocation = location.getCoordinateX();
		BigInteger yNextLocation = location.getCoordinateY();
		switch (this.direction) {
		case NORTH:
			yNextLocation = yNextLocation.add(BigInteger.ONE);
			break;
		case EAST:
			xNextLocation = xNextLocation.add(BigInteger.ONE);
			break;
		case SOUTH:
			yNextLocation = yNextLocation.subtract(BigInteger.ONE);
			break;
		case WEST:
			xNextLocation = xNextLocation.subtract(BigInteger.ONE);
			break;
		default:
			throw new IllegalArgumentException();
		}
		Location nextLocation = Location.valueOf(xNextLocation, yNextLocation);
		if (this.playground.contains(nextLocation)) {
			this.location = nextLocation;
		}
	}

}
