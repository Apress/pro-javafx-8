import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
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

public class MeshViewExample extends Application {
    private Model model;
    private View view;

    public MeshViewExample() {
        model = new Model();
    }

    @Override
    public void start(Stage stage) throws Exception {
        view = new View(model);
        hookupEvents();
        stage.setTitle("MeshView Example");
        stage.setScene(view.scene);
        stage.show();
    }

    private void hookupEvents() {
        view.colorPicker.setOnAction(event -> {
            ColorPicker colorPicker = (ColorPicker) event.getSource();
            model.setMaterial(new PhongMaterial(colorPicker.getValue()));
        });

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
        private ObjectProperty<Material> material = new SimpleObjectProperty<>(
                this, "material", new PhongMaterial(Color.BLUE));
        private ObjectProperty<DrawMode> drawMode = new SimpleObjectProperty<>(
                this, "drawMode", DrawMode.FILL);
        private ObjectProperty<CullFace> cullFace = new SimpleObjectProperty<>(
                this, "cullFace", CullFace.BACK);

        public final double getRotate() {
            return rotate.get();
        }

        public final void setRotate(double rotate) {
            this.rotate.set(rotate);
        }

        public final DoubleProperty rotateProperty() {
            return rotate;
        }

        public final Material getMaterial() {
            return material.get();
        }

        public final void setMaterial(Material material) {
            this.material.set(material);
        }

        public final ObjectProperty<Material> materialProperty() {
            return material;
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

        public MeshView meshView;

        public ColorPicker colorPicker;
        public ComboBox<DrawMode> drawModeComboBox;
        public ComboBox<CullFace> cullFaceComboBox;
        public Slider rotateSlider;


        public View(Model model) {
            meshView = new MeshView(createSimplex(200.0f));
            meshView.materialProperty().bind(model.materialProperty());
            meshView.drawModeProperty().bind(model.drawModeProperty());
            meshView.cullFaceProperty().bind(model.cullFaceProperty());
            meshView.setRotationAxis(new Point3D(1, 1, 1));
            meshView.rotateProperty().bind(model.rotateProperty());

            colorPicker = new ColorPicker(Color.BLUE);

            drawModeComboBox = new ComboBox<>();
            drawModeComboBox.setItems(FXCollections.observableArrayList(
                    DrawMode.FILL, DrawMode.LINE));
            drawModeComboBox.setValue(DrawMode.FILL);

            cullFaceComboBox = new ComboBox<>();
            cullFaceComboBox.setItems(FXCollections.observableArrayList(
                    CullFace.BACK, CullFace.FRONT, CullFace.NONE));
            cullFaceComboBox.setValue(CullFace.BACK);

            HBox hbox1 = new HBox(10, new Label("Color:"), colorPicker,
                    new Label("DrawMode:"), drawModeComboBox,
                    new Label("CullFace:"), cullFaceComboBox);
            hbox1.setPadding(new Insets(10, 10, 10, 10));
            hbox1.setAlignment(Pos.CENTER);

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


            BorderPane root = new BorderPane(meshView, null, null, controlPanel, null);

            scene = new Scene(root, 640, 480);
        }

        private Mesh createSimplex(float length) {
            TriangleMesh mesh = new TriangleMesh();

            mesh.getPoints().addAll(
                    0.0f, 0.0f, 0.0f,   // O
                    length, 0.0f, 0.0f, // A
                    0.0f, length, 0.0f, // B
                    0.0f, 0.0f, length  // C
            );

            mesh.getTexCoords().addAll(
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f
            );

            mesh.getFaces().addAll(
                    0, 0, 2, 1, 1, 2, // OBA
                    0, 0, 3, 1, 2, 2, // OCB
                    0, 0, 1, 1, 3, 2, // OAC
                    1, 0, 2, 1, 3, 2  // ABC
            );

            mesh.getFaceSmoothingGroups().addAll(0, 0, 0, 0);

            return mesh;
        }
    }
}
