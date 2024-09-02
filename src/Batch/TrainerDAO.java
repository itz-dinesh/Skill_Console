package Batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrainerDAO {
    
    private Connection connection;

    // Constructor to initialize the connection
    public TrainerDAO(Connection connection) {
        this.connection = connection;
    }

    // Method to authenticate a trainer
    public Trainer loginTrainer(String username, String password) throws SQLException {
        String query = "SELECT * FROM trainers WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Trainer(username, username); // Use username for the name here or adjust accordingly
            }
            return null; // Return null if no match found
        }
    }

    // Method to get the number of candidates enrolled in a batch
    public int getEnrolledCandidatesCount(String batchName) throws SQLException {
        String query = "SELECT COUNT(*) FROM batches WHERE batch_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, batchName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        }
    }

    // Method to get the list of candidates in a batch
    public List<User> getCandidatesInBatch(String batchName) throws SQLException {
        List<User> candidates = new ArrayList<>();
        String query = "SELECT users.name, users.email FROM users " +
                       "JOIN batches ON users.email = batches.candidate_email " +
                       "WHERE batches.batch_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, batchName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                candidates.add(new User(name, email, null, null, null, null, null));
            }
        }
        return candidates;
    }
}
