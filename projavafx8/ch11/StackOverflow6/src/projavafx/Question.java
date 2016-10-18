package projavafx;

import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author johan
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Question {
    
    static final SimpleDateFormat sdf = new SimpleDateFormat ("dd-MM-YY");

    private StringProperty ownerProperty = new SimpleStringProperty();
    private String question;
    private long timestamp;

    public Question (String o, String q, long t) {
        this.ownerProperty.set(o);
        this.question = q;
        this.timestamp = t;
    }
    
    public String getOwner() {
        return ownerProperty.get();
    }

    public void setOwner(String owner) {
        this.ownerProperty.set(owner);
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
    
    public String getTimestampString() {
        return sdf.format(new Date(timestamp));
    }
    
}
