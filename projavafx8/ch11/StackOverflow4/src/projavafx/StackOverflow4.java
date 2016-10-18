package projavafx;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class StackOverflow4 extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        ListView<Question> listView = new ListView<>();
        listView.setItems(getObservableList());
        listView.setCellFactory(l -> new QuestionCell());
        StackPane root = new StackPane();
        root.getChildren().add(listView);

        Scene scene = new Scene(root, 500, 300);

        primaryStage.setTitle("StackOverflow List");
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println ("Done with the setup");
    }

    ObservableList<Question> getObservableList() throws IOException {
        String url = "http://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=javafx&site=stackoverflow";      
        Service<ObservableList<Question>> service = new QuestionRetrievalService(url);


        ObservableList<Question> answer = FXCollections.observableArrayList();
        service.stateProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                System.out.println("State of service is "+service.getState());
                if (service.getState().equals(Worker.State.SUCCEEDED)) {
                    answer.addAll(service.getValue());
                }
            }
        });
        service.start();
        return answer;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
