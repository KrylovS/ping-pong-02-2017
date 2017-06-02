package sample.services.game;


import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.GameWorldState;
import gameLogic.event_system.messages.PlatformState;
import gameLogic.event_system.messages.PlatformStateUpdate;
import gameLogic.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.websocket.GameSocketService;
import sample.websocket.Message;

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

    public void addPlatformSetTask(int gameId, int userIndex, PlatformState platformState) {
        userInputService.submit(
                () -> gameMap
                        .get(gameId)
//                        .alterPast(userIndex, platformState)
                        .getPlatformByIndex(userIndex)
                        .setState(platformState.getDiscreteRotation(userIndex, GameConfig.PLAYERS_NUM))
        );
    }

    public void addPlatformUpdateTask(int gameId, int userIndex, PlatformStateUpdate platformStateUpdate) {
        userInputService.submit(
                () -> gameMap
                        .get(gameId)
                        .getPlatformByIndex(userIndex)
                        .applyUpdate(platformStateUpdate.getDiscreteRotation(userIndex, GameConfig.PLAYERS_NUM))
        );
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
        final Message<GameWorldState> message = game.getWorldStateMessage();
        game.saveMessage(message);
        final List<Message<GameWorldState>> messageList = game.getMessageList(message);

        gameSocketService.updateGamePartyState(game.getId(), messageList);
    }

}
