import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class Predefined3DShapesExample extends Application {
    private Model model;
    private View view;

    public Predefined3DShapesExample() {
        model = new Model();
    }

    @Override
    public void start(Stage stage) throws Exception {
        view = new View(model);
        hookupEvents();
        stage.setTitle("Pre-defined 3D Shapes Example");
        stage.setScene(view.scene);
        stage.show();
    }

    private void hookupEvents() {
        view.drawModeComboBox.setOnAction(event -> {
            ComboBox<DrawMode> drawModeComboBox = (ComboBox<DrawMode>) event.getSource();
            model.setDrawMode(drawModeComboBox.getValue());
        });

        view.cullFaceComboBox.setOnAction(event -> {
            ComboBox<CullFace> cullFaceComboBox = (ComboBox<CullFace>) event.getSource();
            model.setCullFace(cullFaceComboBox.getValue());
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Model {
        private DoubleProperty rotate = new SimpleDoubleProperty(
                this, "rotate", 60.0d);
        private ObjectProperty<DrawMode> drawMode = new SimpleObjectProperty<>(
                this, "drawMode", DrawMode.FILL);
        private ObjectProperty<CullFace> cullFace = new SimpleObjectProperty<>(
                this, "cullFace", CullFace.BACK);


        public final double getRotate() {
            return rotate.doubleValue();
        }

        public final void setRotate(double rotate) {
            this.rotate.set(rotate);
        }

        public final DoubleProperty rotateProperty() {
            return rotate;
        }

        public final DrawMode getDrawMode() {
            return drawMode.getValue();
        }

        public final void setDrawMode(DrawMode drawMode) {
            this.drawMode.set(drawMode);
        }

        public final ObjectProperty<DrawMode> drawModeProperty() {
            return drawMode;
        }

        public final CullFace getCullFace() {
            return cullFace.get();
        }

        public final void setCullFace(CullFace cullFace) {
            this.cullFace.set(cullFace);
        }

        public final ObjectProperty<CullFace> cullFaceProperty() {
            return cullFace;
        }
    }

    private static class View {
        public Scene scene;

        public Sphere sphere;
        public Cylinder cylinder;
        public Box box;

        public ComboBox<DrawMode> drawModeComboBox;
        public ComboBox<CullFace> cullFaceComboBox;
        public Slider rotateSlider;

        public View(Model model) {
            sphere = new Sphere(50);
            cylinder = new Cylinder(50, 100);
            box = new Box(100, 100, 100);

            sphere.setTranslateX(-200);
            cylinder.setTranslateX(0);
            box.setTranslateX(200);

            sphere.setMaterial(new PhongMaterial(Color.RED));
            cylinder.setMaterial(new PhongMaterial(Color.BLUE));
            box.setMaterial(new PhongMaterial(Color.GREEN));

            setupShape3D(sphere, model);
            setupShape3D(cylinder, model);
            setupShape3D(box, model);

            Group shapesGroup = new Group(sphere, cylinder, box);

            drawModeComboBox = new ComboBox<>();
            drawModeComboBox.setItems(FXCollections.observableArrayList(
                    DrawMode.FILL, DrawMode.LINE));
            drawModeComboBox.setValue(DrawMode.FILL);

            cullFaceComboBox = new ComboBox<>();
            cullFaceComboBox.setItems(FXCollections.observableArrayList(
                    CullFace.BACK, CullFace.FRONT, CullFace.NONE));
            cullFaceComboBox.setValue(CullFace.BACK);

            HBox hbox1 = new HBox(10, new Label("DrawMode:"), drawModeComboBox,
                    new Label("CullFace:"), cullFaceComboBox);
            hbox1.setPadding(new Insets(10, 10, 10, 10));
            hbox1.setAlignment(Pos.CENTER_LEFT);

            rotateSlider = new Slider(-180.0d, 180.0d, 60.0d);
            rotateSlider.setMinWidth(400.0d);
            rotateSlider.setMajorTickUnit(10.0d);
            rotateSlider.setMinorTickCount(5);
            rotateSlider.setShowTickMarks(true);
            rotateSlider.setShowTickLabels(true);

            rotateSlider.valueProperty().bindBidirectional(model.rotateProperty());

            HBox hbox2 = new HBox(10, new Label("Rotate Around (1, 1, 1) Axis:"),
                    rotateSlider);
            hbox2.setPadding(new Insets(10, 10, 10, 10));
            hbox2.setAlignment(Pos.CENTER_LEFT);

            VBox controlPanel = new VBox(10, hbox1, hbox2);
            controlPanel.setPadding(new Insets(10, 10, 10, 10));

            BorderPane root = new BorderPane(shapesGroup, null, null, controlPanel, null);
            scene = new Scene(root, 640, 480);
        }

        private void setupShape3D(Shape3D shape3D, Model model) {
            shape3D.setTranslateY(240.0d);
            shape3D.setRotationAxis(new Point3D(1.0d, 1.0d, 1.0d));

            shape3D.drawModeProperty().bind(model.drawModeProperty());
            shape3D.cullFaceProperty().bind(model.cullFaceProperty());
            shape3D.rotateProperty().bind(model.rotateProperty());
        }
    }
}
