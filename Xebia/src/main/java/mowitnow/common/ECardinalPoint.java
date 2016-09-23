package mowitnow.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The enumeration for the cardinal point.<br />
 * This cardinal point enumeration offers also some helper in order to determine the next or the previous clockwise cardinal point.<br />
 * This cardinal point enumeration can also parse a single standardized char and convert it to a cardinal point enumeration element.<br /> 
 */
public enum ECardinalPoint {
	/**
	 * The NORTH cardinal point.<br />
	 * The NORTH cardinal point identifier is the char 'N'.<br />
	 * The previous clockwise rotation cardinal point is WEST (or 'W') .<br />
	 * The next clockwise rotation cardinal point is EAST (or 'E').<br />
	 */
	NORTH('N', 'W', 'E'),
	/**
	 * The EAST cardinal point.<br />
	 * The EAST cardinal point identifier is the char 'E'.<br />
	 * The previous clockwise rotation cardinal point is NORTH (or 'N').<br />
	 * The next clockwise rotation cardinal point is SOUTH (or 'S').<br />
	 */
	EAST('E', 'N', 'S'),
	/**
	 * The SOUTH cardinal point.<br />
	 * The SOUTH cardinal point identifier is the char 'S'.<br />
	 * The previous clockwise rotation cardinal point is EAST (or 'E').<br />
	 * The next clockwise rotation cardinal point is WEST (or 'W').<br />
	 */
	SOUTH('S', 'E', 'W'),
	/**
	 * The WEST cardinal point.<br />
	 * The WEST cardinal point identifier is the char 'W'.<br />
	 * The previous clockwise rotation cardinal point is SOUTH (or 'S').<br />
	 * The next clockwise rotation cardinal point is NORTH (or 'N').<br />
	 */
	WEST('W', 'S', 'N')
	;

	/**
	 * The cardinal point identifier as a single char. It is provided upon cardinal point definition.
	 */
	private char cardinalPointId;

	/**
	 * The previous cardinal point identifier as a single char. It is provided upon cardinal point definition.
	 */
	private char previousCardinalPointId;

	/**
	 * The next cardinal point identifier as a single char. It is provided upon cardinal point definition.
	 */
	private char nextCardinalPointId;

	/**
	 * The cardinal point constructor to be used upon definition.
	 * 
	 * @param cardinalPointId The cardinal point identifier as a single char.
	 * @param previousCardinalPointId The previous cardinal point identifier as a single char.
	 * @param nextCardinalPointId The next cardinal point identifier as a single char.
	 */
	private ECardinalPoint(char cardinalPointId, char previousCardinalPointId, char nextCardinalPointId) {
		this.cardinalPointId = cardinalPointId;
		this.previousCardinalPointId = previousCardinalPointId;
		this.nextCardinalPointId = nextCardinalPointId;
	}

	/**
	 * Getter for the cardinal point identifier.
	 * 
	 * @return The cardinal point identifier as a single char.
	 */
	public char getId() {
		return cardinalPointId;
	}

	/**
	 * An internal single map in order to index all the cardinal points by their identifier.
	 * Beware of the order of this field inside the class to avoid some trouble. 
	 */
	static final Map<Character, ECardinalPoint> cardinalPointIdIndexMap = new HashMap<Character, ECardinalPoint>();

	/**
	 * The initialization code block for indexing all the cardinal points by their single char identifier.<br />
	 * Beware of the order of this code block inside the class to avoid some trouble. 
	 */
	static {
		for (ECardinalPoint cardinalPointToIndex : ECardinalPoint.values()) {
			cardinalPointIdIndexMap.put(Character.valueOf(cardinalPointToIndex.getId()), cardinalPointToIndex);
		}
	}
	
	/**
	 * A getter for finding the right cardinal point based on some char that should be a cardinal point identifier.
	 * 
	 * @param input a single char that should be a cardinal point identifier.
	 * 
	 * @return The cardinal point that has the inputed char as identifier or null if there is no matching cardinal point identifier.  
	 */
	public static ECardinalPoint valueOf(char input) {
		return cardinalPointIdIndexMap.get(Character.valueOf(input));
	}

	/**
	 * Getter for the next cardinal point (Clockwise rotation).
	 * 
	 * @return the next clockwise cardinal point enumeration based on current cardinal point definition.
	 */
	public ECardinalPoint next() {
		return valueOf(nextCardinalPointId);
	}

	/**
	 * Getter for the previous cardinal point (Clockwise rotation).
	 * 
	 * @return the previous clockwise cardinal point enumeration based on current cardinal point definition.
	 */
	public ECardinalPoint previous() {
		return valueOf(previousCardinalPointId);
	}

	/**
	 * A static method for listing any indexed cardinal point identifiers.<br />
	 * A new set is constructed and returned to avoid outer code to mess with the internal Map of this enumeration.<br />
	 * 
	 * @return the full set of char defined as identifier for cardinal points.
	 */
	public static Set<Character> getPossibleIdentifier() {
		return new HashSet<Character>(cardinalPointIdIndexMap.keySet());
	}

}
