package sample.services.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.logging.Logger;
import static org.junit.Assert.*;


@ActiveProfiles({"test"})
@SpringBootTest
@RunWith(SpringRunner.class)
public class GameServiceTest {
    private final Logger logger = Logger.getLogger(GameServiceTest.class.getName());

    @Autowired
    private GameService gameService;

    @Test
    public void gameAdditionTest() {
        logger.info("Testing game addition");
        gameService.init();
        gameService.addGame();
        gameService.addGame();

        assertEquals(gameService.getGamesNumber(), 2);
        logger.info("OK");
    }
}
