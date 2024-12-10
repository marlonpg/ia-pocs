package com.gambasoftware.poc.chatexample.enums;

public enum ModelName {
    GPT_4O_MINI("gpt-4o-mini"),
    LLAMA_3_2("llama3.2"),
    FALCON_7B("tiiuae/falcon-7b-instruct");

    private final String modelName;

    ModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }
}
