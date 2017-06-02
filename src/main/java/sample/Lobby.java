package sample;




import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.PlayerAnnouncement;
import gameLogic.event_system.messages.ScoreMessage;
import gameLogic.event_system.messages.SectorCollision;
import gameLogic.game.EventBus;
import gameLogic.game.Game;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.services.account.AccountServiceDB;
import sample.services.game.GameService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class Lobby {
    private AtomicInteger userIdCounter;
    private ConcurrentMap<Integer, BiMap<String, Integer>> userPartyMap;

    @Autowired
    GameService gameService;

    @Autowired
    AccountServiceDB accountServiceDB;

    public Lobby() {
        init();
        addListeners();
    }

    public void init() {
        userIdCounter = new AtomicInteger();
        userPartyMap = new ConcurrentHashMap<>();
    }

    public void addListeners() {
        EventBus.addEventListener(
                SectorCollision.class.getName(),
                event -> {
                    final SectorCollision realEvent = (SectorCollision) event;
                    this.handleSectorCollisionEvent(realEvent.getGameId(), realEvent.getUserIndex(), realEvent.isVictory());
                    return null;
                }
        );
    }

    public void reset() {
        gameService.init();
        init();
    }

    public List<PlayerAnnouncement> getCurrLobbyState(int gameIndex) {
        return userPartyMap.get(gameIndex).entrySet().stream()
                .map(entry -> new PlayerAnnouncement(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(PlayerAnnouncement::getPosition))
                .collect(Collectors.toList());

    }

    public List<String> getSortedPlayers(int gameIndex) {
        return userPartyMap.get(gameIndex).entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Integer getPlayerPartyId(String email, Integer partyId) {
        return userPartyMap.get(partyId).get(email);
    }

    public synchronized Integer addPlayer(String email) {
        final int nextGameId = gameService.getNextGameId();

        if (userPartyMap.containsKey(nextGameId)) {
            userPartyMap.get(nextGameId).put(
                    email,
                    getNewUserId()
            );

            if (userPartyMap.get(nextGameId).size() == GameConfig.PLAYERS_NUM) {
                gameService.addGame();
            }
        } else {
            final BiMap<String, Integer> newEntry = HashBiMap.create();
            newEntry.put(email, getNewUserId());
            userPartyMap.put(nextGameId, newEntry);
        }

        return nextGameId;
    }

    @Nullable
    public synchronized String removePlayer(int partyId, int playerId) {
        if (userPartyMap.containsKey(partyId) && userPartyMap.get(partyId).inverse().containsKey(playerId)) {
            final int playersNum = userPartyMap.get(partyId).size();
            final String email = userPartyMap.get(partyId).inverse().get(playerId);

            if (playersNum == 1) {
                userPartyMap.remove(partyId);
            } else {
                userPartyMap.get(partyId).inverse().remove(playerId);
            }

            return email;
        }

        return null;

    }

    private Integer getNewUserId() {
        return userIdCounter.getAndIncrement() % GameConfig.PLAYERS_NUM;
    }

    private void handleSectorCollisionEvent(int gameId, int userIndex, boolean isVictory) {
        final String email = userPartyMap.get(gameId).inverse().get(userIndex);
        EventBus.dispatchEvent(ScoreMessage.class.getName(), new ScoreMessage(email, isVictory));
    }
}
