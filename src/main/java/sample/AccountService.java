package sample;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by sergey on 23.02.17.
 */

@Service
public class AccountService {
    private Map<String, UserProfile> userStorage = new HashMap<>();

    @Nullable
    public UserProfile register(@NotNull String email, @NotNull String login, @NotNull String password) {
        final UserProfile userProfile = new UserProfile(email, login, password);
        if(!userStorage.containsKey(email)) {
            userProfile.setId();
            userStorage.put(email, userProfile);
            return userProfile;
        }
        return null;
    }


    public boolean login(@NotNull String email, @NotNull String password) {
        return userStorage.containsKey(email) && userStorage.get(email).getPassword().equals(password);
    }

    public UserProfile getUser(@NotNull String email) {
        return userStorage.get(email);
    }

    @Nullable
    public UserProfile update(@NotNull UserProfile userProfile, @NotNull UserProfile changedProfile) {
        if(!isEmptyField(changedProfile.getEmail())) {
            if (!changedProfile.getEmail().equals(userProfile.getEmail()) &&
                    userStorage.containsKey(changedProfile.getEmail())) {
                return null;
            }
        }
        userStorage.remove(userProfile.getEmail());
        updateNotNullFields(userProfile, changedProfile);
        userStorage.put(userProfile.getEmail(), userProfile);
        return userProfile;
    }

    public void updateScore(@NotNull UserProfile userProfile) {
        userStorage.put(userProfile.getEmail(), userProfile);
    }

    public ArrayList<UserProfile> getSortedUsersByScore() {
        final ArrayList<UserProfile> userProfileArrayList = new ArrayList<UserProfile>();
        userProfileArrayList.addAll(userStorage.values());
        Collections.sort(userProfileArrayList, new Comparator<UserProfile>() {
            @Override
            public int compare(UserProfile o1, UserProfile o2) {
                if(o1.getScore() < o2.getScore()) {
                    return 1;
                }
                else if(o1.getScore() > o2.getScore()) {
                    return -1;
                }
                return 0;
            }
        });

        return  userProfileArrayList;
    }

    private boolean isEmptyField(String field) {
        return (field == null) || field.isEmpty();
    }

    private void updateNotNullFields(@NotNull UserProfile userProfile, @NotNull UserProfile changedProfile) {
        if(!isEmptyField(changedProfile.getEmail())) {
            userProfile.setEmail(changedProfile.getEmail());
        }
        if(!isEmptyField(changedProfile.getLogin())) {
            userProfile.setLogin(changedProfile.getLogin());
        }
        if(!isEmptyField(changedProfile.getPassword())) {
            userProfile.setPassword(changedProfile.getPassword());
        }
    }

    public void flush() {
        userStorage.clear();
    }
}
