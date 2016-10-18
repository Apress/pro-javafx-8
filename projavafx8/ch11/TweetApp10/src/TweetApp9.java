
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TweetApp9 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        primaryStage.setTitle("TweetTable");
        TableView<Tweet> tableView = new TableView<Tweet>();
        TableColumn<Tweet, String> dateColumn = new TableColumn<Tweet, String>("Date");
        TableColumn<Tweet, String> authorColumn = new TableColumn<Tweet, String>("Author");
        TableColumn<Tweet, String> textColumn = new TableColumn<Tweet, String>("Text");
        textColumn.setPrefWidth(400);
        dateColumn.setCellValueFactory(new Callback<CellDataFeatures<Tweet, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Tweet, String> cdf) {
                Tweet tweet = cdf.getValue();
                return new SimpleStringProperty(tweet.getTimeStamp());
            }
        });
        authorColumn.setCellValueFactory(new Callback<CellDataFeatures<Tweet, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Tweet, String> cdf) {
                Tweet tweet = cdf.getValue();
                return new SimpleStringProperty(tweet.getAuthor());
            }
        });
        textColumn.setCellValueFactory(new Callback<CellDataFeatures<Tweet, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(CellDataFeatures<Tweet, String> cdf) {
                Tweet tweet = cdf.getValue();
                return new SimpleStringProperty(tweet.getTitle());
            }
        });


        tableView.getColumns().addAll(dateColumn, authorColumn, textColumn);
        tableView.setItems(getObservableList());

        StackPane root = new StackPane();
        root.getChildren().add(tableView);
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    ObservableList<Tweet> getObservableList() throws MalformedURLException {
        final ObservableList<Tweet> tweets = FXCollections.observableArrayList();
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
        return tweets;

    }
}
