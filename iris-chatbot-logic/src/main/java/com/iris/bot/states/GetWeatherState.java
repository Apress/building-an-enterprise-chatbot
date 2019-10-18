package com.iris.bot.states;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.State;

public class GetWeatherState extends State {

	public GetWeatherState() {
		super("getWeatherState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {

		/*
		 * default response in case there is a network issue or if the 3rd party API takes a lot of time or if there is some other exception
		 */
		String answer = "I am unable to get the weather report right now. But I hope it be a nice and charming day for you :) ";

		/*
		 * GET API that provides weather details
		 */
		String uri = "http://api.openweathermap.org/data/2.5/weather?appid=YOUR_API_KEY&q=";
		String cityName = "dublin";

		try {
			RestTemplate restTemplate = new RestTemplate();
			String result = restTemplate.getForObject(uri, String.class);

			ObjectMapper mapper = new ObjectMapper();

			JsonNode actualObj = mapper.readTree(result);
			ArrayNode jsonNode1 = (ArrayNode) actualObj.get("weather");
			JsonNode jsonNode2 = actualObj.get("main");

			String description = jsonNode1.get(0).get("description").textValue();

			String temperature = jsonNode2.get("temp").toString();
			Double tempInCelsius = Double.parseDouble(temperature) - 273.15;
			double roundOff = Math.round(tempInCelsius * 100.0) / 100.0;
			String humidity = jsonNode2.get("humidity").toString();

			answer = "It seems to be " + description + " at the moment in " + cityName + ". The temperature is "
					+ roundOff + " degrees. Humidity" + " is close to " + humidity
					+ ".\n I wish I were human to feel it. Anyways, what else do you want to know from me? ";

		} catch (Exception e) {
		}
		return answer;
	}
}
