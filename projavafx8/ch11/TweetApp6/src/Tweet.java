
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author johan
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Tweet {

  private String author;
  private String title;
  @XmlElement(name="pubDate")
  private String timeStamp;

  public Tweet() {}
  
  public Tweet (String a, String t, String s) {
    this.author = a;
    this.title = t;
    this.timeStamp = s;
  }
  
  /**
   * @return the author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * @param author the author to set
   */
  public void setAuthor(String author) {
    this.author = author;
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
  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }
  
//  @Override
//  public String toString () {
//    return "--["+timeStamp+"] "+author+": "+title;
//  }
  
}
