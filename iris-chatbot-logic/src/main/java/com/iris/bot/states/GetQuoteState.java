package com.iris.bot.states;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;
import com.iris.bot.state.State;

public class GetQuoteState extends State {

	public GetQuoteState() {
		super("getQuoteState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		SessionStorage.saveSlotsToSession(matchedIntent, session);

		boolean eligible = true;
		String answer = "";

		int age = Integer.parseInt(SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "age", null));
		String smoked = SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "smoked", null);
		int weight = Integer.parseInt(SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "weight", null));
		int height = Integer.parseInt(SessionStorage.getStringFromSlotOrSession(matchedIntent, session, "height", null));

		/*
		 * checking business logic and calculating bmi(body mass index)
		 * In example, eligibility is defined based on whether bmi is less than or greater than 33
		 */
		if (age > 60 || age < 18)
			eligible = false;
		if (smoked.equalsIgnoreCase("yes"))
			eligible = false;

		double weightInKilos = weight * 0.453592;
		double heightInMeters = ((double) height) / 100;
		double bmi = weightInKilos / Math.pow(heightInMeters, 2.0);

		if (bmi > 33)
			eligible = false;

		if (eligible) {
			answer = "Great News! You are eligible for an accelerated UW Decision.\nPlease proceed with your application "
					+ "at this link: https://www.dummylink.com \n"
					+ "Anything else that I could help you with?";
		} else {
			answer = "Unfortunately, You are not eligible for an Accelerated UW Decision.\nPlease register at https://www.dummylink.com "
					+ "and our representatives will contact with you shortly to further process your application\n"
					+ "Anything else that I could help you with?";
		}
		/*
		 * remove attributes stored in session. All of these 4 attributes are treated as short term in the example.
		 * We have used session to also store details of which slots to prompt for and which not to based on whether they were answered by user 
		 */
		session.removeAttribute("getageprompt");
		session.removeAttribute("getsmokedprompt");
		session.removeAttribute("getheightprompt");
		session.removeAttribute("getweightprompt");
		session.removeAttribute("askquotelastquestion");

		session.removeAttribute("height");
		session.removeAttribute("age");
		session.removeAttribute("smoked");
		session.removeAttribute("weight");
		return answer;
	}

}
