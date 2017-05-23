package gameLogic.event_system.messages;


import gameLogic.common.CommonFunctions;
import gameLogic.event_system.messages.interfaces.DescreteRotationInvariant;

import java.util.List;

public class GameWorldState implements DescreteRotationInvariant<GameWorldState> {
    private BallState ballState;
    private List<PlatformState> platformsState;

    public GameWorldState(BallState ballState, List<PlatformState> platformsState) {
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
    public GameWorldState getDiscreteRotation() {
        final int playerNum = platformsState.size();

        return new GameWorldState(
                ballState.rotate(2 * Math.PI / playerNum),
                CommonFunctions.getCircularTransposition(platformsState)
        );
    }
}
