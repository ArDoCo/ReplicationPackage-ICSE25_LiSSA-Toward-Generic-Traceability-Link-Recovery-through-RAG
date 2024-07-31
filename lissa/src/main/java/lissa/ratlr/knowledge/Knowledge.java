package lissa.ratlr.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Artifact.class, name = "artifact"), @JsonSubTypes.Type(value = Element.class, name = "element") })
public abstract sealed class Knowledge permits Artifact, Element {
    @JsonProperty
    private final String identifier;
    @JsonProperty
    private final String type;
    @JsonProperty
    private final String content;

    protected Knowledge(String identifier, String type, String content) {
        this.identifier = identifier;
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }
}
