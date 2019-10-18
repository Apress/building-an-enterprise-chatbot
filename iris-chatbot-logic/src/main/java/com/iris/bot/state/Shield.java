package com.iris.bot.state;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;

/*
 * Shield is an interface. Class that implements Shield will implement the validate method and return true if validation condition is met.
 * Otherwise false will be returned and transition to that state will not happen
 */
public interface Shield {

	public boolean validate(MatchedIntent match, Session session);
}
