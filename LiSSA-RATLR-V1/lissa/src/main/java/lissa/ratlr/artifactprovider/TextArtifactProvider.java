package lissa.ratlr.artifactprovider;

import lissa.ratlr.Configuration;
import lissa.ratlr.knowledge.Artifact;
import lissa.ratlr.knowledge.Knowledge;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class TextArtifactProvider extends ArtifactProvider {

    private static final Logger logger = LoggerFactory.getLogger(TextArtifactProvider.class);

    protected final File path;
    protected final Artifact.ArtifactType artifactType;
    protected final List<Artifact> artifacts;

    public TextArtifactProvider(Configuration.ModuleConfiguration configuration) {
        this.path = new File(configuration.argumentAsString("path"));
        if (!path.exists()) {
            throw new IllegalArgumentException("Path does not exist: " + path.getAbsolutePath());
        }
        this.artifactType = Artifact.ArtifactType.from(configuration.argumentAsString("artifact_type"));
        this.artifacts = new ArrayList<>();
    }

    protected void loadFiles() {
        List<File> files = new ArrayList<>();
        if (this.path.isFile()) {
            files.add(this.path);
        } else {
            files.addAll(Arrays.asList(Objects.requireNonNull(this.path.listFiles())));
        }

        files.stream().map(File::toPath).forEach(it -> {
            if (Files.isRegularFile(it)) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try (BufferedReader reader = new BufferedReader(new FileReader(it.toFile()))) {
                    IOUtils.copy(reader, bos, StandardCharsets.UTF_8);
                    String content = bos.toString(StandardCharsets.UTF_8);
                    artifacts.add(new Artifact(it.getFileName().toString(), artifactType, content));
                } catch (IOException e) {
                    logger.error("{}: {}", e.getMessage(), it.toFile(), e);
                    throw new UncheckedIOException(e);
                }
            }
        });
    }

    @Override
    public List<Artifact> getArtifacts() {
        if (artifacts.isEmpty())
            this.loadFiles();
        var artifacts = new ArrayList<>(this.artifacts);
        artifacts.sort(Comparator.comparing(Knowledge::getIdentifier));
        return artifacts;
    }

    @Override
    public Artifact getArtifact(String identifier) {
        if (artifacts.isEmpty())
            this.loadFiles();
        return artifacts.stream()
                .filter(it -> it.getIdentifier().equals(identifier))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Artifact not found"));
    }
}
