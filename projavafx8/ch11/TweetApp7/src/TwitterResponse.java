import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author johan
 */
@XmlRootElement(name="rss")
@XmlAccessorType(XmlAccessType.FIELD)
public class TwitterResponse {

  public List<Tweet> getTweets() {
    return channel.getItem();
  }
  
  private TwitterResponse.Channel channel = new Channel();

  @XmlAccessorType(XmlAccessType.FIELD)
  private static class Channel {

    private String title;
    @XmlElement(name="item")
    private List<Tweet> item = new LinkedList<Tweet>();
    
    public Channel() {
    }

    /**
     * @return the item
     */
    public List<Tweet> getItem() {
      return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(List<Tweet> item) {
      this.item = item;
    }
  }
  
  
  
}
