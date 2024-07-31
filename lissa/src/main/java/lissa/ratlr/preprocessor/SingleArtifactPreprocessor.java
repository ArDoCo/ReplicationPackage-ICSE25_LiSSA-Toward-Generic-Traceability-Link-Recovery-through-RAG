package lissa.ratlr.preprocessor;

import lissa.ratlr.knowledge.Artifact;
import lissa.ratlr.knowledge.Element;

import java.util.List;

public class SingleArtifactPreprocessor extends Preprocessor {
    @Override
    public List<Element> preprocess(List<Artifact> artifacts) {
        return artifacts.stream().map(artifact -> new Element(artifact.getIdentifier(), artifact.getType(), artifact.getContent(), 0, null, true)).toList();
    }
}
