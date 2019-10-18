package com.iris.bot.states;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.State;

public class GetClaimIdState extends State {

	public GetClaimIdState() {
		super("getClaimIdState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		SessionStorage.saveSlotsToSession(matchedIntent, session);

		String reply = "I am not sure if I have hungover right now. Cant seem to follow you right.";
		if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "claimId", null) == null) {
			if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "getclaimidprompt", null) == null)
				reply = "No Problem. Could you tell me the Claim Id Please?";
			else
				reply = "Sorry, I did not get the claim ID. Can you please re-enter it?";
		}
		session.setAttribute("getclaimidprompt", "flag1");
		return reply;
	}

}
