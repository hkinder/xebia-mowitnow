package mowitnow.area;

import java.io.Serializable;

import mowitnow.common.Location;

/**
 * An interface for expressing any kind of area inside a cartesian coordination system.<br />
 * Any object implementing the area interface must provide a method for checking that a cartesian coordination location whether belongs or not to that area.<br />
 * Any object implementing this area interface must also follow the usual guideline for the Serializable : provide a serialVersionUID field and implement the hashCode and equals methods.<br />
 */
public interface IArea extends Serializable {

	/**
	 * The mandatory method for checking that a cartesian coordination location whether belongs or not to the current area. 
	 * 
	 * @param location the cartesian coordination location to check
	 * 
	 * @return true if the location belongs to the area, false otherwise.
	 * 
	 * @throws IllegalArgumentException If the location is null.
	 */
	public boolean contains(Location location);
	
}
