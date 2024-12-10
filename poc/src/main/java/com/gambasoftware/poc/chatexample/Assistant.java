package com.gambasoftware.poc.chatexample;

import dev.langchain4j.service.SystemMessage;

public interface Assistant {
    @SystemMessage("You are a good friend of mine. Answer using slang.")
    String chat(String userMessage);
}