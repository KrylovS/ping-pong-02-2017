package sample.services.score;

import gameLogic.event_system.messages.ScoreMessage;
import gameLogic.event_system.messages.SectorCollision;
import gameLogic.game.EventBus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sergey on 01.06.17.
 */

@Service
public class ScoreService {
    private Map<String, Integer> sessionIdtoScore = new ConcurrentHashMap<>();
    private Map<String, String> emailToSessionId = new ConcurrentHashMap<>();

    public ScoreService() {
        addListeners();
    }

    public void addListeners() {
        EventBus.addEventListener(
                ScoreMessage.class.getName(),
                event -> {
                    final ScoreMessage realEvent = (ScoreMessage) event;
                    this.handleScoreMessage(realEvent.getEmail(), realEvent.isVictory());
                    return null;
                }
        );
    }

    public void addSession(String email, String sessionId) {
        emailToSessionId.put(email, sessionId);
    }

    public void addScore(String email, Integer score) {
        sessionIdtoScore.put(emailToSessionId.get(email), score);
    }

    public Integer getScoreBySession(String sessionId) {
        return sessionIdtoScore.get(sessionId);
    }

    public Integer getScoreByEmail(String email) {
        return sessionIdtoScore.get(emailToSessionId.get(email));
    }

    public void removeResultByEmail(String email) {
        sessionIdtoScore.remove(emailToSessionId.get(email));
        emailToSessionId.remove(email);
    }

    public void removeResultBySession(String sessionId) {
        sessionIdtoScore.remove(sessionId);
    }

    public void removeEmailToSessionId(String email) {
        emailToSessionId.remove(email);
    }



    private void handleScoreMessage(String email, boolean isVictory) {
        if (isVictory) {
            addScore(email, 1);
        } else {
            addScore(email, 0);
        }
    }
}
