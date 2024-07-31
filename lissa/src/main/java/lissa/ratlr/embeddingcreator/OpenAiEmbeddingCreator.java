package lissa.ratlr.embeddingcreator;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import lissa.ratlr.Configuration;
import lissa.ratlr.Environment;

public class OpenAiEmbeddingCreator extends CachedEmbeddingCreator {

    public OpenAiEmbeddingCreator(Configuration.ModuleConfiguration configuration) {
        super(configuration.argumentAsString("model", "text-embedding-ada-002"));
    }

    @Override
    protected EmbeddingModel createEmbeddingModel(String model) {
        String openAiOrganizationId = Environment.getenv("OPENAI_ORGANIZATION_ID");
        String openAiApiKey = Environment.getenv("OPENAI_API_KEY");
        if (openAiOrganizationId == null || openAiApiKey == null) {
            throw new IllegalStateException("OPENAI_ORGANIZATION_ID or OPENAI_API_KEY environment variable not set");
        }
        return new OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder().modelName(model)
                .organizationId(openAiOrganizationId)
                .apiKey(openAiApiKey)
                .maxRetries(0)
                .build();
    }
}
