
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TweetApp11 extends Application {

  ObservableList<Tweet> tweets = FXCollections.observableArrayList();

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws MalformedURLException {
    primaryStage.setTitle("TweetList");
    TableView<Tweet> tableView = new TableView<Tweet>();
    TableColumn<Tweet, String> dateColumn = new TableColumn<Tweet, String>("Date");
    dateColumn.setCellValueFactory(new PropertyValueFactory<Tweet, String>("timeStamp"));
    TableColumn<Tweet, String> authorColumn = new TableColumn<Tweet, String>("Author");
    authorColumn.setCellValueFactory(new PropertyValueFactory<Tweet, String>("author"));
    TableColumn<Tweet, String> textColumn = new TableColumn<Tweet, String>("Text");
    textColumn.setCellValueFactory(new PropertyValueFactory<Tweet, String>("title"));

    tableView.getColumns().addAll(dateColumn, authorColumn, textColumn);
    tableView.setItems(tweets);

    StackPane root = new StackPane();
    root.getChildren().add(tableView);
    primaryStage.setScene(new Scene(root, 300, 250));
    primaryStage.show();
    getObservableList();
  }

  void getObservableList() throws MalformedURLException {
    String loc = "http://search.twitter.com/search.rss?q=javafx";
    final TwitterRetrievalService twitterRetrievalService = new TwitterRetrievalService(loc);
    twitterRetrievalService.start();
    twitterRetrievalService.stateProperty().addListener(new InvalidationListener() {

      @Override
      public void invalidated(Observable arg0) {
        State now = twitterRetrievalService.getState();
        System.out.println("State of service = " + now);
        if (now == State.SUCCEEDED) {
          tweets.setAll(twitterRetrievalService.getValue());
        }

      }
    });


  }
}
