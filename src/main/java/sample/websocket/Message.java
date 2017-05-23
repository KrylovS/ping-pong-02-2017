package sample.websocket;

import org.jetbrains.annotations.NotNull;

/**
 * Created by sergey on 22.04.17.
 */
@SuppressWarnings("NullableProblems")
public class Message {
    @NotNull
    private String type;
    @NotNull
    private String content;

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public String getContent() {
        return content;
    }

    public Message() {
    }

    public Message(@NotNull String type, @NotNull String content) {
        this.type = type;
        this.content = content;
    }

    public Message(@NotNull Class clazz, @NotNull String content) {
        this(clazz.getName(), content);
    }

    public String getStingMessage() {
        return "\"type\": \"" + this.getType() + "\", " + "\"content\":\""   + this.getContent() + "\"";
    }
}

