package lissa.ratlr.classifier;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lissa.ratlr.Configuration;
import lissa.ratlr.cache.Cache;

public class SimpleOpenAiClassifier extends SimpleClassifier {
    public SimpleOpenAiClassifier(Configuration.ModuleConfiguration configuration) {
        super(configuration, configuration.argumentAsString("model", "gpt-4o-mini"));
    }

    public SimpleOpenAiClassifier(Cache cache, String model, ChatLanguageModel llm, String template) {
        super(cache, model, llm, template);
    }

    @Override
    protected ChatLanguageModel createChatModel(String model) {
        return ChatModels.createOpenAiChatModel(model);
    }

    @Override
    protected SimpleClassifier copyOf(Cache cache, String model, ChatLanguageModel llm, String template) {
        return new SimpleOpenAiClassifier(cache, model, llm, template);
    }
}
