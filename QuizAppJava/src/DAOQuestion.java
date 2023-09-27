import Classes.Question;
import Classes.Response;
import Classes.Topic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOQuestion {
    private Connection connection;

    public DAOQuestion(Connection connection) {
        this.connection = connection;
    }

    public void saveQuestion(Question question) throws SQLException {
        String insertQuery = "INSERT INTO Questions (topic_id, difficultyRank, content) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, question.getTopicId().getId());
            preparedStatement.setInt(2, question.getDifficultyRank());
            preparedStatement.setString(3, question.getContent());
            preparedStatement.executeUpdate();

            // Get the auto-generated ID of the newly inserted question
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                question.setId(generatedId);
            }
        }
    }

    public void updateQuestion(Question question) throws SQLException {
        String updateQuery = "UPDATE Questions SET topic_id=?, difficultyRank=?, content=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, question.getTopicId().getId());
            preparedStatement.setInt(2, question.getDifficultyRank());
            preparedStatement.setString(3, question.getContent());
            preparedStatement.setInt(4, question.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void deleteQuestion(int questionId) throws SQLException {
        String deleteQuery = "DELETE FROM Questions WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, questionId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteAllQuestion() throws SQLException {
        String deleteQuery = "TRUNCATE TABLE questions CASCADE;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        }
    }

    public List<Question> searchQuestionsByTopic(String topic) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String selectQuery = "SELECT Q.id AS question_id, Q.topic_id, Q.difficultyRank, Q.content, " +
                "R.id AS response_id, R.text, R.isCorrect " +
                "FROM Questions Q " +
                "INNER JOIN Topics T ON Q.topic_id = T.id " +
                "LEFT JOIN Responses R ON Q.id = R.question_id " +
                "WHERE T.name = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, topic);
            ResultSet resultSet = preparedStatement.executeQuery();

            int currentQuestionId = -1;
            Question currentQuestion = null;

            while (resultSet.next()) {
                int questionId = resultSet.getInt("question_id");
                int topicId = resultSet.getInt("topic_id");
                int difficultyRank = resultSet.getInt("difficultyRank");
                String content = resultSet.getString("content");

                if (questionId != currentQuestionId) {
                    currentQuestion = new Question(questionId, new Topic(topicId, topic), difficultyRank, content, new ArrayList<>());
                    questions.add(currentQuestion);
                    currentQuestionId = questionId;
                }

                int responseId = resultSet.getInt("response_id");
                String responseText = resultSet.getString("text");
                boolean isCorrect = resultSet.getBoolean("isCorrect");

                if (responseText != null) {
                    currentQuestion.getResponses().add(new Response(responseId, currentQuestion, responseText, isCorrect));
                }
            }
        }

        return questions;
    }

    public Question getQuestionById(int questionId) throws SQLException {
        String selectQuery = "SELECT topic_id, difficultyRank, content FROM Questions WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, questionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int topicId = resultSet.getInt("topic_id");
                int difficultyRank = resultSet.getInt("difficultyRank");
                String content = resultSet.getString("content");
                return new Question(questionId, new Topic(topicId, ""), difficultyRank, content, new ArrayList<>());
            }
        }
        return null;
    }
}
