package com.iris.bot.config;

import com.iris.bot.intent.IntentMatcherService;
import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.StateMachine;

public class StateMachineManager {

	/** The intent matcher service for IRIS bot. */
	protected IntentMatcherService intentMatcherService;

	/** The state machine */
	protected StateMachine stateMachine;

	/**
	 * Constructs the bot by passing configuration class that sets up the intent matcher service and state machine.
	 * 
	 */
	public StateMachineManager(IrisConfiguration configuration) {
		intentMatcherService = configuration.getIntentMatcherService();
		stateMachine = configuration.getStateMachine();
	}

	public String respond(Session session, String utterance) throws Exception {
		try {
			/*
			 * invokes intent matcher service match method that returns matched intent.
			 * This method sends user utterance and session as an input and obtains matched intent from intent classification service
			 */
			MatchedIntent matchedIntent = intentMatcherService.match(utterance, session);
			/*
			 * This method sends the matched intent as an input along with session and gets the response back from state machine. 
			 */
			String response = stateMachine.trigger(matchedIntent, session);
			// finally the response is returned 
			return response;
		} catch (IllegalStateException e) {
			throw new Exception("Hit illegal state", e);
		}
	}

}
