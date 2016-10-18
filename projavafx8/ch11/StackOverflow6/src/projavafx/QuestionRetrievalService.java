package projavafx;

import java.net.URL;
import java.util.zip.GZIPInputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
public class QuestionRetrievalService extends Service<ObservableList<Question>> {

    private String loc;

    public QuestionRetrievalService(String loc) {
        this.loc = loc;
    }

    @Override
    protected Task<ObservableList<Question>> createTask() {
        return new Task<ObservableList<Question>>() {

            @Override
            protected ObservableList<Question> call() throws Exception {
                System.out.println("QRS start");
                URL host = new URL(loc);
                JsonReader jr = Json.createReader(new GZIPInputStream(host.openConnection().getInputStream()));

                JsonObject jsonObject = jr.readObject();
                JsonArray jsonArray = jsonObject.getJsonArray("items");
                System.out.println("#elements = " + jsonArray.size());

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
        };
    }

}
