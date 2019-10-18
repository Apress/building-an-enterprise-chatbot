// We define intent classes in com.iris.bot.intent package
package com.iris.bot.intent;

import java.util.Collection;
import java.util.Collections;

import com.iris.bot.slot.Slot;
import com.iris.bot.slot.Slots;

public class Intent {
	/** The name of the intent. */
	protected String name;

	/** The slots for the intent.
	 * There could be 0 or more slots defined for each intent.
	 * Slots contain a list of Slot and methods to add and get Slot  */
	protected Slots slots = new Slots();

	/**
	 constructor with name as parameter
	 It sets the Intent name at the time of intent creation 
	 */
	public Intent(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the intent.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a slot to the intent.
	 */
	public void addSlot(Slot slot) {
		slots.add(slot);
	}

	/**
	 * Returns the slots for the intent.
	 */
	public Collection<Slot> getSlots() {
		return Collections.unmodifiableCollection(slots.getSlots());
	}

}
