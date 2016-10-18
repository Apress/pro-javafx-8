package projavafx;

import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

/**
 *
 * @author johan
 */
public class StackOverflow3 extends Application {

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
    }

    ObservableList<Question> getObservableList() throws IOException {
        String url = "http://api.stackexchange.com/2.2/search?tagged=javafx&site=stackoverflow";
        URL host = new URL(url);
        JsonReader jr = Json.createReader(new GZIPInputStream(host.openConnection().getInputStream()));

        JsonObject jsonObject = jr.readObject();
        JsonArray jsonArray = jsonObject.getJsonArray("items");
        ObservableList<Question> answer = FXCollections.observableArrayList();

        jsonArray.iterator().forEachRemaining((JsonValue e) -> {
            JsonObject obj = (JsonObject) e;
            JsonString name = obj.getJsonObject("owner").getJsonString("display_name");
            JsonString quest = obj.getJsonString("title");
            JsonNumber jsonNumber = obj.getJsonNumber("creation_date");
            Question q = new Question(name.getString(), quest.getString(), jsonNumber.longValue() * 1000);
            answer.add(q);
        });
        return answer;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
