package sample.services.account;

import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sample.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;


@ActiveProfiles({"test"})
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest extends UserRelatedTest {
    @Autowired
    @Qualifier("AccountServiceDB")
    private AccountServiceInterface accountService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private final Logger logger = Logger.getLogger(AccountServiceTest.class.getName());

    @Before
    public void initDB() {
        final Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:h2:mem:testdb", "sa", "");
        flyway.migrate();
    }

    @Test
    public void testRegisterSuccess() {
        logger.info("Testing successfull user creation");
        final UserProfile userProfile = getRandomUser();
        final UserProfile savedUser = accountService.register(userProfile);

        assertTrue(isEqual(userProfile, savedUser));

        logger.info("OK");
    }

    @Test
    public void testRegisterConflict() {
        logger.info("Testing conflict user creation");
        final UserProfile userProfile = getRandomUser();

        assertNotEquals(accountService.register(userProfile), null);
        assertEquals(accountService.register(userProfile), null);
        logger.info("OK");
    }

    @Test
    public void testGetUserSuccess() {
        logger.info("Testing successfull user extraction");
        final UserProfile userProfile = getRandomUser();

        accountService.register(userProfile);
        final UserProfile extractedUser = accountService.getUser(userProfile.getEmail());

        assertTrue(isEqual(userProfile, extractedUser));
        logger.info("OK");
    }

    @Test
    public void testSuccessfullLogin() {
        logger.info("Testing successfull credentials validation");
        final UserProfile userProfile = getRandomUser();

        accountService.register(userProfile);
        final boolean isValid = accountService.login(userProfile.getEmail(), userProfile.getPassword());

        assertTrue(isValid);
        logger.info("OK");
    }

    @Test
    public void testLoginFailed() {
        logger.info("Testing failed credentials validation");
        final UserProfile userProfile = getRandomUser();
        final UserProfile anotherUser = getRandomUser();

        accountService.register(userProfile);
        final boolean isValid = accountService.login(userProfile.getEmail(), anotherUser.getPassword());

        assertFalse(isValid);
        logger.info("OK");
    }

    @Test
    public void getUserFail() {
        logger.info("Testing non-existing user extraction");
        assertEquals(accountService.getUser(getRandomUser().getEmail()), null);
        logger.info("OK");
    }

    @Test
    public void testSuccessfullUserUpdate() {
        logger.info("Testing successfull user update");

        final UserProfile userProfile = getRandomUser();
        final UserProfile userUpdate = getRandomUser();

        userUpdate.setEmail(userProfile.getEmail());

        accountService.register(userProfile);
        final UserProfile newUser = accountService.update(userProfile.getEmail(), userUpdate);

        assertTrue(isEqual(newUser, userUpdate));
        logger.info("OK");
    }

    @Test
    public void testEmailConflictUserUpdate() {
        logger.info("Testing email conflict user update");

        final UserProfile firstUser = getRandomUser();
        final UserProfile secondUser = getRandomUser();

        final UserProfile firstUserUpdate = getRandomUser();
        firstUserUpdate.setEmail(secondUser.getEmail());

        accountService.register(firstUser);
        accountService.register(secondUser);

        final UserProfile newUser = accountService.update(firstUser.getEmail(), firstUserUpdate);
        assertEquals(newUser, null);

        logger.info("OK");
    }

    @Test
    public void testNonExistingUserUpdate() {
        logger.info("Test non existing user update");

        final UserProfile userProfile = getRandomUser();
        final UserProfile userUpdate = getRandomUser();

        final UserProfile newUser = accountService.update(userProfile.getEmail(), userUpdate);

        assertEquals(newUser, null);
        logger.info("OK");
    }

    @Test
    public void testSuccessfullScoreUpdate() {
        logger.info("Test successful score update");

        final UserProfile userProfile = getRandomUser();
        accountService.register(userProfile);

        final int newScore = 100;
        userProfile.setScore(newScore);
        final UserProfile newUser = accountService.updateScore(userProfile);

        assertEquals(newUser.getScore(), newScore);

        logger.info("OK");
    }

    @Test
    public void testNonExistingUserScoreUpdate() {
        logger.info("Test non existing user score update");

        final UserProfile extractedUser = accountService.updateScore(getRandomUser());
        assertEquals(extractedUser, null);

        logger.info("OK");
    }

    @Test
    public void testSuccessfullRatingUpdate() {
        logger.info("Test successful rating update");

        final UserProfile userProfile = getRandomUser();
        accountService.register(userProfile);

        final int newRating = 100;
        userProfile.setRating(newRating);
        final UserProfile newUser = accountService.updateRating(userProfile);

        assertEquals(newUser.getRating(), newRating);

        logger.info("OK");
    }

    @Test
    public void testNonExistingUserRatingUpdate() {
        logger.info("Test non existing user rating update");

        final UserProfile extractedUser = accountService.updateRating(getRandomUser());
        assertEquals(extractedUser, null);

        logger.info("OK");
    }

    @Test
    public void testSuccessfulGetLeaders() {
        logger.info("Test successful user extraction");

        final int maxRating = 10000;
        final int startRating = 1000;
        final int ratingStep = 100;

        final List<Integer> ratingList = new ArrayList<>();
        for (int i = maxRating; i > startRating; i -= ratingStep) {
            ratingList.add(i);
        }

        for (int rating : ratingList) {
            final UserProfile userProfile = getRandomUser();
            userProfile.setRating(rating);
            accountService.register(userProfile);
        }

        final List<UserProfile> extractedUsers = accountService.getSortedUsersByRating(ratingList.size());
        final List<Integer> extractedRatings = new ArrayList<>();

        for (UserProfile user : extractedUsers) {
            extractedRatings.add(user.getRating());
        }

        assertEquals(extractedRatings.size(), ratingList.size());
        for (int i = 0; i != extractedRatings.size(); ++i) {
            assertEquals(extractedRatings.get(i), ratingList.get(i));
        }

        logger.info("OK");

    }
}
