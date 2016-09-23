package mowitnow.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The enumeration for the order.<br />
 * This order enumeration can parse a single standardized char and convert it to an order enumeration element.<br /> 
 */
public enum EOrder {
	/**
	 * The MOVE order.<br />
	 * The MOVE order identifier is the char 'A'.<br />
	 */
	MOVE('A'),
	/**
	 * The LEFT order.<br />
	 * The LEFT order identifier is the char 'G'.<br />
	 */
	LEFT('G'),
	/**
	 * The RIGHT order.<br />
	 * The RIGHT order identifier is the char 'D'.<br />
	 */
	RIGHT('D')
	;

	/**
	 * The order identifier as a single char. It is provided upon order definition.
	 */
	private char orderId;

	/**
	 * The order constructor to be used upon definition.
	 * 
	 * @param orderId The order identifier as a single char.
	 */
	private EOrder(char orderId) {
		this.orderId = orderId;
	}


	/**
	 * Getter for the order identifier.
	 * 
	 * @return The order identifier as a single char.
	 */
	public char getOrderId() {
		return orderId;
	}

	/**
	 * An internal single map in order to index all the orders by their identifier.
	 * Beware of the order of this field inside the class to avoid some trouble. 
	 */
	static final Map<Character, EOrder> orderIdIndexMap = new HashMap<Character, EOrder>();

	/**
	 * The initialization code block for indexing all the orders by their single char identifier.
	 * Beware of the order of this code block inside the class to avoid some trouble. 
	 */
	static {
		for (EOrder orderToIndex : EOrder.values()) {
			orderIdIndexMap.put(Character.valueOf(orderToIndex.getOrderId()), orderToIndex);
		}
	}

	/**
	 * A getter for finding the right order based on some char that should be an order identifier.
	 * 
	 * @param input a single char that should be an order identifier.
	 * 
	 * @return The order that has the inputed char as identifier or null if there is no matching order identifier.  
	 */
	public static EOrder valueOf(char input) {
		return orderIdIndexMap.get(Character.valueOf(input));
	}
	
	/**
	 * A static method for listing any indexed order identifier.<br />
	 * A new set is constructed and returned to avoid outer code to mess with the internal Map of this enumeration.<br />
	 * 
	 * @return the full set of char defined as identifier for orders.
	 */
	public static Set<Character> getPossibleIdentifier() {
		return new HashSet<Character>(orderIdIndexMap.keySet());
	}
	
}
