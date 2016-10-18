package projavafx;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import javafx.application.Application;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author johan
 */
public class StackOverflowXML extends Application {
   
    @Override
    public void start(Stage primaryStage) throws IOException, ParserConfigurationException, SAXException {
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

    ObservableList<Question> getObservableList() throws IOException, ParserConfigurationException, SAXException {
        ObservableList<Question> answer = FXCollections.observableArrayList();
        InputStream inputStream = this.getClass().getResourceAsStream("/stackoverflow.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(inputStream);
        NodeList questionNodes = doc.getElementsByTagName("item");
        int count = questionNodes.getLength();
        for (int i = 0; i < count; i++) {
            Question question = new Question();
            Element questionNode = (Element) questionNodes.item(i);
           
            NodeList childNodes = questionNode.getChildNodes();
            int cnt2 = childNodes.getLength();
            for (int j = 0; j < cnt2; j++) {
                Node me = childNodes.item(j);
                String nodeName = me.getNodeName();
                if ("creation_date".equals(nodeName)) {
                    question.setTimestamp(Long.parseLong(me.getTextContent()));
                }
                if ("owner".equals(nodeName)) {
                    question.setOwner(me.getTextContent());
                }
                if ("title".equals(nodeName)) {
                    question.setQuestion(me.getTextContent());
                }
            }
            answer.add(question);
        }
        return answer;
      
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
