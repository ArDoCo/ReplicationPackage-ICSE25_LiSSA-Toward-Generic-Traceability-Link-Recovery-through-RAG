package lissa.ratlr.embeddingcreator;

import lissa.ratlr.Configuration;
import lissa.ratlr.knowledge.Element;

import java.util.List;

public abstract class EmbeddingCreator {
    public float[] calculateEmbedding(Element element) {
        return calculateEmbeddings(List.of(element)).getFirst();
    }

    public abstract List<float[]> calculateEmbeddings(List<Element> elements);

    public static EmbeddingCreator createEmbeddingCreator(Configuration.ModuleConfiguration configuration) {
        return switch (configuration.name()) {
        case "ollama" -> new OllamaEmbeddingCreator(configuration);
        case "openai" -> new OpenAiEmbeddingCreator(configuration);
        default -> throw new IllegalStateException("Unexpected value: " + configuration.name());
        };
    }
}
