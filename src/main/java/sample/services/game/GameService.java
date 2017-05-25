package sample.services.game;


import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.GameWorldState;
import gameLogic.event_system.messages.PlatformState;
import gameLogic.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.websocket.GameSocketService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class GameService {
    @Autowired
    GameSocketService gameSocketService;

    private volatile ConcurrentMap<Integer, Game> gameMap;
    private ExecutorService executorService = Executors.newFixedThreadPool(10); // TODO move thread number to config
    private AtomicInteger gameCounter;

    public GameService() {
        init();
    }

    public void init() {
        gameMap = new ConcurrentHashMap<>();
        gameCounter = new AtomicInteger();
        executorService.submit(this::renderLoop);
    }

    public int getGamesNumber() {
        return gameMap.size();
    }

    public int getNextGameId() {
        return gameCounter.intValue() + 1;
    }

    public int addGame() {
        final int newId = gameCounter.incrementAndGet();
        final Game newGame = new Game(newId);
        gameMap.put(newId, newGame);

        return newId;
    }

    public List<GameWorldState> getGameStateList(int gameId) {
        final Game game = gameMap.get(gameId);

        final List<GameWorldState> stateList = new ArrayList<>();
        stateList.add(game.getWorldState());

        for (int i = 0; i != game.getUserNum() - 1; ++i) {
            stateList.add(stateList.get(stateList.size() - 1).getDiscreteRotation());
        }

        return stateList;
    }

    public void addUserTask(int gameId, int userIndex, PlatformState platformState) {
        executorService.submit(() -> gameMap.get(gameId).getPlatformByIndex(userIndex).setState(platformState));
    }

    private void renderLoop() {
        while (true) {
            gameMap.values().forEach(
                game -> {
                    final long timeLeft = System.currentTimeMillis() - game.getLastUpdateTime();

                    if (timeLeft >= GameConfig.RENDER_TIME) {
                        executorService.submit(() -> {
                            final long timestamp = System.currentTimeMillis();
                            final long updateTimeLeft = timestamp - game.getLastUpdateTime();
                            final long transmitTimeLeft = timestamp - game.getLastTransmitTime();

                            updateGame(updateTimeLeft, game);
                            transmitState(transmitTimeLeft, game);
                        });
                    }
                }
            );
        }
    }

    private void updateGame(double updateTimeLeft, Game game) {
        if (updateTimeLeft >= GameConfig.RENDER_TIME) {
            game.makeIteration(updateTimeLeft);
        }
    }

    private void transmitState(double transmitTimeLeft, Game game) {
        if (transmitTimeLeft >= GameConfig.WS_TIME) {
            gameSocketService.updateGamePartyState(game.getId(), getGameStateList(game.getId()));
        }
    }

}
