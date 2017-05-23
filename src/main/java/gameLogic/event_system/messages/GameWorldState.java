package gameLogic.event_system.messages;


import java.util.List;

public class GameWorldState {
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
}
