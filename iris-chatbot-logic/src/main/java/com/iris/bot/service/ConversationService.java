package com.iris.bot.service;

import org.springframework.stereotype.Service;

import com.iris.bot.config.IrisConfiguration;
import com.iris.bot.config.StateMachineManager;
import com.iris.bot.request.ConversationRequest;
import com.iris.bot.response.ConversationResponse;
import com.iris.bot.session.Session;
import com.iris.bot.session.SessionStorage;

@Service("conversationService")
/*
 * 
 * ConversationService creates a static instance of state machine manager which is passed IrisConfiguration in constructor arguments. 
 * It further creates a static instance of SessionStorage class. These classes are created static because only one instance of these class should be
 * instantiated. Further, singleton design pattern could also be used to design these single instances.
 * ConversationService calls respond method of StateMachineManager and returns response to the controller. Controller calls getResonse method of ConversationService
 * by passing the ConversationRequest
 *  
 */
public class ConversationService {

	private static StateMachineManager irisStateMachineManager = new StateMachineManager(new IrisConfiguration());

	private static SessionStorage userSessionStorage = new SessionStorage();

	public ConversationResponse getResponse(ConversationRequest req) {
		// default response to be sent if there is a server side exception 
		String response = "Umm...I apologise. Either I am not yet trained to answer that or I think I have had a lot of Guinness today. "
				+ "I am unable to answer that at the moment. " + "Could you try asking something else Please !";

		// if the request message is a salutation like hi or hello, then instead of passing this information to statemachine manager, a hard-coded response to salutation can be returned from the service layer
		if (req.getMessage().equalsIgnoreCase("hi") || req.getMessage().equalsIgnoreCase("hey iris")) {
			response = "Hi There! My name is IRIS (isn't it nice! My creators gave me this name). I am here to help you answer your queries, get you the status of your claims,"
					+ " tell you about stock prices, find you a financial advisor, inform you about current market trends, help you check your life insurance eligibility "
					+ "or provide you your account balance information.\n"
					+ "Hey, you know what, I can also tell you about current weather in your city. Try asking me out ! ";
		}

		// gets the session object for the sender of the request
		Session session = userSessionStorage.getOrCreateSession(req.getSender());

		//creates response object
		ConversationResponse conversationResponse = new ConversationResponse();

		try {
			// calls respond method of state manager by passing session and message (user utterance)
			response = irisStateMachineManager.respond(session, req.getMessage());
			// response is set to the the conversationResponse and returned to the controller
			conversationResponse.setMessage(response);
		} catch (Exception e) {
			conversationResponse.setMessage(response);
		}
		return conversationResponse;
	}
}
