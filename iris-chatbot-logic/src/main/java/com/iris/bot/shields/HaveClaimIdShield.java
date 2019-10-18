package com.iris.bot.shields;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.Shield;

public class HaveClaimIdShield implements Shield {

	@Override
	public boolean validate(MatchedIntent request, Session session) {
		SessionStorage.saveSlotsToSession(request, session);
		String claimId = SessionStorage.getStringFromSlotOrSession(request, session, "claimId", null);
		return (claimId != null);
	}

}
