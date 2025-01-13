package edu.kit.kastel.sdq.lissa.ratlr.embeddingcreator;

import java.util.List;

import edu.kit.kastel.sdq.lissa.ratlr.Configuration;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.Element;

public abstract class EmbeddingCreator {
    public float[] calculateEmbedding(Element element) {
        return calculateEmbeddings(List.of(element)).getFirst();
    }

    public abstract List<float[]> calculateEmbeddings(List<Element> elements);

    public static EmbeddingCreator createEmbeddingCreator(Configuration.ModuleConfiguration configuration) {
        return switch (configuration.name()) {
            case "ollama" -> new OllamaEmbeddingCreator(configuration);
            case "openai" -> new OpenAiEmbeddingCreator(configuration);
            case "onnx" -> new OnnxEmbeddingCreator(configuration);
            default -> throw new IllegalStateException("Unexpected value: " + configuration.name());
        };
    }
}
