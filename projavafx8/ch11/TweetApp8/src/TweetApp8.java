
import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TweetApp8 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        primaryStage.setTitle("TweetList");
        ListView<Tweet> listView = new ListView<Tweet>();
        listView.setItems( getObservableList());
        listView.setCellFactory(new Callback<ListView<Tweet>, ListCell<Tweet>>() {

            @Override
            public ListCell<Tweet> call(ListView<Tweet> listview) {
                return new TweetCell();
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(listView);
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
        System.out.println("Setup complete");
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
