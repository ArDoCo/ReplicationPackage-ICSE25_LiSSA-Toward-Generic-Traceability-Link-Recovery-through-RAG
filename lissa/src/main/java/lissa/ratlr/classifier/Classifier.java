package lissa.ratlr.classifier;

import lissa.ratlr.Configuration;
import lissa.ratlr.elementstore.ElementStore;
import lissa.ratlr.knowledge.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class Classifier {
    private static final int THREADS = 100;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<ClassificationResult> classify(ElementStore sourceStore, ElementStore targetStore) {
        List<Future<ClassificationResult>> futureResults = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);
        for (var query : sourceStore.getAllElements(true)) {
            var targetCandidates = targetStore.findSimilar(query.second());
            var futureResult = executor.submit(() -> copyOf().classify(query.first(), targetCandidates));
            futureResults.add(futureResult);
        }

        logger.info("Waiting for classification to finish. Elements in queue: {}", futureResults.size());

        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        return futureResults.stream().map(Future::resultNow).toList();
    }

    protected abstract ClassificationResult classify(Element source, List<Element> targets);

    protected abstract Classifier copyOf();

    public static Classifier createClassifier(Configuration.ModuleConfiguration configuration) {
        return switch (configuration.name()) {
        case "mock" -> new MockClassifier();
        case "simple_ollama" -> new SimpleOllamaClassifier(configuration);
        case "simple_openai" -> new SimpleOpenAiClassifier(configuration);
        case "reasoning_ollama" -> new ReasoningOllamaClassifier(configuration);
        case "reasoning_openai" -> new ReasoningOpenAiClassifier(configuration);
        default -> throw new IllegalStateException("Unexpected value: " + configuration.name());
        };
    }

    public record ClassificationResult(Element source, List<Element> targets) {
    }
}
