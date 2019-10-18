package com.iris.bot.config;

import com.iris.bot.intent.Intent;
import com.iris.bot.intent.IntentMatcherService;
import com.iris.bot.intents.AccountBalanceIntent;
import com.iris.bot.intents.AskForQuoteIntent;
import com.iris.bot.intents.ClaimStatusIntent;
import com.iris.bot.intents.ExitIntent;
import com.iris.bot.intents.FindAdvisorIntent;
import com.iris.bot.intents.GeneralQueryIntent;
import com.iris.bot.intents.GetAccTypeIntent;
import com.iris.bot.intents.GetClaimIdIntent;
import com.iris.bot.intents.MarketTrendIntent;
import com.iris.bot.intents.StockPriceIntent;
import com.iris.bot.intents.WeatherIntent;
import com.iris.bot.shields.DontHaveAccTypeShield;
import com.iris.bot.shields.DontHaveQuoteDetailsShield;
import com.iris.bot.shields.HaveAccTypeShield;
import com.iris.bot.shields.HaveClaimIdShield;
import com.iris.bot.shields.HaveQuoteDetailShield;
import com.iris.bot.slot.CustomSlotMatcher;
import com.iris.bot.slots.AccTypeSlot;
import com.iris.bot.slots.AlphaNumericSlot;
import com.iris.bot.slots.BooleanLiteralSlot;
import com.iris.bot.slots.CustomNumericSlot;
import com.iris.bot.slots.IpinSlot;
import com.iris.bot.state.Shield;
import com.iris.bot.state.State;
import com.iris.bot.state.StateMachine;
import com.iris.bot.states.AskForQuoteState;
import com.iris.bot.states.ExitState;
import com.iris.bot.states.FindAdvisorState;
import com.iris.bot.states.GeneralQueryState;
import com.iris.bot.states.GetAccTypeState;
import com.iris.bot.states.GetAccountBalanceState;
import com.iris.bot.states.GetClaimIdState;
import com.iris.bot.states.GetClaimStatus;
import com.iris.bot.states.GetQuoteState;
import com.iris.bot.states.GetWeatherState;
import com.iris.bot.states.MarketTrendState;
import com.iris.bot.states.StartState;
import com.iris.bot.states.StockPriceState;

public class IrisConfiguration {

	public IntentMatcherService getIntentMatcherService() {

		CustomSlotMatcher slotMatcher = new CustomSlotMatcher();
		IntentMatcherService intentMatcherService = new IntentMatcherService(slotMatcher);

		Intent findAdvisorIntent = new FindAdvisorIntent();

		Intent askForQuoteIntent = new AskForQuoteIntent();
		// slots for askForQuote intent fulfillment
		askForQuoteIntent.addSlot(new CustomNumericSlot("age"));
		askForQuoteIntent.addSlot(new CustomNumericSlot("height"));
		askForQuoteIntent.addSlot(new CustomNumericSlot("weight"));
		askForQuoteIntent.addSlot(new BooleanLiteralSlot("smoked"));

		Intent generalQueryIntent = new GeneralQueryIntent();

		Intent stockPriceIntent = new StockPriceIntent();

		Intent marketTrendIntent = new MarketTrendIntent();

		Intent accountBalanceIntent = new AccountBalanceIntent();
		accountBalanceIntent.addSlot(new AccTypeSlot("accType"));
		accountBalanceIntent.addSlot(new IpinSlot("ipin"));

		Intent getAccTypeIntent = new GetAccTypeIntent();
		getAccTypeIntent.addSlot(new AccTypeSlot("accType"));
		getAccTypeIntent.addSlot(new IpinSlot("ipin"));

		Intent weatherIntent = new WeatherIntent();

		Intent claimStatusIntent = new ClaimStatusIntent();
		claimStatusIntent.addSlot(new AlphaNumericSlot("claimId"));

		Intent getClaimIdIntent = new GetClaimIdIntent();
		getClaimIdIntent.addSlot(new AlphaNumericSlot("claimId"));

		Intent exitIntent = new ExitIntent();

		/*
		 * all the intents that we defined above are added to the intent matcher service
		 */
		intentMatcherService.addIntent(findAdvisorIntent);
		intentMatcherService.addIntent(askForQuoteIntent);
		intentMatcherService.addIntent(generalQueryIntent);
		intentMatcherService.addIntent(stockPriceIntent);
		intentMatcherService.addIntent(marketTrendIntent);
		intentMatcherService.addIntent(exitIntent);
		intentMatcherService.addIntent(getAccTypeIntent);
		intentMatcherService.addIntent(accountBalanceIntent);
		intentMatcherService.addIntent(weatherIntent);
		intentMatcherService.addIntent(claimStatusIntent);

		return intentMatcherService;
	}

