package com.iris.bot.states;

import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.State;

public class FindAdvisorState extends State {

	public FindAdvisorState() {
		super("findAdvisorState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		String reply = "You know what, I dont have the data about financial advisors with me."
				+ "\nBut I hope you do get the point that I could have surely provided it to you if I was connected to Prudential database.\n"
				+ "I will let my boss know that you were asking for it. Next time you wont be disappointed, I promise.\n Here, ask me anything else for now please!";
		return reply;
	}

}
