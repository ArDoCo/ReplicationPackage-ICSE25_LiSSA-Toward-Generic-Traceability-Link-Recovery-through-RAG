package edu.kit.kastel.sdq.lissa.ratlr.postprocessor;

import java.util.HashSet;
import java.util.Set;

import edu.kit.kastel.sdq.lissa.ratlr.Configuration;
import edu.kit.kastel.sdq.lissa.ratlr.knowledge.TraceLink;

public class IdentityPostprocessor extends TraceLinkIdPostprocessor {

    private final boolean reverse;

    public IdentityPostprocessor(Configuration.ModuleConfiguration moduleConfiguration) {
        this.reverse = moduleConfiguration.argumentAsBoolean("reverse", false);
    }

    @Override
    public Set<TraceLink> postprocess(Set<TraceLink> traceLinks) {
        if (reverse) {
            Set<TraceLink> resultLinks = new HashSet<>();
            for (TraceLink traceLink : traceLinks) {
                resultLinks.add(new TraceLink(traceLink.targetId(), traceLink.sourceId()));
            }
            return resultLinks;
        }
        return traceLinks;
    }
}
