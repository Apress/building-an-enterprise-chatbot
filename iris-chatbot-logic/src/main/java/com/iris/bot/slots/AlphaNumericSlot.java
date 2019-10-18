package com.iris.bot.slots;

import java.util.ArrayList;

import com.iris.bot.slot.Slot;
import com.iris.bot.slot.MatchedSlot;

import edu.emory.mathcs.backport.java.util.Arrays;

public class AlphaNumericSlot extends Slot {
	private String name;

	public AlphaNumericSlot(String name) {
		this.name = name;
	}

	@Override
	public MatchedSlot match(String utterance) {
		/*
		 * User utterance is split into utterance tokens. We need to see if there is any alphanumeric word in utterance
		 * This implementation is useful for scenarios mentioned below:
		 * my claim id is gi123 can you tell the claim status
		 * claim status of abc123
		 */
		ArrayList<String> utteranceTokens = new ArrayList<String>(Arrays.asList(utterance.split("\\s+")));
		String claimId = null;
		for (String token : utteranceTokens) {
			if (!token.matches("[a-zA-Z]+")) {
				token = token.replace(".", "");
				token = token.trim();
				claimId = token;
				return new MatchedSlot(this, claimId, claimId);
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

}
