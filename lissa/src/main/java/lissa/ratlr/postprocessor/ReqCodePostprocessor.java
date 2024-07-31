package lissa.ratlr.postprocessor;

import lissa.ratlr.knowledge.TraceLink;

import java.util.LinkedHashSet;
import java.util.Set;

public class ReqCodePostprocessor extends TraceLinkIdPostprocessor {
    @Override
    public Set<TraceLink> postprocess(Set<TraceLink> traceLinks) {
        // TraceLink[sourceId=UC10E1.txt, targetId=BeanValidator.java]
        // => TraceLink[sourceId=UC10E1, targetId=BeanValidator]
        Set<TraceLink> result = new LinkedHashSet<>();
        for (TraceLink traceLink : traceLinks) {
            String sourceId = traceLink.sourceId();
            String targetId = traceLink.targetId();
            sourceId = sourceId.substring(0, sourceId.indexOf("."));
            targetId = targetId.substring(0, targetId.indexOf("."));
            result.add(new TraceLink(sourceId, targetId));
        }
        return result;
    }
}
