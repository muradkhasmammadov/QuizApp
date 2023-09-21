import Classes.Question;
import Classes.Topic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            String jdbcUrl = "jdbc:postgresql://localhost:5433/quizapp";
            String username = "postgres";
            String password = "Murad.123";
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            DAOQuestion daoQuestion = new DAOQuestion(connection);

            // Add a new question
            Question newQuestion = new Question();
            newQuestion.setTopicId(new Topic(2, "Math"));
            newQuestion.setDifficultyRank(5);
            newQuestion.setContent("What is the 2 + 7?");

            newQuestion.setTopicId(new Topic(3, "History"));
            newQuestion.setDifficultyRank(5);
            newQuestion.setContent("When the SSR collapsed?");

            daoQuestion.saveQuestion(newQuestion);

            // Update an existing question
            int questionIdToUpdate = newQuestion.getId();
            Question existingQuestion = daoQuestion.getQuestionById(questionIdToUpdate);
            existingQuestion.setContent("What is the 2 + 5?");
            existingQuestion.setDifficultyRank(3);
            daoQuestion.updateQuestion(existingQuestion);

            // Delete a question
            int questionIdToDelete = newQuestion.getId();
            daoQuestion.deleteQuestion(questionIdToDelete);

            // Search questions by topic
            String topicToSearch = "Math";
            List<Question> foundQuestions = daoQuestion.searchQuestionsByTopic(topicToSearch);

            for (Question foundQuestion : foundQuestions) {
                System.out.println("Question ID: " + foundQuestion.getId());
                System.out.println("Content: " + foundQuestion.getContent());

            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
