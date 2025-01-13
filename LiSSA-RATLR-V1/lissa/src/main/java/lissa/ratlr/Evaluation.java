package lissa.ratlr;

import com.fasterxml.jackson.databind.ObjectMapper;
import lissa.ratlr.artifactprovider.ArtifactProvider;
import lissa.ratlr.classifier.Classifier;
import lissa.ratlr.elementstore.ElementStore;
import lissa.ratlr.embeddingcreator.EmbeddingCreator;
import lissa.ratlr.knowledge.TraceLink;
import lissa.ratlr.postprocessor.TraceLinkIdPostprocessor;
import lissa.ratlr.preprocessor.Preprocessor;
import lissa.ratlr.resultaggregator.ResultAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class Evaluation {

    private static final Logger logger = LoggerFactory.getLogger(Evaluation.class);
    private final Path groundTruth;
    private final boolean hasHeader;
    private final Path config;

    public Evaluation(Path groundTruth, boolean hasHeader, Path config) {
        this.groundTruth = groundTruth;
        this.hasHeader = hasHeader;
        this.config = config;
    }

    public void run() throws IOException {
        Configuration configuration = new ObjectMapper().readValue(config.toFile(), Configuration.class);

        ArtifactProvider sourceArtifactProvider = ArtifactProvider.createArtifactProvider(configuration.sourceArtifactProvider());
        ArtifactProvider targetArtifactProvider = ArtifactProvider.createArtifactProvider(configuration.targetArtifactProvider());

        Preprocessor sourcePreprocessor = Preprocessor.createPreprocessor(configuration.sourcePreprocessor());
        Preprocessor targetPreprocessor = Preprocessor.createPreprocessor(configuration.targetPreprocessor());

        EmbeddingCreator embeddingCreator = EmbeddingCreator.createEmbeddingCreator(configuration.embeddingCreator());
        ElementStore sourceStore = new ElementStore(configuration.sourceStore(), false);
        ElementStore targetStore = new ElementStore(configuration.targetStore(), true);

        Classifier classifier = Classifier.createClassifier(configuration.classifier());
        ResultAggregator aggregator = ResultAggregator.createResultAggregator(configuration.resultAggregator());

        TraceLinkIdPostprocessor traceLinkIdPostProcessor = TraceLinkIdPostprocessor.createTraceLinkIdPostprocessor(configuration.traceLinkIdPostprocessor());

        configuration.serializeAndDestroyConfiguration();

        // RUN
        logger.info("Loading artifacts");
        var sourceArtifacts = sourceArtifactProvider.getArtifacts();
        var targetArtifacts = targetArtifactProvider.getArtifacts();

        logger.info("Preprocessing artifacts");
        var sourceElements = sourcePreprocessor.preprocess(sourceArtifacts);
        var targetElements = targetPreprocessor.preprocess(targetArtifacts);

        logger.info("Calculating embeddings");
        var sourceEmbeddings = embeddingCreator.calculateEmbeddings(sourceElements);
        var targetEmbeddings = embeddingCreator.calculateEmbeddings(targetElements);

        logger.info("Building element stores");
        sourceStore.setup(sourceElements, sourceEmbeddings);
        targetStore.setup(targetElements, targetEmbeddings);

        logger.info("Classifying Tracelinks");
        var llmResults = classifier.classify(sourceStore, targetStore);
        var traceLinks = aggregator.aggregate(sourceElements, targetElements, llmResults);

        logger.info("Postprocessing Tracelinks");
        traceLinks = traceLinkIdPostProcessor.postprocess(traceLinks);

        logger.info("Evaluating Results");
        generateStatistics(traceLinks, configuration);
    }

    private void generateStatistics(Set<TraceLink> traceLinks, Configuration configuration) throws IOException {
        logger.info("Skipping header: {}", hasHeader);
        Set<TraceLink> validTraceLinks = Files.readAllLines(groundTruth)
                .stream()
                .skip(hasHeader ? 1 : 0)
                .map(l -> l.split(","))
                .map(it -> new TraceLink(it[0], it[1]))
                .collect(Collectors.toSet());
        logger.info("Valid TraceLinks: {}", validTraceLinks.size());
        logger.info("Found TraceLinks: {}", traceLinks.size());

        Set<TraceLink> truePositives = traceLinks.stream().filter(validTraceLinks::contains).collect(Collectors.toSet());
        Set<TraceLink> falsePositives = traceLinks.stream().filter(it -> !validTraceLinks.contains(it)).collect(Collectors.toSet());
        Set<TraceLink> falseNegatives = validTraceLinks.stream().filter(it -> !traceLinks.contains(it)).collect(Collectors.toSet());

        logger.info("True Positives: {}", truePositives.size());
        logger.info("False Positives: {}", falsePositives.size());
        logger.info("False Negatives: {}", falseNegatives.size());

        double precision = (double) truePositives.size() / (truePositives.size() + falsePositives.size());
        double recall = (double) truePositives.size() / (truePositives.size() + falseNegatives.size());
        double f1 = 2 * precision * recall / (precision + recall);
        logger.info("Precision: {}", precision);
        logger.info("Recall: {}", recall);
        logger.info("F1: {}", f1);

        // Store information to one file (config and results)
        var resultFile = new File("results-" + configuration.traceLinkIdPostprocessor().name() + "-" + UUID.nameUUIDFromBytes(configuration.toString()
                .getBytes(StandardCharsets.UTF_8)) + ".md");
        logger.info("Storing results to " + resultFile.getName());
        Files.writeString(resultFile.toPath(), "## Configuration\n```json\n" + configuration.serializeAndDestroyConfiguration() + "\n```\n\n");
        Files.writeString(resultFile.toPath(), "## Results\n", StandardOpenOption.APPEND);
        Files.writeString(resultFile.toPath(), "* True Positives: " + truePositives.size() + "\n", StandardOpenOption.APPEND);
        Files.writeString(resultFile.toPath(), "* False Positives: " + falsePositives.size() + "\n", StandardOpenOption.APPEND);
        Files.writeString(resultFile.toPath(), "* False Negatives: " + falseNegatives.size() + "\n", StandardOpenOption.APPEND);
        Files.writeString(resultFile.toPath(), "* Precision: " + precision + "\n", StandardOpenOption.APPEND);
        Files.writeString(resultFile.toPath(), "* Recall: " + recall + "\n", StandardOpenOption.APPEND);
        Files.writeString(resultFile.toPath(), "* F1: " + f1 + "\n", StandardOpenOption.APPEND);
    }
}
