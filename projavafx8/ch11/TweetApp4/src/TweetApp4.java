
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.jackson.map.ObjectMapper;

public class TweetApp4 extends Application {

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
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    ObservableList<Tweet> getObservableList() throws IOException {
        ObservableList<Tweet> answer = FXCollections.observableArrayList();
        String url = "http://search.twitter.com/search.json?q=javafx";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> rawMap = mapper.readValue(new URL(url), Map.class);
        List<Map<String, Object>> results = (List) rawMap.get("results");
        for (Map<String, Object> entry : results) {
            Tweet tweet = new Tweet();
            tweet.setTimeStamp((String) entry.get("created_at"));
            tweet.setAuthor((String) entry.get("from_user"));
            tweet.setTitle((String) entry.get("text"));
            answer.add(tweet);
        }
        return answer;
    }
}
