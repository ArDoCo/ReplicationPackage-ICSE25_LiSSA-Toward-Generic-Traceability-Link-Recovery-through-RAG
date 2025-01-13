package lissa.ratlr.postprocessor;

import lissa.ratlr.Configuration;
import lissa.ratlr.knowledge.TraceLink;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.function.Function;

public class TraceLinkIdPostprocessor {
    private final IdProcessor idProcessor;

    private TraceLinkIdPostprocessor(IdProcessor idProcessor) {
        this.idProcessor = idProcessor;
    }

    protected TraceLinkIdPostprocessor() {
        this.idProcessor = null;
    }

    public static TraceLinkIdPostprocessor createTraceLinkIdPostprocessor(Configuration.ModuleConfiguration moduleConfiguration) {
        return switch (moduleConfiguration.name()) {
            case "req2code" -> new TraceLinkIdPostprocessor(IdProcessor.REQ2CODE);
            case "sad2code" -> new TraceLinkIdPostprocessor(IdProcessor.SAD2CODE);
            case "sad2sam" -> new TraceLinkIdPostprocessor(IdProcessor.SAD2SAM);
            case "sam2sad" -> new TraceLinkIdPostprocessor(IdProcessor.SAM2SAD);
            case "sam2code" -> new TraceLinkIdPostprocessor(IdProcessor.SAM2CODE);
            case "identity" -> new IdentityPostprocessor();
            case null -> new IdentityPostprocessor();
            default -> throw new IllegalStateException("Unexpected value: " + moduleConfiguration.name());
        };
    }

    public Set<TraceLink> postprocess(Set<TraceLink> traceLinks) {
        if (idProcessor == null) {
            throw new IllegalStateException("idProcessor not set or method not overridden");
        }
        Set<TraceLink> result = new LinkedHashSet<>();
        for (TraceLink traceLink : traceLinks) {
            result.add(idProcessor.process(traceLink));
        }
        return result;
    }

    private enum IdProcessor {
        REQ2CODE(sourceId -> sourceId.substring(0, sourceId.indexOf(".")), targetId -> targetId.substring(0, targetId.indexOf("."))),
        SAD2CODE(TraceLinkIdPostprocessor::processSAD, targetId -> targetId),
        SAM2SAD(TraceLinkIdPostprocessor::processSAM, TraceLinkIdPostprocessor::processSAD),
        SAD2SAM(TraceLinkIdPostprocessor::processSAD, TraceLinkIdPostprocessor::processSAM),
        SAM2CODE(TraceLinkIdPostprocessor::processSAM, targetId -> targetId);

        private final Function<String, String> sourceIdProcessor;
        private final Function<String, String> targetIdProcessor;

        IdProcessor(Function<String, String> sourceIdProcessor, Function<String, String> targetIdProcessor) {
            this.sourceIdProcessor = sourceIdProcessor;
            this.targetIdProcessor = targetIdProcessor;
        }

        public TraceLink process(TraceLink traceLink) {
            return new TraceLink(sourceIdProcessor.apply(traceLink.sourceId()), targetIdProcessor.apply(traceLink.targetId()));
        }
    }

    private static String processSAD(String sadID) {
        return String.valueOf(Integer.parseInt(sadID.substring(sadID.lastIndexOf("$") + 1)) + 1);
    }

    private static String processSAM(String samID) {
        return samID.substring(samID.lastIndexOf("$") + 1);
    }
}
