package lissa.ratlr.classifier;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lissa.ratlr.Configuration;
import lissa.ratlr.cache.Cache;

public class ReasoningOllamaClassifier extends ReasoningClassifier {
    public ReasoningOllamaClassifier(Configuration.ModuleConfiguration configuration) {
        super(configuration, configuration.argumentAsString("model", "llama3:8b"));
    }

    protected ReasoningOllamaClassifier(Cache cache, String model, ChatLanguageModel llm, String prompt, boolean useOriginalArtifacts,
            boolean useSystemMessage) {
        super(cache, model, llm, prompt, useOriginalArtifacts, useSystemMessage);
    }

    @Override
    protected ChatLanguageModel createChatModel(String model) {
        return ChatModels.createOllamaChatModel(model);
    }

    @Override
    protected ReasoningClassifier copyOf(Cache cache, String model, ChatLanguageModel llm, String prompt, boolean useOriginalArtifacts,
            boolean useSystemMessage) {
        return new ReasoningOllamaClassifier(cache, model, llm, prompt, useOriginalArtifacts, useSystemMessage);
    }
}
