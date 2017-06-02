package gameLogic.event_system.messages;


import com.fasterxml.jackson.annotation.JsonGetter;

public class SectorCollision {
    private int userIndex;
    private int gameId;

    private boolean victory;

    public SectorCollision(int index, int gameId, boolean victory) {
        this.userIndex = index;
        this.gameId = gameId;
        this.victory = victory;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public int getGameId() {
        return gameId;
    }

    @JsonGetter("victory")
    public boolean isVictory() {
        return victory;
    }
}
