package live.hi.services.ms365connector.dto.msgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Body {
    private String contentType;
    private String content;

    public Body(@JsonProperty("contentType") String contentType,
                @JsonProperty("content") String content) {
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Body{" +
                "contentType='" + contentType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
