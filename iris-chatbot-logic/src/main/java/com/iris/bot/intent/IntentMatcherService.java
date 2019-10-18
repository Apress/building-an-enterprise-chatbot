package com.iris.bot.intent;

import java.util.HashMap;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.bot.response.IntentResponse;
import com.iris.bot.session.Session;
import com.iris.bot.slot.CustomSlotMatcher;
import com.iris.bot.slot.MatchedSlot;
import com.iris.bot.slot.Slot;

public class IntentMatcherService {

	/** A map of possible intent names and intents that are defined in Iris Configuration */
	private HashMap<String, Intent> intents = new HashMap<String, Intent>();

	/** The slot matcher method to use for named entity recognition. */
	private CustomSlotMatcher slotMatcher;

	/** Intent Matcher Service constructor that sets slot matcher */
	public IntentMatcherService(CustomSlotMatcher slotMatcher) {
		this.slotMatcher = slotMatcher;
	}

	/*
	 * RestTemplate is a Synchronous client to perform HTTP requests, exposing a simple, template method API over underlying HTTP client libraries. The RestTemplate offers templates for common scenarios by HTTP method, in addition to the generalized exchange and execute methods that support of less frequent cases.
	 */
	protected RestTemplate restTemplate = new RestTemplate();

	/* This method take user utterance and session as an input, obtains matched intent from intent classification service, 
	 * performs named entity recognition on slots defined for the matched intent and sets the matched intent into user session.
	 * Session is a server side storage mechanism that stores user's interaction and resets the information or persists 
	 * based on the interaction duration and the type of information
	 *  */
	public MatchedIntent match(String utterance, Session session) {

		// getIntent method returns the matched intent
		Intent matchedIntent = getIntent(utterance);

		/*
		 * We define slots associated with each Intent in the Iris Configuration class.
		 * Each of these slots have a match method defined to describe how entities are to be matched.
		 * Depending on the entity and implementation, various NER models can be used to recognize entities.
		 * This method returns a map of slot and matched slot object. Slot contains a slot name and a match method and 
		 * MatchedSlot contains slot that was matched, value that was used to match on and the value that was matched 
		 */
		HashMap<Slot, MatchedSlot> matchedSlots = slotMatcher.match(session, matchedIntent, utterance);

		/*
		 * once we get the matched intent, we set the value of the intent in session.
		 * We will discuss session under IRIS Memory topic
		 */
		session.setAttribute("currentIntentName", matchedIntent.getName());

		/*
		 * Finally, an object with matched intent,matched slots and the utterance against whcih the intents and slots
		 * were matched is returned
		 */
		return new MatchedIntent(matchedIntent, matchedSlots, utterance);
	}

	/*
	 * getIntent method takes user utterance and returns object of type Intent.
	 * This is then used by the match method to match slots for that intent.
	 */
	public Intent getIntent(String utterance) {

		/*
		 * Intent Response is a plain java object with 3 attributes - utterance, intent name and probability returned by the intent service
		 */
		IntentResponse matchedIntent = new IntentResponse();

		/*
		 * If the intent classification engine is not able to classify the utterance into some intent with some threshold or if
		 * the engine is unable to return a valid response, we fallback it to be a general query intent to be on the safe side
		 */
		String defaultIntentName = "generalQueryIntent";
		String matchedIntentName = null;

		/*
		 * ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJOs (Plain Old Java Objects),
		 * or to and from a general-purpose JSON Tree Model (JsonNode), as well as related functionality for performing conversions.
		 * ObjectMapper is a part of com.fasterxml.jackson.databind package which is a high-performance JSON processor for Java
		 */
		ObjectMapper mapper = new ObjectMapper();

		/*
		 * There are certain enumeration that defines simple on/off features to set for ObjectMapper.
		 * ACCEPT_CASE_INSENSITIVE_PROPERTIES is a feature that will allow for more forgiving deserialization of incoming JSON
		 * FAIL_ON_UNKNOWN_PROPERTIES is a feature that determines whether encountering of unknown properties (ones that do not map to a property, and there is no "any setter" or handler that can handle it) should result in a failure (by throwing a JsonMappingException) or not.
		 */
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			matchedIntent = restTemplate.getForObject("http://localhost:8080" + "/intent/" + utterance, IntentResponse.class);

			if(matchedIntent != null && matchedIntent.getIntent()!=null){
				matchedIntentName = matchedIntent.getIntent();
			}
			else
				// if matched intent is null, we consider default intent to be the matched intent 
				matchedIntentName = defaultIntentName;
		} catch (Exception e) {
			// in case of any exception too, we consider default
			matchedIntentName = defaultIntentName;
		}

		// finally, we return the intent object with the matched intent name back to the match method of Intent Matcher Service
		return intents.get(matchedIntentName);
	}

	public void addIntent(Intent intent) {
		intents.put(intent.getName(), intent);
	}

}
