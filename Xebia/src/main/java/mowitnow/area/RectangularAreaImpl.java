package mowitnow.area;

import java.math.BigInteger;

import mowitnow.common.Location;

/**
 * A rectangular area in a cartesian coordination system.<br />
 * Only two location are needed for expressing a rectangle in a cartesian coordination system: two opposite corners.<br />
 * Once the recatngular area object is created it cannot be modified. The object is immutable.<br />
 */
public class RectangularAreaImpl implements IArea {

	/**
	 * The serialVersionUID useful in case of serialization of objects.
	 */
	private static final long serialVersionUID = -7072411592251189647L;

	/**
	 * The first corner used to express the rectangle: the top right corner cartesian coordination location of the rectangle.
	 */
	private final Location topRightCorner;

	/**
	 * The second corner used to express the rectangle: the bottom left corner cartesian coordination location of the rectangle.
	 */
	private final Location bottomLeftCorner;

	/**
	 * The rectangle area constructor with two location.<br />
	 * 
	 * @param topRightCorner the top right corner cartesian coordination location.
	 * @param bottomLeftCorner the bottom left corner cartesian coordination location.
	 * 
	 * @throws IllegalArgumentException If any parameter is null or if each corner does not describe its assumed position.
	 */
	public RectangularAreaImpl(Location topRightCorner, Location bottomLeftCorner) {
		if (topRightCorner == null || bottomLeftCorner == null
				|| topRightCorner.getCoordinateY().compareTo(bottomLeftCorner.getCoordinateY()) < 0
				|| topRightCorner.getCoordinateX().compareTo(bottomLeftCorner.getCoordinateX()) < 0) {
			throw new IllegalArgumentException();
		}
		this.topRightCorner = topRightCorner;
		this.bottomLeftCorner = bottomLeftCorner;
	}

	/**
	 * The simplified rectangle area constructor with only the top right corner.<br />
	 * In that case the bottom left corner cartesian coordination location is assumed to be (0,0).
	 * 
	 * @param topRightCorner the top right corner cartesian coordination location.
	 * 
	 * @throws IllegalArgumentException If any parameter is null or if each corner does not describe its assumed position.
	 */
	public RectangularAreaImpl(Location topRightCorner) {
		this(topRightCorner, Location.valueOf(BigInteger.ZERO, BigInteger.ZERO));
	}

	/**
	 * Getter for the top right corner cartesian coordination location.
	 * 
	 * @return The top right corner cartesian coordination location.
	 */
	public Location getTopRightCorner() {
		return topRightCorner;
	}

	/**
	 * Getter for the bottom left corner cartesian coordination location.
	 * 
	 * @return The bottom left corner cartesian coordination location.
	 */
	public Location getBottomLeftCorner() {
		return bottomLeftCorner;
	}

	/**
	 * The method for checking that a cartesian coordination location whether belongs or not to the current cartesian coordination rectangular area. 
	 * 
	 * @param location the cartesian coordination location to check
	 * 
	 * @return true if the location belongs to the area, false otherwise.
	 * 
	 * @throws IllegalArgumentException If the location is null or invalid.
	 */
	@Override
	public boolean contains(Location locationToTest) {
		if (locationToTest == null || locationToTest.getCoordinateX() == null || locationToTest.getCoordinateY() == null) {
			throw new IllegalArgumentException();
		}
		if (topRightCorner.getCoordinateY().compareTo(locationToTest.getCoordinateY()) < 0 || bottomLeftCorner.getCoordinateY().compareTo(locationToTest.getCoordinateY()) > 0) {
			return false;
		}
		if (topRightCorner.getCoordinateX().compareTo(locationToTest.getCoordinateX()) < 0 || bottomLeftCorner.getCoordinateX().compareTo(locationToTest.getCoordinateX()) > 0) {
			return false;
		}
		return true;
	}

	/**
	 * The usual hash code calculation.<br />
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((topRightCorner == null) ? 0 : topRightCorner.hashCode());
		result = prime * result + ((bottomLeftCorner == null) ? 0 : bottomLeftCorner.hashCode());
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
		RectangularAreaImpl other = (RectangularAreaImpl) obj;
		if (topRightCorner == null) {
			if (other.topRightCorner != null) {
				return false;
			}
		} else if (!topRightCorner.equals(other.topRightCorner)) {
			return false;
		}
		if (bottomLeftCorner == null) {
			if (other.bottomLeftCorner != null) {
				return false;
			}
		} else if (!bottomLeftCorner.equals(other.bottomLeftCorner)) {
			return false;
		}
		return true;
	}

}
