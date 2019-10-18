package com.iris.bot.session;

import java.util.HashMap;

import com.iris.bot.state.State;

/*
 * Session class helps in conversation flow management by storing state and intent related information in attributes.
 * It also helps in maintaining the information exchange between the user and the server by serving as a temporary storage layer.
 * There is a reset method that re-initialize attributes when called. A session is created with a current timestamp and an empty attributes map
 */
public class Session {
	// 1 hour expiry
	public long expiryTimeinMilliSec = 60 * 60 * 1000l;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();

	/*
	 * long term attributes do not get reset when session expires or when reset method is called
	 */
	private HashMap<String, Object> longTermAttributes = new HashMap<String, Object>();

	// time in milliseconds when the session was created. This is used to check whether the session is valid 
	private long timestamp;

	/*
	 * A default session constructor is called which assigns current time in milliseconds to timestamp variable
	 */
	public Session() {
		this.timestamp = System.currentTimeMillis();
	}

	public void updateCurrentState(State currentState) {
		attributes.put("current_state", currentState);
	}

	public void updateCurrentIntent(String currentIntent) {
		attributes.put("current_intent", currentIntent);
	}

	/*
	 * checks if this is a valid session. Returns boolean
	 */
	public boolean isValid() {
		if (timestamp + expiryTimeinMilliSec < System.currentTimeMillis())
			return false;
		return true;
	}

	/*
	 * returns a session attribute
	 */
	public Object getAttribute(String attribute) {
		return attributes.get(attribute);
	}

	/*
	 * sets a session attribute
	 */
	public void setAttribute(String key, Object object) {
		attributes.put(key, object);
	}

	/**
	 * Removes the specified attribute from the session.
	 */
	public void removeAttribute(String attributeName) {
		attributes.remove(attributeName.toLowerCase());
	}

	/**
	 * Resets the session. Removing all attributes. Long term attributes
	 * are not removed from the session.
	 */
	public void reset() {
		attributes = new HashMap<String, Object>();
	}
}
