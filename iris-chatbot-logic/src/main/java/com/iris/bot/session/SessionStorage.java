package com.iris.bot.session;

import java.util.HashMap;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.slot.MatchedSlot;

/*
 * Helper class which holds all user sessions and also provides method to get or create session.
 */
public class SessionStorage {

	// a map of user id and  sessions
	HashMap<String, Session> userSession = new HashMap<String, Session>();

	/*
	 * this method first checks if there is a session for this user (user ID). It also checks if the session is valid
	 *If there is no session for that user or if the session has expired, it will create a new session. Else it will return the active session
	 */
	public Session getOrCreateSession(String userId) {

		if (!userSession.containsKey(userId) || !userSession.get(userId).isValid()) {
			Session session = new Session();
			userSession.put(userId, session);
		}
		return userSession.get(userId);
	}


	/**
	 * Gets a String value from the session (if it exists) or the slot (if a
	 * match exists).
	 * 
	 * @param match
	 *            The intent match.
	 * @param session
	 *            The session.
	 * @param slotName
	 *            The name of the slot.
	 * @param defaultValue
	 *            The default value if not value found in the session or slot.
	 * @return The string value.
	 */
	public static String getStringFromSlotOrSession(MatchedIntent match, Session session, String slotName,
			String defaultValue) {
		String sessionValue = (String) session.getAttribute(slotName);
		if (sessionValue != null) {
			return sessionValue;
		}

		return getStringSlot(match, slotName, defaultValue);
	}

	/**
	 * Gets a String based slot value from an intent match.
	 * 
	 * @param match
	 *            The intent match to get the slot value from.
	 * @param slotName
	 *            The name of the slot.
	 * @param defaultValue
	 *            The default value to use if no slot found.
	 * @return The string value.
	 */
	public static String getStringSlot(MatchedIntent match, String slotName, String defaultValue) {
		if (match.getSlotMatch(slotName) != null && match.getSlotMatch(slotName).getMatchedValue() != null) {
			return (String) match.getSlotMatch(slotName).getMatchedValue();
		} else {
			return defaultValue;
		}
	}

	/**
	 * Saves all the matched slots for an IntentMatch into the session.
	 * 
	 * @param match
	 *            The intent match.
	 * @param session
	 *            The session.
	 */
	public static void saveSlotsToSession(MatchedIntent match, Session session) {
		for (MatchedSlot matchedSlot : match.getSlotMatches().values()) {
			session.setAttribute(matchedSlot.getSlot().getName(), matchedSlot.getMatchedValue());
		}
	}
}
