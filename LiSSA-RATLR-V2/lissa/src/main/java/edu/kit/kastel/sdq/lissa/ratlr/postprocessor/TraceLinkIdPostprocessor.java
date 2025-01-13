package edu.kit.kastel.sdq.lissa.ratlr.postprocessor;

import java.util.Set;

import edu.kit.kastel.sdq.lissa.ratlr.Configuration;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.TraceLink;

public abstract class TraceLinkIdPostprocessor {
    public static TraceLinkIdPostprocessor createTraceLinkIdPostprocessor(
            Configuration.ModuleConfiguration moduleConfiguration) {
        return switch (moduleConfiguration.name()) {
            case "req2code" -> new ReqCodePostprocessor();
            case "sad2code" -> new SadCodePostprocessor();
            case "req2req" -> new ReqReqPostprocessor();
            case "identity" -> new IdentityPostprocessor(moduleConfiguration);
            case null -> new IdentityPostprocessor(moduleConfiguration);
            default -> throw new IllegalStateException("Unexpected value: " + moduleConfiguration.name());
        };
    }

    public abstract Set<TraceLink> postprocess(Set<TraceLink> traceLinks);
}
