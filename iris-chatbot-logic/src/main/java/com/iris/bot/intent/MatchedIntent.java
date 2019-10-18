package com.iris.bot.intent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.iris.bot.slot.Slot;
import com.iris.bot.slot.MatchedSlot;

public class MatchedIntent {
	
	/** The intent that was matched. */
	private Intent intent;

	/** Map of slots that were matched. */
	private HashMap<Slot, MatchedSlot> slotMatches;

	/** The utterance that was matched against. */
	private String utterance;

	/**
	 * Constructor.
	 * 
	 * @param intent
	 *            The intent that was matched.
	 * @param slotMatches
	 *            The slots that were matched.
	 * @param utterance
	 *            The utterance that was matched against.
	 */
	public MatchedIntent(Intent intent, HashMap<Slot, MatchedSlot> slotMatches, String utterance) {

		this.intent = intent;
		this.slotMatches = slotMatches;
		this.utterance = utterance;
	}

	/**
	 * Returns the Intent that was matched.
	 * 
	 * @return The intent that was matched.
	 */
	public Intent getIntent() {
		return intent;
	}

	/**
	 * Returns the slots that were matched.
	 * 
	 * @return Map of the slots that were matched.
	 */
	public Map<Slot, MatchedSlot> getSlotMatches() {
		return Collections.unmodifiableMap(slotMatches);
	}

	/**
	 * Returns the specified slot match if the slot was matched.
	 * 
	 * @param slotName
	 *            The name of the slot to return.
	 * @return The slot match or null if the slot was not matched.
	 */
	public MatchedSlot getSlotMatch(String slotName) {
		for (MatchedSlot match : slotMatches.values()) {
			if (match.getSlot().getName().equalsIgnoreCase(slotName)) {
				return match;
			}
		}
		return null;
	}

	/**
	 * Returns the utterance that was matched against.
	 * 
	 * @return The utterance that was matched against.
	 */
	public String getUtterance() {
		return utterance;
	}

}
