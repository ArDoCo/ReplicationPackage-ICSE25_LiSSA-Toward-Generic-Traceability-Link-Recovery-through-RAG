package lissa.ratlr.embeddingcreator;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import lissa.ratlr.Configuration;
import lissa.ratlr.Environment;
import okhttp3.Credentials;

import java.time.Duration;
import java.util.Map;

public class OllamaEmbeddingCreator extends CachedEmbeddingCreator {

    public OllamaEmbeddingCreator(Configuration.ModuleConfiguration configuration) {
        super(configuration.argumentAsString("model", "nomic-embed-text:v1.5"));
    }

    @Override
    protected EmbeddingModel createEmbeddingModel(String model) {
        String host = Environment.getenvNonNull("OLLAMA_EMBEDDING_HOST");
        String user = Environment.getenv("OLLAMA_EMBEDDING_USER");
        String password = Environment.getenv("OLLAMA_EMBEDDING_PASSWORD");

        var ollamaEmbedding = new OllamaEmbeddingModel.OllamaEmbeddingModelBuilder().baseUrl(host).modelName(model).timeout(Duration.ofMinutes(5));
        if (user != null && password != null && !user.isEmpty() && !password.isEmpty()) {
            ollamaEmbedding.customHeaders(Map.of("Authorization", Credentials.basic(user, password)));
        }
        return ollamaEmbedding.build();
    }
}
