package gameLogic.event_system.messages;

import gameLogic.common.CommonFunctions;
import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;


public class PlayerAnnouncement implements DiscreteRotationInvariant<PlayerAnnouncement> {
    private String nickname;
    private int position;

    public PlayerAnnouncement(String nickname, Integer position) {
        this.nickname = nickname;
        this.position = position;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public PlayerAnnouncement getDiscreteRotation(int stepNum, int totalSteps) {
        final int newPosition = CommonFunctions.getCircularOffset(position, stepNum, totalSteps);
        return new PlayerAnnouncement(nickname, newPosition);
    }

    @Override
    public String toString() {
        return "nickname: " + nickname + " position: " + position;
    }
}
