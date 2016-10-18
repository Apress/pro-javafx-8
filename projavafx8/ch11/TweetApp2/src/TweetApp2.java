import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TweetApp2 extends Application {

  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("TweetList");
    ListView<Tweet> listView = new ListView<Tweet>();
    listView.setItems(getObservableList());
    listView.setCellFactory(new Callback<ListView<Tweet>, ListCell<Tweet>>() {
      
      @Override
      public ListCell<Tweet> call(ListView<Tweet> listview) {
        return new TweetCell() ;
      }
    });
    
    StackPane root = new StackPane();
    root.getChildren().add(listView);
    primaryStage.setScene(new Scene(root, 300, 250));
    primaryStage.show();
  }
  
  ObservableList<Tweet> getObservableList() {
    ObservableList<Tweet> answer = FXCollections.observableArrayList();
    Tweet t1 = new Tweet("johan", "some first tweet", "today");
    Tweet t2 = new Tweet("jim", "my second tweet", "yesterday");
    answer.addAll(t1,t2);
    return answer;
  }
}
