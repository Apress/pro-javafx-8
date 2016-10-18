package projavafx;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author johan
 */
public class StackOverflow2 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        ListView<Question> listView = new ListView<>();
        listView.setItems(getObservableList());
        listView.setCellFactory(l -> new QuestionCell());
        StackPane root = new StackPane();
        root.getChildren().add(listView);
        
        Scene scene = new Scene(root, 500, 300);
        
        primaryStage.setTitle("StackOverflow List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    ObservableList<Question> getObservableList() {
        ObservableList<Question> answer = FXCollections.observableArrayList();
        long now = System.currentTimeMillis();
        long yesterday = now - 1000*60*60*24;
        Question q1 = new Question("James", "How can I call a REST service?", now);
        Question q2 = new Question("Stephen", "Does JavaFX work on Android?", yesterday);
        answer.addAll(q1, q2);
        return answer;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
