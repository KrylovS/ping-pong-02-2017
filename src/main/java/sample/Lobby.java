package sample;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.PlayerAnnouncement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Lobby() {
        init();
    }

    public void init() {
        userIdCounter = new AtomicInteger();
        userPartyMap = new ConcurrentHashMap<>();
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

    public synchronized void removePlayer(int partyId, int playerId) {
        if (userPartyMap.containsKey(partyId) && userPartyMap.get(partyId).containsKey(playerId)) {

        }

    }

    private Integer getNewUserId() {
        return userIdCounter.getAndIncrement() % GameConfig.PLAYERS_NUM;
    }
}
