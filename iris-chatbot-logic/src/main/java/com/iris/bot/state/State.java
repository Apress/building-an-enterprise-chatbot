package com.iris.bot.state;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;

/*
 * State is an abstract class. Concrete State classes will implement execute method which will be triggered when a transition to that state happens
 */
public abstract class State {
	String name;

	public String toString() {
		return name;
	}

	public State(String stateName) {
		name = stateName;
	}

	public String getName() {
		return name;
	}

	/*
	 * execute method takes session and matched intent as argument. Action of the state is defined in this method
	 */
	public abstract String execute(MatchedIntent matchedIntent, Session session);

}
