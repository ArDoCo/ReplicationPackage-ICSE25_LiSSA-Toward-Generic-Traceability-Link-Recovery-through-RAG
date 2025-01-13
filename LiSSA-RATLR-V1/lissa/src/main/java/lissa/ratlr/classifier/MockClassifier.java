package lissa.ratlr.classifier;

import lissa.ratlr.knowledge.Element;

import java.util.List;

public class MockClassifier extends Classifier {
    @Override
    protected ClassificationResult classify(Element source, List<Element> targets) {
        return new ClassificationResult(source, targets);
    }

    @Override
    protected Classifier copyOf() {
        return new MockClassifier();
    }
}
