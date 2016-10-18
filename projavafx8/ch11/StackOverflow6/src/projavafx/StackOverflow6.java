
package projavafx;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author johan
 */
public class StackOverflow6 extends Application {

    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");

    @Override
    public void start(Stage primaryStage) throws IOException {
        TableView<Question> tableView = new TableView<>();
        tableView.setItems(getObservableList());
        TableColumn<Question, String> dateColumn = new TableColumn<>("Date");
        TableColumn<Question, String> ownerColumn = new TableColumn<>("Owner");
        TableColumn<Question, String> questionColumn = new TableColumn<>("Question");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("timestampString"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));       

        questionColumn.setPrefWidth(350);
        tableView.getColumns().addAll(dateColumn, ownerColumn, questionColumn);
        StackPane root = new StackPane();
        root.getChildren().add(tableView);

        Scene scene = new Scene(root, 500, 300);

        primaryStage.setTitle("StackOverflow Table");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    ObservableList<Question> getObservableList() throws IOException {
        String url = "http://api.stackexchange.com/2.2/search?order=desc&sort=activity&tagged=javafx&site=stackoverflow";
        Service<ObservableList<Question>> service = new QuestionRetrievalService(url);

        ObservableList<Question> answer = FXCollections.observableArrayList();
        service.stateProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable observable) {
                System.out.println("value is now " + service.getState());
                if (service.getState().equals(Worker.State.SUCCEEDED)) {
                    answer.addAll(service.getValue());
                }
            }
        });
        System.out.println("START SERVICE = " + service.getTitle());
        service.start();
        return answer;
    }

    private String getTimeStampString(long ts) {
        return sdf.format(new Date(ts));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
