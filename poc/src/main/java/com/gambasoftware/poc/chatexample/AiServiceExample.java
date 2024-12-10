package com.gambasoftware.poc.chatexample;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

//@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, chatMemory = "llama3.2")
public interface AiServiceExample {
    //@SystemMessage("You are a polite assistant")
    String chat(String userMessage);
}
