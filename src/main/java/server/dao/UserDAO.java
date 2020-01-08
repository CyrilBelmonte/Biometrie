package server.dao;

import server.model.User;

import java.sql.*;


public class UserDAO extends DAO {
    public UserDAO(Connection connection) {
        super(connection);
    }

    public User find(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            ResultSet result = statement.executeQuery();
            user = getUserFromRSet(result);
            statement.close();

        } catch (SQLException e) {
            System.err.println("[ERROR] Query exception: " + e.getMessage());
        }

        return user;
    }

    public boolean update(User user) {
        String query = "UPDATE users SET firstName = ?, lastName = ?, email = ?, is_admin = ?, " +
                       "x = ?, y = ?, password = ?, biometric_data = ? WHERE id = ?";

        boolean hasSucceeded = false;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setBoolean(4, user.isAdmin());
            statement.setInt(5, user.getX());
            statement.setInt(6, user.getY());
            statement.setString(7, user.getPassword());
            statement.setString(8, user.getBiometricData());
            statement.setInt(9, user.getId());
            int updatedTuples = statement.executeUpdate();

            if (updatedTuples > 0) {
                hasSucceeded = true;
            }

            statement.close();

        } catch (SQLException e) {
            System.err.println("[ERROR] Query exception: " + e.getMessage());
        }

        return hasSucceeded;
    }

    public boolean insert(User user) {
        String query = "INSERT INTO users (firstName, lastName, email, is_admin, x, y, password, biometric_data)" +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        boolean hasSucceeded = false;

        try {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setBoolean(4, user.isAdmin());
            statement.setInt(5, user.getX());
            statement.setInt(6, user.getY());
            statement.setString(7, user.getPassword());
            statement.setString(8, user.getBiometricData());
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) {
                user.setId(result.getInt(1));
                hasSucceeded = true;
            }

            statement.close();

        } catch (SQLException e) {
            System.err.println("[ERROR] Query exception: " + e.getMessage());
        }

        return hasSucceeded;
    }

    private User getUserFromRSet(ResultSet resultSet) {
        User user = null;

        try {
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("is_admin");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                String password = resultSet.getString("password");
                String biometricData = resultSet.getString("biometric_data");

                user = new User(id, firstName, lastName, email, isAdmin, x, y, password, biometricData);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR] getUserFromRSet: " + e.getMessage());
        }

        return user;
    }
}
