package mowitnow.common;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * A location object for expressing a position inside a cartesian coordination system.<br />
 * The location has an abscissa (X) and an ordinate (Y) expressed as {@link BigInteger} Object in order to avoid some limitation of any java basic type.<br />
 * Once the location object is created it cannot be modified. The object is immutable.<br />
 * The object provides only methods for comparing and consulting the location. <br />
 */
public class Location implements Serializable {

	/**
	 * The serialVersionUID useful in case of serialization of objects.
	 */
	private static final long serialVersionUID = -7072162493545727827L;

	/**
	 * The abscissa of the location.<br />
	 * Once the value is set, it cannot be changed for the current location.<br />
	 */
	private BigInteger coordinateX;

	/**
	 * The ordinate of the location.<br />
	 * Once the value is set, it cannot be changed for the current location.<br />
	 */
	private BigInteger coordinateY;

	/**
	 * The location object constructor that also checks the used parameters.
	 * 
	 * @param xCoordinate The abscissa of the new location.
	 * @param yCoordinate The ordinate of the new location.
	 * 
	 * @throws IllegalArgumentException If any parameter is null.
	 */
	public Location(BigInteger xCoordinate, BigInteger yCoordinate) {
		if (xCoordinate == null || yCoordinate == null) {
			throw new IllegalArgumentException();
		}
		this.coordinateX = xCoordinate;
		this.coordinateY = yCoordinate;
	}

	/**
	 * The getter for the abscissa of the location.
	 * 
	 * @return The abscissa of the location.
	 */
	public BigInteger getCoordinateX() {
		return coordinateX;
	}

	/**
	 * The getter for the ordinate of the location.
	 * 
	 * @return The ordinate of the location.
	 */
	public BigInteger getCoordinateY() {
		return coordinateY;
	}

	/**
	 * The usual hash code calculation.<br />
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinateX == null) ? 0 : coordinateX.hashCode());
		result = prime * result + ((coordinateY == null) ? 0 : coordinateY.hashCode());
		return result;
	}

	/**
	 * The usual equals method.<br />
	 * Important to note : any comparison to a inherited class will result to a false value.<br /> 
	 * 
	 * @param obj the other object to check equality against.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Location other = (Location) obj;
		if (coordinateX == null) {
			if (other.coordinateX != null) {
				return false;
			}
		} else if (!coordinateX.equals(other.coordinateX)) {
			return false;
		}
		if (coordinateY == null) {
			if (other.coordinateY != null) {
				return false;
			}
		} else if (!coordinateY.equals(other.coordinateY)) {
			return false;
		}
		return true;
	}

	/**
	 * A instantiation helper where some caching could be operate.
	 * 
	 * @param xCoordinate The abscissa of the new location.
	 * @param yCoordinate The ordinate of the new location.

	 * @return the new or cached location based on the abscissa and ordinate.
	 * 
	 * @throws IllegalArgumentException If any parameter is null (just as the object constructor).
	 */
	public static Location valueOf(BigInteger xCoordinate, BigInteger yCoordinate) {
		return new Location(xCoordinate, yCoordinate);
	}
}
