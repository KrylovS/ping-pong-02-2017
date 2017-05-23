package gameLogic.game;


import gameLogic.event_system.messages.GameWorldState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameService {
    private ConcurrentMap<Integer, Game> gameMap;
    private ExecutorService executorService = Executors.newFixedThreadPool(10); // TODO move thread number to config



    List<GameWorldState> getGameState(int gameId) {
        final Game game = gameMap.get(gameId);

        final List<GameWorldState> stateList = new ArrayList<>();
        stateList.add(game.getWorldState());

        for (int i = 0; i != game.getUserNum() - 1; ++i) {
            stateList.add(stateList.get(stateList.size() - 1).getDiscreteRotation());
        }

        return stateList;
    }

}
