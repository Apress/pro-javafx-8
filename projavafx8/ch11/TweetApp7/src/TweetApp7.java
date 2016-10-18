
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.bind.JAXB;

public class TweetApp7 extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws MalformedURLException  {
    primaryStage.setTitle("TweetList");
    ListView<Tweet> listView = new ListView<Tweet>();
    listView.setItems(getObservableList());
    listView.setCellFactory(new Callback<ListView<Tweet>, ListCell<Tweet>>() {

      @Override
      public ListCell<Tweet> call(ListView<Tweet> listview) {
        return new TweetCell();
      }
    });

    StackPane root = new StackPane();
    root.getChildren().add(listView);
    primaryStage.setScene(new Scene(root, 300, 250));
    primaryStage.show();
  }

  ObservableList<Tweet> getObservableList() throws MalformedURLException {
    String loc = "http://search.twitter.com/search.rss?q=javafx";
    URL url = new URL(loc);
    TwitterResponse response = JAXB.unmarshal(url, TwitterResponse.class);
    List<Tweet> raw = response.getTweets();
    ObservableList<Tweet>  answer = FXCollections.observableArrayList(raw);
   
    return answer;
  }

}
