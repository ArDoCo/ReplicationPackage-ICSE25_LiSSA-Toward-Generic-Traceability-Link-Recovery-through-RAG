package lissa.ratlr.classifier;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lissa.ratlr.Configuration;
import lissa.ratlr.cache.Cache;

public class SimpleOllamaClassifier extends SimpleClassifier {

    public SimpleOllamaClassifier(Configuration.ModuleConfiguration configuration) {
        super(configuration, configuration.argumentAsString("model", "llama3:8b"));
    }

    public SimpleOllamaClassifier(Cache cache, String model, ChatLanguageModel llm, String template) {
        super(cache, model, llm, template);
    }

    @Override
    protected ChatLanguageModel createChatModel(String model) {
        return ChatModels.createOllamaChatModel(model);
    }

    @Override
    protected SimpleClassifier copyOf(Cache cache, String model, ChatLanguageModel llm, String template) {
        return new SimpleOllamaClassifier(cache, model, llm, template);
    }
}
