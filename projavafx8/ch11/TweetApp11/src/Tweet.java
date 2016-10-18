
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author johan
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Tweet {

  private final StringProperty authorProperty = new SimpleStringProperty();
  public final StringProperty authorProperty() {return authorProperty;}
  private String title;
  private String timeStamp;

  public Tweet() {}
  
  public Tweet (String a, String t, String s) {
    this.authorProperty.set(a);
    this.title = t;
    this.timeStamp = s;
  }
  
  /**
   * @return the author
   */
  public String getAuthor() {
    return authorProperty.get();
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(String author) {
    authorProperty.set(author);
  
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the timeStamp
   */
  public String getTimeStamp() {
    return timeStamp;
  }

  /**
   * @param timeStamp the timeStamp to set
   */
    @XmlElement(name="pubDate")
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }
  
//  @Override
//  public String toString () {
//    return "--["+timeStamp+"] "+author+": "+title;
//  }
  
}
