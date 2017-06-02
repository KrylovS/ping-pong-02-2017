package sample;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by sergey on 23.02.17.
 */
public class UserProfile {
    private String email;
    private String password;
    private String login;
    private int score;
    private int rating;
    private int party;
    private long id;

    public UserProfile(UserProfile userProfile) {
        this.email = userProfile.email;
        this.login = userProfile.login;
        this.password = userProfile.password;
        this.id = userProfile.id;
        this.score = userProfile.score;
        this.rating = userProfile.rating;
        this.party = userProfile.party;
    }

    public UserProfile(@JsonProperty("email") String email, @JsonProperty("login") String login,
                       @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
        this.login = login;
    }
  
    public UserProfile() {

    }
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public final void setId() {
        this.id = ID_GENERATOR.getAndIncrement();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public int getRating() {
        return rating;
    }

    public int getParty() { return party; }

    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setParty(int party) { this.party = party; }
}
