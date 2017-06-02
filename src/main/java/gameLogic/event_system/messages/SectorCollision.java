package gameLogic.event_system.messages;


public class SectorCollision {
    private int userIndex;
    private int gameId;

    public SectorCollision(int index, int gameId) {
        this.userIndex = index;
        this.gameId = gameId;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public int getGameId() {
        return gameId;
    }
}
