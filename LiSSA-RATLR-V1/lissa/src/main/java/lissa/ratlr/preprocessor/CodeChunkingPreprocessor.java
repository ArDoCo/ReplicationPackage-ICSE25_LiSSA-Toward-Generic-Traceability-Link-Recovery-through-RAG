package lissa.ratlr.preprocessor;

import lissa.ratlr.Configuration;
import lissa.ratlr.knowledge.Artifact;
import lissa.ratlr.knowledge.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CodeChunkingPreprocessor extends Preprocessor {

    private final RecursiveSplitter.Language language;
    private final int chunkSize;

    public CodeChunkingPreprocessor(Configuration.ModuleConfiguration configuration) {
        this.language = Objects.requireNonNull(RecursiveSplitter.Language.valueOf(configuration.argumentAsString("language")));
        this.chunkSize = configuration.argumentAsInt("chunk_size", 60);
    }

    @Override
    public List<Element> preprocess(List<Artifact> artifacts) {
        List<Element> elements = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            List<Element> preprocessed = preprocess(artifact);
            elements.addAll(preprocessed);
        }
        return elements;
    }

    protected List<Element> preprocess(Artifact artifact) {
        List<String> segments = RecursiveSplitter.fromLanguage(language, chunkSize).splitText(artifact.getContent());
        List<Element> elements = new ArrayList<>();

        Element artifactAsElement = new Element(artifact.getIdentifier(), artifact.getType(), artifact.getContent(), 0, null, false);
        elements.add(artifactAsElement);

        for (int i = 0; i < segments.size(); i++) {
            String segment = segments.get(i);
            Element segmentAsElement = new Element(artifact.getIdentifier() + SEPARATOR + i, artifact.getType(), segment, 1, artifactAsElement, true);
            elements.add(segmentAsElement);
        }

        return elements;
    }
}
