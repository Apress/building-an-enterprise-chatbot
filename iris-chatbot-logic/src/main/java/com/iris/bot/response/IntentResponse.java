package com.iris.bot.response;

public class IntentResponse {

	private String utterance;
	private String intent;
	private Double probability;

	public IntentResponse(){
		
	}

	public IntentResponse(String utterance, String intent, Double probability) {
		this.utterance = utterance;
		this.intent = intent;
		this.probability = probability;
	}


	public String getUtterance() {
		return utterance;
	}


	public void setUtterance(String utterance) {
		this.utterance = utterance;
	}


	public String getIntent() {
		return intent;
	}


	public void setIntent(String intent) {
		this.intent = intent;
	}


	public Double getProbability() {
		return probability;
	}


	public void setProbability(Double probability) {
		this.probability = probability;
	}
	
}
