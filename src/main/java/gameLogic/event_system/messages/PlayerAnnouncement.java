package gameLogic.event_system.messages;

import gameLogic.event_system.messages.interfaces.DiscreteRotationInvariant;

/**
 * Created by artem on 5/26/17.
 */
public class PlayerAnnouncement implements DiscreteRotationInvariant<PlayerAnnouncement> {
    private String nickname;
    private int position;

    public PlayerAnnouncement(String nickname, int position) {
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
        final int newPosition = (position + stepNum) % totalSteps;
        return new PlayerAnnouncement(nickname, newPosition);
    }
}
