package projavafx;

/**
 *
 * @author johan
 */
public class Question {
    
    private String owner;
    private String question;
    private long timestamp;

    public Question (String o, String q, long t) {
        this.owner = o;
        this.question = q;
        this.timestamp = t;
    }
    
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
}
