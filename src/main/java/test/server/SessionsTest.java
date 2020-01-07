package test.server;

import server.model.Session;
import server.model.User;
import server.tools.SessionsManager;

import java.time.LocalDateTime;


public class SessionsTest {
    public static void main(String[] args) throws InterruptedException {
        SessionsManager sessionsManager = new SessionsManager();

        User user = new User(12, "John", "DOE", "john.doe@cergy.fr", 1, 2, "password");
        Session session = sessionsManager.createSession(user);

        while (true) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            System.err.println("[" + currentDateTime + "] Session expiration datetime: " + session.getExpirationDate());
            System.err.println("[" + currentDateTime + "] Session key " + session.getKey());
            System.out.println("[" + currentDateTime + "] Is the session valid ? " + !session.isExpired());
            System.out.println("[" + currentDateTime + "] SessionsManager has this session ? "
                               + sessionsManager.hasSession(session.getKey()));

            // sessionsManager.getSession(session.getKey());

            Thread.sleep(1000);
        }

        // sessionsManager.stop();
    }
}
