package lissa.ratlr.preprocessor;

import dev.langchain4j.data.document.splitter.DocumentBySentenceSplitter;
import lissa.ratlr.Configuration;
import lissa.ratlr.knowledge.Artifact;
import lissa.ratlr.knowledge.Element;

import java.util.ArrayList;
import java.util.List;

public class SentencePreprocessor extends Preprocessor {
    public SentencePreprocessor(Configuration.ModuleConfiguration configuration) {
    }

    @Override
    public List<Element> preprocess(List<Artifact> artifacts) {
        List<Element> elements = new ArrayList<>();
        for (Artifact artifact : artifacts) {
            List<Element> preprocessed = preprocessIntern(artifact);
            elements.addAll(preprocessed);
        }
        return elements;
    }

    private List<Element> preprocessIntern(Artifact artifact) {
        DocumentBySentenceSplitter splitter = new DocumentBySentenceSplitter(Integer.MAX_VALUE, 0);
        String[] sentences = splitter.split(artifact.getContent());
        List<Element> elements = new ArrayList<>();

        Element artifactAsElement = new Element(artifact.getIdentifier(), artifact.getType(), artifact.getContent(), 0, null, false);
        elements.add(artifactAsElement);

        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i];
            Element sentenceAsElement = new Element(artifact.getIdentifier() + SEPARATOR + i, artifact.getType(), sentence, 1, artifactAsElement, true);
            elements.add(sentenceAsElement);
        }
        return elements;
    }
}
