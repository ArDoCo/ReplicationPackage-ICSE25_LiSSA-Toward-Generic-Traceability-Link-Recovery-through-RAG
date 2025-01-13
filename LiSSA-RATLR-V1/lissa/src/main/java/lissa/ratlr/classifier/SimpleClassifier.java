package lissa.ratlr.classifier;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lissa.ratlr.Configuration;
import lissa.ratlr.cache.Cache;
import lissa.ratlr.cache.CacheManager;
import lissa.ratlr.knowledge.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class SimpleClassifier extends Classifier {

    private static final String DEFAULT_TEMPLATE = """
            Question: Here are two parts of software development artifacts. \n
            {source_type}: '''{source_content}''' \n
            {target_type}: '''{target_content}'''
            Are they related? \n
            Answer with 'yes' or 'no'.
            """;

    private final Cache cache;
    private final ChatLanguageModel llm;
    private final String template;
    private final String model;

    protected SimpleClassifier(Configuration.ModuleConfiguration configuration, String model) {
        this.template = configuration.argumentAsString("template", DEFAULT_TEMPLATE);
        this.cache = CacheManager.getDefaultInstance().getCache(this.getClass().getSimpleName() + "_" + model);
        this.model = model;
        this.llm = createChatModel(model);
    }

    protected SimpleClassifier(Cache cache, String model, ChatLanguageModel llm, String template) {
        this.cache = cache;
        this.model = model;
        this.llm = llm;
        this.template = template;
    }

    @Override
    protected final Classifier copyOf() {
        return copyOf(cache, model, createChatModel(model), template);
    }

    protected abstract SimpleClassifier copyOf(Cache cache, String model, ChatLanguageModel llm, String template);

    protected abstract ChatLanguageModel createChatModel(String model);

    @Override
    protected final ClassificationResult classify(Element source, List<Element> targets) {
        List<Element> relatedTargets = new ArrayList<>();

        for (var target : targets) {
            String llmResponse = classify(source, target);
            boolean isRelated = llmResponse.toLowerCase().contains("yes");
            if (isRelated) {
                relatedTargets.add(target);
            }
        }
        return new ClassificationResult(source, relatedTargets);
    }

    private String classify(Element source, Element target) {
        String request = template.replace("{source_type}", source.getType())
                .replace("{source_content}", source.getContent())
                .replace("{target_type}", target.getType())
                .replace("{target_content}", target.getContent());

        String key = UUID.nameUUIDFromBytes(request.getBytes()).toString();
        String cachedResponse = cache.get(key, String.class);
        if (cachedResponse != null) {
            return cachedResponse;
        } else {
            logger.info("Classifying: {} and {}", source.getIdentifier(), target.getIdentifier());
            String response = llm.generate(request);
            cache.put(key, response);
            return response;
        }
    }

}
