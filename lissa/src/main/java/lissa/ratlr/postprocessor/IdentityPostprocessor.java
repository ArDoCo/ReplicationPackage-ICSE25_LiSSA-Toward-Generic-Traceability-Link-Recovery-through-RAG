package lissa.ratlr.postprocessor;

import lissa.ratlr.knowledge.TraceLink;

import java.util.Set;

public class IdentityPostprocessor extends TraceLinkIdPostprocessor {
    @Override
    public Set<TraceLink> postprocess(Set<TraceLink> traceLinks) {
        return traceLinks;
    }
}
