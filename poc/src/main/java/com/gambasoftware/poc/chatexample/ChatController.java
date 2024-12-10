package com.gambasoftware.poc.chatexample;

import com.gambasoftware.poc.chatexample.enums.ModelName;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {


    @Autowired
    private ChatService chatService;


    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        Assistant assistant = AiServices.create(Assistant.class, chatService.getChatLanguageModel(ModelName.LLAMA_3_2));
        return assistant.chat(message);
    }
}
