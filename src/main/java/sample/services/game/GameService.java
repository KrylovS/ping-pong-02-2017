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
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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

    public void play(int gameIndex) {
        this.gameMap.get(gameIndex).play();
    }

    public void pause(int gameIndex) {
        this.gameMap.get(gameIndex).pause();
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
        final GameWorldState initialState = game.getWorldState();
        
        return IntStream.range(0, game.getUserNum()).boxed()
                .map(i -> initialState.getDiscreteRotation(i, game.getUserNum()))
                .collect(Collectors.toList());
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
        if (game.isRunning() && updateTimeLeft >= GameConfig.RENDER_TIME) {
            game.makeIteration(updateTimeLeft);
        }
    }

    private void transmitState(double transmitTimeLeft, Game game) {
        if (game.isRunning() && transmitTimeLeft >= GameConfig.WS_TIME) {
            gameSocketService.updateGamePartyState(game.getId(), getGameStateList(game.getId()));
        }
    }

}
