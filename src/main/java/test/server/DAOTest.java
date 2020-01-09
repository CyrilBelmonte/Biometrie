package test.server;

import server.dao.DAOFactory;
import server.model.User;
import server.tools.AES;
import server.tools.Tools;


public class DAOTest {
    public static void main(String[] args) {
        boolean hasSucceeded;

        User user1 = new User("John", "DOE", "john.doe@cergy.fr", false, 1, 2,
            Tools.hmacMD5(Tools.hmacMD5("password", "1"), "2"),
            AES.encrypt("biometric_data", "lArDiz6XFYobz8YnETSEAw=="));

        hasSucceeded = DAOFactory.getUserDAO().insert(user1);
        System.out.println("Insertion of the user: " + hasSucceeded);
        System.out.println("New user ID: " + user1.getId());

        User user2 = DAOFactory.getUserDAO().find(user1.getId());
        System.out.println("Find: " + user2.getFirstName());

        int x = Tools.getSeed();
        int y = Tools.getSeed();

        User user3 = new User("Alice", "DOE", "alice.doe@cergy.fr", true, x, y,
            Tools.hmacMD5(Tools.hmacMD5("123456", String.valueOf(x)), String.valueOf(y)),
            AES.encrypt("biometric_data", "Q9Elm0Cjtaxf/uO/ql2yKA=="));

        hasSucceeded = DAOFactory.getUserDAO().insert(user3);
        System.out.println("Insertion of the user: " + hasSucceeded);
        System.out.println("New user ID: " + user3.getId());

        User user4 = DAOFactory.getUserDAO().find(user3.getId());
        System.out.println("Find: " + user4.getFirstName());
    }
}
