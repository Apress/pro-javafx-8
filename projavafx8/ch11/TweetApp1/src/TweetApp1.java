import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TweetApp1 extends Application {

  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("TweetList");
    ListView<Tweet> listView = new ListView<Tweet>();
    listView.setItems(getObservableList());
    StackPane root = new StackPane();
    root.getChildren().add(listView);
    primaryStage.setScene(new Scene(root, 500, 300));
    primaryStage.show();
  }
  
  ObservableList<Tweet> getObservableList() {
    ObservableList<Tweet> answer = FXCollections.observableArrayList();
    Tweet t1 = new Tweet("JavaFXFan", "I love JavaFX!!", "today");
    Tweet t2 = new Tweet("JavaDeveloper", "Developing \"Hello World\" in JavaFX...", "yesterday");
    answer.addAll(t1,t2);
    return answer;
  }
}
