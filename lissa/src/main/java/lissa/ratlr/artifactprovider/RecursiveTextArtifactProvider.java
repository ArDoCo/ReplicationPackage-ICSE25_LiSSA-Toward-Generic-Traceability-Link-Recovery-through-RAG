package lissa.ratlr.artifactprovider;

import lissa.ratlr.Configuration;
import lissa.ratlr.knowledge.Artifact;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class RecursiveTextArtifactProvider extends TextArtifactProvider {

    private final String[] extensions;

    public RecursiveTextArtifactProvider(Configuration.ModuleConfiguration configuration) {
        super(configuration);
        this.extensions = configuration.argumentAsString("extensions").toLowerCase().split(",");
    }

    @Override
    protected void loadFiles() {
        try {
            Files.walk(this.path.toPath()).forEach(it -> {
                if (Files.isRegularFile(it) && hasCorrectExtension(it)) {
                    try (Scanner scan = new Scanner(it.toFile()).useDelimiter("\\A")) {
                        if (scan.hasNext()) {
                            String content = scan.next();
                            var relativePath = this.path.toPath().relativize(it);
                            String pathWithDefinedSeparators = relativePath.toString().replace("\\", "/");
                            artifacts.add(new Artifact(pathWithDefinedSeparators, artifactType, content));
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean hasCorrectExtension(Path file) {
        String fileName = file.getFileName().toString().toLowerCase();
        for (String extension : extensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
