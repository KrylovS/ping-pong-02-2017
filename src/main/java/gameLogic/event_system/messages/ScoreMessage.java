package gameLogic.event_system.messages;


public class ScoreMessage {
    private String email;
    private boolean isVictory;

    public ScoreMessage(String email, boolean isVictory) {
        this.email = email;
        this.isVictory = isVictory;
    }

    public String getEmail() {
        return email;
    }

    public boolean isVictory() {
        return isVictory;
    }
}
