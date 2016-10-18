
import java.io.IOException;
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.javafxdata.datasources.io.NetworkSource;
import org.javafxdata.datasources.protocol.ObjectDataSource;

public class TweetAppDataFX extends Application {

  ObservableList<Tweet> tweets = FXCollections.observableArrayList();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws MalformedURLException, IOException {
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
    System.out.println("Setup complete");
  }

  ObservableList<Tweet> getObservableList() throws MalformedURLException, IOException {
    String url = "http://search.twitter.com/search.rss?q=javafx";
    NetworkSource reader = new NetworkSource(url);
    ObjectDataSource objectDataSource = new ObjectDataSource().reader(reader).tag("item").clazz(Tweet.class);
    Service service = objectDataSource.retrieve();
    return objectDataSource.getData();
  }
  
}
