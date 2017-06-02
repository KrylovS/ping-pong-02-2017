package gameLogic.event_system.messages;


import com.fasterxml.jackson.annotation.JsonGetter;

public class GameTermination {
    private boolean success;
    private int userIndex;
    private long timestamp;

    public GameTermination(boolean isSuccess, int userIndex, long timestamp) {
        this.success = isSuccess;
        this.userIndex = userIndex;
        this.timestamp = timestamp;
    }

    @JsonGetter("success")
    public boolean isSuccess() {
        return success;
    }

    @JsonGetter("userIndex")
    public int getUserIndex() {
        return userIndex;
    }

    @JsonGetter("timestamp")
    public long getTimestamp() {
        return this.timestamp;
    }

}
