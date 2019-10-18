package com.iris.bot.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.iris.bot.request.ConversationRequest;
import com.iris.bot.response.ConversationResponse;
import com.iris.bot.service.ConversationService;

@RestController
public class ConversationController {

	@Autowired
	ConversationService conversationService;

	final static Logger logger = LoggerFactory.getLogger(ConversationController.class);

	@RequestMapping(value = "/respond", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public  ConversationResponse getKeywordresults(@ModelAttribute ConversationRequest request) {
		return conversationService.getResponse(request);
	}
}
