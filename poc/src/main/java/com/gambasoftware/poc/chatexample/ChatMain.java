package com.gambasoftware.poc.chatexample;

import com.gambasoftware.poc.chatexample.enums.ModelName;
import dev.langchain4j.model.chat.ChatLanguageModel;

public class ChatMain {
    public static void main(String[] args) {
        ///////////
        //Simple switch between models
        ///////////
        ChatService chatService = new ChatService();
        ChatLanguageModel chatLanguageModel = chatService.getChatLanguageModel(ModelName.LLAMA_3_2);
        String inputMessage = "Say 'Welcome, my friend.' in Japanese";
        System.out.println("ChatLanguageModel answer: " + chatLanguageModel.generate(inputMessage) + "\n\n");
    }
}
