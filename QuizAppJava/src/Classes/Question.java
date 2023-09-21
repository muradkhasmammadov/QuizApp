package Classes;

import java.util.List;
public class Question {
    private int id;
    private Topic topicId;
    private int difficultyRank;
    private String content;
    private List<Response> responses;


    public Question(int id, Topic topicId, int difficultyRank, String content, List<Response> responses) {
        this.id = id;
        this.topicId = topicId;
        this.difficultyRank = difficultyRank;
        this.content = content;
        this.responses = responses;
    }

    public Question() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topic getTopicId() {
        return topicId;
    }

    public void setTopicId(Topic topicId) {
        this.topicId = topicId;
    }

    public int getDifficultyRank() {
        return difficultyRank;
    }

    public void setDifficultyRank(int difficultyRank) {
        this.difficultyRank = difficultyRank;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }
}
