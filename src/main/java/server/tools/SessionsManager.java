package server.tools;

import server.model.Session;
import server.model.User;

import java.util.HashMap;
import java.util.UUID;


public class SessionsManager {
    private HashMap<String, Session> sessions;
    SessionsValidityCheck sessionsValidityCheck;

    public SessionsManager() {
        sessions = new HashMap<>();
        sessionsValidityCheck = new SessionsValidityCheck();
        sessionsValidityCheck.start();
    }

    public Session createSession(User user) {
        String key = UUID.randomUUID().toString();
        Session session = new Session(user, key);
        sessions.put(key, session);

        return session;
    }

    public Session getSession(String key) {
        Session session = sessions.get(key);
        session.resetExpirationDate();

        return session;
    }

    public boolean hasSession(String key) {
        return sessions.containsKey(key);
    }

    class SessionsValidityCheck extends Thread {
        private boolean isStopped;

        public SessionsValidityCheck() {
            isStopped = false;
        }

        @Override
        public void run() {
            while (!isStopped) {
                try {
                    sessions.entrySet().removeIf(e -> e.getValue().isExpired());
                    Thread.sleep(1000);

                } catch (InterruptedException e) {}
            }
        }

        public void shutdown() {
            isStopped = true;
        }
    }

    public void stop() {
        try {
            sessionsValidityCheck.shutdown();
            sessionsValidityCheck.join();

        } catch (InterruptedException e) {}
    }
}
