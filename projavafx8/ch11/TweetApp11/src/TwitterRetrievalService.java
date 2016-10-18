
import java.net.URL;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javax.xml.bind.JAXB;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author johan
 */
public class TwitterRetrievalService extends Service<ObservableList<Tweet>> {

  
  private String loc;

  public TwitterRetrievalService(String loc) {
    this.loc = loc;
  }

  @Override
  protected Task createTask() {
    Task task = new SingleRetrieverTask(this.loc);
    return task;
  }

  
  private static class SingleRetrieverTask extends Task<ObservableList<Tweet>> {

    private String location;

    public SingleRetrieverTask(final String loc) {
      location = loc;
    }

    @Override
    protected ObservableList<Tweet> call() throws Exception {
      try {
      URL url = new URL(location);
      TwitterResponse response = JAXB.unmarshal(url, TwitterResponse.class);
      List<Tweet> raw = response.getTweets();
      ObservableList<Tweet> answer = FXCollections.observableArrayList(raw);
      return answer;
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
    
    
  }
}