	public StateMachine getStateMachine() {
		/*
		 * creates an instance of StateMachine that holds start state, a map of states, 
		 * and a map of state transitions, all of which are defined below in IrisConfiguration
		 */
		StateMachine stateMachine = new StateMachine();

		State startState = new StartState();

		State askforQuoteState = new AskForQuoteState();
		State getQuoteState = new GetQuoteState();
		Shield haveQuoteDetailShield = new HaveQuoteDetailShield();
		Shield dontHaveQuoteDetailsShield = new DontHaveQuoteDetailsShield();

		State findAdvisorState = new FindAdvisorState();
		State generalQueryState = new GeneralQueryState();
		State stockPriceState = new StockPriceState();
		State marketTrendState = new MarketTrendState();

		State getAccountBalanceState = new GetAccountBalanceState();
		Shield haveAccTypeShield = new HaveAccTypeShield();
		Shield dontHaveAccTypeShield = new DontHaveAccTypeShield();
		State getAccTypeState = new GetAccTypeState();

		State getWeatherState = new GetWeatherState();

		State getClaimStatusState = new GetClaimStatus();
		Shield haveClaimIdShield = new HaveClaimIdShield();
		State getClaimIdState = new GetClaimIdState();

		State exitState = new ExitState();

		/*
		 * Here we initialize the start state. Start state execute method is never supposed to be called
		 */
		stateMachine.setStartState(startState);

		/*
		 * this transition says that if we are in start state, and a generalQueryIntent is obtained,
		 * we remain in the generalQueryState (and trigger execute method of this state)
		 */
		stateMachine.addTransition("generalQueryIntent", startState, generalQueryState);
		/*
		 * this transition says that if we are in start state, and a askForQuoteIntent intent is obtained,
		 * we change to target state which is getQuoteState if the shield conditions are validated.
		 * Else we check the next transition condition
		 */
		stateMachine.addTransition("askForQuoteIntent", startState, getQuoteState, haveQuoteDetailShield);
		/*
		 * If the shield conditions are not validated for the askForQuoteIntent, it means that we do not have all the information
		 * for switching to getQuoteState which provides quote details. Hence in that case we switch to askforQuoteState without the need
		 * of a shield 
		 */
		stateMachine.addTransition("askForQuoteIntent", startState, askforQuoteState);
		stateMachine.addTransition("findAdvisorIntent", startState, findAdvisorState);
		stateMachine.addTransition("stockPriceIntent", startState, stockPriceState);
		stateMachine.addTransition("marketTrendIntent", startState, marketTrendState);
		
		/*
		 * if we are in start state and user's intent was to get account balance, we validate with shield if we have account type
		 * and ipin details to switch to getAccountBalanceState and trigger its execute method
		 */
		stateMachine.addTransition("accountBalanceIntent", startState, getAccountBalanceState, haveAccTypeShield);
		/*
		 * otherwise, if shield does not validate, it means we do not have all the details and hence we switch to getAccTypeState
		 * to get all the details
		 */
		stateMachine.addTransition("accountBalanceIntent", startState, getAccTypeState);
		stateMachine.addTransition("weatherIntent", startState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", startState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", startState, getClaimIdState);

		// state transiitons from getAccountBalanceState
		stateMachine.addTransition("accountBalanceIntent", getAccountBalanceState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", getAccountBalanceState, getAccTypeState);
		stateMachine.addTransition("askForQuoteIntent", getAccountBalanceState, askforQuoteState);
		stateMachine.addTransition("marketTrendIntent", getAccountBalanceState, marketTrendState);
		stateMachine.addTransition("findAdvisorIntent", getAccountBalanceState, findAdvisorState);
		stateMachine.addTransition("stockPriceIntent", getAccountBalanceState, stockPriceState);
		stateMachine.addTransition("weatherIntent", getAccountBalanceState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", getAccountBalanceState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", getAccountBalanceState, getClaimIdState);

		stateMachine.addTransition("accountBalanceIntent", getAccTypeState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", getAccTypeState, getAccTypeState, dontHaveAccTypeShield);
		stateMachine.addTransition("exitIntent", getAccountBalanceState, exitState);
		stateMachine.addTransition("exitIntent", getAccTypeState, exitState);
		stateMachine.addTransition("askForQuoteIntent", getAccTypeState, askforQuoteState);
		stateMachine.addTransition("findAdvisorIntent", getAccTypeState, findAdvisorState);
		stateMachine.addTransition("stockPriceIntent", getAccTypeState, stockPriceState);
		stateMachine.addTransition("marketTrendIntent", getAccTypeState, marketTrendState);
		stateMachine.addTransition("weatherIntent", getAccTypeState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", getAccTypeState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", getAccTypeState, getClaimIdState);

		// state transitions from generalQueryState
		stateMachine.addTransition("generalQueryIntent", generalQueryState, generalQueryState);
		stateMachine.addTransition("askForQuoteIntent", generalQueryState, askforQuoteState);
		stateMachine.addTransition("stockPriceIntent", generalQueryState, stockPriceState);
		stateMachine.addTransition("marketTrendIntent", generalQueryState, marketTrendState);
		stateMachine.addTransition("accountBalanceIntent", generalQueryState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", generalQueryState, getAccTypeState);
		stateMachine.addTransition("findAdvisorIntent", generalQueryState, findAdvisorState);
		stateMachine.addTransition("weatherIntent", generalQueryState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", generalQueryState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", generalQueryState, getClaimIdState);
		stateMachine.addTransition("exitIntent", generalQueryState, exitState);

		// state transitions from marketTrendState
		stateMachine.addTransition("marketTrendIntent", marketTrendState, marketTrendState);
		stateMachine.addTransition("findAdvisorIntent", marketTrendState, findAdvisorState);
		stateMachine.addTransition("askForQuoteIntent", marketTrendState, askforQuoteState);
		stateMachine.addTransition("generalQueryIntent", marketTrendState, generalQueryState);
		stateMachine.addTransition("weatherIntent", marketTrendState, getWeatherState);
		stateMachine.addTransition("accountBalanceIntent", marketTrendState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", marketTrendState, getAccTypeState);
		stateMachine.addTransition("claimStatusIntent", marketTrendState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", marketTrendState, getClaimIdState);
		stateMachine.addTransition("exitIntent", marketTrendState, exitState);

		// state transitions from askforQuoteState
		stateMachine.addTransition("askForQuoteIntent", askforQuoteState, getQuoteState, haveQuoteDetailShield);
		stateMachine.addTransition("askForQuoteIntent", askforQuoteState, askforQuoteState, dontHaveQuoteDetailsShield);
		stateMachine.addTransition("generalQueryIntent", askforQuoteState, generalQueryState);
		stateMachine.addTransition("marketTrendIntent", askforQuoteState, marketTrendState);
		stateMachine.addTransition("findAdvisorIntent", askforQuoteState, findAdvisorState);
		stateMachine.addTransition("accountBalanceIntent", askforQuoteState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", askforQuoteState, getAccTypeState);
		stateMachine.addTransition("exitIntent", askforQuoteState, exitState);
		stateMachine.addTransition("stockPriceIntent", askforQuoteState, stockPriceState);
		stateMachine.addTransition("claimStatusIntent", askforQuoteState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", askforQuoteState, getClaimIdState);
		stateMachine.addTransition("weatherIntent", askforQuoteState, getWeatherState);

		// state transitions from getQuoteState
		stateMachine.addTransition("stockPriceIntent", getQuoteState, stockPriceState);
		stateMachine.addTransition("marketTrendIntent", getQuoteState, marketTrendState);
		stateMachine.addTransition("weatherIntent", getQuoteState, getWeatherState);
		stateMachine.addTransition("generalQueryIntent", getQuoteState, generalQueryState);
		stateMachine.addTransition("findAdvisorIntent", getQuoteState, findAdvisorState);
		stateMachine.addTransition("accountBalanceIntent", getQuoteState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", getQuoteState, getAccTypeState);
		stateMachine.addTransition("claimStatusIntent", getQuoteState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", getQuoteState, getClaimIdState);
		stateMachine.addTransition("askForQuoteIntent", getQuoteState, askforQuoteState);
		stateMachine.addTransition("exitIntent", getQuoteState, exitState);

		// state transitions from findAdvisorState
		stateMachine.addTransition("exitIntent", findAdvisorState, exitState);
		stateMachine.addTransition("marketTrendIntent", findAdvisorState, marketTrendState);
		stateMachine.addTransition("findAdvisorIntent", findAdvisorState, findAdvisorState);
		stateMachine.addTransition("askForQuoteIntent", findAdvisorState, askforQuoteState);
		stateMachine.addTransition("generalQueryIntent", findAdvisorState, generalQueryState);
		stateMachine.addTransition("weatherIntent", findAdvisorState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", findAdvisorState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", findAdvisorState, getClaimIdState);
		stateMachine.addTransition("accountBalanceIntent", findAdvisorState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", findAdvisorState, getAccTypeState);
		stateMachine.addTransition("stockPriceIntent", findAdvisorState, stockPriceState);

		// state transitions from getClaimStatusState
		stateMachine.addTransition("exitIntent", getClaimIdState, exitState);
		stateMachine.addTransition("marketTrendIntent", getClaimIdState, marketTrendState);
		stateMachine.addTransition("findAdvisorIntent", getClaimIdState, findAdvisorState);
		stateMachine.addTransition("askForQuoteIntent", getClaimIdState, askforQuoteState);
		stateMachine.addTransition("generalQueryIntent", getClaimIdState, generalQueryState);
		stateMachine.addTransition("weatherIntent", getClaimIdState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", getClaimIdState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", getClaimIdState, getClaimIdState);
		stateMachine.addTransition("accountBalanceIntent", getClaimIdState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", getClaimIdState, getAccTypeState);
		stateMachine.addTransition("stockPriceIntent", getClaimIdState, stockPriceState);

		stateMachine.addTransition("claimStatusIntent", getClaimStatusState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", getClaimStatusState, getClaimIdState);
		stateMachine.addTransition("exitIntent", getClaimStatusState, exitState);
		stateMachine.addTransition("marketTrendIntent", getClaimStatusState, marketTrendState);
		stateMachine.addTransition("findAdvisorIntent", getClaimStatusState, findAdvisorState);
		stateMachine.addTransition("askForQuoteIntent", getClaimStatusState, askforQuoteState);
		stateMachine.addTransition("generalQueryIntent", getClaimStatusState, generalQueryState);
		stateMachine.addTransition("weatherIntent", getClaimStatusState, getWeatherState);
		stateMachine.addTransition("accountBalanceIntent", getClaimStatusState, getAccountBalanceState,
				haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", getClaimStatusState, getAccTypeState);
		stateMachine.addTransition("stockPriceIntent", getClaimStatusState, stockPriceState);

		// state transitions from exitState
		stateMachine.addTransition("generalQueryIntent", exitState, generalQueryState);
		stateMachine.addTransition("askForQuoteIntent", exitState, askforQuoteState);
		stateMachine.addTransition("findAdvisorIntent", exitState, findAdvisorState);
		stateMachine.addTransition("stockPriceIntent", exitState, stockPriceState);
		stateMachine.addTransition("marketTrendIntent", exitState, marketTrendState);
		stateMachine.addTransition("weatherIntent", exitState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", exitState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", exitState, getClaimIdState);
		stateMachine.addTransition("accountBalanceIntent", exitState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", exitState, getAccTypeState);

		// state transitions from getWeatherState
		stateMachine.addTransition("generalQueryIntent", getWeatherState, generalQueryState);
		stateMachine.addTransition("askForQuoteIntent", getWeatherState, askforQuoteState);
		stateMachine.addTransition("findAdvisorIntent", getWeatherState, findAdvisorState);
		stateMachine.addTransition("stockPriceIntent", getWeatherState, stockPriceState);
		stateMachine.addTransition("marketTrendIntent", getWeatherState, marketTrendState);
		stateMachine.addTransition("weatherIntent", getWeatherState, getWeatherState);
		stateMachine.addTransition("claimStatusIntent", getWeatherState, getClaimStatusState, haveClaimIdShield);
		stateMachine.addTransition("claimStatusIntent", getWeatherState, getClaimIdState);
		stateMachine.addTransition("accountBalanceIntent", getWeatherState, getAccountBalanceState, haveAccTypeShield);
		stateMachine.addTransition("accountBalanceIntent", getWeatherState, getAccTypeState);
		stateMachine.addTransition("exitIntent", getWeatherState, exitState);

		return stateMachine;
	}
}
