package projavafx.datafxstackoverflow;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.datafx.provider.ListDataProvider;
import org.datafx.provider.ListDataProviderBuilder;
import org.datafx.reader.RestSource;
import org.datafx.reader.RestSourceBuilder;
import org.datafx.reader.converter.InputStreamConverter;
import org.datafx.reader.converter.JsonConverter;

/**
 *
 * @author johan
 */
public class StackOverflowDataFX extends Application {
    
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
        InputStreamConverter converter = new JsonConverter("item", Question.class);
        
        RestSource restSource = RestSourceBuilder.create()
                .converter(converter)
                .host("http://api.stackexchange.com")
                .path("2.2").path("search")
                .queryParam("order", "desc")
                .queryParam("sort", "activity")
                .queryParam("tagged", "javafx")
                .queryParam("site", "stackoverflow").build();
        ListDataProvider<Question> ldp = ListDataProviderBuilder.create()
                .dataReader(restSource)
                .build();
        Worker<ObservableList<Question>> retrieve = ldp.retrieve();
        return retrieve.getValue();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
