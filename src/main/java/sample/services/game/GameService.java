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
    private ExecutorService renderLoopService = Executors.newFixedThreadPool(1); // TODO move thread number to config
    private ExecutorService partyProgressService = Executors.newFixedThreadPool(6); // TODO move thread number to config
    private ExecutorService stateTransmissionService = Executors.newFixedThreadPool(3); // TODO move thread number to config
    private ExecutorService userInputService = Executors.newFixedThreadPool(3); // TODO move thread number to config
    private AtomicInteger gameCounter;

    public GameService() {
        init();
    }

    public void init() {
        gameMap = new ConcurrentHashMap<>();
        gameCounter = new AtomicInteger();
        renderLoopService.submit(this::renderLoop);
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
                .map(i -> {
                    final GameWorldState localState = initialState.getDiscreteRotation(-i, game.getUserNum());
                    localState.getPlatformsState().forEach(platformState -> platformState.setActive(false));
                    localState.getPlatformsState().get(0).setActive(true);

                    return localState;
                })
                .collect(Collectors.toList());
    }

    public void addUserTask(int gameId, int userIndex, PlatformState platformState) {
        userInputService.submit(() -> gameMap
                .get(gameId)
                .getPlatformByIndex(userIndex)
                .setState(platformState.getDiscreteRotation(userIndex, GameConfig.PLAYERS_NUM)));
    }

    private void renderLoop() {
        while (true) {
            gameMap.values().forEach(
                game -> {
                    if (game.isRunning()) {
                        final long updateTimeLeft = System.currentTimeMillis() - game.getLastUpdateTime();
                        if (updateTimeLeft >= GameConfig.RENDER_TIME) {
                            game.resetLastUpdateTime();
                            partyProgressService.submit(() -> updateGame(updateTimeLeft, game));
                        }

                        final long transmitTimeLeft = System.currentTimeMillis() - game.getLastTransmitTime();
                        if (transmitTimeLeft >= GameConfig.WS_TIME) {
                            game.resetLastTransmitTime();
                            stateTransmissionService.submit(() -> transmitState(game));
                        }
                    }
                }
            );
        }
    }

    private void updateGame(double updateTimeLeft, Game game) {
        game.makeIteration(updateTimeLeft);
    }

    private void transmitState(Game game) {
        gameSocketService.updateGamePartyState(game.getId(), getGameStateList(game.getId()));
    }

}
