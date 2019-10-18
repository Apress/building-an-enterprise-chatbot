package com.iris.bot.states;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.State;

public class GetAccTypeState extends State {

	public GetAccTypeState() {
		super("getAccTypeState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		SessionStorage.saveSlotsToSession(matchedIntent, session);

		String reply = null;

		if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "ipin", null) == null) {
			if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "getipinprompt", null) == null)
				reply = "Sure I will help you with that! Since this is a confidential information, I will need additional details to verify "
						+ "your identity. Can you tell me your 6 digits IPIN please?";
			else
				reply = "Either you have not entered 6 digits code or the IPIN entered by you is incorrect. Please verify and type again !";
			session.setAttribute("getipinprompt", "flag1");
		}

		else if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "accType", null) == null) {
			if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "getaccTypeprompt", null) == null)
				reply = "Your IPIN was successfully verified. Are you looking for Annuities balance or 401k account balance?";
			else
				reply = "I did not understand that. Did you say annuities or 401k?";
			session.setAttribute("getaccTypeprompt", "flag1");
		}
		return reply;
	}

}
