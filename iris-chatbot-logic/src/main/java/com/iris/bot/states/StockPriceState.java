package com.iris.bot.states;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iris.bot.intent.MatchedIntent;
import com.iris.bot.session.Session;
import com.iris.bot.state.State;

public class StockPriceState extends State {

	public StockPriceState() {
		super("stockPriceState");
	}

	@Override
	public String execute(MatchedIntent matchedIntent, Session session) {

		/*
		 * In the URL below, we have hard coded symbol=MSFT. MSFT is the symbol for Microsoft. In an actual implementation we should
		 * retrieve the company name from user utterance and find its symbol and then pass it in the GET request below.
		 * There are many ways to convert from company name to symbol such as by calling publicly available services or by maintaining a mapping
		 */
		String uri = "https://www.alphavantage.co/query?apikey=YOUR_KEY&function=TIME_SERIES_DAILY&outputsize=compact&symbol=MSFT";

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);

		// default answer in case the 3rd party API does not respond or if there is any network related issue
		String answer = "I am somehow unable to retrieve stock price details right now. But I will be able to help you with your other queries.";

		ObjectMapper mapper = new ObjectMapper();
		try {
			/*
			 * As we know that stock market do no run on the weekends and certain holidays. IRIS is expected to provide
			 * real time stock performance data. In normal working day, we parse out performance detail of current day but however
			 * if there was a holiday or if stock market was closed or if the stock did not trade that day, we get the performance detail of the previous day.
			 */
			Date date = new Date();
			String today = new SimpleDateFormat("yyyy-MM-dd").format(date);
			String yday = new SimpleDateFormat("yyyy-MM-dd").format(yesterday(1));
			String dayBeforeYday = new SimpleDateFormat("yyyy-MM-dd").format(yesterday(2));

			JsonNode actualObj = mapper.readTree(result);
			JsonNode jsonNode1 = actualObj.get("Time Series (Daily)");
			JsonNode jsonNode2 = jsonNode1.get(today);
			JsonNode jsonNode3 = jsonNode1.get(yday);
			JsonNode jsonNode4 = jsonNode1.get(dayBeforeYday);
			if (jsonNode2 != null) {
				answer = "Today PRU stock opened at " + jsonNode2.get("1. open").textValue() + " and closed at "
						+ jsonNode2.get("4. close").textValue();
				answer = answer + " It saw an intraday high of " + jsonNode2.get("2. high").textValue()
						+ " and an intraday low of " + jsonNode2.get("3. low").textValue();
				answer = answer + ". Total volume traded is " + jsonNode2.get("5. volume").textValue() + "\n"
						+ "How else can I help you?";
			} else if (jsonNode3 != null) {
				answer = "I dont have the trading info for today as of now, but Yesterday PRU stock opened at "
						+ jsonNode3.get("1. open").textValue() + " and closed at "
						+ jsonNode3.get("4. close").textValue();
				answer = answer + " It saw an intraday high of " + jsonNode3.get("2. high").textValue()
						+ " and an intraday low of " + jsonNode3.get("3. low").textValue();
				answer = answer + ". Total volume traded is " + jsonNode3.get("5. volume").textValue() + "\n"
						+ "How else can I help you?";
			} else if (jsonNode4 != null) {
				answer = "On friday, before weekend, PRU stock opened at " + jsonNode4.get("1. open").textValue()
						+ " and closed at " + jsonNode4.get("4. close").textValue();
				answer = answer + " It saw an intraday high of " + jsonNode4.get("2. high").textValue()
						+ " and an intraday low of " + jsonNode4.get("3. low").textValue();
				answer = answer + ". Total volume traded is " + jsonNode4.get("5. volume").textValue() + "\n"
						+ "How else can I help you?";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}

	/*
	 *  a method to return 'days' before current day. If the value of 'days' is 1, yesterday date is returned.
	 *  If the value is 2, day before yesterday is returned and so on
	 */
	private Date yesterday(int days) {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -days);
		return cal.getTime();
	}

}
