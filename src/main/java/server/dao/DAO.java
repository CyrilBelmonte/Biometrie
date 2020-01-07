package server.dao;

import java.sql.Connection;


public abstract class DAO {
    protected Connection connection;

    public DAO(Connection connection) {
        this.connection = connection;
    }
}
