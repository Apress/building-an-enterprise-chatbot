package com.iris.bot.states;

import java.util.Random;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.State;

public class GetAccountBalanceState extends State {

	public GetAccountBalanceState() {
		super("getAccountBalanceState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		String reply = null;
		Random rand = new Random();
		String accType = SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "accType", null);
		if (accType.equalsIgnoreCase("Annuities")) {
			/*
			 * In real world implementation, we will actually call a service or query a database to get the account balance.
			 * For the sake of implementation here, we are returning a random integer here
			 */
			reply = "Your Annuities account balance is: " + (rand.nextInt(1000) + 100) + "."
					+ "\nAnything else that I can do for you?";
		} else if (accType.equalsIgnoreCase("401k"))
			reply = "Your 401K account balance is: " + (rand.nextInt(4000) + 500) + "."
					+ "\nAnything else that you want to know?";
		else
			reply = "Sorry, I am not able to retrieve your " + accType
			+ " balance right now.\nHow else can I help you?";

		/*
		 * slot details saved in session attributes previously are now removed.
		 * We cannot store these details even at a session level as the user may request for account balance again 
		 * but this time he may need balance details of a different type of account. However, we still store these values in session
		 * until we reach here so that we know that these information have been answered by user and shields can than validate
		 */
		session.removeAttribute("acctype");
		session.removeAttribute("getaccTypeprompt");
		session.removeAttribute("getipinprompt");
		session.removeAttribute("ipin");

		return reply;
	}

}
