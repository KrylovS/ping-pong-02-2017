package sample;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sergey on 27.03.17.
 */
@Repository
public class UserMapper implements RowMapper<UserProfile> {
    @Override
    public UserProfile mapRow(ResultSet resultSet, int i) throws SQLException {
        final UserProfile userProfile = new UserProfile();
        userProfile.setLogin(resultSet.getString("login"));
        userProfile.setEmail(resultSet.getString("email"));
        userProfile.setPassword(resultSet.getString("password"));
        userProfile.setScore(resultSet.getInt("score"));
        userProfile.setRating(resultSet.getInt("rating"));
        userProfile.setId(resultSet.getInt("id"));

        return userProfile;
    }
}
