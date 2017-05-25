package sample.services.game;

import gameLogic.config_models.GameConfig;
import gameLogic.game.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sample.Lobby;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


@ActiveProfiles({"test"})
@SpringBootTest
@RunWith(SpringRunner.class)
public class LobbyTest {
    private final Logger logger = Logger.getLogger(LobbyTest.class.getName());
    private List<String> players;
    @Autowired
    Lobby lobby;

    @Before
    public void init() {
        players = IntStream.range(0, GameConfig.PLAYERS_NUM + 1).boxed()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Test
    public void testUserAddition() {
        logger.info("Testing player addition");
        lobby.reset();

        final List<Integer> playerIds = players.stream()
                .map(email -> lobby.addPlayer(email))
                .collect(Collectors.toList());

        for (int i = 0; i != GameConfig.PLAYERS_NUM; ++i) {
            assertEquals(playerIds.get(0), playerIds.get(i));
            logger.info(String.format("Same party [%d] OK", i));
        }

        assertNotEquals(playerIds.get(0), playerIds.get(playerIds.size() - 1));
        logger.info("Different parties OK");
        logger.info("OK");
    }

    @Test
    public void testGetPlayerPartyId() {
        logger.info("Testing player addition");
        lobby.reset();

        final List<Integer> partyIds = players.stream()
                .map(email -> lobby.addPlayer(email))
                .collect(Collectors.toList());

        IntStream.range(0, players.size()).boxed()
                .forEach(i -> {
                    final int correctPartyId = i % GameConfig.PLAYERS_NUM;
                    final int playerId = lobby.getPlayerPartyId(players.get(i), partyIds.get(i));
                    assertEquals(correctPartyId, playerId);
                    logger.info(String.format("[%d] OK", i));
                });

        logger.info("OK");
    }

    @Test
    public void testGetSortedPlayers() {
        logger.info("Testing sorted players extraction");
        lobby.reset();

        final Integer firstPartyIndex = players.stream()
                .map(email -> lobby.addPlayer(email))
                .collect(Collectors.toList()).get(0);

        final List<String> samePartyPlayers = players.subList(0, GameConfig.PLAYERS_NUM);
        final List<String> extractedSamePartyPlayers = lobby.getSortedPlayers(firstPartyIndex);

        assertEquals(extractedSamePartyPlayers.size(), GameConfig.PLAYERS_NUM);
        IntStream.range(0, GameConfig.PLAYERS_NUM).boxed().forEach(
                i -> assertEquals(samePartyPlayers.get(i), extractedSamePartyPlayers.get(i))
        );

        logger.info("OK");
    }
}
