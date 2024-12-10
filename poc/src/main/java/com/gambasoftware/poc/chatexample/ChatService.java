package com.gambasoftware.poc.chatexample;

import com.gambasoftware.poc.chatexample.enums.ModelName;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.huggingface.HuggingFaceChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    public ChatLanguageModel getChatLanguageModel(ModelName modelName) {
        if (ModelName.FALCON_7B == modelName) {
            return HuggingFaceChatModel.builder()
                    .modelId(ModelName.FALCON_7B.getModelName())
                    .accessToken("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")
                    .build();
        } else if (ModelName.GPT_4O_MINI == modelName) {
            return OpenAiChatModel.builder()
                    .apiKey("demo")
                    .modelName(ModelName.GPT_4O_MINI.getModelName())
                    .build();
        }
        return OllamaChatModel.builder()
                .modelName("llama3.2")
                .baseUrl("http://localhost:11434")
                .build();
    }
}
