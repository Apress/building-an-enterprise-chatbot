package com.iris.bot.slot;

/*
 * Slot is defined as an abstract class. Concrete class of Slot will implement match method
 */
public abstract class Slot {
	public abstract MatchedSlot match(String utteranceToken);
	public abstract String getName();

}