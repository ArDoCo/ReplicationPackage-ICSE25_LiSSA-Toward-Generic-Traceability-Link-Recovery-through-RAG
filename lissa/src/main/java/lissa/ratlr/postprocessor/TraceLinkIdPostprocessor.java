package lissa.ratlr.postprocessor;

import lissa.ratlr.Configuration;
import lissa.ratlr.knowledge.TraceLink;

import java.util.Set;

public abstract class TraceLinkIdPostprocessor {
    public static TraceLinkIdPostprocessor createTraceLinkIdPostprocessor(Configuration.ModuleConfiguration moduleConfiguration) {
        return switch (moduleConfiguration.name()) {
        case "req2code" -> new ReqCodePostprocessor();
        case "sad2code" -> new SadCodePostprocessor();

        case "identity" -> new IdentityPostprocessor();
        case null -> new IdentityPostprocessor();
        default -> throw new IllegalStateException("Unexpected value: " + moduleConfiguration.name());
        };
    }

    public abstract Set<TraceLink> postprocess(Set<TraceLink> traceLinks);
}
