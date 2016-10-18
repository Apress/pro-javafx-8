import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;

public class FXMLLoaderExampleController {
    @FXML
    private TextField address;

    @FXML
    private WebView webView;

    @FXML
    private Button loadButton;

    private String name;

    public FXMLLoaderExampleController(String name) {
        this.name = name;
    }

    @FXML
    public void actionHandler() {
        webView.getEngine().load(address.getText());
    }
}
