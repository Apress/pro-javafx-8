
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class TweetApp3 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
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
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    ObservableList<Tweet> getObservableList() throws IOException {
        String url = "http://search.twitter.com/search.json?q=javafx";
        JsonFactory f = new JsonFactory();
        JsonParser jp = f.createJsonParser(new URL(url));
        ObservableList<Tweet> answer = FXCollections.observableArrayList();

        JsonToken token = jp.nextToken();
        while (token != JsonToken.START_ARRAY) {
            token = jp.nextToken();
        }
        while (token != JsonToken.END_ARRAY) {
            token = jp.nextToken();
            if (token == JsonToken.START_OBJECT) {
                Tweet tweet = parseTweet(jp);
                answer.add(tweet);
            }
        }
        return answer;
    }

    private Tweet parseTweet(JsonParser jp) throws IOException {
        Tweet tweet = new Tweet();
        JsonToken token = jp.nextToken();

        while (token != JsonToken.END_OBJECT) {
            if (token == JsonToken.START_OBJECT) {
                while (token != JsonToken.END_OBJECT) {
                    token = jp.nextToken();
                }
            }
            if (token == JsonToken.FIELD_NAME) {
                String fieldname = jp.getCurrentName();

                if ("created_at".equals(fieldname)) {
                    jp.nextToken();
                    tweet.setTimeStamp(jp.getText());
                }
                if ("from_user".equals(fieldname)) {
                    jp.nextToken();
                    tweet.setAuthor(jp.getText());
                }
                if ("text".equals(fieldname)) {
                    jp.nextToken();
                    tweet.setTitle(jp.getText());
                }
            }
            token = jp.nextToken();
        }
        return tweet;
    }
}
