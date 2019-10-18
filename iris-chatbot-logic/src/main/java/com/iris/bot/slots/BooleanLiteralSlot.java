package com.iris.bot.slots;

import com.iris.bot.slot.Slot;
import com.iris.bot.slot.MatchedSlot;

public class BooleanLiteralSlot extends Slot {

	private String name;

	public BooleanLiteralSlot(String name) {
		this.name = name;
	}

	/*
	 * match method of BooleanLiteralSlot. We need to recognize if the user meant 'no' or 'yes' in any which way.
	 * One of the simplest ways to implement this will be to verify by string matching of most commonly used words to express  
	 */
	@Override
	public MatchedSlot match(String utterance) {
		if (utterance.toLowerCase().contains("yes") || utterance.toLowerCase().contains("yeah")
				|| utterance.toLowerCase().contains("ya") || utterance.toLowerCase().contains("yup")) {
			return new MatchedSlot(this, utterance, "yes");
		} else if (utterance.toLowerCase().contains("no") || utterance.toLowerCase().contains("na")
				|| utterance.toLowerCase().contains("nopes") || utterance.toLowerCase().contains("noo")
				|| utterance.toLowerCase().contains("nope") || utterance.toLowerCase().contains("dont")
				|| utterance.toLowerCase().contains("don't") || utterance.toLowerCase().contains("do not")) {
			return new MatchedSlot(this, utterance, "no");
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

}
