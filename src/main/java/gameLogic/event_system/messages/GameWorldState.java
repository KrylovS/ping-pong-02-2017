package gameLogic.event_system.messages;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gameLogic.common.CommonFunctions;
import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;

import java.util.List;
import java.util.stream.Collectors;

public class GameWorldState implements DiscreteRotationInvariant<GameWorldState> {
    private BallState ballState;
    private List<PlatformState> platformsState;

    @JsonCreator
    public GameWorldState(@JsonProperty("ballState") BallState ballState, @JsonProperty("platformsState") List<PlatformState> platformsState) {
        this.ballState = ballState;
        this.platformsState = platformsState;
    }

    public BallState getBallState() {
        return ballState;
    }

    public List<PlatformState> getPlatformsState() {
        return platformsState;
    }

    @Override
    public GameWorldState getDiscreteRotation(int stepNum, int totalSteps) {
        final double rotationAngle = 2 * Math.PI / totalSteps * stepNum;
        final BallState newBallState = ballState.rotate(rotationAngle);
        final List<PlatformState> newPlatformsState = CommonFunctions.getCircularTransposition(platformsState).stream().
                map(state -> state.getDiscreteRotation(stepNum, totalSteps))
                .collect(Collectors.toList());

        return new GameWorldState(
                newBallState,
                newPlatformsState
        );
    }
}
