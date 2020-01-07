package server.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Session {
    private User user;
    private String key;
    private LocalDateTime expirationDate;

    public Session(User user, String key) {
        this.user = user;
        this.key = key;
        resetExpirationDate();
    }

    public void resetExpirationDate() {
        expirationDate = LocalDateTime.now().plus(1, ChronoUnit.MINUTES);
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }

    public User getUser() {
        return user;
    }

    public String getKey() {
        return key;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
