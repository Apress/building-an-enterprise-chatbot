package com.iris.bot.shields;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.Shield;

public class DontHaveQuoteDetailsShield implements Shield {

	@Override
	public boolean validate(MatchedIntent match, Session session) {

		// save slots into session
		SessionStorage.saveSlotsToSession(match, session);

		String age = SessionStorage.getStringFromSlotOrSession(match, session, "age", null);
		String smoked = SessionStorage.getStringFromSlotOrSession(match, session, "smoked", null);
		String height = SessionStorage.getStringFromSlotOrSession(match, session, "height", null);
		String weight = SessionStorage.getStringFromSlotOrSession(match, session, "weight", null);

		/*
		 * if we dont have all the slots fulfilled, we need to return true so that askForQuote state is executed again
		 * As there are multiple questions asked in askForQuote state, unless all questions are answered and all values
		 * populated, the state remains the same, unless the intent of the user changes 
		 */
		return (age == null || smoked == null || height == null || weight == null);
	}

}
