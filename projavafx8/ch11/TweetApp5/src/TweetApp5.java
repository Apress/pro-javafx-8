
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TweetApp5 extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException, SAXException, ParserConfigurationException {
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

  ObservableList<Tweet> getObservableList() throws IOException, ParserConfigurationException, SAXException {
    ObservableList<Tweet> answer = FXCollections.observableArrayList();
    String url = "http://search.twitter.com/search.rss?q=javafx";

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(url);
    NodeList tweetNodes = doc.getElementsByTagName("item");
    int count = tweetNodes.getLength();
    for (int i = 0; i < count; i++) {
      Tweet tweet = new Tweet();
      Node tweetNode = tweetNodes.item(i);
      NodeList childNodes = tweetNode.getChildNodes();
      int cnt2 = childNodes.getLength();
      for (int j = 0; j < cnt2; j++) {
        Node me = childNodes.item(j);
        String nodeName = me.getNodeName();
        if ("pubDate".equals(nodeName)) {
          tweet.setTimeStamp(me.getTextContent());
        }
        if ("author".equals(nodeName)) {
          tweet.setAuthor(me.getTextContent());
        }
        if ("title".equals(nodeName)) {
          tweet.setTitle(me.getTextContent());
        }
      }
      answer.add(tweet);
    }
    return answer;
  }
}
