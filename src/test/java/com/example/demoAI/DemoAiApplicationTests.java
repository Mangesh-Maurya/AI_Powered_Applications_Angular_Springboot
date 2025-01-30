package com.example.demoAI;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
class DemoAiApplicationTests {
	
	@Autowired
	private ChatService chatService;

	@Test
	void testResponse() throws JsonProcessingException {
	CricketResponse cricketResponse	= chatService.generateCricketResponse("who is sachin ?");
	System.out.println(cricketResponse);
	}

	@Test 
	void testTemplate() throws IOException {
		String s = chatService.loadPromptTemplate("prompts/cricket_bot.txt");
		String prompt = chatService.putValuesInPromptTemplate(s, Map.of("inputText", "what is cricket"));
		System.out.println(prompt);

	}

}
