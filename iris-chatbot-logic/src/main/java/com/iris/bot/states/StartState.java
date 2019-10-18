package com.iris.bot.states;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.State;

public class StartState extends State {

	public StartState() {
		super("startState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		throw new IllegalStateException("You shouldn't be executing this state!");
	}

}
