package server.dao;

import java.sql.Connection;


public class DAOFactory {
    private static final Connection connection = MySQLConnection.getConnection();

    public static UserDAO getUserDAO() {
        return new UserDAO(connection);
    }
}
