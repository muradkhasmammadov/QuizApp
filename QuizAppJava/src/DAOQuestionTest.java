import Classes.Question;
import Classes.Topic;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;
public class DAOQuestionTest {

    String jdbcUrl = "jdbc:postgresql://localhost:5433/quizapp";
    String username = "postgres";
    String password = "Murad.123";
    Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

    DAOQuestion daoQuestion = new DAOQuestion(connection);

    public DAOQuestionTest() throws SQLException {
    }

    @Test
    public void testSaveQuestion() throws SQLException {

        Question question = new Question();
        question.setTopicId(new Topic(4, "Cars"));
        question.setDifficultyRank(2);
        question.setContent("What is the name of Ford's owner?");

        try {
            daoQuestion.saveQuestion(question);
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        Question savedQuestion = daoQuestion.getQuestionById(question.getId());
        assertNotNull(savedQuestion);
        assertEquals(question.getContent(), savedQuestion.getContent());
    }
    @Test
    public void testUpdateQuestion() throws SQLException {

        Question question = new Question();
        question.setTopicId(new Topic(4, "Cars"));
        question.setDifficultyRank(4);
        question.setContent("What is the name of BMW's owner?");
        daoQuestion.saveQuestion(question);


        question.setContent("How many models does BMW have?");
        daoQuestion.updateQuestion(question);
        try {
            daoQuestion.updateQuestion(question);
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        Question updatedQuestion = daoQuestion.getQuestionById(question.getId());
        assertNotNull(updatedQuestion);
        assertEquals("How many models does BMW have?", updatedQuestion.getContent());
    }

    @Test
    public void testDeleteQuestion() throws SQLException{
        Question question = new Question();
        question.setTopicId(new Topic(4, "Cars"));
        question.setDifficultyRank(1);
        question.setContent("What is the name of Mercedes's last model?");
        daoQuestion.saveQuestion(question);

        int questionIdToDelete = question.getId();
        daoQuestion.deleteQuestion(questionIdToDelete);
        try {
            daoQuestion.deleteQuestion(question.getId());
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        Question deletedQuestion = daoQuestion.getQuestionById(question.getId());
        assertNull(deletedQuestion);
    }

    @Test
    public void testSearchQuestionsByTopic() throws SQLException {
        daoQuestion.deleteAllQuestion();
        Question question1 = new Question();
        question1.setTopicId(new Topic(2, "Math"));
        question1.setDifficultyRank(3);
        question1.setContent("What is the result of 6 power 3?");

        Question question2 = new Question();
        question2.setTopicId(new Topic(2, "Math"));
        question2.setDifficultyRank(2);
        question2.setContent("What is the result of 1000/225 ?");

        try {
            daoQuestion.saveQuestion(question1);
            daoQuestion.saveQuestion(question2);
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }

        List<Question> foundQuestions;
        try {
            foundQuestions = daoQuestion.searchQuestionsByTopic("Math");
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
            return;
        }

        assertEquals(2, foundQuestions.size());
    }
}