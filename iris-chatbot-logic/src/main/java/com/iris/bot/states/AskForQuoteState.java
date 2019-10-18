package com.iris.bot.states;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.State;

public class AskForQuoteState extends State {

	public AskForQuoteState() {
		super("askForQuoteState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {

		SessionStorage.saveSlotsToSession(matchedIntent, session);

		// default reply
		String reply = "I am having trouble understanding...";

		// checking for age
		if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "age", null) == null) {
			// age is set in session to be the last question asked in askQuote at this point
			session.setAttribute("askQuoteLastQuestion", "age");
			/*
			 * in order to differentiate between whether we are asking this question for the first time or had we asked before and
			 * the user had not answered, we use "getageprompt". If "getageprompt" value is null, we have not asked this question to user before
			 * in that particular session.
			 * It helps to differentiate the reply message 
			 */

			if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "getageprompt", null) == null)
				reply = "Sure, I will help you with that. May I know your age?";
			else
				/*
				 * Let's say we were expecting that the user will enter his age and that is the current question in conversation.
				 * However, instead of replying age, user changes the intent by asking about weather. IRIS is designed to handle
				 * intent switches from one context to another.
				 * But, next time if user desires to get quote again, we will not ask questions already answered and even the ask message will be different
				 * just like how its mentioned in the if else reply message here
				 */
				reply = "I am not sure if I got your age right last time. Please type again";

			// setting getageprompt in session to note that age has been asked before
			session.setAttribute("getageprompt", "flag1");

			// same logic applies for whether the user answered to his smoking status or not
		} else if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "smoked", null) == null) {
			session.setAttribute("askQuoteLastQuestion", "smoked");
			if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "getsmokedprompt", null) == null)
				reply = "Have you smoked in the last 12 months?";
			else
				reply = "Last time you did not tell me if you smoked in the last 12 months, Have you?";
			session.setAttribute("getsmokedprompt", "flag1");

			// same logic applies for height
		} else if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "height", null) == null) {
			session.setAttribute("askQuoteLastQuestion", "height");
			if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "getheightprompt", null) == null)
				reply = "What's your height (in centimeters)?";
			else
				reply = "What's your height (in centimeters)? Please help me understand again?";
			session.setAttribute("getheightprompt", "flag1");

			// lastly, same logic for weight as well
		} else if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "weight", null) == null) {
			session.setAttribute("askQuoteLastQuestion", "weight");
			if (SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "getweightprompt", null) == null)
				reply = "What's your weight (in pounds)?";
			else
				reply = "Tell me your weight in pounds again. I did not get it the last time";
			session.setAttribute("getweightprompt", "flag1");
		}
		return reply;
	}

}
