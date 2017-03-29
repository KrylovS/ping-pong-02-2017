package gameLogic.game.events;


public class SectorCollisionEvent {
    private double platformAngle;

    public double getPlatformAngle() {
        return platformAngle;
    }

    public void setPlatformAngle(double platformAngle) {
        this.platformAngle = platformAngle;
    }
}
