package com.iris.bot.states;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.State;

public class MarketTrendState extends State {

	public MarketTrendState() {
		super("marketTrendState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {

		// 3rd party API that provides us the current market trend
		String uri = "https://www.alphavantage.co/query?function=SECTOR&apikey=A7RMMBAZ63KUIH8W";

		// Java client that performs HTTP request and gets response by performing a GET call to the URL
		RestTemplate restTemplate = new RestTemplate();
		/*
		 * Response is mapped to a string object below. However, in actual development we should create a Java Bean (POJO)
		 * that will be used to map the response into a java response object by using getForObject method
		 */
		String result = restTemplate.getForObject(uri, String.class);

		String answer = "";

		/*
		 * ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJO. It is part of Jackson, a standard java library for parsing JSON  
		 */
		ObjectMapper mapper = new ObjectMapper();
		try {

			/*
			 * JsonNode is used to parse response in a JSON tree model representation by Jackson. JsonNode is a base class for all JSON nodes, which form the basis of JSON Tree Model that Jackson implements. One way to think of these nodes is to consider them similar to DOM nodes in XML DOM trees
			 */
			JsonNode actualObj = mapper.readTree(result);
			JsonNode jsonNode1 = actualObj.get("Rank A: Real-Time Performance");

			answer = "Energy Sector is " + jsonNode1.get("Energy").textValue() + ". Utilities at "
					+ jsonNode1.get("Utilities").textValue() + ". Real Estate at "
					+ jsonNode1.get("Real Estate").textValue() + ". Consumer Staples at "
					+ jsonNode1.get("Consumer Staples").textValue() + ". Health Care at "
					+ jsonNode1.get("Health Care").textValue() + ". Materials at "
					+ jsonNode1.get("Materials").textValue() + ". Telecommunication Services at "
					+ jsonNode1.get("Telecommunication Services").textValue() + ". Industrials at "
					+ jsonNode1.get("Industrials").textValue() + ". Information Technology at "
					+ jsonNode1.get("Information Technology").textValue() + ". Consumer Discretionary at "
					+ jsonNode1.get("Consumer Discretionary").textValue() + ". Financials at "
					+ jsonNode1.get("Financials").textValue() + "\nWhat else do you want to know?";
		} catch (Exception e) {
			e.printStackTrace();
			result = "I am unable to retrieve this information right now. There is some problem at my end.\nTry asking something else!";
		}
		return answer;
	}

}
