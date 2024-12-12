package com.gambasoftware.poc.chatexample;

import com.gambasoftware.poc.chatexample.enums.ModelName;
import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;

import static dev.langchain4j.data.message.UserMessage.userMessage;

public class ChatMemoryMain {
    public static void main(String[] args) {
        ///////////
        //ChatMemory
        ///////////
        ChatService chatService = new ChatService();
        ChatLanguageModel chatLanguageModel = chatService.getChatLanguageModel(ModelName.LLAMA_3_2);
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .build();

        chatMemory.add(userMessage("Answer only with max 10 words."));

        // First user message
        chain.execute("Who is Ronaldinho Gaucho?");

        // Second user message
        chain.execute("Does he like to play soccer?");

        // Print the current conversation from memory
        System.out.println("Chat History of messages:");
        chatMemory.messages().forEach(chatMessage -> System.out.println(chatMessage.text()));

    }
}
