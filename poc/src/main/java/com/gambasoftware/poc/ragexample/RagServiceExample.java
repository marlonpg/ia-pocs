package com.gambasoftware.poc.ragexample;

import com.gambasoftware.poc.chatexample.Assistant;
import com.gambasoftware.poc.chatexample.ChatService;
import com.gambasoftware.poc.chatexample.enums.ModelName;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.List;

//RAG (Retrieval-Augmented Generation)
//LLM's knowledge is limited to the data it has been trained on.
public class RagServiceExample {
    public static void main(String[] args) {
        ChatService chatService = new ChatService();
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.pdf");
        List<Document> documents = FileSystemDocumentLoader.loadDocuments("D://home/github/ia-pocs/poc/src/main/resources/", pathMatcher);

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatService.getChatLanguageModel(ModelName.LLAMA_3_2))
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();

        String answer = assistant.chat("What is the address that is mentioned inside mtg-rules.pdf?");
        System.out.println(answer);
    }
}
