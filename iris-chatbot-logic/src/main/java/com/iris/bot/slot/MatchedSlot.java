package com.iris.bot.slot;

/*
 * MatchedSlot contains slot related information such as the slot that was matched, the original value that was was used to match on and 
 * the value that was matched
 */
public class MatchedSlot {
	/** The slot that was matched. */
	private Slot slot;

	/** The original value that was used to matched on. */
	private String originalValue;

	/** The value that was matched. */
	private Object matchedValue;

	public MatchedSlot(Slot slot, String originalValue, Object value) {
		this.slot = slot;
		this.originalValue = originalValue;
		this.setMatchedValue(value);
	}

	/**
	 * returns the slot that was matched
	 */
	public Slot getSlot() {
		return slot;
	}

	/** returns the original valuw
	 */
	public String getOriginalValue() {
		return originalValue;
	}

	/*
	 * returns the matched value
	 */
	public Object getMatchedValue() {
		return matchedValue;
	}

	/*
	 * sets the matched value in constructor. Method is declared as private as the value is only set in constructor 
	 */
	private void setMatchedValue(Object matchedValue) {
		this.matchedValue = matchedValue;
	}
}
