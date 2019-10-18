package com.iris.bot.slots;

import com.iris.bot.slot.Slot;
import com.iris.bot.slot.MatchedSlot;

public class IpinSlot extends Slot {
	private String name;

	public IpinSlot(String name) {
		this.name = name;
	}

	@Override
	public MatchedSlot match(String token) {
		if (token.matches("[0-9]+") && token.length() == 6 && token.equalsIgnoreCase("123456")) {
			return new MatchedSlot(this, token, token);
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

}
