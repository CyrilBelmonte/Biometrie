package test.server;

import server.dao.DAOFactory;
import server.model.User;


public class DAOTest {
    public static void main(String[] args) {
        boolean hasSucceeded;

        User user1 = new User("John", "DOE", "john.doe@cergy.fr", 1, 2, "password");
        hasSucceeded = DAOFactory.getUserDAO().insert(user1);
        System.out.println("Insertion of the user: " + hasSucceeded);
        System.out.println("New user ID: " + user1.getId());

        User user2 = DAOFactory.getUserDAO().find(user1.getId());
        System.out.println("Find: " + user2.getFirstName());

        User user3 = new User("Bob", "DOE", "bob.doe@cergy.fr", 6, 100, "azerty");
        hasSucceeded = DAOFactory.getUserDAO().insert(user3);
        System.out.println("Insertion of the user: " + hasSucceeded);
        System.out.println("New user ID: " + user3.getId());

        User user4 = DAOFactory.getUserDAO().find(user3.getId());
        System.out.println("Find: " + user4.getFirstName());
    }
}