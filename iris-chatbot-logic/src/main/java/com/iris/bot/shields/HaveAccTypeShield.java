package com.iris.bot.shields;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.Shield;

public class HaveAccTypeShield implements Shield {

	@Override
	public boolean validate(MatchedIntent match, Session session) {
		SessionStorage.saveSlotsToSession(match, session);
		String accType = SessionStorage.getStringFromSlotOrSession(match, session, "accType", null);
		String ipin = SessionStorage.getStringFromSlotOrSession(match, session, "ipin", null);
		return (accType != null && ipin != null);
	}

}
