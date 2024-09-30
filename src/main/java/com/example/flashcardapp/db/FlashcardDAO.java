package com.example.flashcardapp.db;

import com.example.flashcardapp.model.Flashcard;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlashcardDAO {
    private Connection connection;

    public FlashcardDAO(Connection connection) {
        this.connection = connection;
    }

    public void addFlashcard(Flashcard flashcard) throws SQLException {
        String sql = "INSERT INTO flashcards (question, answer) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());
            statement.executeUpdate();
        }
    }

    public Flashcard getFlashcard(int id) throws SQLException {
        String sql = "SELECT * FROM flashcards WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Flashcard(
                    resultSet.getInt("id"),
                    resultSet.getString("question"),
                    resultSet.getString("answer")
                );
            }
        }
        return null;
    }

    public List<Flashcard> getAllFlashcards() throws SQLException {
        List<Flashcard> flashcards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                flashcards.add(new Flashcard(
                    resultSet.getInt("id"),
                    resultSet.getString("question"),
                    resultSet.getString("answer")
                ));
            }
        }
        return flashcards;
    }

    public void updateFlashcard(Flashcard flashcard) throws SQLException {
        String sql = "UPDATE flashcards SET question = ?, answer = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, flashcard.getQuestion());
            statement.setString(2, flashcard.getAnswer());
            statement.setInt(3, flashcard.getId());
            statement.executeUpdate();
        }
    }

    public void deleteFlashcard(int id) throws SQLException {
        String sql = "DELETE FROM flashcards WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
