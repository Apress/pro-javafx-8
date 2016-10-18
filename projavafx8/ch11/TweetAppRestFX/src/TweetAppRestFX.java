
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import restfx.web.GetQuery;
import restfx.web.Query;
import restfx.web.QueryListener;

public class TweetAppRestFX extends Application {

  ObservableList<Tweet> tweets = FXCollections.observableArrayList();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws MalformedURLException {
    primaryStage.setTitle("TweetList");
    ListView<Tweet> listView = new ListView<Tweet>();
    listView.setItems(tweets);
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
    getObservableList();
    System.out.println("Setup complete");
  }

  void getObservableList() {
    GetQuery getQuery = new GetQuery("search.twitter.com", "/search.json");
    getQuery.getParameters().put("q", "javafx");
    getQuery.execute(new QueryListener<Object>() {

      @Override
      @SuppressWarnings("unchecked")
      public void queryExecuted(Query<Object> task) {
        Map<String, Object> value = (Map<String, Object>) task.getValue();
        List<Object> results = (List<Object>) value.get("results");
        for (Object target: results) {
          Map<String,String> tweetMap = (Map<String, String>)target;
          String timeStamp = tweetMap.get("created_at");
          String author = tweetMap.get("from_user");
          String title = tweetMap.get("text");
          Tweet tweet = new Tweet(author, title, timeStamp);
          tweets.add(tweet);
        }
      }
    });
  }

  
  }
