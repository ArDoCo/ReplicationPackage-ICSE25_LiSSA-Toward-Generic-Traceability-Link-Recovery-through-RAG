package lissa.ratlr.resultaggregator;

import lissa.ratlr.Configuration;
import lissa.ratlr.classifier.Classifier;
import lissa.ratlr.knowledge.Element;
import lissa.ratlr.knowledge.TraceLink;

import java.util.List;
import java.util.Set;

public abstract class ResultAggregator {
    public abstract Set<TraceLink> aggregate(List<Element> sourceElements, List<Element> targetElements,
            List<Classifier.ClassificationResult> classificationResults);

    public static ResultAggregator createResultAggregator(Configuration.ModuleConfiguration configuration) {
        return switch (configuration.name()) {
        case "any_connection" -> new AnyResultAggregator(configuration);
        default -> throw new IllegalStateException("Unexpected value: " + configuration.name());
        };
    }
}
