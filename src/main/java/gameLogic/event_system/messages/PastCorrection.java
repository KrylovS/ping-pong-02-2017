package gameLogic.event_system.messages;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;

public class PastCorrection implements DiscreteRotationInvariant<PastCorrection> {
    private int lastMessageId;
    private int msElapsed;
    private PlatformState platformState;
    private BallState ballState;

    @JsonCreator
    public PastCorrection(
            @JsonProperty("lastMessageId") int lastMessageId,
            @JsonProperty("msElapsed") int msElapsed,
            @JsonProperty("ballState") BallState ballState,
            @JsonProperty("platformState") PlatformState platformState
    ) {
        this.lastMessageId = lastMessageId;
        this.msElapsed = msElapsed;
        this.platformState = platformState;
        this.ballState = ballState;
    }

    public int getLastMessageId() {
        return lastMessageId;
    }

    public int getMsElapsed() {
        return msElapsed;
    }

    public PlatformState getPlatformState() {
        return platformState;
    }

    public BallState getBallState() {
        return ballState;
    }

    @Override
    public PastCorrection getDiscreteRotation(int stepNum, int totalSteps) {
        return new PastCorrection(
                lastMessageId,
                msElapsed,
                ballState.getDiscreteRotation(stepNum, totalSteps),
                platformState.getDiscreteRotation(stepNum, totalSteps)
        );
    }
}

