package sample.services.account;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import sample.UserMapper;
import sample.UserProfile;
import sample.encoding.EncoderServiceInterface;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sergey on 26.03.17.
 */

@Service("AccountServiceDB")
public class AccountServiceDB implements AccountServiceInterface {
    final JdbcTemplate jdbcTemplate;
    private static final UserMapper USER_MAPPER = new UserMapper();
    private final Logger logger = Logger.getLogger(AccountServiceDB.class.getName());

    @Autowired
    EncoderServiceInterface encoderService;

    @Autowired
    public AccountServiceDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Nullable
    @Override
    public UserProfile register(UserProfile userProfile) {
        try {
            jdbcTemplate.update("INSERT INTO \"User\" (login, email, password, score, rating) values (?, ?, ?, ?, ?) ",
                    userProfile.getLogin(), userProfile.getEmail(),
                    encoderService.getPasswordEncoder().encode(userProfile.getPassword()),
                    userProfile.getScore(), userProfile.getRating());
            return getUser(userProfile.getEmail());
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return null;
        }

    }

    @Override
    public boolean login(@NotNull String email, @NotNull String password) {
        try {
            final UserProfile userProfile = jdbcTemplate.queryForObject("SELECT * FROM \"User\" WHERE LOWER(email) = LOWER(?)",
                    new Object[]{email}, USER_MAPPER);
            return encoderService.getPasswordEncoder().matches(password, userProfile.getPassword());
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return false;
        }
    }

    @Nullable
    @Override
    public UserProfile getUser(@NotNull String email) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM \"User\" WHERE LOWER(email) = LOWER(?)",
                    new Object[]{email}, USER_MAPPER);
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return null;
        }
    }

    @Nullable
    @Override
    public UserProfile update(@NotNull String email, @NotNull UserProfile changedProfile) {
        final UserProfile userProfile = getUser(email);
        if (userProfile == null) {
            return null;
        }

        if (encoderService.getPasswordEncoder().matches(changedProfile.getPassword(), userProfile.getPassword())) {
            changedProfile.setPassword("");
        }

        updateNotNullFields(userProfile, changedProfile);

        try {
            jdbcTemplate.update("UPDATE \"User\" SET login = ?, email = ?, password = ?, score = ?, rating = ? WHERE id = ?",
                    userProfile.getLogin(), userProfile.getEmail(), userProfile.getPassword(), userProfile.getScore(),
                    userProfile.getRating(), userProfile.getId());
            return getUser(userProfile.getEmail());
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return null;
        }

    }

    @Nullable
    @Override
    public UserProfile updateScore(@NotNull UserProfile userProfile) {
        try {
            jdbcTemplate.update("UPDATE \"User\" SET score = score + ? WHERE LOWER(email) = LOWER(?)",
                    userProfile.getScore(), userProfile.getEmail());
            jdbcTemplate.update("UPDATE \"User\" SET party = party + 1 WHERE LOWER(email) = LOWER(?)",
                    userProfile.getEmail());
            jdbcTemplate.update("UPDATE \"User\" SET rating = floor(100 * score / party) WHERE LOWER(email) = LOWER(?)",
                    userProfile.getEmail());
            return getUser(userProfile.getEmail());
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return null;
        }
    }

    @Nullable
    @Override
    public List<UserProfile> getSortedUsersByScore(int count) {
        try {
            return jdbcTemplate.query("SELECT * FROM \"User\" ORDER BY score DESC LIMIT ?",
                    new Object[]{count}, USER_MAPPER);
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return null;
        }
    }

    @Nullable
    @Override
    public UserProfile updateRating(@NotNull UserProfile userProfile) {
        try {
            jdbcTemplate.update("UPDATE \"User\" SET rating = ? WHERE LOWER(email) = LOWER(?)",
                    userProfile.getRating(), userProfile.getEmail());
            return getUser(userProfile.getEmail());
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return null;
        }
    }

    @Nullable
    @Override
    public List<UserProfile> getSortedUsersByRating(int count) {
        try {
            return jdbcTemplate.query("SELECT * FROM \"User\" ORDER BY rating DESC LIMIT ?",
                    new Object[]{count}, USER_MAPPER);
        } catch (DataAccessException e) {
            logger.log(Level.WARNING, "Exception : ", e);
            return null;
        }
    }

    private boolean isEmptyField(String field) {
        return ((field == null) || field.isEmpty());
    }

    private void updateNotNullFields(@NotNull UserProfile userProfile, @NotNull UserProfile changedProfile) {
        if(!isEmptyField(changedProfile.getEmail())) {
            userProfile.setEmail(changedProfile.getEmail());
        }
        if(!isEmptyField(changedProfile.getLogin())) {
            userProfile.setLogin(changedProfile.getLogin());
        }
        if(!isEmptyField(changedProfile.getPassword())) {
            userProfile.setPassword(encoderService.getPasswordEncoder().encode(changedProfile.getPassword()));
        }
    }
}
