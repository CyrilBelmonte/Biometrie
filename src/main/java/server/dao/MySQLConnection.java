package server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySQLConnection {
    private static Connection connection;

    private MySQLConnection() {}

    public static Connection getConnection() {
        if (connection == null) {
            String host = MySQLConfiguration.HOST;
            String base = MySQLConfiguration.BASE;
            String user = MySQLConfiguration.USER;
            String password = MySQLConfiguration.PASSWORD;
            String options = MySQLConfiguration.OPTIONS;

            String url = "jdbc:mysql://" + host + "/" + base + options;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);

            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("[ERROR] Unable to connect to the database: " + e.getMessage());
            }
        }

        return connection;
    }
}
