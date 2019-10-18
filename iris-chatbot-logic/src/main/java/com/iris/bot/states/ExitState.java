package com.iris.bot.states;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.State;

public class ExitState extends State {

	public ExitState() {
		super("exitState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		String answer = "Anything else that I may help you with?";
		return answer;
	}

}
