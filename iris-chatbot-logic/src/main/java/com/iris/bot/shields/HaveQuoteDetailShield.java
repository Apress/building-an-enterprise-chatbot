package com.iris.bot.shields;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.Shield;

public class HaveQuoteDetailShield implements Shield {

	@Override
	public boolean validate(MatchedIntent match, Session session) {

		// save slots to session
		SessionStorage.saveSlotsToSession(match, session);

		// get all validation entities from session
		String age = SessionStorage.getStringFromSlotOrSession(match, session, "age", null);
		String smoked = SessionStorage.getStringFromSlotOrSession(match, session, "smoked", null);
		String height = SessionStorage.getStringFromSlotOrSession(match, session, "height", null);
		String weight = SessionStorage.getStringFromSlotOrSession(match, session, "weight", null);

		//return true if all values exist, else return false
		return (age != null && smoked != null && height != null && weight != null);
	}

}
