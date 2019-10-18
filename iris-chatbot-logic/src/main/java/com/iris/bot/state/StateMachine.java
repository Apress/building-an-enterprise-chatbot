package com.iris.bot.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.iris.bot.intent.Intent;
import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;

/*
 * StateMachine is the backbone class for IRIS state management. It contains start state, a map of states, 
 * and a map of state transitions, all of which are defined in the Iris Configuration
 */
public class StateMachine {

	/*
	 * start state, there will always be a pre-defined start state which will be the initial conversation state.
	 * Start state is initialized in Iris configuration class
	 */
	private State startState;

	// a map of all the defined states
	private HashMap<String, State> states = new HashMap<String, State>();

	// a map of transition key and a list of possible transitions
	private HashMap<String, List<Transition>> stateTransitions = new HashMap<String, List<Transition>>();

	public void setStartState(State state) {
		this.startState = state;
	}

	// method to add states in the state map
	private void addState(State state) {
		states.put(state.getName(), state);
		if (startState == null) {
			startState = state;
		}
	}

	/*
	 *  addTransition method is used to add transition from one state to another.
	 *  It requires intent name, from state and to state to define the transition
	 */
	public void addTransition(String intentName, State fromState, State toState) {
		// when no Shield is passed, it is passed as null
		addTransition(intentName, fromState, toState, null);
	}

	/*
	 * over loaded addTransition method that is similar to above but Shield is to be validated for this transition
	 */
	public void addTransition(String intentName, State fromState, State toState, Shield shield) {
		if (!states.containsKey(fromState.getName())) {
			addState(fromState);
		}

		if (!states.containsKey(toState.getName())) {
			addState(toState);
		}

		String key = makeTransitionKey(intentName, fromState);
		List<Transition> transitionList = stateTransitions.get(key);
		if (transitionList == null) {
			transitionList = new ArrayList<Transition>();
			stateTransitions.put(key, transitionList);
		}

		transitionList.add(new Transition(toState, shield));

	}

	/*
	 * This method is the heart of state machine.
	 * It receives the matched intent as an input along with session to know the current state.
	 * It then does a series of things - obtains current state from session or initializes start state if no current state, 
	 * then gets the matched intent and generates a transition key to look up in the transition map and finally triggers the execute method of
	 * target state and updates the state in session
	 */
	public String trigger(final MatchedIntent matchedIntent, final Session session) {

		State currentState = startState;

		// gets the current state from session. if it is new session, this will be null
		String currentStateName = (String) session.getAttribute("currentStateName");

		if (currentStateName != null) {
			currentState = states.get(currentStateName);
			// at this point, current state should not be null and hence an exception is thrown as handling of this condition is unknown
			if (currentState == null) {
				throw new IllegalStateException("Illegal current state in session:" + currentStateName);
			}
		}

		Intent intent = matchedIntent.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		// intent should not null here as it is expected that the matched intent will be an intent from the defined intent list
		if (intentName == null) {
			throw new IllegalArgumentException("Request missing intent." + matchedIntent.toString());
		}

		// generate transition key by using the pattern 'intentname-statename'
		String key = makeTransitionKey(intentName, currentState);

		// get the target state transition list from the state transitions map
		List<Transition> transitionToStateList = stateTransitions.get(key);

		/*
		 * if there is a condition where the intent is valid and current state is valid but there is no transaction defined,
		 * and if there is no definition of where to go, it is an illegal state condition and cannot be handled
		 */
		if (transitionToStateList == null) {
			throw new IllegalStateException("Could not find state to transition to. Intent: " + intentName
					+ " Current State: " + currentState);
		}

		State transitionToState = null;

		// find first matching to-state, and check shield conditions. This method iterates one by one to find successful transition target state
		for (Transition transition : transitionToStateList) {
			if (transition.getShield() == null) {
				// if there is no shield condition and there is a valid transition, assign the transitionToState as that target state
				transitionToState = transition.getToState();
				break;
			} else {
				// if there is a shield condition, it will be validated and upon successful validation, target state will be assigned as transitionToState
				if (transition.getShield().validate(matchedIntent, session)) {
					transitionToState = transition.getToState();
					break;
				}
			}
		}

		// if state machine didn't find any matching states, it is an illegal state as it is not defined.
		if (transitionToState == null) {
			throw new IllegalStateException("Could not find state to transition to. Failed all guards. Intent: "
					+ intentName + " Current State: " + currentState);
		}

		// action to be performed upon successful transition is executed and response is returned
		String response = transitionToState.execute(matchedIntent, session);

		// current state is now updated in session
		session.setAttribute("currentStateName", transitionToState.getName());

		return response;
	}

	/*
	 * transition key is defined to store transition key and a list of transitions
	 */
	private String makeTransitionKey(String intentName, State state) {
		return intentName + '-' + state.getName();
	}
}

