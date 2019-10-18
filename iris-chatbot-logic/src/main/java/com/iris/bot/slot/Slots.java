package com.iris.bot.slot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/** The slots for the intent.
 * There could be 0 or more slots defined for each intent.
 * Slots contain a list of Slot and methods to add and get Slot  */

public class Slots {
	/** The map of slots. */
	private HashMap<String, Slot> slots = new HashMap<String, Slot>();

	/**
	 * Adds a slot to the map.
	 */
	public void add(Slot slot) {
		slots.put(slot.getName().toLowerCase(), slot);
	}

	/**
	 * Gets the specified slot from the map.
	 */
	public Slot getSlot(String name) {
		return slots.get(name.toLowerCase());
	}

	/**
	 * Returns the slots in the map.
	 */
	public Collection<Slot> getSlots() {
		return Collections.unmodifiableCollection(slots.values());
	}
}
