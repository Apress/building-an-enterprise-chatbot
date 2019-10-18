package com.iris.bot.slot;

import java.util.HashMap;

import com.iris.bot.intent.Intent;
import com.iris.bot.session.Session;

/*
 * Custom Slot Matcher class is used to iterate on all the slots for the matched intent and execute match method of each of those slots
 * to return all the matched slots. This class can be further customized and designed to have multiple types of slot matcher implementation
 */
public class CustomSlotMatcher {

	/*
	 * match method takes session, intent and user utterance as an input and returns a map of Slot and MatchedSlot details.
	 * This method can further contain business logic depending upon the implementation. 
	 */
	public HashMap<Slot, MatchedSlot> match(Session session, Intent intent, String utterance) {
		HashMap<Slot, MatchedSlot> matchedSlots = new HashMap<Slot, MatchedSlot>();

		// iterate intent to get all slots defined for this matched intent
		for (Slot slot : intent.getSlots()) {
			/*
			 * use case specific business logic handling. 'askQuoteLastQuestion' is a session variable, and its logic will be explained
			 * when we discuss about state machines and conversation flow management
			 */
			String slotCheck = String.valueOf(session.getAttribute("askQuoteLastQuestion"));
			if (slot.getName().equalsIgnoreCase(slotCheck) || slotCheck.equalsIgnoreCase("null")) {
				// match method defined in each slot is executed and MatchedSlot is returned
				MatchedSlot match = slot.match(utterance);
				if (match != null && match.getMatchedValue() != "null") {
					matchedSlots.put(slot, match);
				}
			}
		}
		return matchedSlots;
	}
}
