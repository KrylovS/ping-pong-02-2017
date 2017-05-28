package sample.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("NullableProblems")
public class Message<T> {
    @NotNull
    private String type;
    @NotNull
    private T content;

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public T getContent() {
        return content;
    }

    public Message() {
    }

    public Message(@NotNull @JsonProperty("type") String type, @NotNull @JsonProperty("content") T content) {
        this.type = type;
        this.content = content;
    }

    /*
    public Message(@NotNull Class clazz, @NotNull String content) {
        this(clazz.getName(), content);
    }
    */

    public String getStingMessage() {
        return "\"type\": \"" + this.getType() + "\", " + "\"content\":\""   + this.getContent() + "\"";
    }
}

