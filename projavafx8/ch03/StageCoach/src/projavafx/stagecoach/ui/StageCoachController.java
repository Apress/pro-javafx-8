package projavafx.stagecoach.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class StageCoachController {
    @FXML
    private Rectangle blue;

    @FXML
    private VBox contentBox;

    @FXML
    private Text textStageX;

    @FXML
    private Text textStageY;

    @FXML
    private Text textStageH;

    @FXML
    private Text textStageW;

    @FXML
    private Text textStageF;

    @FXML
    private CheckBox checkBoxResizable;

    @FXML
    private CheckBox checkBoxFullScreen;

    @FXML
    private HBox titleBox;

    @FXML
    private Label titleLabel;

    @FXML
    private TextField titleTextField;

    @FXML
    private Button toBackButton;

    @FXML
    private Button toFrontButton;

    @FXML
    private Button closeButton;

    private Stage stage;
    private StringProperty title = new SimpleStringProperty();
    private double dragAnchorX;
    private double dragAnchorY;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setupBinding(StageStyle stageStyle) {
        checkBoxResizable.setDisable(stageStyle == StageStyle.TRANSPARENT
            || stageStyle == StageStyle.UNDECORATED);
        textStageX.textProperty().bind(new SimpleStringProperty("x: ")
            .concat(stage.xProperty().asString()));
        textStageY.textProperty().bind(new SimpleStringProperty("y: ")
            .concat(stage.yProperty().asString()));
        textStageW.textProperty().bind(new SimpleStringProperty("width: ")
            .concat(stage.widthProperty().asString()));
        textStageH.textProperty().bind(new SimpleStringProperty("height: ")
            .concat(stage.heightProperty().asString()));
        textStageF.textProperty().bind(new SimpleStringProperty("focused: ")
            .concat(stage.focusedProperty().asString()));
        stage.setResizable(true);
        checkBoxResizable.selectedProperty()
            .bindBidirectional(stage.resizableProperty());
        checkBoxFullScreen.selectedProperty().addListener((ov, oldValue, newValue) ->
            stage.setFullScreen(checkBoxFullScreen.selectedProperty().getValue()));
        title.bind(titleTextField.textProperty());
        stage.titleProperty().bind(title);
        stage.initStyle(stageStyle);
    }

    @FXML
    public void toBackEventHandler(ActionEvent e) {
        stage.toBack();
    }

    @FXML
    public void toFrontEventHandler(ActionEvent e) {
        stage.toFront();
    }

    @FXML
    public void closeEventHandler(ActionEvent e) {
        stage.close();
    }


    @FXML
    public void mousePressedHandler(MouseEvent me) {
        dragAnchorX = me.getScreenX() - stage.getX();
        dragAnchorY = me.getScreenY() - stage.getY();
    }

    @FXML
    public void mouseDraggedHandler(MouseEvent me) {
        stage.setX(me.getScreenX() - dragAnchorX);
        stage.setY(me.getScreenY() - dragAnchorY);
    }
}
