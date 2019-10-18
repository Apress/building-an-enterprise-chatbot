package com.iris.bot.slots;

import com.iris.bot.slot.Slot;
import com.iris.bot.slot.MatchedSlot;

public class AccTypeSlot extends Slot {

	private String name;

	public AccTypeSlot(String name) {
		this.name = name;
	}

	/*
	 * For intent where we want to understand what type of account balance the usr is looking for, a very simple method is to apply a 
	 * string match of possible account types. Since we are only looking for whether the utterance contains any of those keywords,
	 * all of the below possibilities are covered:
	 * i am looking for annuities account balance
	 * annuities
	 * annuities balance
	 * tell 401k balance
	 * want my retirement balance etc.    
	 */
	@Override
	public MatchedSlot match(String utterance) {
		if (utterance.toLowerCase().contains("annuities") || utterance.toLowerCase().contains("annuity")) {
			return new MatchedSlot(this, "annuities", "annuities");
		} else if (utterance.toLowerCase().contains("401k") || utterance.toLowerCase().contains("retirement")
				|| utterance.toLowerCase().contains("401") || utterance.toLowerCase().contains("401 k")) {
			return new MatchedSlot(this, "401k", "401k");
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

}
