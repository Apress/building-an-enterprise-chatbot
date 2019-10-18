package com.iris.bot.states;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.State;

public class GeneralQueryState extends State {

	public GeneralQueryState() {
		super("generalQueryState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {
		String answer = "I am so Sorry, I do not have any information related to your query. Can I help you with something else?";

		String uri = "https://www.dummy-knowledge-base-service-url?inputString=";
		uri = uri + matchedIntent.getUtterance();

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);

		ObjectMapper mapper = new ObjectMapper();
		try {

			if(result!=null){
				/*
				 * if result is not null and contains response (answer), we parse that information and assign it to the answer variable
				 * answer = "PARSED-INFORMATION from result" + "\nAnything else that you would like to ask?";
				 */
			}
			else{

				/*
				 * there was no answer obtained from the knowledge repository, then in order to back fill with some valid response,
				 * we call the enterprise search api and pass the utterance as a search string 
				 */
				uri = "https://www.my-enterprise-website.com/searchservice/fullsearch?&inputSearchString=";
				/*
				 *  the utterance is added to the HTTP GET request. Depending on the implementation it could be GET or POST and service may have different parameters  
				 */
				uri = uri + matchedIntent.getUtterance();
				result = restTemplate.getForObject(uri, String.class);
				mapper = new ObjectMapper();
				try {
					/*
					 * Here we try to parse the JSON response and if there is a result with a decent score returned from the search engine,
					 * we read the title and description of the result and add it before sending the response back
					 */

					answer = "Sorry I do not have an exact answer to this right now. "
							+ "You may get some details on the page - " + "TITLE OF THE PAGE OBTAINED FROM RESPONSE"
							+ ". Click here -> " + "URL LINK" + " for more info."
							+ "\nAnything else that you would like to ask?";

				} catch (Exception e) {
					answer = "I am so Sorry, I do not have any information related to your query. Can I help you with something else?";
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			answer = "I am so Sorry, I do not have any information related to your query. Can I help you with something else?";
		}
		return answer;
	}
}
