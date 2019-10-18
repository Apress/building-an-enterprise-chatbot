package com.iris.bot.state;

/*
 * Transition class holds target state and shield information
 */
public class Transition {

	private State toState;
	private Shield shield;

	public Transition(State toState, Shield shield) {
		super();
		this.shield = shield;
		this.toState = toState;
	}

	public State getToState() {
		return toState;
	}

	public void setToState(State toState) {
		this.toState = toState;
	}

	public Shield getShield() {
		return shield;
	}

	public void setShield(Shield shield) {
		this.shield = shield;
	}

}
