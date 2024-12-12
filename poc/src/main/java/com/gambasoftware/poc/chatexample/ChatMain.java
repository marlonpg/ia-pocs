package com.gambasoftware.poc.chatexample;

import com.gambasoftware.poc.chatexample.enums.ModelName;
import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;

import static dev.langchain4j.data.message.UserMessage.userMessage;

public class ChatMain {
    public static void main(String[] args) {
        ///////////
        //Simple switch between models
        ///////////
        ChatService chatService = new ChatService();
        ChatLanguageModel chatLanguageModel = chatService.getChatLanguageModel(ModelName.LLAMA_3_2);
        String inputMessage = "Say 'Hello World' in Japanese";
        System.out.println("ChatLanguageModel answer: " + chatLanguageModel.generate(inputMessage) + "\n\n");
    }
}
